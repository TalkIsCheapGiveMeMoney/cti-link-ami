/*
 * Copyright 2004-2006 Stefan Reuter
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.Extension;
import org.asteriskjava.live.HangupCause;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.AbstractChannelEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DtmfEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.ParkedCallEvent;
import org.asteriskjava.manager.event.ParkedCallGiveUpEvent;
import org.asteriskjava.manager.event.ParkedCallTimeOutEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.event.UnparkedCallEvent;
import org.asteriskjava.manager.event.VarSetEvent;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinet.ctilink.ami.AmiEventListener;
import com.tinet.ctilink.ami.inc.AmiChanVarNameConst;
import com.tinet.ctilink.ami.inc.AmiChannelStatusConst;
import com.tinet.ctilink.ami.inc.AmiEventTypeConst;
import com.tinet.ctilink.ami.inc.AmiParamConst;
import com.tinet.ctilink.ami.util.AmiUtil;
import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.EnterpriseSetting;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.json.JSONObject;
import com.tinet.ctilink.util.ContextUtil;

import sun.misc.BASE64Decoder;





/**
 * Manages channel events on behalf of an AsteriskServer.
 *
 * @author srt
 * @version $Id: ChannelManager.java 1381 2009-10-19 19:48:15Z srt $
 */

public class ChannelManager  {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	 
   private RedisService redisService;
	   
   private AmiEventListener amiEventListener;

	/**
	 * How long we wait before we remove hung up channels from memory (in
	 * milliseconds).
	 */
	private static final long REMOVAL_THRESHOLD = 15 * 60 * 1000L; // 15 minutes

	private final AsteriskServerImpl server;
	
//	private final AmiServerImpl observer;

	/**
	 * A map of all active channel by their unique id.
	 */
	private final Set<AsteriskChannelImpl> channels;

	/**
	 * Creates a new instance.
	 *
	 * @param server the server this channel manager belongs to.
	 */
	ChannelManager(AsteriskServerImpl server) {
		this.server = server;
		this.channels = new HashSet<AsteriskChannelImpl>();

		this.redisService = ContextUtil.getBean(RedisService.class);
		this.amiEventListener = ContextUtil.getBean(AmiEventListener.class);
	}

	void initialize() throws ManagerCommunicationException {
		initialize(null);
	}

	void initialize(List<String> variables) throws ManagerCommunicationException {
		ResponseEvents re;

		disconnected();
		StatusAction sa = new StatusAction();
		sa.setVariables(variables);
		for (;;) {

			re = server.sendEventGeneratingAction(sa);
			if (re.getResponse() instanceof ManagerError) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}

		for (ManagerEvent event : re.getEvents()) {
			if (event instanceof StatusEvent) {
				handleStatusEvent((StatusEvent) event);
			}
		}
	}

	void disconnected() {
		synchronized (channels) {
			channels.clear();
		}
	}

	/**
	 * Returns a collection of all active AsteriskChannels.
	 *
	 * @return a collection of all active AsteriskChannels.
	 */
	Collection<AsteriskChannel> getChannels() {
		Collection<AsteriskChannel> copy;

		synchronized (channels) {
			copy = new ArrayList<AsteriskChannel>(channels.size() + 2);
			for (AsteriskChannel channel : channels) {
				if (channel.getState() != ChannelState.HUNGUP) {
					copy.add(channel);
				}
			}
		}
		return copy;
	}

	private void addChannel(AsteriskChannelImpl channel) {
		synchronized (channels) {
			channels.add(channel);
		}
	}

	/**
	 * Removes channels that have been hung more than {@link #REMOVAL_THRESHOLD}
	 * milliseconds.
	 */
	private void removeOldChannels() {
		Iterator<AsteriskChannelImpl> i;

		synchronized (channels) {
			i = channels.iterator();
			while (i.hasNext()) {
				final AsteriskChannel channel = i.next();
				final Date dateOfRemoval = channel.getDateOfRemoval();
				if (channel.getState() == ChannelState.HUNGUP && dateOfRemoval != null) {
					final long diff = DateUtil.getDate().getTime() - dateOfRemoval.getTime();
					if (diff >= REMOVAL_THRESHOLD) {
						i.remove();
					}
				}
			}
		}
	}

	private AsteriskChannelImpl addNewChannel(String uniqueId, String name, Date dateOfCreation, String callerIdNumber, String callerIdName, ChannelState state, String account) {
		final AsteriskChannelImpl channel;
		final String traceId;

		channel = new AsteriskChannelImpl(server, name, uniqueId, dateOfCreation);
		channel.setCallerId(new CallerId(callerIdName, callerIdNumber));
		channel.setAccount(account);
		channel.stateChanged(dateOfCreation, state);
		logger.info("Adding channel " + channel.getName() + "(" + channel.getId() + ")");

		traceId = getTraceId(channel);
		channel.setTraceId(traceId);

		addChannel(channel);

		if (traceId != null && (!name.toLowerCase(Locale.ENGLISH).startsWith("local/")
				|| (name.endsWith(",1") || name.endsWith(";1")))) {
			final OriginateCallbackData callbackData;
			callbackData = server.getOriginateCallbackDataByTraceId(traceId);
			if (callbackData != null && callbackData.getChannel() == null) {
				callbackData.setChannel(channel);
				try {
					callbackData.getCallback().onDialing(channel);
				} catch (Throwable t) {
					logger.warn("Exception dispatching originate progress.", t);
				}
			}
		}
		server.fireNewAsteriskChannel(channel);
		return channel;
	}

	void handleStatusEvent(StatusEvent event) {
		AsteriskChannelImpl channel;
		final Extension extension;
		boolean isNew = false;
		Map<String, String> variables = event.getVariables();

		channel = getChannelImplById(event.getUniqueId());
		if (channel == null) {
			Date dateOfCreation;

			if (event.getSeconds() != null) {
				dateOfCreation = new Date(DateUtil.getDate().getTime() - (event.getSeconds() * 1000L));
			} else {
				dateOfCreation = DateUtil.getDate();
			}
			channel = new AsteriskChannelImpl(server, event.getChannel(), event.getUniqueId(), dateOfCreation);
			isNew = true;
			if (variables != null) {
				for (String variable : variables.keySet()) {
					channel.updateVariable(variable, variables.get(variable));
				}
			}
		}

		if (event.getContext() == null && event.getExtension() == null && event.getPriority() == null) {
			extension = null;
		} else {
			extension = new Extension(event.getContext(), event.getExtension(), event.getPriority());
		}

		synchronized (channel) {
			channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
			channel.setAccount(event.getAccountCode());
			if (event.getChannelState() != null) {
				channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getChannelState()));
			}
			channel.extensionVisited(event.getDateReceived(), extension);

			if (event.getBridgedChannel() != null) {
				final AsteriskChannelImpl linkedChannel = getChannelImplByName(event.getBridgedChannel());
				if (linkedChannel != null) {
					// the date used here is not correct!
					channel.channelLinked(event.getDateReceived(), linkedChannel);
					synchronized (linkedChannel) {
						linkedChannel.channelLinked(event.getDateReceived(), channel);
					}
				}
			}
		}

		if (isNew) {
			logger.info("Adding new channel " + channel.getName());
			addChannel(channel);
			server.fireNewAsteriskChannel(channel);
		}
	}

	/**
	 * Returns a channel from the ChannelManager's cache with the given name If
	 * multiple channels are found, returns the most recently CREATED one. If
	 * two channels with the very same date exist, avoid HUNGUP ones.
	 *
	 * @param name the name of the requested channel.
	 * @return the (most recent) channel if found, in any state, or null if none
	 *         found.
	 */
	AsteriskChannelImpl getChannelImplByName(String name) {
		Date dateOfCreation = null;
		AsteriskChannelImpl channel = null;

		if (name == null) {
			return null;
		}

		synchronized (channels) {
			for (AsteriskChannelImpl tmp : channels) {
				if (tmp.getName() != null && tmp.getName().equals(name)) {
					// return the most recent channel or when dates are similar,
					// the active one
					if (dateOfCreation == null || tmp.getDateOfCreation().after(dateOfCreation)
							|| (tmp.getDateOfCreation().equals(dateOfCreation)
									&& tmp.getState() != ChannelState.HUNGUP)) {
						channel = tmp;
						dateOfCreation = channel.getDateOfCreation();
					}
				}
			}
		}
		return channel;
	}

	/**
	 * Returns a NON-HUNGUP channel from the ChannelManager's cache with the
	 * given name.
	 *
	 * @param name the name of the requested channel.
	 * @return the NON-HUNGUP channel if found, or null if none is found.
	 */
	AsteriskChannelImpl getChannelImplByNameAndActive(String name) {

		// In non bristuffed AST 1.2, we don't have uniqueid header to match the
		// channel
		// So we must use the channel name
		// Channel name is unique at any give moment in the * server
		// But asterisk-java keeps Hungup channels for a while.
		// We don't want to retrieve hungup channels.

		AsteriskChannelImpl channel = null;

		if (name == null) {
			return null;
		}

		synchronized (channels) {
			for (AsteriskChannelImpl tmp : channels) {
				if (tmp.getName() != null && tmp.getName().equals(name) && tmp.getState() != ChannelState.HUNGUP) {
					channel = tmp;
				}
			}
		}
		return channel;
	}

	AsteriskChannelImpl getChannelImplById(String id) {
		if (id == null) {
			return null;
		}

		synchronized (channels) {
			for (AsteriskChannelImpl channel : channels) {
				if (id.equals(channel.getId())) {
					return channel;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the other side of a local channel.
	 * <p/>
	 * Local channels consist of two sides, like "Local/1234@from-local-60b5,1"
	 * and "Local/1234@from-local-60b5,2" (for Asterisk 1.4) or
	 * "Local/1234@from-local-60b5;1" and "Local/1234@from-local-60b5;2" (for
	 * Asterisk 1.6) this method returns the other side.
	 *
	 * @param localChannel one side
	 * @return the other side, or <code>null</code> if not available or if the
	 *         given channel is not a local channel.
	 */
	AsteriskChannelImpl getOtherSideOfLocalChannel(AsteriskChannel localChannel) {
		final String name;
		final char num;

		if (localChannel == null) {
			return null;
		}

		name = localChannel.getName();
		if (name == null || !name.startsWith("Local/")
				|| (name.charAt(name.length() - 2) != ',' && name.charAt(name.length() - 2) != ';')) {
			return null;
		}

		num = name.charAt(name.length() - 1);

		if (num == '1') {
			return getChannelImplByName(name.substring(0, name.length() - 1) + "2");
		} else if (num == '2') {
			return getChannelImplByName(name.substring(0, name.length() - 1) + "1");
		} else {
			return null;
		}
	}

	public void handleNewChannelEvent(NewChannelEvent event) {
		AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

		if (channel == null) {
			if (event.getChannel() == null) {
				logger.info("Ignored NewChannelEvent with empty channel name (uniqueId=" + event.getUniqueId() + ")");
			} else {
				channel = addNewChannel(event.getUniqueId(), event.getChannel(), event.getDateReceived(), event.getCallerIdNum(),
						event.getCallerIdName(), ChannelState.valueOf(event.getChannelState()), event.getAccountCode());
			}
		} else {
			// channel had already been created probably by a NewCallerIdEvent
			synchronized (channel) {
				channel.nameChanged(event.getDateReceived(), event.getChannel());
				channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
				channel.stateChanged(event.getDateReceived(), ChannelState.valueOf(event.getChannelState()));
			}
		}
	}

	public void handleNewExtenEvent(NewExtenEvent event) {
		AsteriskChannelImpl channel;
		final Extension extension;

		channel = getChannelImplById(event.getUniqueId());
		if (channel == null) {
			logger.error("Ignored NewExtenEvent for unknown channel " + event.getChannel());
			return;
		}

		extension = new Extension(event.getContext(), event.getExtension(), event.getPriority(), event.getApplication(),
				event.getAppData());

		synchronized (channel) {
			channel.extensionVisited(event.getDateReceived(), extension);
		}
	}

	public void handleNewStateEvent(NewStateEvent event) {
		
		logger.info("The begin of handleNewStateEvent!" );
		String channelName = "";	
		String channelUniqueId = "";
			
		channelName = event.getChannel();	
		channelUniqueId = event.getUniqueId();
		AsteriskChannelImpl channel = getChannelImplById(channelUniqueId);
		if (channel == null) {
			// NewStateEvent can occur for an existing channel that now has a
			// different unique id (originate with Local/)
			channel = getChannelImplByNameAndActive(event.getChannel());
			if (channel != null) {
				logger.info("Changing unique id for '" + channel.getName() + "' from " + channel.getId() + " to "
						+ event.getUniqueId());
				channel.idChanged(event.getDateReceived(), event.getUniqueId());
			}

			if (channel == null) {
				logger.info("Creating new channel due to NewStateEvent '" + event.getChannel() + "' unique id "
						+ event.getUniqueId());
				// NewStateEvent can occur instead of a NewChannelEvent
				channel = addNewChannel(event.getUniqueId(), event.getChannel(), event.getDateReceived(),
						event.getCallerIdNum(), event.getCallerIdName(), ChannelState.valueOf(event.getChannelState()),
						null /* account code not available */);
			}			
		}
		
		
		// NewStateEvent can provide a new CallerIdNum or CallerIdName not
		// previously received through a
		// NewCallerIdEvent. This happens at least on outgoing legs from the
		// queue application to agents.
		if (event.getCallerIdNum() != null || event.getCallerIdName() != null) {
			String cidnum = "";
			String cidname = "";
			CallerId currentCallerId = channel.getCallerId();

			if (currentCallerId != null) {
				cidnum = currentCallerId.getNumber();
				cidname = currentCallerId.getName();
			}

			if (event.getCallerIdNum() != null) {
				cidnum = event.getCallerIdNum();
			}

			if (event.getCallerIdName() != null) {
				cidname = event.getCallerIdName();
			}

			CallerId newCallerId = new CallerId(cidname, cidnum);
			logger.debug("Updating CallerId (following NewStateEvent) to: " + newCallerId.toString());
			channel.setCallerId(newCallerId);

			// Also, NewStateEvent can return a new channel name for the same
			// channel uniqueid, indicating the channel has been
			// renamed but no related RenameEvent has been received.
			// This happens with mISDN channels (see AJ-153)
			if (event.getChannel() != null && !event.getChannel().equals(channel.getName())) {
				logger.info("Renaming channel (following NewStateEvent) '" + channel.getName() + "' to '"
						+ event.getChannel() + "'");
				synchronized (channel) {
					channel.nameChanged(event.getDateReceived(), event.getChannel());
				}
			}
		}

		String channelCno = ((AbstractChannelEvent) event).getChanVarialbe("channel_cno") ;
		String enterpriseId = ((AbstractChannelEvent) event).getChanVarialbe("enterprise_id") ;
		if(StringUtils.isNotEmpty(channelCno))
		{			
			if (event.getChannelState() != null) {
				if (!channel.getState().equals(ChannelState.valueOf(event.getChannelState()))) {
					
				}
			}
			
			JSONObject j=new JSONObject();
			j.put(AmiParamConst.ENTERPRISEID, enterpriseId);
			j.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.STATUS);	
			j.put(AmiParamConst.VARIABLE_CNO, channelCno);	
			
			String callType = "";
			String channelState = "";
			channelState = AmiChannelStatusConst.TransformChannelState(event.getChannelState()).toString();	
			j.put(AmiParamConst.CHANNELSTATE, channelState);
			if(ChannelState.valueOf(event.getChannelState()) == ChannelState.RINGING)
			{
				try{
					callType = channel.getVariable(AmiChanVarNameConst.CDR_CALL_TYPE);
				
				}catch(org.asteriskjava.live.NoSuchChannelException e)
				{
					
				}						
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				//弹屏参数设置	
				EnterpriseSetting entSetting = redisService.get(Const.REDIS_DB_CONF_INDEX
						, String.format(CacheKey.ENTERPRISE_SETTING_ENTERPRISE_ID_NAME
						,Integer.parseInt(enterpriseId)
						, Const.ENTERPRISE_SETTING_NAME_CRM_URL_POPUP_USER_FIELD)
						, EnterpriseSetting.class);
				if (entSetting != null && entSetting.getId() != null) {
					if (StringUtils.isNotEmpty(entSetting.getProperty())) {
						JSONObject clientData = new JSONObject();
						String property[] = StringUtils.split(entSetting.getProperty(), ",");
						try{
							String channelMainChannel = channel.getVariable(AmiChanVarNameConst.MAIN_CHANNEL);
							AsteriskChannel mainChannel = null;
							if(StringUtils.isNotEmpty(channelMainChannel))
							{
								mainChannel = server.getChannelByName(channelMainChannel);
								try{
									for (String var : property) {
										clientData.put(var, mainChannel.getNoCacheVariable(var));
									}
								}catch(org.asteriskjava.live.NoSuchChannelException e)
								{
									
								}						
								catch(Exception e)
								{
									e.printStackTrace();
								}
								j.put(AmiParamConst.VARIABLE_STATUS_VARIABLES,clientData);
							}
						}catch(org.asteriskjava.live.NoSuchChannelException e)
						{
							
						}						
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			String channelCustomerNumber = "";
			String channelCustomerNumberType = "";
			String channelCustomerAreaCode = "";			
			String channelNumberTrunk = "";
			String channelCallType = "";					
			String bridgedUniqueId = "";					
			String cno = "";
			String queueName = "";
			String hotline = "";				
			String bridgedChannelName = "";
			String detailCallType = "";
			
			
			try{	
				callType = channel.getVariable(AmiChanVarNameConst.CDR_CALL_TYPE);				
				queueName = channel.getVariable(AmiChanVarNameConst.CUR_QUEUE);
				hotline = channel.getVariable(AmiChanVarNameConst.CDR_HOTLINE);						
				channelCustomerAreaCode = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_AREA_CODE);	
				channelNumberTrunk = channel.getVariable(AmiChanVarNameConst.CDR_NUMBER_TRUNK);
				channelCallType = channel.getVariable(AmiChanVarNameConst.CDR_CALL_TYPE);	
				detailCallType = channel.getVariable(AmiChanVarNameConst.CDR_DETAIL_CALL_TYPE);						
				bridgedChannelName = channel.getVariable(AmiChanVarNameConst.MAIN_CHANNEL);
				bridgedUniqueId = channel.getVariable(AmiChanVarNameConst.LINKEDID);
				channelCustomerNumber = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_NUMBER);
				channelCustomerNumberType = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_NUMBER_TYPE);		
			}catch(org.asteriskjava.live.NoSuchChannelException e){
				
			}						
			catch(Exception e){
				
			}
			
			j.put(AmiParamConst.CHANNEL, event.getChannel());
			j.put(AmiParamConst.UNIQUEID, channelUniqueId);
			j.put(AmiParamConst.CALL_TYPE, channelCallType);
			j.put(AmiParamConst.CUSTOMER_NUMBER, channelCustomerNumber);
			j.put(AmiParamConst.CUSTOMER_NUMBER_TYPE, channelCustomerNumberType);
			j.put(AmiParamConst.CUSTOMER_NUMBER_AREA_CODE, channelCustomerAreaCode);
			j.put(AmiParamConst.DETAIL_CALL_TYPE, detailCallType);
			j.put(AmiParamConst.VARIABLE_HOTLINE, hotline);
			j.put(AmiParamConst.VARIABLE_NUMBER_TRUNK, channelNumberTrunk);
			j.put(AmiParamConst.VARIABLE_QUEUE, queueName);	
			j.put(AmiParamConst.VARIABLE_BRIDGED_CHANNEL, bridgedChannelName);
			j.put(AmiParamConst.VARIABLE_BRIDGED_UNIQUEID, bridgedUniqueId);	
			amiEventListener.publishEvent(j);
			
			// 来电推送
			int pushType = 0;
			int curlType = 0;

			if ((Const.CDR_CALL_TYPE_OB_WEBCALL + "").equals(channelCallType)
					&& StringUtils.isEmpty(channelUniqueId)) {
				pushType = Const.ENTERPRISE_PUSH_TYPE_RINGING_WEB_CALL;
				curlType = Const.CURL_TYPE_RINGING_WEBCALL;
			} else {
				pushType = Const.ENTERPRISE_PUSH_TYPE_RINGING_IB;
				curlType = Const.CURL_TYPE_RINGING_IB;
			}

			Map<String, String> pushEvent = new HashMap<String, String>();
			pushEvent.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.RINGING);
			pushEvent.put(AmiParamConst.VARIABLE_ENTERPRISE_ID, String.valueOf(enterpriseId));
			pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER, channelCustomerNumber);
			pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_NUMBER_TYPE, channelCustomerNumberType);
			pushEvent.put(AmiParamConst.VARIABLE_CUSTOMER_AREA_CODE, channelCustomerAreaCode);
			pushEvent.put(AmiParamConst.VARIABLE_NUMBER_TRUNK, channelNumberTrunk);
			pushEvent.put(AmiParamConst.VARIABLE_CALL_TYPE, channelCallType);
			pushEvent.put(AmiParamConst.VARIABLE_RINGING_TIME, com.tinet.ctilink.util.DateUtil
					.format(new Date(), com.tinet.ctilink.util.DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss));
			pushEvent.put(AmiParamConst.VARIABLE_UNIQUEID, channelUniqueId);

			// 根据企业设置推送Curl
			AmiUtil.pushCurl(channel, pushEvent, Integer.parseInt(enterpriseId), pushType, curlType);
			
		}
		
		logger.info("The end of handleNewStateEvent!" );
	}

	/**
	 * 检查NewStateEvent是否是坐席事件
	 * @param cno
	 * @return
	 */
	private boolean checkWhetherAgentEvent(String cno) {
		return StringUtils.isNotEmpty(cno);
	}

	public void handleNewCallerIdEvent(NewCallerIdEvent event) {
		AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

		if (channel == null) {
			// NewCallerIdEvent can occur for an existing channel that now has a
			// different unique id (originate with Local/)
			channel = getChannelImplByNameAndActive(event.getChannel());
			if (channel != null) {
				logger.info("Changing unique id for '" + channel.getName() + "' from " + channel.getId() + " to "
						+ event.getUniqueId());
				channel.idChanged(event.getDateReceived(), event.getUniqueId());
			}

			if (channel == null) {
				// NewCallerIdEvent can occur before NewChannelEvent
				channel = addNewChannel(event.getUniqueId(), event.getChannel(), event.getDateReceived(),
						event.getCallerIdNum(), event.getCallerIdName(), ChannelState.DOWN,
						null /* account code not available */);
			}
		}

		synchronized (channel) {
			channel.setCallerId(new CallerId(event.getCallerIdName(), event.getCallerIdNum()));
			channel.setChannelType(0);
			String channelCustomerNumber = "";
			String channelCustomerNumberType = "";
			String channelCustomerAreaCode = "";
			String channelUniqueId = "";			
			String channelNumberTrunk = "";
			String channelCallType = "";					
			String bridgedUniqueId = "";					
			String cno = "";
			String queueName = "";
			String hotline = "";
			String bridgedChannelName = "";
			String detailCallType = "";
			try{
				cno = channel.getVariable(AmiChanVarNameConst.CDR_DETAIL_CNO);
			}catch(Exception e)
			{
				e.printStackTrace();
				return;				
			}
			if(checkWhetherAgentEvent(cno))
			{
				channel.setChannelType(1);
				JSONObject j=new JSONObject();
				String channelName = "";
				String enterpriseId = "";
				enterpriseId = channel.getVariable(AmiChanVarNameConst.ENTERPRISE_ID);
				j.put(AmiParamConst.ENTERPRISEID, enterpriseId);	
				channelName = event.getChannel();	
				channelUniqueId = event.getUniqueId();
				String channelState = ((Integer)AmiChannelStatusConst.TRYING).toString();
				try{					
					queueName = channel.getVariable(AmiChanVarNameConst.CUR_QUEUE);
					hotline = channel.getVariable(AmiChanVarNameConst.CDR_HOTLINE);						
					channelCustomerAreaCode = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_AREA_CODE);	
					channelNumberTrunk = channel.getVariable(AmiChanVarNameConst.CDR_NUMBER_TRUNK);
					channelCallType = channel.getVariable(AmiChanVarNameConst.CDR_CALL_TYPE);	
					detailCallType = channel.getVariable(AmiChanVarNameConst.CDR_DETAIL_CALL_TYPE);						
					bridgedChannelName = channel.getVariable(AmiChanVarNameConst.MAIN_CHANNEL);
					bridgedUniqueId = channel.getVariable(AmiChanVarNameConst.LINKEDID);
					channelCustomerNumber = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_NUMBER);
					channelCustomerNumberType = channel.getVariable(AmiChanVarNameConst.CDR_CUSTOMER_NUMBER_TYPE);		
				}catch(org.asteriskjava.live.NoSuchChannelException e)
				{
					e.printStackTrace();
					return;
				}						
				catch(Exception e)
				{
					e.printStackTrace();							
					return;
				}
				j.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.STATUS);	
				j.put(AmiParamConst.VARIABLE_CNO, cno);	
				j.put(AmiParamConst.CHANNELSTATE, channelState);
				j.put(AmiParamConst.CHANNEL, event.getChannel());
				j.put(AmiParamConst.UNIQUEID, channelUniqueId);
				j.put(AmiParamConst.CALL_TYPE, channelCallType);
				j.put(AmiParamConst.CUSTOMER_NUMBER, channelCustomerNumber);
				j.put(AmiParamConst.CUSTOMER_NUMBER_TYPE, channelCustomerNumberType);
				j.put(AmiParamConst.CUSTOMER_NUMBER_AREA_CODE, channelCustomerAreaCode);
				j.put(AmiParamConst.DETAIL_CALL_TYPE, detailCallType);
				j.put(AmiParamConst.VARIABLE_HOTLINE, hotline);
				j.put(AmiParamConst.VARIABLE_NUMBER_TRUNK, channelNumberTrunk);
				j.put(AmiParamConst.VARIABLE_QUEUE, queueName);	
				j.put(AmiParamConst.VARIABLE_BRIDGED_CHANNEL, bridgedChannelName);
				j.put(AmiParamConst.VARIABLE_BRIDGED_UNIQUEID, bridgedUniqueId);	
				amiEventListener.publishEvent(j);
			}
		
		}
	}

	public void handleHangupEvent(HangupEvent event) {
		
		logger.info(" The begining of  HangupEvent handler! " + event.getChannel());
		HangupCause cause = null;
		AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
		if (channel == null) {
			logger.error("Ignored HangupEvent for unknown channel " + event.getChannel());
			return;
		}
		if (event.getCause() != null) {
			cause = HangupCause.getByCode(event.getCause());
		}

		synchronized (channel) {
			channel.hungup(event.getDateReceived(), cause, event.getCauseTxt());
			String channelUniqueId = "";
			String channelCallType = "";					
			String cno = "";
			String channelState = "";
			String enterpriseId = "";
			
			int channelType = channel.getChannelType();
			if(channelType == 1 )
			{
				JSONObject j=new JSONObject();
				String channelName = "";
				channelState = ((Integer)AmiChannelStatusConst.IDLE).toString();
				channelName = event.getChannel();	
				channelUniqueId = event.getUniqueId();
				enterpriseId = channel.getVariable(AmiChanVarNameConst.ENTERPRISE_ID);
				cno = channel.getVariable(AmiChanVarNameConst.CDR_DETAIL_CNO);				
				j.put(AmiParamConst.VARIABLE_EVENT, AmiEventTypeConst.STATUS);	
				j.put(AmiParamConst.ENTERPRISEID, enterpriseId);	
				j.put(AmiParamConst.VARIABLE_CNO, cno);	
				j.put(AmiParamConst.CHANNELSTATE, channelState);
				j.put(AmiParamConst.CHANNEL, event.getChannel());
				j.put(AmiParamConst.UNIQUEID, channelUniqueId);				
	
				amiEventListener.publishEvent(j);
				
			}
		}

		logger.info("Removing channel " + channel.getName() + " due to hangup (" + cause + ")");
		removeOldChannels();
		
		logger.info(" The end of  HangupEvent handler! " + event.getChannel());
	}

	public void handleDialEvent(DialEvent event) {
		final AsteriskChannelImpl sourceChannel = getChannelImplById(event.getUniqueId());
		final AsteriskChannelImpl destinationChannel = getChannelImplById(event.getDestUniqueId());

		if (sourceChannel == null) {
			logger.info("Ignored DialEvent for unknown source channel " + event.getChannel() + " with unique id "
					+ event.getUniqueId());
			return;
		}
		if (destinationChannel == null) {
			logger.info("Ignored DialEvent for unknown destination channel " + event.getDestination()
					+ " with unique id " + event.getDestUniqueId());
			return;
		}

		logger.info(sourceChannel.getName() + " dialed " + destinationChannel.getName());
		getTraceId(sourceChannel);
		getTraceId(destinationChannel);
		synchronized (sourceChannel) {
			sourceChannel.channelDialed(event.getDateReceived(), destinationChannel);
		}
		synchronized (destinationChannel) {
			destinationChannel.channelDialing(event.getDateReceived(), sourceChannel);
		}
	}

	public void handleBridgeEvent(BridgeEvent event) {
		final AsteriskChannelImpl channel1 = getChannelImplById(event.getUniqueId1());
		final AsteriskChannelImpl channel2 = getChannelImplById(event.getUniqueId2());
		if (channel1 == null) {
			logger.error("Ignored BridgeEvent for unknown channel " + event.getChannel1());
			return;
		}
		if (channel2 == null) {
			logger.error("Ignored BridgeEvent for unknown channel " + event.getChannel2());
			return;
		}

		if (event.isLink()) {
			logger.info("Linking channels " + channel1.getName() + " and " + channel2.getName());
			synchronized (channel1) {
				channel1.channelLinked(event.getDateReceived(), channel2);
			}

			synchronized (channel2) {
				channel2.channelLinked(event.getDateReceived(), channel1);
			}
		}

		if (event.isUnlink()) {
			logger.info("Unlinking channels " + channel1.getName() + " and " + channel2.getName());
			synchronized (channel1) {
				channel1.channelUnlinked(event.getDateReceived());
			}

			synchronized (channel2) {
				channel2.channelUnlinked(event.getDateReceived());
			}
		}
	}

	public void handleRenameEvent(RenameEvent event) {
		AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());

		if (channel == null) {
			logger.error("Ignored RenameEvent for unknown channel with uniqueId " + event.getUniqueId());
			return;
		}

		logger.info("Renaming channel '" + channel.getName() + "' to '" + event.getNewname() + "', uniqueId is "
				+ event.getUniqueId());
		synchronized (channel) {
			channel.nameChanged(event.getDateReceived(), event.getNewname());
		}
	}

	public void handleCdrEvent(CdrEvent event) {
		final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
		final AsteriskChannelImpl destinationChannel = getChannelImplByName(event.getDestinationChannel());
		final CallDetailRecordImpl cdr;

		if (channel == null) {
			logger.info("Ignored CdrEvent for unknown channel with uniqueId " + event.getUniqueId());
			return;
		}

		cdr = new CallDetailRecordImpl(channel, destinationChannel, event);

		synchronized (channel) {
			channel.callDetailRecordReceived(event.getDateReceived(), cdr);
		}
	}

	private String getTraceId(AsteriskChannel channel) {
		String traceId;

		try {
			traceId = channel.getVariable(Constants.VARIABLE_TRACE_ID);
		} catch (Exception e) {
			traceId = null;
		}
		// logger.info("TraceId for channel " + channel.getName() + " is " +
		// traceId);
		return traceId;
	}

	void handleParkedCallEvent(ParkedCallEvent event) {
		// Only bristuffed versions: AsteriskChannelImpl channel =
		// getChannelImplById(event.getUniqueId());
		AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());

		if (channel == null) {
			logger.info("Ignored ParkedCallEvent for unknown channel " + event.getChannel());
			return;
		}

		synchronized (channel) {
			// todo The context should be "parkedcalls" or whatever has been
			// configured in features.conf
			// unfortunately we don't get the context in the ParkedCallEvent so
			// for now we'll set it to null.
			Extension ext = new Extension(null, event.getExten(), 1);
			channel.setParkedAt(ext);
			logger.info("Channel " + channel.getName() + " is parked at " + channel.getParkedAt().getExtension());
		}
	}

	void handleParkedCallGiveUpEvent(ParkedCallGiveUpEvent event) {
		// Only bristuffed versions: AsteriskChannelImpl channel =
		// getChannelImplById(event.getUniqueId());
		AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());

		if (channel == null) {
			logger.info("Ignored ParkedCallGiveUpEvent for unknown channel " + event.getChannel());
			return;
		}

		Extension wasParkedAt = channel.getParkedAt();

		if (wasParkedAt == null) {
			logger.info("Ignored ParkedCallGiveUpEvent as the channel was not parked");
			return;
		}

		synchronized (channel) {
			channel.setParkedAt(null);
		}
		logger.info("Channel " + channel.getName() + " is unparked (GiveUp) from " + wasParkedAt.getExtension());
	}

	void handleParkedCallTimeOutEvent(ParkedCallTimeOutEvent event) {
		// Only bristuffed versions: AsteriskChannelImpl channel =
		// getChannelImplById(event.getUniqueId());
		final AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());

		if (channel == null) {
			logger.info("Ignored ParkedCallTimeOutEvent for unknown channel " + event.getChannel());
			return;
		}

		Extension wasParkedAt = channel.getParkedAt();

		if (wasParkedAt == null) {
			logger.info("Ignored ParkedCallTimeOutEvent as the channel was not parked");
			return;
		}

		synchronized (channel) {
			channel.setParkedAt(null);
		}
		logger.info("Channel " + channel.getName() + " is unparked (Timeout) from " + wasParkedAt.getExtension());
	}

	void handleUnparkedCallEvent(UnparkedCallEvent event) {
		// Only bristuffed versions: AsteriskChannelImpl channel =
		// getChannelImplById(event.getUniqueId());
		final AsteriskChannelImpl channel = getChannelImplByNameAndActive(event.getChannel());

		if (channel == null) {
			logger.info("Ignored UnparkedCallEvent for unknown channel " + event.getChannel());
			return;
		}

		Extension wasParkedAt = channel.getParkedAt();

		if (wasParkedAt == null) {
			logger.info("Ignored UnparkedCallEvent as the channel was not parked");
			return;
		}

		synchronized (channel) {
			channel.setParkedAt(null);
		}
		logger.info("Channel " + channel.getName() + " is unparked (moved away) from " + wasParkedAt.getExtension());
	}

	public void handleVarSetEvent(VarSetEvent event) {
		if (event.getUniqueId() == null) {
			return;
		}

		final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
		if (channel == null) {
			logger.info("Ignored VarSetEvent for unknown channel with uniqueId " + event.getUniqueId());
			return;
		}

		synchronized (channel) {
			channel.updateVariable(event.getVariable(), event.getValue());
		}
	}

	public void handleDtmfEvent(DtmfEvent event) {
		// we are only intrested in END events
		if (event.isBegin()) {
			return;
		}

		if (event.getUniqueId() == null) {
			return;
		}

		final AsteriskChannelImpl channel = getChannelImplById(event.getUniqueId());
		if (channel == null) {
			logger.info("Ignored DtmfEvent for unknown channel with uniqueId " + event.getUniqueId());
			return;
		}

		final Character dtmfDigit;
		if (event.getDigit() == null || event.getDigit().length() < 1) {
			dtmfDigit = null;
		} else {
			dtmfDigit = event.getDigit().charAt(0);
		}

		synchronized (channel) {
			if (event.isReceived()) {
				channel.dtmfReceived(dtmfDigit);
			}
			if (event.isSent()) {
				channel.dtmfSent(dtmfDigit);
			}
		}
	}
	
	
public static Map<String,String> buildParams(String url, String[] paramName, String[] paramVariable, HashMap<String, String> map, Object channel){
		Map<String,String> params = new HashMap();
		BASE64Decoder base64Decoder = new BASE64Decoder();
//		AsteriskChannelImpl channel
		
		for(int i=0; i< paramName.length; i++){
			try{
				String param = new String(base64Decoder.decodeBuffer(paramName[i]));
                String variable = new String(base64Decoder.decodeBuffer(paramVariable[i]));
//                String param = new String(base64Decoder.decodeBuffer(paramName[i]));
//                String variable = new String(base64Decoder.decodeBuffer(paramVariable[i]));
                String value = null;
                if(channel != null){//使用channel获取变量
	                int count = 0;
	                while(variable.contains("${") && variable.contains("}") && count < 20){
	                    String var = variable.substring(variable.indexOf("${")+2, variable.indexOf("}"));
	                    String varval="";
	                    //
//	                    if(channel instanceof AsteriskChannel){
//	                    	varval = ((AsteriskChannel)channel).getVariable(var);
//	            		}else 
	            		if(channel instanceof AsteriskChannel){
	            			varval = ((AsteriskChannel)channel).getVariable(var);
	            		}
	                    variable = variable.substring(0, variable.indexOf("${")) + varval + variable.substring(variable.indexOf("}")+1);
	                    count++;
	                }
	                if(map.get(variable) != null){
	                	value = map.get(variable);
	                }else{
//		                if(channel instanceof AgiChannel){
//		                	value = ((AgiChannel)channel).getVariable(variable);
//		        		}else if(channel instanceof AsteriskChannel){
//		        			value = ((AsteriskChannel)channel).getVariable(variable);
//		        		}
	                }
                }else{//只使用map中变量
                	if(map.get(variable) != null){
	                	value = map.get(variable);
	                }
                	if(param.equals(Const.ENTERPRISE_PUSH_TYPE_TEL_STATUS_SIGN_NAME)){
                		param = variable;
                	}
                	if(param.equals(Const.ENTERPRISE_PUSH_TYPE_TEL_STATUS_SIGN__KEY_NAME) 
                			|| param.equals(Const.ENTERPRISE_PUSH_TYPE_TEL_STATUS_SIGN__KEY_VALUE)){
                		continue;
                	}
                }
    			if(value == null){
    				value = "";
    			}
//				BasicNameValuePair nv = new BasicNameValuePair(param, value);
//				params.add(nv);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return params;
	}
	
	
}

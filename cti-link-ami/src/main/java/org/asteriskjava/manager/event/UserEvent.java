/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.manager.event;

import java.util.Map;

import org.asteriskjava.live.AsteriskChannel;

/**
 * Abstract base class for user events.
 * <p>
 * You can send arbitrary user events via the UserEvent application provided
 * with asterisk. A user event by default has the attributes channel and
 * uniqueId but you can add custom attributes by specifying an event body.
 * <p>
 * To add your own user events you must subclass this class and name it
 * corresponding to your event. If you plan to send an event by
 * <code>UserEvent(VIPCall)</code> you will create a new class called
 * VIPCallEvent that extends UserEvent. The name of this class is important:
 * Just use the name of the event you will send (VIPCall in this example) and
 * append "Event".
 * <p>
 * To pass additional data create appropriate attributes with getter and setter
 * methods in your new class.
 * <p>
 * Example:
 * 
 * <pre>
 * public class VIPCallEvent extends UserEvent {
 * 	private String firstName;
 * 
 * 	public VIPCallEvent(Object source) {
 * 		super(source);
 * 	}
 * 
 * 	public String getFirstName() {
 * 		return firstName;
 * 	}
 * 
 * 	public void setFirstName(String firstName) {
 * 		this.firstName = firstName;
 * 	}
 * }
 * </pre>
 * 
 * To send this event use <code>UserEvent(VIPCall|firstName: Jon)</code> in your
 * dialplan. Asterisk up to 1.2 (including) does only support one property in
 * the UserEvent so something like
 * <code>UserEvent(VIPCall|firstName: Jon|lastName: Doe)</code> will not work as
 * expected.
 * <p>
 * The UserEvent is implemented in <code>apps/app_userevent.c</code>.
 * <p>
 * Note that you must register your UserEvent with the ManagerConnection you are
 * using in order to be recognized.
 * 
 * @see org.asteriskjava.manager.ManagerConnection#registerUserEventClass(Class)
 * 
 * @author srt
 * @version $Id: UserEvent.java 938 2007-12-31 03:23:38Z srt $
 */
public abstract class UserEvent extends ManagerEvent {
	/**
	 * Serial version identifier
	 */
	private static final long serialVersionUID = 3256725065466000695L;

	/**
	 * The unique id of the channel.
	 */
	private String uniqueId;
	
	/**
	 * The name of the channel.
	 */
	private String channel;
	
	private AsteriskChannel asteriskChannel;
	
	private String enterpriseId;
	
	private Map<String, String> chanVariables;
	
	public UserEvent(Object source) {
		super(source);
	}

	/**
	 * Returns the unqiue id of the channel this event occured in.
	 * 
	 * @return the unqiue id of the channel this event occured in.
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets the unqiue id of the channel this event occured in.
	 * 
	 * @param uniqueId
	 *            the unqiue id of the channel this event occured in.
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	/**
	 * Returns the name of the channel this event occured in.
	 * 
	 * @return the name of the channel this event occured in.
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * Sets the name of the channel this event occured in.
	 * 
	 * @param channel
	 *            the name of the channel this event occured in.
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public AsteriskChannel getAsteriskChannel() {
		return asteriskChannel;
	}

	public void setAsteriskChannel(AsteriskChannel asteriskChannel) {
		this.asteriskChannel = asteriskChannel;
	}
	
	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Map<String, String> getChanVariables() {
		return chanVariables;
	}

	public void setChanVariables(Map<String, String> chanVariables) {
		this.chanVariables = chanVariables;
	}
	
	public String getChanVarialbe(String key)
	{
		return chanVariables.get(key);
	}
}

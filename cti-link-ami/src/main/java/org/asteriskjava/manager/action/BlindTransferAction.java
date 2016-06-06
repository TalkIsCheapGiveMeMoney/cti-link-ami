package org.asteriskjava.manager.action;

import org.asteriskjava.manager.action.AbstractManagerAction;

public class BlindTransferAction extends AbstractManagerAction {
	static final long serialVersionUID = 1L;

	private String channel;
	private String exten;
	private String context;

	/**
	 * Creates a new empty AtxferAction.
	 */
	public BlindTransferAction() {

	}

	/**
	 * Creates a new AtxferAction that initiates an attended transfer of the
	 * given channel to the given context, extension, priority triple.
	 *
	 * @param channel
	 *            the name of the channel to transfer
	 * @param context
	 *            the destination context
	 * @param exten
	 *            the destination extension
	 * @param priority
	 *            the destination priority
	 */
	public BlindTransferAction(String channel, String context, String extension) {
		this.channel = channel;
		this.context = context;
		this.exten = extension;
	}

	/**
	 * Returns the name of this action, i.e. "Atxfer".
	 */
	@Override
	public String getAction() {
		return "BlindTransfer";
	}

	/**
	 * Returns name of the channel to transfer.
	 *
	 * @return the name of the channel to transfer
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Sets name of the channel to transfer.
	 *
	 * @param channel
	 *            the name of the channel to transfer
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * Returns the destination context.
	 *
	 * @return the destination context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Sets the destination context.
	 *
	 * @param context
	 *            the destination context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}




}
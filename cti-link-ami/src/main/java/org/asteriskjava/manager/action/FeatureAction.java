package org.asteriskjava.manager.action;

import org.asteriskjava.manager.action.AbstractManagerAction;

public class FeatureAction extends AbstractManagerAction {
	static final long serialVersionUID = 1L;

	private String channel;
	private String extension;
	private String context;
	private String feature;

	/**
	 * Creates a new empty AtxferAction.
	 */
	public FeatureAction() {

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
	public FeatureAction(String channel, String context, String extension, String feature) {
		this.channel = channel;
		this.context = context;
		this.extension = extension;
		this.feature = feature;
	}

	/**
	 * Returns the name of this action, i.e. "Atxfer".
	 */
	@Override
	public String getAction() {
		return "Feature";
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

	/**
	 * Returns the destination extension.
	 *
	 * @return the destination extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets the destination extension.
	 *
	 * @param exten
	 *            the destination extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

}
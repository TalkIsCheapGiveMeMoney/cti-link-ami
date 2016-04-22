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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.action.AbstractManagerAction;

/**
 * The HangupAction causes Asterisk to hang up a given channel.
 * <p>
 * Hangup with a cause code is only supported by Asterisk versions later than
 * 1.6.2.
 *
 * @author srt
 * @version $Id: HangupAction.java 1280 2009-04-03 18:54:51Z srt $
 */
public class IndicateAction extends AbstractManagerAction {
	/**
	 * Serializable version identifier
	 */
	static final long serialVersionUID = 0L;

	private String channel;
	private Integer indicate;

	/**
	 * Creates a new empty HangupAction.
	 */
	public IndicateAction() {

	}

	public IndicateAction(String chan) {
		this.channel = chan;
	}
	/**
	 * Creates a new HangupAction that hangs up the given channel.
	 *
	 * @param channel
	 *            the name of the channel to hangup.
	 * @since 0.2
	 */

	/**
	 * Creates a new HangupAction that hangs up the given channel with the given
	 * cause code.
	 *
	 * @param channel
	 *            the name of the channel to hangup.
	 * @param cause
	 *            the cause code. The cause code must be >= 0 and <= 127.
	 * @since 1.0.0
	 */

	/**
	 * Returns the name of this action, i.e. "Hangup".
	 */
	@Override
	public String getAction() {
		return "Indicate";
	}

	public void setChannel(String chan) {
		channel = chan;
	}

	public String getChannel() {
		return channel;
	}

	/**
	 * Returns the hangup cause.
	 *
	 * @return the hangup cause.
	 * @since 1.0.0
	 */
	public Integer getIndicate() {
		return indicate;
	}

	/**
	 * Sets the hangup cause. The cause code must be >= 0 and <= 127.
	 * <p>
	 * This property is optional.
	 *
	 * @param cause
	 *            the hangup cause.
	 * @since 1.0.0
	 */
	public void setIndicate(Integer indicate) {
		this.indicate = indicate;
	}
}

package org.asteriskjava.manager.event;

/**
 * AgiExecEvents are triggered when an AGI command is executed. For each command
 * two events are triggered: one before excution ("Start") and one after
 * execution ("End").
 * <p/>
 * The following sub events are reported:
 * <ul>
 * <li>Start: Execution of an AGI command has started.</li>
 * <li>End: Execution of an AGI command has finished.</li>
 * </ul>
 * It is implemented in <code>res/res_agi.c</code>.
 * <p/>
 * Available since Asterisk 1.6
 *
 * @author srt
 * @version $Id: AgiExecEvent.java 959 2008-02-02 23:56:59Z srt $
 * @since 1.0.0
 */
public class AgiExecEvent extends ManagerEvent {
	/**
	 * Serializable version identifier.
	 */
	static final long serialVersionUID = 0L;

	/**
	 * Execution of an AGI command has started.
	 */
	public static final String SUB_EVENT_START = "Start";

	/**
	 * Execution of an AGI command has finished.
	 */
	public static final String SUB_EVENT_END = "End";

	private String channel;
	private String subEvent;
	private String commandId;
	private String command;
	private Integer resultCode;
	private String result;

	/**
	 * Creates a new AgiExecEvent.
	 *
	 * @param source
	 */
	public AgiExecEvent(Object source) {
		super(source);
	}

	/**
	 * Returns the name of the channel this event occurred on.
	 *
	 * @return the name of the channel this event occurred on.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Sets the name of the channel this event occurred on.
	 *
	 * @param channel
	 *            the name of the channel this event occurred on.
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * Returns the sub event type. This is either "Start" or "End".
	 *
	 * @return the sub event type.
	 * @see #SUB_EVENT_START
	 * @see #SUB_EVENT_END
	 */
	public String getSubEvent() {
		return subEvent;
	}

	/**
	 * Sets the sub event type.
	 *
	 * @param subEvent
	 *            the sub event type.
	 */
	public void setSubEvent(String subEvent) {
		this.subEvent = subEvent;
	}

	/**
	 * Returns the command id. The command is a random number generated by
	 * Asterisk that allows matching the "End" sub event with the corresponding
	 * "Start" sub event.
	 *
	 * @return the command id.
	 */
	public String getCommandId() {
		return commandId;
	}

	/**
	 * Sets the command id.
	 *
	 * @param commandId
	 *            the command id.
	 */
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	/**
	 * Returns the AGI command.
	 *
	 * @return the AGI command.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the AGI command.
	 *
	 * @param command
	 *            the AGI command.
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Returns the result code.
	 *
	 * @return the result code.
	 */
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * Sets the result code.
	 *
	 * @param resultCode
	 *            the result code.
	 */
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * Returns the result as a string.
	 * <p>
	 * They correspond to the numeric values returned by
	 * {@link #getResultCode()}. Usually you will want to stick with the numeric
	 * values.
	 * <p>
	 * Possible values are:
	 * <ul>
	 * <li>Failure (corresponds to result code -1)</li>
	 * <li>Success (corresponds to result code 200)</li>
	 * <li>KeepAlive (corresponds to result code 210)</li>
	 * <li>Command not permitted on a dead channel (corresponds to result code
	 * 511)</li>
	 * <li>Usage (corresponds to result code 520)</li>
	 * </ul>
	 *
	 * @return a string respresentation of the result.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets the string respresentation of the result.
	 *
	 * @param result
	 *            a string respresentation of the result.
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Checks is this a start sub event.
	 *
	 * @return <code>true</code> if this is a "Start" sub event,
	 *         <code>false</code> otherwise.
	 */
	public boolean isStart() {
		return isSubEvent(SUB_EVENT_START);
	}

	/**
	 * Checks is this an end sub event.
	 *
	 * @return <code>true</code> if this is an "End" sub event,
	 *         <code>false</code> otherwise.
	 */
	public boolean isEnd() {
		return isSubEvent(SUB_EVENT_END);
	}

	private boolean isSubEvent(String subEvent) {
		return this.subEvent != null && this.subEvent.equalsIgnoreCase(subEvent);
	}
}
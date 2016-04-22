package org.asteriskjava.live;

/**
 * The lifecycle status of a {@link AsteriskQueueEntry}.
 * 
 * @author gmi
 */
public enum QueueEntryState {
	/**
	 * The user joined the queue.
	 */
	JOINED,

	/**
	 * The user left the queue.
	 */
	LEFT
}

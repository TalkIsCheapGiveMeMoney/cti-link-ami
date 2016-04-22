/*
 *  Copyright 2005-2006 Stefan Reuter
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
package org.asteriskjava.live.internal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.SendActionCallback;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerConnectionPool {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final BlockingQueue<ManagerConnection> connections;

	public ManagerConnectionPool(int size) {
		this.connections = new ArrayBlockingQueue<ManagerConnection>(size);
	}

	public Integer getConnectionCount() {
		return connections.size();
	}

	public void close() {
		for (ManagerConnection connection : connections) {
			try {
				if (connection != null && (connection.getState() == ManagerConnectionState.CONNECTED || connection
						.getState() == ManagerConnectionState.RECONNECTING)) {
					connection.logoff();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void clear() {
		connections.clear();
	}

	public void add(ManagerConnection connection) {
		put(connection);
	}

	public ManagerResponse sendAction(ManagerAction action) throws Exception {
		ManagerConnection connection;
		ManagerResponse response;

		connection = get();
		try {
			response = connection.sendAction(action);
		} catch (Exception e) {
			throw e;
		} finally {
			put(connection);
		}

		return response;
	}

	public ManagerResponse sendAction(ManagerAction action, long timeout) throws Exception {
		ManagerConnection connection;
		ManagerResponse response;

		connection = get();
		try {
			response = connection.sendAction(action, timeout);
		} finally {
			put(connection);
		}

		return response;
	}
	
	public void sendAction(ManagerAction action, SendActionCallback callback) throws Exception {
		ManagerConnection connection;

		connection = get();
		try {
			connection.sendAction(action, callback);
		} finally {
			put(connection);
		}

		return;
	}
	
	public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) throws Exception {
		return sendEventGeneratingAction(action, -1);
	}

	public ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout) throws Exception {
		ManagerConnection connection;
		ResponseEvents responseEvents;

		connection = get();
		try {
			if (timeout > 0) {
				responseEvents = connection.sendEventGeneratingAction(action, timeout);
			} else {
				responseEvents = connection.sendEventGeneratingAction(action);
			}
		} finally {
			put(connection);
		}

		return responseEvents;
	}

	/**
	 * Retrieves a connection from the pool.
	 * 
	 * @return the retrieved connection, or <code>null</code> if interrupted
	 *         while waiting for a connection to become available.
	 */
	private ManagerConnection get() {
		try {
			ManagerConnection connection = connections.take();
			if (connection.getState() == ManagerConnectionState.INITIAL || connection
					.getState() == ManagerConnectionState.DISCONNECTED) {
				try {
					connection.login("off");
				} catch (Exception e) {
					throw new ManagerCommunicationException("Unable to login: " + e.getMessage(), e);
				}
			}
			return connection;
		} catch (InterruptedException e) {
			logger.error("Interrupted while waiting for ManagerConnection to become available", e);
			Thread.currentThread().interrupt();
			return null;
		}
	}

	private void put(ManagerConnection connection) {
		try {
			connections.put(connection);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted while trying to add connection to pool");
		}
	}
}

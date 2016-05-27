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


public class SorceryMemoryCacheExpireAction extends AbstractManagerAction {
	/**
	 * Serial version identifier
	 */
	private static final long serialVersionUID = 3978144348493591607L;

	public String cache;

	
	/**
	 * Creates a new empty SetVarAction.
	 */
	public SorceryMemoryCacheExpireAction() {

	}

	

	/**
	 * Returns the name of this action, i.e. "SetVar".
	 * 
	 * @return the name of this action
	 */
	@Override
	public String getAction() {
		return "SorceryMemoryCacheExpire";
	}



	public String getCache() {
		return cache;
	}



	public void setCache(String cache) {
		this.cache = cache;
	}

	
}

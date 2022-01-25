/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astrapi69.model.api;

import java.io.Serializable;

/**
 * The interface {@link IDetachable} provides the ability to detach an object that was previously
 * attached to a context. This reduces the amount of state required by an object. This makes the
 * object cheaper to replicate in a clustered environment.
 */
public interface IDetachable extends Serializable
{

	/**
	 * Detach an object.
	 */
	void detach();
}

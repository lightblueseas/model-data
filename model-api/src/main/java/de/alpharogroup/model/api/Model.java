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
package de.alpharogroup.model.api;

/**
 * A Model decorates the actual model object that can be used by any other UI-Component. Model
 * implementations are used as a facade for the real model so that users have control over the
 * actual persistence strategy.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface Model<T> extends Attachable, Detachable
{
	/**
	 * Gets the model object.
	 *
	 * @return The model object
	 */
	T getObject();

	/**
	 * Sets the model object.
	 *
	 * @param object
	 *            The model object
	 */
	void setObject(final T object);
}

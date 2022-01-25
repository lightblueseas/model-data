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

/**
 * The marker interface {@link IWrapModel} represents a model that serves as a decorator for another.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface IWrapModel<T> extends IModel<T>
{

	/**
	 * Gets the wrapped model.
	 *
	 * @return the wrapped model
	 */
	IModel<?> getWrappedModel();
}

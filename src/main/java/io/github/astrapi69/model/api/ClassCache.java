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

import java.util.Map;

/**
 * The interface {@link ClassCache} provides methods for a class cache. This inteface was previously
 * in the PropertyResolver class.
 */
public interface ClassCache
{

	/**
	 * Returns the class map from the cache.
	 *
	 * @param clz
	 *            the class
	 * @return the map of the given class
	 */
	Map<String, GetAndSet> get(Class<?> clz);

	/**
	 * Put the class into the cache, or if that class shouldn't be cached do nothing.
	 *
	 * @param clz
	 *            the class
	 * @param values
	 *            the values
	 */
	void put(Class<?> clz, Map<String, GetAndSet> values);
}

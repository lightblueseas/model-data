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
package io.github.astrapi69.model.property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.astrapi69.model.api.ClassCache;
import io.github.astrapi69.model.api.GetAndSet;

public class DefaultClassCache implements ClassCache
{
	private final ConcurrentHashMap<Class<?>, Map<String, GetAndSet>> map = new ConcurrentHashMap<>(
		16);

	@Override
	public Map<String, GetAndSet> get(Class<?> clz)
	{
		return map.get(clz);
	}

	@Override
	public void put(Class<?> clz, Map<String, GetAndSet> values)
	{
		map.put(clz, values);
	}
}

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
package io.github.astrapi69.model.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory caching proxy classes.
 *
 * @see #createClass(Class)
 */
public class CachingProxyFactory implements IProxyFactory
{

	private final IProxyFactory factory;
	private final ConcurrentHashMap<Object, IProxyFactory> scopes = new ConcurrentHashMap<>(2);

	public CachingProxyFactory(IProxyFactory factory)
	{
		this.factory = factory;
	}

	@Override
	public Class<?> createClass(Class<?> clazz)
	{
		return getFactory().createClass(clazz);
	}

	@Override
	public Object createInstance(final Class<?> proxyClass, final Callback callback)
	{
		return getFactory().createInstance(proxyClass, callback);
	}

	public void destroy(Object application)
	{
		scopes.remove(application);
	}

	@Override
	public Callback getCallback(Object proxy)
	{
		return getFactory().getCallback(proxy);
	}

	private IProxyFactory getFactory()
	{
		Object key;

		key = CachingProxyFactory.class;

		IProxyFactory result = scopes.get(key);
		if (result == null)
		{
			IProxyFactory tmpResult = scopes.putIfAbsent(key, result = new ApplicationScope());
			if (tmpResult != null)
			{
				result = tmpResult;
			}
		}

		return result;
	}

	private class ApplicationScope implements IProxyFactory
	{

		private final Map<Class<?>, Class<?>> proxyClasses = new ConcurrentHashMap<>();

		@Override
		public Class<?> createClass(Class<?> clazz)
		{
			Class<?> proxyClazz = proxyClasses.get(clazz);
			if (proxyClazz == null)
			{
				proxyClazz = factory.createClass(clazz);
				if (proxyClazz == null)
				{
					proxyClazz = NOT_PROXYABLE.class;
				}
				proxyClasses.put(clazz, proxyClazz);
			}

			if (proxyClazz == NOT_PROXYABLE.class)
			{
				proxyClazz = null;
			}

			return proxyClazz;
		}

		@Override
		public Object createInstance(final Class<?> proxyClass, final Callback callback)
		{
			return factory.createInstance(proxyClass, callback);
		}

		@Override
		public Callback getCallback(Object proxy)
		{
			return factory.getCallback(proxy);
		}
	}

	private class NOT_PROXYABLE
	{
	}
}

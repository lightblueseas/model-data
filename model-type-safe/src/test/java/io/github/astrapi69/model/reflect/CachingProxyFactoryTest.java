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

import io.github.astrapi69.model.reflect.CachingProxyFactory;
import io.github.astrapi69.model.reflect.IProxyFactory;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Test for {@link CachingProxyFactory}.
 *
 * @author svenmeier
 */
public class CachingProxyFactoryTest
{

	@Test
	public void test() throws Exception
	{
		final int[] count = new int[1];

		IProxyFactory factory = new CachingProxyFactory(new IProxyFactory()
		{

			@Override
			public Class<?> createClass(Class<?> clazz)
			{
				count[0]++;

				return clazz;
			}

			@Override
			public Object createInstance(Class<?> proxyClass, Callback callback)
			{
				return null;
			}

			@Override
			public Callback getCallback(Object proxy)
			{
				return null;
			}
		});

		factory.createClass(String.class);
		factory.createClass(String.class);
		assertEquals(1, count[0]);
	}
}

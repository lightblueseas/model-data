/**
 * Copyright (C) 2015 Asterios Raptis
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astrapi69.model.property;

import java.lang.reflect.Array;

public final class ArrayLengthGetSet extends AbstractGetAndSet
{
	ArrayLengthGetSet()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getTargetClass()
	{
		return int.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(final Object object)
	{
		return Array.getLength(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(final Object object)
	{
		throw new RuntimeException("Can't get a new value from a length of an array: " + object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final Object object, final Object value)
	{
		throw new RuntimeException("You can't set the length on an array:" + object);
	}
}

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
package de.alpharogroup.model.property;

import java.lang.reflect.Array;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
public final class ArrayGetSet extends AbstractGetAndSet
{
	private final Class<?> clzComponentType;
	private final int index;

	ArrayGetSet(Class<?> clzComponentType, int index)
	{
		this.clzComponentType = clzComponentType;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getTargetClass()
	{
		return clzComponentType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object object)
	{
		if (Array.getLength(object) > index)
		{
			return Array.get(object, index);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(Object object)
	{
		Object value = null;
		try
		{
			value = clzComponentType.newInstance();
			Array.set(object, index, value);
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Cannot set new value " + value + " at index " + index
				+ " for array holding elements of class " + clzComponentType, e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Object object, Object value)
	{
		Array.set(object, index, value);
	}
}

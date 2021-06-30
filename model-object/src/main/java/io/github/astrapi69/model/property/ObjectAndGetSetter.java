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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.astrapi69.model.api.GetAndSet;

public final class ObjectAndGetSetter
{
	private final GetAndSet getAndSetter;
	private final Object value;

	/**
	 * Instantiates a new object and get setter.
	 *
	 * @param getAndSetter
	 *            the get and setter
	 * @param value
	 *            the value
	 */
	public ObjectAndGetSetter(GetAndSet getAndSetter, Object value)
	{
		this.getAndSetter = getAndSetter;
		this.value = value;
	}

	/**
	 * @return Field or null if no field exists for expression
	 */
	public Field getField()
	{
		return getAndSetter.getField();
	}

	/**
	 * @return Getter method or null if no getter exists for expression
	 */
	public Method getGetter()
	{
		return getAndSetter.getGetter();
	}

	/**
	 * @return Setter method or null if no setter exists for expression
	 */
	public Method getSetter()
	{
		return getAndSetter.getSetter();
	}

	/**
	 * @return class of property value
	 */
	public Class<?> getTargetClass()
	{
		return getAndSetter.getTargetClass();
	}

	/**
	 * @return The value
	 */
	public Object getValue()
	{
		return getAndSetter.getValue(value);
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(Object value)
	{
		getAndSetter.setValue(this.value, value);
	}
}

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
final class MethodGetAndSet extends AbstractGetAndSet
{
	private final Field field;
	private final Method getMethod;
	private final Method setMethod;

	MethodGetAndSet(Method getMethod, Method setMethod, Field field)
	{
		this.getMethod = getMethod;
		this.getMethod.setAccessible(true);
		this.field = field;
		this.setMethod = setMethod;
	}

	public final static Method findSetter(Method getMethod, Class<?> clz)
	{
		String name = getMethod.getName();
		if (name.startsWith(GET))
		{
			name = SET + name.substring(3);
		}
		else
		{
			name = SET + name.substring(2);
		}
		try
		{
			Method method = clz.getMethod(name, new Class[] { getMethod.getReturnType() });
			if (method != null)
			{
				method.setAccessible(true);
			}
			return method;
		}
		catch (NoSuchMethodException e)
		{
			Method[] methods = clz.getMethods();
			for (Method method : methods)
			{
				if (method.getName().equals(name))
				{
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length == 1)
					{
						if (parameterTypes[0].isAssignableFrom(getMethod.getReturnType()))
						{
							return method;
						}
					}
				}
			}
			log.log(Level.FINE, "Cannot find setter corresponding to " + getMethod);
		}
		catch (Exception e)
		{
			log.log(Level.FINE, "Cannot find setter corresponding to " + getMethod);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField()
	{
		return field;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getGetter()
	{
		return getMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getSetter()
	{
		return setMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getTargetClass()
	{
		return getMethod.getReturnType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Object getValue(final Object object)
	{
		Object ret = null;
		try
		{
			ret = getMethod.invoke(object, (Object[])null);
		}
		catch (InvocationTargetException ex)
		{
			throw new RuntimeException(
				"Error calling method: " + getMethod + " on object: " + object, ex.getCause());
		}
		catch (Exception ex)
		{
			throw new RuntimeException(
				"Error calling method: " + getMethod + " on object: " + object, ex);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(Object object)
	{
		if (setMethod == null)
		{
			log.log(Level.WARNING, "Null setMethod");
			return null;
		}

		Class<?> clz = getMethod.getReturnType();
		Object value = null;
		try
		{
			value = clz.newInstance();
			setMethod.invoke(object, value);
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Cannot set new value " + value, e);
		}
		return value;
	}

	/**
	 * @param object
	 * @param value
	 */
	@Override
	public final void setValue(final Object object, final Object value)
	{

		Object converted = value;


		if (setMethod != null)
		{
			try
			{
				setMethod.invoke(object, converted);
			}
			catch (InvocationTargetException ex)
			{
				throw new RuntimeException(
					"Error calling method: " + setMethod + " on object: " + object, ex.getCause());
			}
			catch (Exception ex)
			{
				throw new RuntimeException(
					"Error calling method: " + setMethod + " on object: " + object, ex);
			}
		}
		else if (field != null)
		{
			try
			{
				field.set(object, converted);
			}
			catch (Exception ex)
			{
				throw new RuntimeException(
					"Error setting field: " + field + " on object: " + object, ex);
			}
		}
		else
		{
			throw new RuntimeException("no set method defined for value: " + value + " on object: "
				+ object + " while respective getMethod being " + getMethod.getName());
		}
	}
}

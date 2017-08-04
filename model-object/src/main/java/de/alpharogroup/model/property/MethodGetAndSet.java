package de.alpharogroup.model.property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class MethodGetAndSet extends AbstractGetAndSet
{
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
			log.debug("Cannot find setter corresponding to " + getMethod);
		}
		catch (Exception e)
		{
			log.debug("Cannot find setter corresponding to " + getMethod);
		}
		return null;
	}

	private final Method getMethod;
	private final Method setMethod;

	private final Field field;

	MethodGetAndSet(Method getMethod, Method setMethod, Field field)
	{
		this.getMethod = getMethod;
		this.getMethod.setAccessible(true);
		this.field = field;
		this.setMethod = setMethod;
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
			log.warn("Null setMethod");
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
			log.warn("Cannot set new value " + value, e);
		}
		return value;
	}

	/**
	 * @param object
	 * @param value
	 * @param converter
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

package de.alpharogroup.model.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
public final class ArrayPropertyGetSet extends AbstractGetAndSet
{
	private final static Method findSetter(final Method getMethod, final Class<?> clz)
	{
		String name = getMethod.getName();
		name = SET + name.substring(3);
		try
		{
			return clz.getMethod(name, new Class[] { int.class, getMethod.getReturnType() });
		}
		catch (Exception e)
		{
			log.log(Level.FINE, "Can't find setter method corresponding to " + getMethod);
		}
		return null;
	}

	final private Method getMethod;
	final private Integer index;

	private Method setMethod;

	ArrayPropertyGetSet(final Method method, final int index)
	{
		this.index = index;
		getMethod = method;
		getMethod.setAccessible(true);
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
	public Object getValue(Object object)
	{
		Object ret = null;
		try
		{
			ret = getMethod.invoke(object, index);
		}
		catch (InvocationTargetException ex)
		{
			throw new RuntimeException(
				"Error calling index property method: " + getMethod + " on object: " + object,
				ex.getCause());
		}
		catch (Exception ex)
		{
			throw new RuntimeException(
				"Error calling index property method: " + getMethod + " on object: " + object, ex);
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
			setMethod = findSetter(getMethod, object.getClass());
		}

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
			setMethod.invoke(object, index, value);
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Cannot set new value " + value + " at index " + index, e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final Object object, final Object value)
	{
		if (setMethod == null)
		{
			setMethod = findSetter(getMethod, object.getClass());
		}
		if (setMethod != null)
		{
			setMethod.setAccessible(true);
			try
			{
				setMethod.invoke(object, index, value);
			}
			catch (InvocationTargetException ex)
			{
				throw new RuntimeException(
					"Error index property calling method: " + setMethod + " on object: " + object,
					ex.getCause());
			}
			catch (Exception ex)
			{
				throw new RuntimeException(
					"Error index property calling method: " + setMethod + " on object: " + object,
					ex);
			}
		}
		else
		{
			throw new RuntimeException(
				"No set method defined for value: " + value + " on object: " + object);
		}
	}
}
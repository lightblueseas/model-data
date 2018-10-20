package de.alpharogroup.model.property;

import java.lang.reflect.Array;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			log.warn("Cannot set new value " + value + " at index " + index
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

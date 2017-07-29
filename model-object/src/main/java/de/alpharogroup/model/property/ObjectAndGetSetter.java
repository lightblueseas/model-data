package de.alpharogroup.model.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.alpharogroup.model.api.GetAndSet;

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

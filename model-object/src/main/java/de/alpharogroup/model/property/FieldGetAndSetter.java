package de.alpharogroup.model.property;

import java.lang.reflect.Field;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
public class FieldGetAndSetter extends AbstractGetAndSet
{
	private final Field field;

	/**
	 * Construct.
	 *
	 * @param field
	 *            the field
	 */
	public FieldGetAndSetter(final Field field)
	{
		super();
		this.field = field;
		this.field.setAccessible(true);
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
	public Class<?> getTargetClass()
	{
		return field.getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(final Object object)
	{
		try
		{
			return field.get(object);
		}
		catch (final Exception ex)
		{
			throw new RuntimeException(
				"Error getting field value of field " + field + " from object " + object, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(final Object object)
	{
		final Class<?> clz = field.getType();
		Object value = null;
		try
		{
			value = clz.newInstance();
			field.set(object, value);
		}
		catch (final Exception e)
		{
			log.log(Level.WARNING, "Cannot set field " + field + " to " + value, e);
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final Object object, Object value)
	{
		try
		{
			field.set(object, value);
		}
		catch (final Exception ex)
		{
			throw new RuntimeException("Error setting field value of field " + field + " on object "
				+ object + ", value " + value, ex);
		}
	}
}
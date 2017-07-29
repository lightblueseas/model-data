package de.alpharogroup.model.property;

import java.util.Map;

public final class MapGetSet extends AbstractGetAndSet
{
	private final String key;

	MapGetSet(String key)
	{
		this.key = key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(final Object object)
	{
		return ((Map<?, ?>)object).get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(final Object object)
	{
		// Map can't make a newValue or should it look what is more in the
		// map and try to make one of the class if finds?
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setValue(final Object object, final Object value)
	{
		((Map<String, Object>)object).put(key, value);
	}
}
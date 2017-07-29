package de.alpharogroup.model.property;

import java.util.List;

public final class ListGetSet extends AbstractGetAndSet
{
	final private int index;

	ListGetSet(int index)
	{
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(final Object object)
	{
		if (((List<?>)object).size() <= index)
		{
			return null;
		}
		return ((List<?>)object).get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object newValue(Object object)
	{
		// List can't make a newValue or should it look what is more in the
		// list and try to make one of the class if finds?
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setValue(final Object object, final Object value)
	{
		List<Object> lst = (List<Object>)object;

		if (lst.size() > index)
		{
			lst.set(index, value);
		}
		else if (lst.size() == index)
		{
			lst.add(value);
		}
		else
		{
			while (lst.size() < index)
			{
				lst.add(null);
			}
			lst.add(value);
		}
	}
}
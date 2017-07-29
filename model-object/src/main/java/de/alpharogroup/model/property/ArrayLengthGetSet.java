package de.alpharogroup.model.property;

import java.lang.reflect.Array;

public final class ArrayLengthGetSet extends AbstractGetAndSet
	{
		ArrayLengthGetSet()
		{
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getValue(final Object object)
		{
			return Array.getLength(object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setValue(final Object object, final Object value)
		{
			throw new RuntimeException("You can't set the length on an array:" + object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object newValue(final Object object)
		{
			throw new RuntimeException("Can't get a new value from a length of an array: " +
				object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getTargetClass()
		{
			return int.class;
		}
	}
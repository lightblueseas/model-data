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

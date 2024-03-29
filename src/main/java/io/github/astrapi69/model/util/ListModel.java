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
package io.github.astrapi69.model.util;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import io.github.astrapi69.model.api.IModel;
import lombok.NoArgsConstructor;

/**
 * Based on <code>IModel</code> but for lists of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside list
 */
@NoArgsConstructor
public class ListModel<T> extends GenericCollectionModel<List<T>>
{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Creates model that will contain <code>list</code>.
	 *
	 * @param list
	 *            the list
	 */
	public ListModel(final List<T> list)
	{
		super(list);
	}

	/**
	 * Factory method for models that contain list. This factory method will automatically rebuild a
	 * nonserializable <code>list</code> into a serializable {@link ArrayList}.
	 *
	 * @param <C>
	 *            model type
	 * @param list
	 *            The list, which may or may not be Serializable
	 * @return A IModel object wrapping the Set
	 */
	public static <C> IModel<List<C>> of(final List<C> list)
	{
		return new ListModel<>(list);
	}

	/** {@inheritDoc} */
	@Override
	protected List<T> newSerializableCollectionOf(final List<T> object)
	{
		if (object != null)
		{
			return new ArrayList<>(object);
		}
		return null;
	}
}

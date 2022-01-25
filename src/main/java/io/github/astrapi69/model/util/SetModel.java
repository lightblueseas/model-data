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

import java.util.HashSet;
import java.util.Set;

import lombok.NoArgsConstructor;
import io.github.astrapi69.model.api.IModel;


/**
 * Based on <code>IModel</code> but for sets of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside set
 */
@NoArgsConstructor
public class SetModel<T> extends GenericCollectionModel<Set<T>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates model that will contain <code>set</code>.
	 *
	 * @param set
	 *            the set
	 */
	public SetModel(final Set<T> set)
	{
		super(set);
	}

	/**
	 * Factory method for models that contain sets. This factory method will automatically rebuild a
	 * nonserializable <code>set</code> into a serializable one.
	 *
	 * @param <C>
	 *            model type
	 * @param set
	 *            The Set, which may or may not be Serializable
	 * @return A IModel object wrapping the Set
	 */
	public static <C> IModel<Set<C>> ofSet(final Set<C> set)
	{
		return new SetModel<>(set);
	}

	/** {@inheritDoc} */
	@Override
	protected Set<T> newSerializableCollectionOf(final Set<T> object)
	{
		if (object != null)
		{
			return new HashSet<>(object);
		}
		return null;
	}

}

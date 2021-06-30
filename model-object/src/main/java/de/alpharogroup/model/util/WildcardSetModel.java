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
package de.alpharogroup.model.util;

import java.util.HashSet;
import java.util.Set;

import io.github.astrapi69.model.api.Model;
import lombok.NoArgsConstructor;


/**
 * Based on <code>Model</code> but for sets of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside set
 */
@NoArgsConstructor
public class WildcardSetModel<T> extends GenericCollectionModel<Set<T>>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method for models that contain sets. This factory method will automatically rebuild a
	 * nonserializable <code>set</code> into a serializable one.
	 *
	 * @param <C>
	 *            model type
	 * @param set
	 *            The Set, which may or may not be Serializable
	 * @return A Model object wrapping the Set
	 */
	public static <C> Model<Set<C>> ofSet(final Set<C> set)
	{
		return new WildcardSetModel<>(set);
	}

	/**
	 * Creates model that will contain <code>set</code>.
	 *
	 * @param set
	 *            the set
	 */
	public WildcardSetModel(final Set<T> set)
	{
		super(set);
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

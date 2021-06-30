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

import java.util.ArrayList;
import java.util.Collection;

import io.github.astrapi69.model.api.Model;
import lombok.NoArgsConstructor;

/**
 * Based on <code>Model</code> but for any collections of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <T>
 *            type of object inside collection
 */
@NoArgsConstructor
public class WildcardCollectionModel<T> extends GenericCollectionModel<Collection<T>>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method for models that contain collections. This factory method will automatically
	 * rebuild a nonserializable <code>collection</code> into a serializable {@link ArrayList}.
	 *
	 * @param <C>
	 *            model type
	 * @param collection
	 *            The Collection, which may or may not be Serializable
	 * @return A Model object wrapping the Set
	 */
	public static <C> Model<Collection<C>> of(final Collection<C> collection)
	{
		return new WildcardCollectionModel<>(collection);
	}

	/**
	 * Creates model that will contain <code>collection</code>.
	 *
	 * @param collection
	 *            the collection
	 */
	public WildcardCollectionModel(final Collection<T> collection)
	{
		super(collection);
	}

	/** {@inheritDoc} */
	@Override
	protected Collection<T> newSerializableCollectionOf(final Collection<T> object)
	{
		if (object != null)
		{
			return new ArrayList<>(object);
		}
		return null;
	}
}

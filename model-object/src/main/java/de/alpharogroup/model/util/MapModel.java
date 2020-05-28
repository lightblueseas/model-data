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

import java.util.HashMap;
import java.util.Map;

import de.alpharogroup.model.api.Model;
import lombok.NoArgsConstructor;

/**
 * Based on <code>Model</code> but for maps of serializable objects.
 *
 * @author Timo Rantalaiho
 * @param <K>
 *            map's key type
 * @param <V>
 *            map's value type
 */
@NoArgsConstructor
public class MapModel<K, V> extends GenericCollectionModel<Map<K, V>>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method for models that contain maps. This factory method will automatically rebuild a
	 * nonserializable <code>map</code> into a serializable one.
	 *
	 * @param <K>
	 *            key type in map
	 * @param <V>
	 *            value type in map
	 * @param map
	 *            The Map, which may or may not be Serializable
	 * @return A Model object wrapping the Map
	 */
	public static <K, V> Model<Map<K, V>> ofMap(final Map<K, V> map)
	{
		return new MapModel<>(map);
	}

	/**
	 * Creates model that will contain <code>map</code>.
	 *
	 * @param map
	 *            the map
	 */
	public MapModel(final Map<K, V> map)
	{
		super(map);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<K, V> newSerializableCollectionOf(final Map<K, V> object)
	{
		if (object != null)
		{
			return new HashMap<>(object);
		}
		return null;
	}
}
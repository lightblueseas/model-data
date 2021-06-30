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
package de.alpharogroup.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.astrapi69.model.api.Attachable;
import io.github.astrapi69.model.api.Detachable;
import io.github.astrapi69.model.api.Model;
import io.github.astrapi69.model.api.ObjectClassAware;
import de.alpharogroup.model.util.MapModel;
import de.alpharogroup.model.util.WildcardCollectionModel;
import de.alpharogroup.model.util.WildcardListModel;
import de.alpharogroup.model.util.WildcardSetModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The class {@link GenericModel} is the basic implementation of an <code>Model</code>. Decorates a
 * simple object. This class is only for small object, if you want to store large objects consider
 * to use LoadableDetachableModel instead.
 *
 * @param <T>
 *            the generic type of the model object
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public abstract class GenericModel<T> implements Model<T>, ObjectClassAware<T>
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
	public static <C> Model<Collection<C>> ofCollection(final Collection<C> collection)
	{
		return new WildcardCollectionModel<>(collection);
	}

	/**
	 * Factory method for models that contain lists. This factory method will automatically rebuild
	 * a nonserializable <code>list</code> into a serializable one.
	 *
	 * @param <C>
	 *            model type
	 * @param list
	 *            The List, which may or may not be Serializable
	 * @return A Model object wrapping the List
	 */
	public static <C> Model<List<C>> ofList(final List<C> list)
	{
		return WildcardListModel.of(list);
	}

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
		return MapModel.ofMap(map);
	}

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
		return WildcardSetModel.ofSet(set);
	}

	/** Backing object. */
	private T object;


	/**
	 * Instantiates a new {@link GenericModel}.
	 *
	 * @param object
	 *            the object
	 */
	public GenericModel(T object)
	{
		setObject(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		if (object instanceof Attachable)
		{
			((Attachable)object).attach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void detach()
	{
		if (object instanceof Detachable)
		{
			((Detachable)object).detach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getObjectClass()
	{
		return object != null ? (Class<T>)object.getClass() : null;
	}

}

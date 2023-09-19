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
package io.github.astrapi69.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.astrapi69.model.api.Attachable;
import io.github.astrapi69.model.api.IDetachable;
import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.model.api.IObjectClassAwareModel;
import io.github.astrapi69.model.util.MapModel;
import io.github.astrapi69.model.util.WildcardCollectionModel;
import io.github.astrapi69.model.util.WildcardListModel;
import io.github.astrapi69.model.util.WildcardSetModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The class {@link Model} is the basic implementation of an <code>IModel</code>. Decorates a
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
public class Model<T> implements IModel<T>, IObjectClassAwareModel<T>
{

	/** The Constant serialVersionUID. */
	@Serial
	private static final long serialVersionUID = 1L;
	/** Backing object. */
	private T object;

	/**
	 * Instantiates a new {@link Model}.
	 *
	 * @param object
	 *            the object
	 */
	public Model(T object)
	{
		setObject(object);
	}

	/**
	 * Factory method for models that contain collections. This factory method will automatically
	 * rebuild a nonserializable <code>collection</code> into a serializable {@link ArrayList}.
	 *
	 * @param <C>
	 *            model type
	 * @param collection
	 *            The Collection, which may or may not be Serializable
	 * @return A IModel object wrapping the Set
	 */
	public static <C> IModel<Collection<C>> ofCollection(final Collection<C> collection)
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
	 * @return A IModel object wrapping the List
	 */
	public static <C> IModel<List<C>> ofList(final List<C> list)
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
	 * @return A IModel object wrapping the Map
	 */
	public static <K, V> IModel<Map<K, V>> ofMap(final Map<K, V> map)
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
	 * @return A IModel object wrapping the Set
	 */
	public static <C> IModel<Set<C>> ofSet(final Set<C> set)
	{
		return WildcardSetModel.ofSet(set);
	}

	/**
	 * Factory methods for IModel which uses type inference to make code shorter. Equivalent to
	 * <code>new BaseModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return IModel that contains <code>object</code>
	 */
	public static <T> IModel<T> of()
	{
		return new Model<>();
	}

	/**
	 * Supresses generics warning when converting model types.
	 *
	 * @param <T>
	 *            the generic type
	 * @param model
	 *            the model
	 * @return <code>model</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> IModel<T> of(final IModel<?> model)
	{
		return (IModel<T>)model;
	}

	/**
	 * Factory methods for IModel which uses type inference to make code shorter. Equivalent to
	 * <code>new BaseModel&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return IModel that contains <code>object</code>
	 */
	public static <T> IModel<T> of(final T object)
	{
		return new Model<>(object);
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
		if (object instanceof IDetachable)
		{
			((IDetachable)object).detach();
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

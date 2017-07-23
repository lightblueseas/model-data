package de.alpharogroup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.alpharogroup.model.api.Model;
import de.alpharogroup.model.util.MapModel;
import de.alpharogroup.model.util.WildcardCollectionModel;
import de.alpharogroup.model.util.WildcardListModel;
import de.alpharogroup.model.util.WildcardSetModel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The class {@link SerializableModel} contains only model object that implements the
 * {@link Serializable} interface.
 *
 * @param <T>
 *            the generic type
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SerializableModel<T extends Serializable> extends GenericModel<T>
{

	private static final long serialVersionUID = 1L;

	public SerializableModel(T object)
	{
		super(object);
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
		return new WildcardListModel<>(list);
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
		return new MapModel<>(map);
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
		return new WildcardSetModel<>(set);
	}

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
	 * Factory methods for Model which uses type inference to make code shorter. Equivalent to
	 * <code>new Model&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return Model that contains <code>object</code>
	 */
	public static <T extends Serializable> Model<T> of(final T object)
	{
		return new SerializableModel<>(object);
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
	public static <T> Model<T> of(final Model<?> model)
	{
		return (Model<T>)model;
	}

	/**
	 * Factory methods for Model which uses type inference to make code shorter. Equivalent to
	 * <code>new SerializableModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return Model that contains <code>object</code>
	 */
	public static <T extends Serializable> Model<T> ofModel()
	{
		return new SerializableModel<>();
	}

	/**
	 * Set the model object; calls setObject(java.io.Serializable). The model object must be
	 * serializable, as it is stored in the session
	 *
	 * @param object
	 *            the model object
	 */
	@Override
	public void setObject(final T object)
	{
		if (object != null)
		{
			if (!(object instanceof Serializable))
			{
				throw new RuntimeException("Model object must be Serializable");
			}
		}
		super.setObject(object);
	}

}

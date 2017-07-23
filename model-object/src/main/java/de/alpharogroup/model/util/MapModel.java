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
	 * Creates model that will contain <code>map</code>.
	 *
	 * @param map the map
	 */
	public MapModel(final Map<K, V> map)
	{
		setObject(map);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<K, V> newSerializableCollectionOf(final Map<K, V> object)
	{
		return new HashMap<>(object);
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
}
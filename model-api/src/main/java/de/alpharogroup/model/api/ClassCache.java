package de.alpharogroup.model.api;

import java.util.Map;

/**
 * The interface {@link ClassCache} provides methods for a class cache. This inteface was previously
 * in the PropertyResolver class.
 */
public interface ClassCache
{

	/**
	 * Returns the class map from the cache.
	 *
	 * @param clz
	 *            the class
	 * @return the map of the given class
	 */
	Map<String, GetAndSet> get(Class<?> clz);

	/**
	 * Put the class into the cache, or if that class shouldn't be cached do nothing.
	 *
	 * @param clz
	 *            the class
	 * @param values
	 *            the values
	 */
	void put(Class<?> clz, Map<String, GetAndSet> values);
}
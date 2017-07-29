package de.alpharogroup.model.api;

import java.util.Map;

/**
 * An implementation of the class can be set on the
 * {@link PropertyResolver#setClassCache(org.apache.wicket.Application, org.apache.wicket.core.util.lang.PropertyResolver.IClassCache)}
 * method for a specific application. This class cache can then be a special map with
 * an eviction policy or do nothing if nothing should be cached for the given class.
 *
 * For example if you have proxy classes that are constantly created you could opt for not
 * caching those at all or have a special Map implementation that will evict that class at a
 * certain point.
 *
 * @author jcompagner
 */
public interface ClassCache {
	/**
	 * Put the class into the cache, or if that class shouldn't be cached do
	 * nothing.
	 *
	 * @param clz
	 * @param values
	 */
	void put(Class<?> clz, Map<String, GetAndSet> values);

	/**
	 * Returns the class map from the cache.
	 *
	 * @param clz
	 * @return the map of the given class
	 */
	Map<String, GetAndSet> get(Class<?> clz);
}
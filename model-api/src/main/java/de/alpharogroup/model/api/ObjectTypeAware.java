package de.alpharogroup.model.api;

import java.lang.reflect.Type;

/**
 * The interface {@link ObjectTypeAware} can resolve the type of the generic object type.
 *
 * @param <T>
 *            the generic type
 */
public interface ObjectTypeAware<T>
{

	/**
	 * Gets the type of the generic object type
	 *
	 * @return the type of the generic object type
	 */
	Type getObjectType();
}
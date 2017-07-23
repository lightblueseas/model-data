package de.alpharogroup.model.api;

/**
 * The interface {@link ObjectClassAware} can resolve the class of the generic type.
 *
 * @param <T>
 *            the generic type
 */
public interface ObjectClassAware<T>
{

	/**
	 * Gets the object class.
	 *
	 * @return the object class
	 */
	Class<T> getObjectClass();
}
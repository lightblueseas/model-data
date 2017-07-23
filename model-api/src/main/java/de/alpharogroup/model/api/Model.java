package de.alpharogroup.model.api;

/**
 * A Model decorates the actual model object that can be used by any other UI-Component. Model
 * implementations are used as a facade for the real model so that users have control over the
 * actual persistence strategy.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface Model<T> extends Attachable, Detachable
{
	/**
	 * Gets the model object.
	 *
	 * @return The model object
	 */
	T getObject();

	/**
	 * Sets the model object.
	 *
	 * @param object
	 *            The model object
	 */
	void setObject(final T object);
}

package de.alpharogroup.model.api;

/**
 * The marker interface {@link WrapModel} represents a model that serves as a decorator for another.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface WrapModel<T> extends Model<T>
{

	/**
	 * Gets the wrapped model.
	 *
	 * @return the wrapped model
	 */
	Model<?> getWrappedModel();
}

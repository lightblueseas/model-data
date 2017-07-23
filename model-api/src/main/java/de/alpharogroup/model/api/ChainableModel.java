package de.alpharogroup.model.api;

/**
 * The interface {@link ChainableModel} provides chaining for model objects. The
 * {@link ChainableModel} is also responsible to detach the internal models.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface ChainableModel<T> extends Model<T>
{

	/**
	 * Sets the chained model.
	 *
	 * @param model the new chained model
	 */
	public void setChainedModel(Model<?> model);

	/**
	 * Gets the chained model.
	 *
	 * @return the chained model
	 */
	public Model<?> getChainedModel();

}

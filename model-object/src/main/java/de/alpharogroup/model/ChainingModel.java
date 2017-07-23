package de.alpharogroup.model;

import de.alpharogroup.model.api.Attachable;
import de.alpharogroup.model.api.ChainableModel;
import de.alpharogroup.model.api.Detachable;
import de.alpharogroup.model.api.Model;

/**
 * The class {@link ChainingModel} is the default implementation of {@link ChainableModel}
 * interface.
 *
 * @param <T>
 *            the generic type of the model object
 *
 * @see AbstractPropertyModel
 */
public class ChainingModel<T> implements ChainableModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Any model object (which may or may not implement Model) */
	private Object target;

	/**
	 * Instantiates a new {@link ChainingModel}.
	 *
	 * @param target
	 *            the target object
	 */
	public ChainingModel(final Object target)
	{
		this.target = target;
	}

	/**
	 * Unsets this property model's instance variables and detaches the model.
	 */
	@Override
	public void detach()
	{
		// Detach nested object if it's a detachable
		if (target instanceof Detachable)
		{
			((Detachable)target).detach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		if (target instanceof Attachable)
		{
			((Attachable)target).attach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setObject(final T object)
	{
		if (target instanceof Model)
		{
			((Model<T>)target).setObject(object);
		}
		else
		{
			target = object;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getObject()
	{
		if (target instanceof Model)
		{
			return ((Model<T>)target).getObject();
		}
		return (T)target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model<?> getChainedModel()
	{
		if (target instanceof Model)
		{
			return (Model<?>)target;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChainedModel(final Model<?> model)
	{
		target = model;
	}

	/**
	 * @return The target - object or model
	 */
	protected final Object getTarget()
	{
		return target;
	}

	/**
	 * Sets a new target - object or model.
	 *
	 * @param modelObject
	 *            the model object
	 * @return this object
	 */
	protected final ChainingModel<T> setTarget(final Object modelObject)
	{
		this.target = modelObject;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("Model:classname=[");
		sb.append(getClass().getName()).append(']');
		sb.append(":nestedModel=[").append(target).append(']');
		return sb.toString();
	}

}

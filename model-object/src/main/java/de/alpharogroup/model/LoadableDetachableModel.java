package de.alpharogroup.model;

import de.alpharogroup.model.api.Model;
import lombok.NoArgsConstructor;

/**
 * Model that makes working with detachable models a breeze. LoadableDetachableModel holds a
 * temporary, transient model object, that is set when {@link #getObject()} is called by calling
 * abstract method 'load', and that will be reset/ set to null on {@link #detach()}.
 *
 * A usage example:
 *
 * <pre>
 * LoadableDetachableModel venueListModel = new LoadableDetachableModel()
 * {
 * 	protected Object load()
 * 	{
 * 		return getVenueDao().findVenues();
 * 	}
 * };
 * </pre>
 *
 * <p>
 * Though you can override methods {@link #onAttach()} and {@link #onDetach()} for additional
 * attach/ detach behavior, the point of this class is to hide as much of the attaching/ detaching
 * as possible. So you should rarely need to override those methods, if ever.
 * </p>
 *
 * @author Eelco Hillenius
 * @author Igor Vaynberg
 *
 * @param <T>
 *            The Model Object type
 */
@NoArgsConstructor
public abstract class LoadableDetachableModel<T> implements Model<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** keeps track of whether this model is attached or detached */
	private transient boolean attached = false;

	/** temporary, transient object. */
	private transient T transientModelObject;

	/**
	 * This constructor is used if you already have the object retrieved and want to wrap it with a
	 * detachable model.
	 *
	 * @param object
	 *            retrieved instance of the detachable object
	 */
	public LoadableDetachableModel(T object)
	{
		this.transientModelObject = object;
		attached = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void detach()
	{
		if (attached)
		{
			try
			{
				onDetach();
			}
			finally
			{
				attached = false;
				transientModelObject = null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T getObject()
	{
		if (!attached)
		{
			attached = true;
			transientModelObject = load();
			onAttach();
		}
		return transientModelObject;
	}

	/**
	 * Checks if the object is attached.
	 *
	 * @return true, if the object is attached otherwise false
	 */
	public final boolean isAttached()
	{
		return attached;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append(":attached=").append(attached).append(":tempModelObject=[")
			.append(this.transientModelObject).append("]");
		return sb.toString();
	}

	/**
	 * Loads and returns the (temporary) model object.
	 *
	 * @return the (temporary) model object
	 */
	protected abstract T load();

	/**
	 * Attaches to the current request. Implement this method with custom behavior, such as loading
	 * the model object.
	 */
	protected void onAttach()
	{
	}

	/**
	 * Detaches from the current request. Implement this method with custom behavior, such as
	 * setting the model object to null.
	 */
	protected void onDetach()
	{
	}


	/**
	 * Manually loads the model with the specified object. Subsequent calls to {@link #getObject()}
	 * will return {@code object} until {@link #detach()} is called.
	 *
	 * @param object
	 *            The object to set into the model
	 */
	@Override
	public void setObject(final T object)
	{
		attached = true;
		transientModelObject = object;
	}

}

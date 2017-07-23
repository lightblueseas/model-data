package de.alpharogroup.model;

import de.alpharogroup.model.api.Attachable;
import de.alpharogroup.model.api.Detachable;
import de.alpharogroup.model.api.Model;
import de.alpharogroup.model.api.ObjectClassAware;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The class {@link GenericModel} is the basic implementation of an <code>Model</code>. Decorates a
 * simple object. This class is only for small object, if you want to store large objects consider
 * to use LoadableDetachableModel instead.
 *
 * @param <T>
 *            the generic type of the model object
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class GenericModel<T> implements Model<T>, ObjectClassAware<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Backing object. */
	private T object;

	/**
	 * Instantiates a new {@link GenericModel}.
	 *
	 * @param object the object
	 */
	public GenericModel(T object)
	{
		setObject(object);
	}

	/**
	 * Factory methods for {@link GenericModel} which uses type inference to make code shorter.
	 * Equivalent to <code>new GenericModel&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return Model that contains <code>object</code>
	 */
	public static <T> GenericModel<T> of(final T object)
	{
		return new GenericModel<>(object);
	}

	/**
	 * Factory methods for {@link GenericModel} which uses type inference to make code shorter.
	 * Equivalent to <code>new GenericModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return The new {@link GenericModel} with no <code>object</code>
	 */
	public static <T> GenericModel<T> of()
	{
		return new GenericModel<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void detach()
	{
		if (object instanceof Detachable)
		{
			((Detachable)object).detach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getObjectClass()
	{
		return object != null ? (Class<T>)object.getClass() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		if (object instanceof Attachable)
		{
			((Attachable)object).attach();
		}
	}
}

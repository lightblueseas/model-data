package de.alpharogroup.model;

import java.io.Serializable;

import de.alpharogroup.model.api.Model;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class {@link SerializableModel} contains only model object that implements the
 * {@link Serializable} interface.
 *
 * @param <T>
 *            the generic type of the model object
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SerializableModel<T extends Serializable> extends GenericModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new {@link SerializableModel}.
	 *
	 * @param object
	 *            the object
	 */
	public SerializableModel(T object)
	{
		super(object);
	}

	/**
	 * Factory methods for Model which uses type inference to make code shorter. Equivalent to
	 * <code>new Model&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return Model that contains <code>object</code>
	 */
	public static <T extends Serializable> Model<T> of(final T object)
	{
		return new SerializableModel<>(object);
	}

	/**
	 * Supresses generics warning when converting model types.
	 *
	 * @param <T>
	 *            the generic type
	 * @param model
	 *            the model
	 * @return <code>model</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Model<T> of(final Model<?> model)
	{
		return (Model<T>)model;
	}

	/**
	 * Factory methods for Model which uses type inference to make code shorter. Equivalent to
	 * <code>new SerializableModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return Model that contains <code>object</code>
	 */
	public static <T extends Serializable> Model<T> of()
	{
		return new SerializableModel<>();
	}

	/**
	 * Set the model object; calls setObject(java.io.Serializable). The model object must be
	 * serializable, as it is stored in the session
	 *
	 * @param object
	 *            the model object
	 */
	@Override
	public void setObject(final T object)
	{
		if (object != null)
		{
			if (!(object instanceof Serializable))
			{
				throw new RuntimeException("Model object must be Serializable");
			}
		}
		super.setObject(object);
	}

}

package de.alpharogroup.model;

import de.alpharogroup.model.api.Model;
import lombok.NoArgsConstructor;

/**
 * The class {@link BaseModel} for simple objects.
 *
 * @param <T>
 *            the generic type of the model object
 */
@NoArgsConstructor
public class BaseModel<T> extends GenericModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new base model.
	 *
	 * @param object
	 *            the object
	 */
	public BaseModel(T object)
	{
		super(object);
	}

	/**
	 * Factory methods for Model which uses type inference to make code shorter. Equivalent to
	 * <code>new BaseModel&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return Model that contains <code>object</code>
	 */
	public static <T> Model<T> of(final T object)
	{
		return new BaseModel<>(object);
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
	 * <code>new BaseModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return Model that contains <code>object</code>
	 */
	public static <T> Model<T> of()
	{
		return new BaseModel<>();
	}


}

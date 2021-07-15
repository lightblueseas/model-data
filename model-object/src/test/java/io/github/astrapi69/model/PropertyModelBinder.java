package io.github.astrapi69.model;

public class PropertyModelBinder<T> extends AbstractPropertyModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Property expression for property access. */
	private final String expression;

	/** The property model to bind with this one */
	private final PropertyModel<T> bindModel;

	/**
	 * Construct with a wrapped (IModel) or unwrapped (non-IModel) object and a property expression
	 * that works on the given model.
	 *
	 * @param bindModel
	 *            The property model to bind with this one
	 * @param modelObject
	 *            The model object, which may or may not implement IModel
	 * @param expression
	 *            Property expression for property access
	 */
	public PropertyModelBinder(final PropertyModel<T> bindModel, final Object modelObject, final String expression)
	{
		super(modelObject);
		this.expression = expression;
		this.bindModel = bindModel;
	}

	/**
	 * Type-infering factory method.
	 *
	 * @param <Z>
	 *            the generic type
	 * @param bindModel
	 *            The property model to bind with
	 * @param parent
	 *            object that contains the property
	 * @param property
	 *            property path
	 * @return {@link PropertyModel} instance
	 */
	public static <Z> PropertyModelBinder<Z> of(final PropertyModel<Z> bindModel, final Object parent, final String property)
	{
		return new PropertyModelBinder<>(bindModel, parent, property);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String propertyExpression()
	{
		return expression;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append(":expression=[").append(expression).append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void setObject(T object)
	{
		super.setObject(object);
		this.bindModel.setObject(object);
	}
}

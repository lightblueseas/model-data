package de.alpharogroup.model;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.azeckoski.reflectutils.ReflectUtils;

import de.alpharogroup.model.api.Model;
import de.alpharogroup.model.api.ObjectClassAware;
import de.alpharogroup.model.api.PropertyReflectionAwareModel;

/**
 * The class {@link AbstractPropertyModel} serves as a base class for different kinds of property
 * models. By default, this class uses {@link PropertyUtils} to resolve expressions on the target
 * model object.
 *
 * @param <T>
 *            the generic type
 */
public abstract class AbstractPropertyModel<T> extends ChainingModel<T>
	implements
		ObjectClassAware<T>,
		PropertyReflectionAwareModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param modelObject
	 *            The nested model object
	 */
	public AbstractPropertyModel(final Object modelObject)
	{
		super(modelObject);
	}

	/**
	 * @return The innermost model or the object if the target is not a model
	 */
	public final Object getInnermostModelOrObject()
	{
		Object object = getTarget();
		while (object instanceof Model)
		{
			final Object tmp = ((Model<?>)object).getObject();
			if (tmp == object)
			{
				break;
			}
			object = tmp;
		}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getObject()
	{
		final String expression = propertyExpression();
		if (StringUtils.isEmpty(expression))
		{
			// Return a meaningful value for an empty property expression
			return (T)getInnermostModelOrObject();
		}
		else if (expression.startsWith("."))
		{
			throw new IllegalArgumentException(
				"Property expressions cannot start with a '.' character");
		}

		final Object target = getInnermostModelOrObject();
		if (target != null)
		{
			try
			{
				// return (T)PropertyResolver.getValue(expression, target);
				return (T)PropertyUtils.getProperty(target, expression);
			}
			catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	private T getProperty(final String expression, final Object target) {
		try {
			// return (T)PropertyResolver.getValue(expression, target);

			return (T) PropertyUtils.getProperty(target, expression);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		try {

			return (T) PropertyUtils.getNestedProperty(target, expression);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field field;
		try {
			field = target.getClass().getField(expression);
			return (T) field.get(target);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return (T) ReflectUtils.getInstance().getFieldValue(target, expression);
	}

	/**
	 * @return model object class
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getObjectClass()
	{
		final String expression = propertyExpression();
		final Object target = getInnermostModelOrObject();

		if (StringUtils.isEmpty(expression))
		{
			// Return a meaningful value for an empty property expression
			return (Class<T>)(target != null ? target.getClass() : null);
		}

		if (target != null)
		{
			try
			{
				return (Class<T>)PropertyUtils.getPropertyType(target, expression);
			}
			catch (final Exception e)
			{
				// ignore.
			}
		}
		// TODO see if this can be done with beanutils
		else if (getTarget() instanceof ObjectClassAware)
		{
			try
			{
				final Class<?> targetClass = ((ObjectClassAware<?>)getTarget()).getObjectClass();
				if (targetClass != null)
				{
					final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(expression,
						targetClass);
					return (Class<T>)propertyDescriptor.getPropertyType();
				}
			}
			catch (final IntrospectionException e)
			{
				// ignore.
			}

		}
		return null;
	}

	/**
	 * Gets the property expression for this model
	 *
	 * @return The property expression
	 */
	public final String getPropertyExpression()
	{
		return propertyExpression();
	}

	@Override
	public Field getPropertyField()
	{
		final String expression = propertyExpression();
		if (StringUtils.isEmpty(expression) == false)
		{
			final Object target = getInnermostModelOrObject();
			if (target != null)
			{
				try
				{
					return target.getClass().getDeclaredField(expression);
				}
				catch (final Exception ignore)
				{
					// ignore.
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getPropertyGetter()
	{
		final String expression = propertyExpression();
		if (StringUtils.isEmpty(expression) == false)
		{
			final Object target = getInnermostModelOrObject();
			if (target != null)
			{
				try
				{
					final Class<?> targetClass = target.getClass();
					final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(expression,
						targetClass);
					return propertyDescriptor.getReadMethod();
				}
				catch (final Exception ignore)
				{
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getPropertySetter()
	{
		final String expression = propertyExpression();
		if (StringUtils.isEmpty(expression) == false)
		{
			final Object target = getInnermostModelOrObject();
			if (target != null)
			{
				try
				{

					final Class<?> targetClass = target.getClass();
					final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(expression,
						targetClass);
					return propertyDescriptor.getWriteMethod();
				}
				catch (final Exception ignore)
				{
				}
			}
		}
		return null;
	}

	/**
	 * @return The property expression for the component
	 */
	protected abstract String propertyExpression();

	/**
	 * Applies the property expression on the model object using the given object argument.
	 *
	 * @param object
	 *            The object that will be used when setting a value on the model object
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setObject(final T object)
	{
		final String expression = propertyExpression();
		if (StringUtils.isEmpty(expression))
		{
			// TODO check, really do this?
			// why not just set the target to the object?
			final Object target = getTarget();
			if (target instanceof Model)
			{
				((Model<T>)target).setObject(object);
			}
			else
			{
				setTarget(object);
			}
		}
		else
		{
			final Object target = getInnermostModelOrObject();

			if (target != null)
			{
				try
				{
					PropertyUtils.setProperty(target, expression, object);
				}
				catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}
}

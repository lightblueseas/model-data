package de.alpharogroup.model.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The interface {@link PropertyReflectionAwareModel} provides reflection information about the model
 * object property.
 *
 * @param <T>
 *            the generic type of the model object
 */
public interface PropertyReflectionAwareModel<T> extends Model<T>
{

	/**
	 * Gets the field of model property or null if the field doesn't exist.
	 *
	 * @return the property field
	 */
	public Field getPropertyField();

	/**
	 * Gets the getter method of model property or null if the method doesn't exist.
	 *
	 * @return Method or null
	 */
	public Method getPropertyGetter();


	/**
	 * Gets the setter method of model property or null if the method doesn't exist.
	 *
	 * @return Method or null
	 */
	public Method getPropertySetter();
}

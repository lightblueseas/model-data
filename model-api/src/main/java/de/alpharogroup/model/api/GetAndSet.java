package de.alpharogroup.model.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author jcompagner
 */
public interface GetAndSet {
	/**
	 * @param object
	 *            The object where the value must be taken from.
	 *
	 * @return The value of this property
	 */
	public Object getValue(final Object object);

	/**
	 * @return The target class of the object that as to be set.
	 */
	public Class<?> getTargetClass();

	/**
	 * @param object
	 *            The object where the new value must be set on.
	 *
	 * @return The new value for the property that is set back on that object.
	 */
	public Object newValue(Object object);

	/**
	 * @param object
	 * @param value
	 * @param converter
	 */
	public void setValue(final Object object, final Object value);

	/**
	 * @return Field or null if there is no field
	 */
	public Field getField();

	/**
	 * @return Getter method or null if there is no getter
	 */
	public Method getGetter();

	/**
	 * @return Setter of null if there is no setter
	 */
	public Method getSetter();
}
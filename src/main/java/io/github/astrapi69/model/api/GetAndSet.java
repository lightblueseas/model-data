/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astrapi69.model.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * The interface {@link GetAndSet} provides methods to get and set values of an object in a model.
 * This inteface was previously in the PropertyResolver class.
 */
public interface GetAndSet
{
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

	/**
	 * @return The target class of the object that as to be set.
	 */
	public Class<?> getTargetClass();

	/**
	 * @param object
	 *            The object where the value must be taken from.
	 *
	 * @return The value of this property
	 */
	public Object getValue(final Object object);

	/**
	 * @param object
	 *            The object where the new value must be set on.
	 *
	 * @return The new value for the property that is set back on that object.
	 */
	public Object newValue(Object object);

	/**
	 * Sets the value.
	 *
	 * @param object
	 *            the object
	 * @param value
	 *            the value
	 */
	public void setValue(final Object object, final Object value);
}

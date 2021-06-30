/**
 * Copyright (C) 2015 Asterios Raptis
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
 * The interface {@link PropertyReflectionAwareModel} provides reflection information about the
 * model object property.
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

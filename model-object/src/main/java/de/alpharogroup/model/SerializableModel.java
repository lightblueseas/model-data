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
package de.alpharogroup.model;

import java.io.Serializable;

import io.github.astrapi69.model.api.Model;
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
	 * Set the model object; calls setObject(java.io.Serializable). The model object must be
	 * serializable, as it is stored in the session
	 *
	 * @param object
	 *            the model object
	 */
	@Override
	public void setObject(final T object)
	{
		super.setObject(object);
	}

}

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
package io.github.astrapi69.model;

import java.io.Serial;

import io.github.astrapi69.model.api.IModel;
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
	@Serial
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
	 * Factory methods for IModel which uses type inference to make code shorter. Equivalent to
	 * <code>new BaseModel&lt;TypeOfObject&gt;()</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @return IModel that contains <code>object</code>
	 */
	public static <T> IModel<T> of()
	{
		return new BaseModel<>();
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
	public static <T> IModel<T> of(final IModel<?> model)
	{
		return (IModel<T>)model;
	}

	/**
	 * Factory methods for IModel which uses type inference to make code shorter. Equivalent to
	 * <code>new BaseModel&lt;TypeOfObject&gt;(object)</code>.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @return IModel that contains <code>object</code>
	 */
	public static <T> IModel<T> of(final T object)
	{
		return new BaseModel<>(object);
	}


}

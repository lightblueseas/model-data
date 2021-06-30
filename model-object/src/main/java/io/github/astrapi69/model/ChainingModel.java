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
package io.github.astrapi69.model;

import io.github.astrapi69.model.api.Attachable;
import io.github.astrapi69.model.api.ChainableModel;
import io.github.astrapi69.model.api.Detachable;
import io.github.astrapi69.model.api.Model;

/**
 * The class {@link ChainingModel} is the default implementation of {@link ChainableModel}
 * interface.
 *
 * @param <T>
 *            the generic type of the model object
 *
 * @see AbstractPropertyModel
 */
public class ChainingModel<T> implements ChainableModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Any model object (which may or may not implement Model) */
	private Object target;

	/**
	 * Instantiates a new {@link ChainingModel}.
	 *
	 * @param target
	 *            the target object
	 */
	public ChainingModel(final Object target)
	{
		this.target = target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		if (target instanceof Attachable)
		{
			((Attachable)target).attach();
		}
	}

	/**
	 * Unsets this property model's instance variables and detaches the model.
	 */
	@Override
	public void detach()
	{
		// Detach nested object if it's a detachable
		if (target instanceof Detachable)
		{
			((Detachable)target).detach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model<?> getChainedModel()
	{
		if (target instanceof Model)
		{
			return (Model<?>)target;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChainedModel(final Model<?> model)
	{
		target = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getObject()
	{
		if (target instanceof Model)
		{
			return ((Model<T>)target).getObject();
		}
		return (T)target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setObject(final T object)
	{
		if (target instanceof Model)
		{
			((Model<T>)target).setObject(object);
		}
		else
		{
			target = object;
		}
	}

	/**
	 * @return The target - object or model
	 */
	protected final Object getTarget()
	{
		return target;
	}

	/**
	 * Sets a new target - object or model.
	 *
	 * @param modelObject
	 *            the model object
	 * @return this object
	 */
	protected final ChainingModel<T> setTarget(final Object modelObject)
	{
		this.target = modelObject;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("Model:classname=[");
		sb.append(getClass().getName()).append(']');
		sb.append(":nestedModel=[").append(target).append(']');
		return sb.toString();
	}

}

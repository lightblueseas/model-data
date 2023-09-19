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

import io.github.astrapi69.model.api.Attachable;
import io.github.astrapi69.model.api.IChainingModel;
import io.github.astrapi69.model.api.IDetachable;
import io.github.astrapi69.model.api.IModel;

/**
 * The class {@link ChainingModel} is the default implementation of {@link IChainingModel}
 * interface.
 *
 * @param <T>
 *            the generic type of the model object
 *
 * @see AbstractPropertyModel
 */
public class ChainingModel<T> implements IChainingModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Any model object (which may or may not implement IModel) */
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
		if (target instanceof IDetachable)
		{
			((IDetachable)target).detach();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IModel<?> getChainedModel()
	{
		if (target instanceof IModel)
		{
			return (IModel<?>)target;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChainedModel(final IModel<?> model)
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
		if (target instanceof IModel)
		{
			return ((IModel<T>)target).getObject();
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
		if (target instanceof IModel)
		{
			((IModel<T>)target).setObject(object);
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
		final StringBuilder sb = new StringBuilder("IModel:classname=[");
		sb.append(getClass().getName()).append(']');
		sb.append(":nestedModel=[").append(target).append(']');
		return sb.toString();
	}

	/**
	 * @return The innermost model or the object if the target is not a model
	 */
	public final Object getInnermostModelOrObject()
	{
		Object object = getTarget();
		while (object instanceof IModel)
		{
			Object tmp = ((IModel<?>)object).getObject();
			if (tmp == object)
			{
				break;
			}
			object = tmp;
		}
		return object;
	}

}

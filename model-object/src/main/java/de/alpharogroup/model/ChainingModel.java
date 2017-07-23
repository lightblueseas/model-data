/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alpharogroup.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.alpharogroup.model.api.Attachable;
import de.alpharogroup.model.api.ChainableModel;
import de.alpharogroup.model.api.Detachable;
import de.alpharogroup.model.api.Model;

/**
 * Default implementation of ChainingModel
 *
 * @param <T>
 *            The Model object type
 *
 * @see AbstractPropertyModel
 *
 * @since 6.0.0
 */
public class ChainingModel<T> implements ChainableModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	protected static final Logger LOG = LoggerFactory.getLogger(ChainingModel.class);

	/** Any model object (which may or may not implement Model) */
	private Object target;

	public ChainingModel(final Object modelObject)
	{
		target = modelObject;
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

	@Override
	public void attach()
	{
		if (target instanceof Attachable)
		{
			((Attachable)target).attach();
		}
	}

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

	@Override
	public Model<?> getChainedModel()
	{
		if (target instanceof Model)
		{
			return (Model<?>)target;
		}
		return null;
	}

	@Override
	public void setChainedModel(final Model<?> model)
	{
		target = model;
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
	 * @param modelObject the model object
	 * @return this object
	 */
	protected final ChainingModel<T> setTarget(final Object modelObject)
	{
		this.target = modelObject;
		return this;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("Model:classname=[");
		sb.append(getClass().getName()).append(']');
		sb.append(":nestedModel=[").append(target).append(']');
		return sb.toString();
	}
}

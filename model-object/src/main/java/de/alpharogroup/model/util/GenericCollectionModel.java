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
package de.alpharogroup.model.util;

import java.io.Serializable;

import de.alpharogroup.model.GenericModel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The class {@link GenericCollectionModel} is the base class for wild card collections that
 * contains object that do not appear to implement the {@link Serializable} interface.
 *
 * @param <T>
 *            the generic type of the model object
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class GenericCollectionModel<T> extends GenericModel<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new {@link GenericCollectionModel}.
	 *
	 * @param object
	 *            the object
	 */
	public GenericCollectionModel(T object)
	{
		super(object);
	}

	/**
	 * Creates a serializable version of the object. The object is usually a collection.
	 *
	 * @param object
	 *            the object
	 * @return serializable version of <code>object</code>
	 */
	protected abstract T newSerializableCollectionOf(T object);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObject(T object)
	{
		if (!(object instanceof Serializable))
		{
			object = newSerializableCollectionOf(object);
		}
		super.setObject(object);
	}

}

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

import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.model.api.SerializableBiConsumer;
import io.github.astrapi69.model.api.SerializableFunction;

/**
 * <code>LambdaBindingModel</code> is a basic implementation of an <code>IModel</code> that can bind
 * two model object together. It uses a serializable {@link java.util.function.Supplier} to get the
 * object and {@link java.util.function.Consumer} to set them.
 *
 * @param <T>
 *            The type of the IModel Object
 */
public abstract class LambdaBindingModel<T> implements IModel<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor hidden, instantiation is done using one of the factory methods
	 */
	private LambdaBindingModel()
	{
	}

	/**
	 * Create a {@link LambdaBindingModel} for a given source and target, that will be always up to
	 * date
	 *
	 * <br>
	 * <br>
	 * Usage:
	 *
	 * <pre>
	 * {@code
	 * 	LambdaModel.of(personSourceModel, personTargetModel, Person::getName, Person::setName)
	 * }
	 * </pre>
	 *
	 * The models will be detached automatically.
	 *
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param getter
	 *            used to get a value
	 * @param setter
	 *            used to set a value
	 *
	 * @param <X>
	 *            target model object type
	 * @param <T>
	 *            model object type
	 *
	 * @return model
	 */
	public static <X, T> IModel<T> of(final IModel<X> source, final IModel<X> target,
		final SerializableFunction<X, T> getter, final SerializableBiConsumer<X, T> setter)
	{
		return new LambdaBindingModel<T>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void attach()
			{
				source.attach();
				target.attach();
			}

			@Override
			public void detach()
			{
				source.detach();
				target.detach();
			}

			@Override
			public T getObject()
			{
				final X sourceObject = source.getObject();
				if (sourceObject == null)
				{
					return null;
				}
				return getter.apply(sourceObject);
			}

			@Override
			public void setObject(final T t)
			{
				final X sourceObject = source.getObject();
				if (sourceObject != null)
				{
					setter.accept(sourceObject, t);
				}
				final X targetObject = target.getObject();
				if (targetObject != null)
				{
					setter.accept(targetObject, t);
				}
			}
		};
	}

	/**
	 * Create a {@link LambdaBindingModel} for a given source and target model, that will be always
	 * up to date for the given property
	 *
	 * <br>
	 * <br>
	 * Usage:
	 *
	 * <pre>
	 * {@code
	 * 	LambdaModel.of(personSourceModel, companyTargetModel, Person::getName, Person::setName, Company::setName)
	 * }
	 * </pre>
	 *
	 * The models will be detached automatically.
	 *
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param sourceGetter
	 *            used to get the source value
	 * @param sourceSetter
	 *            used to set the source value
	 * @param targetGetter
	 *            used to get the target value
	 * @param targetSetter
	 *            used to set the target value
	 *
	 * @param <X>
	 *            source model object type
	 *
	 * @param <Y>
	 *            target model object type
	 * @param <T>
	 *            model object type
	 *
	 * @return model
	 */
	public static <X, Y, T> IModel<T> of(final IModel<X> source, final IModel<Y> target,
		final SerializableFunction<X, T> sourceGetter,
		final SerializableBiConsumer<X, T> sourceSetter,
		final SerializableFunction<Y, T> targetGetter,
		final SerializableBiConsumer<Y, T> targetSetter)
	{
		return new LambdaBindingModel<T>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void attach()
			{
				source.attach();
				target.attach();
			}

			@Override
			public void detach()
			{
				source.detach();
				target.detach();
			}

			@Override
			public T getObject()
			{
				final X sourceObject = source.getObject();
				final Y targetObject = target.getObject();
				if (sourceObject == null && targetObject == null)
				{
					return null;
				}
				if (sourceObject != null && targetObject != null)
				{
					T sourceApply = sourceGetter.apply(sourceObject);
					T targetApply = targetGetter.apply(targetObject);
					if (sourceApply != null && !sourceApply.equals(targetApply))
					{
						targetSetter.accept(targetObject, sourceApply);
					}
				}
				return sourceGetter.apply(sourceObject);
			}

			@Override
			public void setObject(final T t)
			{
				final X sourceObject = source.getObject();
				if (sourceObject != null)
				{
					sourceSetter.accept(sourceObject, t);
				}
				final Y targetObject = target.getObject();
				if (targetObject != null)
				{
					targetSetter.accept(targetObject, t);
				}
			}
		};
	}

	@Override
	public void setObject(final T t)
	{
		throw new UnsupportedOperationException("setObject(Object) not supported");
	}
}

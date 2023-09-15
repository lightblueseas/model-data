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

import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import io.github.astrapi69.model.api.IModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SimpleLambdaModel<T> implements IModel<T>
{

	SerializableSupplier<T> getter;
	SerializableConsumer<T> setter;

	/**
	 * Create a {@link LambdaModel}. Usage:
	 *
	 * <pre>
	 * {@code
	 * 	LambdaModel.of(person::getName, person::setName)
	 * }
	 * </pre>
	 *
	 * @param getter
	 *            used to get value
	 * @param setter
	 *            used to set value
	 * @return model
	 *
	 * @param <T>
	 *            model object type
	 */
	public static <T> IModel<T> of(final SerializableSupplier<T> getter,
		final SerializableConsumer<T> setter)
	{

		return new SimpleLambdaModel<>(getter, setter);
	}

	/**
	 * Create a {@link LambdaModel} for a given target. Usage:
	 *
	 * <pre>
	 * {@code
	 * 	LambdaModel.of(personModel, Person::getName)
	 * }
	 * </pre>
	 *
	 * The target model will be detached automatically.
	 *
	 * @param target
	 *            target for getter and setter
	 * @param getter
	 *            used to get a value
	 * @param <X>
	 *            target model object type
	 * @param <T>
	 *            model object type
	 *
	 * @return model
	 */
	public static <X, T> IModel<T> of(final IModel<X> target,
		final SerializableFunction<X, T> getter)
	{
		return new SimpleLambdaModel<>(null, null)
		{
			@Serial
			private static final long serialVersionUID = 1L;

			@Override
			public void attach()
			{
				target.attach();
			}

			@Override
			public void detach()
			{
				target.detach();
			}

			@Override
			public T getObject()
			{
				final X x = target.getObject();
				if (x == null)
				{
					return null;
				}
				return getter.apply(x);
			}

			@Override
			public void setObject(final T t)
			{
				throw new UnsupportedOperationException("setObject(Object) not supported");
			}
		};
	}

	/**
	 * Create a {@link LambdaModel} for a given target. Usage:
	 *
	 * <pre>
	 * {@code
	 * 	LambdaModel.of(personModel, Person::getName, Person::setName)
	 * }
	 * </pre>
	 *
	 * The target model will be detached automatically.
	 *
	 * @param target
	 *            target for getter and setter
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
	public static <X, T> IModel<T> of(final IModel<X> target,
		final SerializableFunction<X, T> getter, final SerializableBiConsumer<X, T> setter)
	{
		return new SimpleLambdaModel<>(null, null)
		{
			@Serial
			private static final long serialVersionUID = 1L;

			@Override
			public void attach()
			{
				target.attach();
			}

			@Override
			public void detach()
			{
				target.detach();
			}

			@Override
			public T getObject()
			{
				final X x = target.getObject();
				if (x == null)
				{
					return null;
				}
				return getter.apply(x);
			}

			@Override
			public void setObject(final T t)
			{
				final X x = target.getObject();
				if (x != null)
				{
					setter.accept(x, t);
				}
			}
		};
	}

	/**
	 * Attach an object.
	 */
	@Override
	public void attach()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Detach an object.
	 */
	@Override
	public void detach()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Gets the model object.
	 *
	 * @return The model object
	 */
	@Override
	public T getObject()
	{
		try
		{
			return getter.get();
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	/**
	 * Sets the model object.
	 *
	 * @param object
	 *            The model object
	 */
	@Override
	public void setObject(T object)
	{
		try
		{
			setter.accept(object);
		}
		catch (NullPointerException e)
		{
			throw new RuntimeException("Set property to a null object is not supported", e);
		}
	}
}

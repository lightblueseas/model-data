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

import java.util.Objects;

import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import io.github.astrapi69.model.LambdaModel;

/**
 * A {@link IModel} decorates the actual model object that can be used by any other UI-Component.
 * IModel implementations are used as a facade for the real model so that users have control over
 * the actual persistence strategy.
 *
 * @param <T>
 *            the generic type of the model object
 */
@FunctionalInterface
public interface IModel<T> extends Attachable, IDetachable
{
	/**
	 * Gets the model object.
	 *
	 * @return The model object
	 */
	T getObject();

	/**
	 * Sets the model object.
	 *
	 * @param object
	 *            The model object
	 * @throws UnsupportedOperationException
	 *             unless overridden
	 */
	default void setObject(final T object)
	{
		throw new UnsupportedOperationException(
			"Override this method to support setObject(Object)");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	default void attach()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	default void detach()
	{
	}

	/**
	 * Returns a IModel checking whether the predicate holds for the contained object, if it is not
	 * {@code null}. If the predicate doesn't evaluate to {@code true}, the contained object will be
	 * {@code null}.
	 *
	 * @param predicate
	 *            a predicate to be used for testing the contained object
	 * @return a new IModel
	 */
	default IModel<T> filter(SerializablePredicate<? super T> predicate)
	{
		Objects.nonNull(predicate);
		return new IModel<T>()
		{
			@Override
			public T getObject()
			{
				T object = IModel.this.getObject();
				if (object != null && predicate.test(object))
				{
					return object;
				}
				else
				{
					return null;
				}
			}

			@Override
			public void detach()
			{
				IModel.this.detach();
			}
		};
	}

	/**
	 * Returns a IModel applying the given mapper to the contained object, if it is not
	 * {@code null}.
	 *
	 * @param <R>
	 *            the new type of the contained object
	 * @param mapper
	 *            a mapper, to be applied to the contained object
	 * @return a new IModel
	 */
	default <R> IModel<R> map(SerializableFunction<? super T, R> mapper)
	{
		Objects.nonNull(mapper);
		return new IModel<R>()
		{
			@Override
			public R getObject()
			{
				T object = IModel.this.getObject();
				if (object == null)
				{
					return null;
				}
				else
				{
					return mapper.apply(object);
				}
			}

			@Override
			public void detach()
			{
				IModel.this.detach();
			}
		};
	}

	/**
	 * Returns a {@link IModel} applying the given combining function to the current model object
	 * and to the one from the other model, if they are not {@code null}.
	 *
	 * @param <R>
	 *            the resulting type
	 * @param <U>
	 *            the other models type
	 * @param other
	 *            another model to be combined with this one
	 * @param combiner
	 *            a function combining this and the others object to a result.
	 * @return a new IModel
	 */
	default <R, U> IModel<R> combineWith(IModel<U> other,
		SerializableBiFunction<? super T, ? super U, R> combiner)
	{
		Objects.nonNull(combiner);
		Objects.nonNull(other);
		return new IModel<R>()
		{
			@Override
			public R getObject()
			{
				T t = IModel.this.getObject();
				U u = other.getObject();
				if (t != null && u != null)
				{
					return combiner.apply(t, u);
				}
				else
				{
					return null;
				}
			}

			@Override
			public void detach()
			{
				other.detach();
				IModel.this.detach();
			}
		};
	}

	/**
	 * Returns a IModel applying the given IModel-bearing mapper to the contained object, if it is
	 * not {@code null}.
	 *
	 * @param <R>
	 *            the new type of the contained object
	 * @param mapper
	 *            a mapper, to be applied to the contained object
	 * @return a new IModel
	 * @see LambdaModel#of(IModel, SerializableFunction, SerializableBiConsumer)
	 */
	default <R> IModel<R> flatMap(SerializableFunction<? super T, IModel<R>> mapper)
	{
		Objects.nonNull(mapper);
		return new IModel<R>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public R getObject()
			{
				T object = IModel.this.getObject();
				if (object != null)
				{
					IModel<R> model = mapper.apply(object);
					if (model != null)
					{
						return model.getObject();
					}
					else
					{
						return null;
					}
				}
				else
				{
					return null;
				}
			}

			@Override
			public void setObject(R object)
			{
				T modelObject = IModel.this.getObject();
				if (modelObject != null)
				{
					IModel<R> model = mapper.apply(modelObject);
					if (model != null)
					{
						model.setObject(object);
					}
				}
			}

			@Override
			public void detach()
			{
				T object = IModel.this.getObject();
				IModel.this.detach();
				if (object != null)
				{
					IModel<R> model = mapper.apply(object);
					if (model != null)
					{
						model.detach();
					}
				}
			}
		};
	}

	/**
	 * Returns a IModel, returning either the contained object or the given default value, depending
	 * on the {@code null}ness of the contained object.
	 *
	 * <p>
	 * Possible usages:
	 * <ul>
	 * <li>{@code myComponent = new AnyComponent(&quot;someId&quot;, someModel.orElse(defaultValue));}
	 * - This way Wicket will make use of the default value if the model object of
	 * <em>someModel</em> is {@code null}.</li>
	 * <li>in the middle of the application logic:
	 * {@code ... = someModel.orElse(default).getModelObject();}</li>
	 * </ul>
	 *
	 * </p>
	 *
	 * @param other
	 *            a default value
	 * @return a new IModel
	 */
	default IModel<T> orElse(T other)
	{
		return new IModel<T>()
		{
			@Override
			public T getObject()
			{
				T object = IModel.this.getObject();
				if (object == null)
				{
					return other;
				}
				else
				{
					return object;
				}
			}

			@Override
			public void detach()
			{
				IModel.this.detach();
			}
		};
	}

	/**
	 * Returns a IModel, returning either the contained object or invoking the given supplier to get
	 * a default value.
	 *
	 * @param other
	 *            a supplier to be used as a default
	 * @return a new IModel
	 */
	default IModel<T> orElseGet(SerializableSupplier<? extends T> other)
	{
		Objects.nonNull(other);
		return new IModel<T>()
		{
			@Override
			public T getObject()
			{
				T object = IModel.this.getObject();
				if (object == null)
				{
					return other.get();
				}
				else
				{
					return object;
				}
			}

			@Override
			public void detach()
			{
				IModel.this.detach();
			}
		};
	}

	/**
	 * Returns a IModel, returning whether the contained object is non-null.
	 *
	 * @return a new IModel
	 */
	default IModel<Boolean> isPresent()
	{
		return new IModel<Boolean>()
		{
			@Override
			public Boolean getObject()
			{
				return IModel.this.getObject() != null;
			}

			@Override
			public void detach()
			{
				IModel.this.detach();
			}
		};
	}

	/**
	 * Suppresses generics warning when casting model types.
	 *
	 * @param <T>
	 * @param model
	 * @return cast <code>model</code>
	 */
	@SuppressWarnings("unchecked")
	static <T> IModel<T> of(IModel<?> model)
	{
		return (IModel<T>)model;
	}
}

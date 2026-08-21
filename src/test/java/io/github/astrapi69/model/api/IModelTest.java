/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.astrapi69.model.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.model.BaseModel;
import io.github.astrapi69.model.LambdaModel;
import io.github.astrapi69.model.lambda.Person;

/**
 * Test class for the default methods of {@link IModel}
 */
public class IModelTest
{

	/**
	 * Test method for
	 * {@link IModel#filter(org.danekja.java.util.function.serializable.SerializablePredicate)}
	 */
	@Test
	public void testFilter()
	{
		IModel<String> model = BaseModel.of("foo");

		assertEquals("foo", model.filter(value -> value.startsWith("f")).getObject());
		assertNull(model.filter(value -> value.startsWith("x")).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#filter(org.danekja.java.util.function.serializable.SerializablePredicate)} with
	 * a null model object; the predicate must not be evaluated
	 */
	@Test
	public void testFilterWithNullObject()
	{
		IModel<String> model = BaseModel.of();

		assertNull(model.filter(value -> {
			throw new IllegalStateException("predicate must not be called for null objects");
		}).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#filter(org.danekja.java.util.function.serializable.SerializablePredicate)} with
	 * a null predicate
	 */
	@Test
	public void testFilterWithNullPredicate()
	{
		IModel<String> model = BaseModel.of("foo");

		assertThrows(NullPointerException.class, () -> model.filter(null));
	}

	/**
	 * Test method for
	 * {@link IModel#map(org.danekja.java.util.function.serializable.SerializableFunction)}
	 */
	@Test
	public void testMap()
	{
		Person person = new Person();
		person.setName("foo");
		IModel<Person> model = BaseModel.of(person);

		assertEquals("foo", model.map(Person::getName).getObject());
		// chained map
		assertEquals(3, model.map(Person::getName).map(String::length).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#map(org.danekja.java.util.function.serializable.SerializableFunction)} with a
	 * null model object; the mapper must not be applied
	 */
	@Test
	public void testMapWithNullObject()
	{
		IModel<Person> model = BaseModel.of();

		assertNull(model.map(Person::getName).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#map(org.danekja.java.util.function.serializable.SerializableFunction)} with a
	 * null mapper
	 */
	@Test
	public void testMapWithNullMapper()
	{
		IModel<String> model = BaseModel.of("foo");

		assertThrows(NullPointerException.class, () -> model.map(null));
	}

	/**
	 * Test method for
	 * {@link IModel#combineWith(IModel, org.danekja.java.util.function.serializable.SerializableBiFunction)}
	 */
	@Test
	public void testCombineWith()
	{
		IModel<String> left = BaseModel.of("Hello");
		IModel<String> right = BaseModel.of("World");

		IModel<String> combined = left.combineWith(right, (l, r) -> l + " " + r);

		assertEquals("Hello World", combined.getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#combineWith(IModel, org.danekja.java.util.function.serializable.SerializableBiFunction)}
	 * with a null value on either side; the combiner must not be applied
	 */
	@Test
	public void testCombineWithNullObject()
	{
		IModel<String> value = BaseModel.of("Hello");
		IModel<String> nullValue = BaseModel.of();

		assertNull(value.combineWith(nullValue, (l, r) -> l + " " + r).getObject());
		assertNull(nullValue.combineWith(value, (l, r) -> l + " " + r).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#combineWith(IModel, org.danekja.java.util.function.serializable.SerializableBiFunction)}
	 * with null arguments
	 */
	@Test
	public void testCombineWithNullArguments()
	{
		IModel<String> model = BaseModel.of("Hello");

		assertThrows(NullPointerException.class, () -> model.combineWith(null, (l, r) -> l));
		assertThrows(NullPointerException.class,
			() -> model.combineWith(BaseModel.of("World"), null));
	}

	/**
	 * Test method for
	 * {@link IModel#flatMap(org.danekja.java.util.function.serializable.SerializableFunction)}
	 */
	@Test
	public void testFlatMap()
	{
		Person person = new Person();
		person.setName("foo");
		IModel<Person> model = BaseModel.of(person);

		IModel<String> nameModel = model.flatMap(p -> LambdaModel.of(p::getName, p::setName));

		assertEquals("foo", nameModel.getObject());
		// setObject is propagated through the mapped model to the bean
		nameModel.setObject("bar");
		assertEquals("bar", person.getName());
	}

	/**
	 * Test method for
	 * {@link IModel#flatMap(org.danekja.java.util.function.serializable.SerializableFunction)} with
	 * a null model object
	 */
	@Test
	public void testFlatMapWithNullObject()
	{
		IModel<Person> model = BaseModel.of();

		assertNull(model.flatMap(p -> LambdaModel.of(p::getName, p::setName)).getObject());
	}

	/**
	 * Test method for {@link IModel#as(Class)}
	 */
	@Test
	public void testAs()
	{
		IModel<Object> model = BaseModel.of((Object)"foo");

		IModel<String> stringModel = model.as(String.class);

		assertEquals("foo", stringModel.getObject());
	}

	/**
	 * Test method for {@link IModel#as(Class)} with an object that is not an instance of the given
	 * type
	 */
	@Test
	public void testAsNoInstance()
	{
		IModel<Object> model = BaseModel.of((Object)Integer.valueOf(1));

		IModel<String> stringModel = model.as(String.class);

		assertNull(stringModel.getObject());
	}

	/**
	 * Test method for {@link IModel#as(Class)} with a null model object
	 */
	@Test
	public void testAsWithNullObject()
	{
		IModel<Object> model = BaseModel.of();

		assertNull(model.as(String.class).getObject());
	}

	/**
	 * Test method for {@link IModel#as(Class)} with a null class argument
	 */
	@Test
	public void testAsWithNullClass()
	{
		IModel<Object> model = BaseModel.of((Object)"foo");

		assertThrows(NullPointerException.class, () -> model.as(null));
	}

	/**
	 * Test method for {@link IModel#orElse(Object)}
	 */
	@Test
	public void testOrElse()
	{
		IModel<String> nullModel = BaseModel.of();
		IModel<String> valueModel = BaseModel.of("value");

		assertEquals("default", nullModel.orElse("default").getObject());
		assertEquals("value", valueModel.orElse("default").getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#orElseGet(org.danekja.java.util.function.serializable.SerializableSupplier)}
	 */
	@Test
	public void testOrElseGet()
	{
		IModel<String> nullModel = BaseModel.of();
		IModel<String> valueModel = BaseModel.of("value");

		assertEquals("default", nullModel.orElseGet(() -> "default").getObject());
		// the supplier must not be called if the object is present
		assertEquals("value", valueModel.orElseGet(() -> {
			throw new IllegalStateException("supplier must not be called for present objects");
		}).getObject());
	}

	/**
	 * Test method for
	 * {@link IModel#orElseGet(org.danekja.java.util.function.serializable.SerializableSupplier)}
	 * with a null supplier
	 */
	@Test
	public void testOrElseGetWithNullSupplier()
	{
		IModel<String> model = BaseModel.of();

		assertThrows(NullPointerException.class, () -> model.orElseGet(null));
	}

	/**
	 * Test method for {@link IModel#isPresent()}
	 */
	@Test
	public void testIsPresent()
	{
		assertTrue(BaseModel.of("value").isPresent().getObject());
		assertFalse(BaseModel.<String> of().isPresent().getObject());
	}

	/**
	 * Test method for {@link IModel#of(IModel)}
	 */
	@Test
	public void testStaticOf()
	{
		IModel<String> model = BaseModel.of("foo");

		IModel<CharSequence> cast = IModel.of(model);

		assertSame(model, cast);
		assertEquals("foo", cast.getObject());
	}

	/**
	 * Test method for the default implementation of {@link IModel#setObject(Object)}
	 */
	@Test
	public void testDefaultSetObjectThrows()
	{
		IModel<String> readOnly = () -> "foo";

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> readOnly.setObject("bar"));
		assertInstanceOf(UnsupportedOperationException.class, exception);
	}

	/**
	 * Test method for the default implementations of {@link IModel#attach()} and
	 * {@link IModel#detach()}; both are no-ops and must not fail
	 */
	@Test
	public void testDefaultAttachDetachAreNoOps()
	{
		IModel<String> model = () -> "foo";

		model.attach();
		model.detach();
		assertEquals("foo", model.getObject());
	}

	/**
	 * Test method for detach propagation through the models created by the default methods
	 */
	@Test
	public void testDetachPropagation()
	{
		final boolean[] detached = { false };
		IModel<String> model = new IModel<String>()
		{
			@Override
			public String getObject()
			{
				return "foo";
			}

			@Override
			public void detach()
			{
				detached[0] = true;
			}
		};

		model.map(String::length).detach();
		assertTrue(detached[0]);

		detached[0] = false;
		model.filter(value -> true).detach();
		assertTrue(detached[0]);
	}
}

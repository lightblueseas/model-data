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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.model.lambda.Person;

/**
 * Tests for {@link LambdaModel}
 */
@SuppressWarnings("javadoc")
public class LambdaModelTest
{
	private void check(final IModel<String> personNameModel)
	{
		assertThat(personNameModel.getObject(), is(nullValue()));

		final String personName = "new name";
		personNameModel.setObject(personName);
		assertThat(personNameModel.getObject(), is(personName));

		serialize(personNameModel, personName);
	}

	@Test
	public void explicitLambdas()
	{
		final Person person = new Person();
		final IModel<String> personNameModel = LambdaModel.<String> of(() -> person.getName(),
			(name) -> person.setName(name));
		check(personNameModel);
		person.setName("foo");
		String object = personNameModel.getObject();
		assertThat(personNameModel.getObject(), is(object));
	}

	@Test
	public void methodReference()
	{
		final Person person = new Person();
		final IModel<String> personNameModel = LambdaModel.of(person::getName, person::setName);
		check(personNameModel);
	}

	private void serialize(final IModel<String> personNameModel, final String personName)
	{
		assertThat(personNameModel, is(instanceOf(LambdaModel.class)));
		assertThat(personNameModel.getObject(), is(personName));
	}

	@Test
	public void targetModel()
	{
		String actual;
		String expected;
		String currentValue;
		Person person = new Person();
		final IModel<Person> target = SerializableModel.of(person);

		final IModel<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);
		check(personNameModel);
		// set value over the bean and check model
		currentValue = "foo";
		person.setName(currentValue);
		actual = personNameModel.getObject();
		expected = currentValue;
		assertEquals(actual, expected);
		// new scenario
		// set value over the model and check bean
		currentValue = "bar";
		personNameModel.setObject(currentValue);
		actual = person.getName();
		expected = currentValue;
		assertEquals(actual, expected);
	}

	@Test
	public void targetModelNull()
	{
		final IModel<Person> target = SerializableModel.of((Person)null);

		final IModel<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);

		personNameModel.setObject("new name");
		assertThat(personNameModel.getObject(), is(nullValue()));
	}

	@Test
	public void targetModelPerson()
	{
		final IModel<Person> target = SerializableModel.of(new Person());

		final IModel<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);

		personNameModel.setObject("new name");
		assertThat(personNameModel.getObject(), is("new name"));
	}

	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void targetReadOnly()
	{
		final IModel<Person> target = SerializableModel.of(new Person());

		final IModel<String> personNameModel = LambdaModel.of(target, Person::getName);
		check(personNameModel);
	}


	/**
	 * Test method for {@link SimpleLambdaModel#getObject()} and
	 * {@link SimpleLambdaModel#setObject(Object)}
	 */
	@Test
	public void testLambdas()
	{
		String actual;
		String expected;
		String currentValue;
		Person person;
		IModel<String> personNameModel;
		// initialize test objects
		person = new Person();
		// personNameModel = LambdaModel.of(() -> person.getName(), (name) -> person.setName(name));
		personNameModel = LambdaModel.of(person::getName, person::setName);
		// new scenario
		// set value over the bean and check model
		currentValue = "foo";
		person.setName(currentValue);
		actual = personNameModel.getObject();
		expected = currentValue;
		assertEquals(actual, expected);
		// new scenario
		// set value over the model and check bean
		currentValue = "bar";
		personNameModel.setObject(currentValue);
		actual = person.getName();
		expected = currentValue;
		assertEquals(actual, expected);
	}


	/**
	 * Test method for {@link SimpleLambdaModel#getObject()} and
	 * {@link SimpleLambdaModel#setObject(Object)}
	 */
	@Test
	public void testLambdasWithLambdas()
	{
		String actual;
		String expected;
		String currentValue;
		Person person;
		Person otherPerson;
		IModel<String> personNameModel;
		IModel<String> otherPersonNameModel;
		// initialize test objects
		person = new Person();
		otherPerson = new Person();
		personNameModel = LambdaModel.of(person::getName, person::setName);
		otherPersonNameModel = LambdaModel.of(otherPerson::getName, otherPerson::setName);
		LambdaModel.of(personNameModel::getObject, otherPersonNameModel::setObject);
		LambdaModel.of(otherPersonNameModel::getObject, personNameModel::setObject);
		// new scenario
		// set value over the bean and check model
		currentValue = "foo";
		person.setName(currentValue);
		actual = personNameModel.getObject();
		expected = currentValue;
		assertEquals(actual, expected);
		// new scenario
		// set value over the model and check bean
		currentValue = "bar";
		personNameModel.setObject(currentValue);
		actual = person.getName();
		expected = currentValue;
		assertEquals(actual, expected);
	}
}

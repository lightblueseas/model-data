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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.testng.annotations.Test;

import io.github.astrapi69.model.api.Model;
import io.github.astrapi69.model.lambda.Person;

/**
 * Tests for {@link LambdaModel}
 */
@SuppressWarnings("javadoc")
public class LambdaModelTest
{
	private void check(final Model<String> personNameModel)
		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
		ClassNotFoundException, InstantiationException, IOException
	{
		assertThat(personNameModel.getObject(), is(nullValue()));

		final String personName = "new name";
		personNameModel.setObject(personName);
		assertThat(personNameModel.getObject(), is(personName));

		serialize(personNameModel, personName);
	}

	@Test
	public void explicitLambdas() throws NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		final Person person = new Person();
		final Model<String> personNameModel = LambdaModel.<String> of(() -> person.getName(),
			(name) -> person.setName(name));
		check(personNameModel);
	}

	@Test
	public void methodReference() throws NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		final Person person = new Person();
		final Model<String> personNameModel = LambdaModel.of(person::getName, person::setName);
		check(personNameModel);
	}

	private void serialize(final Model<String> personNameModel, final String personName)
	{
		assertThat(personNameModel, is(instanceOf(LambdaModel.class)));
		assertThat(personNameModel.getObject(), is(personName));
	}

	@Test
	public void targetModel() throws NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		final Model<Person> target = SerializableModel.of(new Person());

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);
		check(personNameModel);
	}

	@Test
	public void targetModelNull()
	{
		final Model<Person> target = SerializableModel.of((Person)null);

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);

		personNameModel.setObject("new name");
		assertThat(personNameModel.getObject(), is(nullValue()));
	}

	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void targetReadOnly() throws NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		final Model<Person> target = SerializableModel.of(new Person());

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName);
		check(personNameModel);
	}
}

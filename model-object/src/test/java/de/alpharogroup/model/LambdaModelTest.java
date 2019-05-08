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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import de.alpharogroup.clone.object.CloneObjectExtensions;
import de.alpharogroup.model.api.Model;
import de.alpharogroup.model.lambda.Person;

/**
 * Tests for {@link LambdaModel}
 */
@SuppressWarnings("javadoc")
public class LambdaModelTest
{
	private void check(final Model<String> personNameModel)
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		assertThat(personNameModel.getObject(), is(nullValue()));

		final String personName = "new name";
		personNameModel.setObject(personName);
		assertThat(personNameModel.getObject(), is(personName));

		serialize(personNameModel, personName);
	}

	@Test
	public void explicitLambdas()
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException

	{
		final Person person = new Person();
		final Model<String> personNameModel = LambdaModel.<String> of(() -> person.getName(),
			(name) -> person.setName(name));
		check(personNameModel);
	}

	@Test
	public void methodReference()
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException

	{
		final Person person = new Person();
		final Model<String> personNameModel = LambdaModel.of(person::getName, person::setName);
		check(personNameModel);
	}

	private void serialize(final Model<String> personNameModel, final String personName)
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException
	{
		final Model<String> clone = CloneObjectExtensions.clone(personNameModel);
		assertThat(clone, is(instanceOf(LambdaModel.class)));
		assertThat(clone.getObject(), is(personName));
	}

	@Test
	public void targetModel()
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException

	{
		final Model<Person> target = SerializableModel.of(new Person());

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);
		check(personNameModel);
	}

	@Test
	public void targetModelNull()
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException

	{
		final Model<Person> target = SerializableModel.of((Person)null);

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName,
			Person::setName);

		personNameModel.setObject("new name");
		assertThat(personNameModel.getObject(), is(nullValue()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void targetReadOnly()
		throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, ClassNotFoundException, InstantiationException, IOException

	{
		final Model<Person> target = SerializableModel.of(new Person());

		final Model<String> personNameModel = LambdaModel.of(target, Person::getName);
		check(personNameModel);
	}
}

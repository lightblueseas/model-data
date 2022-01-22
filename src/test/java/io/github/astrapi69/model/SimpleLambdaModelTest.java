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

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import io.github.astrapi69.model.api.Model;
import io.github.astrapi69.test.objects.Person;

/**
 * The unit test class for the class {@link SimpleLambdaModel}
 */
public class SimpleLambdaModelTest
{
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
		Model<String> personNameModel;
		// initialize test objects
		person = new Person();
		personNameModel = new SimpleLambdaModel<String>(() -> person.getName(),
			(name) -> person.setName(name));
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

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

import io.github.astrapi69.model.api.IModel;
import org.testng.annotations.Test;

import io.github.astrapi69.test.object.Company;
import io.github.astrapi69.test.object.Person;
import io.github.astrapi69.test.object.enumtype.Gender;

public class LambdaBindingModelTest
{

	@Test
	public void testOf()
	{
		String actual;
		String expected;
		Person person;
		Person otherPerson;
		IModel<Person> personModel;
		IModel<Person> otherPersonModel;
		// new scenario
		person = Person.builder().name("Joe").gender(Gender.MALE).build();
		otherPerson = Person.builder().build();

		personModel = SerializableModel.of(person);
		otherPersonModel = SerializableModel.of(otherPerson);

		IModel<String> nameModel = LambdaBindingModel.of(personModel, otherPersonModel,
			Person::getName, Person::setName);
		expected = "new name";
		nameModel.setObject(expected);
		actual = person.getName();
		assertEquals(actual, expected);
		actual = otherPerson.getName();
		assertEquals(actual, expected);
	}

	@Test
	public void testOfPersonAndCompany()
	{
		String actual;
		String expected;
		Person person;
		Company company;
		IModel<Person> personModel;
		IModel<Company> companyModel;
		// new scenario
		person = Person.builder().name("Joe").gender(Gender.MALE).build();
		company = Company.builder().build();

		personModel = SerializableModel.of(person);
		companyModel = SerializableModel.of(company);

		IModel<String> nameModel = LambdaBindingModel.of(personModel, companyModel, Person::getName,
			Person::setName, Company::getName, Company::setName);
		expected = "new name";
		nameModel.setObject(expected);
		actual = person.getName();
		assertEquals(actual, expected);
		actual = company.getName();
		assertEquals(actual, expected);
		expected = "foo";
		personModel.getObject().setName(expected);
		// call getObject for update both models
		nameModel.getObject();
		actual = company.getName();
		assertEquals(actual, expected);
	}

}

package io.github.astrapi69.model;

import io.github.astrapi69.model.api.Model;
import io.github.astrapi69.test.objects.Company;
import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.test.objects.enums.Gender;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class LambdaBindingModelTest
{

	@Test public void testOf()
	{
		String actual;
		String expected;
		Person person;
		Person otherPerson;
		Model<Person> personModel;
		Model<Person> otherPersonModel;
		// new scenario
		person = Person.builder().name("Joe").gender(Gender.MALE).build();
		otherPerson = Person.builder().build();

		personModel = SerializableModel.of(person);
		otherPersonModel = SerializableModel.of(otherPerson);

		Model<String> nameModel = LambdaBindingModel.of(
			personModel,
			otherPersonModel,
			Person::getName,
			Person::setName);
		expected = "new name";
		nameModel.setObject(expected);
		actual = person.getName();
		assertEquals(actual, expected);
		actual = otherPerson.getName();
		assertEquals(actual, expected);
	}

	@Test public void testOfPersonAndCompany()
	{
		String actual;
		String expected;
		Person person;
		Company company;
		Model<Person> personModel;
		Model<Company> companyModel;
		// new scenario
		person = Person.builder().name("Joe").gender(Gender.MALE).build();
		company = Company.builder().build();

		personModel = SerializableModel.of(person);
		companyModel = SerializableModel.of(company);

		Model<String> nameModel = LambdaBindingModel.of(
			personModel,
			companyModel,
			Person::getName,
			Person::setName,
			Company::setName);
		expected = "new name";
		nameModel.setObject(expected);
		actual = person.getName();
		assertEquals(actual, expected);
		actual = company.getName();
		assertEquals(actual, expected);
	}

}

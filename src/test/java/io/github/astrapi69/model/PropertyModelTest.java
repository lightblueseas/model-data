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
import io.github.astrapi69.test.objects.Employee;
import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.test.objects.enums.Gender;

public class PropertyModelTest
{

	/**
	 * Tests setting a value on a {@link PropertyModel} when a property is <code>null</code> and an
	 * abstract class type. This should end in an exception because Wicket can't decide what to
	 * instantiate on behalf of the program.
	 */
	@Test(expectedExceptions = RuntimeException.class)
	public void setWithNullPathAbstract()
	{
		final Person person = new Person();
		final PropertyModel<String> model = new PropertyModel<>(person, "abstractAddress.street");
		model.setObject("foo");
	}

	@Test
	public void testSetInnerProperty()
	{
		String actual;
		String expected;
		PropertyModel<String> model;
		Employee employee;
		// new scenario with object
		employee = Employee.builder().person(Person.builder().name("bar").build()).build();
		// create PropertyModel
		model = new PropertyModel<>(employee, "person.name");
		// and set value persons name from employee over the model
		model.setObject("foo");
		// prove it
		assertEquals(employee.getPerson().getName(), model.getObject());

		// new scenario with model object
		Model<Employee> employeeModel = BaseModel.of(employee);
		model = new PropertyModel<>(employeeModel, "person.name");
		model.setObject("foo");
		assertEquals(employee.getPerson().getName(), model.getObject());

		employee.getPerson().setName("bar");
		assertEquals(employee.getPerson().getName(), model.getObject());
	}


	@Test
	public void testPropertyModelSetGender()
	{
		Gender actual;
		Gender expected;
		Employee employeeJoe;
		PropertyModel<Gender> modelJoeGender;
		// new scenario
		employeeJoe = Employee.builder()
			.person(Person.builder().name("Joe").gender(Gender.MALE).build()).build();

		modelJoeGender = new PropertyModel<>(employeeJoe, "person.gender");
		modelJoeGender.setObject(Gender.MALE);
		expected = employeeJoe.getPerson().getGender();
		actual = modelJoeGender.getObject();
		assertEquals(expected, actual);
	}

	@Test
	public void testPropertyModelSetMarried()
	{
		boolean actual;
		boolean expected;
		Employee employeeJoe;
		PropertyModel<Boolean> modelJoeMarried;
		// new scenario
		employeeJoe = Employee.builder()
			.person(Person.builder().name("Joe").gender(Gender.MALE).build()).build();
		modelJoeMarried = PropertyModel.of(employeeJoe, "person.married");
		modelJoeMarried.setObject(true);

		expected = employeeJoe.getPerson().getMarried();
		actual = modelJoeMarried.getObject();
		assertEquals(expected, actual);
	}

	@Test
	public void testPropertyModelSetOther()
	{
		Gender actual;
		Gender expected;
		Employee employeeJoe;
		Employee employeeCeo;
		PropertyModel<Gender> modelJoeGender;
		PropertyModel<Gender> modelCeoGender;
		// new scenario
		employeeJoe = Employee.builder()
			.person(Person.builder().name("Joe").gender(Gender.MALE).build()).build();


		modelJoeGender = new PropertyModel<>(employeeJoe, "person.gender");
		modelJoeGender.setObject(Gender.MALE);
		expected = employeeJoe.getPerson().getGender();
		actual = modelJoeGender.getObject();
		assertEquals(expected, actual);


		employeeCeo = Employee.builder().person(Person.builder().build()).build();
		modelCeoGender = new PropertyModel<>(employeeCeo, "person.gender");
		PropertyModel<Gender> bindModel = new PropertyModel<>(modelJoeGender, "person.gender");
	}
}

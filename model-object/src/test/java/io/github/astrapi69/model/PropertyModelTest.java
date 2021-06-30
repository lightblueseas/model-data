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
		final Employee employee = Employee.builder().person(Person.builder().build()).build();
		PropertyModel<String> model = new PropertyModel<>(employee, "person.name");
		model.setObject("foo");
		assertEquals(employee.getPerson().getName(), model.getObject());

		Model<Employee> employeeModel = BaseModel.of(employee);
		model = new PropertyModel<>(employeeModel, "person.name");
		model.setObject("foo");
		assertEquals(employee.getPerson().getName(), model.getObject());
	}

}

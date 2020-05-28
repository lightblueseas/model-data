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
package de.alpharogroup.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

public class GenericModelTest
{

	@Test
	public void testSetInnerProperty()
	{
		final Employee employee = Employee.builder().person(Person.builder().name("bar").build())
			.build();
		final PropertyModel<String> model = new PropertyModel<>(employee, "person.name");
		model.setObject("foo");
		String expected = employee.getPerson().getName();
		String actual = model.getObject();
		assertEquals(expected, actual);
	}

}

package de.alpharogroup.model;


import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

public class GenericModelTest
{

	@Test
	public void testSetInnerProperty()
	{
		final Employee employee = Employee.builder().person(Person.builder().build()).build();
		final PropertyModel<String> model = new PropertyModel<>(employee, "person.name");
		model.setObject("foo");
		AssertJUnit.assertEquals(employee.getPerson().getName(), model.getObject());
	}

}

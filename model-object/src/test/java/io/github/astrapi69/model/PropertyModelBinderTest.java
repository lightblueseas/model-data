package io.github.astrapi69.model;

import io.github.astrapi69.test.objects.Employee;
import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.test.objects.enums.Gender;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class PropertyModelBinderTest
{

	@Test
	public void testCombinePropertyModel()
	{
		final Employee employeeJoe = Employee.builder().person(Person.builder()
			.name("Joe")
			.gender(Gender.MALE)
			.build()).build();

		final Employee employeeMaria = Employee.builder().person(Person.builder()
			.name("Maria")
			.gender(Gender.MALE)
			.build()).build();

		PropertyModel<Boolean> modelJoe = new PropertyModel<Boolean>(employeeJoe, "person.married");

		PropertyModelBinder<Boolean> employeeJoeMariaModel = PropertyModelBinder.of(modelJoe, employeeMaria, "person.married");
		employeeJoeMariaModel.setObject(true);

		Boolean joeMarried = employeeJoe.getPerson().getMarried();
		Boolean mariaMarried = employeeMaria.getPerson().getMarried();
		assertEquals(joeMarried, mariaMarried);
		// set the master bean
		employeeMaria.getPerson().setMarried(true);
		joeMarried = employeeJoe.getPerson().getMarried();
		mariaMarried = employeeMaria.getPerson().getMarried();
		assertEquals(joeMarried, mariaMarried);
		// set the slave bean does not change the master bean
		modelJoe.setObject(false);
		joeMarried = employeeJoe.getPerson().getMarried();
		assertFalse(joeMarried);
		mariaMarried = employeeMaria.getPerson().getMarried();
		assertTrue(mariaMarried);
	}
}

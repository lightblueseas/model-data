package de.alpharogroup.model;

import org.junit.Test;
import org.testng.AssertJUnit;

import de.alpharogroup.model.api.Model;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

public class PropertyModelTest
{

	/**
	 * Tests setting a value on a {@link PropertyModel} when a property is <code>null</code> and an
	 * abstract class type. This should end in an exception because Wicket can't decide what to
	 * instantiate on behalf of the program.
	 */
	@Test(expected = RuntimeException.class)
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
		AssertJUnit.assertEquals(employee.getPerson().getName(), model.getObject());

		Model<Employee> employeeModel = BaseModel.of(employee);
		model = new PropertyModel<>(employeeModel, "person.name");
		model.setObject("foo");
		AssertJUnit.assertEquals(employee.getPerson().getName(), model.getObject());
	}

}

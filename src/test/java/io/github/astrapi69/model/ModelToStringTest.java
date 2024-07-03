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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;

import org.junit.jupiter.api.Test;


/**
 * Tests the toString() method on the models in the io.github.astrapi69.model package.
 */
public class ModelToStringTest
{
	/**
	 * Tests LoadableDetachableModel.toString()
	 */
	@Test
	public void loadableDetachableModel()
	{
		String actual;
		String expected;
		LoadableDetachableModel<String> model;
		model = new MyLoadableDetachableModel();
		actual = model.toString();
		assertTrue(actual.contains(":attached=false"));
		assertTrue(actual.contains(":tempModelObject=[null]"));

		model.getObject();
		actual = model.toString();
		assertTrue(actual.contains(":attached=true"));
		assertTrue(actual.contains(":tempModelObject=[foo]"));

		model.detach();
		actual = model.toString();
		assertTrue(actual.contains(":attached=false"));
		assertTrue(actual.contains(":tempModelObject=[null]"));
	}

	/**
	 * Tests the PropertyModel.toString() method.
	 */
	@Test
	public void propertyModel()
	{
		String actual;
		String expected;
		PropertyModel<Void> emptyModel;
		PropertyModel<String> stringProperty;
		Properties properties;

		emptyModel = new PropertyModel<>("", null);
		actual = emptyModel.toString();
		expected = "IModel:classname=[io.github.astrapi69.model.PropertyModel]:nestedModel=[]:expression=[null]";
		assertEquals(expected, actual);

		properties = new Properties();
		properties.put("name", "foo");
		stringProperty = new PropertyModel<>(properties, "name");
		actual = stringProperty.toString();
		expected = "IModel:classname=[io.github.astrapi69.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, actual);

		stringProperty.getObject();
		actual = stringProperty.toString();
		expected = "IModel:classname=[io.github.astrapi69.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, actual);

		InnerPOJO innerPOJO;
		PropertyModel<?> pojoProperty;

		innerPOJO = new InnerPOJO();
		pojoProperty = new PropertyModel<>(innerPOJO, "pojo");

		expected = "IModel:classname=[io.github.astrapi69.model.PropertyModel]:nestedModel=[pojo]:expression=[pojo]";
		assertEquals(expected, pojoProperty.toString());
	}

	/**
	 * Used for models in testing.
	 */
	private static class InnerPOJO
	{
		@Override
		public String toString()
		{
			return "pojo";
		}
	}

	private static final class MyLoadableDetachableModel extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected String load()
		{
			return "foo";
		}
	}
}

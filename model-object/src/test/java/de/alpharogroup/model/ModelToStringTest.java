/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.alpharogroup.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;


/**
 * Tests the toString() method on the models in the de.alpharogroup.model package.
 */
public class ModelToStringTest
{
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

	/**
	 * Tests LoadableDetachableModel.toString()
	 */
	@Test
	public void loadableDetachableModel()
	{
		final LoadableDetachableModel<String> model = new MyLoadableDetachableModel();
		assertTrue(model.toString().contains(":attached=false"));
		assertTrue(model.toString().contains(":tempModelObject=[null]"));

		model.getObject();
		assertTrue(model.toString().contains(":attached=true"));
		assertTrue(model.toString().contains(":tempModelObject=[foo]"));

		model.detach();
		assertTrue(model.toString().contains(":attached=false"));
		assertTrue(model.toString().contains(":tempModelObject=[null]"));
	}

	/**
	 * Tests the PropertyModel.toString() method.
	 */
	@Test
	public void propertyModel()
	{
		final PropertyModel<Void> emptyModel = new PropertyModel<>("", null);
		String expected = "Model:classname=[de.alpharogroup.model.PropertyModel]:nestedModel=[]:expression=[null]";
		assertEquals(expected, emptyModel.toString());

		final Properties properties = new Properties();
		properties.put("name", "foo");
		final PropertyModel<String> stringProperty = new PropertyModel<>(properties, "name");

		expected = "Model:classname=[de.alpharogroup.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, stringProperty.toString());

		stringProperty.getObject();
		expected = "Model:classname=[de.alpharogroup.model.PropertyModel]:nestedModel=[{name=foo}]:expression=[name]";
		assertEquals(expected, stringProperty.toString());

		final InnerPOJO innerPOJO = new InnerPOJO();
		final PropertyModel<?> pojoProperty = new PropertyModel<>(innerPOJO, "pojo");

		expected = "Model:classname=[de.alpharogroup.model.PropertyModel]:nestedModel=[pojo]:expression=[pojo]";
		assertEquals(expected, pojoProperty.toString());
	}
}

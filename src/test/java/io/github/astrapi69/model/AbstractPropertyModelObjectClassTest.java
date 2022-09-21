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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.Serializable;

import io.github.astrapi69.model.api.IModel;
import org.testng.annotations.Test;

/**
 * <p>
 * If AbstractPropertyModel has an target that implements the IObjectClassAwareModel interface then
 * the class of that target is used to infer the modeled property type.
 * </p>
 *
 * @see <a href="https://issues.apache.org/jira/browse/WICKET-2937">WICKET-2937</a>
 * @author Pedro Santos
 */
public class AbstractPropertyModelObjectClassTest
{

	/**
	 *
	 */
	@Test
	void testBaseModel()
	{
		assertPropertyModelTargetTypeIsInteger(new BaseModel<CustomType>(new CustomType()));
	}

	/**
	 * Just asserting that the the property expression for the somePropety is aware of this property
	 * type.
	 *
	 * @param modelForCustomTypeObject
	 */
	private void assertPropertyModelTargetTypeIsInteger(IModel<?> modelForCustomTypeObject)
	{
		assertEquals(Integer.class,
			new PropertyModel<IModel<?>>(modelForCustomTypeObject, "someProperty")
				.getObjectClass());
	}

	/**
	 * Asserting that there is no problem in working with an AbstractPropertyModel targeting an
	 * IObjectClassAwareModel not initialized with an known class.
	 *
	 * @see <a href="https://issues.apache.org/jira/browse/WICKET-3253">WICKET-3253</a>
	 */
	@Test
	public void testLazyClassResolution()
	{
		IModel<CustomBean> modelCustomBean = new SerializableModel<>(null);
		PropertyModel<CustomType> customTypeModel = new PropertyModel<>(modelCustomBean,
			"customType");
		PropertyModel<Integer> somePropertyModel = new PropertyModel<>(customTypeModel,
			"someProperty");
		assertNull(somePropertyModel.getObjectClass());
		modelCustomBean.setObject(new CustomBean());
		assertEquals(Integer.class, somePropertyModel.getObjectClass());
	}

	/**
	 *
	 */
	@Test
	public void testSerializableModel()
	{
		assertPropertyModelTargetTypeIsInteger(new SerializableModel<>(new CustomType()));
	}

	private static class CustomBean implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private CustomType customType;

		@SuppressWarnings("unused")
		public CustomType getCustomType()
		{
			return customType;
		}

		@SuppressWarnings("unused")
		public void setCustomType(CustomType customType)
		{
			this.customType = customType;
		}
	}

	private static class CustomType implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private Integer someProperty;

		@SuppressWarnings("unused")
		public Integer getSomeProperty()
		{
			return someProperty;
		}

		@SuppressWarnings("unused")
		public void setSomeProperty(Integer someProperty)
		{
			this.someProperty = someProperty;
		}
	}
}

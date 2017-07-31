package de.alpharogroup.model;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;

import de.alpharogroup.model.api.Model;

/**
 * <p>
 * If AbstractPropertyModel has an target that implements the ObjectClassAware interface then
 * the class of that target is used to infer the modeled property type.
 * </p>
 *
 * @see <a href="https://issues.apache.org/jira/browse/WICKET-2937">WICKET-2937</a>
 * @author Pedro Santos
 */
public class AbstractPropertyModelObjectClassTest extends Assert
{

	/**
	 *
	 */
	@Test
	public void testModel()
	{
		assertPropertyModelTargetTypeIsInteger(new SerializableModel<>(new CustomType()));
	}

	/**
	 * Just asserting that the the property expression for the somePropety is aware of this property
	 * type.
	 *
	 * @param modelForCustomTypeObject
	 */
	private void assertPropertyModelTargetTypeIsInteger(Model<?> modelForCustomTypeObject)
	{
		assertEquals(Integer.class, new PropertyModel<Model<?>>(modelForCustomTypeObject,
			"someProperty").getObjectClass());
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
		Model<CustomBean> modelCustomBean = new SerializableModel<>(null);
		PropertyModel<CustomType> customTypeModel = new PropertyModel<>(modelCustomBean,
			"customType");
		PropertyModel<Integer> somePropertyModel = new PropertyModel<>(customTypeModel,
			"someProperty");
		assertNull(somePropertyModel.getObjectClass());
		modelCustomBean.setObject(new CustomBean());
		assertEquals(Integer.class, somePropertyModel.getObjectClass());
	}

	private static class CustomType implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private Integer someProperty;

		@SuppressWarnings("unused")
		public void setSomeProperty(Integer someProperty)
		{
			this.someProperty = someProperty;
		}

		@SuppressWarnings("unused")
		public Integer getSomeProperty()
		{
			return someProperty;
		}
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
}
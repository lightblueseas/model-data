/**
 * Copyright (C) 2015 Asterios Raptis
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astrapi69.model;

import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

/**
 * https://issues.apache.org/jira/browse/WICKET-3929
 *
 * @author Carl-Eric Menzel
 */
public class PropertyModelWithListTest
{
	@Test
	public void containerPropertyModel()
	{
		final BeansContainer container = new BeansContainer();
		final Bean bean = new Bean();
		bean.setText("Wrinkly and green I am.");
		container.getBeans().add(bean);
		final PropertyModel<String> model = new PropertyModel<>(container, "beans[0].text");
		assertEquals("Wrinkly and green I am.", model.getObject());
	}

	@Test
	public void listPropertyModel()
	{
		final List<Bean> beans = new ArrayList<>();
		final Bean bean = new Bean();
		bean.setText("Wrinkly and green I am.");
		beans.add(bean);
		final PropertyModel<String> model = new PropertyModel<>(beans, "0.text");
		assertEquals("Wrinkly and green I am.", model.getObject());
	}

	@Test
	public void nestedListPropertyModel()
	{
		final List<List<Bean>> outer = new ArrayList<>();
		final List<Bean> inner = new ArrayList<>();
		outer.add(inner);
		final Bean bean = new Bean();
		bean.setText("Wrinkly and green I am.");
		inner.add(bean);
		final PropertyModel<String> model = new PropertyModel<>(outer, "0[0].text");
		assertEquals("Wrinkly and green I am.", model.getObject());
	}

	/** */
	public static class Bean
	{
		private String text;

		/**
		 * @return the bean's text
		 */
		public String getText()
		{
			return text;
		}

		/**
		 * @param text
		 *            the bean's text
		 */
		public void setText(final String text)
		{
			this.text = text;
		}
	}

	/** */
	public static class BeansContainer
	{
		private List<Bean> beans = new ArrayList<>();

		/**
		 * @return the beans
		 *
		 */
		public List<Bean> getBeans()
		{
			return beans;
		}

		/**
		 * @param beans
		 *            the bean
		 */
		public void setBeans(final List<Bean> beans)
		{
			this.beans = beans;
		}
	}
}

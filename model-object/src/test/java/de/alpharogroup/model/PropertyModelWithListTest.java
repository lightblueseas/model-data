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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://issues.apache.org/jira/browse/WICKET-3929
 *
 * @author Carl-Eric Menzel
 */
public class PropertyModelWithListTest extends Assert
{
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

	/**
	 * @throws Exception
	 */
	@Test
	public void containerPropertyModel() throws Exception
	{
		final BeansContainer container = new BeansContainer();
		final Bean bean = new Bean();
		bean.setText("Wrinkly and green I am.");
		container.getBeans().add(bean);
		final PropertyModel<String> model = new PropertyModel<>(container, "beans[0].text");
		assertEquals("Wrinkly and green I am.", model.getObject());
	}

	/**
	 *
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void listPropertyModel() throws Exception
	{
		final List<Bean> beans = new ArrayList<>();
		final Bean bean = new Bean();
		bean.setText("Wrinkly and green I am.");
		beans.add(bean);
		final PropertyModel<String> model = new PropertyModel<>(beans, "0.text");
		assertEquals("Wrinkly and green I am.", model.getObject());
	}

	/**
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void nestedListPropertyModel() throws Exception
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
}
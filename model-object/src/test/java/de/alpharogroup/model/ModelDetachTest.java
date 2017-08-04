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

import org.junit.Assert;
import org.junit.Test;

import de.alpharogroup.model.api.Model;

/**
 * Tests the detach behavior for compatibility with Detachable nested objects, ensuring that the
 * detach method is called for those nested objects.
 */
public class ModelDetachTest extends Assert
{
	static class Detachable implements de.alpharogroup.model.api.Detachable
	{
		private static final long serialVersionUID = 1L;

		private boolean detached = false;

		@Override
		public void detach()
		{
			detached = true;
		}
	}


	/**
	 * Performs the nested test for AbstractPropertyModel.
	 */
	@Test
	public void abstractPropertyModelDetach()
	{
		Detachable detachable = new Detachable();
		Model<?> model = new AbstractPropertyModel<Void>(detachable)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String propertyExpression()
			{
				return null;
			}
		};
		model.detach();
		assertTrue(detachable.detached);
	}

	/**
	 * Performs the nested test for PropertyModel.
	 */
	@Test
	public void propertyModelDetach()
	{
		Detachable detachable = new Detachable();
		Model<?> model = new PropertyModel<Void>(detachable, "foo");
		model.detach();
		assertTrue(detachable.detached);
	}
}

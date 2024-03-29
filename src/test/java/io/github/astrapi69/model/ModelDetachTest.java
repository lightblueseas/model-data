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


import static org.testng.AssertJUnit.assertTrue;

import java.io.Serial;

import org.testng.annotations.Test;

import io.github.astrapi69.model.api.IDetachable;
import io.github.astrapi69.model.api.IModel;

/**
 * Tests the detach behavior for compatibility with IDetachable nested objects, ensuring that the
 * detach method is called for those nested objects.
 */
public class ModelDetachTest
{
	/**
	 * Performs the nested test for AbstractPropertyModel.
	 */
	@Test
	public void abstractPropertyModelDetach()
	{
		Detachable detachable = new Detachable();
		IModel<?> model = new AbstractPropertyModel<Void>(detachable)
		{
			@Serial
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
		IModel<?> model = new PropertyModel<Void>(detachable, "foo");
		model.detach();
		assertTrue(detachable.detached);
	}

	static class Detachable implements IDetachable
	{
		@Serial
		private static final long serialVersionUID = 1L;

		private boolean detached = false;

		@Override
		public void detach()
		{
			detached = true;
		}
	}
}

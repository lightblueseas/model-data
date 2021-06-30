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
package io.github.astrapi69.model.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.astrapi69.model.api.GetAndSet;

public abstract class AbstractGetAndSet implements GetAndSet
{
	protected static final String GET = "get";
	protected static final String IS = "is";
	protected static final String SET = "set";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getGetter()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getSetter()
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getTargetClass()
	{
		return null;
	}
}

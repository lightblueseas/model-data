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
package de.alpharogroup.model.lambda;

import java.io.Serializable;

/**
 * A test object for lambda related tests
 */
public class Address implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String street;

	private int number;

	public int getNumber()
	{
		return number;
	}

	public String getStreet()
	{
		return street;
	}

	public void setNumber(final int number)
	{
		this.number = number;
	}

	public void setStreet(final String street)
	{
		this.street = street;
	}
}

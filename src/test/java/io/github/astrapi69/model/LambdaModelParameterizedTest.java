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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import io.github.astrapi69.model.api.IModel;
import io.github.astrapi69.random.object.RandomObjectFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

import io.github.astrapi69.model.lambda.Person;
import io.github.astrapi69.test.object.A;
import io.github.astrapi69.test.object.AlgorithmModel;
import io.github.astrapi69.test.object.ClonableObject;
import io.github.astrapi69.test.object.Company;
import io.github.astrapi69.test.object.Customer;
import io.github.astrapi69.test.object.Employee;
import io.github.astrapi69.test.object.EmployeeList;
import io.github.astrapi69.test.object.Light;
import io.github.astrapi69.test.object.Member;
import io.github.astrapi69.test.object.NotSerializable;
import io.github.astrapi69.test.object.Permission;
import io.github.astrapi69.test.object.PremiumMember;
import io.github.astrapi69.test.object.Television;
import io.github.astrapi69.test.object.annotation.classtype.AnnotatedClass;
import io.github.astrapi69.test.object.annotation.classtype.AnnotatedTestClass;
import io.github.astrapi69.test.object.annotation.classtype.ClassExtendsAnnotatedInterface;
import io.github.astrapi69.test.object.annotation.classtype.SubAnnotatedClass;
import io.github.astrapi69.test.object.auth.AccessRight;
import io.github.astrapi69.test.object.auth.Role;
import io.github.astrapi69.test.object.auth.Roles;

/**
 * Tests for {@link LambdaModel}
 */
@SuppressWarnings("javadoc")
public class LambdaModelParameterizedTest
{


	static List<Class> testClasses = Arrays.asList(Person.class, AnnotatedClass.class,
		AnnotatedTestClass.class, ClassExtendsAnnotatedInterface.class, SubAnnotatedClass.class,
		AccessRight.class, Roles.class, Role.class, AlgorithmModel.class, A.class,
		ClonableObject.class, Company.class, Customer.class, EmployeeList.class, Employee.class,
		Light.class, Member.class, NotSerializable.class, Permission.class, Person.class,
		PremiumMember.class, Television.class);

	@ParameterizedTest
	@FieldSource("testClasses")
	public void test(Class<?> val)
		throws NoSuchFieldException, IllegalAccessException, InstantiationException
	{
		assertNotNull(val);
	}

}

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
package de.alpharogroup.model.typesafe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static de.alpharogroup.model.typesafe.TypeSafeModel.from;
import static de.alpharogroup.model.typesafe.TypeSafeModel.model;
import static de.alpharogroup.model.typesafe.TypeSafeModel.path;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import de.alpharogroup.model.typesafe.TypeSafeModel;

import de.alpharogroup.model.BaseModel;
import de.alpharogroup.model.LoadableDetachableModel;
import de.alpharogroup.model.PropertyModel;
import de.alpharogroup.model.SerializableModel;
import de.alpharogroup.model.api.Model;
import de.alpharogroup.model.reflect.Reflection;

/**
 * Test for {@link TypeSafeModel}.
 *
 * @author svenmeier
 */
@SuppressWarnings("serial")
public class TypeSafeModelTest {

	@Test
	public void inheritedTypeVariable() {
		G2 g = new G2();

		TypeSafeModel<String> model = model(from(g).getT());

		assertEquals(null, model.getObject());

		assertEquals(String.class, model.getObjectClass());
	}

	@Test
	public void backtrackedTypeVariable() {
		G1 g = new G1();

		TypeSafeModel<String> model = model(from(g).getList().get(0));

		assertEquals(null, model.getObject());

		assertEquals(String.class, model.getObjectClass());
	}

	@Test
	public void typeErasedWithUpperBound() {
		G<Serializable> g = new G<>();

		TypeSafeModel<Serializable> model = model(from(g).getT());

		assertEquals(null, model.getObject());

		assertEquals(null, model.getObjectClass());
	}

	@Test
	public void toStringNeverFails() {
		Model<B> model = model(from(A.class).getB()).bind(SerializableModel.of((A) null));

		// targetType is unknown, thus #getPath() fails - but #toString()
		// catches all exceptions anyway
		assertEquals("", model.toString());
	}

	@Test
	public void improveTargetTypeWithTargetObjectClass() {
		Model<Serializable> target = new SerializableModel<Serializable>() {
			@Override
			public Serializable getObject() {
				return new A();
			}
		};

		TypeSafeModel<A> model = model(from(A.class)).bind(target);

		assertEquals(A.class, model.getObjectClass());
	}

	@Test
	public void loadableDetachable() {
		final int[] got = new int[1];

		Model<String> string = new SerializableModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				got[0]++;

				return null;
			}
		};

		Model<String> model = model(from(string)).loadableDetachable();
		model.getObject();
		model.getObject();
		assertEquals(1, got[0]);

		model.detach();

		model.getObject();
		model.getObject();
		assertEquals(2, got[0]);
	}

	@Test
	public void nullFails() {
		try {
			from((A) null);

			fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void getTarget() {
		A a = new A();

		TypeSafeModel<A> model = model(from(a));

		assertEquals(A.class, model.getObjectClass());
		assertEquals("", model.getPath());

		assertEquals(a, model.getObject());
	}

	@Test
	public void getTargetNotBoundFails() {
		TypeSafeModel<String> model = model(from(String.class));

		try {
			model.getObject();

			fail();
		} catch (Exception ex) {
		}

	}

	@Test
	public void setTargetFails() {
		A a = new A();

		TypeSafeModel<A> model = model(from(a));

		assertEquals(A.class, model.getObjectClass());
		assertEquals("", model.getPath());

		try {
			model.setObject(new A());

			fail();
		} catch (UnsupportedOperationException expected) {
		}
	}

	@Test
	public void getInt() {
		A a = new A();

		a.integer = 42;

		TypeSafeModel<Integer> model = model(from(a).getInteger());

		assertEquals(Integer.TYPE, model.getObjectClass());
		assertEquals("integer", model.getPath());

		assertEquals(Integer.valueOf(42), model.getObject());
	}

	@Test
	public void setInt() {
		A a = new A();

		TypeSafeModel<Integer> model = model(from(a).getInteger());

		assertEquals(Integer.TYPE, model.getObjectClass());
		assertEquals("integer", model.getPath());

		model.setObject(Integer.valueOf(42));

		assertEquals(42, a.integer);
	}

	@Test
	public void getBoolean() {
		A a = new A();

		a.bool = true;

		TypeSafeModel<Boolean> model = model(from(a).isBool());

		assertEquals(Boolean.TYPE, model.getObjectClass());
		assertEquals("bool", model.getPath());

		assertEquals(Boolean.TRUE, model.getObject());
	}

	@Test
	public void setBoolean() {
		A a = new A();

		TypeSafeModel<Boolean> model = model(from(a).isBool());

		assertEquals(Boolean.TYPE, model.getObjectClass());
		assertEquals("bool", model.getPath());

		model.setObject(Boolean.TRUE);

		assertEquals(true, a.bool);
	}

	@Test
	public void setIntToNullFails() {
		A a = new A();

		TypeSafeModel<Integer> model = model(from(a).getInteger());

		assertEquals(Integer.TYPE, model.getObjectClass());
		assertEquals("integer", model.getPath());

		try {
			model.setObject(null);

			fail();
		} catch (RuntimeException expected) {
		}
	}

	@Test
	public void getObject() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<B> model = model(from(a).getB());

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());

		assertEquals(a.b, model.getObject());
	}

	@Test
	public void setObject() {
		A a = new A();

		TypeSafeModel<B> model = model(from(a).getB());

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());

		B b = new B();
		model.setObject(b);

		assertEquals(b, a.b);
	}

	@Test
	public void getInterface() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<I<C>> model = model(from(a).getI());

		assertEquals(I.class, model.getObjectClass());
		assertEquals("i", model.getPath());

		assertEquals(a.b, model.getObject());
	}

	@Test
	public void getGenericListFromInterface() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<List<C>> model = model(from(a).getI().getTs());

		assertEquals(List.class, model.getObjectClass());
		assertEquals("i.ts", model.getPath());

		assertEquals(a.b.cs, model.getObject());
	}

	@Test
	public void getRawList() {
		B b = new B();
		b.cs.add(new C());

		TypeSafeModel<Object> model = model(from(b).getRs().get(0));

		assertEquals(null, model.getObjectClass());
		assertEquals("rs.get(i)", model.getPath());

		assertEquals(b.cs.get(0), model.getObject());
	}

	@Test
	public void getGenericMap() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<Map<String, String>> model = model(from(a).getB()
				.getStrings());

		assertEquals(Map.class, model.getObjectClass());
		assertEquals("b.strings", model.getPath());

		assertEquals(a.b.strings, model.getObject());
	}

	@Test
	public void setGenericMap() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<Map<String, String>> model = model(from(a).getB()
				.getStrings());

		assertEquals(Map.class, model.getObjectClass());
		assertEquals("b.strings", model.getPath());

		Map<String, String> strings = new HashMap<>();
		model.setObject(strings);
		assertEquals(strings, a.b.strings);
	}

	@Test
	public void getIntFromNull() {
		A a = new A();

		TypeSafeModel<Character> model = model(from(a).getB().getCharacter());

		assertEquals(Character.TYPE, model.getObjectClass());
		assertEquals("b.character", model.getPath());

		assertEquals(null, model.getObject());
	}

	@Test
	public void setCharacterOnNullFails() {
		A a = new A();

		TypeSafeModel<Character> model = model(from(a).getB().getCharacter());

		assertEquals(Character.TYPE, model.getObjectClass());
		assertEquals("b.character", model.getPath());

		try {
			model.setObject('c');

			fail();
		} catch (NullPointerException expected) {
		}
	}

	@Test
	public void getValueFromGenericMap() {
		A a = new A();

		a.b = new B();

		a.b.strings.put("key", "value");

		TypeSafeModel<String> model = model(from(a).getB().getStrings().get("key"));

		assertEquals(String.class, model.getObjectClass());
		assertEquals("b.strings.get(O)", model.getPath());

		assertEquals("value", model.getObject());
	}

	@Test
	public void setValueToGenericMap() {
		A a = new A();

		a.b = new B();

		TypeSafeModel<String> model = model(from(a).getB().getStrings().get("key"));

		assertEquals(String.class, model.getObjectClass());
		assertEquals("b.strings.get(O)", model.getPath());

		model.setObject("value");

		assertEquals("value", a.b.strings.get("key"));
	}

	@Test
	public void getGenericListFromNull() {
		A a = new A();

		TypeSafeModel<List<C>> model = model(from(a).getB().getCs());

		assertEquals(List.class, model.getObjectClass());
		assertEquals("b.cs", model.getPath());

		assertEquals(null, model.getObject());
	}

	@Test
	public void getEntryFromGenericList() {
		final List<C> cs = new ArrayList<>();

		C c = new C();
		cs.add(c);

		Model<List<C>> target = new BaseModel<List<C>>() {
			@Override
			public List<C> getObject() {
				return cs;
			}
		};

		TypeSafeModel<C> model = model(from(target).get(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("get(i)", model.getPath());

		assertEquals(c, model.getObject());
	}

	@Test
	public void setEntryToGenericList() {
		final List<C> cs = new ArrayList<>();
		cs.add(new C());

		Model<List<C>> target = new BaseModel<List<C>>() {
			@Override
			public List<C> getObject() {
				return cs;
			}
		};

		TypeSafeModel<C> model = model(from(target).get(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("get(i)", model.getPath());

		C c = new C();
		model.setObject(c);

		assertEquals(c, cs.get(0));
	}

	@Test
	public void getGenericList() {
		A a = new A();
		a.b = new B();

		TypeSafeModel<List<C>> model = model(from(a).getB().getCs());

		assertEquals(List.class, model.getObjectClass());
		assertEquals("b.cs", model.getPath());

		assertEquals(a.b.cs, model.getObject());
	}

	@Test
	public void setGenericList() {
		A a = new A();
		a.b = new B();

		TypeSafeModel<List<C>> model = model(from(a).getB().getCs());

		assertEquals(List.class, model.getObjectClass());
		assertEquals("b.cs", model.getPath());

		List<C> cs = new ArrayList<>();
		model.setObject(cs);

		assertEquals(cs, a.b.cs);
	}

	@Test
	public void getObjectByIndex() {
		A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		c.string = "string";

		TypeSafeModel<C> model = model(from(a).getB().getC(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("b.getC(i)", model.getPath());

		assertEquals(c, model.getObject());
	}

	@Test
	public void getOutOfBoundsFromGenericList() {
		B b = new B();

		TypeSafeModel<C> model = model(from(b).getCs().get(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("cs.get(i)", model.getPath());

		assertEquals(null, model.getObject());
	}

	@Test
	public void getObjectFromGenericList() {
		A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		c.string = "string";

		TypeSafeModel<C> model = model(from(a).getB().getCs().get(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("b.cs.get(i)", model.getPath());

		assertEquals(c, model.getObject());
	}

	@Test
	public void getObjectFromEntryFromGenericList() {
		A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		c.string = "string";

		TypeSafeModel<String> model = model(from(a).getB().getC(0).getString());

		assertEquals(String.class, model.getObjectClass());
		assertEquals("b.getC(i).string", model.getPath());

		assertEquals("string", model.getObject());
	}

	@Test
	public void setObjectFromEntryFromGenericList() {
		A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		TypeSafeModel<String> model = model(from(a).getB().getC(0).getString());

		assertEquals(String.class, model.getObjectClass());
		assertEquals("b.getC(i).string", model.getPath());

		model.setObject("string");

		assertEquals("string", c.string);
	}

	@Test
	public void detach() {
		final boolean[] detached = { false };

		Model<A> target = new SerializableModel<A>() {
			@Override
			public A getObject() {
				return null;
			}

			@Override
			public void setObject(A a) {
			}

			@Override
			public void detach() {
				detached[0] = true;
			}
		};

		TypeSafeModel<A> model = model(from(target));

		model.detach();

		assertTrue(detached[0]);
	}

	@Test
	public void setTargetInModel() {
		final A[] as = new A[] { new A() };

		Model<A> target = new SerializableModel<A>() {

			@Override
			public A getObject() {
				return as[0];
			}

			@Override
			public void setObject(A a) {
				as[0] = a;
			}

			@Override
			public void detach() {
			}
		};

		TypeSafeModel<A> model = model(from(target));

		assertEquals(A.class, model.getObjectClass());
		assertEquals("", model.getPath());

		A a = new A();

		model.setObject(a);

		assertEquals(a, as[0]);
	}

	@Test
	public void getObjectFromObjectClassAwareModel() {
		A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		PropertyModel<B> target = new PropertyModel<>(a, "b");

		TypeSafeModel<C> model = model(from(target).getC(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("getC(i)", model.getPath());

		assertEquals(a.b.cs.get(0), model.getObject());
	}

	/**
	 * A {@link IObjectClassAwareModel} doesn't provide generic information,
	 * thus the type of the model result is {@code List<Object>} only.
	 */
	@Test
	public void getEntryFromGenericListInObjectClassAwareModelFails() {
		B b = new B();
		b.cs.add(new C());

		PropertyModel<List<C>> target = new PropertyModel<>(b, "cs");

		try {
			model(from(target).get(0).getString());

			fail();
		} catch (ClassCastException expected) {
		}
	}

	@Test
	public void getWithNestedLazyModel() {
		A a = new A();
		a.integer = 0;

		B b = new B();

		C c = new C();
		b.cs.add(c);

		TypeSafeModel<C> model = model(from(b).getC(from(a).getInteger()));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("getC(i)", model.getPath());

		assertEquals(c, model.getObject());
	}

	@Test
	public void setWithNestedLazyModel() {
		A a = new A();
		a.integer = 0;

		B b = new B();

		C c = new C();

		TypeSafeModel<C> model = model(from(b).getC(from(a).getInteger()));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("getC(i)", model.getPath());

		model.setObject(c);

		assertEquals(c, b.cs.get(0));
	}

	@Test
	public void getObjectFromGenericModel() {
		final A a = new A();
		a.b = new B();

		C c = new C();
		a.b.cs.add(c);

		Model<A> target = new SerializableModel<A>() {
			@Override
			public A getObject() {
				return a;
			}
		};

		TypeSafeModel<C> model = model(from(target).getB().getC(0));

		assertEquals(C.class, model.getObjectClass());
		assertEquals("b.getC(i)", model.getPath());

		assertEquals(a.b.cs.get(0), model.getObject());
	}

	/**
	 * issue #569
	 * <p>
	 * {@link LoadableDetachableModel#getObject()}'s type is a type variable.
	 */
	@Test
	public void getObjectFromLoadableModel() {
		final A a = new A();
		a.b = new B();

		Model<A> target = new LoadableDetachableModel<A>() {
			@Override
			protected A load() {
				return a;
			}

			@Override
			public void attach()
			{
			}
		};

		TypeSafeModel<B> model = model(from(target).getB());

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());

		assertEquals(a.b, model.getObject());
	}

	@Test
	public void getFromPrivateConstructor() {
		final A a = new A();
		a.p = P.P1;

		TypeSafeModel<String> model = model(from(a).getP().getString());

		assertEquals(String.class, model.getObjectClass());
		assertEquals("p.string", model.getPath());

		assertEquals(a.p.getString(), model.getObject());
	}

	@Test
	public void getInherited() {
		final A a = new A();
		a.b = new B();
		a.b.d = new D();

		TypeSafeModel<String> model = model(from(a).getB().getD().getString());

		assertEquals(String.class, model.getObjectClass());
		assertEquals("b.d.string", model.getPath());

		assertEquals(a.b.d.string, model.getObject());
	}

	@Test
	public void getEnum() {
		final A a = new A();
		a.e = E.E1;

		TypeSafeModel<E> model = model(from(a).getE());

		assertEquals(E.class, model.getObjectClass());
		assertEquals("e", model.getPath());

		assertEquals(a.e, model.getObject());
	}

	@Test
	public void getFinal() {
		final A a = new A();
		a.f = new F();

		TypeSafeModel<F> model = model(from(a).getF());

		assertEquals(F.class, model.getObjectClass());
		assertEquals("f", model.getPath());

		assertEquals(a.f, model.getObject());
	}

	@Test
	public void getFinalFails() {
		final A a = new A();

		try {
			model(from(a).getFinalB().getCharacter());

			fail();
		} catch (NullPointerException expected) {
		}
	}

	@Test
	public void getStringFromFinalFails() {
		final A a = new A();
		a.f = new F();
		a.f.string = "string";

		try {
			from(a).getF().getString();

			fail();
		} catch (NullPointerException expected) {
		}
	}

	@Test
	public void noBindFails() {
		TypeSafeModel<B> model = model(from(A.class).getB());

		try {
			model.getObject();

			fail();
		} catch (Exception ex) {
		}
	}

	@Test
	public void bindToGenericModel() {
		final A a = new A();
		a.b = new B();

		TypeSafeModel<B> model = model(from(A.class).getB()).bind(
				new SerializableModel<A>() {
					@Override
					public A getObject() {
						return a;
					}
				});

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());
		assertEquals(a.b, model.getObject());
	}

	@Test
	public void bindToTypeErasedModel() {
		final A a = new A();
		a.b = new B();

		TypeSafeModel<B> model = model(from(A.class).getB()).bind(new SerializableModel<>(a));

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());
		assertEquals(a.b, model.getObject());
	}

	@Test
	public void fromTypeErasedModelFails() {
		class GenericModel<T> implements Model<T> {
			@Override
			public void detach() {
			}

			@Override
			public T getObject() {
				return null;
			}

			@Override
			public void setObject(T arg0) {
			}

			@Override
			public void attach()
			{
			}
		};

		try {
			from(new GenericModel<A>());

			fail();
		} catch (RuntimeException ex) {
			assertEquals("cannot detect target type", ex.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void fromTypeErasedModelWithClass() throws Exception
	{
		final A a = new A();
		a.b = new B();

		Model target = new SerializableModel(a);

		TypeSafeModel<B> model = model(((A)from(target, A.class)).getB());

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());
		assertEquals(a.b, model.getObject());
	}


	@Test
	public void bindToTypeErasedModelWithNull() {
		TypeSafeModel<B> model = model(from(A.class).getB()).bind(
				new SerializableModel<A>(null));

		assertNull(model.getObjectType());
		assertNull(model.getObjectClass());

		try {
			model.getPath();

			fail();
		} catch (RuntimeException ex) {
			assertEquals("cannot detect target type", ex.getMessage());
		}
	}

	@Test
	public void bindToObject() {
		final A a = new A();
		a.b = new B();

		TypeSafeModel<B> model = model(from(A.class).getB()).bind(a);

		assertEquals(B.class, model.getObjectClass());
		assertEquals("b", model.getPath());
		assertEquals(a.b, model.getObject());
	}

	@Test
	public void getPath() {
		assertEquals("b.cs.size()", path(from(A.class).getB().getCs().size()));
	}

	@Test
	public void propertyReflectionAwareModel() throws Exception {
		A a = new A();
		a.b = new B();

		TypeSafeModel<Character> model = model(from(a).getB().getCharacter());

		assertNull(model.getPropertyField());
		assertEquals(B.class.getMethod("getCharacter"),
				model.getPropertyGetter());
		assertEquals(B.class.getMethod("setCharacter", Character.TYPE),
				model.getPropertySetter());
	}

	@Test
	public void propertyReflectionAwareModelNoProperty() throws Exception {
		A a = new A();
		a.b = new B();

		TypeSafeModel<C> model = model(from(a).getB().getC(0));

		assertNull(model.getPropertyField());
		assertNull(model.getPropertyGetter());
		assertNull(model.getPropertySetter());
	}

	@Test
	public void fromProxy() throws Exception {
		final List<String> list = new ArrayList<>();

		@SuppressWarnings("unchecked")
		I<String> proxy = (I<String>) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{I.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return list;
			}
		});

		TypeSafeModel<List<String>> model = model(from(proxy).getTs());

		assertSame(list, model.getObject());
	}

	public static class A implements Serializable {

		B b;

		F f;

		E e;

		P p;

		int integer;

		boolean bool;

		public E getE() {
			return e;
		}

		public P getP() {
			return p;
		}

		public F getF() {
			return f;
		}

		public final B getFinalB() {
			return b;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		public I<C> getI() {
			return b;
		}

		int getInteger() {
			return integer;
		}

		void setInteger(int integer) {
			this.integer = integer;
		}

		public void setBool(boolean bool) {
			this.bool = bool;
		}

		public boolean isBool() {
			return bool;
		}
	}

	public static class B implements I<C>, Serializable {

		char character;

		Map<String, String> strings = new HashMap<>();

		List<C> cs = new ArrayList<>();

		D d;

		B() {
		}

		public char getCharacter() {
			return character;
		}

		public void setCharacter(char character) {
			this.character = character;
		}

		public Map<String, String> getStrings() {
			return strings;
		}

		public void setStrings(Map<String, String> strings) {
			this.strings = strings;
		}

		public List<C> getCs() {
			return cs;
		}

		public D getD() {
			return d;
		}

		public void setCs(List<C> cs) {
			this.cs = cs;
		}

		public C getC(int index) {
			return cs.get(index);
		}

		public void setC(int index, C c) {
			if (index == cs.size()) {
				cs.add(c);
			} else {
				cs.set(index, c);
			}
		}

		@Override
		public List<C> getTs() {
			return cs;
		}

		@SuppressWarnings("rawtypes")
		public List getRs() {
			return cs;
		}
	}

	public static class C implements Serializable {

		String string;

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}
	}

	public static class D extends C {
	}

	public static interface I<T> {
		public List<T> getTs();
	}

	public static final class F {

		public String string;

		public String getString() {
			return string;
		}
	}

	public static class P {

		public static P P1 = new P();

		private P() {
		}

		public String getString() {
			return "P";
		}
	}

	public static enum E {
		E1;
	}

	public static class G<T extends Serializable> {
		/**
		 * {@link Reflection} has to walk up the type hierarchy to resolve the
		 * return type.
		 *
		 * @see TypeSafeModelTest#inheritedTypeVariable()
		 */
		public T getT() {
			return null;
		}

		/**
		 * {@link Reflection} has to backtrack from type variable {@code E} to
		 * {@code T} to resolve the return type of {@link List#get(int)}
		 *
		 * @see TypeSafeModelTest#backtrackedTypeVariable()
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public List<T> getList() {
			return new ArrayList();
		}
	}

	public static class G1 extends G<String> {
	}

	public static class G2 extends G1 {
	}
}
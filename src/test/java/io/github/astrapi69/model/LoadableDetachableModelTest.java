/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.astrapi69.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.junit.jupiter.api.Test;

/**
 * Tests the states of a LoadableDetachableModel
 */
@SuppressWarnings("javadoc")
public class LoadableDetachableModelTest
{
	/** Serialization helper */
	@SuppressWarnings("unchecked")
	private <T> LoadableDetachableModel<T> deserialize(byte[] serialized)
		throws IOException, ClassNotFoundException
	{
		LoadableDetachableModel<T> deserialized = null;

		try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
			ObjectInputStream ois = new ObjectInputStream(bais);)
		{
			deserialized = (LoadableDetachableModel<T>)ois.readObject();
		}
		return deserialized;
	}

	/**
	 * Checks whether the LDM can escape recursive calls.
	 */
	@Test
	public void exceptionDuringLoadKeepsLDMDetached()
	{
		class ExceptionalLoad extends LoadableDetachableModel<Integer>
		{
			private static final long serialVersionUID = 1L;

			private boolean detachCalled = false;

			@Override
			protected Integer load()
			{
				throw new RuntimeException();
			}

			@Override
			protected void onDetach()
			{
				detachCalled = true;
			}
		}

		ExceptionalLoad ldm = new ExceptionalLoad();

		assertFalse(ldm.isAttached());
		try
		{
			assertEquals(1, ldm.getObject());
			fail("shouldn't get here");
		}
		catch (RuntimeException e)
		{
		}
		ldm.detach();
		assertFalse(ldm.isAttached());
		assertTrue(ldm.detachCalled);
	}

	@Test
	public void onAttachCalled()
	{
		class AttachingLoadableModel extends LoadableDetachableModel<Integer>
		{
			private static final long serialVersionUID = 1L;

			private boolean attachCalled = false;

			@Override
			protected Integer load()
			{
				return null;
			}

			@Override
			protected void onAttach()
			{
				attachCalled = true;
			}
		}

		AttachingLoadableModel m = new AttachingLoadableModel();
		m.getObject();

		assertTrue(m.isAttached());
		assertTrue(m.attachCalled);
	}

	/**
	 * Checks whether the LDM can escape recursive calls.
	 */
	@Test
	public void recursiveGetObjectDoesntCauseInfiteLoop()
	{
		class RecursiveLoad extends LoadableDetachableModel<Integer>
		{
			private static final long serialVersionUID = 1L;

			private int count = 0;

			@Override
			protected Integer load()
			{
				count++;
				getObject();
				return count;
			}
		}

		RecursiveLoad ldm = new RecursiveLoad();

		assertFalse(ldm.isAttached());
		assertEquals(1, ldm.getObject());
		assertTrue(ldm.isAttached());
	}

	/**
	 * Tests serialization/deserialization of LDM retaining the correct state.
	 *
	 * @throws Exception
	 */
	@Test
	public void serializationDeserializationRetainsInternalState() throws Exception
	{
		SerializedLoad ldm = new SerializedLoad();
		assertEquals(1, ldm.getObject());
		ldm.detach();

		byte[] serialized = serialize(ldm);

		LoadableDetachableModel<Integer> deserialized = deserialize(serialized);

		assertFalse(deserialized.isAttached());
		assertEquals(2, deserialized.getObject());
		assertTrue(deserialized.isAttached());
		deserialized.detach();
		assertFalse(deserialized.isAttached());
	}

	/** Deserialization helper */
	private byte[] serialize(Serializable ldm) throws IOException
	{
		byte[] stream = { };
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);)
		{
			oos.writeObject(ldm);
			stream = baos.toByteArray();
		}
		return stream;
	}

	/**
	 * Tests the factory method {@link LoadableDetachableModel#of(SerializableSupplier)}: the
	 * supplier is called lazily, the loaded object is cached until detach and reloaded afterwards.
	 */
	@Test
	public void factoryMethodOfLoadsLazilyAndCachesUntilDetach()
	{
		final int[] loadCount = { 0 };
		LoadableDetachableModel<Integer> ldm = LoadableDetachableModel.of(() -> ++loadCount[0]);

		assertFalse(ldm.isAttached());
		assertEquals(0, loadCount[0]);

		assertEquals(1, ldm.getObject());
		assertTrue(ldm.isAttached());
		// the loaded object is cached, so no further load happens
		assertEquals(1, ldm.getObject());
		assertEquals(1, loadCount[0]);

		ldm.detach();
		assertFalse(ldm.isAttached());
		// after detach the object is loaded again
		assertEquals(2, ldm.getObject());
		assertEquals(2, loadCount[0]);
	}

	/**
	 * Tests the factory method {@link LoadableDetachableModel#of(SerializableSupplier)} with a null
	 * supplier
	 */
	@Test
	public void factoryMethodOfWithNullSupplierThrows()
	{
		assertThrows(NullPointerException.class, () -> LoadableDetachableModel.of(null));
	}

	/**
	 * Tests that the constructor with an object starts in attached state and loads only after
	 * detach
	 */
	@Test
	public void constructorWithObjectStartsAttached()
	{
		LoadableDetachableModel<Integer> ldm = new LoadableDetachableModel<Integer>(5)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Integer load()
			{
				return 6;
			}
		};

		assertTrue(ldm.isAttached());
		assertEquals(5, ldm.getObject());
		ldm.detach();
		assertEquals(6, ldm.getObject());
	}

	private static class SerializedLoad extends LoadableDetachableModel<Integer>
	{
		private static final long serialVersionUID = 1L;

		private int count = 0;

		@Override
		protected Integer load()
		{
			return ++count;
		}
	}
}

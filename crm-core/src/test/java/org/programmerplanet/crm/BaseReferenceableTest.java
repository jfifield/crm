package org.programmerplanet.crm;

import java.util.UUID;

import org.programmerplanet.crm.BaseReferenceable;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class BaseReferenceableTest extends TestCase {

	public void testEquals() {
		TestObject1 object1a = new TestObject1();
		TestObject1 object1b = new TestObject1();

		assertFalse(object1a.equals(null));
		assertTrue(object1a.equals(object1a));
		assertFalse(object1a.equals(object1b));

		object1a.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		object1b.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertTrue(object1a.equals(object1b));
		assertTrue(object1b.equals(object1a));

		object1a.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		assertFalse(object1a.equals(object1b));
		assertFalse(object1b.equals(object1a));

		TestObject2 object2 = new TestObject2();
		object2.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertFalse(object1a.equals(object2));
		assertFalse(object2.equals(object1a));
		assertFalse(object1b.equals(object2));
		assertFalse(object2.equals(object1b));
	}

	public void testHashCode() {
		TestObject1 object1a = new TestObject1();
		TestObject1 object1b = new TestObject1();

		assertFalse(object1a.hashCode() == object1b.hashCode());

		object1a.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		object1b.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertTrue(object1a.hashCode() == object1b.hashCode());

		object1a.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		assertFalse(object1a.hashCode() == object1b.hashCode());
	}

	private static class TestObject1 extends BaseReferenceable {
	}

	private static class TestObject2 extends BaseReferenceable {
	}

}

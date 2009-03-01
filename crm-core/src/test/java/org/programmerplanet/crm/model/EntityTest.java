package org.programmerplanet.crm.model;

import java.util.UUID;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class EntityTest extends TestCase {

	public void testEquals() {
		TestEntity1 entity1a = new TestEntity1();
		TestEntity1 entity1b = new TestEntity1();

		assertFalse(entity1a.equals(null));
		assertTrue(entity1a.equals(entity1a));
		assertFalse(entity1a.equals(entity1b));

		entity1a.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		entity1b.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertTrue(entity1a.equals(entity1b));
		assertTrue(entity1b.equals(entity1a));

		entity1a.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		assertFalse(entity1a.equals(entity1b));
		assertFalse(entity1b.equals(entity1a));

		TestEntity2 entity2 = new TestEntity2();
		entity2.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertFalse(entity1a.equals(entity2));
		assertFalse(entity2.equals(entity1a));
		assertFalse(entity1b.equals(entity2));
		assertFalse(entity2.equals(entity1b));
	}

	public void testHashCode() {
		TestEntity1 entity1a = new TestEntity1();
		TestEntity1 entity1b = new TestEntity1();

		assertFalse(entity1a.hashCode() == entity1b.hashCode());

		entity1a.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		entity1b.setId(UUID.fromString("70a7031e-a5f6-40c4-a70f-10c8c171d7dc"));
		assertTrue(entity1a.hashCode() == entity1b.hashCode());

		entity1a.setId(UUID.fromString("654680ca-bead-4a70-a449-da1ee8187402"));
		assertFalse(entity1a.hashCode() == entity1b.hashCode());
	}

	private static class TestEntity1 extends Entity {
	}

	private static class TestEntity2 extends Entity {
	}

}

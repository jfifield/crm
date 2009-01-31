package org.programmerplanet.crm.model;

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

		entity1a.setId(new Long(1));
		entity1b.setId(new Long(1));
		assertTrue(entity1a.equals(entity1b));
		assertTrue(entity1b.equals(entity1a));

		entity1a.setId(new Long(2));
		assertFalse(entity1a.equals(entity1b));
		assertFalse(entity1b.equals(entity1a));

		TestEntity2 entity2 = new TestEntity2();
		entity2.setId(new Long(1));
		assertFalse(entity1a.equals(entity2));
		assertFalse(entity2.equals(entity1a));
		assertFalse(entity1b.equals(entity2));
		assertFalse(entity2.equals(entity1b));
	}

	public void testHashCode() {
		TestEntity1 entity1a = new TestEntity1();
		TestEntity1 entity1b = new TestEntity1();

		assertFalse(entity1a.hashCode() == entity1b.hashCode());

		entity1a.setId(new Long(1));
		entity1b.setId(new Long(1));
		assertTrue(entity1a.hashCode() == entity1b.hashCode());

		entity1a.setId(new Long(2));
		assertFalse(entity1a.hashCode() == entity1b.hashCode());
	}

	private static class TestEntity1 extends Entity {
	}

	private static class TestEntity2 extends Entity {
	}

}

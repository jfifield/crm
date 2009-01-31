package org.programmerplanet.crm.model;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationObjectTest extends TestCase {

	public void testEquals() {
		ApplicationObject applicationObject1 = new ApplicationObject();
		ApplicationObject applicationObject2 = new ApplicationObject();

		assertFalse(applicationObject1.equals(null));
		assertTrue(applicationObject1.equals(applicationObject1));
		assertFalse(applicationObject1.equals(applicationObject2));

		applicationObject1.setApplicationId(new Long(1));
		applicationObject1.setObjectId(new Long(2));
		applicationObject2.setApplicationId(new Long(1));
		applicationObject2.setObjectId(new Long(2));
		assertTrue(applicationObject1.equals(applicationObject2));
		assertTrue(applicationObject2.equals(applicationObject1));

		applicationObject1.setApplicationId(new Long(2));
		assertFalse(applicationObject1.equals(applicationObject2));
		assertFalse(applicationObject2.equals(applicationObject1));
	}

	public void testHashCode() {
		ApplicationObject applicationObject1 = new ApplicationObject();
		ApplicationObject applicationObject2 = new ApplicationObject();

		assertFalse(applicationObject1.hashCode() == applicationObject2.hashCode());

		applicationObject1.setApplicationId(new Long(1));
		applicationObject1.setObjectId(new Long(2));
		applicationObject2.setApplicationId(new Long(1));
		applicationObject2.setObjectId(new Long(2));
		assertTrue(applicationObject1.hashCode() == applicationObject2.hashCode());

		applicationObject1.setApplicationId(new Long(2));
		assertFalse(applicationObject1.hashCode() == applicationObject2.hashCode());
	}

}

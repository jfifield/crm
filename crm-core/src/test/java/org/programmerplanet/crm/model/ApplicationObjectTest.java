package org.programmerplanet.crm.model;

import java.util.UUID;

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

		applicationObject1.setApplicationId(UUID.fromString("c64e3d6d-290c-4f87-bd7a-1824c42a56de"));
		applicationObject1.setObjectId(UUID.fromString("0a6567cd-c30e-4519-9dca-e4d21de4b07a"));
		applicationObject2.setApplicationId(UUID.fromString("c64e3d6d-290c-4f87-bd7a-1824c42a56de"));
		applicationObject2.setObjectId(UUID.fromString("0a6567cd-c30e-4519-9dca-e4d21de4b07a"));
		assertTrue(applicationObject1.equals(applicationObject2));
		assertTrue(applicationObject2.equals(applicationObject1));

		applicationObject1.setApplicationId(UUID.fromString("99d62893-90c1-47b0-b380-ba58c52f64eb"));
		assertFalse(applicationObject1.equals(applicationObject2));
		assertFalse(applicationObject2.equals(applicationObject1));
	}

	public void testHashCode() {
		ApplicationObject applicationObject1 = new ApplicationObject();
		ApplicationObject applicationObject2 = new ApplicationObject();

		assertFalse(applicationObject1.hashCode() == applicationObject2.hashCode());

		applicationObject1.setApplicationId(UUID.fromString("fc0a8dc9-42d4-43df-a955-c26733481ed8"));
		applicationObject1.setObjectId(UUID.fromString("eab120bb-fe5d-4160-8445-c7ecc9494615"));
		applicationObject2.setApplicationId(UUID.fromString("fc0a8dc9-42d4-43df-a955-c26733481ed8"));
		applicationObject2.setObjectId(UUID.fromString("eab120bb-fe5d-4160-8445-c7ecc9494615"));
		assertTrue(applicationObject1.hashCode() == applicationObject2.hashCode());

		applicationObject1.setApplicationId(UUID.fromString("29302f4b-c208-4c42-bc68-a93af7237454"));
		assertFalse(applicationObject1.hashCode() == applicationObject2.hashCode());
	}

}

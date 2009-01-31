package org.programmerplanet.crm.model;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectDefinitionTest extends TestCase {

	public void testGenerateTableName() {
		ObjectDefinition objectDefinition = new ObjectDefinition();
		
		objectDefinition.setObjectName("Account");
		assertEquals("o_account", objectDefinition.generateTableName());

		objectDefinition.setObjectName("Sales Lead");
		assertEquals("o_sales_lead", objectDefinition.generateTableName());

		objectDefinition.setObjectName("Guided Tour");
		assertEquals("o_guided_tour", objectDefinition.generateTableName());

		objectDefinition.setObjectName("A & B");
		assertEquals("o_a_b", objectDefinition.generateTableName());
	}

	public void testGetPluralObjectName() {
		ObjectDefinition objectDefinition = new ObjectDefinition();
		
		objectDefinition.setObjectName("Account");
		assertEquals("Accounts", objectDefinition.getPluralObjectName());

		objectDefinition.setObjectName("Sales Lead");
		assertEquals("Sales Leads", objectDefinition.getPluralObjectName());

		objectDefinition.setObjectName("Opportunity");
		assertEquals("Opportunities", objectDefinition.getPluralObjectName());
	}

}

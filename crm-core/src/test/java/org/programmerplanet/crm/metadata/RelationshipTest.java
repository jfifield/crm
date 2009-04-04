package org.programmerplanet.crm.metadata;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class RelationshipTest extends TestCase {

	public void testGenerateTableName() {
		Relationship relationship = new Relationship();
		ObjectDefinition objectDefinition1 = new ObjectDefinition();
		ObjectDefinition objectDefinition2 = new ObjectDefinition();
		
		objectDefinition1.setObjectName("Account");
		objectDefinition2.setObjectName("Contact");
		assertEquals("r_account_contact", relationship.generateTableName(objectDefinition1, objectDefinition2));

		objectDefinition1.setObjectName("Sales Lead");
		objectDefinition2.setObjectName("Account");
		assertEquals("r_sales_lead_account", relationship.generateTableName(objectDefinition1, objectDefinition2));
	}

}

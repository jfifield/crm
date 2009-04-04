package org.programmerplanet.crm.metadata;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class FieldDefinitionTest extends TestCase {

	public void testGenerateColumnName() {
		FieldDefinition fieldDefinition = new FieldDefinition();

		fieldDefinition.setFieldName("Account");
		assertEquals("f_account", fieldDefinition.generateColumnName());

		fieldDefinition.setFieldName("First Name");
		assertEquals("f_first_name", fieldDefinition.generateColumnName());

		fieldDefinition.setFieldName("D.O.B.");
		assertEquals("f_d_o_b", fieldDefinition.generateColumnName());
	}

}

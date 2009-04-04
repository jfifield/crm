package org.programmerplanet.crm.schema;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface SchemaManager {

	void createTable(ObjectDefinition objectDefinition);

	void renameTable(String oldTableName, ObjectDefinition objectDefinition);

	void dropTable(ObjectDefinition objectDefinition);

	void createColumn(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition);

	void renameColumn(ObjectDefinition objectDefinition, String oldColumnName, FieldDefinition fieldDefinition);

	void dropColumn(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition);

	void createTable(Relationship relationship, ObjectDefinition objectDefinition1, ObjectDefinition objectDefinition2);

	void dropTable(Relationship relationship);

}

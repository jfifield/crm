package org.programmerplanet.crm.metadata.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.OptionList;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface FieldDefinitionDao {

	FieldDefinition getFieldDefinition(UUID id);

	List<FieldDefinition> getFieldDefinitionsForObject(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	void insertFieldDefinition(FieldDefinition fieldDefinition);

	void updateFieldDefinition(FieldDefinition fieldDefinition);

	void deleteFieldDefinition(FieldDefinition fieldDefinition);

	boolean isFieldNameUnique(UUID objectId, UUID id, String fieldName);

	List<FieldDefinition> getFieldDefinitionsOfObjectType(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsOfOptionListType(OptionList optionList);

}

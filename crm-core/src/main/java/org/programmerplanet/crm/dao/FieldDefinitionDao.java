package org.programmerplanet.crm.dao;

import java.util.List;

import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.OptionList;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface FieldDefinitionDao {

	FieldDefinition getFieldDefinition(Long id);

	List getFieldDefinitionsForObject(ObjectDefinition objectDefinition);

	List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	void insertFieldDefinition(FieldDefinition fieldDefinition);

	void updateFieldDefinition(FieldDefinition fieldDefinition);

	void deleteFieldDefinition(FieldDefinition fieldDefinition);

	boolean isFieldNameUnique(Long objectId, Long id, String fieldName);

	List getFieldDefinitionsOfObjectType(ObjectDefinition objectDefinition);

	List getFieldDefinitionsOfOptionListType(OptionList optionList);

}
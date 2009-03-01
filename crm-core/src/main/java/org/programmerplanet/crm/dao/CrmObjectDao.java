package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface CrmObjectDao {

	List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions);

	List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, UUID id);

	UUID insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, UUID id);

	void deleteCrmObject(ObjectDefinition objectDefinition, UUID id);

	void insertCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId);

	void clearCrmObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value);

}

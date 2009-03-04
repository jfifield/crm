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

	List<Map> getCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions);

	List<Map> getRelatedCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List<Map> getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	Map getCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id);

	UUID insertCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data, UUID id);

	void deleteCrmObject(ObjectDefinition objectDefinition, UUID id);

	void insertCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId);

	void clearCrmObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value);

}

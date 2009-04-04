package org.programmerplanet.crm.data.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ObjectDataDao {

	List<Map> getObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions);

	List<Map> getRelatedObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List<Map> getObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	Map getObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id);

	UUID insertObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data);

	void updateObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data, UUID id);

	void deleteObject(ObjectDefinition objectDefinition, UUID id);

	void insertObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId);

	void clearObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value);

}

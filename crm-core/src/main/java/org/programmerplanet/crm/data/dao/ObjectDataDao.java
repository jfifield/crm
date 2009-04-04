package org.programmerplanet.crm.data.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ObjectDataDao {

	List<ObjectData> getObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions);

	List<ObjectData> getRelatedObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List<ObjectData> getObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	ObjectData getObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id);

	UUID insertObject(ObjectData objectData);

	void updateObject(ObjectData objectData);

	void deleteObject(ObjectDefinition objectDefinition, UUID id);

	void insertObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId);

	void clearObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value);

}

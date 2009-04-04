package org.programmerplanet.crm.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationService {

	List<Application> getAllApplications();

	Application getApplication(UUID id);

	ObjectDefinition getObjectDefinition(UUID id);

	ObjectDefinition getObjectDefinition(String objectName);

	List<ObjectDefinition> getObjectDefinitionsForApplication(Application application);

	List<FieldDefinition> getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	List<Map> getCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions);

	List<Map> getRelatedCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List<Map> getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	Map getCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id);

	Relationship getRelationship(UUID parentObjectId, UUID childObjectId);

	List<Relationship> getRelationshipsForObject(ObjectDefinition objectDefinition);

	FileInfo getFileInfo(UUID id);

	void insertFile(FileInfo fileInfo, InputStream inputStream);

	void getFile(UUID id, OutputStream outputStream);

	UUID insertCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data, UUID id);

	void deleteCrmObject(ObjectDefinition objectDefinition, UUID id);

	void insertCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

}

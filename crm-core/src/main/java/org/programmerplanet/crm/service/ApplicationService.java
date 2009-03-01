package org.programmerplanet.crm.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.model.User;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationService {

	User getUser(String username, String password);

	List getAllApplications();

	Application getApplication(UUID id);

	ObjectDefinition getObjectDefinition(UUID id);

	ObjectDefinition getObjectDefinition(String objectName);

	List getObjectDefinitionsForApplication(Application application);

	List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions);

	List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id);

	Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, UUID id);

	Relationship getRelationship(UUID parentObjectId, UUID childObjectId);

	List getRelationshipsForObject(ObjectDefinition objectDefinition);

	FileInfo getFileInfo(UUID id);

	void insertFile(FileInfo fileInfo, InputStream inputStream);

	void getFile(UUID id, OutputStream outputStream);

	UUID insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, UUID id);

	void deleteCrmObject(ObjectDefinition objectDefinition, UUID id);

	void insertCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

	void deleteCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId);

}

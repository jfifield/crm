package org.programmerplanet.crm.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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

	Application getApplication(Long id);

	ObjectDefinition getObjectDefinition(Long id);

	ObjectDefinition getObjectDefinition(String objectName);

	List getObjectDefinitionsForApplication(Application application);

	List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions);

	List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id);

	List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id);

	Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Long id);

	Relationship getRelationship(Long parentObjectId, Long childObjectId);

	List getRelationshipsForObject(ObjectDefinition objectDefinition);

	FileInfo getFileInfo(Long id);

	void insertFile(FileInfo fileInfo, InputStream inputStream);

	void getFile(Long id, OutputStream outputStream);

	Long insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, Long id);

	void deleteCrmObject(ObjectDefinition objectDefinition, Long id);

	void insertCrmObjectRelationship(ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId);

	void deleteCrmObjectRelationship(ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId);

}

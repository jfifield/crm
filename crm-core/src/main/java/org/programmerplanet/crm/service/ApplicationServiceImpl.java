package org.programmerplanet.crm.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.programmerplanet.crm.dao.ApplicationDao;
import org.programmerplanet.crm.dao.CrmObjectDao;
import org.programmerplanet.crm.dao.FieldDefinitionDao;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.dao.RelationshipDao;
import org.programmerplanet.crm.dao.UserDao;
import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.model.User;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationServiceImpl implements ApplicationService {

	private ObjectDefinitionDao objectDefinitionDao;
	private FieldDefinitionDao fieldDefinitionDao;
	private ApplicationDao applicationDao;
	private RelationshipDao relationshipDao;
	private CrmObjectDao crmObjectDao;
	private FileDao fileDao;
	private UserDao userDao;

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public void setFieldDefinitionDao(FieldDefinitionDao fieldDefinitionDao) {
		this.fieldDefinitionDao = fieldDefinitionDao;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setRelationshipDao(RelationshipDao relationshipDao) {
		this.relationshipDao = relationshipDao;
	}

	public void setCrmObjectDao(CrmObjectDao crmObjectDao) {
		this.crmObjectDao = crmObjectDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getUser(java.lang.String, java.lang.String)
	 */
	public User getUser(String username, String password) {
		return userDao.getUser(username, password);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getAllApplications()
	 */
	public List getAllApplications() {
		return applicationDao.getAllApplications();
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getApplication(java.lang.Long)
	 */
	public Application getApplication(Long id) {
		return applicationDao.getApplication(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getObjectDefinition(java.lang.Long)
	 */
	public ObjectDefinition getObjectDefinition(Long id) {
		return objectDefinitionDao.getObjectDefinition(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getObjectDefinition(java.lang.String)
	 */
	public ObjectDefinition getObjectDefinition(String objectName) {
		return objectDefinitionDao.getObjectDefinition(objectName);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getObjectDefinitionsForApplication(org.programmerplanet.crm.model.Application)
	 */
	public List getObjectDefinitionsForApplication(Application application) {
		return objectDefinitionDao.getObjectDefinitionsForApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFieldDefinitionsForObjectList(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFieldDefinitionsForObjectView(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObjectView(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObjects(org.programmerplanet.crm.model.ObjectDefinition, java.util.List)
	 */
	public List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions) {
		return crmObjectDao.getCrmObjects(objectDefinition, fieldDefinitions);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getRelatedCrmObjects(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id) {
		return crmObjectDao.getRelatedCrmObjects(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObjectsAvailableForLinking(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id) {
		return crmObjectDao.getCrmObjectsAvailableForLinking(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.lang.Long)
	 */
	public Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Long id) {
		return crmObjectDao.getCrmObject(objectDefinition, fieldDefinitions, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getRelationship(java.lang.Long, java.lang.Long)
	 */
	public Relationship getRelationship(Long parentObjectId, Long childObjectId) {
		return relationshipDao.getRelationship(parentObjectId, childObjectId);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getRelationshipsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getRelationshipsForObject(ObjectDefinition objectDefinition) {
		return relationshipDao.getRelationshipsForObject(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFileInfo(java.lang.Long)
	 */
	public FileInfo getFileInfo(Long id) {
		return fileDao.getFileInfo(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#insertFile(org.programmerplanet.crm.model.FileInfo, java.io.InputStream)
	 */
	public void insertFile(FileInfo fileInfo, InputStream inputStream) {
		fileDao.insertFile(fileInfo, inputStream);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFile(java.lang.Long, java.io.OutputStream)
	 */
	public void getFile(Long id, OutputStream outputStream) {
		fileDao.getFile(id, outputStream);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#insertCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.util.Map)
	 */
	public Long insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data) {
		return crmObjectDao.insertCrmObject(objectDefinition, fieldDefinitions, data);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#updateCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.util.Map, java.lang.Long)
	 */
	public void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, Long id) {
		crmObjectDao.updateCrmObject(objectDefinition, fieldDefinitions, data, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#deleteCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void deleteCrmObject(ObjectDefinition objectDefinition, Long id) {
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);

		Map crmObjectData = crmObjectDao.getCrmObject(objectDefinition, fieldDefinitions, id);

		// delete referenced files
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			if (fieldDefinition.getDataType().equals(DataType.FILE)) {
				Number fileId = (Number)crmObjectData.get(fieldDefinition.getColumnName());
				if (fileId != null) {
					fileDao.deleteFile(new Long(fileId.longValue()));
				}
			}
		}

		// delete 'many' relationships
		List relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Iterator i = relationships.iterator(); i.hasNext();) {
			Relationship relationship = (Relationship)i.next();
			crmObjectDao.deleteCrmObjectRelationships(relationship, objectDefinition, id);
		}

		// null 'one' relationships
		List objectFieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfObjectType(objectDefinition);
		for (Iterator i = objectFieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			ObjectDefinition ownerObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			crmObjectDao.clearCrmObjectValue(ownerObjectDefinition, fieldDefinition, id);
		}

		crmObjectDao.deleteCrmObject(objectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#insertCrmObjectRelationship(org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void insertCrmObjectRelationship(ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId) {
		Relationship relationship = relationshipDao.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		crmObjectDao.insertCrmObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#deleteCrmObjectRelationship(org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void deleteCrmObjectRelationship(ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId) {
		Relationship relationship = relationshipDao.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		crmObjectDao.deleteCrmObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

}

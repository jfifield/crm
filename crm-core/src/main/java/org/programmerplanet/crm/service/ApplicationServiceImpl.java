package org.programmerplanet.crm.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.dao.CrmObjectDao;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;
import org.programmerplanet.crm.metadata.dao.ApplicationDao;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.metadata.dao.RelationshipDao;
import org.programmerplanet.crm.model.FileInfo;

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

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List)
	 */
	public List<Map> getCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions) {
		return crmObjectDao.getCrmObjects(objectDefinition, fieldDefinitions);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getRelatedCrmObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<Map> getRelatedCrmObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		return crmObjectDao.getRelatedCrmObjects(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObjectsAvailableForLinking(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<Map> getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		return crmObjectDao.getCrmObjectsAvailableForLinking(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getCrmObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.UUID)
	 */
	public Map getCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id) {
		return crmObjectDao.getCrmObject(objectDefinition, fieldDefinitions, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFileInfo(java.util.UUID)
	 */
	public FileInfo getFileInfo(UUID id) {
		return fileDao.getFileInfo(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#saveFile(org.programmerplanet.crm.model.FileInfo, java.io.InputStream)
	 */
	public void saveFile(FileInfo fileInfo, InputStream inputStream) {
		fileDao.insertFile(fileInfo, inputStream);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#getFile(java.util.UUID, java.io.OutputStream)
	 */
	public void getFile(UUID id, OutputStream outputStream) {
		fileDao.getFile(id, outputStream);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#insertCrmObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.Map)
	 */
	public UUID insertCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data) {
		return crmObjectDao.insertCrmObject(objectDefinition, fieldDefinitions, data);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#updateCrmObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.Map, java.util.UUID)
	 */
	public void updateCrmObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data, UUID id) {
		crmObjectDao.updateCrmObject(objectDefinition, fieldDefinitions, data, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#deleteCrmObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteCrmObject(ObjectDefinition objectDefinition, UUID id) {
		List<FieldDefinition> fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);

		Map crmObjectData = crmObjectDao.getCrmObject(objectDefinition, fieldDefinitions, id);

		// delete referenced files
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getDataType().equals(DataType.FILE)) {
				UUID fileId = (UUID)crmObjectData.get(fieldDefinition.getColumnName());
				if (fileId != null) {
					fileDao.deleteFile(fileId);
				}
			}
		}

		// delete 'many' relationships
		List<Relationship> relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Relationship relationship : relationships) {
			crmObjectDao.deleteCrmObjectRelationships(relationship, objectDefinition, id);
		}

		// null 'one' relationships
		List<FieldDefinition> objectFieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfObjectType(objectDefinition);
		for (FieldDefinition fieldDefinition : objectFieldDefinitions) {
			ObjectDefinition ownerObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			crmObjectDao.clearCrmObjectValue(ownerObjectDefinition, fieldDefinition, id);
		}

		crmObjectDao.deleteCrmObject(objectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#saveCrmObjectRelationship(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void saveCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		Relationship relationship = relationshipDao.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		crmObjectDao.insertCrmObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

	/**
	 * @see org.programmerplanet.crm.service.ApplicationService#deleteCrmObjectRelationship(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteCrmObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		Relationship relationship = relationshipDao.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		crmObjectDao.deleteCrmObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

}

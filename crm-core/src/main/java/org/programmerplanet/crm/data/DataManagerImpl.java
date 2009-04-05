package org.programmerplanet.crm.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.data.dao.ObjectDataDao;
import org.programmerplanet.crm.data.dao.FileDao;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DataManagerImpl implements DataManager {

	private MetadataManager metadataManager;
	private ObjectDataDao objectDataDao;
	private FileDao fileDao;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public void setObjectDataDao(ObjectDataDao objectDataDao) {
		this.objectDataDao = objectDataDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List)
	 */
	public List<ObjectData> getObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions) {
		return objectDataDao.getObjects(objectDefinition, fieldDefinitions);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getRelatedObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<ObjectData> getRelatedObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		return objectDataDao.getRelatedObjects(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getObjectsAvailableForLinking(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<ObjectData> getObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		return objectDataDao.getObjectsAvailableForLinking(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.UUID)
	 */
	public ObjectData getObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id) {
		return objectDataDao.getObject(objectDefinition, fieldDefinitions, id);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getFileInfo(java.util.UUID)
	 */
	public FileInfo getFileInfo(UUID id) {
		return fileDao.getFileInfo(id);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#saveFile(org.programmerplanet.crm.data.FileInfo, java.io.InputStream)
	 */
	public void saveFile(FileInfo fileInfo, InputStream inputStream) {
		fileDao.insertFile(fileInfo, inputStream);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#getFile(java.util.UUID, java.io.OutputStream)
	 */
	public void getFile(UUID id, OutputStream outputStream) {
		fileDao.getFile(id, outputStream);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#saveObject(org.programmerplanet.crm.data.ObjectData)
	 */
	public void saveObject(ObjectData objectData) {
		if (objectData.getId() == null) {
			objectDataDao.insertObject(objectData);
		}
		else {
			objectDataDao.updateObject(objectData);
		}
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#deleteObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteObject(ObjectDefinition objectDefinition, UUID id) {
		List<FieldDefinition> fieldDefinitions = metadataManager.getFieldDefinitionsForObject(objectDefinition);

		ObjectData objectData = objectDataDao.getObject(objectDefinition, fieldDefinitions, id);
		Map data = objectData.getData();

		// delete referenced files
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getDataType().equals(DataType.FILE)) {
				UUID fileId = (UUID)data.get(fieldDefinition.getColumnName());
				if (fileId != null) {
					fileDao.deleteFile(fileId);
				}
			}
		}

		// delete 'many' relationships
		List<Relationship> relationships = metadataManager.getRelationshipsForObject(objectDefinition);
		for (Relationship relationship : relationships) {
			objectDataDao.deleteObjectRelationships(relationship, objectDefinition, id);
		}

		// null 'one' relationships
		List<FieldDefinition> objectFieldDefinitions = metadataManager.getFieldDefinitionsOfObjectType(objectDefinition);
		for (FieldDefinition fieldDefinition : objectFieldDefinitions) {
			ObjectDefinition ownerObjectDefinition = metadataManager.getObjectDefinition(fieldDefinition.getObjectId());
			objectDataDao.clearObjectValue(ownerObjectDefinition, fieldDefinition, id);
		}

		objectDataDao.deleteObject(objectDefinition, id);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#saveObjectRelationship(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void saveObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		Relationship relationship = metadataManager.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		objectDataDao.insertObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

	/**
	 * @see org.programmerplanet.crm.data.DataManager#deleteObjectRelationship(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteObjectRelationship(ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		Relationship relationship = metadataManager.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		objectDataDao.deleteObjectRelationship(relationship, parentObjectDefinition, parentId, childObjectDefinition, childId);
	}

}

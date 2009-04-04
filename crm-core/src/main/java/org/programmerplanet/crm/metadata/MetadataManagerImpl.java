package org.programmerplanet.crm.metadata;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.metadata.dao.ApplicationDao;
import org.programmerplanet.crm.metadata.dao.ApplicationObjectDao;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.metadata.dao.OptionListDao;
import org.programmerplanet.crm.metadata.dao.OptionListItemDao;
import org.programmerplanet.crm.metadata.dao.RelationshipDao;
import org.programmerplanet.crm.schema.SchemaManager;
import org.programmerplanet.crm.util.ListUtil;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class MetadataManagerImpl implements MetadataManager {

	private ObjectDefinitionDao objectDefinitionDao;
	private FieldDefinitionDao fieldDefinitionDao;
	private ApplicationDao applicationDao;
	private ApplicationObjectDao applicationObjectDao;
	private RelationshipDao relationshipDao;
	private OptionListDao optionListDao;
	private OptionListItemDao optionListItemDao;
	private FileDao fileDao;
	private SchemaManager schemaManager;

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public void setFieldDefinitionDao(FieldDefinitionDao fieldDefinitionDao) {
		this.fieldDefinitionDao = fieldDefinitionDao;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setApplicationObjectDao(ApplicationObjectDao applicationObjectDao) {
		this.applicationObjectDao = applicationObjectDao;
	}

	public void setRelationshipDao(RelationshipDao relationshipDao) {
		this.relationshipDao = relationshipDao;
	}

	public void setOptionListDao(OptionListDao optionListDao) {
		this.optionListDao = optionListDao;
	}

	public void setOptionListItemDao(OptionListItemDao optionListItemDao) {
		this.optionListItemDao = optionListItemDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setSchemaManager(SchemaManager schemaManager) {
		this.schemaManager = schemaManager;
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getApplications()
	 */
	public List<Application> getApplications() {
		return applicationDao.getApplications();
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getApplication(java.util.UUID)
	 */
	public Application getApplication(UUID id) {
		return applicationDao.getApplication(id);
	}

	private void insertApplication(Application application) {
		int viewIndex = getNextApplicationViewIndex();
		application.setViewIndex(new Integer(viewIndex));
		applicationDao.insertApplication(application);
	}

	private int getNextApplicationViewIndex() {
		List applications = applicationDao.getApplications();
		return getNextIndexValue(applications, "viewIndex");
	}

	private void updateApplication(Application application) {
		applicationDao.updateApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public void saveApplication(Application application) {
		if (application.getId() != null) {
			this.updateApplication(application);
		}
		else {
			this.insertApplication(application);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public void deleteApplication(Application application) {
		applicationDao.deleteApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveApplicationViewIndex(org.programmerplanet.crm.metadata.Application, java.lang.String)
	 */
	public void moveApplicationViewIndex(Application application, String direction) {
		List<Application> applications = applicationDao.getApplications();
		ListUtil.moveElement(applications, application, direction);

		int index = 0;
		for (Application application2 : applications) {
			if (application2.getViewIndex() == null || application2.getViewIndex().intValue() != index) {
				application2.setViewIndex(new Integer(index));
				applicationDao.updateApplication(application2);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getObjectDefinitionsForApplication(org.programmerplanet.crm.metadata.Application)
	 */
	public List<ObjectDefinition> getObjectDefinitionsForApplication(Application application) {
		return objectDefinitionDao.getObjectDefinitionsForApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getRelatedObjectDefinitionsForObject(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public Map<Relationship, ObjectDefinition> getRelatedObjectDefinitionsForObject(ObjectDefinition objectDefinition) {
		Map relatedObjectDefinition = new LinkedHashMap();
		List<Relationship> relationshipForObject = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Relationship relationship : relationshipForObject) {
			ObjectDefinition childObjectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getChildObjectId());
			relatedObjectDefinition.put(relationship, childObjectDefinition);
		}
		return relatedObjectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getObjectDefinitions()
	 */
	public List<ObjectDefinition> getObjectDefinitions() {
		return objectDefinitionDao.getObjectDefinitions();
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getObjectDefinition(java.util.UUID)
	 */
	public ObjectDefinition getObjectDefinition(UUID id) {
		return objectDefinitionDao.getObjectDefinition(id);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getObjectDefinition(java.lang.String)
	 */
	public ObjectDefinition getObjectDefinition(String objectName) {
		return objectDefinitionDao.getObjectDefinition(objectName);
	}
	
	private void insertObjectDefinition(ObjectDefinition objectDefinition) {
		objectDefinitionDao.insertObjectDefinition(objectDefinition);
		schemaManager.createTable(objectDefinition);
	}

	private void updateObjectDefinition(ObjectDefinition objectDefinition) {
		ObjectDefinition originalObjectDefinition = objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDao.updateObjectDefinition(objectDefinition);
		// rename table if necessary...
		if (!originalObjectDefinition.getTableName().equals(objectDefinition.getTableName())) {
			schemaManager.renameTable(originalObjectDefinition.getTableName(), objectDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveObjectDefinition(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public void saveObjectDefinition(ObjectDefinition objectDefinition) {
		if (objectDefinition.getId() != null) {
			this.updateObjectDefinition(objectDefinition);
		}
		else {
			this.insertObjectDefinition(objectDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteObjectDefinition(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public void deleteObjectDefinition(ObjectDefinition objectDefinition) {
		// drop 'many' relationships
		List<Relationship> relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Relationship relationship1 : relationships) {
			Relationship relationship2 = relationshipDao.getRelationship(relationship1.getChildObjectId(), relationship1.getParentObjectId());
			relationshipDao.deleteRelationship(relationship1);
			relationshipDao.deleteRelationship(relationship2);
			schemaManager.dropTable(relationship1);
		}

		// drop 'one' relationships
		List<FieldDefinition> fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfObjectType(objectDefinition);
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			ObjectDefinition ownerObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			fieldDefinitionDao.deleteFieldDefinition(fieldDefinition);
			schemaManager.dropColumn(ownerObjectDefinition, fieldDefinition);
		}

		// delete referenced files
		fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			if (DataType.FILE.equals(fieldDefinition.getDataType())) {
				fileDao.deleteFiles(objectDefinition, fieldDefinition);
			}
		}

		// delete application references
		applicationObjectDao.deleteApplicationObjects(objectDefinition);

		// drop object
		objectDefinitionDao.deleteObjectDefinition(objectDefinition);
		schemaManager.dropTable(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveApplicationObject(org.programmerplanet.crm.metadata.ApplicationObject)
	 */
	public void saveApplicationObject(ApplicationObject applicationObject) {
		int viewIndex = getNextApplicationObjectViewIndex(applicationObject);
		applicationObject.setViewIndex(new Integer(viewIndex));
		applicationObjectDao.insertApplicationObject(applicationObject);
	}

	private int getNextApplicationObjectViewIndex(ApplicationObject applicationObject) {
		Application application = applicationDao.getApplication(applicationObject.getApplicationId());
		List applicationObjects = applicationObjectDao.getApplicationObjectsForApplication(application);
		return getNextIndexValue(applicationObjects, "viewIndex");
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteApplicationObject(org.programmerplanet.crm.metadata.ApplicationObject)
	 */
	public void deleteApplicationObject(ApplicationObject applicationObject) {
		applicationObjectDao.deleteApplicationObject(applicationObject);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveApplicationObjectViewIndex(org.programmerplanet.crm.metadata.ApplicationObject, java.lang.String)
	 */
	public void moveApplicationObjectViewIndex(ApplicationObject applicationObject, String direction) {
		Application application = applicationDao.getApplication(applicationObject.getApplicationId());
		List<ApplicationObject> applicationObjects = applicationObjectDao.getApplicationObjectsForApplication(application);
		ListUtil.moveElement(applicationObjects, applicationObject, direction);

		int index = 0;
		for (ApplicationObject applicationObject2 : applicationObjects) {
			if (applicationObject2.getViewIndex() == null || applicationObject2.getViewIndex().intValue() != index) {
				applicationObject2.setViewIndex(new Integer(index));
				applicationObjectDao.updateApplicationObject(applicationObject2);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getFieldDefinition(java.util.UUID)
	 */
	public FieldDefinition getFieldDefinition(UUID id) {
		return fieldDefinitionDao.getFieldDefinition(id);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getFieldDefinitionsForObject(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObject(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getFieldDefinitionsForObjectList(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getFieldDefinitionsForObjectView(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public List<FieldDefinition> getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObjectView(objectDefinition);
	}

	private void insertFieldDefinition(FieldDefinition fieldDefinition) {
		int viewIndex = getNextFieldDefinitionViewIndex(fieldDefinition);
		fieldDefinition.setViewIndex(new Integer(viewIndex));
		fieldDefinitionDao.insertFieldDefinition(fieldDefinition);

		// create column
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		schemaManager.createColumn(objectDefinition, fieldDefinition);
	}

	private int getNextFieldDefinitionViewIndex(FieldDefinition fieldDefinition) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		return getNextIndexValue(fieldDefinitions, "viewIndex");
	}

	private void updateFieldDefinition(FieldDefinition fieldDefinition) {
		FieldDefinition originalFieldDefinition = fieldDefinitionDao.getFieldDefinition(fieldDefinition.getId());
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition);
		// rename field if necessary...
		if (!originalFieldDefinition.getColumnName().equals(fieldDefinition.getColumnName())) {
			ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			schemaManager.renameColumn(objectDefinition, originalFieldDefinition.getColumnName(), fieldDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveFieldDefinition(org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void saveFieldDefinition(FieldDefinition fieldDefinition) {
		if (fieldDefinition.getId() != null) {
			this.updateFieldDefinition(fieldDefinition);
		}
		else {
			this.insertFieldDefinition(fieldDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteFieldDefinition(org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void deleteFieldDefinition(FieldDefinition fieldDefinition) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		if (fieldDefinition.getDataType().equals(DataType.FILE)) {
			fileDao.deleteFiles(objectDefinition, fieldDefinition);
		}
		fieldDefinitionDao.deleteFieldDefinition(fieldDefinition);
		schemaManager.dropColumn(objectDefinition, fieldDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveFieldDefinitionViewIndex(org.programmerplanet.crm.metadata.FieldDefinition, java.lang.String)
	 */
	public void moveFieldDefinitionViewIndex(FieldDefinition fieldDefinition, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List<FieldDefinition> fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		ListUtil.moveElement(fieldDefinitions, fieldDefinition, direction);

		int index = 0;
		for (FieldDefinition field : fieldDefinitions) {
			if (field.getViewIndex() == null || field.getViewIndex().intValue() != index) {
				field.setViewIndex(new Integer(index));
				fieldDefinitionDao.updateFieldDefinition(field);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveFieldDefinitionListIndex(org.programmerplanet.crm.metadata.FieldDefinition, java.lang.String)
	 */
	public void moveFieldDefinitionListIndex(FieldDefinition fieldDefinition, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List<FieldDefinition> fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
		ListUtil.moveElement(fieldDefinitions, fieldDefinition, direction);

		int index = 0;
		for (FieldDefinition field : fieldDefinitions) {
			if (field.getListIndex() == null || field.getListIndex().intValue() != index) {
				field.setListIndex(new Integer(index));
				fieldDefinitionDao.updateFieldDefinition(field);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#addFieldDefinitionListIndex(org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void addFieldDefinitionListIndex(FieldDefinition fieldDefinition) {
		int listIndex = getNextFieldDefinitionListIndex(fieldDefinition);
		fieldDefinition.setListIndex(new Integer(listIndex));
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition);
	}

	private int getNextFieldDefinitionListIndex(FieldDefinition fieldDefinition) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
		return getNextIndexValue(fieldDefinitions, "listIndex");
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#removeFieldDefinitionListIndex(org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void removeFieldDefinitionListIndex(FieldDefinition fieldDefinition) {
		fieldDefinition.setListIndex(null);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getOptionLists()
	 */
	public List<OptionList> getOptionLists() {
		return optionListDao.getOptionLists();
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getOptionList(java.util.UUID)
	 */
	public OptionList getOptionList(UUID id) {
		return optionListDao.getOptionList(id);
	}

	private void insertOptionList(OptionList optionList) {
		optionListDao.insertOptionList(optionList);
	}

	private void updateOptionList(OptionList optionList) {
		optionListDao.updateOptionList(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveOptionList(org.programmerplanet.crm.metadata.OptionList)
	 */
	public void saveOptionList(OptionList optionList) {
		if (optionList.getId() != null) {
			this.updateOptionList(optionList);
		}
		else {
			this.insertOptionList(optionList);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteOptionList(org.programmerplanet.crm.metadata.OptionList)
	 */
	public void deleteOptionList(OptionList optionList) {
		// delete referencing columns
		List<FieldDefinition> fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfOptionListType(optionList);
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			this.deleteFieldDefinition(fieldDefinition);
		}
		optionListDao.deleteOptionList(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getOptionListItems(org.programmerplanet.crm.metadata.OptionList)
	 */
	public List<OptionListItem> getOptionListItems(OptionList optionList) {
		return optionListItemDao.getOptionListItems(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getOptionListItem(java.util.UUID)
	 */
	public OptionListItem getOptionListItem(UUID id) {
		return optionListItemDao.getOptionListItem(id);
	}

	private void insertOptionListItem(OptionListItem optionListItem) {
		int viewIndex = getNextOptionListItemViewIndex(optionListItem);
		optionListItem.setViewIndex(new Integer(viewIndex));
		optionListItemDao.insertOptionListItem(optionListItem);
	}

	private int getNextOptionListItemViewIndex(OptionListItem optionListItem) {
		OptionList optionList = optionListDao.getOptionList(optionListItem.getOptionListId());
		List optionListItems = optionListItemDao.getOptionListItems(optionList);
		return getNextIndexValue(optionListItems, "viewIndex");
	}

	private void updateOptionListItem(OptionListItem optionListItem) {
		optionListItemDao.updateOptionListItem(optionListItem);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveOptionListItem(org.programmerplanet.crm.metadata.OptionListItem)
	 */
	public void saveOptionListItem(OptionListItem optionListItem) {
		if (optionListItem.getId() != null) {
			this.updateOptionListItem(optionListItem);
		}
		else {
			this.insertOptionListItem(optionListItem);
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteOptionListItem(org.programmerplanet.crm.metadata.OptionListItem)
	 */
	public void deleteOptionListItem(OptionListItem optionListItem) {
		optionListItemDao.deleteOptionListItem(optionListItem);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveOptionListItemViewIndex(org.programmerplanet.crm.metadata.OptionListItem, java.lang.String)
	 */
	public void moveOptionListItemViewIndex(OptionListItem optionListItem, String direction) {
		OptionList optionList = optionListDao.getOptionList(optionListItem.getOptionListId());
		List<OptionListItem> optionListItems = optionListItemDao.getOptionListItems(optionList);
		ListUtil.moveElement(optionListItems, optionListItem, direction);

		int index = 0;
		for (OptionListItem item : optionListItems) {
			if (item.getViewIndex() == null || item.getViewIndex().intValue() != index) {
				item.setViewIndex(new Integer(index));
				optionListItemDao.updateOptionListItem(item);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getRelationship(java.util.UUID)
	 */
	public Relationship getRelationship(UUID id) {
		return relationshipDao.getRelationship(id);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getRelationship(java.util.UUID, java.util.UUID)
	 */
	public Relationship getRelationship(UUID parentObjectId, UUID childObjectId) {
		return relationshipDao.getRelationship(parentObjectId, childObjectId);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#getRelationshipsForObject(org.programmerplanet.crm.metadata.ObjectDefinition)
	 */
	public List<Relationship> getRelationshipsForObject(ObjectDefinition objectDefinition) {
		return relationshipDao.getRelationshipsForObject(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#saveRelationship(org.programmerplanet.crm.metadata.Relationship)
	 */
	public void saveRelationship(Relationship relationship) {
		ObjectDefinition parentObjectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getParentObjectId());
		ObjectDefinition childObjectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getChildObjectId());

		String tableName = relationship.generateTableName(parentObjectDefinition, childObjectDefinition);
		relationship.setTableName(tableName);

		int parentViewIndex = getNextRelationshipViewIndex(parentObjectDefinition);
		relationship.setViewIndex(new Integer(parentViewIndex));

		// now create the inverse
		Relationship inverseRelationship = new Relationship();
		inverseRelationship.setParentObjectId(childObjectDefinition.getId());
		inverseRelationship.setChildObjectId(parentObjectDefinition.getId());
		inverseRelationship.setTableName(tableName);

		int childViewIndex = getNextRelationshipViewIndex(childObjectDefinition);
		inverseRelationship.setViewIndex(new Integer(childViewIndex));

		// insert the two...
		relationshipDao.insertRelationship(relationship);
		relationshipDao.insertRelationship(inverseRelationship);
		// create the table...
		schemaManager.createTable(relationship, parentObjectDefinition, childObjectDefinition);
	}

	private int getNextRelationshipViewIndex(ObjectDefinition objectDefinition) {
		List relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		return getNextIndexValue(relationships, "viewIndex");
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#deleteRelationship(org.programmerplanet.crm.metadata.Relationship)
	 */
	public void deleteRelationship(Relationship relationship) {
		// get the inverse
		Relationship inverseRelationship = relationshipDao.getRelationship(relationship.getChildObjectId(), relationship.getParentObjectId());

		// delete the two...
		relationshipDao.deleteRelationship(relationship);
		relationshipDao.deleteRelationship(inverseRelationship);
		// drop the table...
		schemaManager.dropTable(relationship);
	}

	/**
	 * @see org.programmerplanet.crm.metadata.MetadataManager#moveRelationshipViewIndex(org.programmerplanet.crm.metadata.Relationship, java.lang.String)
	 */
	public void moveRelationshipViewIndex(Relationship relationship, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getParentObjectId());
		List<Relationship> relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		ListUtil.moveElement(relationships, relationship, direction);

		int index = 0;
		for (Relationship relationship2 : relationships) {
			if (relationship2.getViewIndex() == null || relationship2.getViewIndex().intValue() != index) {
				relationship2.setViewIndex(new Integer(index));
				relationshipDao.updateRelationship(relationship2);
			}
			index++;
		}
	}

	private int getNextIndexValue(Collection collection, String propertyName) {
		Integer max = getMaxIndexValue(collection, propertyName);
		return (max != null) ? max.intValue() + 1 : 0;
	}

	private Integer getMaxIndexValue(Collection collection, String propertyName) {
		Integer max = null;
		for (Object object : collection) {
			try {
				Integer value = (Integer)PropertyUtils.getProperty(object, propertyName);
				if (max == null) {
					max = value;
				}
				else if (value != null) {
					max = new Integer(Math.max(max.intValue(), value.intValue()));
				}
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		return max;
	}

}

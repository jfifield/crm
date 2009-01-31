package org.programmerplanet.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.programmerplanet.crm.dao.ApplicationDao;
import org.programmerplanet.crm.dao.ApplicationObjectDao;
import org.programmerplanet.crm.dao.FieldDefinitionDao;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.dao.OptionListDao;
import org.programmerplanet.crm.dao.OptionListItemDao;
import org.programmerplanet.crm.dao.RelationshipDao;
import org.programmerplanet.crm.dao.UserDao;
import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ApplicationObject;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.model.User;
import org.programmerplanet.crm.schema.SchemaManager;
import org.programmerplanet.crm.util.ListUtil;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class AdministrationServiceImpl implements AdministrationService {

	private ObjectDefinitionDao objectDefinitionDao;
	private FieldDefinitionDao fieldDefinitionDao;
	private ApplicationDao applicationDao;
	private ApplicationObjectDao applicationObjectDao;
	private RelationshipDao relationshipDao;
	private OptionListDao optionListDao;
	private OptionListItemDao optionListItemDao;
	private FileDao fileDao;
	private UserDao userDao;
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

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setSchemaManager(SchemaManager schemaManager) {
		this.schemaManager = schemaManager;
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getAllApplications()
	 */
	public List getAllApplications() {
		return applicationDao.getAllApplications();
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getApplication(java.lang.Long)
	 */
	public Application getApplication(Long id) {
		return applicationDao.getApplication(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertApplication(org.programmerplanet.crm.model.Application)
	 */
	public void insertApplication(Application application) {
		int viewIndex = getNextApplicationViewIndex();
		application.setViewIndex(new Integer(viewIndex));
		applicationDao.insertApplication(application);
	}

	private int getNextApplicationViewIndex() {
		List applications = applicationDao.getAllApplications();
		return getNextIndexValue(applications, "viewIndex");
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateApplication(org.programmerplanet.crm.model.Application)
	 */
	public void updateApplication(Application application) {
		applicationDao.updateApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteApplication(org.programmerplanet.crm.model.Application)
	 */
	public void deleteApplication(Application application) {
		applicationDao.deleteApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#moveApplicationViewIndex(org.programmerplanet.crm.model.Application, java.lang.String)
	 */
	public void moveApplicationViewIndex(Application application, String direction) {
		List applications = applicationDao.getAllApplications();
		ListUtil.moveElement(applications, application, direction);

		int index = 0;
		for (Iterator i = applications.iterator(); i.hasNext();) {
			Application application2 = (Application)i.next();
			if (application2.getViewIndex() == null || application2.getViewIndex().intValue() != index) {
				application2.setViewIndex(new Integer(index));
				applicationDao.updateApplication(application2);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getObjectDefinitionsForApplication(org.programmerplanet.crm.model.Application)
	 */
	public List getObjectDefinitionsForApplication(Application application) {
		return objectDefinitionDao.getObjectDefinitionsForApplication(application);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getRelatedObjectDefinitionsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public Map getRelatedObjectDefinitionsForObject(ObjectDefinition objectDefinition) {
		Map relatedObjectDefinition = new LinkedHashMap();
		List relationshipForObject = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Iterator i = relationshipForObject.iterator(); i.hasNext();) {
			Relationship relationship = (Relationship)i.next();
			ObjectDefinition childObjectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getChildObjectId());
			relatedObjectDefinition.put(relationship, childObjectDefinition);
		}
		return relatedObjectDefinition;
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getAllObjectDefinitions()
	 */
	public List getAllObjectDefinitions() {
		return objectDefinitionDao.getAllObjectDefinitions();
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getObjectDefinition(java.lang.Long)
	 */
	public ObjectDefinition getObjectDefinition(Long id) {
		return objectDefinitionDao.getObjectDefinition(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void insertObjectDefinition(ObjectDefinition objectDefinition) {
		objectDefinitionDao.insertObjectDefinition(objectDefinition);
		schemaManager.createTable(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void updateObjectDefinition(ObjectDefinition objectDefinition) {
		ObjectDefinition originalObjectDefinition = objectDefinitionDao.getObjectDefinition(objectDefinition.getId());
		objectDefinitionDao.updateObjectDefinition(objectDefinition);
		// rename table if necessary...
		if (!originalObjectDefinition.getTableName().equals(objectDefinition.getTableName())) {
			schemaManager.renameTable(originalObjectDefinition.getTableName(), objectDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteObjectDefinition(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public void deleteObjectDefinition(ObjectDefinition objectDefinition) {
		// drop 'many' relationships
		List relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		for (Iterator i = relationships.iterator(); i.hasNext();) {
			Relationship relationship1 = (Relationship)i.next();
			Relationship relationship2 = relationshipDao.getRelationship(relationship1.getChildObjectId(), relationship1.getParentObjectId());
			relationshipDao.deleteRelationship(relationship1);
			relationshipDao.deleteRelationship(relationship2);
			schemaManager.dropTable(relationship1);
		}

		// drop 'one' relationships
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfObjectType(objectDefinition);
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			ObjectDefinition ownerObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			fieldDefinitionDao.deleteFieldDefinition(fieldDefinition);
			schemaManager.dropColumn(ownerObjectDefinition, fieldDefinition);
		}

		// delete referenced files
		fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
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
	 * @see org.programmerplanet.crm.service.AdministrationService#insertApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void insertApplicationObject(ApplicationObject applicationObject) {
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
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteApplicationObject(org.programmerplanet.crm.model.ApplicationObject)
	 */
	public void deleteApplicationObject(ApplicationObject applicationObject) {
		applicationObjectDao.deleteApplicationObject(applicationObject);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#moveApplicationObjectViewIndex(org.programmerplanet.crm.model.ApplicationObject, java.lang.String)
	 */
	public void moveApplicationObjectViewIndex(ApplicationObject applicationObject, String direction) {
		Application application = applicationDao.getApplication(applicationObject.getApplicationId());
		List applicationObjects = applicationObjectDao.getApplicationObjectsForApplication(application);
		ListUtil.moveElement(applicationObjects, applicationObject, direction);

		int index = 0;
		for (Iterator i = applicationObjects.iterator(); i.hasNext();) {
			ApplicationObject applicationObject2 = (ApplicationObject)i.next();
			if (applicationObject2.getViewIndex() == null || applicationObject2.getViewIndex().intValue() != index) {
				applicationObject2.setViewIndex(new Integer(index));
				applicationObjectDao.updateApplicationObject(applicationObject2);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getFieldDefinition(java.lang.Long)
	 */
	public FieldDefinition getFieldDefinition(Long id) {
		return fieldDefinitionDao.getFieldDefinition(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getFieldDefinitionsForObject(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObject(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getFieldDefinitionsForObjectList(org.programmerplanet.crm.model.ObjectDefinition)
	 */
	public List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition) {
		return fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void insertFieldDefinition(FieldDefinition fieldDefinition) {
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

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void updateFieldDefinition(FieldDefinition fieldDefinition) {
		FieldDefinition originalFieldDefinition = fieldDefinitionDao.getFieldDefinition(fieldDefinition.getId());
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition);
		// rename field if necessary...
		if (!originalFieldDefinition.getColumnName().equals(fieldDefinition.getColumnName())) {
			ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
			schemaManager.renameColumn(objectDefinition, originalFieldDefinition.getColumnName(), fieldDefinition);
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteFieldDefinition(org.programmerplanet.crm.model.FieldDefinition)
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
	 * @see org.programmerplanet.crm.service.AdministrationService#moveFieldDefinitionViewIndex(org.programmerplanet.crm.model.FieldDefinition, java.lang.String)
	 */
	public void moveFieldDefinitionViewIndex(FieldDefinition fieldDefinition, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		ListUtil.moveElement(fieldDefinitions, fieldDefinition, direction);

		int index = 0;
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition field = (FieldDefinition)i.next();
			if (field.getViewIndex() == null || field.getViewIndex().intValue() != index) {
				field.setViewIndex(new Integer(index));
				fieldDefinitionDao.updateFieldDefinition(field);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#moveFieldDefinitionListIndex(org.programmerplanet.crm.model.FieldDefinition, java.lang.String)
	 */
	public void moveFieldDefinitionListIndex(FieldDefinition fieldDefinition, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getObjectId());
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
		ListUtil.moveElement(fieldDefinitions, fieldDefinition, direction);

		int index = 0;
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition field = (FieldDefinition)i.next();
			if (field.getListIndex() == null || field.getListIndex().intValue() != index) {
				field.setListIndex(new Integer(index));
				fieldDefinitionDao.updateFieldDefinition(field);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#addFieldDefinitionListIndex(org.programmerplanet.crm.model.FieldDefinition)
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
	 * @see org.programmerplanet.crm.service.AdministrationService#removeFieldDefinitionListIndex(org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void removeFieldDefinitionListIndex(FieldDefinition fieldDefinition) {
		fieldDefinition.setListIndex(null);
		fieldDefinitionDao.updateFieldDefinition(fieldDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getAllOptionLists()
	 */
	public List getAllOptionLists() {
		return optionListDao.getAllOptionLists();
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getOptionList(java.lang.Long)
	 */
	public OptionList getOptionList(Long id) {
		return optionListDao.getOptionList(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void insertOptionList(OptionList optionList) {
		optionListDao.insertOptionList(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void updateOptionList(OptionList optionList) {
		optionListDao.updateOptionList(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void deleteOptionList(OptionList optionList) {
		// delete referencing columns
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsOfOptionListType(optionList);
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			this.deleteFieldDefinition(fieldDefinition);
		}
		optionListDao.deleteOptionList(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getOptionListItems(org.programmerplanet.crm.model.OptionList)
	 */
	public List getOptionListItems(OptionList optionList) {
		return optionListItemDao.getOptionListItems(optionList);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getOptionListItem(java.lang.Long)
	 */
	public OptionListItem getOptionListItem(Long id) {
		return optionListItemDao.getOptionListItem(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void insertOptionListItem(OptionListItem optionListItem) {
		int viewIndex = getNextOptionListItemViewIndex(optionListItem);
		optionListItem.setViewIndex(new Integer(viewIndex));
		optionListItemDao.insertOptionListItem(optionListItem);
	}

	private int getNextOptionListItemViewIndex(OptionListItem optionListItem) {
		OptionList optionList = optionListDao.getOptionList(optionListItem.getOptionListId());
		List optionListItems = optionListItemDao.getOptionListItems(optionList);
		return getNextIndexValue(optionListItems, "viewIndex");
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void updateOptionListItem(OptionListItem optionListItem) {
		optionListItemDao.updateOptionListItem(optionListItem);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void deleteOptionListItem(OptionListItem optionListItem) {
		optionListItemDao.deleteOptionListItem(optionListItem);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#moveOptionListItemViewIndex(org.programmerplanet.crm.model.OptionListItem, java.lang.String)
	 */
	public void moveOptionListItemViewIndex(OptionListItem optionListItem, String direction) {
		OptionList optionList = optionListDao.getOptionList(optionListItem.getOptionListId());
		List optionListItems = optionListItemDao.getOptionListItems(optionList);
		ListUtil.moveElement(optionListItems, optionListItem, direction);

		int index = 0;
		for (Iterator i = optionListItems.iterator(); i.hasNext();) {
			OptionListItem item = (OptionListItem)i.next();
			if (item.getViewIndex() == null || item.getViewIndex().intValue() != index) {
				item.setViewIndex(new Integer(index));
				optionListItemDao.updateOptionListItem(item);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getRelationship(java.lang.Long)
	 */
	public Relationship getRelationship(Long id) {
		return relationshipDao.getRelationship(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertRelationship(org.programmerplanet.crm.model.Relationship)
	 */
	public void insertRelationship(Relationship relationship) {
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
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteRelationship(org.programmerplanet.crm.model.Relationship)
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
	 * @see org.programmerplanet.crm.service.AdministrationService#moveRelationshipViewIndex(org.programmerplanet.crm.model.Relationship, java.lang.String)
	 */
	public void moveRelationshipViewIndex(Relationship relationship, String direction) {
		ObjectDefinition objectDefinition = objectDefinitionDao.getObjectDefinition(relationship.getParentObjectId());
		List relationships = relationshipDao.getRelationshipsForObject(objectDefinition);
		ListUtil.moveElement(relationships, relationship, direction);

		int index = 0;
		for (Iterator i = relationships.iterator(); i.hasNext();) {
			Relationship relationship2 = (Relationship)i.next();
			if (relationship2.getViewIndex() == null || relationship2.getViewIndex().intValue() != index) {
				relationship2.setViewIndex(new Integer(index));
				relationshipDao.updateRelationship(relationship2);
			}
			index++;
		}
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getAllUsers()
	 */
	public List getAllUsers() {
		return userDao.getAllUsers();
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#getUser(java.lang.Long)
	 */
	public User getUser(Long id) {
		return userDao.getUser(id);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#insertUser(org.programmerplanet.crm.model.User)
	 */
	public void insertUser(User user) {
		userDao.insertUser(user);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#updateUser(org.programmerplanet.crm.model.User)
	 */
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	/**
	 * @see org.programmerplanet.crm.service.AdministrationService#deleteUser(org.programmerplanet.crm.model.User)
	 */
	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}

	private int getNextIndexValue(Collection collection, String propertyName) {
		Integer max = getMaxIndexValue(collection, propertyName);
		return (max != null) ? max.intValue() + 1 : 0;
	}

	private Integer getMaxIndexValue(Collection collection, String propertyName) {
		Integer max = null;
		for (Iterator i = collection.iterator(); i.hasNext();) {
			Object object = (Object)i.next();
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

package org.programmerplanet.crm.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ApplicationObject;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.model.User;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface AdministrationService {

	List getAllApplications();

	Application getApplication(UUID id);

	void insertApplication(Application application);

	void updateApplication(Application application);

	void deleteApplication(Application application);

	void moveApplicationViewIndex(Application application, String direction);

	List getObjectDefinitionsForApplication(Application application);

	Map getRelatedObjectDefinitionsForObject(ObjectDefinition objectDefinition);

	List getAllObjectDefinitions();

	ObjectDefinition getObjectDefinition(UUID id);

	void insertObjectDefinition(ObjectDefinition objectDefinition);

	void updateObjectDefinition(ObjectDefinition objectDefinition);

	void deleteObjectDefinition(ObjectDefinition objectDefinition);

	void insertApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObject(ApplicationObject applicationObject);

	void moveApplicationObjectViewIndex(ApplicationObject applicationObject, String direction);

	FieldDefinition getFieldDefinition(UUID id);

	List getFieldDefinitionsForObject(ObjectDefinition objectDefinition);

	List getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	void insertFieldDefinition(FieldDefinition fieldDefinition);

	void updateFieldDefinition(FieldDefinition fieldDefinition);

	void deleteFieldDefinition(FieldDefinition fieldDefinition);

	void moveFieldDefinitionViewIndex(FieldDefinition fieldDefinition, String direction);

	void moveFieldDefinitionListIndex(FieldDefinition fieldDefinition, String direction);

	void addFieldDefinitionListIndex(FieldDefinition fieldDefinition);

	void removeFieldDefinitionListIndex(FieldDefinition fieldDefinition);

	List getAllOptionLists();

	OptionList getOptionList(UUID id);

	void insertOptionList(OptionList optionList);

	void updateOptionList(OptionList optionList);

	void deleteOptionList(OptionList optionList);

	List getOptionListItems(OptionList optionList);

	OptionListItem getOptionListItem(UUID id);

	void insertOptionListItem(OptionListItem optionListItem);

	void updateOptionListItem(OptionListItem optionListItem);

	void deleteOptionListItem(OptionListItem optionListItem);

	void moveOptionListItemViewIndex(OptionListItem optionListItem, String direction);

	Relationship getRelationship(UUID id);

	void insertRelationship(Relationship relationship);

	void deleteRelationship(Relationship relationship);

	void moveRelationshipViewIndex(Relationship relationship, String direction);

	List getAllUsers();

	User getUser(UUID id);

	void insertUser(User user);

	void updateUser(User user);

	void deleteUser(User user);

}

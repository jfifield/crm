package org.programmerplanet.crm.metadata;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface MetadataManager {

	List<Application> getApplications();

	Application getApplication(UUID id);

	void saveApplication(Application application);

	void deleteApplication(Application application);

	void moveApplicationViewIndex(Application application, String direction);

	List<ObjectDefinition> getObjectDefinitionsForApplication(Application application);

	Map<Relationship, ObjectDefinition> getRelatedObjectDefinitionsForObject(ObjectDefinition objectDefinition);

	List<ObjectDefinition> getObjectDefinitions();

	ObjectDefinition getObjectDefinition(UUID id);

	ObjectDefinition getObjectDefinition(String objectName);

	void saveObjectDefinition(ObjectDefinition objectDefinition);

	void deleteObjectDefinition(ObjectDefinition objectDefinition);

	void saveApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObject(ApplicationObject applicationObject);

	void moveApplicationObjectViewIndex(ApplicationObject applicationObject, String direction);

	FieldDefinition getFieldDefinition(UUID id);

	List<FieldDefinition> getFieldDefinitionsForObject(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsForObjectList(ObjectDefinition objectDefinition);

	List<FieldDefinition> getFieldDefinitionsForObjectView(ObjectDefinition objectDefinition);

	void saveFieldDefinition(FieldDefinition fieldDefinition);

	void deleteFieldDefinition(FieldDefinition fieldDefinition);

	void moveFieldDefinitionViewIndex(FieldDefinition fieldDefinition, String direction);

	void moveFieldDefinitionListIndex(FieldDefinition fieldDefinition, String direction);

	void addFieldDefinitionListIndex(FieldDefinition fieldDefinition);

	void removeFieldDefinitionListIndex(FieldDefinition fieldDefinition);

	List<OptionList> getOptionLists();

	OptionList getOptionList(UUID id);

	void saveOptionList(OptionList optionList);

	void deleteOptionList(OptionList optionList);

	List<OptionListItem> getOptionListItems(OptionList optionList);

	OptionListItem getOptionListItem(UUID id);

	void saveOptionListItem(OptionListItem optionListItem);

	void deleteOptionListItem(OptionListItem optionListItem);

	void moveOptionListItemViewIndex(OptionListItem optionListItem, String direction);

	Relationship getRelationship(UUID id);

	Relationship getRelationship(UUID parentObjectId, UUID childObjectId);

	List<Relationship> getRelationshipsForObject(ObjectDefinition objectDefinition);

	void saveRelationship(Relationship relationship);

	void deleteRelationship(Relationship relationship);

	void moveRelationshipViewIndex(Relationship relationship, String direction);

}

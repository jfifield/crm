package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.Map;

import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface CrmObjectDao {

	List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions);

	List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id);

	List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id);

	Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Long id);

	Long insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data);

	void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, Long id);

	void deleteCrmObject(ObjectDefinition objectDefinition, Long id);

	void insertCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId);

	void deleteCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId);

	void deleteCrmObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId);

	void clearCrmObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value);

}

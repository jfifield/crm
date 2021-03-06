package org.programmerplanet.crm.metadata.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.metadata.Application;
import org.programmerplanet.crm.metadata.ObjectDefinition;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ObjectDefinitionDao {

	List<ObjectDefinition> getObjectDefinitions();

	ObjectDefinition getObjectDefinition(UUID id);

	ObjectDefinition getObjectDefinition(String objectName);

	List<ObjectDefinition> getObjectDefinitionsForApplication(Application application);

	void insertObjectDefinition(ObjectDefinition objectDefinition);

	void updateObjectDefinition(ObjectDefinition objectDefinition);

	void deleteObjectDefinition(ObjectDefinition objectDefinition);

	boolean isObjectNameUnique(UUID id, String objectName);

}

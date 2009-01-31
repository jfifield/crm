package org.programmerplanet.crm.dao;

import java.util.List;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ObjectDefinitionDao {

	List getAllObjectDefinitions();

	ObjectDefinition getObjectDefinition(Long id);

	ObjectDefinition getObjectDefinition(String objectName);

	List getObjectDefinitionsForApplication(Application application);

	void insertObjectDefinition(ObjectDefinition objectDefinition);

	void updateObjectDefinition(ObjectDefinition objectDefinition);

	void deleteObjectDefinition(ObjectDefinition objectDefinition);

	boolean isObjectNameUnique(Long id, String objectName);

}

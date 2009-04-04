package org.programmerplanet.crm.metadata.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.metadata.Application;
import org.programmerplanet.crm.metadata.ApplicationObject;
import org.programmerplanet.crm.metadata.ObjectDefinition;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationObjectDao {

	List<ApplicationObject> getApplicationObjectsForApplication(Application application);

	ApplicationObject getApplicationObject(UUID applicationId, UUID objectId);

	void insertApplicationObject(ApplicationObject applicationObject);

	void updateApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObjects(ObjectDefinition objectDefinition);

}

package org.programmerplanet.crm.dao;

import java.util.List;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.model.ApplicationObject;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationObjectDao {

	List getApplicationObjectsForApplication(Application application);

	ApplicationObject getApplicationObject(Long applicationId, Long objectId);

	void insertApplicationObject(ApplicationObject applicationObject);

	void updateApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObject(ApplicationObject applicationObject);

	void deleteApplicationObjects(ObjectDefinition objectDefinition);

}

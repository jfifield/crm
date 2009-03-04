package org.programmerplanet.crm.dao;

import java.util.List;
import java.util.UUID;

import org.programmerplanet.crm.model.Application;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationDao {

	List<Application> getAllApplications();

	Application getApplication(UUID id);

	void insertApplication(Application application);

	void updateApplication(Application application);

	void deleteApplication(Application application);

	boolean isApplicationNameUnique(UUID id, String applicationName);

}

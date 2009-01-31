package org.programmerplanet.crm.dao;

import java.util.List;

import org.programmerplanet.crm.model.Application;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface ApplicationDao {

	List getAllApplications();

	Application getApplication(Long id);

	void insertApplication(Application application);

	void updateApplication(Application application);

	void deleteApplication(Application application);

	boolean isApplicationNameUnique(Long id, String applicationName);

}

package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.Application;
import org.programmerplanet.crm.metadata.dao.ApplicationDao;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationValidator implements Validator {

	private ApplicationDao applicationDao;

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public boolean supports(Class clazz) {
		return clazz.equals(Application.class);
	}

	public void validate(Object obj, Errors errors) {
		Application application = (Application)obj;

		// application name is required
		if (StringUtils.isEmpty(application.getApplicationName())) {
			errors.rejectValue("applicationName", "error.required");
		}
		// application name must be unique
		else if (!applicationDao.isApplicationNameUnique(application.getId(), application.getApplicationName())) {
			errors.rejectValue("applicationName", "error.applicationName.exists");
		}
	}

}

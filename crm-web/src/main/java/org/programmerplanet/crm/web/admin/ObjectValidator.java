package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectValidator implements Validator {

	private ObjectDefinitionDao objectDefinitionDao;

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public boolean supports(Class clazz) {
		return clazz.equals(ObjectDefinition.class);
	}

	public void validate(Object obj, Errors errors) {
		ObjectDefinition objectDefinition = (ObjectDefinition)obj;
		
		// object name is required
		if (StringUtils.isEmpty(objectDefinition.getObjectName())) {
			errors.rejectValue("objectName", "error.required");
		}
		// object name must be unique
		else if (!objectDefinitionDao.isObjectNameUnique(objectDefinition.getId(), objectDefinition.getObjectName())) {
			errors.rejectValue("objectName", "error.objectName.exists");
		}
	}

}

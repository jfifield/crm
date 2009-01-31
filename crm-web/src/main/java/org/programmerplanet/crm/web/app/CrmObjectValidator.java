package org.programmerplanet.crm.web.app;

import java.util.Iterator;

import org.programmerplanet.crm.model.CrmObject;
import org.programmerplanet.crm.model.FieldDefinition;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class CrmObjectValidator implements Validator {

	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return clazz.equals(CrmObject.class);
	}

	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		CrmObject crmObject = (CrmObject) obj;
		for (Iterator i = crmObject.getFieldDefinitions().iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			validateField(crmObject, fieldDefinition, errors);
		}
	}

	private void validateField(CrmObject crmObject, FieldDefinition fieldDefinition, Errors errors) {
		boolean required = fieldDefinition.isRequired();
		if (required) {
			String columnName = fieldDefinition.getColumnName();
			Object value = crmObject.getData().get(columnName);
			if (value == null) {
				errors.rejectValue("data[" + columnName + "]", "error.required");
			}
		}
	}

}

package org.programmerplanet.crm.web.app;

import java.util.Iterator;

import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectDataValidator implements Validator {

	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return clazz.equals(ObjectData.class);
	}

	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		ObjectData objectData = (ObjectData) obj;
		for (Iterator i = objectData.getFieldDefinitions().iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			validateField(objectData, fieldDefinition, errors);
		}
	}

	private void validateField(ObjectData objectData, FieldDefinition fieldDefinition, Errors errors) {
		boolean required = fieldDefinition.isRequired();
		if (required) {
			String columnName = fieldDefinition.getColumnName();
			Object value = objectData.getData().get(columnName);
			if (value == null) {
				errors.rejectValue("data[" + columnName + "]", "error.required");
			}
		}
	}

}

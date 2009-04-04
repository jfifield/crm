package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class FieldValidator implements Validator {

	private FieldDefinitionDao fieldDefinitionDao;

	public void setFieldDefinitionDao(FieldDefinitionDao fieldDefinitionDao) {
		this.fieldDefinitionDao = fieldDefinitionDao;
	}

	public boolean supports(Class clazz) {
		return clazz.equals(FieldDefinition.class);
	}

	public void validate(Object obj, Errors errors) {
		FieldDefinition fieldDefinition = (FieldDefinition)obj;
		
		// field name is required
		if (StringUtils.isEmpty(fieldDefinition.getFieldName())) {
			errors.rejectValue("fieldName", "error.required");
		}
		// field name must be unique (within object)
		else if (!fieldDefinitionDao.isFieldNameUnique(fieldDefinition.getObjectId(), fieldDefinition.getId(), fieldDefinition.getFieldName())) {
			errors.rejectValue("fieldName", "error.fieldName.exists");
		}
		
		// data type is required
		if (fieldDefinition.getDataType() == null) {
			errors.rejectValue("dataType", "error.required");
		}
		else {
			if (DataType.SHORT_TEXT.equals(fieldDefinition.getDataType())) {
				// 1-255
				if (fieldDefinition.getDataTypeExt() == null) {
					errors.rejectValue("dataTypeExt", "error.required");
				}
				else if (fieldDefinition.getDataTypeExt().intValue() < 1 || fieldDefinition.getDataTypeExt().intValue() > 255) {
					errors.rejectValue("dataTypeExt", "error.integerRange", new Object[] { new Integer(1), new Integer(255) }, null);
				}
			}
			else if (DataType.LONG_TEXT.equals(fieldDefinition.getDataType())) {
				// 1,2,3
				if (fieldDefinition.getDataTypeExt() == null) {
					errors.rejectValue("dataTypeExt", "error.required");
				}
				else if (fieldDefinition.getDataTypeExt().intValue() < 1 || fieldDefinition.getDataTypeExt().intValue() > 3) {
					errors.rejectValue("dataTypeExt", "error.integerRange", new Object[] { new Integer(1), new Integer(3) }, null);
				}
			}
			else if (DataType.NUMBER.equals(fieldDefinition.getDataType())) {
				// 0-6
				if (fieldDefinition.getDataTypeExt() == null) {
					errors.rejectValue("dataTypeExt", "error.required");
				}
				else if (fieldDefinition.getDataTypeExt().intValue() < 0 || fieldDefinition.getDataTypeExt().intValue() > 6) {
					errors.rejectValue("dataTypeExt", "error.integerRange", new Object[] { new Integer(0), new Integer(6) }, null);
				}
			}
			else if (DataType.MONEY.equals(fieldDefinition.getDataType())) {
				// 0-6
				if (fieldDefinition.getDataTypeExt() == null) {
					errors.rejectValue("dataTypeExt", "error.required");
				}
				else if (fieldDefinition.getDataTypeExt().intValue() < 0 || fieldDefinition.getDataTypeExt().intValue() > 6) {
					errors.rejectValue("dataTypeExt", "error.integerRange", new Object[] { new Integer(0), new Integer(6) }, null);
				}
			}
			else if (DataType.PERCENT.equals(fieldDefinition.getDataType())) {
				// 0-6
				if (fieldDefinition.getDataTypeExt() == null) {
					errors.rejectValue("dataTypeExt", "error.required");
				}
				else if (fieldDefinition.getDataTypeExt().intValue() < 0 || fieldDefinition.getDataTypeExt().intValue() > 6) {
					errors.rejectValue("dataTypeExt", "error.integerRange", new Object[] { new Integer(0), new Integer(6) }, null);
				}
			}
			else if (DataType.OPTION_LIST.equals(fieldDefinition.getDataType())) {
				// list id
				if (fieldDefinition.getDataTypeExtId() == null) {
					errors.rejectValue("dataTypeExtId", "error.required");
				}
			}
			else if (DataType.OBJECT.equals(fieldDefinition.getDataType())) {
				// object id
				if (fieldDefinition.getDataTypeExtId() == null) {
					errors.rejectValue("dataTypeExtId", "error.required");
				}
			}
		}
	}

}

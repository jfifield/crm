package org.programmerplanet.crm.web.converter;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class BooleanConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		Boolean booleanValue = Boolean.FALSE;
		if (value != null) {
			booleanValue = (Boolean)value;
		}
		return booleanValue.booleanValue() ? "Yes" : "No";
	}

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.String, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException {
		Boolean result = Boolean.FALSE;
		if (StringUtils.isNotEmpty(value)) {
			result = new Boolean(value);
		}
		return result;
	}

}

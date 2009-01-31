package org.programmerplanet.crm.converter;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.model.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			if (value instanceof Number) {
				Number number = (Number)value;
				return Long.toString(number.intValue());
			}
			else {
				return value.toString();
			}
		}
		return "";
	}

	/**
	 * @see org.programmerplanet.crm.converter.Converter#convert(java.lang.String, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException {
		Long result = null;
		if (StringUtils.isNotEmpty(value)) {
			result = new Long(value);
		}
		return result;
	}

}

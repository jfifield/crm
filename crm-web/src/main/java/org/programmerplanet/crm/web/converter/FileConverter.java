package org.programmerplanet.crm.web.converter;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class FileConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
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
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.String, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException {
		Long result = null;
		if (StringUtils.isNotEmpty(value)) {
			result = new Long(value);
		}
		return result;
	}

}

package org.programmerplanet.crm.web.converter;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			return value.toString();
		}
		return "";
	}

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.String, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException {
		UUID result = null;
		if (StringUtils.isNotEmpty(value)) {
			result = UUID.fromString(value);
		}
		return result;
	}

}

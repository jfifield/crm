package org.programmerplanet.crm.web.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DateConverter implements Converter {

	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			if (value instanceof Date) {
				DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
				Date date = (Date)value;
				String str = dateFormat.format(date);
				return str;
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
		Date result = null;
		if (StringUtils.isNotEmpty(value)) {
			DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			try {
				result = dateFormat.parse(value);
			}
			catch (ParseException e) {
				throw new ConversionException(e);
			}
		}
		return result;
	}

}

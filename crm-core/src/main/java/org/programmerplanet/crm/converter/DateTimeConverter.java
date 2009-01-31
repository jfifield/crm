package org.programmerplanet.crm.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.model.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DateTimeConverter implements Converter {

	public static final String DEFAULT_DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm a";

	/**
	 * @see org.programmerplanet.crm.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			if (value instanceof Date) {
				DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
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
	 * @see org.programmerplanet.crm.converter.Converter#convert(java.lang.String, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException {
		Date result = null;
		if (StringUtils.isNotEmpty(value)) {
			DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
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

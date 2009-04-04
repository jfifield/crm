package org.programmerplanet.crm.web.converter;

import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class NumberConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.web.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			if (value instanceof Number) {
				NumberFormat numberFormat = NumberFormat.getNumberInstance();
				int fractionDigits = fieldDefinition.getDataTypeExt() != null ? fieldDefinition.getDataTypeExt().intValue() : 0;
				numberFormat.setMinimumFractionDigits(fractionDigits);
				numberFormat.setMaximumFractionDigits(fractionDigits);
				Number number = (Number)value;
				String str = numberFormat.format(number);
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
		Number result = null;
		if (StringUtils.isNotEmpty(value)) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			try {
				result = numberFormat.parse(value);
			}
			catch (ParseException e) {
				throw new ConversionException(e);
			}
		}
		return result;
	}

}

package org.programmerplanet.crm.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.model.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PercentConverter implements Converter {

	/**
	 * @see org.programmerplanet.crm.converter.Converter#convert(java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException {
		if (value != null) {
			if (value instanceof Number) {
				DecimalFormat decimalFormat = getDecimalFormat(fieldDefinition);
				Number number = (Number)value;
				String str = decimalFormat.format(number);
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
		Number result = null;
		if (StringUtils.isNotEmpty(value)) {
			if (value.charAt(value.length() - 1) != '%') {
				value += "%";
			}
			DecimalFormat decimalFormat = getDecimalFormat(fieldDefinition);
			try {
				result = decimalFormat.parse(value);
			}
			catch (ParseException e) {
				throw new ConversionException(e);
			}
		}
		return result;
	}

	private DecimalFormat getDecimalFormat(FieldDefinition fieldDefinition) {
		DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getPercentInstance();
		int fractionDigits = fieldDefinition.getDataTypeExt() != null ? fieldDefinition.getDataTypeExt().intValue() : 0;
		decimalFormat.setMinimumFractionDigits(fractionDigits);
		decimalFormat.setMaximumFractionDigits(fractionDigits);
		return decimalFormat;
	}

}

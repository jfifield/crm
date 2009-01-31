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
public class MoneyConverter implements Converter {

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
			boolean hasNegativeSign = value.charAt(0) == '-' || value.charAt(1) == '-';
			boolean hasCurrencySign = value.charAt(0) == '$' || value.charAt(1) == '$';
			if (hasNegativeSign) {
				value = value.substring(1);
			}
			if (hasCurrencySign) {
				value = value.substring(1);
			}
			value = "$" + value;
			if (hasNegativeSign) {
				value = "-" + value;
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
		DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getCurrencyInstance();
		int fractionDigits = fieldDefinition.getDataTypeExt() != null ? fieldDefinition.getDataTypeExt().intValue() : 0;
		decimalFormat.setMinimumFractionDigits(fractionDigits);
		decimalFormat.setMaximumFractionDigits(fractionDigits);
		decimalFormat.setPositivePrefix("$");
		decimalFormat.setPositiveSuffix("");
		decimalFormat.setNegativePrefix("-$");
		decimalFormat.setNegativeSuffix("");
		return decimalFormat;
	}

}

package org.programmerplanet.crm.converter;

import org.programmerplanet.crm.metadata.FieldDefinition;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class MoneyConverterTest extends TestCase {

	public void testConvertObjectFieldDefinition_Positive() {
		MoneyConverter converter = new MoneyConverter();
		Double value = new Double(2.34);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		String convertedValue = converter.convert(value, fieldDefinition);
		assertEquals("converted value", "$2.34", convertedValue);
	}

	public void testConvertObjectFieldDefinition_Negative() {
		MoneyConverter converter = new MoneyConverter();
		Double value = new Double(-2.34);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		String convertedValue = converter.convert(value, fieldDefinition);
		assertEquals("converted value", "-$2.34", convertedValue);
	}

	public void testConvertStringFieldDefinition_Positive_WithCurrencySymbol() {
		MoneyConverter converter = new MoneyConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("$2.34", fieldDefinition);
		assertEquals("converted value", new Double(2.34), convertedValue);
	}

	public void testConvertStringFieldDefinition_Positive_WithoutCurrencySymbol() {
		MoneyConverter converter = new MoneyConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("2.34", fieldDefinition);
		assertEquals("converted value", new Double(2.34), convertedValue);
	}

	public void testConvertStringFieldDefinition_Negative_WithCurrencySymbol() {
		MoneyConverter converter = new MoneyConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("-$2.34", fieldDefinition);
		assertEquals("converted value", new Double(-2.34), convertedValue);
	}

	public void testConvertStringFieldDefinition_Negative_WithoutCurrencySymbol() {
		MoneyConverter converter = new MoneyConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("-2.34", fieldDefinition);
		assertEquals("converted value", new Double(-2.34), convertedValue);
	}

}

package org.programmerplanet.crm.converter;

import org.programmerplanet.crm.metadata.FieldDefinition;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PercentConverterTest extends TestCase {

	public void testConvertObjectFieldDefinition_Positive() {
		PercentConverter converter = new PercentConverter();
		Double value = new Double(.2345);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		String convertedValue = converter.convert(value, fieldDefinition);
		assertEquals("converted value", "23.45%", convertedValue);
	}

	public void testConvertObjectFieldDefinition_Negative() {
		PercentConverter converter = new PercentConverter();
		Double value = new Double(-.2345);
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		String convertedValue = converter.convert(value, fieldDefinition);
		assertEquals("converted value", "-23.45%", convertedValue);
	}

	public void testConvertStringFieldDefinition_Positive_WithPercentSymbol() {
		PercentConverter converter = new PercentConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("23.45%", fieldDefinition);
		assertEquals("converted value", new Double(.2345), convertedValue);
	}

	public void testConvertStringFieldDefinition_Positive_WithoutPercentSymbol() {
		PercentConverter converter = new PercentConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("23.45", fieldDefinition);
		assertEquals("converted value", new Double(.2345), convertedValue);
	}

	public void testConvertStringFieldDefinition_Negative_WithPercentSymbol() {
		PercentConverter converter = new PercentConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("-23.45%", fieldDefinition);
		assertEquals("converted value", new Double(-.2345), convertedValue);
	}

	public void testConvertStringFieldDefinition_Negative_WithoutPercentSymbol() {
		PercentConverter converter = new PercentConverter();
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setDataTypeExt(new Long(2));
		Object convertedValue = converter.convert("-23.45", fieldDefinition);
		assertEquals("converted value", new Double(-.2345), convertedValue);
	}
	
}

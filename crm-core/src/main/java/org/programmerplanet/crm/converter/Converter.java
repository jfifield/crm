package org.programmerplanet.crm.converter;

import org.programmerplanet.crm.model.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface Converter {

	String convert(Object value, FieldDefinition fieldDefinition) throws ConversionException;
	
	Object convert(String value, FieldDefinition fieldDefinition) throws ConversionException;
	
}

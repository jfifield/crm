package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.web.converter.Converter;
import org.programmerplanet.crm.web.converter.NumberConverter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class AutoNumberRenderer implements FieldRenderer {

	private Converter converter = new NumberConverter();

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write(getAsString(fieldDefinition, value));
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write(getAsString(fieldDefinition, value));
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write(getAsString(fieldDefinition, value));
	}

	private String getAsString(FieldDefinition fieldDefinition, Object value) {
		String stringValue = converter.convert(value, fieldDefinition);
		if (StringUtils.isEmpty(stringValue)) {
			stringValue = StringEscapeUtils.escapeHtml("<auto>");
		}
		return stringValue;
	}

}

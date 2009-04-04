package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.web.converter.Converter;
import org.programmerplanet.crm.web.converter.PercentConverter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PercentRenderer implements FieldRenderer {

	private Converter converter = new PercentConverter();
	
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
		writer.write("<input type=\"text\"");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
		writer.write(" size=\"10\"");
		writer.write(" maxlength=\"20\"");
		writer.write(" value=\"");
		writer.write(getAsString(fieldDefinition, value));
		writer.write("\"/>");
	}

	private String getAsString(FieldDefinition fieldDefinition, Object value) {
		return converter.convert(value, fieldDefinition);
	}

}

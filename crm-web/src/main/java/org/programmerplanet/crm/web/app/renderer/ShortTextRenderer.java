package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.web.converter.Converter;
import org.programmerplanet.crm.web.converter.TextConverter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ShortTextRenderer implements FieldRenderer {

	private Converter converter = new TextConverter();
	
	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		if (value != null) {
			writer.write(getAsString(value, fieldDefinition));
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		if (value != null) {
			writer.write(getAsString(value, fieldDefinition));
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write("<input type=\"text\"");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
		writer.write(" size=\"" + Math.max(fieldDefinition.getDataTypeExt().intValue(), 50) + "\"");
		writer.write(" maxlength=\"" + fieldDefinition.getDataTypeExt() + "\"");
		writer.write(" value=\"");
		if (value != null) {
			writer.write(getAsString(value, fieldDefinition));
		}
		writer.write("\"/>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return StringEscapeUtils.escapeHtml(converter.convert(value, fieldDefinition));
	}

}

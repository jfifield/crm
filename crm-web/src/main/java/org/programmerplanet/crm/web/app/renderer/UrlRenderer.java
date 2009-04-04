package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.converter.TextConverter;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UrlRenderer implements FieldRenderer {

	private Converter converter = new TextConverter();
	
	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write("<a href=\"");
			writer.write(str);
			writer.write("\" target=\"_blank\">");
			writer.write(str);
			writer.write("</a>");
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write("<a href=\"");
			writer.write(str);
			writer.write("\" target=\"_blank\">");
			writer.write(str);
			writer.write("</a>");
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write("<input type=\"text\"");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
		writer.write(" size=\"50\"");
		writer.write(" maxlength=\"100\"");
		writer.write(" value=\"");
		String str = getAsString(value, fieldDefinition);
		writer.write(str);
		writer.write("\"/>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

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
public class LongTextRenderer implements FieldRenderer {

	private Converter converter = new TextConverter();

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		if (value != null) {
			String strValue = getAsString(value, fieldDefinition);
			strValue = strValue.replaceAll("(\r\n|\n|\r)", "<br/>");
			writer.write(strValue);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		if (value != null) {
			String strValue = getAsString(value, fieldDefinition);
			strValue = strValue.replaceAll("(\r\n|\n|\r)", "<br/>");
			writer.write(strValue);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write("<textarea");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
		if (fieldDefinition.getDataTypeExt().intValue() == 1) {
			writer.write(" cols=\"25\"");
			writer.write(" rows=\"5\"");
		}
		else if (fieldDefinition.getDataTypeExt().intValue() == 2) {
			writer.write(" cols=\"50\"");
			writer.write(" rows=\"10\"");
		}
		else if (fieldDefinition.getDataTypeExt().intValue() == 3) {
			writer.write(" cols=\"75\"");
			writer.write(" rows=\"25\"");
		}
		else {
			throw new IllegalArgumentException("DataType extended information is invalid for Long Text: " + fieldDefinition.getDataTypeExt());
		}
		writer.write(">");
		if (value != null) {
			writer.write(getAsString(value, fieldDefinition));
		}
		writer.write("</textarea>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return StringEscapeUtils.escapeHtml(converter.convert(value, fieldDefinition));
	}

}

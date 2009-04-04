package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.web.converter.Converter;
import org.programmerplanet.crm.web.converter.DateTimeConverter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DateTimeRenderer implements FieldRenderer {

	private Converter converter = new DateTimeConverter();
	
	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write(getAsString(value, fieldDefinition));
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write(getAsString(value, fieldDefinition));
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		// output text input
		writer.write("<input type=\"text\"");
		writer.write(" id=\"" + fieldDefinition.getColumnName() + "\"");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
		writer.write(" size=\"22\"");
		writer.write(" maxlength=\"32\"");
		writer.write(" value=\"");
		writer.write(getAsString(value, fieldDefinition));
		writer.write("\"/>");
		
		// output calendar popup
		writer.write("<a href=\"#\"");
		writer.write(" name=\"a_" + fieldDefinition.getColumnName() + "\"");
		writer.write(" id=\"a_" + fieldDefinition.getColumnName() + "\">");
		writer.write("<img src=\"../theme/default/calendar.png\"/>");
		writer.write("</a>");

		writer.write("<script type=\"text/javascript\">");
		writer.write("Calendar.setup({");
		writer.write("inputField:\"" + fieldDefinition.getColumnName() + "\",");
		writer.write("ifFormat:\"%m/%d/%Y %I:%M %p\",");
		writer.write("button:\"a_" + fieldDefinition.getColumnName() + "\",");
		writer.write("singleClick:false,");
		writer.write("weekNumbers:false,");
		writer.write("showsTime:true,");
		writer.write("timeFormat:\"12\"");
		writer.write("});");
		writer.write("</script>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

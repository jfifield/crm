package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.converter.FileConverter;
import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.FileInfo;
import org.programmerplanet.crm.metadata.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class FileRenderer implements FieldRenderer {

	private Converter converter = new FileConverter();

	private DataManager dataManager;

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		renderListOrViewField(writer, value, fieldDefinition);
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		renderListOrViewField(writer, value, fieldDefinition);
	}

	private void renderListOrViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {

			// get metadata required to build file download link
			UUID id = UUID.fromString(str);
			FileInfo fileInfo = dataManager.getFileInfo(id);

			// build file download link
			writer.write("<a href=\"");
			writer.write("download?id=" + id);
			writer.write("\">");
			writer.write("<img src=\"../theme/default/attach.png\"/>");
			writer.write(fileInfo.getFileName());
			writer.write("</a>");
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			renderListOrViewField(writer, value, fieldDefinition);
			writer.write("<input type=\"hidden\"");
			writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
			writer.write(" value=\"" + str + "\"");
			writer.write("\"/>");
		}
		else {
			writer.write("<input type=\"file\"");
			writer.write(" name=\"" + fieldDefinition.getColumnName() + "\"");
			writer.write(" size=\"35\"");
			writer.write("\"/>");
		}
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.OptionList;
import org.programmerplanet.crm.metadata.OptionListItem;
import org.programmerplanet.crm.metadata.dao.OptionListDao;
import org.programmerplanet.crm.metadata.dao.OptionListItemDao;
import org.programmerplanet.crm.web.converter.Converter;
import org.programmerplanet.crm.web.converter.TextConverter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionRenderer implements FieldRenderer {

	private Converter converter = new TextConverter();

	private OptionListDao optionListDao;
	private OptionListItemDao optionListItemDao;
	
	public void setOptionListDao(OptionListDao optionListDao) {
		this.optionListDao = optionListDao;
	}

	public void setOptionListItemDao(OptionListItemDao optionListItemDao) {
		this.optionListItemDao = optionListItemDao;
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write(str);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write(str);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write("<select");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\">");
		writer.write("<option value=\"\"></option>");
		
		String str = getAsString(value, fieldDefinition);
		
		OptionList optionList = optionListDao.getOptionList(fieldDefinition.getDataTypeExtId());
		List optionListItems = optionListItemDao.getOptionListItems(optionList);
		
		for (Iterator i = optionListItems.iterator(); i.hasNext();) {
			OptionListItem item = (OptionListItem)i.next();
			writer.write("<option value=\"");
			writer.write(item.getValue());
			writer.write("\"");
			if (item.getValue().equals(str)) {
				writer.write(" selected=\"selected\"");
			}
			writer.write(">");
			writer.write(item.getValue());
			writer.write("</option>");
		}
		
		writer.write("</select>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

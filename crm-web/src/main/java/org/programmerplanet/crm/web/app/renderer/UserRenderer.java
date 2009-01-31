package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.converter.TextConverter;
import org.programmerplanet.crm.dao.UserDao;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.User;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UserRenderer implements FieldRenderer {

	private Converter converter = new TextConverter();

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderListField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write(str);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderViewField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		String str = getAsString(value, fieldDefinition);
		if (StringUtils.isNotEmpty(str)) {
			writer.write(str);
		}
	}

	/**
	 * @see org.programmerplanet.crm.web.app.renderer.FieldRenderer#renderEditField(java.io.Writer, java.lang.Object, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException {
		writer.write("<select");
		writer.write(" name=\"" + fieldDefinition.getColumnName() + "\">");
		writer.write("<option value=\"\"></option>");
		
		String str = getAsString(value, fieldDefinition);
		
		List users = userDao.getAllUsers();
		for (Iterator i = users.iterator(); i.hasNext();) {
			User user = (User)i.next();
			writer.write("<option value=\"");
			writer.write(user.getUsername());
			writer.write("\"");
			if (user.getUsername().equals(str)) {
				writer.write("selected=\"selected\"");
			}
			writer.write(">");
			writer.write(user.getUsername());
			writer.write("</option>");
		}
		
		writer.write("</select>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.converter.ObjectConverter;
import org.programmerplanet.crm.dao.CrmObjectDao;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectRenderer implements FieldRenderer {

	private Converter converter = new ObjectConverter();

	private ObjectDefinitionDao objectDefinitionDao;
	private FieldDefinitionDao fieldDefinitionDao;
	private CrmObjectDao crmObjectDao;
	private Map converters;

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public void setFieldDefinitionDao(FieldDefinitionDao fieldDefinitionDao) {
		this.fieldDefinitionDao = fieldDefinitionDao;
	}

	public void setCrmObjectDao(CrmObjectDao crmObjectDao) {
		this.crmObjectDao = crmObjectDao;
	}

	public void setConverters(Map converters) {
		this.converters = converters;
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

			// get metadata required to build object title link
			ObjectDefinition refObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getDataTypeExtId());
			List refFieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(refObjectDefinition);
			FieldDefinition refFieldDefinition = (FieldDefinition)refFieldDefinitions.get(0);
			refFieldDefinitions.clear();
			refFieldDefinitions.add(refFieldDefinition);
			Converter objectTitleConverter = (Converter)converters.get(refFieldDefinition.getDataType());

			// get object data to build object title link
			UUID id = UUID.fromString(str);
			Map objectData = crmObjectDao.getCrmObject(refObjectDefinition, refFieldDefinitions, id);
			Object objectTitleValue = objectData.get(refFieldDefinition.getColumnName());
			String objectTitle = null;
			if (objectTitleConverter != null) {
				objectTitle = objectTitleConverter.convert(objectTitleValue, refFieldDefinition);
			}
			else {
				objectTitle = objectTitleValue != null ? objectTitleValue.toString() : "";
			}

			// build object reference link
			writer.write("<a href=\"");
			writer.write(refObjectDefinition.getObjectName() + ".view?id=" + id);
			writer.write("\">");
			writer.write(objectTitle);
			writer.write("</a>");
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

		// get metadata required to build object title link
		ObjectDefinition refObjectDefinition = objectDefinitionDao.getObjectDefinition(fieldDefinition.getDataTypeExtId());
		List refFieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(refObjectDefinition);
		FieldDefinition refFieldDefinition = (FieldDefinition)refFieldDefinitions.get(0);
		refFieldDefinitions.clear();
		refFieldDefinitions.add(refFieldDefinition);
		Converter objectTitleConverter = (Converter)converters.get(refFieldDefinition.getDataType());

		List crmObjects = crmObjectDao.getCrmObjects(refObjectDefinition, refFieldDefinitions);

		for (Iterator i = crmObjects.iterator(); i.hasNext();) {
			Map objectData = (Map)i.next();

			String id = objectData.get("id").toString();
			Object objectTitleValue = objectData.get(refFieldDefinition.getColumnName());
			String objectTitle = null;
			if (objectTitleConverter != null) {
				objectTitle = objectTitleConverter.convert(objectTitleValue, refFieldDefinition);
			}
			else {
				objectTitle = objectTitleValue != null ? objectTitleValue.toString() : "";
			}

			writer.write("<option value=\"");
			writer.write(id);
			writer.write("\"");
			if (id.equals(str)) {
				writer.write(" selected=\"selected\"");
			}
			writer.write(">");
			writer.write(objectTitle);
			writer.write("</option>");
		}

		writer.write("</select>");
	}

	private String getAsString(Object value, FieldDefinition fieldDefinition) {
		return converter.convert(value, fieldDefinition);
	}

}

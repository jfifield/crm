package org.programmerplanet.crm.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class CrmObject {

	private UUID id;
	private Map<String, Object> data;
	private ObjectDefinition objectDefinition;
	private List<FieldDefinition> fieldDefinitions;

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}

	public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}

	public void setFieldDefinitions(List<FieldDefinition> fieldDefinitions) {
		this.fieldDefinitions = fieldDefinitions;
	}

	public List<FieldDefinition> getFieldDefinitions() {
		return fieldDefinitions;
	}

}
package org.programmerplanet.crm.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class CrmObject {

	private UUID id;
	private Map data;
	private ObjectDefinition objectDefinition;
	private List fieldDefinitions;

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public Map getData() {
		return data;
	}

	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}

	public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}

	public void setFieldDefinitions(List fieldDefinitions) {
		this.fieldDefinitions = fieldDefinitions;
	}

	public List getFieldDefinitions() {
		return fieldDefinitions;
	}

}
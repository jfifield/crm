package org.programmerplanet.crm.web.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.UUID;

/**
 * Property editor for <code>java.util.UUID</code>.
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2009 Joseph Fifield
 */
public class UUIDPropertyEditor extends PropertyEditorSupport {

	public void setAsText(String text) throws IllegalArgumentException {
		UUID value = UUID.fromString(text);
		this.setValue(value);
	}

}

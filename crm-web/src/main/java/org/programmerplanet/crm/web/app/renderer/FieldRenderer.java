package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;

import org.programmerplanet.crm.model.FieldDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface FieldRenderer {

	void renderListField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException;

	void renderViewField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException;

	void renderEditField(Writer writer, Object value, FieldDefinition fieldDefinition) throws IOException;

}

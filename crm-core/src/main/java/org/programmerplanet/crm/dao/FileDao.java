package org.programmerplanet.crm.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface FileDao {
	
	FileInfo getFileInfo(UUID id);

	void insertFile(FileInfo fileInfo, InputStream inputStream);

	void getFile(UUID id, OutputStream outputStream);

	void deleteFile(UUID id);

	void deleteFiles(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition);
	
}

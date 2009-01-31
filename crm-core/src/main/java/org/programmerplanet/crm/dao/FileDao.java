package org.programmerplanet.crm.dao;

import java.io.InputStream;
import java.io.OutputStream;

import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface FileDao {
	
	FileInfo getFileInfo(Long id);

	void insertFile(FileInfo fileInfo, InputStream inputStream);

	void getFile(Long id, OutputStream outputStream);

	void deleteFile(Long id);

	void deleteFiles(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition);
	
}

package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.FileInfo;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class FileInfoRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		FileInfo fileInfo = new FileInfo();
		fileInfo.setId(new Long(rs.getLong("id")));
		fileInfo.setFileName(rs.getString("file_name"));
		fileInfo.setFileSize(rs.getLong("file_size"));
		fileInfo.setMimeType(rs.getString("mime_type"));
		return fileInfo;
	}

}
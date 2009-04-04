package org.programmerplanet.crm.dao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcFileDao extends JdbcDaoSupport implements FileDao {

	private LobHandler lobHandler = new DefaultLobHandler();

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#getFileInfo(java.util.UUID)
	 */
	public FileInfo getFileInfo(UUID id) {
		String sql = "SELECT id, file_name, file_size, mime_type FROM crm_file WHERE id = ?::uuid";
		RowMapper fileInfoRowMapper = new FileInfoRowMapper();
		FileInfo fileInfo = (FileInfo)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, fileInfoRowMapper);
		return fileInfo;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#insertFile(org.programmerplanet.crm.model.FileInfo, java.io.InputStream)
	 */
	public void insertFile(FileInfo fileInfo, InputStream inputStream) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_file (id, file_name, file_size, mime_type, content) VALUES (?::uuid, ?, ?, ?, ?)";
		Object[] params = new Object[] { id.toString(), fileInfo.getFileName(), new Long(fileInfo.getFileSize()), fileInfo.getMimeType(), new SqlLobValue(inputStream, (int)fileInfo.getFileSize(), lobHandler) };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.BLOB };
		this.getJdbcTemplate().update(sql, params, types);
		fileInfo.setId(id);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#getFile(java.util.UUID, java.io.OutputStream)
	 */
	public void getFile(UUID id, final OutputStream outputStream) {
		String sql = "SELECT content FROM crm_file WHERE id = ?::uuid";
		Object[] params = new Object[] { id.toString() };

		ResultSetExtractor resultSetExtractor = new AbstractLobStreamingResultSetExtractor() {
			protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
				InputStream inputStream = lobHandler.getBlobAsBinaryStream(rs, "content");
				IOUtils.copy(inputStream, outputStream);
			}
		};

		this.getJdbcTemplate().query(sql, params, resultSetExtractor);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#deleteFile(java.util.UUID)
	 */
	public void deleteFile(UUID id) {
		String sql = "DELETE FROM crm_file WHERE id = ?::uuid";
		Object[] params = new Object[] { id.toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#deleteFiles(org.programmerplanet.crm.metadata.ObjectDefinition, org.programmerplanet.crm.metadata.FieldDefinition)
	 */
	public void deleteFiles(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition) {
		String sql = "DELETE FROM crm_file AS f USING " + objectDefinition.getTableName() + " AS o";
		sql += " WHERE f.id = o." + fieldDefinition.getColumnName();
		this.getJdbcTemplate().update(sql);
	}
}

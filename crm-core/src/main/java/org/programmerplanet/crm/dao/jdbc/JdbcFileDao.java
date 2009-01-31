package org.programmerplanet.crm.dao.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.io.IOUtils;
import org.programmerplanet.crm.dao.FileDao;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;
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
	 * @see org.programmerplanet.crm.dao.FileDao#getFileInfo(java.lang.Long)
	 */
	public FileInfo getFileInfo(Long id) {
		String sql = "SELECT id, file_name, file_size, mime_type FROM crm_file WHERE id = ?";
		RowMapper fileInfoRowMapper = new FileInfoRowMapper();
		FileInfo fileInfo = (FileInfo)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, fileInfoRowMapper);
		return fileInfo;
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#insertFile(org.programmerplanet.crm.model.FileInfo, java.io.InputStream)
	 */
	public void insertFile(FileInfo fileInfo, InputStream inputStream) {
		String sql = "INSERT INTO crm_file (file_name, file_size, mime_type, content) VALUES (?, ?, ?, ?)";
		Object[] params = new Object[] { fileInfo.getFileName(), new Long(fileInfo.getFileSize()), fileInfo.getMimeType(), new SqlLobValue(inputStream, (int)fileInfo.getFileSize(), lobHandler) };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.BLOB };
		this.getJdbcTemplate().update(sql, params, types);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_file_id_seq')");
		fileInfo.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#getFile(java.lang.Long, java.io.OutputStream)
	 */
	public void getFile(Long id, final OutputStream outputStream) {
		String sql = "SELECT content FROM crm_file WHERE id = ?";
		Object[] params = new Object[] { id };

		ResultSetExtractor resultSetExtractor = new AbstractLobStreamingResultSetExtractor() {
			protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
				InputStream inputStream = lobHandler.getBlobAsBinaryStream(rs, "content");
				IOUtils.copy(inputStream, outputStream);
			}
		};

		this.getJdbcTemplate().query(sql, params, resultSetExtractor);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#deleteFile(java.lang.Long)
	 */
	public void deleteFile(Long id) {
		String sql = "DELETE FROM crm_file WHERE id = ?";
		Object[] params = new Object[] { id };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.FileDao#deleteFiles(org.programmerplanet.crm.model.ObjectDefinition, org.programmerplanet.crm.model.FieldDefinition)
	 */
	public void deleteFiles(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition) {
		String sql = "DELETE FROM crm_file AS f USING " + objectDefinition.getTableName() + " AS o";
		sql += " WHERE f.id = o." + fieldDefinition.getColumnName();
		this.getJdbcTemplate().update(sql);
	}
}

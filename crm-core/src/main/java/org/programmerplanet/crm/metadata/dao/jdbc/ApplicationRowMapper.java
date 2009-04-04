package org.programmerplanet.crm.metadata.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.programmerplanet.crm.metadata.Application;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ApplicationRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Application application = new Application();
		application.setId(UUID.fromString(rs.getString("id")));
		application.setApplicationName(rs.getString("application_name"));
		application.setViewIndex(rs.getObject("view_index") != null ? new Integer(rs.getInt("view_index")) : null);
		return application;
	}

}
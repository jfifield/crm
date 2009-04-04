package org.programmerplanet.crm.metadata.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.programmerplanet.crm.metadata.ApplicationObject;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ApplicationObjectRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(UUID.fromString(rs.getString("application_id")));
		applicationObject.setObjectId(UUID.fromString(rs.getString("object_id")));
		applicationObject.setViewIndex(new Integer(rs.getInt("view_index")));
		return applicationObject;
	}

}
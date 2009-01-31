package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.ApplicationObject;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ApplicationObjectRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(new Long(rs.getLong("application_id")));
		applicationObject.setObjectId(new Long(rs.getLong("object_id")));
		applicationObject.setViewIndex(new Integer(rs.getInt("view_index")));
		return applicationObject;
	}

}
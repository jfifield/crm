package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.programmerplanet.crm.model.ObjectDefinition;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class ObjectDefinitionRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ObjectDefinition objectDefinition = new ObjectDefinition();
		objectDefinition.setId(UUID.fromString(rs.getString("id")));
		objectDefinition.setObjectName(rs.getString("object_name"));
		objectDefinition.setTableName(rs.getString("table_name"));
		return objectDefinition;
	}

}
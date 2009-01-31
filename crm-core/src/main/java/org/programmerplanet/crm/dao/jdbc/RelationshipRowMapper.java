package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.Relationship;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class RelationshipRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Relationship relationship = new Relationship();
		relationship.setId(new Long(rs.getLong("id")));
		relationship.setParentObjectId(new Long(rs.getLong("parent_object_id")));
		relationship.setChildObjectId(new Long(rs.getLong("child_object_id")));
		relationship.setViewIndex(rs.getObject("view_index") != null ? new Integer(rs.getInt("view_index")) : null);
		relationship.setTableName(rs.getString("table_name"));
		return relationship;
	}

}
package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class FieldDefinitionRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		FieldDefinition fieldDefinition = new FieldDefinition();
		fieldDefinition.setId(new Long(rs.getLong("id")));
		fieldDefinition.setObjectId(new Long(rs.getLong("object_id")));
		fieldDefinition.setFieldName(rs.getString("field_name"));
		fieldDefinition.setColumnName(rs.getString("column_name"));
		fieldDefinition.setDataType(DataType.getEnum(rs.getInt("data_type")));
		fieldDefinition.setDataTypeExt(rs.getObject("data_type_ext") != null ? new Long(rs.getLong("data_type_ext")) : null);
		fieldDefinition.setRequired(rs.getBoolean("required"));
		fieldDefinition.setListIndex(rs.getObject("list_index") != null ? new Integer(rs.getInt("list_index")) : null);
		fieldDefinition.setViewIndex(rs.getObject("view_index") != null ? new Integer(rs.getInt("view_index")) : null);
		return fieldDefinition;
	}

}
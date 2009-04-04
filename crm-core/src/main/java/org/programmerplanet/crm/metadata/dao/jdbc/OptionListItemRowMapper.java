package org.programmerplanet.crm.metadata.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.programmerplanet.crm.metadata.OptionListItem;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class OptionListItemRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setId(UUID.fromString(rs.getString("id")));
		optionListItem.setOptionListId(UUID.fromString(rs.getString("option_list_id")));
		optionListItem.setValue(rs.getString("item_value"));
		optionListItem.setViewIndex(rs.getObject("view_index") != null ? new Integer(rs.getInt("view_index")) : null);
		return optionListItem;
	}

}
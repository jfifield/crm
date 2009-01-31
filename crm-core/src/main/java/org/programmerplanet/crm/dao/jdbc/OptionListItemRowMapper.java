package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.OptionListItem;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class OptionListItemRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionListItem optionListItem = new OptionListItem();
		optionListItem.setId(new Long(rs.getLong("id")));
		optionListItem.setOptionListId(new Long(rs.getLong("option_list_id")));
		optionListItem.setValue(rs.getString("item_value"));
		optionListItem.setViewIndex(rs.getObject("view_index") != null ? new Integer(rs.getInt("view_index")) : null);
		return optionListItem;
	}

}
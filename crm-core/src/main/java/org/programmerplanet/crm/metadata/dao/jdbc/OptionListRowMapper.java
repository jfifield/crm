package org.programmerplanet.crm.metadata.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.programmerplanet.crm.metadata.OptionList;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class OptionListRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionList optionList = new OptionList();
		optionList.setId(UUID.fromString(rs.getString("id")));
		optionList.setName(rs.getString("option_list_name"));
		return optionList;
	}

}
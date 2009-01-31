package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.OptionList;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class OptionListRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		OptionList optionList = new OptionList();
		optionList.setId(new Long(rs.getLong("id")));
		optionList.setName(rs.getString("option_list_name"));
		return optionList;
	}

}
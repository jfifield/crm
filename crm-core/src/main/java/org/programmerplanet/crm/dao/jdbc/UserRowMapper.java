package org.programmerplanet.crm.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.programmerplanet.crm.model.User;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 */
public class UserRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(new Long(rs.getLong("id")));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setEmailAddress(rs.getString("email_address"));
		user.setAdministrator(rs.getBoolean("administrator"));
		return user;
	}

}
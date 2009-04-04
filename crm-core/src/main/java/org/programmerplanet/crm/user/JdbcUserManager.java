package org.programmerplanet.crm.user;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcUserManager extends JdbcDaoSupport implements UserManager {

	/**
	 * @see org.programmerplanet.crm.user.UserManager#getUsers()
	 */
	public List<User> getUsers() {
		String sql = "SELECT * FROM crm_user ORDER BY username";
		RowMapper userRowMapper = new UserRowMapper();
		List users = this.getJdbcTemplate().query(sql, userRowMapper);
		return users;
	}

	/**
	 * @see org.programmerplanet.crm.user.UserManager#getUser(java.util.UUID)
	 */
	public User getUser(UUID id) {
		String sql = "SELECT * FROM crm_user WHERE id = ?::uuid";
		RowMapper userRowMapper = new UserRowMapper();
		User user = (User)this.getJdbcTemplate().queryForObject(sql, new Object[] { id.toString() }, userRowMapper);
		return user;
	}

	/**
	 * @see org.programmerplanet.crm.user.UserManager#getUser(java.lang.String, java.lang.String)
	 */
	public User getUser(String username, String password) {
		String sql = "SELECT * FROM crm_user WHERE username = ? AND password = ?";
		Object[] params = new Object[] { username, password };
		RowMapper userRowMapper = new UserRowMapper();
		List users = this.getJdbcTemplate().query(sql, params, userRowMapper);
		User user = null;
		if (!users.isEmpty()) {
			if (users.size() == 1) {
				user = (User)users.get(0);
			}
			else {
				throw new IncorrectResultSizeDataAccessException(1, users.size());
			}
		}
		return user;
	}

	/**
	 * @see org.programmerplanet.crm.user.UserManager#saveUser(org.programmerplanet.crm.user.User)
	 */
	public void saveUser(User user) {
		if (user.getId() != null) {
			this.updateUser(user);
		}
		else {
			this.insertUser(user);
		}
	}

	private void insertUser(User user) {
		UUID id = UUID.randomUUID();
		String sql = "INSERT INTO crm_user (id, username, password, first_name, last_name, email_address, administrator) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)";
		Object[] params = new Object[] { id.toString(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmailAddress(), new Boolean(user.isAdministrator()) };
		this.getJdbcTemplate().update(sql, params);
		user.setId(id);
	}

	private void updateUser(User user) {
		String sql = "UPDATE crm_user SET username = ?, password = ?, first_name = ?, last_name = ?, email_address = ?, administrator = ? WHERE id = ?::uuid";
		Object[] params = new Object[] { user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmailAddress(), new Boolean(user.isAdministrator()), user.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.user.UserManager#deleteUser(org.programmerplanet.crm.user.User)
	 */
	public void deleteUser(User user) {
		String sql = "DELETE FROM crm_user WHERE id = ?::uuid";
		Object[] params = new Object[] { user.getId().toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.user.UserManager#isUsernameUnique(java.util.UUID, java.lang.String)
	 */
	public boolean isUsernameUnique(UUID id, String username) {
		String sql = null;
		Object[] params = null;
		if (id != null) {
			sql = "SELECT COUNT(*) FROM crm_user WHERE username = ? AND id <> ?::uuid";
			params = new Object[] { username, id.toString() };
		}
		else {
			sql = "SELECT COUNT(*) FROM crm_user WHERE username = ?";
			params = new Object[] { username };
		}
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}

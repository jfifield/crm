package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.OptionListDao;
import org.programmerplanet.crm.model.OptionList;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcOptionListDao extends JdbcDaoSupport implements OptionListDao {

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#getAllOptionLists()
	 */
	public List getAllOptionLists() {
		String sql = "SELECT * FROM crm_option_list ORDER BY option_list_name";
		RowMapper rowMapper = new OptionListRowMapper();
		List optionLists = this.getJdbcTemplate().query(sql, rowMapper);
		return optionLists;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#getOptionList(java.lang.Long)
	 */
	public OptionList getOptionList(Long id) {
		String sql = "SELECT * FROM crm_option_list WHERE id = ?";
		RowMapper rowMapper = new OptionListRowMapper();
		OptionList optionList = (OptionList)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, rowMapper);
		return optionList;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#insertOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void insertOptionList(OptionList optionList) {
		String sql = "INSERT INTO crm_option_list (option_list_name) VALUES (?)";
		Object[] params = new Object[] { optionList.getName() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_option_list_id_seq')");
		optionList.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#updateOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void updateOptionList(OptionList optionList) {
		String sql = "UPDATE crm_option_list SET option_list_name = ? WHERE id = ?";
		Object[] params = new Object[] { optionList.getName(), optionList.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#deleteOptionList(org.programmerplanet.crm.model.OptionList)
	 */
	public void deleteOptionList(OptionList optionList) {
		String sql = "DELETE FROM crm_option_list WHERE id = ?";
		Object[] params = new Object[] { optionList.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListDao#isNameUnique(java.lang.Long, java.lang.String)
	 */
	public boolean isNameUnique(Long id, String name) {
		String sql = "SELECT COUNT(*) FROM crm_option_list WHERE option_list_name = ? AND id <> ?";
		Object[] params = new Object[] { name, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}

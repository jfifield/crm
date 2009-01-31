package org.programmerplanet.crm.dao.jdbc;

import java.util.List;

import org.programmerplanet.crm.dao.OptionListItemDao;
import org.programmerplanet.crm.model.OptionList;
import org.programmerplanet.crm.model.OptionListItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcOptionListItemDao extends JdbcDaoSupport implements OptionListItemDao {

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#getOptionListItems(org.programmerplanet.crm.model.OptionList)
	 */
	public List getOptionListItems(OptionList optionList) {
		String sql = "SELECT * FROM crm_option_list_item WHERE option_list_id = ? ORDER BY view_index, item_value";
		RowMapper rowMapper = new OptionListItemRowMapper();
		List optionListItems = this.getJdbcTemplate().query(sql, new Object[] { optionList.getId() }, rowMapper);
		return optionListItems;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#getOptionListItem(java.lang.Long)
	 */
	public OptionListItem getOptionListItem(Long id) {
		String sql = "SELECT * FROM crm_option_list_item WHERE id = ?";
		RowMapper rowMapper = new OptionListItemRowMapper();
		OptionListItem optionListItem = (OptionListItem)this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, rowMapper);
		return optionListItem;
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#insertOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void insertOptionListItem(OptionListItem optionListItem) {
		String sql = "INSERT INTO crm_option_list_item (option_list_id, item_value, view_index) VALUES (?, ?, ?)";
		Object[] params = new Object[] { optionListItem.getOptionListId(), optionListItem.getValue(), optionListItem.getViewIndex() };
		this.getJdbcTemplate().update(sql, params);
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('crm_option_list_item_id_seq')");
		optionListItem.setId(new Long(id));
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#updateOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void updateOptionListItem(OptionListItem optionListItem) {
		String sql = "UPDATE crm_option_list_item SET item_value = ?, view_index = ? WHERE id = ?";
		Object[] params = new Object[] { optionListItem.getValue(), optionListItem.getViewIndex(), optionListItem.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#deleteOptionListItem(org.programmerplanet.crm.model.OptionListItem)
	 */
	public void deleteOptionListItem(OptionListItem optionListItem) {
		String sql = "DELETE FROM crm_option_list_item WHERE id = ?";
		Object[] params = new Object[] { optionListItem.getId() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.OptionListItemDao#isValueUnique(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	public boolean isValueUnique(Long optionListId, Long id, String value) {
		String sql = "SELECT COUNT(*) FROM crm_option_list_item WHERE option_list_id = ? AND item_value = ? AND id <> ?";
		Object[] params = new Object[] { optionListId, value, id != null ? id : new Long(0) };
		int count = this.getJdbcTemplate().queryForInt(sql, params);
		return count == 0;
	}

}

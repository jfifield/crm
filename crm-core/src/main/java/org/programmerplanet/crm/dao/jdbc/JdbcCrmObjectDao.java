package org.programmerplanet.crm.dao.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.programmerplanet.crm.dao.CrmObjectDao;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcCrmObjectDao extends JdbcDaoSupport implements CrmObjectDao {

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#getCrmObjects(org.programmerplanet.crm.model.ObjectDefinition, java.util.List)
	 */
	public List getCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions) {
		String sql = getBasicSelectSql(objectDefinition, fieldDefinitions);
		List data = this.getJdbcTemplate().queryForList(sql);
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#getRelatedCrmObjects(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public List getRelatedCrmObjects(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ot.id, ");
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			sql.append("ot.");
			sql.append(columnName);
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" FROM ");
		sql.append(relationship.getTableName());
		sql.append(" AS rt ");
		sql.append("INNER JOIN ");
		sql.append(objectDefinition.getTableName());
		sql.append(" AS ot ");
		sql.append("ON (rt.");
		sql.append(objectDefinition.getTableName());
		sql.append("_id = ot.id) ");
		sql.append("WHERE rt.");
		sql.append(parentObjectDefinition.getTableName());
		sql.append("_id = ? ");

		List data = this.getJdbcTemplate().queryForList(sql.toString(), new Object[] { id });
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#getCrmObjectsAvailableForLinking(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public List getCrmObjectsAvailableForLinking(ObjectDefinition objectDefinition, List fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ot.id, ");
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			sql.append("ot.");
			sql.append(columnName);
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" FROM ");
		sql.append(objectDefinition.getTableName());
		sql.append(" AS ot ");
		sql.append("WHERE NOT EXISTS (");
		sql.append("SELECT * FROM ");
		sql.append(relationship.getTableName());
		sql.append(" AS rt ");
		sql.append("WHERE rt.");
		sql.append(objectDefinition.getTableName());
		sql.append("_id = ot.id ");
		sql.append("AND rt.");
		sql.append(parentObjectDefinition.getTableName());
		sql.append("_id = ?)");
		List data = this.getJdbcTemplate().queryForList(sql.toString(), new Object[] { id });
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#getCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.lang.Long)
	 */
	public Map getCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Long id) {
		String sql = getBasicSelectSql(objectDefinition, fieldDefinitions);
		sql += " WHERE id = ?";
		List data = this.getJdbcTemplate().queryForList(sql, new Object[] { id });
		Map map = (Map)data.get(0);
		return map;
	}

	private String getBasicSelectSql(ObjectDefinition objectDefinition, List fieldDefinitions) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, ");
	
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			sql.append(columnName);
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
	
		sql.append(" FROM ");
		sql.append(objectDefinition.getTableName());
		
		return sql.toString();
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#insertCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.util.Map)
	 */
	public Long insertCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data) {
		List parameters = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(objectDefinition.getTableName());
		sql.append(" (");
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			sql.append(columnName);
			sql.append(", ");
			Object value = data.get(columnName);
			// special case - set autonumber field manually
			if (fieldDefinition.getDataType().equals(DataType.AUTO_NUMBER)) {
				value = getNextAutoNumberValue(objectDefinition, fieldDefinition);
			}
			parameters.add(value);
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(") VALUES (");
		for (int i = 0; i < parameters.size(); i++) {
			sql.append("?, ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(")");
		this.getJdbcTemplate().update(sql.toString(), parameters.toArray());
		
		int id = this.getJdbcTemplate().queryForInt("SELECT currval('" + objectDefinition.getTableName() + "_id_seq')");
		return new Long(id);
	}

	private Integer getNextAutoNumberValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition) {
		// TODO: deal with concurrency issue here - it is possible for 2 people to get the same number
		// TODO: it would probably also be better to maintain the value externally to prevent reuse after deletion
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(");
		sql.append(fieldDefinition.getColumnName());
		sql.append(") FROM ");
		sql.append(objectDefinition.getTableName());
		int value = this.getJdbcTemplate().queryForInt(sql.toString());
		return new Integer(value + 1);
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#updateCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.util.Map, java.lang.Long)
	 */
	public void updateCrmObject(ObjectDefinition objectDefinition, List fieldDefinitions, Map data, Long id) {
		List parameters = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(objectDefinition.getTableName());
		sql.append(" SET ");

		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			// special case - skip update of autonumber field
			if (fieldDefinition.getDataType().equals(DataType.AUTO_NUMBER)) {
				continue;
			}
			String columnName = fieldDefinition.getColumnName();
			sql.append(columnName);
			sql.append(" = ?, ");
			Object value = data.get(columnName);
			parameters.add(value);
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" WHERE id = ?");
		parameters.add(id);
		this.getJdbcTemplate().update(sql.toString(), parameters.toArray());
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#deleteCrmObject(org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void deleteCrmObject(ObjectDefinition objectDefinition, Long id) {
		String tableName = objectDefinition.getTableName();
		String sql = "DELETE FROM " + tableName + " WHERE id = ?";
		this.getJdbcTemplate().update(sql, new Object[] { id });
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#insertCrmObjectRelationship(org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void insertCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId) {
		String sql = "INSERT INTO " + relationship.getTableName();
		sql += " (" + parentObjectDefinition.getTableName() + "_id, " + childObjectDefinition.getTableName() + "_id)";
		sql += " VALUES (?, ?)";
		Object[] params = new Object[] { parentId, childId };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#deleteCrmObjectRelationship(org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void deleteCrmObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId, ObjectDefinition childObjectDefinition, Long childId) {
		String sql = "DELETE FROM " + relationship.getTableName();
		sql += " WHERE " + parentObjectDefinition.getTableName() + "_id = ? AND " + childObjectDefinition.getTableName() + "_id = ?";
		Object[] params = new Object[] { parentId, childId };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#deleteCrmObjectRelationships(org.programmerplanet.crm.model.Relationship, org.programmerplanet.crm.model.ObjectDefinition, java.lang.Long)
	 */
	public void deleteCrmObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, Long parentId) {
		String sql = "DELETE FROM " + relationship.getTableName();
		sql += " WHERE " + parentObjectDefinition.getTableName() + "_id = ?";
		Object[] params = new Object[] { parentId };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.dao.CrmObjectDao#clearCrmObjectValue(org.programmerplanet.crm.model.ObjectDefinition, org.programmerplanet.crm.model.FieldDefinition, java.lang.Object)
	 */
	public void clearCrmObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value) {
		String sql = "UPDATE " + objectDefinition.getTableName();
		sql += " SET " + fieldDefinition.getColumnName() + " = NULL";
		sql += " WHERE " + fieldDefinition.getColumnName() + " = ?";
		Object[] params = new Object[] { value };
		this.getJdbcTemplate().update(sql, params);
	}

}

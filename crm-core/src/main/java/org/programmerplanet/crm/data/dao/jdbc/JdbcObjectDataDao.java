package org.programmerplanet.crm.data.dao.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.programmerplanet.crm.data.dao.ObjectDataDao;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcObjectDataDao extends JdbcDaoSupport implements ObjectDataDao {

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#getObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List)
	 */
	public List<Map> getObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions) {
		String sql = getBasicSelectSql(objectDefinition, fieldDefinitions);
		List data = this.getJdbcTemplate().queryForList(sql);
		convertUUIDValues(data, fieldDefinitions);
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#getRelatedObjects(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<Map> getRelatedObjects(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ot.id, ");
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
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
		sql.append("_id = ?::uuid");

		List data = this.getJdbcTemplate().queryForList(sql.toString(), new Object[] { id.toString() });
		convertUUIDValues(data, fieldDefinitions);
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#getObjectsAvailableForLinking(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public List<Map> getObjectsAvailableForLinking(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Relationship relationship, ObjectDefinition parentObjectDefinition, UUID id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ot.id, ");
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
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
		sql.append("_id = ?::uuid)");
		List data = this.getJdbcTemplate().queryForList(sql.toString(), new Object[] { id.toString() });
		convertUUIDValues(data, fieldDefinitions);
		return data;
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#getObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.UUID)
	 */
	public Map getObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, UUID id) {
		String sql = getBasicSelectSql(objectDefinition, fieldDefinitions);
		sql += " WHERE id = ?::uuid";
		List data = this.getJdbcTemplate().queryForList(sql, new Object[] { id.toString() });
		Map map = (Map)data.get(0);
		convertUUIDValues(map, fieldDefinitions);
		return map;
	}

	private String getBasicSelectSql(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, ");

		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			String columnName = fieldDefinition.getColumnName();
			sql.append(columnName);
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
	
		sql.append(" FROM ");
		sql.append(objectDefinition.getTableName());
		
		return sql.toString();
	}

	private void convertUUIDValues(List objects, List<FieldDefinition> fieldDefinitions) {
		for (Iterator i = objects.iterator(); i.hasNext();) {
			Map object = (Map) i.next();
			convertUUIDValues(object, fieldDefinitions);
		}
	}

	private void convertUUIDValues(Map object, List<FieldDefinition> fieldDefinitions) {
		convertUUIDValue(object, "id");
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			if (fieldDefinition.getDataType().equals(DataType.OBJECT)) {
				String columnName = fieldDefinition.getColumnName();
				convertUUIDValue(object, columnName);
			}
		}
	}

	private void convertUUIDValue(Map object, String columnName) {
		Object uuidValue = object.get(columnName);
		if (uuidValue != null) {
			uuidValue = UUID.fromString(uuidValue.toString());
			object.put(columnName, uuidValue);
		}
	}
	
	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#insertObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.Map)
	 */
	public UUID insertObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data) {
		UUID id = UUID.randomUUID();
		List parameters = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(objectDefinition.getTableName());
		sql.append(" (id, ");
		parameters.add(id);
		for (FieldDefinition fieldDefinition : fieldDefinitions) {
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
			Object parameter = parameters.get(i);
			if (parameter instanceof UUID) {
				sql.append("?::uuid, ");
				parameters.set(i, parameter.toString());
			}
			else {
				sql.append("?, ");
			}
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(")");
		this.getJdbcTemplate().update(sql.toString(), parameters.toArray());
		return id;
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
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#updateObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.Map, java.util.UUID)
	 */
	public void updateObject(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitions, Map data, UUID id) {
		List parameters = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(objectDefinition.getTableName());
		sql.append(" SET ");

		for (FieldDefinition fieldDefinition : fieldDefinitions) {
			// special case - skip update of autonumber field
			if (fieldDefinition.getDataType().equals(DataType.AUTO_NUMBER)) {
				continue;
			}
			String columnName = fieldDefinition.getColumnName();
			Object value = data.get(columnName);
			sql.append(columnName);
			if (value instanceof UUID) {
				sql.append(" = ?::uuid, ");
				value = value.toString();
			}
			else {
				sql.append(" = ?, ");
			}
			parameters.add(value);
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" WHERE id = ?::uuid");
		parameters.add(id.toString());
		this.getJdbcTemplate().update(sql.toString(), parameters.toArray());
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#deleteObject(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteObject(ObjectDefinition objectDefinition, UUID id) {
		String tableName = objectDefinition.getTableName();
		String sql = "DELETE FROM " + tableName + " WHERE id = ?::uuid";
		this.getJdbcTemplate().update(sql, new Object[] { id.toString() });
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#insertObjectRelationship(org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void insertObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		String sql = "INSERT INTO " + relationship.getTableName();
		sql += " (" + parentObjectDefinition.getTableName() + "_id, " + childObjectDefinition.getTableName() + "_id)";
		sql += " VALUES (?::uuid, ?::uuid)";
		Object[] params = new Object[] { parentId.toString(), childId.toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#deleteObjectRelationship(org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteObjectRelationship(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId, ObjectDefinition childObjectDefinition, UUID childId) {
		String sql = "DELETE FROM " + relationship.getTableName();
		sql += " WHERE " + parentObjectDefinition.getTableName() + "_id = ?::uuid AND " + childObjectDefinition.getTableName() + "_id = ?::uuid";
		Object[] params = new Object[] { parentId.toString(), childId.toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#deleteObjectRelationships(org.programmerplanet.crm.metadata.Relationship, org.programmerplanet.crm.metadata.ObjectDefinition, java.util.UUID)
	 */
	public void deleteObjectRelationships(Relationship relationship, ObjectDefinition parentObjectDefinition, UUID parentId) {
		String sql = "DELETE FROM " + relationship.getTableName();
		sql += " WHERE " + parentObjectDefinition.getTableName() + "_id = ?::uuid";
		Object[] params = new Object[] { parentId.toString() };
		this.getJdbcTemplate().update(sql, params);
	}

	/**
	 * @see org.programmerplanet.crm.data.dao.ObjectDataDao#clearObjectValue(org.programmerplanet.crm.metadata.ObjectDefinition, org.programmerplanet.crm.metadata.FieldDefinition, java.lang.Object)
	 */
	public void clearObjectValue(ObjectDefinition objectDefinition, FieldDefinition fieldDefinition, Object value) {
		String sql = "UPDATE " + objectDefinition.getTableName();
		sql += " SET " + fieldDefinition.getColumnName() + " = NULL";
		sql += " WHERE " + fieldDefinition.getColumnName() + " = ?";
		Object[] params = new Object[] { value };
		this.getJdbcTemplate().update(sql, params);
	}

}
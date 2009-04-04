package org.programmerplanet.crm.search;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class JdbcSearchService extends AbstractSearchService {

	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
	public static final String DEFAULT_DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm a";

	private static final List NUMERIC_DATA_TYPES = new ArrayList() {
		{
			add(DataType.AUTO_NUMBER);
			add(DataType.MONEY);
			add(DataType.NUMBER);
			add(DataType.PERCENT);
		}
	};

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @see org.programmerplanet.crm.search.SearchService#index()
	 */
	public void index() {
	}

	/**
	 * @see org.programmerplanet.crm.search.AbstractSearchService#search(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.List, org.programmerplanet.crm.search.SearchCriteria)
	 */
	protected List search(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitionsForSearch, List<FieldDefinition> fieldDefinitionsForDisplay, SearchCriteria searchCriteria) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, ");

		for (FieldDefinition fieldDefinition : fieldDefinitionsForDisplay) {
			String columnName = fieldDefinition.getColumnName();
			sql.append(columnName);
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());

		sql.append(" FROM ");
		sql.append(objectDefinition.getTableName());
		sql.append(" WHERE ");

		String query = searchCriteria.getQuery();
		String[] terms = StringUtils.split(query);

		List params = new LinkedList();

		for (FieldDefinition fieldDefinition : fieldDefinitionsForSearch) {
			String columnName = fieldDefinition.getColumnName();
			for (int j = 0; j < terms.length; j++) {
				String term = terms[j];

				if (NUMERIC_DATA_TYPES.contains(fieldDefinition.getDataType())) {
					// only include if value appears numeric
					try {
						Float value = new Float(term);
						sql.append(columnName);
						sql.append(" = ? OR ");
						params.add(value);
					}
					catch (NumberFormatException e) {
						// ignore
					}
				}
				else if (DataType.DATE.equals(fieldDefinition.getDataType())) {
					// only include if value appears to be a date
					try {
						DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
						Date value = dateFormat.parse(term);
						sql.append(columnName);
						sql.append(" = ? OR ");
						params.add(value);
					}
					catch (ParseException e) {
						// ignore
					}
				}
				else if (DataType.DATE_TIME.equals(fieldDefinition.getDataType())) {
					// only include if value appears to be a date/time
					try {
						DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
						Date value = dateFormat.parse(term);
						sql.append(columnName);
						sql.append(" = ? OR ");
						params.add(value);
					}
					catch (ParseException e) {
						// ignore
					}
				}
				else {
					sql.append(columnName);
					sql.append(" ILIKE ? OR ");
					if (!term.startsWith("%")) {
						term = "%" + term;
					}
					if (!term.endsWith("%")) {
						term += "%";
					}
					params.add(term);
				}
			}
		}

		if (!params.isEmpty()) {
			sql.delete(sql.length() - 4, sql.length());
			List results = this.getJdbcTemplate().queryForList(sql.toString(), params.toArray());
			return results;
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

}

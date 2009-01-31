package org.programmerplanet.crm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.programmerplanet.crm.dao.FieldDefinitionDao;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public abstract class AbstractSearchService implements SearchService {

	private static final List EXCLUDED_DATA_TYPES = new ArrayList() {
		{
			add(DataType.BOOLEAN);
			add(DataType.FILE);
			add(DataType.OBJECT);
		}
	};

	private ObjectDefinitionDao objectDefinitionDao;
	private FieldDefinitionDao fieldDefinitionDao;

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public void setFieldDefinitionDao(FieldDefinitionDao fieldDefinitionDao) {
		this.fieldDefinitionDao = fieldDefinitionDao;
	}

	/**
	 * @see org.programmerplanet.crm.service.SearchService#search(org.programmerplanet.crm.service.SearchCriteria)
	 */
	public List search(SearchCriteria searchCriteria) {
		List results = new LinkedList();
		List objectDefinitions = objectDefinitionDao.getAllObjectDefinitions();
		for (Iterator i = objectDefinitions.iterator(); i.hasNext();) {
			ObjectDefinition objectDefinition = (ObjectDefinition)i.next();
			List fieldDefinitionsForSearch = getFieldDefinitionsForSearch(objectDefinition);
			if (fieldDefinitionsForSearch.isEmpty()) {
				continue;
			}

			try {
				List fieldDefinitionsForDisplay = fieldDefinitionDao.getFieldDefinitionsForObjectList(objectDefinition);
				List data = search(objectDefinition, fieldDefinitionsForSearch, fieldDefinitionsForDisplay, searchCriteria);
				Map entry = new HashMap();
				entry.put("objectDefinition", objectDefinition);
				entry.put("fieldDefinitions", fieldDefinitionsForDisplay);
				entry.put("data", data);
				results.add(entry);
			}
			catch (Exception e) {
				throw new SearchException(e);
			}
		}
		return results;
	}

	protected abstract List search(ObjectDefinition objectDefinition, List fieldDefinitionsForSearch, List fieldDefinitionsForDisplay, SearchCriteria searchCriteria) throws Exception;

	protected List getFieldDefinitionsForSearch(ObjectDefinition objectDefinition) {
		List fieldDefinitions = fieldDefinitionDao.getFieldDefinitionsForObject(objectDefinition);
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			if (EXCLUDED_DATA_TYPES.contains(fieldDefinition.getDataType())) {
				i.remove();
			}
		}
		return fieldDefinitions;
	}

}

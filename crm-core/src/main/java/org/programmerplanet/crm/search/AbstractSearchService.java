package org.programmerplanet.crm.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.dao.FieldDefinitionDao;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;

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
	 * @see org.programmerplanet.crm.search.SearchService#search(org.programmerplanet.crm.search.SearchCriteria)
	 */
	public List search(SearchCriteria searchCriteria) {
		List results = new LinkedList();
		List<ObjectDefinition> objectDefinitions = objectDefinitionDao.getObjectDefinitions();
		for (ObjectDefinition objectDefinition : objectDefinitions) {
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

	protected abstract List search(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitionsForSearch, List<FieldDefinition> fieldDefinitionsForDisplay, SearchCriteria searchCriteria) throws Exception;

	protected List<FieldDefinition> getFieldDefinitionsForSearch(ObjectDefinition objectDefinition) {
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

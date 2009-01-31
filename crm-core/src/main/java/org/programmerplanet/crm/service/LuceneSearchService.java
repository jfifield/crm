package org.programmerplanet.crm.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexModifier;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.programmerplanet.crm.dao.CrmObjectDao;
import org.programmerplanet.crm.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.model.CrmObject;
import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class LuceneSearchService extends AbstractSearchService {

	private File indexDirectory;
	private ObjectDefinitionDao objectDefinitionDao;
	private CrmObjectDao crmObjectDao;

	public void setIndexDirectory(File indexDirectory) {
		this.indexDirectory = indexDirectory;
	}

	public void setObjectDefinitionDao(ObjectDefinitionDao objectDefinitionDao) {
		this.objectDefinitionDao = objectDefinitionDao;
	}

	public void setCrmObjectDao(CrmObjectDao crmObjectDao) {
		this.crmObjectDao = crmObjectDao;
	}

	/**
	 * @see org.programmerplanet.crm.service.SearchService#index()
	 */
	public void index() {
		List objectDefinitions = objectDefinitionDao.getAllObjectDefinitions();
		for (Iterator i = objectDefinitions.iterator(); i.hasNext();) {
			ObjectDefinition objectDefinition = (ObjectDefinition)i.next();
			try {
				index(objectDefinition);
			}
			catch (Exception e) {
				throw new SearchException(e);
			}
		}
	}

	private void index(ObjectDefinition objectDefinition) throws Exception {
		List fieldDefinitions = getFieldDefinitionsForSearch(objectDefinition);

		if (fieldDefinitions.isEmpty()) {
			return;
		}

		List crmObjects = crmObjectDao.getCrmObjects(objectDefinition, fieldDefinitions);
		Analyzer analyzer = new StandardAnalyzer();
		IndexModifier indexModifier = new IndexModifier(getIndexDirectoryForObject(objectDefinition), analyzer, true);
		try {
			for (Iterator i = crmObjects.iterator(); i.hasNext();) {
				Map data = (Map)i.next();

				CrmObject crmObject = new CrmObject();
				crmObject.setObjectDefinition(objectDefinition);
				crmObject.setFieldDefinitions(fieldDefinitions);
				crmObject.setData(data);
				crmObject.setId(new Long(((Number)data.get("id")).longValue()));

				Document document = createDocument(crmObject);
				indexModifier.addDocument(document);
			}
		}
		finally {
			indexModifier.close();
		}
	}

	private Document createDocument(CrmObject crmObject) {
		Document document = new Document();
		for (Iterator i = crmObject.getFieldDefinitions().iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			Object value = crmObject.getData().get(fieldDefinition.getColumnName());
			if (value != null) {
				String fieldName = fieldDefinition.getColumnName();
				String fieldValue = null;
				if (DataType.DATE.equals(fieldDefinition.getDataType())) {
					fieldValue = DateTools.dateToString((Date)value, DateTools.Resolution.DAY);
				}
				else if (DataType.DATE_TIME.equals(fieldDefinition.getDataType())) {
					fieldValue = DateTools.dateToString((Date)value, DateTools.Resolution.MINUTE);
				}
				else {
					fieldValue = value.toString();
				}
				Field field = new Field(fieldName, fieldValue, Field.Store.NO, Field.Index.TOKENIZED);
				document.add(field);
			}
		}
		Long id = crmObject.getId();
		Field field = new Field("id", id.toString(), Field.Store.YES, Field.Index.NO);
		document.add(field);
		return document;
	}

	/**
	 * @see org.programmerplanet.crm.service.AbstractSearchService#search(org.programmerplanet.crm.model.ObjectDefinition, java.util.List, java.util.List, org.programmerplanet.crm.service.SearchCriteria)
	 */
	protected List search(ObjectDefinition objectDefinition, List fieldDefinitionsForSearch, List fieldDefinitionsForDisplay, SearchCriteria searchCriteria) throws Exception {
		List results = new LinkedList();
		Analyzer analyzer = new StandardAnalyzer();
		Searcher searcher = new IndexSearcher(getIndexDirectoryForObject(objectDefinition));
		try {
			List fields = new ArrayList();
			for (Iterator i = fieldDefinitionsForSearch.iterator(); i.hasNext();) {
				FieldDefinition fieldDefinition = (FieldDefinition)i.next();
				fields.add(fieldDefinition.getColumnName());
			}
			QueryParser queryParser = new MultiFieldQueryParser((String[])fields.toArray(new String[0]), analyzer);
			Query query = queryParser.parse(searchCriteria.getQuery());
			Hits hits = searcher.search(query);

			for (Iterator h = hits.iterator(); h.hasNext();) {
				Hit hit = (Hit)h.next();
				String idValue = hit.get("id");
				Long id = new Long(idValue);
				Map crmObject = crmObjectDao.getCrmObject(objectDefinition, fieldDefinitionsForDisplay, id);
				results.add(crmObject);
			}
		}
		finally {
			searcher.close();
		}
		return results;
	}

	private String getIndexDirectoryForObject(ObjectDefinition objectDefinition) {
		return indexDirectory.getAbsolutePath() + "/" + objectDefinition.getTableName();
	}

}

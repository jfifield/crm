package org.programmerplanet.crm.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.dao.ObjectDefinitionDao;
import org.programmerplanet.crm.model.CrmObject;

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
	 * @see org.programmerplanet.crm.search.SearchService#index()
	 */
	public void index() {
		List<ObjectDefinition> objectDefinitions = objectDefinitionDao.getObjectDefinitions();
		for (ObjectDefinition objectDefinition : objectDefinitions) {
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

		List<Map> crmObjects = crmObjectDao.getCrmObjects(objectDefinition, fieldDefinitions);
		Analyzer analyzer = new StandardAnalyzer();
		IndexModifier indexModifier = new IndexModifier(getIndexDirectoryForObject(objectDefinition), analyzer, true);
		try {
			for (Map data : crmObjects) {
				CrmObject crmObject = new CrmObject();
				crmObject.setObjectDefinition(objectDefinition);
				crmObject.setFieldDefinitions(fieldDefinitions);
				crmObject.setData(data);
				crmObject.setId((UUID)data.get("id"));

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
		for (FieldDefinition fieldDefinition : crmObject.getFieldDefinitions()) {
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
		UUID id = crmObject.getId();
		Field field = new Field("id", id.toString(), Field.Store.YES, Field.Index.NO);
		document.add(field);
		return document;
	}

	/**
	 * @see org.programmerplanet.crm.search.AbstractSearchService#search(org.programmerplanet.crm.metadata.ObjectDefinition, java.util.List, java.util.List, org.programmerplanet.crm.search.SearchCriteria)
	 */
	protected List search(ObjectDefinition objectDefinition, List<FieldDefinition> fieldDefinitionsForSearch, List<FieldDefinition> fieldDefinitionsForDisplay, SearchCriteria searchCriteria) throws Exception {
		List results = new LinkedList();
		Analyzer analyzer = new StandardAnalyzer();
		Searcher searcher = new IndexSearcher(getIndexDirectoryForObject(objectDefinition));
		try {
			List fields = new ArrayList();
			for (FieldDefinition fieldDefinition : fieldDefinitionsForSearch) {
				fields.add(fieldDefinition.getColumnName());
			}
			QueryParser queryParser = new MultiFieldQueryParser((String[])fields.toArray(new String[0]), analyzer);
			Query query = queryParser.parse(searchCriteria.getQuery());
			Hits hits = searcher.search(query);

			for (Iterator h = hits.iterator(); h.hasNext();) {
				Hit hit = (Hit)h.next();
				String idValue = hit.get("id");
				UUID id = UUID.fromString(idValue);
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

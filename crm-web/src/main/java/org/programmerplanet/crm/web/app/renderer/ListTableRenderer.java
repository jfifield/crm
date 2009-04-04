package org.programmerplanet.crm.web.app.renderer;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.metadata.DataType;
import org.programmerplanet.crm.metadata.FieldDefinition;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.web.util.MapEntryComparator;
import org.programmerplanet.crm.web.util.URI;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ListTableRenderer {

	private static final String FIRST_LINK_ENABLED = "<a href=\"{0}\"><img src=\"../theme/default/resultset_first.png\"/></a>";
	private static final String FIRST_LINK_DISABLED = "<img src=\"../theme/default/resultset_first_disabled.png\"/>";
	private static final String PREVIOUS_LINK_ENABLED = "<a href=\"{0}\"><img src=\"../theme/default/resultset_previous.png\"/></a>";
	private static final String PREVIOUS_LINK_DISABLED = "<img src=\"../theme/default/resultset_previous_disabled.png\"/>";
	private static final String NEXT_LINK_ENABLED = "<a href=\"{0}\"><img src=\"../theme/default/resultset_next.png\"/></a>";
	private static final String NEXT_LINK_DISABLED = "<img src=\"../theme/default/resultset_next_disabled.png\"/>";
	private static final String LAST_LINK_ENABLED = "<a href=\"{0}\"><img src=\"../theme/default/resultset_last.png\"/></a>";
	private static final String LAST_LINK_DISABLED = "<img src=\"../theme/default/resultset_last_disabled.png\"/>";
	private static final String PAGE_COUNTER = "({0} - {1} of {2})";

	private Map fieldRenderers;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ObjectDefinition objectDefinition;
	private List fieldDefinitions;
	private List data;
	private int pageSize;
	private String source;
	private String sourceObject;
	private UUID sourceObjectId;

	public void setFieldRenderers(Map fieldRenderers) {
		this.fieldRenderers = fieldRenderers;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ObjectDefinition getObjectDefinition() {
		return objectDefinition;
	}

	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		this.objectDefinition = objectDefinition;
	}

	public List getFieldDefinitions() {
		return fieldDefinitions;
	}

	public void setFieldDefinitions(List fieldDefinitions) {
		this.fieldDefinitions = fieldDefinitions;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(String sourceObject) {
		this.sourceObject = sourceObject;
	}

	public UUID getSourceObjectId() {
		return sourceObjectId;
	}

	public void setSourceObjectId(UUID sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
	}

	public void render(Writer writer) throws IOException {
		sort();
		PagedList pagedList = createPagedList();
		renderTable(writer, pagedList);
	}

	private void sort() {
		int sort = this.getSort();
		FieldDefinition fieldDefinition = (FieldDefinition)fieldDefinitions.get(sort);
		Comparator comparator = new MapEntryComparator(fieldDefinition.getColumnName());

		boolean ascending = this.getAscending();
		if (!ascending) {
			comparator = new ReverseComparator(comparator);
		}

		Collections.sort(data, comparator);
	}

	private PagedList createPagedList() {
		PagedList pagedList = new PagedList();
		pagedList.setList(this.data);
		pagedList.setPageSize(this.pageSize);
		int page = getPage();
		pagedList.setPage(page);
		return pagedList;
	}

	private void renderTable(Writer writer, PagedList pagedList) throws IOException {
		writer.write("<table class=\"list\" width=\"80%\">\n");
		renderTableCaption(writer);
		renderTableHeader(writer, pagedList);
		renderTableBody(writer, pagedList);
		writer.write("</table>\n");
	}

	private void renderTableCaption(Writer writer) throws IOException {
		writer.write("<caption>" + objectDefinition.getPluralObjectName() + "</caption>\n");
	}

	private void renderTableHeader(Writer writer, PagedList pagedList) throws IOException {
		writer.write("<thead>\n");
		renderPager(writer, pagedList);
		renderHeader(writer);
		writer.write("</thead>\n");
	}

	private void renderPager(Writer writer, PagedList pagedList) throws IOException {
		writer.write("<tr>\n");
		int columnCount = fieldDefinitions.size() + 1;
		if ("view".equals(source)) {
			columnCount++;
		}
		writer.write("<td colspan=\"" + columnCount + "\" class=\"pager\">\n");

		URI uri = getRequestUri();

		String pageParameter = getPageParameterName();

		// first (enabled if not on first page)
		if (!pagedList.isFirst()) {
			uri.addParameter(pageParameter, "1");
			String link = MessageFormat.format(FIRST_LINK_ENABLED, new Object[] { uri });
			writer.write(link);
		}
		else {
			writer.write(FIRST_LINK_DISABLED);
		}

		writer.write("&nbsp;");

		// previous (enabled if not on first page)
		if (!pagedList.isFirst()) {
			uri.addParameter(pageParameter, Integer.toString(pagedList.getPage() - 1));
			String link = MessageFormat.format(PREVIOUS_LINK_ENABLED, new Object[] { uri });
			writer.write(link);
		}
		else {
			writer.write(PREVIOUS_LINK_DISABLED);
		}

		writer.write("&nbsp;");

		Object[] pageCounterArgs = new Object[] { new Integer(pagedList.getFirstItemNumber()), new Integer(pagedList.getLastItemNumber()), new Integer(pagedList.getTotalItemCount()) };
		String pageCounter = MessageFormat.format(PAGE_COUNTER, pageCounterArgs);
		writer.write(pageCounter);

		writer.write("&nbsp;");

		// next (enabled if not on last page)
		if (!pagedList.isLast()) {
			uri.addParameter(pageParameter, Integer.toString(pagedList.getPage() + 1));
			String link = MessageFormat.format(NEXT_LINK_ENABLED, new Object[] { uri });
			writer.write(link);
		}
		else {
			writer.write(NEXT_LINK_DISABLED);
		}

		writer.write("&nbsp;");

		// last (enabled if not on last page)
		if (!pagedList.isLast()) {
			uri.addParameter(pageParameter, Integer.toString(pagedList.getTotalPages()));
			String link = MessageFormat.format(LAST_LINK_ENABLED, new Object[] { uri });
			writer.write(link);
		}
		else {
			writer.write(LAST_LINK_DISABLED);
		}

		writer.write("</td>\n");
		writer.write("</tr>\n");
	}

	private void renderHeader(Writer writer) throws IOException {
		writer.write("<tr>\n");

		String sortParameter = getSortParameterName();
		String dirParameter = getDirectionParameterName();

		int sort = getSort();
		boolean ascending = getAscending();
		URI uri = getRequestUri();

		int index = 0;
		for (Iterator i = fieldDefinitions.iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();

			uri.addParameter(sortParameter, Integer.toString(index));

			String dir = "a";
			if (index == sort && ascending) {
				dir = "d";
			}
			uri.addParameter(dirParameter, dir);

			String headerClass = "dt_" + fieldDefinition.getDataType().getName();
			headerClass += " sortable";
			if (index == sort) {
				headerClass += ((ascending) ? " order1" : " order2");
			}

			writer.write("<th class=\"" + headerClass + "\">");
			writer.write("<a href=\"" + uri + "\">" + fieldDefinition.getFieldName() + "</a>");
			writer.write("</th>\n");

			index++;
		}

		if ("view".equals(source)) {
			writer.write("<th width=\"1%\"><a href=\"" + objectDefinition.getObjectName() + ".link?source=view&source_object=" + sourceObject + "&source_object_id=" + sourceObjectId + "\"><img src=\"../theme/default/link_add.png\"/></a></th>\n");
			writer.write("<th width=\"1%\"><a href=\"" + objectDefinition.getObjectName() + ".edit?source=view&source_object=" + sourceObject + "&source_object_id=" + sourceObjectId + "\"><img src=\"../theme/default/add.png\"/></a></th>\n");
		}
		else {
			writer.write("<th width=\"1%\"><a href=\"" + objectDefinition.getObjectName() + ".edit?source=list\"><img src=\"../theme/default/add.png\"/></a></th>\n");
		}

		writer.write("</tr>\n");
	}

	private void renderTableBody(Writer writer, PagedList pagedList) throws IOException {
		List list = pagedList.getPageList();

		if (!list.isEmpty()) {
			writer.write("<tbody>\n");
		}

		for (Iterator i = list.iterator(); i.hasNext();) {
			Map row = (Map)i.next();
			writer.write("<tr>\n");

			for (Iterator fi = fieldDefinitions.iterator(); fi.hasNext();) {
				FieldDefinition fieldDefinition = (FieldDefinition)fi.next();
				writer.write("<td class=\"dt_" + fieldDefinition.getDataType().getName() + "\">");

				Object value = row.get(fieldDefinition.getColumnName());
				DataType dataType = fieldDefinition.getDataType();
				FieldRenderer fieldRenderer = (FieldRenderer)fieldRenderers.get(dataType);
				if (fieldRenderer == null) {
					throw new IOException("Unknown field metadata data type: " + dataType);
				}
				fieldRenderer.renderListField(writer, value, fieldDefinition);

				writer.write("</td>\n");
			}

			if ("view".equals(source)) {
				writer.write("<td>\n");
				writer.write("<a href=\"javascript:void(0);\" onclick=\"post('" + objectDefinition.getObjectName() + ".link', {id:'" + row.get("id") + "',source_object:'" + sourceObject + "',source_object_id:'" + sourceObjectId + "',__delete:true});\"><img src=\"../theme/default/link_delete.png\"/></a>\n");
				writer.write("</td>\n");
				writer.write("<td>\n");
				writer.write("<a href=\"" + objectDefinition.getObjectName() + ".view?id=" + row.get("id") + "\"><img src=\"../theme/default/view.png\"/></a>\n");
				writer.write("<a href=\"" + objectDefinition.getObjectName() + ".edit?id=" + row.get("id") + "&source=view&source_object=" + sourceObject + "&source_object_id='" + sourceObjectId + "'\"><img src=\"../theme/default/edit.png\"/></a>\n");
				writer.write("<a href=\"javascript:void(0);\" onclick=\"deleteObject('" + objectDefinition.getObjectName() + "', '" + row.get("id") + "', 'view', '" + sourceObject + "', '" + sourceObjectId + "');\"><img src=\"../theme/default/delete.png\"/></a>\n");
				writer.write("</td>\n");
			}
			else {
				writer.write("<td>\n");
				writer.write("<a href=\"" + objectDefinition.getObjectName() + ".view?id=" + row.get("id") + "&source=list\"><img src=\"../theme/default/view.png\"/></a>\n");
				writer.write("<a href=\"" + objectDefinition.getObjectName() + ".edit?id=" + row.get("id") + "&source=list\"><img src=\"../theme/default/edit.png\"/></a>\n");
				writer.write("<a href=\"javascript:void(0);\" onclick=\"deleteObject('" + objectDefinition.getObjectName() + "', '" + row.get("id") + "');\"><img src=\"../theme/default/delete.png\"/></a>\n");
				writer.write("</td>\n");
			}

			writer.write("</tr>\n");
		}

		if (!list.isEmpty()) {
			writer.write("</tbody>\n");
		}
	}

	private URI getRequestUri() {
		String requestUri = (String)request.getAttribute("javax.servlet.forward.request_uri");
		requestUri = response.encodeURL(requestUri);
		URI uri = new URI(requestUri);

		Map requestParameters = request.getParameterMap();
		uri.addParameters(requestParameters);
		return uri;
	}

	private String getTableId() {
		return "o-" + objectDefinition.getId();
	}

	private String getPageParameterName() {
		return this.getTableId() + "-p";
	}

	private String getSortParameterName() {
		return this.getTableId() + "-s";
	}

	private String getDirectionParameterName() {
		return this.getTableId() + "-d";
	}

	private int getPage() {
		String pageParameter = getPageParameterName();
		String pageValue = request.getParameter(pageParameter);
		if (StringUtils.isEmpty(pageValue)) {
			pageValue = "1";
		}
		return Integer.parseInt(pageValue);
	}

	private int getSort() {
		String sortParameter = getSortParameterName();
		String sortValue = request.getParameter(sortParameter);
		if (StringUtils.isEmpty(sortValue)) {
			sortValue = "0";
		}
		return Integer.parseInt(sortValue);
	}

	private boolean getAscending() {
		String dirParameter = getDirectionParameterName();
		String dirValue = request.getParameter(dirParameter);
		if (StringUtils.isEmpty(dirValue)) {
			dirValue = "a";
		}
		return dirValue.equals("a");
	}

}

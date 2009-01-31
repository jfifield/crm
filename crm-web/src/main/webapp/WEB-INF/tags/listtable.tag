<%@tag import="java.util.Map"%>
<%@tag import="org.springframework.context.ApplicationContext"%>
<%@tag import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@tag import="org.programmerplanet.crm.web.app.renderer.ListTableRenderer"%>

<%@attribute name="objectDefinition" type="org.programmerplanet.crm.model.ObjectDefinition" required="true"%>
<%@attribute name="fieldDefinitions" type="java.util.List" required="true"%>
<%@attribute name="data" type="java.util.List" required="true"%>
<%@attribute name="pageSize" type="java.lang.Integer" required="true"%>
<%@attribute name="source" type="java.lang.String" required="true"%>
<%@attribute name="sourceObject" type="java.lang.String" required="false"%>
<%@attribute name="sourceObjectId" type="java.lang.Long" required="false"%>

<%
	ApplicationContext applicationContext = RequestContextUtils.getWebApplicationContext(request);
	Map fieldRenderers = (Map)applicationContext.getBean("fieldRenderers");
	
	ListTableRenderer renderer = new ListTableRenderer();
	renderer.setFieldRenderers(fieldRenderers);
	renderer.setRequest(request);
	renderer.setResponse(response);
	renderer.setObjectDefinition(objectDefinition);
	renderer.setFieldDefinitions(fieldDefinitions);
	renderer.setData(data);
	renderer.setPageSize(pageSize.intValue());
	renderer.setSource(source);
	renderer.setSourceObject(sourceObject);
	renderer.setSourceObjectId(sourceObjectId);
	
	renderer.render(out);
%>
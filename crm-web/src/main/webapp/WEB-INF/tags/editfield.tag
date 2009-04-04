<%@tag import="java.util.Map"%>
<%@tag import="org.programmerplanet.crm.metadata.DataType"%>
<%@tag import="org.springframework.context.ApplicationContext"%>
<%@tag import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@tag import="org.programmerplanet.crm.web.app.renderer.FieldRenderer"%>

<%@attribute name="value" type="java.lang.Object" required="true"%>
<%@attribute name="fieldDefinition" type="org.programmerplanet.crm.metadata.FieldDefinition" required="true"%>

<%
	ApplicationContext applicationContext = RequestContextUtils.getWebApplicationContext(request);
	Map fieldRenderers = (Map)applicationContext.getBean("fieldRenderers");
	DataType dataType = fieldDefinition.getDataType();
	FieldRenderer fieldRenderer = (FieldRenderer)fieldRenderers.get(dataType);
	if (fieldRenderer == null) {
		throw new JspException("Unknown field metadata data type: " + dataType);
	}
	fieldRenderer.renderEditField(out, value, fieldDefinition);
%>
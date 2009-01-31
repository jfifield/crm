<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title><spring:message code="application.title"/></title>
	<link rel="stylesheet" type="text/css" href="../theme/default/style.css"/>
	<script type="text/javascript" language="JavaScript" src="../script/prototype-1.5.1.js"></script>
	<script type="text/javascript" language="JavaScript" src="../script/scriptaculous-1.7.1_beta3.js"></script>
	<script type="text/javascript" language="JavaScript" src="../components/window/window.js"></script>
	<link rel="stylesheet" type="text/css" href="../components/window/default.css"/>
	<link rel="stylesheet" type="text/css" href="../components/window/alphacube.css"/>
	<script type="text/javascript" language="JavaScript" src="../components/mdlg/mdlg.js"></script>
	<link rel="stylesheet" type="text/css" href="../components/mdlg/mdlg.css"/>
	<script type="text/javascript" language="JavaScript" src="../components/jscalendar/calendar.js"></script>
	<script type="text/javascript" language="JavaScript" src="../components/jscalendar/calendar-en.js"></script>
	<script type="text/javascript" language="JavaScript" src="../components/jscalendar/calendar-setup.js"></script>
	<link rel="stylesheet" type="text/css" href="../components/jscalendar/calendar-system.css"/>
	<script type="text/javascript" language="JavaScript" src="../script/common.js"></script>
</head>
<body>

<table class="tab" width="100%">
<tr>
	<c:set var="style" value=""/>
	<c:if test="${objectDefinition == null}">
		<c:set var="style" value="selected"/>
	</c:if>
	<td class="${style}">
		<a href="home">Home</a>
	</td>
	<c:forEach var="object" items="${crm_objects}">
		<c:set var="style" value=""/>
		<c:if test="${objectDefinition.id == object.id}">
			<c:set var="style" value="selected"/>
		</c:if>
		<td class="${style}">
			<a href="${object.objectName}.list">${object.pluralObjectName}</a>
		</td>
	</c:forEach>
	<td class="spacer" width="97%" align="right">
		<a href="search">Search</a>
		&nbsp;|&nbsp;
		<c:if test="${userSession.user.administrator}">
			<a href="../admin/home" target="crm_admin">Administration</a>
			&nbsp;|&nbsp;
		</c:if>
		<a href="logout">Logout</a>
		&nbsp;
		<form name="applicationForm" method="post" style="display:inline;">
			<select name="application" onchange="document.forms['applicationForm'].submit();">
				<c:forEach var="application" items="${crm_applications}">
					<c:set var="selected" value=""/>
					<c:if test="${userSession.selectedApplicationId == application.id}">
						<c:set var="selected" value=" selected=selected"/>
					</c:if>
					<option value="${application.id}"${selected}>${application.applicationName}</option>
				</c:forEach>
			</select>
		</form>
	</td>
</tr>
</table>

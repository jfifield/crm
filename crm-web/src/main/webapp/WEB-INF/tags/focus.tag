<%@attribute name="form" required="false"%>
<%@attribute name="element" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty form}">
	<c:set var="form" value="0"/>
</c:if>
<script type="text/javascript" language="JavaScript">
	<!--
	var focusControl = document.forms["${form}"].elements["${element}"];
	if (focusControl.type != "hidden") {
		focusControl.focus();
	}
	// -->
</script>

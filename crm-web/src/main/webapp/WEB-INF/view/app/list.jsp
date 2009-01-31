<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<div align="center">

	<h3>${objectDefinition.objectName} List</h3>

	<t:listtable objectDefinition="${objectDefinition}" fieldDefinitions="${fieldDefinitions}" data="${data}" pageSize="25" source="list"/>

</div>

<jsp:include page="include/footer.jsp"/>

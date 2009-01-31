<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteObject(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Object'), 'objectEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
Objects
</p>

<div align="center">

<p>
<table class="list" width="80%">
	<caption>Objects</caption>
	<tr>
		<th>Object Name</th>
		<th width="1%"><a href="objectEdit"><img src="../theme/default/add.png"/></a></th>
	</tr>
	<c:forEach var="objectDefinition" items="${objects}">
		<tr>
			<td>${objectDefinition.objectName}</td>
			<td>
				<a href="objectEdit?id=${objectDefinition.id}"><img src="../theme/default/edit.png"/></a>
				<a href="javascript:void(0);" onclick="_deleteObject(${objectDefinition.id});"><img src="../theme/default/delete.png"/></a>
			</td>
		</tr>
	</c:forEach>
</table>
</p>

</div>

<jsp:include page="include/footer.jsp"/>

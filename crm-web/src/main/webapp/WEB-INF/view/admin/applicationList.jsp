<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteApplication(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Application'), 'applicationEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
Applications
</p>

<div align="center">

<p>
<table class="list" width="80%">
	<caption>Applications</caption>
	<tr>
		<th>Application Name</th>
		<th width="1%"></th>
		<th width="1%"><a href="applicationEdit"><img src="../theme/default/add.png"/></a></th>
	</tr>
	<c:forEach var="application" items="${applications}" varStatus="status">
		<tr>
			<td>${application.applicationName}</td>
			<td>
				<c:choose>
					<c:when test="${!status.first}">
						<a href="javascript:void(0);" onclick="post('applicationEdit', {id:'${application.id}',__move:'up'});"><img src="../theme/default/arrow_up.png"/></a>
					</c:when>
					<c:otherwise>
						<img src="../theme/default/blank.gif"/>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${!status.last}">
						<a href="javascript:void(0);" onclick="post('applicationEdit', {id:'${application.id}',__move:'down'});"><img src="../theme/default/arrow_down.png"/></a>
					</c:when>
					<c:otherwise>
						<img src="../theme/default/blank.gif"/>
					</c:otherwise>
				</c:choose>
			</td>
			<td>
				<a href="applicationEdit?id=${application.id}"><img src="../theme/default/edit.png"/></a>
				<a href="javascript:void(0);" onclick="_deleteApplication('${application.id}');"><img src="../theme/default/delete.png"/></a>
			</td>
		</tr>
	</c:forEach>
</table>
</p>

</div>

<jsp:include page="include/footer.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<div align="center">

	<h3>Link ${childObjectDefinition.objectName} to ${parentObjectDefinition.objectName}</h3>

	<table width="80%">
	<tr>
		<td align="right">
			<a href="${parentObjectDefinition.objectName}.list"><img src="../theme/default/list.png"/></a>
			<a href="${parentObjectDefinition.objectName}.view?id=${id}"><img src="../theme/default/view.png"/></a>
		</td>
	</tr>
	</table>

	<table class="list" width="80%">
	<tr>
		<c:forEach var="fieldDefinition" items="${fieldDefinitions}">
			<th>${fieldDefinition.fieldName}</th>
		</c:forEach>
		<th width="1%"></th>
	</tr>
	<c:forEach var="row" items="${data}">
		<tr>
			<c:forEach var="fieldDefinition" items="${fieldDefinitions}">
				<td>
					<t:listfield value="${row[fieldDefinition.columnName]}" fieldDefinition="${fieldDefinition}"/>
				</td>
			</c:forEach>
			<td>
				<a href="javascript:void(0);" onclick="post('${childObjectDefinition.objectName}.link', {id:${row['id']},source_object:'${parentObjectDefinition.objectName}',source_object_id:${id},__save:true});"><img src="../theme/default/add.png"/></a>
			</td>
		</tr>
	</c:forEach>
	</table>
	
	
</div>

<jsp:include page="include/footer.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<div align="center">

	<h3>${objectDefinition.objectName} View</h3>

	<table width="80%">
	<tr>
		<td align="right">
			<a href="${objectDefinition.objectName}.list"><img src="../theme/default/list.png"/></a>
			<a href="${objectDefinition.objectName}.edit?id=${data['id']}&source=view"><img src="../theme/default/edit.png"/></a>
			<a href="javascript:void(0);" onclick="deleteObject('${objectDefinition.objectName}', '${data['id']}');"><img src="../theme/default/delete.png"/></a>
		</td>
	</tr>
	</table>
	<table class="detail" width="80%">
	
		<c:forEach var="fieldDefinition" items="${fieldDefinitions}" varStatus="status">
			<c:if test="${status.index % 2 == 0}">
				<tr>
				<c:set var="closed" value="false"/>
			</c:if>
			<td class="label">${fieldDefinition.fieldName}:</td>
			<td class="dt_${fieldDefinition.dataType.name}">
				<t:viewfield value="${data[fieldDefinition.columnName]}" fieldDefinition="${fieldDefinition}"/>
			</td>
			<c:if test="${status.index % 2 != 0}">
				</tr>
				<c:set var="closed" value="true"/>
			</c:if>
		</c:forEach>
		<c:if test="${!closed}">
			</tr>
		</c:if>
	</table>

	<c:forEach var="relationship" items="${relationships}">
		<p>
			<t:listtable objectDefinition="${relationship.objectDefinition}" fieldDefinitions="${relationship.fieldDefinitions}" data="${relationship.data}" pageSize="10" source="view" sourceObject="${objectDefinition.objectName}" sourceObjectId="${data['id']}"/>
		</p>
	</c:forEach>

</div>

<jsp:include page="include/footer.jsp"/>

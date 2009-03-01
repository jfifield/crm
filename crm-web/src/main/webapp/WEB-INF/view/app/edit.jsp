<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<div align="center">

	<h3>${objectDefinition.objectName} Edit</h3>

	<spring:hasBindErrors name="object">
		<c:if test="${errors.globalErrorCount > 0}">
			<c:forEach var="error" items="${errors.globalErrors}">
				<span class="error">* <spring:message code="${error.code}" text="${error.defaultMessage}"/></span><br/>
			</c:forEach>
		</c:if>
	</spring:hasBindErrors>                         

	<form name="editForm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${data['id']}"/>
		<table width="80%">
		<tr>
			<td align="right">
				<a href="${objectDefinition.objectName}.list"><img src="../theme/default/list.png"/></a>
				<c:if test="${!empty data['id']}">
				<a href="${objectDefinition.objectName}.view?id=${data['id']}"><img src="../theme/default/view.png"/></a>
				</c:if>
			</td>
		</tr>
		</table>
		<table class="detail" width="80%">
			<c:forEach var="fieldDefinition" items="${fieldDefinitions}" varStatus="status">
				<c:if test="${status.first}">
					<c:set var="focusElement" value="${fieldDefinition.columnName}"/>
				</c:if>
				<c:if test="${status.index % 2 == 0}">
					<tr>
					<c:set var="closed" value="false"/>
				</c:if>
				<c:set var="labelClass" value="label"/>
				<c:if test="${fieldDefinition.required}">
					<c:set var="labelClass" value="${labelClass} required"/>
				</c:if>
				<td class="${labelClass}">${fieldDefinition.fieldName}:</td>
				<td>
					<spring:bind path="object.data[${fieldDefinition.columnName}]">
						<t:editfield value="${status.value}" fieldDefinition="${fieldDefinition}"/>
						<c:if test="${status.error}">
							<c:forEach var="errorMessage" items="${status.errorMessages}">
								<span class="error">* ${errorMessage}</span><br/>
							</c:forEach>
						</c:if>
					</spring:bind>
				</td>
				<c:if test="${status.index % 2 != 0}">
					</tr>
					<c:set var="closed" value="true"/>
				</c:if>
			</c:forEach>
			<c:if test="${!closed}">
				</tr>
			</c:if>
			<tr>
				<td colspan="4" style="text-align: center;">
					<input type="submit" class="button" value="Save" name="__save"/>
					<c:if test="${!empty data['id']}">
					<input type="submit" class="button" value="Delete" name="__delete" onclick="deleteObject('${objectDefinition.objectName}', '${data['id']}', '${param.source}', '${param.source_object}', '${param.source_object_id}'); return false;"/>
					</c:if>
					<input type="submit" class="button" value="Cancel" name="__cancel"/>
				</td>
			</tr>
		</table>
	
	</form>

</div>

<t:focus form="editForm" element="${focusElement}"/>

<jsp:include page="include/footer.jsp"/>

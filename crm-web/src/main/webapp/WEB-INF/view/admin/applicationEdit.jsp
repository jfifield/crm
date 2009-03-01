<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteApplication(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Application'), 'applicationEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="applicationList">Applications</a>
&raquo;
<c:choose>
	<c:when test="${application.id != null}">Edit Application: ${application.applicationName}</c:when>
	<c:otherwise>Add Application</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">
	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${application.id != null}">Edit Application: ${application.applicationName}</c:when>
				<c:otherwise>Add Application</c:otherwise>
			</c:choose>
		</caption>
	
		<spring:bind path="application.applicationName">
		<tr>
			<td class="label required">Application Name:</td>
			<td>
				<input type="text" name="${status.expression}" value="${status.value}"/>          
			</td>
		</tr>
		<c:if test="${status.error}">
		<tr>
			<td></td>
			<td>
				<c:forEach var="errorMessage" items="${status.errorMessages}">
					<span class="error">* ${errorMessage}</span><br/>
				</c:forEach>
			</td>
		</tr>
		</c:if>
		</spring:bind>
		
		<tr>
			<td colspan="2" style="text-align: center;">
				<input type="submit" class="button" value="Save" name="__save"/>
				<c:if test="${application.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteApplication('${application.id}'); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
</form>
</p>

<c:if test="${selectedObjectDefinition != null}">
	<p>
	<table class="list" width="80%">
		<caption>Tabs: ${application.applicationName}</caption>
		<tr>
			<th>Selected</th>
			<th>Available</th>
		</tr>
		<tr>
			<td style="padding: 5px; width: 50%; vertical-align: top;">
				<table class="list" width="100%">
					<c:forEach var="object" items="${selectedObjectDefinition}" varStatus="status">
						<tr>
							<td>${object.objectName}</td>
							<td width="1%">
								<c:choose>
									<c:when test="${!status.first}">
										<a href="javascript:void(0);" onclick="post('applicationObjectEdit', {applicationId:'${application.id}',objectId:'${object.id}',__move:'up'});"><img src="../theme/default/arrow_up.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${!status.last}">
										<a href="javascript:void(0);" onclick="post('applicationObjectEdit', {applicationId:'${application.id}',objectId:'${object.id}',__move:'down'});"><img src="../theme/default/arrow_down.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td width="1%">
								<a href="javascript:void(0);" onclick="post('applicationObjectEdit', {applicationId:'${application.id}',objectId:'${object.id}',__remove:true});"><img src="../theme/default/arrow_right.png"/></a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td style="padding: 5px; width: 50%; vertical-align: top;">
				<table class="list" width="100%">
					<c:forEach var="object" items="${availableObjectDefinition}">
						<tr>
							<td width="1%">
								<a href="javascript:void(0);" onclick="post('applicationObjectEdit', {applicationId:'${application.id}',objectId:'${object.id}',__add:true});"><img src="../theme/default/arrow_left.png"/></a>
							</td>
							<td>${object.objectName}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
	</p>
</c:if>

</div>

<t:focus element="applicationName"/>

<jsp:include page="include/footer.jsp"/>

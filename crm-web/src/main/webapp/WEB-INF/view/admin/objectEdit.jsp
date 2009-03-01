<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteObject(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Object'), 'objectEdit', { id: id, __delete: true });
}
function _deleteField(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Field'), 'fieldEdit', { id: id, __delete: true });
}
function _deleteRelationship(id) {
	confirmPost(format(DELETE_OBJECT_MESSAGE, 'Relationship'), 'relationshipEdit', { id: id, __remove: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="objectList">Objects</a>
&raquo;
<c:choose>
	<c:when test="${objectDefinition.id != null}">Edit Object: ${objectDefinition.objectName}</c:when>
	<c:otherwise>Add Object</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">
	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${objectDefinition.id != null}">Edit Object: ${objectDefinition.objectName}</c:when>
				<c:otherwise>Add Object</c:otherwise>
			</c:choose>
		</caption>
	
		<spring:bind path="objectDefinition.objectName">
		<tr>
			<td class="label required">Object Name:</td>
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
				<c:if test="${objectDefinition.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteObject('${objectDefinition.id}'); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
</form>
</p>

<c:if test="${objectDefinition.id != null}">
	<p>
	<table class="list" width="80%">
		<caption>Fields</caption>
		<tr>
			<th>Field Name</th>
			<th>Data Type</th>
			<th width="1%"></th>
			<th width="1%"><a href="fieldEdit?object_id=${objectDefinition.id}"><img src="../theme/default/add.png"/></a></th>
		</tr>
		<c:forEach var="field" items="${fieldDefinition}" varStatus="status">
			<tr>
				<td>${field.fieldName}</td>
				<td>${field.dataType.title}</td>
				<td>
					<c:choose>
						<c:when test="${!status.first}">
							<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__move:'up'});"><img src="../theme/default/arrow_up.png"/></a>
						</c:when>
						<c:otherwise>
							<img src="../theme/default/blank.gif"/>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!status.last}">
							<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__move:'down'});"><img src="../theme/default/arrow_down.png"/></a>
						</c:when>
						<c:otherwise>
							<img src="../theme/default/blank.gif"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a href="fieldEdit?id=${field.id}"><img src="../theme/default/edit.png"/></a>
					<a href="javascript:void(0);" onclick="_deleteField('${field.id}');"><img src="../theme/default/delete.png"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>
	</p>
	
	<p>
	<table class="list" width="80%">
		<caption>List Fields</caption>
		<tr>
			<th>Selected</th>
			<th>Available</th>
		</tr>
		<tr>
			<td style="padding: 5px; width: 50%; vertical-align: top;">
				<table class="list" width="100%">
					<c:forEach var="field" items="${selectedListFieldDefinition}" varStatus="status">
						<tr>
							<td>${field.fieldName}</td>
							<td width="1%">
								<c:choose>
									<c:when test="${!status.first}">
										<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__moveList:'up'});"><img src="../theme/default/arrow_up.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${!status.last}">
										<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__moveList:'down'});"><img src="../theme/default/arrow_down.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td width="1%">
								<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__removeList:true});"><img src="../theme/default/arrow_right.png"/></a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td style="padding: 5px; width: 50%; vertical-align: top;">
				<table class="list" width="100%">
					<c:forEach var="field" items="${availableListFieldDefinition}">
						<tr>
							<td width="1%">
								<a href="javascript:void(0);" onclick="post('fieldEdit', {id:'${field.id}',__addList:true});"><img src="../theme/default/arrow_left.png"/></a>
							</td>
							<td>${field.fieldName}</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
	</p>
	
	<p>
	<table class="list" width="80%">
		<caption>Relationships</caption>
		<tr>
			<th>Selected</th>
			<th>Available</th>
		</tr>
		<tr>
			<td style="padding: 5px; width: 50%; vertical-align: top;">
				<table class="list" width="100%">
					<c:forEach var="entry" items="${selectedRelationships}" varStatus="status">
						<c:set var="relationship" value="${entry.key}"/>
						<c:set var="object" value="${entry.value}"/>
						<tr>
							<td>${object.objectName}</td>
							<td width="1%">
								<c:choose>
									<c:when test="${!status.first}">
										<a href="javascript:void(0);" onclick="post('relationshipEdit', {id:'${relationship.id}',__move:'up'});"><img src="../theme/default/arrow_up.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${!status.last}">
										<a href="javascript:void(0);" onclick="post('relationshipEdit', {id:'${relationship.id}',__move:'down'});"><img src="../theme/default/arrow_down.png"/></a>
									</c:when>
									<c:otherwise>
										<img src="../theme/default/blank.gif"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td width="1%">
								<a href="javascript:void(0);" onclick="_deleteRelationship('${relationship.id}');"><img src="../theme/default/arrow_right.png"/></a>
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
								<a href="javascript:void(0);" onclick="post('relationshipEdit', {parent_object_id:'${objectDefinition.id}',child_object_id:'${object.id}',__add:true});"><img src="../theme/default/arrow_left.png"/></a>
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

<t:focus element="objectName"/>

<jsp:include page="include/footer.jsp"/>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteField(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Field'), 'fieldEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="objectList">Objects</a>
&raquo;
<a href="objectEdit?id=${objectDefinition.id}">Edit Object: ${objectDefinition.objectName}</a>
&raquo;
<c:choose>
	<c:when test="${fieldDefinition.id != null}">Edit Field: ${fieldDefinition.fieldName}</c:when>
	<c:otherwise>Add Field</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">

	<spring:bind path="fieldDefinition.objectId">
		<input type="hidden" name="${status.expression}" value="${status.value}"/>
	</spring:bind>

	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${fieldDefinition.id != null}">Edit Field: ${fieldDefinition.fieldName}</c:when>
				<c:otherwise>Add Field</c:otherwise>
			</c:choose>
		</caption>
		
		<spring:bind path="fieldDefinition.fieldName">
		<tr>
			<td class="label required">Field Name:</td>
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

		<spring:bind path="fieldDefinition.dataType">
		<tr>
			<td class="label required">Data Type:</td>
			<td>
				<c:choose>
					<c:when test="${fieldDefinition.id == null}">
						<select name="${status.expression}" onchange="document.forms[0].submit();">
							<option value=""></option>
							<c:forEach var="type" items="${dataTypes}">
								<c:set var="selected" value=""/>
								<c:if test="${type.value == status.value}">
									<c:set var="selected" value="selected=\"selected\""/>
								</c:if>
								<option value="${type.value}" ${selected}>${type.title}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						${fieldDefinition.dataType.title}
					</c:otherwise>
				</c:choose>
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

		<spring:bind path="fieldDefinition.dataTypeExt">
		<tr>
			<td class="label required">
				<c:choose>
					<%-- Short Text --%>
					<c:when test="${fieldDefinition.dataType.value == 1}">Size:</c:when>
					<%-- Long Text --%>
					<c:when test="${fieldDefinition.dataType.value == 2}">Size:</c:when>
					<%-- Number --%>
					<c:when test="${fieldDefinition.dataType.value == 4}">Decimal Places:</c:when>
					<%-- Money --%>
					<c:when test="${fieldDefinition.dataType.value == 5}">Decimal Places:</c:when>
					<%-- Percent --%>
					<c:when test="${fieldDefinition.dataType.value == 6}">Decimal Places:</c:when>
					<%-- Option List --%>
					<c:when test="${fieldDefinition.dataType.value == 13}">Option List:</c:when>
					<%-- Object --%>
					<c:when test="${fieldDefinition.dataType.value == 15}">Object:</c:when>
				</c:choose>
			</td>
			<td>
				<c:choose>
					<c:when test="${fieldDefinition.id == null}">
						<c:choose>
							<%-- Short Text --%>
							<c:when test="${fieldDefinition.dataType.value == 1}">
								<input type="text" name="${status.expression}" value="${status.value}" size="10"/>
								(1-255)
							</c:when>
							<%-- Long Text --%>
							<c:when test="${fieldDefinition.dataType.value == 2}">
								<select name="${status.expression}">
									<option value=""></option>
									<c:set var="selected" value=""/>
									<c:if test="${status.value == 1}">
										<c:set var="selected" value="selected=\"selected\""/>
									</c:if>
									<option value="1" ${selected}>Small</option>
									<c:set var="selected" value=""/>
									<c:if test="${status.value == 2}">
										<c:set var="selected" value="selected=\"selected\""/>
									</c:if>
									<option value="2" ${selected}>Medium</option>
									<c:set var="selected" value=""/>
									<c:if test="${status.value == 3}">
										<c:set var="selected" value="selected=\"selected\""/>
									</c:if>
									<option value="3" ${selected}>Large</option>
								</select>
							</c:when>
							<%-- Number --%>
							<c:when test="${fieldDefinition.dataType.value == 4}">
								<input type="text" name="${status.expression}" value="${status.value}" size="10"/>
								(0-6)
							</c:when>
							<%-- Money --%>
							<c:when test="${fieldDefinition.dataType.value == 5}">
								<input type="text" name="${status.expression}" value="${status.value}" size="10"/>
								(0-6)
							</c:when>
							<%-- Percent --%>
							<c:when test="${fieldDefinition.dataType.value == 6}">
								<input type="text" name="${status.expression}" value="${status.value}" size="10"/>
								(0-6)
							</c:when>
							<%-- Option List --%>
							<c:when test="${fieldDefinition.dataType.value == 13}">
								<select name="${status.expression}">
									<option value=""></option>
									<c:forEach var="optionList" items="${optionLists}">
										<c:set var="selected" value=""/>
										<c:if test="${optionList.id == status.value}">
											<c:set var="selected" value="selected=\"selected\""/>
										</c:if>
										<option value="${optionList.id}" ${selected}>${optionList.name}</option>
									</c:forEach>
								</select>
							</c:when>
							<%-- Object --%>
							<c:when test="${fieldDefinition.dataType.value == 15}">
								<select name="${status.expression}">
									<option value=""></option>
									<c:forEach var="object" items="${objectDefinitions}">
										<c:set var="selected" value=""/>
										<c:if test="${object.id == status.value}">
											<c:set var="selected" value="selected=\"selected\""/>
										</c:if>
										<option value="${object.id}" ${selected}>${object.objectName}</option>
									</c:forEach>
								</select>
							</c:when>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:choose>
							<%-- Short Text --%>
							<c:when test="${fieldDefinition.dataType.value == 1}">
								${status.value}
							</c:when>
							<%-- Long Text --%>
							<c:when test="${fieldDefinition.dataType.value == 2}">
								<c:choose>
									<c:when test="${status.value == 1}">Small</c:when>
									<c:when test="${status.value == 2}">Medium</c:when>
									<c:when test="${status.value == 3}">Large</c:when>
								</c:choose>
							</c:when>
							<%-- Number --%>
							<c:when test="${fieldDefinition.dataType.value == 4}">
								${status.value}
							</c:when>
							<%-- Money --%>
							<c:when test="${fieldDefinition.dataType.value == 5}">
								${status.value}
							</c:when>
							<%-- Percent --%>
							<c:when test="${fieldDefinition.dataType.value == 6}">
								${status.value}
							</c:when>
							<%-- Option List --%>
							<c:when test="${fieldDefinition.dataType.value == 13}">
								<c:forEach var="optionList" items="${optionLists}">
									<c:if test="${optionList.id == status.value}">
										${optionList.name}
									</c:if>
								</c:forEach>
							</c:when>
							<%-- Object --%>
							<c:when test="${fieldDefinition.dataType.value == 15}">
								<c:forEach var="object" items="${objectDefinitions}">
									<c:if test="${object.id == status.value}">
										${object.objectName}
									</c:if>
								</c:forEach>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
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
		
		<spring:bind path="fieldDefinition.required">
		<c:choose>
			<%-- Autonumber & Boolean --%>
			<c:when test="${fieldDefinition.dataType.value == 3 || fieldDefinition.dataType.value == 9}">
				<input type="hidden" name="${status.expression}" value="false"/>
			</c:when>
			<%-- Others --%>
			<c:otherwise>
				<tr>
					<td class="label required">Required:</td>
					<td>
						<input type="checkbox" name="${status.expression}" value="true" <c:if test="${status.value}">checked="checked"</c:if>/>
						<input type="hidden" name="_${status.expression}"/>
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
			</c:otherwise>
		</c:choose>
		</spring:bind>
				
		<tr>
			<td colspan="2" style="text-align: center;">
				<input type="submit" class="button" value="Save" name="__save"/>
				<c:if test="${fieldDefinition.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteField(${fieldDefinition.id}); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
	
</form>
</p>

</div>

<t:focus element="fieldName"/>

<jsp:include page="include/footer.jsp"/>

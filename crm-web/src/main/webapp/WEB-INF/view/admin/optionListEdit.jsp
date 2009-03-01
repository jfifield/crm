<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteOptionList(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Option List'), 'optionListEdit', { id: id, __delete: true });
}
function _deleteItem(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Item'), 'optionListItemEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="optionListList">Option Lists</a>
&raquo;
<c:choose>
	<c:when test="${optionList.id != null}">Edit Option List: ${optionList.name}</c:when>
	<c:otherwise>Add Option List</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">
	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${optionList.id != null}">Edit Option List: ${optionList.name}</c:when>
				<c:otherwise>Add Option List</c:otherwise>
			</c:choose>
		</caption>
	
		<spring:bind path="optionList.name">
		<tr>
			<td class="label required">Name:</td>
			<td>
				<input type="text" name="${status.expression}" value="${status.value}" size="32" maxlength="32"/>          
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
				<c:if test="${optionList.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteOptionList('${optionList.id}'); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
</form>
</p>

<c:if test="${optionListItems != null}">
	<p>
	<table class="list" width="80%">
		<caption>Items</caption>
		<tr>
			<th>Value</th>
			<th width="1%"></th>
			<th width="1%"><a href="optionListItemEdit?option_list_id=${optionList.id}"><img src="../theme/default/add.png"/></a></th>
		</tr>
		<c:forEach var="item" items="${optionListItems}" varStatus="status">
			<tr>
				<td>${item.value}</td>
				<td>
					<c:choose>
						<c:when test="${!status.first}">
							<a href="javascript:void(0);" onclick="post('optionListItemEdit', {id:'${item.id}',__move:'up'});"><img src="../theme/default/arrow_up.png"/></a>
						</c:when>
						<c:otherwise>
							<img src="../theme/default/blank.gif"/>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${!status.last}">
							<a href="javascript:void(0);" onclick="post('optionListItemEdit', {id:'${item.id}',__move:'down'});"><img src="../theme/default/arrow_down.png"/></a>
						</c:when>
						<c:otherwise>
							<img src="../theme/default/blank.gif"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a href="optionListItemEdit?id=${item.id}"><img src="../theme/default/edit.png"/></a>
					<a href="javascript:void(0);" onclick="_deleteItem('${item.id}');"><img src="../theme/default/delete.png"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>
	</p>
</c:if>

</div>

<t:focus element="name"/>

<jsp:include page="include/footer.jsp"/>

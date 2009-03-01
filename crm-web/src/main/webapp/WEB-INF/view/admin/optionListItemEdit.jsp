<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteItem(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Item'), 'optionListItemEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="optionListList">Option Lists</a>
&raquo;
<a href="optionListEdit?id=${optionList.id}">Edit Option List: ${optionList.name}</a>
&raquo;
<c:choose>
	<c:when test="${optionListItem.id != null}">Edit Item: ${optionListItem.value}</c:when>
	<c:otherwise>Add Item</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">

	<spring:bind path="optionListItem.optionListId">
		<input type="hidden" name="${status.expression}" value="${status.value}"/>
	</spring:bind>

	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${optionListItem.id != null}">Edit Item: ${optionListItem.value}</c:when>
				<c:otherwise>Add Item</c:otherwise>
			</c:choose>
		</caption>
		
		<spring:bind path="optionListItem.value">
		<tr>
			<td class="label required">Value:</td>
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
				<c:if test="${optionListItem.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteItem('${optionListItem.id}'); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
	
</form>
</p>

</div>

<t:focus element="value"/>

<jsp:include page="include/footer.jsp"/>

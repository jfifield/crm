<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteOptionList(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'Option List'), 'optionListEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
Option Lists
</p>

<div align="center">

<p>
<table class="list" width="80%">
	<caption>Option Lists</caption>
	<tr>
		<th>Name</th>
		<th width="1%"><a href="optionListEdit"><img src="../theme/default/add.png"/></a></th>
	</tr>
	<c:forEach var="optionList" items="${optionLists}">
		<tr>
			<td>${optionList.name}</td>
			<td>
				<a href="optionListEdit?id=${optionList.id}"><img src="../theme/default/edit.png"/></a>
				<a href="javascript:void(0);" onclick="_deleteOptionList('${optionList.id}');"><img src="../theme/default/delete.png"/></a>
			</td>
		</tr>
	</c:forEach>
</table>
</p>

</div>

<jsp:include page="include/footer.jsp"/>

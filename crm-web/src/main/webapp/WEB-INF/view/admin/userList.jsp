<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteUser(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'User'), 'userEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
Users
</p>

<div align="center">

<p>
<table class="list" width="80%">
	<caption>Users</caption>
	<tr>
		<th>Username</th>
		<th>First Name</th>
		<th>Last Name</th>
		<th>Email Address</th>
		<th>Admin</th>
		<th width="1%"><a href="userEdit"><img src="../theme/default/add.png"/></a></th>
	</tr>
	<c:forEach var="user" items="${users}">
		<tr>
			<td>${user.username}</td>
			<td>${user.firstName}</td>
			<td>${user.lastName}</td>
			<td><a href="mailto:${user.emailAddress}">${user.emailAddress}</a></td>
			<td><c:if test="${user.administrator}">Yes</c:if></td>
			<td>
				<a href="userEdit?id=${user.id}"><img src="../theme/default/edit.png"/></a>
				<a href="javascript:void(0);" onclick="_deleteUser('${user.id}');"><img src="../theme/default/delete.png"/></a>
			</td>
		</tr>
	</c:forEach>
</table>
</p>

</div>

<jsp:include page="include/footer.jsp"/>

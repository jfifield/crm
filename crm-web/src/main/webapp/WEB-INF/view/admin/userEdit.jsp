<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<jsp:include page="include/header.jsp"/>

<script type="text/javascript" language="JavaScript">
function _deleteUser(id) {
    confirmPost(format(DELETE_OBJECT_MESSAGE, 'User'), 'userEdit', { id: id, __delete: true });
}
</script>

<p class="breadcrumb">
<a href="home">Administration</a>
&raquo;
<a href="userList">Users</a>
&raquo;
<c:choose>
	<c:when test="${user.id != null}">Edit User: ${user.username}</c:when>
	<c:otherwise>Add User</c:otherwise>
</c:choose>
</p>

<div align="center">

<p>
<form method="post">
	<table class="detail" width="80%">
		<caption>
			<c:choose>
				<c:when test="${user.id != null}">Edit User: ${user.username}</c:when>
				<c:otherwise>Add User</c:otherwise>
			</c:choose>
		</caption>
	
		<spring:bind path="user.username">
		<tr>
			<td class="label required">Username:</td>
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

		<spring:bind path="user.password">
		<tr>
			<td class="label required">Password:</td>
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

		<spring:bind path="user.firstName">
		<tr>
			<td class="label">First Name:</td>
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

		<spring:bind path="user.lastName">
		<tr>
			<td class="label">Last Name:</td>
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

		<spring:bind path="user.emailAddress">
		<tr>
			<td class="label required">Email Address:</td>
			<td>
				<input type="text" name="${status.expression}" value="${status.value}" size="50" maxlength="64"/>          
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

		<spring:bind path="user.administrator">
		<tr>
			<td class="label required">Administrator:</td>
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
		</spring:bind>
		
		<tr>
			<td colspan="2" style="text-align: center;">
				<input type="submit" class="button" value="Save" name="__save"/>
				<c:if test="${user.id != null}">
				<input type="submit" class="button" value="Delete" name="__delete" onclick="_deleteUser('${user.id}'); return false;"/>
				</c:if>
				<input type="submit" class="button" value="Cancel" name="__cancel"/>
			</td>
		</tr>
		
	</table>
</form>
</p>

</div>

<t:focus element="username"/>

<jsp:include page="include/footer.jsp"/>

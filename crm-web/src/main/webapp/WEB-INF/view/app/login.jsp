<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title><spring:message code="application.title"/></title>
	<link rel="stylesheet" type="text/css" href="../theme/default/style.css"/>
</head>
<body>

<div align="center">

<h1>Welcome to <spring:message code="application.title"/></h1>
<br/>

<form method="post">

	<spring:hasBindErrors name="credentials">
		<c:if test="${errors.globalErrorCount > 0}">
			<c:forEach var="error" items="${errors.globalErrors}">
				<span class="error">* <spring:message code="${error.code}" text="${error.defaultMessage}"/></span><br/>
			</c:forEach>
		</c:if>
	</spring:hasBindErrors>

	<table width="25%" class="detail">
		<tr>
			<th colspan="2">Login</th>
		</tr>
	
		<spring:bind path="credentials.username">
		<tr>
			<td class="label">Username: </td>
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
		
		<spring:bind path="credentials.password">
		<tr>
			<td class="label">Password: </td>
			<td>
				<input type="password" name="${status.expression}" />
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
				<input type="submit" class="button" value="Login"/>
			</td>
		</tr>
	</table>
          
</form>

</div>

<t:focus element="username"/>

</body>
</html>
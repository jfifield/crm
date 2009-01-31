<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<jsp:include page="include/header.jsp"/>

<div align="center">

	<h3>Search</h3>

	<form name="searchForm" method="post">
		<table width="35%" class="detail">
			<spring:bind path="searchCriteria.query">
			<tr>
				<td style="text-align: center;">
					<input type="text" name="${status.expression}" value="${fn:escapeXml(status.value)}" size="50"/>          
				</td>
			</tr>
			</spring:bind>
			<tr>
				<td style="text-align: center;">
					<input type="submit" class="button" value="Search"/>
				</td>
			</tr>
		</table>
	</form>

	<c:forEach var="objectResults" items="${searchResults}">
		<p>
			<t:listtable objectDefinition="${objectResults['objectDefinition']}" fieldDefinitions="${objectResults['fieldDefinitions']}" data="${objectResults['data']}" pageSize="10" source="list"/>
		</p>
	</c:forEach>

</div>

<t:focus form="searchForm" element="query"/>

<jsp:include page="include/footer.jsp"/>

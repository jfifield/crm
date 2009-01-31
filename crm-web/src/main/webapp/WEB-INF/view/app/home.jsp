<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="include/header.jsp"/>

<h3>Welcome to <spring:message code="application.title"/>!</h3>

<p>
Eventually this page will be a portal containing relevant information for the current user/application.
</p>

<jsp:include page="include/footer.jsp"/>

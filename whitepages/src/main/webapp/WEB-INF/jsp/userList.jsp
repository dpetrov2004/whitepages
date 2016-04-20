<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>
<style>
.error {
	color: #ff0000;
}
}
</style>
</head>

<body>
	<table style="width: 100%;">
		<tr>
			<th align="left"><a href="<c:url value="/contactList" />"> <spring:message
						text="Contacts" /></a></th>
			<th align="right"><form:form method="post" action="/logout">
					<input type="submit" value="<spring:message text="Logout"/>" />
				</form:form></th>
		</tr>
	</table>

	<h3>
		<spring:message text="Users" />
	</h3>
	
	<form:form modelAttribute="pageFilter">
		<form:errors cssClass="error" />
	</form:form>
	<c:if test="${!empty userList}">
		<table class="data" border="1">
			<tr>
				<th><spring:message text="Id" /></th>
				<th><spring:message text="Login" /></th>
				<th><spring:message text="Full name" /></th>
			</tr>
			<c:forEach items="${userList}" var="user">
				<tr>
					<td><a href="editUser/${user.id}"><spring:message
								text="${user.id}" /></a></td>
					<td><a href="editUser/${user.id}"><spring:message
								text="${user.login}" /></a></td>
					<td><a href="editUser/${user.id}"><spring:message
								text="${user.fullName}" /></a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<form:form method="get" action="editUser/0" modelAttribute="user">
		<input type="submit" value="<spring:message text="Add new user"/>" />
	</form:form>
</body>

</html>

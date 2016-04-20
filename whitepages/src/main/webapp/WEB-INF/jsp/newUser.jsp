<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>

<head>
<title>New User</title>
<style>
.error {
	color: #ff0000;
}
}
</style>
</head>

<body>
	<form:form action="/addNewUser" method="post" modelAttribute="user">
		<form:errors cssClass="error" />
		<table>
			<tr>
				<td><form:label path="id">
						<spring:message text="Id" />
					</form:label></td>
				<td><form:input path="id" readonly="true"/></td>
			</tr>
			<tr>
				<td><form:label path="login">
						<spring:message text="Login" />
					</form:label></td>
				<td><form:input path="login" /></td>
				<td><form:errors path="login" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="password">
						<spring:message text="Password" />
					</form:label></td>
				<td><form:input path="password" /></td>
				<td><form:errors path="password" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="fullName">
						<spring:message text="Full name" />
					</form:label></td>
				<td><form:input path="fullName" /></td>
				<td><form:errors path="fullName" cssClass="error" /></td>
			</tr>
			<form:hidden path="version" />
			<tr>
				<td colspan="2"><input type="submit"
					value="<spring:message text="Create new user"/>" /></td>
			</tr>
		</table>
	</form:form>
</body>

</html>
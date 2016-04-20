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
	<div align="right">
		<form:form method="post" action="/logout">
			<input type="submit" value="<spring:message text="Logout"/>" />
		</form:form>
	</div>
	<h2>
		<spring:message text="Users Manager" />
	</h2>

	<form:form method="post" action="${pageContext.request.contextPath}/editUser/${user.id}" modelAttribute="user">
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
					value="<spring:message text="Save user"/>" /></td>
			</tr>
		</table>
	</form:form>
</body>

</html>

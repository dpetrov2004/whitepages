<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>

<head>
<title>Login</title>
<style>
.error {
	color: #ff0000;
}
}
</style>
</head>

<body>
	<c:if test="${param.error ne null}">
		<div class="error">Invalid username and password.</div>
	</c:if>
	<c:if test="${param.logout ne null}">
		<div class="error">You have been logged out.</div>
	</c:if>
	<form:form action="/login" method="post">
		<table>
			<tr>
				<td><label> User Name : </label></td>
				<td><input type="text" name="username" /></td>
			</tr>
			<tr>
				<td><label> Password: </label></td>
				<td><input type="password" name="password" /></td>
			</tr>
		</table>
		<input type="submit" value="Sign In" />
	</form:form>
	<form:form action="/newUser" method="post">
		<input type="submit" value="Create new user" />
	</form:form>
</body>

</html>
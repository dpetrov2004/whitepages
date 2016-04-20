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
		<spring:message text="Contact Manager" />
	</h2>

	<form:form method="post"
		action="${pageContext.request.contextPath}/editContact/${contact.id}"
		modelAttribute="contact">
		<table>
			<tr>
				<td><form:label path="id">
						<spring:message text="Id" />
					</form:label></td>
				<td><form:input path="id" readonly="true" /></td>
			</tr>
			<tr>
				<td><form:label path="creator.id">
						<spring:message text="Creator" />
					</form:label></td>
				<td><form:input path="creator.id" readonly="true" /></td>
			</tr>
			<tr>
				<td><form:label path="firstName">
						<spring:message text="First name" />
					</form:label></td>
				<td><form:input path="firstName" /></td>
				<td><form:errors path="firstName" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="lastName">
						<spring:message text="Last name" />
					</form:label></td>
				<td><form:input path="lastName" /></td>
				<td><form:errors path="lastName" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="sirName">
						<spring:message text="Sir name" />
					</form:label></td>
				<td><form:input path="sirName" /></td>
				<td><form:errors path="sirName" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="telephoneMobile">
						<spring:message text="Mobile telephone" />
					</form:label></td>
				<td><form:input path="telephoneMobile" /></td>
				<td><form:errors path="telephoneMobile" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="telephoneHome">
						<spring:message text="Home telephone" />
					</form:label></td>
				<td><form:input path="telephoneHome" /></td>
				<td><form:errors path="telephoneHome" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="address">
						<spring:message text="Address" />
					</form:label></td>
				<td><form:input path="address" /></td>
				<td><form:errors path="address" cssClass="error" /></td>
			</tr>
			<tr>
				<td><form:label path="email">
						<spring:message text="Email" />
					</form:label></td>
				<td><form:input path="email" /></td>
				<td><form:errors path="email" cssClass="error" /></td>
			</tr>
			<form:hidden path="version" />
			<tr>
				<td colspan="2"><input type="submit"
					value="<spring:message text="Save contact"/>" /></td>
			</tr>
		</table>
	</form:form>
</body>

</html>

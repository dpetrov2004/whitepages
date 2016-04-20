<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<table style="width: 100%;">
		<tr>
			<th align="left"><a href="<c:url value="/userList" />"> <spring:message
						text="Users" /></a></th>
			<th align="right"><form:form method="post" action="/logout">
					<input type="submit" value="<spring:message text="Logout"/>" />
				</form:form></th>
		</tr>
	</table>

	<h3>
		<spring:message text="Contacts" />
	</h3>

	<form:form method="post" action="${pageContext.request.contextPath}"
		modelAttribute="pageFilter">
		<table>
			<tr>
				<th>&nbsp;</th>
				<th align="left"><spring:message text="First name" /></th>
				<th align="left"><spring:message text="Last name" /></th>
				<th align="left"><spring:message
						text="Telephone (mobile or home)" /></th>
			</tr>
			<tr>
				<td><spring:message text="Filters:" /></td>
				<td><form:input path="firstName" onchange="submit()" /></td>
				<td><form:input path="lastName" onchange="submit()" /></td>
				<td><form:input path="telephone" onchange="submit()" /></td>
			</tr>
		</table>
	</form:form>
	<c:if test="${!empty contactList}">
		<table class="data" border="1">
			<tr>
				<th><spring:message text="Id" /></th>
				<th><spring:message text="First name" /></th>
				<th><spring:message text="Last name" /></th>
				<th><spring:message text="Sir name" /></th>
				<th><spring:message text="Mobile telephone" /></th>
				<th><spring:message text="Home telephone" /></th>
				<th><spring:message text="Address" /></th>
				<th><spring:message text="Email" /></th>
				<th><spring:message text="Creator" /></th>
				<th>&nbsp;</th>
			</tr>
			<c:forEach items="${contactList}" var="contact">
				<tr>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.id}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.firstName}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.lastName}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.sirName}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.telephoneMobile}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.telephoneHome}" /></a></td>
					<td>${contact.address}</td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.email}" /></a></td>
					<td><a href="editContact/${contact.id}"><spring:message
								text="${contact.creator.fullName}" /></a></td>
					<td><a href="deleteContact/${contact.id}"><spring:message
								text="Delete" /></a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<form:form method="get" action="editContact/0" modelAttribute="contact">
		<input type="submit" value="<spring:message text="Add new contact"/>" />
	</form:form>
</body>

</html>

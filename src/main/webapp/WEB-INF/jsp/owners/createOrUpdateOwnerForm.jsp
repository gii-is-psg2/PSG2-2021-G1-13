<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

	<fmt:message key="firstName" var="firstName" />
	<fmt:message key="lastName" var="lastName" />
	<fmt:message key="address" var="address" />
	<fmt:message key="city" var="city" />
	<fmt:message key="telephone" var="telephone" />
	<fmt:message key="username" var="username" />
	<fmt:message key="password" var="password" />
    <h2>
        <c:if test="${owner['new']}"><fmt:message key="new"/></c:if><fmt:message key="owner"/>
    </h2>
    <form:form modelAttribute="owner" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="${firstName}" name="firstName"/>
            <petclinic:inputField label="${lastName}" name="lastName"/>
            <petclinic:inputField label="${address}" name="address"/>
            <petclinic:inputField label="${city}" name="city"/>
            <petclinic:inputField label="${telephone}" name="telephone"/>
            <petclinic:inputField label="${username}" name="user.username"/>
            <petclinic:inputField label="${password}" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${owner['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addOwner"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateOwner"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

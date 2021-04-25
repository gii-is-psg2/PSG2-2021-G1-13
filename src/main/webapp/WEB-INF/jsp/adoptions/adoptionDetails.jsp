<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <jsp:body>
        <h2>
            <fmt:message key="adoption"/>
        </h2>
        <form:form modelAttribute="pet" class="form-horizontal">
            	<div class="form-group has-feedback">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><fmt:message key="owner"/></label>
                    <div class="col-sm-10">
                        <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
                    </div>
                </div>
                <fmt:message key="name" var="name"/>
                <fmt:message key="birthDate" var="birthDate"/>
                <fmt:message key="type" var="type"/>
                <petclinic:inputField label="${name}" name="name" readonly="true"/>
                <petclinic:inputField label="${birthDate}" name="birthDate" readonly="true"/>
                <petclinic:inputField label="${type}" name="type" readonly="true"/>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>

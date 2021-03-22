<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>
        <c:if test="${vet['new']}"><fmt:message key="newo"/> </c:if> <fmt:message key="vet"/>
    </h2>
    <form:form modelAttribute="vet" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
            <fmt:message key="firstName" var="firstname"/>
            <fmt:message key="lastName" var="lastname"/>
            <petclinic:inputField label="${firstname}" name="firstName"/>
            <petclinic:inputField label="${lastname}" name="lastName"/>
            <div class="control-group">
                <fmt:message key="specialties" var="specs"/>
                <petclinic:selectField name="specialties" label="${specs}" names="${specialties}" size="5"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vet['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addVet"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateVet"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">

    <fmt:message key="name" var="name"/>
    <fmt:message key="description" var="description"/>
    <fmt:message key="target" var="target"/>
    <fmt:message key="organization" var="organization"/>
    <fmt:message key="status" var="status"/>
    <h2>
        <c:if test="${cause['new']}"><fmt:message key="new"/></c:if><fmt:message key="cause"/>
    </h2>
    <form:form modelAttribute="cause" class="form-horizontal" id="add-cause-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="${name}" name="name"/>
            <petclinic:inputField label="${description}" name="description"/>
            <petclinic:inputField label="${target}" name="target"/>
            <petclinic:inputField label="${organization}" name="organization"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${cause['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addCause"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateCause"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>

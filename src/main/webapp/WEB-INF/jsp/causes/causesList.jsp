<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<petclinic:layout pageName="causes">
    <h2>Causes</h2>

    <table id="causesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;"><fmt:message key="name"/></th>
            <th style="width: 200px;"><fmt:message key="description"/></th>
            <th><fmt:message key="target"/></th>
            <th style="width: 120px"><fmt:message key="organization"/></th>
            <th><fmt:message key="status"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causes}" var="cause">
            <tr>
                <td>
                    <spring:url value="/causes/{causeId}" var="causeUrl">
                        <spring:param name="causeId" value="${cause.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(causeUrl)}"><c:out value="${cause.name}"/></a>
                </td>
                <td>
                    <c:out value="${cause.description}"/>
                </td>
                <td>
                    <c:out value="${cause.target}"/>
                </td>
                <td>
                    <c:out value="${cause.organization}"/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${cause.closed}">
                            <fmt:message key="closed"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="open"/>
                        </c:otherwise>
                    </c:choose>
                </td>

                
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br/>
    <sec:authorize access="hasAuthority('admin')">
        <a class="btn btn-default" href='<spring:url value="/causes/new" htmlEscape="true"/>'><fmt:message key="addCause"/></a>
    </sec:authorize>
</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<petclinic:layout pageName="causes">

    <h2><fmt:message key="information"/></h2>


    <table class="table table-striped">
        <tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${cause.name}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="description"/></th>
            <td><c:out value="${cause.description}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="target"/></th>
            <td><c:out value="${cause.target}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="organization"/></th>
            <td><c:out value="${cause.organization}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="status"/></th>
            <td><c:choose>
                <c:when test="${cause.closed}">
                    <fmt:message key="closed"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="open"/>
                </c:otherwise>
            </c:choose>
            </td>
        </tr>
    </table>

	<sec:authorize access="hasAuthority('admin')">
    	<spring:url value="{causeId}/edit" var="editUrl">
        	<spring:param name="causeId" value="${cause.id}"/>
    	</spring:url>
    	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default"><fmt:message key="editCause"/></a>
    </sec:authorize>

	<sec:authorize access="hasAuthority('admin')">
		<spring:url value="{causeId}/delete" var="editUrl">
        	<spring:param name="causeId" value="${cause.id}"/>
    	</spring:url>
    	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default"><fmt:message key="deleteCause"/></a>
    </sec:authorize>
    
	<c:choose>
		<c:when test="${open}">
    		<spring:url value="{causeId}/donate" var="addUrl">
        		<spring:param name="causeId" value="${cause.id}"/>
    		</spring:url>
    		<a href="${fn:escapeXml(addUrl)}" class="btn btn-default"><fmt:message key="newDonation"/></a>
		</c:when>
	</c:choose>
    <br/>
    <br/>
    <br/>
    <h2><fmt:message key="donations"/></h2>

    <table id="causesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;"><fmt:message key="date"/></th>
            <th style="width: 200px;"><fmt:message key="amount"/></th>
            <th><fmt:message key="owner"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${cause.donations}" var="donation">
            <tr>
                <td>
                    <c:out value="${donation.date}"/>
                </td>
                <td>
                    <c:out value="${donation.amount}"/>
                </td>
                <td>
                    <c:out value="${donation.client.firstName} ${donation.client.lastName}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>

<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2><fmt:message key="veterinarians"/></h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="firstName"/></th>
            <th><fmt:message key="specialties"/></th>
            <th><fmt:message key="actions"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                    	<fmt:message key="${specialty.name}"/>
                    	<c:out value=" "></c:out>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}"><fmt:message key="none"/></c:if>
                </td>
                <td>
					<sec:authorize access="hasAuthority('admin')">
                    <spring:url value="/vets/{id}/delete" var="editUrl">
       					<spring:param name="id" value="${vet.id}"/>
    				</spring:url>
    				<a href="${fn:escapeXml(editUrl)}"><fmt:message key="deleteVet"/></a>
					</sec:authorize>
                </td>
                <td>
                	<sec:authorize access="hasAuthority('admin')">
                	<spring:url value="/vets/{id}/edit" var="editUrl">
                        <spring:param name="id" value="${vet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}"><fmt:message key="editVet"/></a>
                    </sec:authorize>
    			</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    
				
				

    <sec:authorize access="hasAuthority('admin')">
        <a class="btn btn-default" href='<spring:url value="/vets/new" htmlEscape="true"/>'><fmt:message key="addVet"/></a>
    </sec:authorize>
</petclinic:layout>

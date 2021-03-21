<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2><fmt:message key="veterinarians"/></h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Specialties</th>
            <th>Actions</th>
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

                    <spring:url value="/vets/{id}/delete" var="editUrl">
       					<spring:param name="id" value="${vet.id}"/>
    				</spring:url>
    				<a href="${fn:escapeXml(editUrl)}">Delete Vet</a>

                </td>
                <td>
                
                	<spring:url value="/vets/{id}/edit" var="editUrl">
                        <spring:param name="id" value="${vet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}">Edit Vet</a>
                    
    			</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />"><fmt:message key="xml"/></a>
            </td>            
        </tr>
    </table>
    <sec:authorize access="hasAuthority('admin')">
        <a class="btn btn-default" href='<spring:url value="/vets/new" htmlEscape="true"/>'><fmt:message key="addVet"/></a>
    </sec:authorize>
</petclinic:layout>

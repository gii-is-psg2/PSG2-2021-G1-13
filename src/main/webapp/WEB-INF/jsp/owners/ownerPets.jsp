<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="owner"/><c:out value="${ownerId}"/>&nbsp&nbsp<fmt:message key="pet"/></h2>

    <table id="petTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 25%;"><fmt:message key="name"/></th>
            <th style="width: 25%;"><fmt:message key="birthDate"/></th>
            <th style="width: 25%;"><fmt:message key="type"/></th>
            <th style="width: 25%;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pets}" var="pet">
            <tr>
                <td> 
                   <c:out value="${pet.name}"/> 
                </td> 
                <td> 
                    <c:out value="${pet.birthDate}"/> 
                </td>
                <td> 
                   <c:out value="${pet.type}"/> 
                </td> 
				<td> 
				<c:if test="${pet.adoption == null}">
					<spring:url value="/adoptions/{ownerId}/{petId}/new" var="adoptionFormUrl">
                        <spring:param name="ownerId" value="${ownerId}"/>
                        <spring:param name="petId" value="${pet.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(adoptionFormUrl)}"><fmt:message key="newAdoption"/></a>
				</c:if>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
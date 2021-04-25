<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="owner"/><c:out value="${ownerId}"/>&nbsp<fmt:message key="adoption"/></h2>

    <table id="adoptionTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 20%;"><fmt:message key="pet"/></th>
            <th style="width: 60%;"><fmt:message key="description"/></th>
            <th style="width: 20%;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${adoptions}" var="adoption">
            <tr>
                <td> 
                    <c:out value="${adoption.pet.name}"/> 
                </td>
                <td> 
                   <c:out value="${adoption.description}"/> 
                </td> 
                 <td>
                	<spring:url value="/adoptions/{adoptionId}/{ownerId}/delete" var="adoptionDelete">
                        <spring:param name="ownerId" value="${ownerId}"/>
                        <spring:param name="adoptionId" value="${adoption.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(adoptionDelete)}"><fmt:message key="delAdoption"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="adoptionAppDetails"/></h2>

    <table id="adoptionApplicationTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 15%;"><fmt:message key="owner"/></th>
            <th style="width: 55%;"><fmt:message key="description"/></th>
            <th style="width: 15%;"><fmt:message key="approved"/></th>
            <th style="width: 15%;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${adoptionApllicationDetails}" var="adoptionApllication">
            <tr>
                <td> 
                    <c:out value="${adoptionApllication.owner.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${adoptionApllication.description}"/> 
                </td> 
                <td> 
                   <c:out value="${adoptionApllication.approved}"/> 
                </td> 
                 <td>
                	<spring:url value="/adoptions/{adoptionId}/{ownerId}/delete" var="accept">
                        <spring:param name="ownerId" value="${ownerId}"/>
                        <spring:param name="adoptionId" value="${adoption.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(accept)}"><fmt:message key="accept"/></a>
                    
                    <spring:url value="/adoptionApplication/{adoptionId}/{adoptionApplicationId}/delete" var="reject">
                        <spring:param name="adoptionApplicationId" value="${adoptionApllication.id}"/>
                        <spring:param name="adoptionId" value="${adoptionApllication.adoption.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(reject)}"><fmt:message key="reject"/></a>
                    
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
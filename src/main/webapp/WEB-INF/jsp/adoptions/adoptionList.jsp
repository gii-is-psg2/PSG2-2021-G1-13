<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoption">
    <h2><fmt:message key="availableAdoptions"/></h2>

    <table id="adoptionTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 11%;"><fmt:message key="pet"/></th>
            <th style="width: 60%;"><fmt:message key="description"/></th>
            <th style="width: 15%;"><fmt:message key="owner"/></th>
            <th style="width: 14%;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${adoptions}" var="adoption">
	            <tr>
	            	<td> 
	            		<spring:url value="/adoptions/details/{adoptionId}" var="adoptionDetail">
			                <spring:param name="adoptionId" value="${adoption.id}"/>
			            </spring:url>
			            <a href="${fn:escapeXml(adoptionDetail)}">
			            	<c:out value="${adoption.pet.name}"/> 
			            </a>
	                </td>
	                <td> 
	                   <c:out value="${adoption.description}"/> 
	                </td> 
	                
	                <td> 
	                   <c:out value="${adoption.pet.owner.firstName} ${adoption.pet.owner.lastName}"/> 
	                </td> 
	                
                <td>
                	<spring:url value="/adoptionApplication/new/{ownerId}/{adoptionId}" var="adoptionApp">
                        <spring:param name="ownerId" value="${ownerId}"/>
                        <spring:param name="adoptionId" value="${adoption.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(adoptionApp)}"><fmt:message key="newAdoptionApp"/></a>
                </td>

                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
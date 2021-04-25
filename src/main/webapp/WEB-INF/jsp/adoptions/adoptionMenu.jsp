<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="adoptionMenu"/></h2>
    
    <spring:url value="/adoptions/{ownerId}/list" var="availableAdoptions">
        <spring:param name="ownerId" value="${ownerId}"/>
    </spring:url>
    <a href="${fn:escapeXml(availableAdoptions)}"><fmt:message key="availableAdoptions"/></a>
    <br>
    <spring:url value="/adoptions/{ownerId}" var="ownAdoptions">
        <spring:param name="ownerId" value="${ownerId}"/>
    </spring:url>
    <a href="${fn:escapeXml(ownAdoptions)}"><fmt:message key="ownAdoptions"/></a>
    <br>
    <spring:url value="/owners/{ownerId}/pets" var="ownPets">
        <spring:param name="ownerId" value="${ownerId}"/>
    </spring:url>
    <a href="${fn:escapeXml(ownPets)}"><fmt:message key="ownPets"/></a>	
</petclinic:layout>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hotelReservations">
    <h2><fmt:message key="reservations"/></h2>

    <table id="hotelReservationsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;"><fmt:message key="start"/></th>
            <th style="width: 200px;"><fmt:message key="end"/></th>
            <th style="width: 200px;"><fmt:message key="pet"/></th>
            <th><fmt:message key="actions"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${hotelreservations}" var="hotelReservation">
            <tr>
                <td>
                    <c:out value="${hotelReservation.start}"/>
                </td>
                <td>
                    <c:out value="${hotelReservation.finish}"/>
                </td>
                <td>
                    <c:out value="${hotelReservation.pet.name}"/>
                </td>
            <td>
                	<spring:url value="/hotelreservations/delete/{hotelReservationId}" var="hotelReservationUrl">
                        <spring:param name="hotelReservationId" value="${hotelReservation.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(hotelReservationUrl)}"><fmt:message key="delete"/></a>
                </td>
            
            <td>
                	<spring:url value="/hotelreservations/edit/{hotelReservationId}" var="editUrl">
                        <spring:param name="hotelReservationId" value="${hotelReservation.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}"><fmt:message key="update"/></a>
                </td> 
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="form-group">
    		<form method="get" action="/hotelreservations/new">
    			<button class="btn btn-default" type="submit"><fmt:message key="addReservation"/></button>
			</form>
		</div>
    
</petclinic:layout>
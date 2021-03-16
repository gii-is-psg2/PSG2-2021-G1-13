<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="hotelReservations">
    <h2>Hotel Reservations</h2>

    <table id="hotelReservationsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Start</th>
            <th style="width: 200px;">End</th>
            <th style="width: 200px;">Pet</th>
            <th>Actions</th>
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
                    <a href="${fn:escapeXml(hotelReservationUrl)}">Delete</a>
                </td>
            
            <td>
                	<spring:url value="/hotelreservations/{hotelReservationId}/edit" var="editUrl">
                        <spring:param name="hotelReservationId" value="${hotelReservation.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}">Update</a>
                </td> 
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="form-group">
    		<form method="get" action="/hotelreservations/new">
    			<button class="btn btn-default" type="submit">Add new reservation</button>
			</form>
		</div>
    
</petclinic:layout>
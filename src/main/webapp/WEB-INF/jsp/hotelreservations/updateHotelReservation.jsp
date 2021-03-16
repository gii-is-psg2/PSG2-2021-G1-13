<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

<petclinic:layout pageName="hotelReservations">
	
    	
	<jsp:attribute name="customScript">
		<script>
			$(function () {
				$("#start").datepicker({dateFormat: 'yy/mm/dd'});
				$("#finish").datepicker({dateFormat: 'yy/mm/dd'});
			});
		</script>
	</jsp:attribute>
    <jsp:body>
        <h2>Edit Reservation</h2>
		<script>
    	function chgAction()
    		{
        
        var frm = document.getElementById('id') || null;
        if(frm) {
           frm.action = "/hotelreservations/edit/"+"${hotelReservation.id}";
        }

   			 }
    	</script>
    	<c:out value="${error}"></c:out>
		<form:form modelAttribute="hotelReservation" class="form-horizontal" action="/hotelreservations/edit/{hotelReservationId}" onsubmit = "chgAction()" id = "id">
        
            <div class="form-group has-feedback">
                <petclinic:inputField label="Start" name="start"/>
                <petclinic:inputField label="Finish" name="finish"/>
                <div class="control-group">
                	<petclinic:selectField label="Pet" name="pet" names="${pets}" size="1"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="hotelReservationId" value="${hotelReservation.id}"/>
                    <button class="btn btn-default" type="submit">Add Reservation</button>
                </div>
            </div>
            
        </form:form>

        
    </jsp:body>

</petclinic:layout>
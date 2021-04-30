<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="adoption">

	
    <h2><fmt:message key="newAdoption"/></h2>

	<spring:url value="/adoptions/{ownerId}/{petId}/new" var="adoptionFormUrl">
       <spring:param name="ownerId" value="${ownerId}"/>
       <spring:param name="petId" value="${adoption.pet.id}"/>
   </spring:url>
    
    <form:form modelAttribute="adoption" action="${fn:escapeXml(adoptionFormUrl)}" method="post" class="form-horizontal">
        
        <div class="form-group">
            <div class="control-group" id="pet">
                <label class="col-sm-2 control-label"><fmt:message key="pet"/></label>
                <div class="col-sm-10">
                <c:if test="${error != null}">
                	<h2 class="text-danger"><fmt:message key="${error}"/></h2>
                </c:if>
                    <form:input class="form-control" path="pet" size="30" maxlength="80" readonly="true"/>
                </div>
            </div>
            
              <div class="control-group" id="description">
                <label class="col-sm-2 control-label"><fmt:message key="description"/></label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="description" size="30" maxlength="80"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
            
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default"><fmt:message key="newAdoption"/></button>
            </div>
        </div>

    </form:form>
	
</petclinic:layout>
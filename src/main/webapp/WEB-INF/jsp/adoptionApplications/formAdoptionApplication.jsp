<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="adoption">

	
    <h2><fmt:message key="newAdoptionApp"/></h2>
    
    <form:form modelAttribute="adoptionApplication" action="/adoptionApplication/new" method="post" class="form-horizontal">
        
        <div class="form-group">            
              <div class="control-group" id="description">
                <label class="col-sm-2 control-label"><fmt:message key="description"/></label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="description" size="30" maxlength="80"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
            <form:input class="form-control" path="approved" size="30" maxlength="80" type="hidden"/>
            <form:input class="form-control" path="owner" size="30" maxlength="80" type="hidden"/>
            <form:input class="form-control" path="adoption" size="30" maxlength="80" type="hidden"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default"><fmt:message key="newAdoptionApp"/></button>
            </div>
        </div>

    </form:form>
	
</petclinic:layout>
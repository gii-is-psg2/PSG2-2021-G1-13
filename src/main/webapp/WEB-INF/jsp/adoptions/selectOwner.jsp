<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="owner"/></h2>	
	<form:form modelAttribute="selectOwnerForm" action="/adoptions/menu" method="get" class="form-horizontal">
        <div class="form-group">
            <div class="control-group" id="pet">
                <div class="col-sm-10">
                    <form:select path="owner" size="15">
						<c:forEach items="${ownerList}" var="owner">
							<form:option value="${owner}">${owner.user.username}: ${owner.firstName}&nbsp${owner.lastName}</form:option>
						</c:forEach>
					</form:select>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default"><fmt:message key="select"/></button>
            </div>
        </div>

    </form:form>
	
</petclinic:layout>


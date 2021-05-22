<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="adoptionApp">
    <h2><fmt:message key="owner"/></h2>	
    
    
        <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 20%;"><fmt:message key="username"/></th>
            <th style="width: 20%;"><fmt:message key="firstName"/></th>
            <th style="width: 20%;"><fmt:message key="lastName"/></th>
            <th style="width: 20%"><fmt:message key="telephone"/></th>
            <th style="width: 20%"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${ownerList}" var="owner">
        	<form:form modelAttribute="selectOwnerForm" action="/adoptions" method="post" class="form-horizontal">
	        <form:input type="hidden" path="owner" value="${owner.id}"/>
	         <tr>
                <td>
                    <c:out value="${owner.user.username}"/>
                </td>
                <td>
                    <c:out value="${owner.firstName}"/>
                </td>
                <td>
                    <c:out value="${owner.lastName}"/>
                </td>
                <td>
                    <c:out value="${owner.telephone}"/>
                </td>
                <td>
                    <button type="submit" class="btn btn-default"><fmt:message key="select"/></button>
                </td>             
            </tr>
	    </form:form>
       
        </c:forEach>
        </tbody>
    </table>	
</petclinic:layout>


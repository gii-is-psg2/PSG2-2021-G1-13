<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">
    <fmt:message key="owner"/>
    <fmt:message key="amount" var="amount"/>

    <form:form modelAttribute="donation" class="form-horizontal" id="add-donation-form">
        <div class="form-group has-feedback">
            <form:select path="client" size="15">
                <c:forEach items="${ownerList}" var="owner">
                    <form:option value="${owner}">${owner.user.username}: ${owner.firstName}&nbsp${owner.lastName}</form:option>
                </c:forEach>
            </form:select>
            <petclinic:inputField label="${amount}" name="amount"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default" onclick="return confirm('Do you really want to continue?')" ><fmt:message key="addDonation"/></button>
            </div>
        </div>

    </form:form>

</petclinic:layout>

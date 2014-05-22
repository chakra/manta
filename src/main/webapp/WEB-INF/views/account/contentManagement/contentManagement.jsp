<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/account/${accountContentManagement.accountId}"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/contentManagement/save');$('form:first').submit(); return false; "/>

<div class="canvas">

<div class="details">

<form:form modelAttribute="accountContentManagement" action="" method="POST">
<table>
<tr><td>
    <table>
        <tbody>
            <tr>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" path="displayGenericContent" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="displayGenericContent"><app:message code="admin.setup.label.displayGenericContent" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="customContentURL"><app:message code="admin.setup.label.customContentUrl"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="6" path="customContentURL" style="width:162px"/></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="customContentEditor"><app:message code="admin.setup.label.customContentEditor"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="6" path="customContentEditor" style="width:162px"/></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="customContentViewer"><app:message code="admin.setup.label.customContentViewer"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="6" path="customContentViewer" style="width:162px"/></td>
            </tr>
        </tbody>

        <tbody>
        
    </table>
    
    <table>
        <tr><td  align="center">
            <form:button tabindex="27" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
        </td></tr>
    </table>
    
</td></tr>
</table>
<form:hidden path="accountId" value="${accountContentManagement.accountId}"/>
</form:form>

</div>
</div>
        

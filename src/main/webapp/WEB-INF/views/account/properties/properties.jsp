<%@ page import="java.util.Date" %>
<%@ page import="com.espendwise.manta.auth.AppUser" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/account/${accountProperties.accountId}"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/properties/save');$('form:first').submit(); return false; "/>

<div class="canvas">

<div class="details">

<form:form modelAttribute="accountProperties" action="" method="POST">
<table width = "100%">
<tr><td>
    <table width = "100%">
        <tbody>
            <tr>
                <td width = "50%">
                    <div class="label">
                        <form:checkbox tabIndex="1" cssClass="checkbox" path="allowToChangePassword" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="allowToChangePassword"><app:message code="admin.account.properties.label.allowToChangePassword" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>

                <td><div class="label"><form:label path="purchasingSystem"><app:message code="admin.account.properties.label.purchasingSystem"/><span class="colon">:</span></form:label></div></td>
                <td>
                   <form:select tabindex="2" style="width:200px" path="purchasingSystem"><form:option value=""><app:message code="admin.global.select"/></form:option>
	                    <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.purchasingSystemCds}">
	                        <form:option value="${type.object2}"><app:message code="refcodes.CUSTOMER_SYSTEM_APPROVAL_CD.${type.object1}" text="${type.object2}"/></form:option>
	                    </c:forEach>
                   </form:select>
                 </td>
                
            </tr>
            <tr>
                <td width = "50%">
                    <div class="label">
                        <form:checkbox tabIndex="3" cssClass="checkbox" path="allowToEnterPurchaseNum" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="allowToEnterPurchaseNum"><app:message code="admin.account.properties.label.allowToEnterPurchaseNum" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
                <td colspan ="2">
                    <div class="label">
	            	<%
	                    AppUser appUser = Auth.getAppUser();
	                    String isControlDisabled = new Boolean(!appUser.isAdmin() && !appUser.isSystemAdmin()).toString();
	                %>
                        <form:checkbox tabIndex="4" cssClass="checkbox" path="trackPunchoutOrderMessages" value="true" disabled="<%=isControlDisabled%>"/>
                        <form:label cssStyle="padding-left: 5px" path="trackPunchoutOrderMessages"><app:message code="admin.account.properties.label.trackPunchoutOrderMessages" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan ="3">
                    <div class="label">
                        <form:checkbox tabIndex="5" cssClass="checkbox" path="allowToPayCreditCard" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="allowToPayCreditCard"><app:message code="admin.account.properties.label.allowToPayCreditCard" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan ="3">
                    <div class="label">
                        <form:checkbox tabIndex="6" cssClass="checkbox" path="useEstimatedSalesTax" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="useEstimatedSalesTax"><app:message code="admin.account.properties.label.useEstimatedSalesTax" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan ="3">
                    <div class="label">
                        <form:checkbox tabIndex="7" cssClass="checkbox" path="useResaleItems" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="useResaleItems"><app:message code="admin.account.properties.label.useResaleItems" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan ="3">
                    <div class="label">
                        <form:checkbox tabIndex="8" cssClass="checkbox" path="useBudgets" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="useBudgets"><app:message code="admin.account.properties.label.useBudgets" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan ="3">
                    <div class="label">
                        <form:checkbox tabIndex="9" cssClass="checkbox" path="useAlternateUserInterface" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="useAlternateUserInterface"><app:message code="admin.account.properties.label.useAlternateUserInterface" text="GENERIC_CONTENT"/></form:label>
                    </div>
                </td>
            </tr>

        </tbody>

          
    </table>
    
    <table width = "100%">
        <tr><td  align="center">
            <form:button tabindex="10" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
        </td></tr>
    </table>
    
</td></tr>
</table>
<form:hidden path="accountId" value="${accountProperties.accountId}"/>
</form:form>

</div>
</div>
        

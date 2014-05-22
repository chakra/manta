<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.model.view.UserIdentView" %>
<%@ page import="com.espendwise.manta.model.data.UserData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/user/${userId > 0 ? userId: 0}');$('form:first').submit(); return false; "/>
<c:set var="cloneAction" value="$('form:first').attr('action','${baseUrl}/user/${user.userId > 0 ? user.userId: 0}/clone');$('form:first').submit(); return false; "/>
<c:set var="changeUserType" value="$('form:first').attr('action','${baseUrl}/user/${user.userId > 0 ? user.userId : 0}/changeUserType');$('form:first').submit();"/>
<c:set var="resetAction" value="$('form:first').attr('action','${baseUrl}/user/${user.userId > 0 ? user.userId : 0}/reset');$('form:first').submit(); return false;"/>

<app:message var="dateFormat" code="format.prompt.dateFormat" />

<app:dateIncludes/>

<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState($('#countryCd').attr("value"),'#state', jscountries, true, 'invisible')});
</script>

<script type="text/javascript">
    function inputKeyHit(e) {
        var key = 13;
        if (e) {
            key = window.event ? e.keyCode : e.which;
        }

        var ivrUINum = document.getElementById('ivrUserIdentificationNumberId');
        var pinMandatory = document.getElementById('pinMandatoryId');
        var pinConfirmMandatory = document.getElementById('pinConfirmMandatoryId');
        var ivrPIN = document.getElementById('ivrPINId');
        var ivrConfirmPIN = document.getElementById('ivrConfirmPINId');

        if (key == 13 || 'undefined' == typeof key) {
            if (ivrUINum &&
                    pinMandatory && pinConfirmMandatory &&
                    ivrPIN && ivrConfirmPIN) {
                if (ivrUINum.value.length > 0) {
                    pinMandatory.style.visibility = "";
                    pinConfirmMandatory.style.visibility = "";

                    ivrPIN.disabled = "";
                    ivrPIN.style.backgroundColor = "#fff";

                    ivrConfirmPIN.disabled = "";
                    ivrConfirmPIN.style.backgroundColor = "#fff";
                } else {
                    pinMandatory.style.visibility = "hidden";
                    pinConfirmMandatory.style.visibility = "hidden";

                    ivrPIN.disabled = "disabled";
                    ivrPIN.value = "";
                    ivrPIN.style.backgroundColor = "#ccc";

                    ivrConfirmPIN.disabled = "disabled";
                    ivrConfirmPIN.value = "";
                    ivrConfirmPIN.style.backgroundColor = "#ccc";
                }
            }
            return false;
        } else {
            return true;
        }
    }

    window.onload = inputKeyHit;

    <c:if test="${user.canUseLoginAs}">

    function _loginas() {
        try{
            $.ajax({ url: '${baseUrl}/user/${userId > 0 ? userId : 0}/loginas', cache:false, async:true,
                        data:$("#user").serialize(),
                        type:'POST',
                        success:function (value) { if(value && value.length && value.length > 0) { locatewin = window.open(value, "Login_As_To_"+$("input[name='loginAsTarget']:checked").attr("value"), "menubar=no,resizable=yes,scrollbars=yes,toolbar=no,status=yes,height=650,width=1020,left=80,top=85"); locatewin.focus();}},
                        error:function () { }
                    }
            );
        }   catch(e) {
        }
        return false;
    }
    </c:if>

</script>

<div class="canvas">

<div class="details">

<c:choose>
	<c:when test="${user.userId != null && user.userId > 0}">
		<c:set var="focusField" value="userLogonName"/>
	</c:when>
	<c:otherwise>
		<c:set var="focusField" value="userType"/>
	</c:otherwise>
</c:choose>
<form:form modelAttribute="user" id="user" action="" method="POST" focus="${focusField}">
<form:hidden path="isClone" value="${user.isClone}"/>
<form:hidden path="sourceUserId" value="${user.sourceUserId}"/>
<table width="100%">

<tr>
    <td>
        <table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: left">
                    <div class="row">
                        <div class="cell">
                            <table>
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userId"><app:message code="admin.user.label.userId"/><span class="colon">:</span></form:label>
                                        </div>
                                    </td>
                                    <td><div class="labelValue"><c:out value="${user.userId}" default="0"/></div><form:hidden path="userId" value="${user.userId}"/></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td style="text-align: right"></td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userType"><app:message code="admin.user.label.type"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.userId != null && user.userId > 0}">
                                                <app:refcdMap var="userTypeMap" code="USER_TYPE_CD" invert="true"/>
                                                <app:message code="refcodes.USER_TYPE_CD.${userTypeMap[user.userType]}" text="${user.userType}"/>
                                                <form:hidden path="userType" value="${userType}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
                                                <form:select tabindex="1" style="width:238px" path="userType" onchange="${changeUserType}">
                                                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                                                    <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.getAllowedUserTypes(currentUserType)}">
                                                        <form:option value="${type.object2}"><app:message code="refcodes.USER_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td colspan="3" align="right">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userLogonName"><app:message code="admin.user.label.userName"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                        </div>
                                    </td>
                                    <td>
                                        <form:input tabindex="2" path="userLogonName" size="35" style="width:230px"/>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="userStatus"><app:message code="admin.user.label.status"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                            </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="7" style="width:200px" path="userStatus">
                                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}">
                                                <form:option value="${type.object2}"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${type.object1}" text="${type.object2}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </td>
                                    <td style="vertical-align: top;"> </td>
                                </tr>


                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userPassword"><app:message code="admin.user.label.newPassword"/><span class="colon">:</span></form:label>
                                            <c:if test="${user.userId == null || user.userId == 0}">
                                                <span class="reqind">*</span>
                                            </c:if>
                                        </div>
                                    </td>
                                    <td>
                                        <form:password tabindex="3" path="userPassword" size="35" showPassword="true" style="width:230px"/>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="userActiveDate"><app:message code="admin.user.label.userActiveDate"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                            </div>
                                    </td>
                                    <td class="cell value">
                                        <form:input tabindex="8" path="userActiveDate" cssClass="datepicker2Col standardCal standardActiveCal" style="width:190px"/>
                                    </td>
                                </tr>


                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userConfirmPassword"><app:message code="admin.user.label.confirmPassword"/><span class="colon">:</span></form:label>
                                            <c:if test="${user.userId == null || user.userId == 0}">
                                                <span class="reqind">*</span>
                                            </c:if>
                                        </div>
                                    </td>
                                    <td>
                                        <form:password tabindex="4" path="userConfirmPassword" size="35" showPassword="true" style="width:230px"/>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="userInactiveDate"><app:message code="admin.user.label.userInactiveDate"/><span class="colon">:</span></form:label>
                                            </div>
                                    </td>
                                    <td class="cell value">
                                        <form:input tabindex="9" path="userInactiveDate" cssClass="datepicker2Col standardCal standardActiveCal" style="width:190px"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userCode"><app:message code="admin.user.label.userCode"/><span class="colon">:</span></form:label>
                                        </div>
                                    </td>
                                    <td>
                                        <form:input tabindex="5" path="userCode" size="35"  style="width:230px"/>
                                    </td>
                                    <td colspan="3">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="userLanguage"><app:message code="admin.user.label.preferredLanguage"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                        </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="6" style="width:238px" path="userLanguage">
                                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                                            <c:forEach var="type" items="${user.availableLanguages}">
                                                <form:option value="${type.languageCode}">${type.uiName}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </td>
                                    <td colspan="3">&nbsp;</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </td>

                <td style="vertical-align:top;text-align: right;white-space: nowrap;">

                    <table width="100%">
                        <tbody>
                        <tr>
                            <td style="text-align: right">
                                <div class="actions">
                                    <form:button tabindex="1000" disabled="${!user.multiStoreDb || user.mainDbAlive == true ? false : true}"  onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                                    <c:if test="${user.userId != null && user.userId > 0}">
                                        &nbsp;&nbsp;&nbsp; <form:button disabled="${!user.multiStoreDb || user.mainDbAlive == true ? false : true}" tabindex="30" onclick="${resetAction} return false;"><app:message code="admin.global.button.reset"/></form:button>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                        <tbody>
                        <tr>
                            <td align="right">
                                <br>
                                <table>
                                    <tr>
                                        <td>
                                            <app:box name="admin.user.label.actions" cssClass="white">
                                                <table cellpadding="0" cellspacing="0" border="0">
                                                    <tbody>
                                                    <c:if test="${user.canUseLoginAs}">
                                                        <tr>
                                                            <td nowrap>
                                                                <form:button tabindex="10" onclick="javascript: return _loginas()">
                                                                    <app:message code="admin.global.button.loginAs"/>
                                                                </form:button>
                                                                <br> <form:radiobutton cssClass="radio" path="loginAsTarget" value="PROCUREMENT"/>&nbsp;<app:message code="admin.user.loginAs.label.procurement"/>
                                                                <br><form:radiobutton cssClass="radio" path="loginAsTarget" value="SERVICES"/>&nbsp;<app:message code="admin.user.loginAs.label.services"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 0"><hr></td>
                                                        </tr>
                                                    </c:if>
                                                    <tr>
                                                        <td nowrap>
                                                            <form:button  disabled="${(!user.multiStoreDb || user.mainDbAlive == true) && (user.userId != null && user.userId > 0) ? false : true}"  tabindex="11" onclick="${cloneAction} return false;">
                                                                <app:message code="admin.user.button.cloneUser"/>
                                                            </form:button>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </app:box>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                </td>

            </tr>

        </table>
    </td>
</tr>

<tr><td><hr/></td></tr>

<tr>
    <td>
        <table>
            <tbody>
            <tr>
                <td colspan="5">
                    <div class="subHeader"><app:message code="admin.user.label.contactInformation"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.firstName"><app:message code="admin.user.label.firstName"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td>
                    <form:input tabindex="11" path="userContact.contact.firstName" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.telephone"><app:message code="admin.user.label.phone"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="19" path="userContact.contact.telephone" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.lastName"><app:message code="admin.user.label.lastName"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td>
                    <form:input tabindex="12" path="userContact.contact.lastName" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.fax"><app:message code="admin.user.label.fax"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="20" path="userContact.contact.fax" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.address1"><app:message code="admin.user.label.address1"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td>
                    <form:input tabindex="13" path="userContact.contact.address.address1" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.mobile"><app:message code="admin.user.label.mobile"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="21" path="userContact.contact.mobile" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.address2"><app:message code="admin.user.label.address2"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td>
                    <form:input tabindex="14" path="userContact.contact.address.address2" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.email"><app:message code="admin.user.label.email"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="22" path="userContact.contact.email" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.city"><app:message code="admin.user.label.city"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td>
                    <form:input tabindex="15" path="userContact.contact.address.city" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.escalationEmail"><app:message code="admin.user.label.escalationEmail"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="23" path="userContact.escalationEmail" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.postalCode"><app:message code="admin.user.label.postalCode"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td>
                    <form:input tabindex="16" path="userContact.contact.address.postalCode" size="35" style="width:230px"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="userContact.textingAddress"><app:message code="admin.user.label.textingAddress"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td colspan="2">
                    <form:input tabindex="24" path="userContact.textingAddress" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.stateProvinceCd">
                        <app:message code="admin.user.label.stateProvince"/>
                        <span id="state" class="reqind invisible">
               					*
               			</span>
                        </form:label>
                    </div>
                </td>
                <td colspan="4" >
                    <form:input tabindex="17" path="userContact.contact.address.stateProvinceCd" size="35" style="width:230px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="userContact.contact.address.countryCd"><app:message code="admin.user.label.country"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                    </div>
                </td>
                <td colspan="4" >
                    <form:select tabindex="18" id="countryCd" style="width:238px" path="userContact.contact.address.countryCd" onchange="countryUsesState(this.value,'#state', jscountries,false,'invisible')" >
                        <form:option value=""><app:message code="admin.global.select"/></form:option>
                        <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">
                            <form:option value="${country.shortDesc}">${country.uiName}</form:option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            </tbody>
        </table>
    </td>
</tr>

<tr><td><hr/></td></tr>

<tr>
    <td>
        <table>
            <tbody>
            <tr>
                <td colspan="5">
                    <div class="subHeader"><app:message code="admin.user.label.interactiveVoiceResponse"/></div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="label">
                        <form:label path="ivrUserIdentificationNumber"><app:message code="admin.user.label.userIdentificationNumber"/><span class="colon">:</span></form:label>
                    </div>
                </td>
                <td>
                    <form:input tabindex="25" id="ivrUserIdentificationNumberId" path="ivrUserIdentificationNumber" size="35"
                                onkeypress="javascript: return inputKeyHit(event);" onblur="javascript: return inputKeyHit();"/>
                </td>
                <td>
                    <div class="label">
                        <form:label path="ivrPIN"><app:message code="admin.user.label.PIN"/><span class="colon">:</span></form:label>
                        <span id="pinMandatoryId" class="reqind" style="visibility: hidden;">*</span>
                    </div>
                </td>
                <td colspan="2">
                    <form:password tabindex="26" id="ivrPINId" path="ivrPIN" size="35" showPassword="true"/>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <div class="label">
                        (<app:message code="admin.user.label.mobilePhoneNumber"/>)
                    </div>
                </td>
                <td>
                    <div class="label">
                        <form:label path="ivrConfirmPIN"><app:message code="admin.user.label.confirmPIN"/><span class="colon">:</span></form:label>
                        <span id="pinConfirmMandatoryId" class="reqind" style="visibility: hidden;">*</span>
                    </div>
                </td>
                <td colspan="2">
                    <form:password tabindex="27" id="ivrConfirmPINId" path="ivrConfirmPIN" size="35" showPassword="true"/>
                </td>
            </tr>
            </tbody>
        </table>
    </td>
</tr>

<tr><td><hr/></td></tr>



<!--- ######################################### --->
<!--- #############   PERMISSIONS        ###### --->
<!--- ######################################### --->

<tr>
    <td class="subitem">
        <div class="row">
            <div class="cell" style="width:50%">
                <table width="100%">
                    <colgroup>
                        <col width="50%"/>
                        <col width="50%"/>
                    </colgroup>
                    <thead><tr><td colspan="2"><div class="subHeader"><app:message code="admin.user.label.permissions"/></div></td></tr></thead>
                    <tbody>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="28" cssClass="checkbox" path="approveOrders"/>
                            <span class="label"><app:message code="admin.user.label.approveOrders" /></span>
                        </td>
                        <td class="cell label">
                            <form:checkbox tabindex="31" cssClass="checkbox" path="doesNotUseReporting"/>
                            <span class="label"><app:message code="admin.user.label.doesNotUseReporting" /></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="29" cssClass="checkbox" path="browseOnly"/>
                            <span class="label"><app:message code="admin.user.label.browseOnly" /></span>
                        </td>
                        <td class="cell">
                            <form:checkbox tabindex="32" cssClass="checkbox" path="updateBillToInformation"/>
                            <span class="label"><app:message code="admin.user.label.updateBillToInformation" /></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="30" cssClass="checkbox" path="corporateUser"/>
                            <span class="label"><app:message code="admin.user.label.corporateUser" /></span>
                        </td>
                        <td class="cell label">
                            <form:checkbox tabindex="33" cssClass="checkbox" path="updateShipToInformation"/>
                            <span class="label"><app:message code="admin.user.label.updateShipToInformation" /></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="cell" style="padding-left:50px;padding-top:30px">
                <app:box name="admin.user.label.services" cssClass="white">
                    <table style="padding:0" cellpadding="0" cellspacing="0" border="0" >
                        <tbody style="padding:0">
                        <c:forEach var="userRole" items="${requestScope['appResource'].dbConstantsResource.userRoleCds}">
                            <tr>
                                <td style="padding:0;padding-bottom: 5px"><form:radiobutton tabindex="34" class="radio" path="userRole" value="${userRole.object2}"/></td>
                                <td style="padding:0;" class="label"><app:message code="refcodes.USER_ROLE_CD.UI.${userRole.object1}" text="${userRole.object2}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </app:box>
            </div>

        </div>

    </td>
</tr>


<tr><td><hr/></td></tr>

<!--- ######################################### --->
<!--- #############   PURCHASING         ####### --->
<!--- ######################################### --->

<tr>
    <td class="subitem">
        <div class="row">
            <div class="cell" style="width:50%">
                <table width="100%">
                    <colgroup>
                        <col width="50%"/>
                        <col width="50%"/>
                    </colgroup>
                    <thead><tr><td colspan="2"> <div class="subHeader"><app:message code="admin.user.label.purchasing"/></div></td></tr></thead>
                    <tbody>
                    <tr>
                        <td class="cell label"><div class="label"><app:message code="admin.user.label.payment" /></div></td>
                        <td class="cell label"><div class="label"><app:message code="admin.user.label.restrictions" /></div></td>
                    </tr>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="36" cssClass="checkbox" path="creditCard"/>
                            <span class="label"><app:message code="admin.user.label.creditCard"/></span>
                        </td>
                        <td class="cell label"><form:checkbox tabindex="39" cssClass="checkbox" path="poNumRequired"/>
                            <span class="label"><app:message code="admin.user.label.poNumRequired"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="37" cssClass="checkbox" path="onAccount"/>
                            <span class="label"><app:message code="admin.user.label.onAccount" /></span>
                        </td>
                        <td  class="cell label">
                            <form:checkbox tabindex="40" cssClass="checkbox" path="showPrices"/>
                            <span class="label"><app:message code="admin.user.label.showPrices" /></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell label">
                            <form:checkbox tabindex="38" cssClass="checkbox" path="otherPayment"/>
                            <span class="label"><app:message code="admin.user.label.otherPayment" /></span>
                        </td>
                        <td class="cell label">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </td>
</tr>


<tr><td><hr/></td></tr>


<!--- ######################################### --->
<!--- ####x# PRIMARY ENTITY ASSOCIATION ####### --->
<!--- ######################################### --->

<tr valign="top" >
    <td valign="top" width="100%">
        <table width="100%" valign="top">
            <thead><tr><td colspan="5"> <div class="subHeader"><app:message code="admin.user.label.primaryEntityAssociations"/></div></td></tr></thead>

            <tr valign="top" width="100%">
				<td colspan="5"  valign="top" width="100%" align="center">
                    <c:choose>
                        <c:when test="${!user.multiStoreDb || user.mainDbAlive == true}">
							<c:if test="${user.userType != null}">
                                <c:set var="administrator" value="<%=RefCodeNames.USER_TYPE_CD.ADMINISTRATOR%>" />
                                <c:set var="store_administrator" value="<%=RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR%>" />
                                <c:set var="system_administrator" value="<%=RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR%>" />

                                <c:set var="readOnlyFlag" value="${(administrator == user.userType || store_administrator == user.userType || system_administrator == user.userType) ? false : true}" />
								<c:set var="entlength" value="${fn:length(user.entities.selectableObjects)}"/>
								<c:set var="columnCount" value="${(entlength - (entlength % 5)) / 5}" />
								<c:set var="columnCount" value="${(columnCount == 0) ? 1 : columnCount}" />
								<c:set var="columnCount" value="${columnCount < 4 ? columnCount : 3 }" />
								<c:set var="colElementsCount" value="${(entlength - (entlength % columnCount)) / columnCount}" />
                                <c:if test="${entlength <= 10}">
                                  <c:set var="columnCount" value="1"/>
                                  <c:set var="colElementsCount" value="${entlength}"/>
                                </c:if>
								<table width="100%"  valign="top">
                                <tr valign="top">
                                    <c:forEach var="ii" begin="0" end="${columnCount-1}">
										<c:set var="startInd" value="${ii*colElementsCount}"/>
										<c:set var="endInd" value="${ii<(columnCount-1) ? (startInd + colElementsCount - 1) : (entlength+1)}"/>
										<c:set var="showCol" value="false"/>
                                        <c:forEach var="entity" items="${user.entities.selectableObjects}" varStatus="i" begin="${startInd}" end="${endInd}">
                                            <c:if test="${(!readOnlyFlag) || (entity.value.mainStoreId == user.currentStore[0].mainStoreId)}">
												<c:set var="showCol" value="true"/>
											</c:if>
										</c:forEach>
									<c:if test="${showCol}">
                                    <td valign="top" style="vertical-align: top;">
										<table valign="top">
											<tr class="row">
												<td class="cell cell-text">
													<div class="label"><app:message code="admin.user.label.entityName" /></div>
												</td>
												<td class="cell cell-text">
													<div class="label"><app:message code="admin.user.label.default" /></div>
												</td>
											</tr>
                                            <c:forEach var="entity" items="${user.entities.selectableObjects}" varStatus="i" begin="${startInd}" end="${endInd}">
                                                <c:if test="${(!readOnlyFlag) || (entity.value.mainStoreId == user.currentStore[0].mainStoreId)}">
                                                    <tr class="row">
                                                        <td class="cell cell-text" align="left">
                                                            <form:checkbox cssClass="checkbox" tabindex="${40 + i.count}" path="entities.selectableObjects[${i.index}].selected" disabled="${readOnlyFlag}"/>
                                                            <c:if test="${readOnlyFlag}">
                                                                <form:hidden path="entities.selectableObjects[${i.index}].selected" value="true"/>
                                                            </c:if>
                                                            <c:out value="${entity.value.storeName}" />
                                                        </td>
                                                        <td class="cell cell-text">
                                                            <form:radiobutton class="radio" tabindex="${41 + fn:length(user.entities.selectableObjects)}" path="defaultStoreId" value="${entity.value.mainStoreId}" disabled="${readOnlyFlag}"/>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${readOnlyFlag}">
                                                <form:hidden path="defaultStoreId"/>
                                            </c:if>
										</table>
									</td>
									</c:if>
									</c:forEach>
                                 </tr>
                                 </table>
                            </c:if>
                        </c:when>
						<c:otherwise>
							<div class="label" style="text-align: center">(<app:message code="admin.user.text.warning.mainDbUnavailable"/>)</div>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
	</td>
</tr>

<tr><td><hr/></td></tr>

<tr>
    <td>
        <table width="100%">
            <tr>
                <td style="text-align: right;white-space: nowrap;">
                    <form:button disabled="${!user.multiStoreDb || user.mainDbAlive == true ? false : true}" tabindex="1000" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                    <c:if test="${user.userId != null && user.userId > 0}">
                        &nbsp;&nbsp;&nbsp;<form:button disabled="${!user.multiStoreDb || user.mainDbAlive == true ? false : true}" tabindex="30" onclick="${resetAction} return false;"><app:message code="admin.global.button.reset"/></form:button>
                    </c:if>
                </td>
            </tr>
        </table>
    </td>
</tr>

</table>
</form:form>

</div>
</div>


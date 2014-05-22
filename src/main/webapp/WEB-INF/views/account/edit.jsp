<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.RefCodeNamesKeys" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>

<app:url var="baseUtl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUtl}/account/${account.accountId > 0?account.accountId: 0}');$('form:first').submit(); return false; "/>
<c:set var="clearAction" value="$('form:first').attr('action','${baseUtl}/account/${account.accountId > 0?account.accountId: 0}/clear');$('form:first').submit(); return false;"/>
<c:set var="cloneAction" value="$('form:first').attr('action','${baseUtl}/account/${account.accountId > 0?account.accountId: 0}/clone');$('form:first').submit(); return false; "/>


<%
  String err = AppI18nUtil.getMessage("validation.web.error.emptyBillingAddress");
%>

<script>
function checkAndCopyValues(from, to) {
        var empty = true;
        $.each(from, function(i,val) {
            var aa = val?document.getElementById(val).value:null;
            if (aa != null && aa.length > 0) {
                empty = false;
            }
        }  );
        if (empty) {
            if ('undefined' == $(".webError") || $(".webError").text().length == 0 ) {
                $(".webErrors").text(
                "<%=err%>"
                );
            }
		} else {
            copyValues(from, to);
            $(".webErrors").text("");
        }
}
</script>


<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState(document.getElementById('billingAddress.countryCd').value,'#billingReqindStateSign', jscountries, true)});
    var billlinAdressFields = ['billingAddress.address1', 'billingAddress.address2', 'billingAddress.address3', 'billingAddress.city', 'billingAddress.stateProvinceCd' , 'billingAddress.postalCode' ,'billingAddress.countryCd'];
    var primaryAdressFields = ['primaryContact.address.address1', 'primaryContact.address.address2', 'primaryContact.address.address3', 'primaryContact.address.city', 'primaryContact.address.stateProvinceCd' , 'primaryContact.address.postalCode' ,'primaryContact.address.countryCd'];
</script>

<div class="canvas">

<div class="details">

<form:form modelAttribute="account" action="" method="POST">
<table>
<tr><td>
    <table>
        <tbody>
        <tr>
            <td><div class="label"><form:label path="accountId"><app:message code="admin.account.label.accountId"/><span class="colon">:</span></form:label></div></td>
            <td colspan="3"><div class="labelValue"><c:out value="${account.accountId}" default="0"/></div><form:hidden path="accountId" value="${account.accountId}"/></td>
        </tr>


        <tr>

            <td><div class="label"><form:label  path="accountName"><app:message code="admin.account.label.accountName"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td colspan="2"><form:input tabindex="1" path="accountName" cssClass="inputLong"/></td>

            <td  rowspan="4" style="vertical-align: top;">
                <table>
                    <tbody>
                    <tr>
                        <td>
                            <div class="box">
                                <div class="boxTop">
                                    <div class="topWrapper"><span class="left">&nbsp;</span>
                                        <span class="center" style=""><span class="boxName"><app:message code="admin.account.label.budgetType"/><span class="reqind">*</span></span></span>
                                        <span class="right">&nbsp;</span>
                                    </div>
                                </div>
                                <div class="boxMiddle">
                                    <div class="middleWrapper">
                                        <span class="left">&nbsp;</span>
                                        <div class="boxContent">
                                            <table cellpadding="0" cellspacing="0" border="0">
                                                <tbody>
                                                <c:forEach var="budgetAccrualType" items="${requestScope['appResource'].dbConstantsResource.budgetAccrualTypes}">
                                                    <tr>
                                                        <td><form:radiobutton tabindex="4" class="radio" path="accountBudgetType" value="${budgetAccrualType.object2}"/></td>
                                                        <td class="label"><app:message code="refcodes.BUDGET_ACCRUAL_TYPE_CD.${budgetAccrualType.object1}" text="${budgetAccrualType.object2}"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <span class="right">&nbsp;</span></div>
                                </div>
                                <div class="boxBottom">
                                    <div class="bottomWrapper"><span class="left">&nbsp;</span>
                                        <span class="center">&nbsp;</span>
                                        <span class="right">&nbsp;</span>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td><div class="label"><form:label path="accountType"><app:message code="admin.account.label.accountType"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td colspan="2">
                <form:select tabindex="2" path="accountType" cssClass="selectLong"><form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.accountTypes}">
                        <form:option value="${type.object2}"><app:message code="refcodes.ACCOUNT_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>

        </tr>

        <tr>
            <td><div class="label"><form:label path="accountStatus"><app:message code="admin.account.label.status"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td colspan="2">
                <form:select tabindex="3" path="accountStatus" cssClass="selectLong"><form:option value=""><app:message code="admin.global.select"/></form:option>

                    <c:forEach var="code" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}">
                        <form:option value="${code.object2}"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${code.object1}" text="${code.object2}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>

        </tr>

        <tr>
            <td colspan="3" style="height:15px"></td>
        </tr>




        <tr>

            <td></td>
            <td width="190px"></td>
            <td ><div class="label"><form:label path="timeZone"><app:message code="admin.account.label.timeZone"/><span class="colon">:</span></form:label></div></td>
            <td>
                <form:select tabindex="5" path="timeZone" cssClass="selectMid"><form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.timeZones}">
                        <form:option value="${type.object2}"><app:message code="refcodes.TIME_ZONE_CD.${type.object1}" text="${type.object2}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>

        </tr>

        <tr>

            <td></td>
            <td></td>
            <td><div class="label"><form:label path="distributorReferenceNumber"><app:message code="admin.account.label.distrReferenceNumber"/><span class="colon">:</span></form:label></div></td>
            <td><form:input tabindex="6" path="distributorReferenceNumber"  cssClass="inputMid"/></td>
        </tr>
        </tbody>
    </table>
</td></tr>

<tr><td>
<hr>
    <table>

        <tbody>
        <tr><td colspan="5"><div class="subHeader"><app:message code="admin.account.subheader.billingAddress"/></div></td></tr>
        <tr>
            <td><div class="label"><form:label path="billingAddress.address1"><app:message code="admin.account.label.billingAddress.address.address1"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td><form:input tabindex="7"  path="billingAddress.address1"  cssClass="inputMid"/></td>
            <td></td>
            <td width="80px"><div class="label"><form:label path="billingAddress.city"><app:message code="admin.account.label.billingAddress.address.city"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td><form:input tabindex="10"  path="billingAddress.city"  cssClass="inputMid"/></td>
        </tr>
        <tr>
            <td><div class="label"><form:label path="billingAddress.address2"><app:message code="admin.account.label.billingAddress.address.address2"/><span class="colon">:</span></form:label></div></td>
            <td><form:input  tabindex="8"  path="billingAddress.address2"  cssClass="inputMid"/></td>
            <td></td>
            <td><div class="label"><form:label path="billingAddress.stateProvinceCd"><app:message code="admin.account.label.billingAddress.address.state"/><span class="colon">:</span></form:label><span id="billingReqindStateSign" class="reqind hide">*</span></div></td>
            <td style="white-space: nowrap;"><form:input  tabindex="11" path="billingAddress.stateProvinceCd" style="width:55px"/>
                               <span class="postalCodeAroundState">
                                   <form:label path="billingAddress.postalCode">
                                       <app:message code="admin.account.label.billingAddress.address.postalCode"/>
                                       <span class="colon">:</span>
                                   </form:label><span class="reqind">*</span>
                                   <span class="labelValue"><form:input tabindex="11" path="billingAddress.postalCode" style="width:84px"/> </span>
                               </span>
            </td>
        </tr>
        <tr>
            <td><div class="label"><form:label path="billingAddress.address3"><app:message code="admin.account.label.billingAddress.address.address3"/><span class="colon">:</span></form:label></div></td>
            <td><form:input tabindex="9"  path="billingAddress.address3"  cssClass="inputMid"/></td>
            <td></td>
            <td><div class="label"><form:label path="billingAddress.countryCd"><app:message code="admin.account.label.billingAddress.address.country"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td> <form:select tabindex="12" onchange="countryUsesState(this.value,'#billingReqindStateSign', jscountries)"  path="billingAddress.countryCd"  cssClass="selectMid">
                <form:option value=""><app:message code="admin.global.select"/></form:option>
                <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">
                    <form:option value="${country.shortDesc}">${country.uiName}</form:option>
                </c:forEach>
            </form:select></td>
        </tr>
        </tbody>
    </table>
</td></tr>

<tr><td>
<hr>
<table>

    <tbody>

    <tr><td colspan="5"><div class="subHeader"><app:message code="admin.account.subheader.primaryContact"/></div></td></tr>
    <tr>
        <td><div class="label"><form:label path="primaryContact.firstName"><app:message code="admin.account.label.primaryContact.firstName"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="13" path="primaryContact.firstName"  cssClass="inputMid"/></td>
        <td></td>
        <td width="80px"><div class="label"><form:label path="primaryContact.telephone"><app:message code="admin.account.label.primaryContact.telephone"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="15"  path="primaryContact.telephone"  cssClass="inputMid"/></td>
    </tr>

    <tr>
        <td><div class="label"><form:label path="primaryContact.lastName"><app:message code="admin.account.label.primaryContact.lastName"/><span class="colon">:</span></form:label></div></td>
        <td><form:input  tabindex="14" path="primaryContact.lastName"  cssClass="inputMid"/></td>
        <td></td>
        <td><div class="label"><form:label path="primaryContact.mobile"><app:message code="admin.account.label.primaryContact.mobile"/><span class="colon">:</span></form:label></div></td>
        <td><form:input  tabindex="16" path="primaryContact.mobile"  cssClass="inputMid"/></td>
    </tr>

    <tr>
        <td></td>
        <td  style="vertical-align:bottom;height:50px"><button tabindex="19" onclick="checkAndCopyValues(billlinAdressFields, primaryAdressFields); return false;"><app:message code="admin.global.button.copyBillingAddress"/></button></td>
        <td></td>
        <td><div class="label"><form:label path="primaryContact.fax"><app:message code="admin.account.label.primaryContact.fax"/><span class="colon">:</span></form:label></div></td>
        <td><form:input  tabindex="17" path="primaryContact.fax"  cssClass="inputMid"/></td>
    </tr>

    <tr>
        <td><div class="label"><form:label path="primaryContact.address.address1"><app:message code="admin.account.label.primaryContact.address.address1"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="20" path="primaryContact.address.address1"  cssClass="inputMid"/></td>
        <td></td>
        <td><div class="label"><form:label path="primaryContact.email"><app:message code="admin.account.label.primaryContact.email"/><span class="colon">:</span></form:label></div></td>
        <td><form:input  tabindex="18" path="primaryContact.email" cssClass="inputMid"/></td>
    </tr>
    <tr>
        <td><div class="label"><form:label path="primaryContact.address.address2"><app:message code="admin.account.label.primaryContact.address.address2"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="21" path="primaryContact.address.address2" cssClass="inputMid"/></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td><div class="label"><form:label path="primaryContact.address.address3"><app:message code="admin.account.label.primaryContact.address.address3"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="22" path="primaryContact.address.address3" cssClass="inputMid"/></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td><div class="label"><form:label path="primaryContact.address.city"><app:message code="admin.account.label.primaryContact.address.city"/><span class="colon">:</span></form:label></div></td>
        <td><form:input tabindex="23"  path="primaryContact.address.city" cssClass="inputMid"/></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td><div class="label"><form:label path="primaryContact.address.stateProvinceCd"><app:message code="admin.account.label.primaryContact.address.state"/><span class="colon">:</span></form:label></div></td>
        <td style="white-space: nowrap;"><form:input tabindex="24" path="primaryContact.address.stateProvinceCd" style="width:55px"/>
                               <span class="postalCodeAroundState">
                                   <form:label path="primaryContact.address.postalCode">
                                       <app:message code="admin.account.label.primaryContact.address.postalCode"/>
                                       <span class="colon">:</span>
                                   </form:label><span>&nbsp;</span>
                                   <span class="labelValue"><form:input path="primaryContact.address.postalCode" tabindex="25" style="width:86px"/></span>
                               </span>
        </td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>
            <div class="label">
                <form:label path="primaryContact.address.countryCd">
                    <app:message code="admin.account.label.primaryContact.address.country"/>
                    <span class="colon">:</span>
                </form:label>
            </div>
        </td>
        <td>
            <form:select tabindex="26" path="primaryContact.address.countryCd" cssClass="selectMid">
                <form:option value=""><app:message code="admin.global.select"/></form:option>
                <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">
                    <form:option value="${country.shortDesc}">${country.uiName}</form:option>
                </c:forEach>
            </form:select>
        </td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    </tbody>

</table></td></tr>
 <tr><td  align="center">
     <table >
         <tr>
             <td><form:button tabindex="27" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button></td>
             <c:if test="${ account.accountId!=null && account.accountId>0}">
                 <td style="padding-left:30px;">
                     <form:button  tabindex="30" onclick="${cloneAction} return false;"><app:message code="admin.global.button.clone"/></form:button>
                 </td>
             </c:if>
         </tr>
     </table>

 </td></tr>
</table>
</form:form>

</div>
</div>


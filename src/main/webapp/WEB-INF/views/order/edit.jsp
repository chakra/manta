<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.util.OrderUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<app:url var="sortUrl" value="/order/${order.orderId}/sortby"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/order/${order.orderId}/update');$('form:first').submit(); return false; "/>
<c:set var="cancelAction" value="$('form:first').attr('action','${baseUrl}/order/${order.orderId}/cancelOrder');$('form:first').submit(); return false; "/>
<c:set var="addItemAction" value="$('form:first').attr('method','GET'); $('form:first').attr('action','${baseUrl}/order/${orderId > 0 ? orderId: 0}/addItem');$('form:first').submit(); return false; "/>
<c:set var="printOrderDetailsAction" value="$('form:first').attr('action','${baseUrl}/order/${order.orderId}/printOrderDetails');$('form:first').submit(); return false; "/>
<c:set var="finallyHandlerLocation" value="function(value) {f_setFocus('locationFilterInputNames');}"/>
<c:set var="clearFilteredLocations" value="$('form:first').attr('action','${baseUrl}/order/${order.orderId}/clear/location');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />

<app:message var="dateFormat" code="format.prompt.dateFormat" />

<app:dateIncludes/>

<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState($('#countryCd').attr("value"),'#state', jscountries, true, 'invisible')});

function calcTotal2() {
  var calcTotal = 0.0;
  
  if ($("#subTotal").length > 0 && $("#subTotal").val() != "") {
    calcTotal += $("#subTotal").val() * 1.0;
  }
  if ($("#totalFreightCost").length > 0 && $("#totalFreightCost").val() != "") {
    calcTotal += $("#totalFreightCost").val() * 1.0;
  }
  if ($("#totalTaxCost").length > 0 && $("#totalTaxCost").val() != "") {   
    calcTotal += $("#totalTaxCost").val() * 1.0;
  }
  if ($("#totalMiscCost").length > 0 && $("#totalMiscCost").val() != "") {
    calcTotal += $("#totalMiscCost").val() * 1.0;
  }
  if ($("#rushOrderCharge").length > 0 && $("#rushOrderCharge").val() != "") {
    calcTotal += $("#rushOrderCharge").val() * 1.0;
  }
  if ($("#fuelSurCharge").length > 0 && $("#fuelSurCharge").val() != "") {
    calcTotal += $("#fuelSurCharge").val() * 1.0;
  }
  if ($("#smallOrderFee").length > 0 && $("#smallOrderFee").val() != "") {
    calcTotal += $("#smallOrderFee").val() * 1.0;
  }
  if ($("#discount").length > 0 && $("#discount").val() != "") {
    var rawDiscount = $("#discount").val() * 1.0;
    if (rawDiscount > 0) {
        calcTotal = calcTotal - rawDiscount;
    } else {
        calcTotal = calcTotal + rawDiscount;
    }
  }

  if (0.0 != calcTotal)  {
    $("#totalAmount").val(calcTotal);
  } else {
    $("#totalAmount").val(0.00);
  }

  return true;
}

</script>

<c:set var="fullControl" value="true" />
<c:set var="erpInactive" value="false" />
<c:set var="adminFlag" value="true" />
<c:set var="erpActiveAllowChange" value="false" />
<c:set var="nonEdiOrder" value="false" />
<c:set var="ediAllowChange" value="false" />

<c:set var="consolidatedOrderType" value="<%=RefCodeNames.ORDER_TYPE_CD.CONSOLIDATED%>" />
<c:set var="isConsolidatedOrder" value="${order.orderInfo.orderData.orderTypeCd == consolidatedOrderType}" />
<c:set var="accountId" value="${order.orderInfo.orderExtras.accountId}" />
<c:set var="siteId" value="${order.orderInfo.orderExtras.locationId}" />

<c:set var="pendingOrderReview" value="<%=RefCodeNames.ORDER_STATUS_CD.PENDING_ORDER_REVIEW%>" />
<c:set var="pendingReview" value="<%=RefCodeNames.ORDER_STATUS_CD.PENDING_REVIEW%>" />
<c:set var="pendingApproval" value="<%=RefCodeNames.ORDER_STATUS_CD.PENDING_APPROVAL%>" />
<c:set var="pendingConsolidation" value="<%=RefCodeNames.ORDER_STATUS_CD.PENDING_CONSOLIDATION%>" />
<c:set var="pendingDate" value="<%=RefCodeNames.ORDER_STATUS_CD.PENDING_DATE%>" />
<c:set var="erpRejected" value="<%=RefCodeNames.ORDER_STATUS_CD.ERP_REJECTED%>" />
<c:set var="cancelled" value="<%=RefCodeNames.ORDER_STATUS_CD.CANCELLED%>" />
<c:set var="duplicated" value="<%=RefCodeNames.ORDER_STATUS_CD.DUPLICATED%>" />
<c:set var="invoiced" value="<%=RefCodeNames.ORDER_STATUS_CD.INVOICED%>" />
<c:set var="erpReleasedPoError" value="<%=RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED_PO_ERROR%>" />
<c:set var="erpCancelled" value="<%=RefCodeNames.ORDER_STATUS_CD.ERP_CANCELLED%>" />
<c:set var="erpReleased" value="<%=RefCodeNames.ORDER_STATUS_CD.ERP_RELEASED%>" />
<c:set var="processErpPo" value="<%=RefCodeNames.ORDER_STATUS_CD.PROCESS_ERP_PO%>" />

<c:set var="edi850" value="<%=RefCodeNames.ORDER_SOURCE_CD.EDI_850%>" />
<c:set var="storeTypeMLA" value="<%=RefCodeNames.STORE_TYPE_CD.MLA%>" />

<c:set var="orderStatus" value="${order.orderInfo.orderData.orderStatusCd}" />
<c:set var="orderSource" value="${order.orderInfo.orderData.orderSourceCd}" />
<c:set var="clwSwitch" value="true" />


<c:if test="${orderStatus == pendingDate ||
              orderStatus == pendingConsolidation ||
              orderStatus == pendingApproval ||
              orderStatus == pendingOrderReview ||
              erpRejected}">
    <c:set var="erpInactive" value="true" />
</c:if>
<c:if test="${fullControl &&
              (orderStatus == cancelled ||
               orderStatus == duplicated ||
               orderStatus == pendingReview ||
               orderStatus == invoiced ||
               orderStatus == erpReleasedPoError ||
               orderStatus == erpCancelled ||
               orderStatus == erpRejected
              )}">
    <c:set var="erpInactive" value="true" />
</c:if>

<c:if test="${orderStatus == erpReleased || orderStatus == processErpPo}">
    <c:set var="erpActiveAllowChange" value="true" />
</c:if>

<c:if test="${orderSource != edi850}">
    <c:set var="nonEdiOrder" value="true" />
</c:if>

<c:set var="ediAllowChange" value="${order.orderInfo.allowModification}" />

<c:set var="allowWhenNoErp" value="${adminFlag && erpInactive}" />
<c:set var="allowWhenNoErpNoEdi" value="${adminFlag && erpInactive && (nonEdiOrder || fullControl)}" />
<c:set var="allowWhenErpEdi" value="${adminFlag && ((erpActiveAllowChange && ediAllowChange) || fullControl)}" />

<c:set var="allowEditAmounts" value="false" />
<c:if test="${allowWhenNoErp || allowWhenErpEdi}">
    <c:set var="allowEditAmounts" value="true" />
</c:if>
<c:set var="taxReadOnly" value="true" />

<app:locateLayer var="siteLayer"
    titleLabel="admin.global.filter.label.locateSite.title"
    closeLabel='admin.global.button.close'
    layer='${baseUrl}/locate/site/single'
    action="${baseUrl}/locate/site/selected?filter=order.setFilteredLocations"
    idGetter="siteId"
    nameGetter="siteName"
    targetNames="locationFilterInputNames"
    targetIds="locationFilterInputIds"
    finallyHandler="${finallyHandlerLocation}"/>

<div class="canvas">
<div class="details">

<form:form modelAttribute="order" id="orderFormId" name="orderForm" action="" method="POST" focus="${focusField}">
<table width="100%">

<tr>
    <td>
        <table width="100%">
            <tr>
                <td style="text-align: right;white-space: nowrap;">
                    <c:if test="${not empty order.orderInfo.orderData.accountId && order.orderInfo.orderData.accountId > 0}" >
                        <form:button tabindex="1000" onclick="${updateAction} return false;"><app:message code="admin.order.label.updateOrder"/></form:button>
                    </c:if>
                </td>
            </tr>
        </table>
    </td>
</tr>

<tr>
    <td>
    <div class="row">
    <div class="cell">
        <!--- ########################################## --->
        <!--- #############   COMMON INFO        ############### --->
        <!--- ######################################### --->
        <table width="80%">
            <tr>
                <td colspan="3">
                    <div class="subHeader">
                        <c:choose>
                            <c:when test="${isConsolidatedOrder}">
                                <app:message code="admin.order.label.consolidatedOrder"/>
                            </c:when>
                            <c:otherwise>
                                <app:message code="admin.order.label.orderHeaderInformation"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </td>
            </tr>
            <tr>
                <td width="40%" style="vertical-align: top;">
                    <table>
                        <tr>
                            <td><div class="subHeader"><form:label path="orderInfo.orderData.orderNum" style="subHeader"><app:message code="admin.order.label.webOrderNumSingle"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.orderNum}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="cell label"><form:label path="orderInfo.orderData.requestPoNum"><app:message code="admin.order.label.customerPoNum"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.requestPoNum}"/></div></td>
                        </tr>
                        <c:if test="${order.orderInfo.orderCreditCard != null}">
                            <tr>
                                <td><div class="cell label"><form:label path="orderInfo.orderCreditCard"><app:message code="admin.order.label.customerUsedCreditCard"/><span class="colon">:</span></form:label></div></td>
                                <td>&nbsp;</td>
                            </tr>
                        </c:if>
                        <c:if test="${clwSwitch}">
                            <tr>
                                <td><div class="cell label"><form:label path="orderInfo.orderData.erpOrderNum"><app:message code="admin.order.label.erpOrderNum"/><span class="colon">:</span></form:label></div></td>
                                <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.erpOrderNum}"/></div></td>
                            </tr>
                        </c:if>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.refOrderNum"><app:message code="admin.order.label.customerRequistionNum"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.refOrderNum}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.orderSourceCd"><app:message code="admin.order.label.orderMethod"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.orderSourceCd}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.orderContactName"><app:message code="admin.order.label.contactName"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.orderContactName}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.orderContactPhoneNum"><app:message code="admin.order.label.contactPhoneNum"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.orderContactPhoneNum}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.orderContactEmail"><app:message code="admin.order.label.contactEmail"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.orderContactEmail}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderedDate"><app:message code="admin.order.label.dateOrdered"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderedDate}"/></div></td>
                        </tr>
                        <tr>
                            <td class="label"><label><app:message code="admin.order.label.newOrderDate"/><span class="colon">:</span></label></td>
                            <td class="label value"><form:input tabindex="1" path="newOrderDate" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.contractId"><app:message code="admin.order.label.contractID"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.contractId}"/></div></td>
                        </tr>
                        <c:if test="${order.orderInfo.orderData.orderStatusCd != null}">
                            <c:if test="${order.orderInfo.orderData.orderStatusCd == pendingReview || order.orderInfo.orderData.orderStatusCd == pendingOrderReview}">
                                <tr>
                                    <td><div class="label"><form:label path="locationFilter"><app:message code="admin.order.label.locationID" /><span class="colon">:</span></form:label></div></td>
                                    <td>
                                        <div class="labelValue">
                                            <textarea id="locationFilterInputNames"
                                                      focusable="true"
                                                      tabindex="3"
                                                      readonly="true"
                                                      class="readonly"
                                                      style="background-color: #E8E8E8;">${order.filteredLocationCommaNames}
                                            </textarea>
                                            <br>
                                            <button onclick="${siteLayer}"><app:message code="admin.global.filter.text.searchSite"/></button><br>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:if>
                    </table>
                </td>
                <td width="40%" style="vertical-align: top;">
                    <table>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderExtras.accountName"><app:message code="admin.order.label.accountName"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderExtras.accountName}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderExtras.locationName"><app:message code="admin.order.label.locationName"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderExtras.locationName}"/></div></td>
                        </tr>
                        <tr>
                            <td style="vertical-align: top;"><div class="label"><form:label path="locationAddress"><app:message code="admin.order.label.shippingAddress"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="labelValue">
                                    <c:if test="${not empty order.orderInfo.orderExtras.locationAddress1}">
                                        <c:out value="${order.orderInfo.orderExtras.locationAddress1}"/>
                                        <br>
                                    </c:if>
                                    <c:if test="${not empty order.orderInfo.orderExtras.locationAddress2}">
                                        <c:out value="${order.orderInfo.orderExtras.locationAddress2}"/>
                                        <br>
                                    </c:if>
                                    <c:if test="${not empty order.orderInfo.orderExtras.locationAddress3}">
                                        <c:out value="${order.orderInfo.orderExtras.locationAddress3}"/>
                                        <br>
                                    </c:if>
                                    <c:if test="${not empty order.orderInfo.orderExtras.locationAddress4}">
                                        <c:out value="${order.orderInfo.orderExtras.locationAddress4}"/>
                                        <br>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderExtras.locationCity"><app:message code="admin.order.label.city"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderExtras.locationCity}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderExtras.locationState"><app:message code="admin.order.label.state"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderExtras.locationState}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderExtras.locationZip"><app:message code="admin.order.label.postalCode"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderExtras.locationZip}"/></div></td>
                        </tr>
                    </table>
                </td>
                <td width="20%" style="vertical-align: top;">
                    <table>
                        <tr>
                            <td><div class="label"><form:label path="orderPlacedBy"><app:message code="admin.order.label.placedBy"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderPlacedBy}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="subTotal"><app:message code="admin.order.label.subTotal"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="labelValue">
                                    <c:out value="${order.subTotal}"/>
                                    <form:hidden id="subTotal" path="subTotal"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalFreightCost"><app:message code="admin.order.label.freight"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <c:choose>
                                        <c:when test="${allowEditAmounts}">
                                            <form:input path="totalFreightCost" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${order.totalFreightCost}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalMiscCost"><app:message code="admin.order.label.handling"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <c:choose>
                                        <c:when test="${allowEditAmounts}">
                                            <form:input path="totalMiscCost" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${order.totalMiscCost}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${not empty rushOrderCharge}">
                            <tr>
                                <td><div class="label"><form:label path="rushOrderCharge"><app:message code="admin.order.label.rushOrderCharge"/><span class="colon">:</span></form:label></div></td>
                                <td>
                                    <div class="value">
                                        <c:choose>
                                            <c:when test="${allowEditAmounts}">
                                                <form:input path="rushOrderCharge" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${order.rushOrderCharge}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        
                        <tr>
                            <td><div class="label"><form:label path="smallOrderFee"><app:message code="admin.order.label.smallOrderFee"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <c:choose>
                                        <c:when test="${allowEditAmounts}">
                                            <form:input path="smallOrderFee" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${order.smallOrderFee}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="fuelSurCharge"><app:message code="admin.order.label.fuelSurcharge"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <c:choose>
                                        <c:when test="${allowEditAmounts}">
                                            <form:input path="fuelSurCharge" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${order.fuelSurCharge}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="discount"><app:message code="admin.order.label.discount"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <c:choose>
                                        <c:when test="${allowEditAmounts}">
                                            <form:input path="discount" cssClass="std numberCode" style="width:80px" onChange="return calcTotal2();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${order.discount}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalTaxCost"><app:message code="admin.order.label.tax"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="labelValue">
                                    <c:out value="${order.totalTaxCost}"/>
                                    <form:hidden id="totalTaxCost" path="totalTaxCost"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalAmount"><app:message code="admin.order.label.total"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><form:input path="totalAmount" cssClass="std numberCode" style="width:80px; background-color: #E8E8E8;" readonly="true"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderStatus"><app:message code="admin.order.label.orderStatus"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <form:select path="orderStatus" focusable="true">
                                        <form:option value=""><app:message code="admin.global.select"/></form:option>
                                        <c:forEach var="source" items="${order.orderStatuses}">
                                            <form:option value="${source.object1}"><app:message code="refcodes.ORDER_STATUS_CD.${source.object1}" text="${source.object2}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="workflowStatus"><app:message code="admin.order.label.workflowInd"/><span class="colon">:</span></form:label></div></td>
                            <td>
                                <div class="value">
                                    <form:select path="workflowStatus" focusable="true">
                                        <c:forEach var="source" items="${order.workflowStatuses}">
                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="applyBudget"><app:message code="admin.order.label.applyBudget"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><form:checkbox cssClass="checkbox" path="applyBudget"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="rebillOrder"><app:message code="admin.order.label.rebillOrder"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><form:checkbox cssClass="checkbox" path="rebillOrder"/></div></td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td align="left" style="vertical-align: top;">
                    <c:if test="${not empty order.orderInfo.orderMeta}">
                    <table>
                        <tr>
                            <td colspan="2">
                                <div class="subHeader">495</div>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="left">
                                <table>
                                <c:forEach var="orderMeta" items="${order.orderInfo.orderMeta}">
                                <tr>
                                    <td>
                                        <span class="label"><c:out value="${orderMeta.name}"/></span>
                                    </td>
                                    <td>
                                        <c:out value="${orderMeta.value}"/>
                                    </td>
                                </tr>
                                </c:forEach>

                                <c:if test="${not empty order.orderInfo.referenceNums}">
                                <br>
                                <br>
                                    <c:forEach var="referenceNum" items="${order.orderInfo.referenceNums}">
                                    <tr>
                                        <td>
                                            <span class="label">
                                                <c:out value="${referenceNum.orderPropertyTypeCd}"/>.
                                                <c:out value="${referenceNum.shortDesc}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <c:out value="${referenceNum.value}"/>
                                        </td>
                                    </tr>
                                    </c:forEach>
                                </c:if>
                                </table>
                            </td>
                        </tr>
                    </table>
                    </c:if>
                </td>

                <td colspan="2" align="left" style="vertical-align: top;">
                    <c:if test="${order.orderInfo.customerShipTo != null}">
                    <table>
                        <tr>
                            <td colspan="2">
                                <div class="subHeader">500</div>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="left">
                                <table>
                                    <tr>
                                        <td><div class="label"><form:label path="orderInfo.customerShipTo.shortDesc"><app:message code="admin.order.label.customerShippingName"/><span class="colon">:</span></form:label></div></td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.shortDesc}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td><div class="label"><form:label path="orderInfo.customerShipTo.address1"><app:message code="admin.order.label.customerShippingAddress"/><span class="colon">:</span></form:label></div></td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.address1}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.address2}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.address2}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.address3}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td><div class="label"><form:label path="orderInfo.customerShipTo.city"><app:message code="admin.order.label.city"/><span class="colon">:</span></form:label></div></td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.city}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td><div class="label"><form:label path="orderInfo.customerShipTo.stateProvinceCd"><app:message code="admin.order.label.state"/><span class="colon">:</span></form:label></div></td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.stateProvinceCd}"/></div></td>
                                    </tr>
                                    <tr>
                                        <td><div class="label"><form:label path="orderInfo.customerShipTo.postalCode"><app:message code="admin.order.label.postalCode"/><span class="colon">:</span></form:label></div></td>
                                        <td><div class="labelValue"><c:out value="${order.orderInfo.customerShipTo.postalCode}"/></div></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    </c:if>
                </td>
            </tr>
        </table>
        
        <table width="100%">
            <c:if test="${not empty order.orderInfo.orderData.comments}">
                <tr><td><hr/></td></tr>
                <tr>
                    <td><div class="label"><form:label path="orderInfo.orderData.comments"><app:message code="admin.order.label.customerComments"/><span class="colon">:</span></form:label></div></td>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="3"><div class="value"><c:out value="${order.orderInfo.orderData.comments}"/></div></td>
                </tr>
            </c:if>

            <c:if test="${order.orderNote != null}">
                <tr><td><hr/></td></tr>
                <tr>
                    <td><div class="label"><form:label path="orderNote.value" htmlEscape="true"><app:message code="admin.order.label.notes"/><span class="colon">:</span></form:label></div></td>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td width="60%" colspan="2">
                        <div class="value">
                            <c:out value="${order.orderNote.value}" escapeXml="true"/>
                            <br>
                            (<app:message code="admin.global.text.addedBy"/>&nbsp;
                             <c:out value="${order.orderNote.addBy}"/>&nbsp;
                             <app:message code="admin.global.text.on"/>&nbsp;
                            <app:formatDate value="${order.orderNote.addDate}"/>)
                        </div>
                    </td>
                    <td>&nbsp;</td>
                </tr>
            </c:if>
            <tr>
                <td align="center">
                    <app:locateLayer var='orderNoteLayerReference'
                            layerName="orderItemNoteLayer"
                            titleLabel='admin.order.label.viewOrderNotes'
                            closeLabel='admin.global.button.close'
                            layer='${baseUrl}/order/${order.orderId}/viewOrderNote?view=true'/>   
                    <button id="viewAllOrderNotesId" onclick="${orderNoteLayerReference}"><app:message code="admin.order.label.viewAllOrderDetailNotes"/></button>
                    
                    <app:locateLayer var='addOrderNoteLayerReference'
                            layerName="orderItemNoteLayer"
                            titleLabel='admin.order.label.addOrderNote'
                            closeLabel='admin.global.button.close'
                            layer='${baseUrl}/order/${order.orderId}/addOrderNote?view=false'
                            action="${baseUrl}/order/${order.orderId}/saveOrderNote"/>
                    <button id="addOrderNoteId" onclick="${addOrderNoteLayerReference}"><app:message code="admin.order.label.addOrderDetailNote"/></button>

                    <form:button id="printOrderDetailsId" cssClass="button" onclick="${printOrderDetailsAction} return false;"><app:message code="admin.order.label.printOrderDetails"/></form:button>
                    
                    <c:if test="${order.orderInfo.orderData.orderStatusCd != null}">
                        <c:if test="${order.orderInfo.orderData.orderStatusCd == pendingReview ||
                                      order.orderInfo.orderData.orderStatusCd == pendingOrderReview ||
                                      order.orderInfo.orderData.orderStatusCd == pendingApproval ||
                                      order.orderInfo.orderData.orderStatusCd == pendingOrderReview ||
                                      order.orderInfo.orderData.orderStatusCd == pendingConsolidation ||
                                      order.orderInfo.orderData.orderStatusCd == pendingDate ||
                                      fullControl}">
                            <app:locateLayer var='printTempPoReference'
                                    layerName="printTempPoLayer"
                                    titleLabel='admin.order.label.selectDistributor'
                                    closeLabel='admin.global.button.close'
                                    layer='${baseUrl}/order/${order.orderId}/prepareTempPo'
                                    action="${baseUrl}/order/${order.orderId}/printTempPo"/>

                                <button id="printTempPoId" onclick="${printTempPoReference}"><app:message code="admin.order.label.printTemporaryPO"/></button>
                        </c:if>
                    </c:if>
                </td>
            </tr>
            <tr><td><hr/></td></tr>
            <tr>
                <td>
                    <div class="subHeader">
                        <app:message code="admin.order.label.orderItemStatus"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <table class="searchResult" width="60%">
                        <thead class="header">
                            <tr class="row">
                                <th class="label"><app:message code="admin.order.label.cumulativeSummary"/><span class="colon">:</span>&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.date" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.ordered" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.accepted" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.shipped" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.backordered" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.substituted" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.invoiced" />&nbsp;</th>
                                <th class="cell cell-text"><app:message code="admin.order.label.returned" />&nbsp;</th>
                            </tr>
                        </thead>
                        
                        <tbody class="body">
                            <tr class="row" align="center">
                                <td>&nbsp;</td>
                                <td class="cell-element">
                                    <c:if test="${order.orderNote != null}">
                                        <c:if test="${order.orderNote.addDate != null && order.orderNote.addBy != null}">
                                            (<app:message code="admin.global.text.addedBy"/>&nbsp;
                                             <c:out value="${order.orderNote.addBy}"/>&nbsp;
                                             <app:message code="admin.global.text.on"/>&nbsp;
                                            <app:formatDate value="${order.orderNote.addDate}"/>)
                                        </c:if>
                                    </c:if>
                                </td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.ordered}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.accepted}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.shipped}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.backordered}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.substituted}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.invoiced}"/></td>
                                <td class="cell cell-element"><c:out value="${order.cumulativeSummary.returned}"/></td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>

            <c:choose>
                <c:when test="${!order.simpleServiceOrder}">
                    <c:if test="${order.orderItems != null}">
                        <tr><td><hr/></td></tr>
                        <tr>
                            <td>
                                <table class="searchResult" width="100%">
                                    <thead class="header">
                                        <tr class="row">
                                            <th class="cell cell-text"><app:message code="admin.order.label.lineNum" /></th>
                                            <c:if test="${clwSwitch}">
                                                <th class="cell cell-text"><a class="sort" href="${sortUrl}/outboundPoNum"><app:message code="admin.order.label.outboundPONum" /></a></th>
                                            </c:if>
                                            <c:choose>
                                                <c:when test="${clwSwitch}">
                                                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemSkuNum"><app:message code="admin.order.label.cwSKUNum" /></a></th>
                                                </c:when>
                                                <c:otherwise>
                                                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemSkuNum"><app:message code="admin.order.label.SKUNum" /></a></th>
                                                </c:otherwise>
                                            </c:choose>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/distItemSkuNum"><app:message code="admin.order.label.distSKUNum" /></a></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.distName" /></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemShortDesc"><app:message code="admin.order.label.productName" /></a></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.uom" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.pack" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.itemSize" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.customerPrice" /></th>
                                            <c:if test="${storeTypeMLA == order.storeType}">
                                                <th class="cell cell-text"><app:message code="admin.order.label.cwCost" /></th>
                                            </c:if>
                                            <th class="cell cell-text"><app:message code="admin.order.label.taxExempt" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.taxAmount" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.qty" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.status" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poStatus" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.itemNote" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.addItemNote" /><br><hr><br><app:message code="admin.order.label.resale" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poAndShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.targetShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.openLineStatus" /></th>
                                        </tr>
                                    </thead>

                                    <tbody class="body">
                                        <c:forEach var="itemInfo" varStatus="i" items="${order.orderItems}" >
                                            <c:set var="qty" value="${itemInfo.orderItem.totalQuantityOrdered}" />
                                            <c:set var="iIndex" value="${i.index}" />

                                            <tr class="row">
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
                                                <c:if test="${clwSwitch}">
                                                    <td class="cell cell-text">
                                                        <c:choose>
                                                            <c:when test="${not empty itemInfo.orderItem.outboundPoNum}">
                                                                <c:out value="${itemInfo.orderItem.outboundPoNum}"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <app:message code="admin.global.label.na"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSkuNum}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.distItemSkuNum}"/></td>
                                                <td class="cell cell-text">
                                                    <c:out value="${itemInfo.distName}"/>&nbsp;
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:if test="${not empty itemInfo.orderItem.itemShortDesc}">
                                                        <c:out value="${itemInfo.orderItem.itemShortDesc}"/>
                                                    </c:if>
                                                </td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemUom}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemPack}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSize}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.custContractPrice}"/></td>
                                                <c:if test="${storeTypeMLA == order.storeType}">
                                                    <td class="cell cell-text"><c:out value="${itemInfo.orderItem.distItemCost}"/></td>
                                                </c:if>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.taxAmount}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.totalQuantityOrdered}"/></td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${not empty itemInfo.orderItem.orderItemStatusCd}">
                                                            <app:message code="refcodes.ORDER_ITEM_STATUS_CD.${itemInfo.orderItem.orderItemStatusCd}" text="${itemInfo.orderItem.orderItemStatusCd}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <app:message code="admin.order.label.ordered"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:message code="refcodes.PURCHASE_ORDER_STATUS_CD.${itemInfo.purchaseOrder.purchaseOrderStatusCd}" text="${itemInfo.purchaseOrder.purchaseOrderStatusCd}"/>
                                                </td>
                                                <td class="cell cell-text">

                                                    <c:set var="viewDisplayProperty" value="none" />
                                                    <c:if test="${itemInfo.hasNote}">
                                                        <c:set var="viewDisplayProperty" value="block" />
                                                    </c:if>
                                                    
                                                    <%String viewItemNoteButtonId = "viewItemNote_" + pageContext.getAttribute("iIndex");%>
                                                    
                                                    <span id="viewItemNote_${i.index}" style="display: ${viewDisplayProperty};">

                                                        <app:locateLayer var='itemNoteLayerReference_${i.index}'
                                                            layerName="orderItemNoteLayer"
                                                            titleLabel='admin.order.label.viewItemNotes'
                                                            closeLabel='admin.global.button.close'
                                                            layer='${baseUrl}/order/${order.orderId}/viewItemNote?index=${i.index}&view=true'/>   

                                                        <%String callFunctionItemNote = (String) pageContext.getAttribute("itemNoteLayerReference_" + pageContext.getAttribute("iIndex")); %>
                                                        <button id="locateItemNote_${i.index}" onclick="<%=callFunctionItemNote%>"><app:message code="admin.global.button.view"/></button>

                                                    </span>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${itemInfo.orderItem.orderItemId != null && itemInfo.orderItem.orderItemId > 0}">

                                                            <%String postHandlerFunction = "function() { document.getElementById('" + viewItemNoteButtonId + "').style.display = 'block'; }"; %>

                                                            <app:locateLayer var='addItemNoteLayerReference_${i.index}'
                                                                layerName="orderItemNoteLayer"
                                                                titleLabel='admin.order.label.addItemNote'
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/order/${order.orderId}/addItemNote?view=false'
                                                                action="${baseUrl}/order/${order.orderId}/saveItemNote?index=${i.index}"
                                                                postHandler="<%=postHandlerFunction%>"/> 

                                                            <%String callFunctionAddItemNote = (String) pageContext.getAttribute("addItemNoteLayerReference_" + pageContext.getAttribute("iIndex")); %>
                                                            <button id="locateAddItemNote_${i.index}" onclick="<%=callFunctionAddItemNote%>"><app:message code="admin.global.button.add"/></button>

                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:formatDate value="${itemInfo.orderItem.erpPoDate}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:formatDate value="${itemInfo.orderItem.targetShipDate}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:out value="${itemInfo.openLineStatusCd}"/>
                                                </td>
                                            </tr>
                                            
                                            <c:if test="${allowWhenNoErpNoEdi || allowWhenErpEdi}">
                                            <!-- the input fields for each item when no integration necessary -->
                                            <!-- and the input fields for each item when erp and may be edi integration is necessary -->
                                            <tr>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <c:if test="${clwSwitch}">
                                                    <td class="cell cell-text">&nbsp;</td>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${allowWhenErpEdi}">

                                                        <td class="cell cell-text">
                                                            <c:set var="itemLayerName" value="itemLayer_${i.index}" />
                                                            <c:set var="itemLayerIndex" value="${i.index}" />

                                                            <app:locateLayer var="itemLayerReference_${i.index}"
                                                                layerName="itemLayer"
                                                                titleLabel="admin.global.filter.label.locateItem.title"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/item/single'
                                                                action="${baseUrl}/locate/item/selected?filter=order.setFilteredItem&index=${i.index}"
                                                                idGetter="itemId"
                                                                nameGetter="itemSku"
                                                                targetNames="itemFilterInputSkuS_${i.index}"
                                                                targetIds="itemFilterInputIdS_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('itemFilterInputSkuS_${i.index}');}"/>
                                                            <div class="value">
                                                                <form:input id="itemFilterInputSkuS_${i.index}" path="orderItems[${i.index}].newItemView.itemSku" style="width:80px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionItem = (String) pageContext.getAttribute("itemLayerReference_" + pageContext.getAttribute("itemLayerIndex")); %>

                                                            <button id="locateItem_${i.index}" onclick="<%=callFunctionItem%>" style="white-space: nowrap;"><app:message code="admin.order.label.locateItem"/></button><br>
                                                        </td>

                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <c:choose>
                                                    <c:when test="${allowWhenErpEdi}">

                                                        <td class="cell cell-text">
                                                            <c:set var="distLayerName" value="distLayer_${i.index}" />
                                                            <c:set var="distLayerIndex" value="${i.index}" />

                                                            <app:locateLayer var="distLayerReference_${i.index}"
                                                                layerName="distrLayer"
                                                                titleLabel="admin.global.filter.text.searchDistributors"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/distributor/single'
                                                                action="${baseUrl}/locate/distributor/selected?filter=order.setFilteredDist&index=${i.index}"
                                                                idGetter="distributorId"
                                                                nameGetter="distributorName"
                                                                targetNames="distFilterInputNameS_${i.index}"
                                                                targetIds="distFilterInputIdS_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('distFilterInputNameS_${i.index}');}"/>
                                                            <div class="value">
                                                                <form:input id="distFilterInputNameS_${i.index}" path="orderItems[${i.index}].newDistView.distributorName" style="width:90px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionDist = (String) pageContext.getAttribute("distLayerReference_" + pageContext.getAttribute("distLayerIndex")); %>

                                                            <button id="locateDist_${i.index}" onclick="<%=callFunctionDist%>" style="white-space: nowrap;"><app:message code="admin.order.label.locateDist"/></button><br>
                                                        </td>
                                                        
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <c:choose>
                                                    <c:when test="${storeTypeMLA != order.storeType}">
                                                        <td class="cell cell-text">
                                                            <div class="value"><form:input path="orderItems[${i.index}].newItemPrice" cssClass="std numberCode" style="width:70px"/></div>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:if test="${storeTypeMLA == order.storeType}">
                                                    <td class="cell cell-text">
                                                    
                                                        <div class="value"><form:input path="orderItems[${i.index}].newCwCost" cssClass="std numberCode" style="width:80px"/></div>
                                                        
                                                        <td class="cell cell-text">

                                                            <app:locateLayer var="cwCostLayerReference_${i.index}"
                                                                layerName="cwCostLayer"
                                                                titleLabel="admin.order.label.selectItemCost"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/itemCost/single?accountId=${order.orderInfo.orderData.accountId}&itemSkuNum=${itemInfo.orderItem.itemSkuNum}&distId=${itemInfo.distId}&distErpNum=${itemInfo.orderItem.distErpNum}'
                                                                action="${baseUrl}/locate/itemCost/selected?filter=order.setFilteredCwCost&index=${i.index}"
                                                                idGetter="distId"
                                                                nameGetter="itemCost"
                                                                targetNames="itemCostFilterInput_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('itemCostFilterInput_${i.index}');}"/>

                                                            <div class="value">
                                                                <form:input id="itemCostFilterInput_${i.index}" path="orderItems[${i.index}].newCwCostView.itemCost" style="width:90px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionCwCost = (String) pageContext.getAttribute("cwCostLayerReference_" + pageContext.getAttribute("iIndex")); %>

                                                            <button id="locateItemCost_${i.index}" onclick="<%=callFunctionCwCost%>" style="white-space: nowrap;"><app:message code="admin.order.label.getCost"/></button><br>
                                                        </td>
                                                    </td>
                                                </c:if>
                                                <td class="cell cell-text">
                                                    <form:checkbox cssClass="checkbox" path="orderItems[${i.index}].taxExempt"/>
                                                </td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <c:choose>
                                                    <c:when test="${!isConsolidatedOrder}">
                                                        <td class="cell cell-text">
                                                            <div class="value"><form:input path="orderItems[${i.index}].itemQuantity" cssClass="std numberCode" style="width:70px"/></div>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td class="cell cell-text">
                                                    <form:select path="orderItems[${i.index}].newItemStatus" focusable="true">
                                                        <c:forEach var="source" items="${order.orderItemStatuses}">
                                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </td>
                                                <td class="cell cell-text">
                                                    <form:select path="orderItems[${i.index}].newPoItemStatus" focusable="true">
                                                        <c:forEach var="source" items="${order.poItemStatuses}">
                                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">
                                                    <form:checkbox cssClass="checkbox" path="orderItems[${i.index}].reSale"/>
                                                </td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                            </tr>
                                            </c:if>

                                            <c:if test="${not empty itemInfo.orderItemActionDescs}">
                                                <c:set var="shippedQty" value="0" scope="page" />
                                                <c:set var="substitutedFlag" value="false" />
                                                <c:set var="shippedFlag" value="false" />
                                                <c:forEach var="itemActionDetail" varStatus="j" items="${itemInfo.orderItemActionDescs}" >
                                                    <c:choose>
                                                        <c:when test="${itemActionDetail.orderItemAction.actionCd == 'Substituted' || 
                                                                        itemActionDetail.orderItemAction.quantity == null}" >
                                                            <c:set var="substitutedFlag" value="true" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="itemActionDetail.orderItemAction.actionCd == 'Shipped'">
                                                                <c:set var="shippedQty" value="${shippedQty + itemActionDetail.orderItemAction.quantity}" scope="page"/>
                                                                <c:set var="shippedFlag" value="true" />
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <tr>
                                                        <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
                                                        <c:if test="${clwSwitch}">
                                                            <td class="cell cell-text">&nbsp;</td>
                                                        </c:if>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.affectedSku}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.distItemSkuNum}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemShortDesc}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemUom}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemPack}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>

                                                        <c:if test="${storeTypeMLA == order.storeType}">
                                                            <td class="cell cell-text"><c:out value="${itemActionDetail.itemDistCost}"/></td>
                                                        </c:if>

                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.quantity}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.actionCd}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">
                                                            <app:formatDate value="${itemActionDetail.orderItemAction.actionDate}"/>
                                                        </td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>

                                            <c:set var="backorderedQty" value="${qty - shippedQty}" />
                                            <c:if test="${shippedFlag && !substitutedFlag && (backorderedQty > 0)}">
                                                <tr>
                                                    <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
                                                    <c:if test="${clwSwitch}">
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:if>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSkuNum}"/></td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>

                                                    <c:if test="${storeTypeMLA == order.storeType}">
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </c:if>
                                                    
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text"><c:out value="${backorderedQty}"/></td>
                                                    <td class="cell cell-text"><app:message code="admin.order.label.backordered"/></td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                    <td class="cell cell-text">&nbsp;</td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:if test="${order.orderItems != null}">
                        <tr><td><hr/></td></tr>
                        <tr>
                            <td>
                                <table class="searchResult" width="100%">
                                    <thead class="header">
                                        <tr class="row">
                                            <th class="cell cell-text"><app:message code="admin.order.label.lineNum" /></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/outboundPoNum"><app:message code="admin.order.label.outboundPONum" /></a></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/AssetName"><app:message code="admin.order.label.assetName" /></a></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.distName" /></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/ServiceName"><app:message code="admin.order.label.serviceName" /></a></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.customerPrice" /></th>
                                            <c:if test="${storeTypeMLA == order.storeType}">
                                                <th class="cell cell-text"><app:message code="admin.order.label.cwCost" /></th>
                                            </c:if>
                                            <th class="cell cell-text"><app:message code="admin.order.label.taxRate" />/<app:message code="admin.order.label.taxExempt" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.taxAmount" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.qty" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.status" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poStatus" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.itemNote" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poAndShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.targetShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.openLineStatus" /></th>
                                            <c:if test="${allowWhenNoErpNoEdi || allowWhenErpEdi}">
                                                <th class="cell cell-text"><app:message code="admin.order.label.selectForCancel" /></th>
                                                <th class="cell value" nowrap>
                                                    <a href="javascript:checkAll('orderFormId', 'selectToCancel.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                                                    <a href="javascript:checkAll('orderFormId', 'selectToCancel.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                                                </th>
                                            </c:if>
                                        </tr>
                                    </thead>

                                    <tbody class="body">
                                        <c:forEach var="itemInfo" varStatus="i" items="${order.orderItems}" >
                                            <c:set var="qty" value="${itemInfo.orderItem.totalQuantityOrdered}" />
                                            <c:set var="iIndex" value="${i.index}" />

                                            <tr class="row">
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${not empty itemInfo.orderItem.erpPoNum}">
                                                            <c:out value="${itemInfo.orderItem.erpPoNum}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <app:message code="admin.global.label.na"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:if test="${not empty itemInfo.assetInfo.shortDesc}">
                                                        <c:out value="${itemInfo.assetInfo.shortDesc}"/>
                                                    </c:if>
                                                </td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.distName}"/>&nbsp;</td>
                                                <td class="cell cell-text">
                                                    <c:if test="${not empty itemInfo.orderItem.itemShortDesc}">
                                                        <c:out value="${itemInfo.orderItem.itemShortDesc}"/>
                                                    </c:if>
                                                </td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.custContractPrice}"/></td>
                                                <c:if test="${storeTypeMLA == order.storeType}">
                                                    <td class="cell cell-text"><c:out value="${itemInfo.orderItem.distItemCost}"/></td>
                                                </c:if>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.taxRate}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.taxAmount}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.totalQuantityOrdered}"/></td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${not empty itemInfo.orderItem.orderItemStatusCd}">
                                                            <app:message code="refcodes.ORDER_ITEM_STATUS_CD.${itemInfo.orderItem.orderItemStatusCd}" text="${itemInfo.orderItem.orderItemStatusCd}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <app:message code="admin.order.label.ordered"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:message code="refcodes.PURCHASE_ORDER_STATUS_CD.${itemInfo.purchaseOrder.purchaseOrderStatusCd}" text="${itemInfo.purchaseOrder.purchaseOrderStatusCd}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${itemInfo.hasNote}">
                                                            <%String viewItemNoteButtonId = "viewItemNote_" + pageContext.getAttribute("iIndex");%>

                                                                <app:locateLayer var='itemNoteLayerReference_${i.index}'
                                                                    layerName="orderItemNoteLayer"
                                                                    titleLabel='admin.order.label.viewItemNotes'
                                                                    closeLabel='admin.global.button.close'
                                                                    layer='${baseUrl}/order/${order.orderId}/viewItemNote?index=${i.index}&view=true'/>   

                                                                <%String callFunctionItemNote = (String) pageContext.getAttribute("itemNoteLayerReference_" + pageContext.getAttribute("iIndex")); %>
                                                                <button id="locateItemNote_${i.index}" onclick="<%=callFunctionItemNote%>"><app:message code="admin.global.button.view"/></button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:if test="${itemInfo.orderItem.orderItemId != null && itemInfo.orderItem.orderItemId > 0}">
                                                                <app:locateLayer var='addItemNoteLayerReference_${i.index}'
                                                                    layerName="orderItemNoteLayer"
                                                                    titleLabel='admin.order.label.addItemNote'
                                                                    closeLabel='admin.global.button.close'
                                                                    layer='${baseUrl}/order/${order.orderId}/addItemNote?view=false'
                                                                    action="${baseUrl}/order/${order.orderId}/saveItemNote?index=${i.index}"/> 

                                                                <%String callFunctionAddItemNote = (String) pageContext.getAttribute("addItemNoteLayerReference_" + pageContext.getAttribute("iIndex")); %>
                                                                <button id="locateAddItemNote_${i.index}" onclick="<%=callFunctionAddItemNote%>"><app:message code="admin.global.button.add"/></button>
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${itemInfo.orderItem.erpPoDate != null}">
                                                            <app:formatDate value="${itemInfo.orderItem.erpPoDate}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            &nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:if test="${itemInfo.deliveryDate != null}">
                                                        &nbsp;
                                                        <app:formatDate value="${itemInfo.deliveryDate}"/>
                                                    </c:if>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:formatDate value="${itemInfo.orderItem.targetShipDate}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:out value="${itemInfo.openLineStatusCd}"/>
                                                </td>
                                                <c:if test="${allowWhenNoErpNoEdi || allowWhenErpEdi}">
                                                    <c:if test="${itemInfo.orderItem.orderItemId != null && itemInfo.orderItem.orderItemId > 0}">
                                                        <td class="cell cell-text" align="center">
                                                            <form:checkbox cssClass="checkbox" path="selectToCancel.selectableObjects[${i.index}].selected"/>
                                                        </td>
                                                    </c:if>
                                                </c:if>
                                            </tr>
                                            
                                            <c:if test="${allowWhenNoErpNoEdi || allowWhenErpEdi}">
                                            <!-- the input fields for each item when no integration necessary -->
                                            <!-- and the input fields for each item when erp and may be edi integration is necessary -->
                                            <tr>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${itemInfo.assetInfo != null}">
                                                            <app:locateLayer var="assetLayerReference_${iIndex}"
                                                                layerName="assetLayer"
                                                                titleLabel="admin.global.filter.label.locateAsset.title"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/asset/single?siteId=${order.orderInfo.orderData.siteId}'
                                                                action="${baseUrl}/locate/asset/selected?filter=order.setFilteredAsset&index=${iIndex}"
                                                                idGetter="assetId"
                                                                nameGetter="assetName"
                                                                targetNames="assetFilterInputNames_${i.index}"
                                                                targetIds="assetFilterInputId_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('assetFilterInputNames_${iIndex}');}"/>

                                                            <div class="value">
                                                                <form:input id="assetFilterInputNames_${iIndex}" path="orderItems[${iIndex}].newAssetView.assetName" style="width:80px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionAsset = (String) pageContext.getAttribute("assetLayerReference_" + pageContext.getAttribute("iIndex")); %>

                                                            <button id="locateItem_${i.index}" onclick="<%=callFunctionAsset%>" style="white-space: nowrap;"><app:message code="admin.order.label.locateAsset"/></button><br>
                                                        </c:when>
                                                        <c:otherwise>&nbsp;</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${allowWhenErpEdi}">
                                                            <c:set var="distLayerName" value="distLayer_${i.index}" />
                                                            <c:set var="distLayerIndex" value="${i.index}" />

                                                            <app:locateLayer var="distLayerReference_${i.index}"
                                                                layerName="distrLayer"
                                                                titleLabel="admin.global.filter.text.searchDistributors"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/distributor/single'
                                                                action="${baseUrl}/locate/distributor/selected?filter=order.setFilteredDist&index=${i.index}"
                                                                idGetter="distributorId"
                                                                nameGetter="distributorName"
                                                                targetNames="distFilterInputNameS_${i.index}"
                                                                targetIds="distFilterInputIdS_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('distFilterInputNameS_${i.index}');}"/>
                                                            <div class="value">
                                                                <form:input id="distFilterInputNameS_${i.index}" path="orderItems[${i.index}].newDistView.distributorName" style="width:90px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionDist = (String) pageContext.getAttribute("distLayerReference_" + pageContext.getAttribute("distLayerIndex")); %>

                                                            <button id="locateDist_${i.index}" onclick="<%=callFunctionDist%>" style="white-space: nowrap;"><app:message code="admin.order.label.locateDist"/></button><br>
                                                        </c:when>
                                                        <c:otherwise>&nbsp;</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${allowWhenErpEdi}">
                                                            <app:locateLayer var="serviceLayerReference_${i.index}"
                                                                layerName="serviceLayer"
                                                                titleLabel="admin.global.filter.text.searchServices"
                                                                closeLabel='admin.global.button.close'
                                                                layer='${baseUrl}/locate/service/single?siteId=${order.orderInfo.orderData.siteId}&contractId=${order.orderInfo.orderData.contractId}&assetId=${itemInfo.assetInfo.assetId}'
                                                                action="${baseUrl}/locate/service/selected?filter=order.setFilteredService&index=${i.index}"
                                                                idGetter="serviceId"
                                                                nameGetter="serviceName"
                                                                targetNames="serviceFilterInputName_${i.index}"
                                                                targetIds="serviceFilterInputId_${i.index}"
                                                                finallyHandler="function(value) {f_setFocus('serviceFilterInputName_${i.index}');}"/>

                                                            <div class="value">
                                                                <form:input id="serviceFilterInputName_${i.index}" path="orderItems[${i.index}].newServiceView.serviceName" style="width:90px; background-color: #E8E8E8;" readonly="true"/>
                                                                <br>
                                                            </div>
                                                            <%String callFunctionService = (String) pageContext.getAttribute("serviceLayerReference_" + pageContext.getAttribute("iIndex")); %>

                                                            <button id="locateDist_${iIndex}" onclick="<%=callFunctionService%>" style="white-space: nowrap;"><app:message code="admin.order.label.locateService"/></button><br>
                                                        </c:when>
                                                        <c:otherwise>&nbsp;</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:choose>
                                                        <c:when test="${storeTypeMLA != order.storeType}">
                                                            <div class="value"><form:input path="orderItems[${i.index}].newItemPrice" cssClass="std numberCode" style="width:70px"/></div>
                                                        </c:when>
                                                        <c:otherwise>&nbsp;</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:if test="${storeTypeMLA == order.storeType}">
                                                        <div class="value"><form:input path="orderItems[${i.index}].newCwCost" cssClass="std numberCode" style="width:80px"/></div>
                                                    </c:if>
                                                </td>
                                                <td class="cell cell-text">
                                                    <form:checkbox cssClass="checkbox" path="orderItems[${i.index}].taxExempt"/>
                                                </td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">
                                                    <form:select path="orderItems[${i.index}].newItemStatus" focusable="true">
                                                        <c:forEach var="source" items="${order.orderItemStatuses}">
                                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </td>
                                                <td class="cell cell-text">
                                                    <form:select path="orderItems[${i.index}].newPoItemStatus" focusable="true">
                                                        <c:forEach var="source" items="${order.poItemStatuses}">
                                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                                <td class="cell cell-text">&nbsp;</td>
                                            </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                </c:otherwise>
            </c:choose>

            <tr>
                <td align="left">
                    <form:button cssClass="button" onclick="${addItemAction} return false;"><app:message code="admin.order.label.addItem"/></form:button>
                </td>
            </tr>
            
            <c:if test="${order.distributionSummary != null}">
                <c:if test="${fn:length(order.distributionSummary.distSummaryLines) > 0}">
                    <tr>
                        <td align="left">
                            <div class="subHeader">
                                <app:message code="admin.order.label.distributionInformation" />
                            </div>
                            <br>
                            <table class="searchResult">
                                <thead class="header">
                                    <tr class="label" style="border-spacing: 10px;">
                                        <th class="cell cell-text"><app:message code="admin.order.label.distName"/></th>
                                        <th class="cell cell-text"><app:message code="admin.order.label.lines"/></th>
                                        <th class="cell cell-text"><app:message code="admin.order.label.total"/></th>
                                        <th colspan="2">&nbsp;</th>
                                    </tr>
                                </thead>
                                <tbody class="body">
                                    <c:forEach var="distLine" items="${order.distributionSummary.distSummaryLines}">
                                        <tr class="row">
                                            <td class="cell cell-text"><c:out value="${distLine.distName}"/></td>
                                            <td class="cell cell-text"><c:out value="${distLine.distLineItemCount}"/></td>
                                            <td class="cell cell-text"><c:out value="${distLine.distTotal}"/></td>
                                            <td colspan="2" class="cell cell-text">&nbsp;</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </c:if>
            </c:if>

        </table>
    </div>
    </div>
    </td>
</tr>

<tr>
    <td>
        <table width="100%">
            <tr>
                <td style="text-align: right;white-space: nowrap;">
                    <c:if test="${order.showCancelButton}" >
                        <form:button cssClass="button" onclick="${cancelAction} return false;"><app:message code="admin.order.label.cancelOrder"/></form:button>
                        &nbsp;
                    </c:if>
                    <c:if test="${not empty order.orderInfo.orderData.accountId && order.orderInfo.orderData.accountId > 0}" >
                        <form:button tabindex="1000" onclick="${updateAction} return false;"><app:message code="admin.order.label.updateOrder"/></form:button>
                    </c:if>
                </td>
            </tr>
        </table>
    </td>
</tr>

</table>
<form:hidden id="locationFilterInputIds" path="locationFilter" value="${order.filteredLocationCommaIds}"/>
</form:form>

</div>
</div>


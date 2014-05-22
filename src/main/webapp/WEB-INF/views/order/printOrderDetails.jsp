<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.util.OrderUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<c:set var="backToEditAction" value="$('form:first').attr('action','${baseUrl}/order/${order.orderId}/backToEdit');$('form:first').submit(); return false; "/>

<app:message var="dateFormat" code="format.prompt.dateFormat" />
<app:dateIncludes/>

<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState($('#countryCd').attr("value"),'#state', jscountries, true, 'invisible')});

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
<c:set var="propertyOrderNote" value="<%=RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE%>" />
<c:set var="propertyCustomerCartComment" value="<%=RefCodeNames.ORDER_PROPERTY_TYPE_CD.CUSTOMER_CART_COMMENTS%>" />


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

<div class="canvas">
<div class="details">

<form:form modelAttribute="order" id="orderFormId" name="orderForm" action="" method="POST" focus="${focusField}">
<table width="100%">

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

                        <c:if test="${(order.orderInfo.referenceNums != null) && (fn:length(order.orderInfo.referenceNums) > 0)}">
                            <c:forEach var="refNum" varStatus="i" items="${order.orderInfo.referenceNums}" >
                                <tr>
                                    <td><div class="label"><c:out value="${refNum.orderPropertyTypeCd}" />.<c:out value="${refNum.shortDesc}" /></div></td>
                                    <td><div class="labelValue"><c:out value="${refNum.value}" /></div></td>
                                </tr>
                            </c:forEach>
                        </c:if>

                        <tr>
                            <td><div class="label"><form:label path="orderedDate"><app:message code="admin.order.label.dateOrdered"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderedDate}" /></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderInfo.orderData.contractId"><app:message code="admin.order.label.contractID"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${order.orderInfo.orderData.contractId}"/></div></td>
                        </tr>
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

                        <c:if test="${order.orderInfo.customerShipTo != null}">
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
                        </c:if>

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
                            <td><div class="labelValue"><c:out value="${order.subTotal}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalFreightCost"><app:message code="admin.order.label.freight"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><c:out value="${order.totalFreightCost}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalMiscCost"><app:message code="admin.order.label.handling"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><c:out value="${order.totalMiscCost}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="smallOrderFee"><app:message code="admin.order.label.smallOrderFee"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><c:out value="${order.smallOrderFee}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="fuelSurCharge"><app:message code="admin.order.label.fuelSurcharge"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><c:out value="${order.fuelSurCharge}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalTaxCost"><app:message code="admin.order.label.tax"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><c:out value="${order.totalTaxCost}"/></div></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="totalAmount"><app:message code="admin.order.label.total"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><form:input path="totalAmount" cssClass="std numberCode" style="width:80px; background-color: #E8E8E8;" readonly="true" disabled="true"/></div></td>
                            
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="orderStatus"><app:message code="admin.order.label.orderStatus"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="value"><nowrap><app:message code="refcodes.ORDER_STATUS_CD.${order.orderStatus}" text="${order.orderStatus}"/></nowrap></div></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <c:if test="${(order.orderInfo.orderProperties != null) && (fn:length(order.orderInfo.orderProperties) > 0)}">
            <table width="100%">
                <% int count = 1;%>
                <c:forEach var="orderNote" varStatus="i" items="${order.orderInfo.orderProperties}" >
                    <c:if test="${propertyOrderNote == orderNote.orderPropertyTypeCd ||
                                  propertyCustomerCartComment == orderNote.orderPropertyTypeCd}">
                        <tr>
                            <td width="20%">
                                <span class="cell label">
                                    <label><app:message code="admin.order.label.note" /><span class="colon">:</span></label>
                                </span>

                                &nbsp;
                                <span class="labelValue">
                                    <c:out value="<%=count%>" />
                                </span>
                                <br>

                                <span class="cell label">
                                    <label><app:message code="admin.order.label.addedBy" /><span class="colon">:</span></label>
                                </span>

                                &nbsp;
                                <span class="labelValue">
                                    <c:out value="${orderNote.addBy}" />
                                </span>
                                <br>
                                
                                <span class="cell label">
                                    <label><app:message code="admin.order.label.addedDate" /><span class="colon">:</span></label>
                                </span>

                                &nbsp;
                                <span class="labelValue">
                                    <app:formatDate value="${orderNote.addDate}"/>
                                </span>
                                <br>
                            </td>
                            <td align="left" width="80%">
                                <c:set var="noteShortDesc" value="${orderNote.shortDesc}" />
                                <c:set var="noteValue" value="${orderNote.value}" />
                                <c:if test="${(not empty noteShortDesc) && (not empty noteValue) && (noteShortDesc != noteValue)}">
                                    <label><c:out value="${orderNote.shortDesc}" /></label>
                                </c:if>
                                <br>

                                <c:if test="${fn:contains(orderNote.value, 'This email') || fn:contains(orderNote.value, 'This Email')}">
                                    <pre>
                                </c:if>
                                <c:set var="noteValue" value="${orderNote.value}" />
                                <%
                                    String n = (String) pageContext.getAttribute("noteValue");
                                    String convertedN = new String(n.getBytes(), "UTF-8");
                                %>
                                <span class="labelValue">
                                    <%=convertedN%>
                                </span>
                                <c:if test="${fn:contains(orderNote.value, 'This email') || fn:contains(orderNote.value, 'This Email')}">
                                    </pre>
                                </c:if>
                            </td>
                        </tr>
                        <% count++; %>
                    </c:if>
                </c:forEach>
            </table>
        </c:if>
        
        <table width="100%">
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
                            <tr class="row">
                                <td>&nbsp;</td>
                                <td class="cell cell-text">
                                    <c:if test="${order.orderNote != null}">
                                        <c:if test="${order.orderNote.addDate != null && order.orderNote.addBy != null}">
                                            (<app:message code="admin.global.text.addedBy"/>&nbsp;
                                             <c:out value="${order.orderNote.addBy}"/>&nbsp;
                                             <app:message code="admin.global.text.on"/>&nbsp;
                                            <app:formatDate value="${order.orderNote.addDate}"/>)
                                        </c:if>
                                    </c:if>
                                </td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.ordered}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.accepted}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.shipped}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.backordered}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.substituted}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.invoiced}"/></td>
                                <td class="cell cell-text"><c:out value="${order.cumulativeSummary.returned}"/></td>
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
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/distName"><app:message code="admin.order.label.outboundPONum" /></a></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/webOrderNum"><app:message code="admin.order.label.cwSKUNum" /></a></th>
                                            <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderDate"><app:message code="admin.order.label.distSKUNum" /></a></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.distName" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.productName" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.uom" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.pack" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.itemSize" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.customerPrice" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.cwCost" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.qty" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.status" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poStatus" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.poAndShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.targetShipDate" /></th>
                                            <th class="cell cell-text"><app:message code="admin.order.label.openLineStatus" /></th>
                                        </tr>
                                    </thead>

                                    <tbody class="body">
                                        <c:forEach var="itemInfo" varStatus="i" items="${order.orderItems}" >
                                            <c:set var="qty" value="${itemInfo.orderItem.totalQuantityOrdered}" />
                                            <c:set var="iIndex" value="${i.index}" />

                                            <tr class="row"><td colspan="17"></td></tr>
                                            <tr class="row">
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
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
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSkuNum}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.distItemSkuNum}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.distName}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemShortDesc}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemUom}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemPack}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSize}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.custContractPrice}"/></td>
                                                <td class="cell cell-text"><c:out value="${itemInfo.orderItem.distItemCost}"/></td>
                                                <td class="cell cell-text"><c:out value="${qty}"/></td>
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
                                                    <app:formatDate value="${itemInfo.orderItem.erpPoDate}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <app:formatDate value="${itemInfo.orderItem.targetShipDate}"/>
                                                </td>
                                                <td class="cell cell-text">
                                                    <c:out value="${itemInfo.openLineStatusCd}"/>
                                                </td>
                                            </tr>

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
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.affectedSku}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.distItemSkuNum}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemShortDesc}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemUom}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemPack}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.itemDistCost}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.quantity}"/></td>
                                                        <td class="cell cell-text"><c:out value="${itemActionDetail.orderItemAction.actionCd}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><app:formatDate value="${itemActionDetail.orderItemAction.actionDate}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                            
                                            <c:if test="${(itemInfo.orderItemNotes != null) && (fn:length(itemInfo.orderItemNotes) > 0)}">
                                                <c:forEach var="orderItemNote" varStatus="j" items="${itemInfo.orderItemNotes}" >
                                                    <tr>
                                                        <td class="cell cell-text"><c:out value="${itemInfo.orderItem.orderLineNum}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text"><c:out value="${itemInfo.orderItem.itemSkuNum}" /></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text" colspan="10"> 
                                                            <c:set var="itemNoteValue" value="${orderItemNote.value}" />
                                                            <%
                                                                String note = (String) pageContext.getAttribute("itemNoteValue");
                                                                String convertedNote = new String(note.getBytes(), "UTF-8");
                                                            %>
                                                            <%=convertedNote%>
                                                        </td>                      
                                                        <td class="cell cell-text"><app:formatDate value="${orderItemNote.addDate}"/></td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                        <td class="cell cell-text">&nbsp;</td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>

                                        </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>

        </table>
        <table width="100%">
            <tr><td><hr/></td></tr>
            <tr>
                <td>
                <label>Note:</label>  In order to print and view all fields, you must set your browser to "Landscape".
                <br>
                <br>
                <label>If you have Internet Explorer:</label>
                <br>
                Go to File:  Page Setup.  Under Orientation, Click "Landscape", then "OK".
                <br>
                To Print, just go to File: Print.
                <br>
                <br>
                <label>If you have Netscape:</label>
                <br>
                Go to File: Print.  Click on the "Properties" button.
                <br>
                Select the "Layout" tab.  Under Orientation, Click "Landscape", then "OK".
                </td>
            </tr>
            <tr><td><hr/></td></tr>
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
                    <form:button tabindex="1000" onclick="${backToEditAction} return false;"><app:message code="admin.global.button.back"/></form:button>
                </td>
            </tr>
        </table>
    </td>
</tr>

</table>
</form:form>

</div>
</div>


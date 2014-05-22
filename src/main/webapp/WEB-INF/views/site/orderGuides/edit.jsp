<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>

<c:set var="orderGuideIdToEdit" value="${(empty  siteOrderGuideDetail.orderGuideId ? 0 : siteOrderGuideDetail.orderGuideId)}"/>

<app:url var="baseUrl" value="/location/${siteOrderGuideDetail.siteId}"/>

<app:url var="baseUtl"/>
<app:url var="sortUrl" value="/location/${siteOrderGuideDetail.siteId}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0 ? siteOrderGuideDetail.orderGuideId: 0}/sortby"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0?siteOrderGuideDetail.orderGuideId: 0}/save');$('form:first').submit(); return false; "/>
<c:set var="deleteAction" value="$('form:first').attr('action','${baseUrl}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0?siteOrderGuideDetail.orderGuideId: 0}/delete');$('form:first').submit(); return false; "/>
<c:set var="deleteItemsAction" value="$('form:first').attr('action','${baseUrl}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0?siteOrderGuideDetail.orderGuideId: 0}/deleteItems');$('form:first').submit(); return false; "/>
<c:set var="addNewItemsAction" value="$('form:first').attr('action','${baseUrl}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0?siteOrderGuideDetail.orderGuideId: 0}/addNewItems');$('form:first').submit(); return false; "/>
<c:set var="locateItemPostHandler" value="function(value) { window.location.href='${baseUrl}/orderGuides/${siteOrderGuideDetail.orderGuideId > 0 ? siteOrderGuideDetail.orderGuideId: 0}/redraw'; }" />

<app:url var="baseUrlLocate"/>
<c:set var="detailUrl" value="/location/${siteOrderGuideDetail.siteId}"/>

<script language="JavaScript1.2">
function reloadItems() {
	var form = document.getElementById("siteOrderGuideDetail");
	form.action.value = "addNewItems";
	form.submit();
	return false;

}
</script>

<c:set var="finallyAct" value="function(value) {reloadItems()}"/>


<div class="canvas">
<div class="details">
    <form:form id="siteOrderGuideDetail" modelAttribute="siteOrderGuideDetail" action="" method="POST">

<app:locateLayer var="itemLayer"
                 titleLabel="admin.global.filter.label.locateItem.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrlLocate}/locate/itemOrderGuide/multi?catalogId=${siteOrderGuideDetail.catalogId}'
                 action="${baseUrlLocate}/locate/itemOrderGuide/selected?filter=siteOrderGuideDetail.addNewItems"
                 idGetter="itemId"
                 nameGetter="itemSku"
                 targetNames="itemFilterInputSkus"
                 targetIds="itemFilterInputIds"
                 postHandler="${locateItemPostHandler}"
                 />


    <table >
        <tbody>
        <tr>
            <td><div class="label"><form:label path="orderGuideId"><app:message code="admin.site.orderGuideEdit.label.orderGuideId"/><span class="colon">:</span></form:label></div></td>
            <td ><div class="labelValue"><c:out value="${siteOrderGuideDetail.orderGuideId}" default="0"/></div><form:hidden  path="orderGuideId"/></td>
            <td><div class="label"><form:label path="catalogId"><app:message code="admin.site.orderGuideEdit.label.catalogId"/><span class="colon">:</span></form:label></div></td>
            <td ><div class="labelValue"><c:out value="${siteOrderGuideDetail.catalogId}" default="0"/></div><form:hidden  path="catalogId"/></td>
       </tr>

        </tbody>

        <tbody>

        <tr>

            <td><div class="label"><form:label  path="orderGuideName"><app:message code="admin.site.orderGuideEdit.label.orderGuideName"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td ><form:input tabindex="1" path="orderGuideName" cssClass="inputLong"/></td>
            <td><div class="label"><form:label path="catalogName"><app:message code="admin.site.orderGuideEdit.label.catalogName"/><span class="colon">:</span></form:label></div></td>
            <td ><div class="labelValue"><c:out value="${siteOrderGuideDetail.catalogName}" default="0"/></div><form:hidden  path="catalogName"/></td>
       </tr>


        <tr>
            <td><div class="label"><form:label path="orderGuideTypeCd"><app:message code="admin.site.orderGuideEdit.label.orderGuideType"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="2" path="orderGuideTypeCd" cssClass="selectLong"><form:option value=""><app:message code="admin.global.select"/></form:option>
                    <form:option value="<%=RefCodeNames.ORDER_GUIDE_TYPE_CD.BUYER_ORDER_GUIDE%>"><%=RefCodeNames.ORDER_GUIDE_TYPE_CD.BUYER_ORDER_GUIDE%></form:option>
                    <form:option value="<%=RefCodeNames.ORDER_GUIDE_TYPE_CD.SITE_ORDER_GUIDE_TEMPLATE%>"><%=RefCodeNames.ORDER_GUIDE_TYPE_CD.SITE_ORDER_GUIDE_TEMPLATE%></form:option>
                </form:select>
            </td>
            <td><div class="label"><form:label path="shareBuyerOrderGuide"><app:message code="admin.site.orderGuideEdit.label.shareBuyerOrderGuide"/><span class="colon">:</span></form:label></div></td>
            <td>
                <form:radiobutton tabindex="3" class="radio" path="shareBuyerOrderGuide" cssClass="radio" readonly="true" disabled="true" value="true"/> <span class="label"><app:message code="admin.global.text.yes"/></span>
                <form:radiobutton tabindex="3" class="radio" path="shareBuyerOrderGuide" cssClass="radio" readonly="true" disabled="true" value="false"/> <span class="label"><app:message code="admin.global.text.no"/></span>
            </td>
        </tr>

        <tr>
            <td colspan="4" style="height:15px"></td>
        </tr>
		<tr>
			<td  colspan="4" align="center">
	             <form:button tabindex="4" onclick="${updateAction}"><app:message code="admin.global.button.save"/></form:button>
                 <c:if test="${siteOrderGuideDetail.orderGuideId > 0}">&nbsp;&nbsp;&nbsp;
					<form:button tabindex="5" onclick="${deleteAction}"><app:message code="admin.global.button.delete"/></form:button>
                 </c:if>

	    	</td>
	    </tr>
        </tbody>
    </table>
   <div  style="padding-right: 150px;">
   <hr><br>
   <c:if test="${siteOrderGuideDetail.result != null}">
        <div class="resultCount label">
            <table width="100%">
                <tr>
                    <td align="left">
                        <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value">${fn:length(siteOrderGuideDetail.result)}</label>
                    </td>
                    <td align="right">
                        <label><app:message code="admin.site.orderGuideEdit.label.totalAmount" /><span class="colon">:</span></label>
                        <label class="value"><app:formatCurrency value="${siteOrderGuideDetail.totalAmount}"/></label>
                    </td>
                </tr>
            </table>
        </div><br>
    </c:if>

       <div class="search">
            <br>
            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="6%"/>
                    <col width="4%"/>

                    <col width="15%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="10"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="5%"/>

                    <col width="5%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">

                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/quantity"><app:message code="admin.site.orderGuide.item.label.quantity" /></a></th>
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/extendedPrice"><app:message code="admin.site.orderGuide.item.label.extendedPrice" /></a></th>

                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemSkuNum"><app:message code="admin.site.orderGuide.item.label.SKU" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/custItemSkuNum"><app:message code="admin.site.orderGuide.item.label.custSKU" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/productName"><app:message code="admin.site.orderGuide.item.label.productName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemSize"><app:message code="admin.site.orderGuide.item.label.itemSize" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/pack"><app:message code="admin.site.orderGuide.item.label.pack" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/uom"><app:message code="admin.site.orderGuide.item.label.UOM" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/mfg"><app:message code="admin.site.orderGuide.item.label.mfg" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/mfgSku"><app:message code="admin.site.orderGuide.item.label.mfgSKU" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/distSku"><app:message code="admin.site.orderGuide.item.label.distSKU" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/category"><app:message code="admin.site.orderGuide.item.label.category" /></a></th>

                    <th class="cell cell-text cell-element" nowrap>
                         <a href="javascript:checkAll('orderGuideItems', 'items.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                         <a href="javascript:checkAll('orderGuideItems', 'items.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                     </th>
                </tr>

                </thead>

                <tbody class="body">

		        <c:if test="${siteOrderGuideDetail.result != null}">
                
<% System.out.println("RESULT NOT NULL");%>

                <c:forEach var="item" varStatus="i" items="${siteOrderGuideDetail.result}">

                    <tr class="row">
                        <td class="cell cell-number">
                            <form:input tabindex="5" path="items.selectableObjects[${i.index}].value.quantityStr" style="width:50px"/>
                        </td>
                        <td class="cell cell-number"><app:formatCurrency value="${item.value.extendedPrice}"/></td>

                        <td class="cell cell-text"><c:out  value="${item.value.itemSkuNum}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.custItemSkuNum}"/></td>

                        <td class="cell cell-text"><c:out  value="${item.value.productName}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.itemSize}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.pack}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.uom}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.mfg}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.mfgSku}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.distSku}"/></td>
                        <td class="cell cell-text"><c:out  value="${item.value.category}"/></td>
                         
                        <td class="cell cell-element">
                            <c:if test="${item.value.orderGuideStructureId > 0}">
                                <form:checkbox cssClass="checkbox" path="items.selectableObjects[${i.index}].selected"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </c:if>
                 <tr>
                    <td colspan="11" valign="top" align="center" style="white-space: nowrap;">
                        <c:if test="${siteOrderGuideDetail.orderGuideId > 0}">
                            <form:button tabindex="6" onclick="${itemLayer}"><app:message code="admin.global.button.locateItem"/></form:button>
                            <c:if test="${siteOrderGuideDetail.result != null}">&nbsp;&nbsp;&nbsp;
                                <form:button tabindex="7" onclick="${deleteItemsAction}"><app:message code="admin.global.button.deleteSelected"/></form:button>
                            </c:if>
                        </c:if>
                    </td>
                </tr>

                </tbody>
            </table>
            <br><br>

        </div>

    </form:form>
  
 </div>
</div>
</div>

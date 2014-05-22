<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/itemOrderGuide/filter"/>
<app:url var="sortUrl" value="/locate/itemOrderGuide/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateItemOrderGuideFilterResultId', 'itemLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateItemOrderGuideFilter" id="locateItemOrderGuideFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

        <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first">
                                <label><app:message code="admin.global.filter.label.itemId" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">
                                <form:input tabindex="1" id="itemId" path="itemId" cssClass="filterValue"/>
                            </td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.itemName" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="itemName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="itemNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="4" path="itemNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.itemCategory" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="itemCategory" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="itemCategoryFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="4" path="itemCategoryFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.itemSku" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="itemSku" cssClass="filterValue"/></td>
                            <td nowrap colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="5" path="itemSkuFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.RefCodeNames.SKU_TYPE_CD.STORE%>"/><span class="label"><app:message code="refcodes.SKU_TYPE_CD.STORE"/></span>
                                <form:radiobutton tabindex="5" path="itemSkuFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR%>"/><span class="label"><app:message code="refcodes.SKU_TYPE_CD.DISTRIBUTOR"/></span>
                                <form:radiobutton tabindex="5" path="itemSkuFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.RefCodeNames.SKU_TYPE_CD.MANUFACTURER%>"/><span class="label"><app:message code="refcodes.SKU_TYPE_CD.MANUFACTURER"/></span><br>
                                <form:radiobutton tabindex="6" path="itemSkuFilterSubType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                                <form:radiobutton tabindex="7" path="itemSkuFilterSubType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>

                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <span>
                                    <form:checkbox tabindex="7" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" />
                                </span>
                            </td>

                            <td colspan="2" class="cell">&nbsp;<br><br></td>
                        </tr>
                            <tr>
                                <td colspan="4"><br><br></td>
                            </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <form:button  id="search" cssClass="button" tabindex="8"  onclick="$('form:first').submit(); return false;">
                                    <app:message code="admin.global.button.search"/>
                                </form:button>
                            </td>
                            <td colspan="2" class="cell">
                            </td>
                        </tr>   <tr>
                            <td colspan="4"><br><br></td>
                        </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>

    <c:if test="${locateItemOrderGuideFilterResult.result != null}">
        <hr style="margin-right: 15px;"/>
        <table class="searchResult" width="95%" >
            <tr>
                <td class="resultCount label">
                    <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value"></label>${fn:length(locateItemOrderGuideFilterResult.result)}
                </td>
                <td align="right" style="padding:0;vertical-align: top">
                    <form:button style="margin-top:-5px" cssClass="button" tabindex="9" onclick="return ${doReturnSelected}">
                        <app:message code="admin.global.button.returnSelected"/>
                    </form:button>
                </td>
            </tr>
        </table>
    </c:if>

    </form:form>

    <form:form id="locateItemOrderGuideFilterResultId" modelAttribute="locateItemOrderGuideFilterResult" method="POST">
        <c:if test="${locateItemOrderGuideFilterResult.result != null}">

            <table class="searchResult" width="95%">
                <colgroup>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="15%"/>
                    <col width="8%"/>
                    <col width="6%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="9%"/>
                    <col width="10%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/itemId"><app:message code="admin.global.filter.label.itemId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemSku"><app:message code="admin.global.filter.label.itemSku" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemName"><app:message code="admin.global.filter.label.itemName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemUom"><app:message code="admin.global.filter.label.itemUom" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemPack"><app:message code="admin.global.filter.label.itemPack" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemCategoryName"><app:message code="admin.global.filter.label.itemCategory" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemManufacturerName"><app:message code="admin.global.filter.label.itemManufacturer" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemManufacturerSku"><app:message code="admin.global.filter.label.itemManufacturerSku" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemDistributorSku"><app:message code="admin.global.filter.label.itemDistributorSku" /></a></th>
                	<th class="cell cell-text"><a class="sort" href="${sortUrl}/itemStatusCd"><app:message code="admin.global.filter.label.status" /></a></th>
					<th class="cell cell-text cell-element">
                        <a href="javascript:checkAll('locateItemOrderGuideFilterResultId', 'selectedItems.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('locateItemOrderGuideFilterResultId', 'selectedItems.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="item" items="${locateItemOrderGuideFilterResult.result}" varStatus="i">
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${item.value.itemId}"/></td>
                        <td class="cell cell-number"><c:out value="${item.value.itemSku}"/></td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateItemOrderGuideFilterResultId', 'selectedItems.selectableObjects','item_${item.value.itemId}');return ${doReturnSelected}"><c:out value="${item.value.itemName}"/></a></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemUom}"/></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemPack}"/></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemCategoryName}"/></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemManufacturerName}"/></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemManufacturerSku}"/></td>
                        <td class="cell cell-text"><c:out value="${item.value.itemDistributorSku}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.ITEM_STATUS_CD.${item.value.itemStatusCd}" text="${item.value.itemStatusCd}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="item_${item.value.itemId}" path="selectedItems.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>

                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


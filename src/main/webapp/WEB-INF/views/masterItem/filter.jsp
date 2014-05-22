<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/masterItem/filter"/>
<app:url var="sortUrl" value="/masterItem/filter/sortby"/>
<app:url var="editUrl" value="/masterItem"/>
<app:url var="reloadUrl" value="/masterItem"/>
<app:url var="baseUrl"/>
<app:url var="importUrl" value="/uploadFile"/>


<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="cloneAction"  value="$('form:first').attr('action','${editUrl}/${masterItem.itemId > 0?masterItem.itemId: 0}/clone');$('form:first').attr('method','POST');$('form:first').submit(); return false; "/>

<script type="text/javascript" language="JavaScript">
function setItemCloneId(cloneId) {
		document.getElementById("masterItemFlterFormId").cloneItemId.value=cloneId;
}
</script>


<div class="search">

        <div class="filter" >
		<form:form modelAttribute="masterItemFilter" id="masterItemFlterFormId" method="GET" action="${searchUrl}" focus="filterValue">
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="1" focusable="true" id="itemId" path="itemId" cssClass="filterValue"/>
                    </td>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemProperty" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:select tabindex="20" path="itemPropertyCd" focusable="true" style="width:150px">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.itemPropertyTypes}">
                                <form:option value="${type.object2}"><app:message code="refcodes.ITEM_PROPERTY_CD.${type.object1}" text="${type.object2}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="21" focusable="true" id="itemProperty" path="itemProperty" cssClass="filterValue" style="width:100px"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="2" focusable="true" id="filterValue" path="itemName" cssClass="filterValue"/>
                    </td>
                    <td class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="3" path="itemNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="4" path="itemNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
					<td colspan=3></td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemCategory" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="filterValue" path="itemCategory" cssClass="filterValue"/>
                    </td>
                    <td colspan="4" class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="6" path="itemCategoryFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="7" path="itemCategoryFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemManufacturer" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="8" focusable="true" id="manufacturer" path="manufacturer" cssClass="filterValue"/>
                    </td>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemDistributor" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="9" focusable="true" id="distributor" path="distributor" cssClass="filterValue"/>
                    </td>
                    <td colspan="4"></td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.itemSku" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="10" focusable="true" id="sku" path="itemSku" cssClass="filterValue"/>
                    </td>
                    <td colspan="4" class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="11" path="itemSkuTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.STORE%>"/><span class="label"><app:message code="admin.global.filter.label.store"/></span>&nbsp;<form:radiobutton tabindex="12" path="itemSkuTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.DISTRIBUTOR%>"/><span class="label"><app:message code="admin.global.filter.label.distributor"/></span>&nbsp;<form:radiobutton tabindex="13" path="itemSkuTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.MANUFACTURER%>"/><span class="label"><app:message code="admin.global.filter.label.manufacturer"/></span>
                        <br><form:radiobutton tabindex="14" path="itemSkuFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>&nbsp;<form:radiobutton tabindex="15" path="itemSkuFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>


                <tr>
                    <td class="cell label first"></td>
                    <td class="cell">
                        <label><br><form:checkbox tabindex="16" cssClass="checkbox" focusable="true" path="greenCertified"/><app:message code="admin.global.filter.label.greenCertified" /><br></label>
                        <label><br><form:checkbox tabindex="17" cssClass="checkbox" focusable="true" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label>
                    </td>
                   <td class="cell" colspan="4">
                        <label><br><form:checkbox tabindex="18" cssClass="checkbox" focusable="true" path="showMfgInfo"/><app:message code="admin.global.filter.label.showManufInfo" /><br></label>
                        <label><br><form:checkbox tabindex="19" cssClass="checkbox" focusable="true" path="showDistInfo"/><app:message code="admin.global.filter.label.showDistrInfo" /><br><br></label>
                    </td>

                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="5">
                        <form:button id="search"  cssClass="button" tabindex="22"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="23" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        &nbsp;&nbsp;&nbsp;
                        <form:button tabindex="24" onclick="${cloneAction}"><app:message code="admin.global.button.clone"/></form:button >
                        &nbsp;&nbsp;&nbsp;
                        <button type="button" tabindex="25" onclick="location.href='${importUrl}'"><app:message code="admin.global.button.import"/></button >
                        <br><br><br>
                    </td>
                </tr>
            </table>
        </div>
		<form:hidden path="cloneItemId"/>
	</form:form>

    <c:if test="${masterItemFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(masterItemFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${masterItemFilterResult.result != null}">
		<form:form modelAttribute="masterItemFilterResult" id="masterItemFlterResultFormId" method="POST" action="${cloneAction}">
        <table class="searchResult" width="100%">
            <colgroup>
                <col width="5%"/>
                <col width="15%"/>
                <col width="7%"/>
                <col width="7%"/>
                <col width="7%"/>
                <col width="7%"/>
                <col width="7%"/>
                <col width="7%"/>
				<c:if test="${masterItemFilter.showMfgInfo}">
                <col width="7%"/>
                <col width="7%"/>
				</c:if>
				<c:if test="${masterItemFilter.showDistInfo}">
                <col width="7%"/>
                <col width="7%"/>
				</c:if>
                <col width="7%"/>
                <col width="7%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/itemId"><app:message code="admin.global.filter.label.itemId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemName"><app:message code="admin.global.filter.label.itemName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/sku"><app:message code="admin.global.filter.label.itemSku" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/size"><app:message code="admin.global.filter.label.itemSize" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/pack"><app:message code="admin.global.filter.label.itemPack" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/uom"><app:message code="admin.global.filter.label.itemUom" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/color"><app:message code="admin.global.filter.label.itemColor" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/category"><app:message code="admin.global.filter.label.itemCategory" /></a></th>
				<c:if test="${masterItemFilter.showMfgInfo}">
				<th class="cell cell-text"><a class="sort" href="${sortUrl}/manufacturer"><app:message code="admin.global.filter.label.itemManufacturer" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/manufacturerSku"><app:message code="admin.global.filter.label.itemManufacturerSku" /></a></th>
                </c:if>
				<c:if test="${masterItemFilter.showDistInfo}">
				<th class="cell cell-text"><a class="sort" href="${sortUrl}/distributor"><app:message code="admin.global.filter.label.itemDistributor" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/distributorSku"><app:message code="admin.global.filter.label.itemDistributorSku" /></a></th>
				</c:if>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.itemStatus" /></a></th>
                <th class="cell cell-text"><app:message code="admin.global.filter.label.select" /></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="masterItem" items="${masterItemFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${masterItem.itemId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${masterItem.itemId}"><c:out value="${masterItem.itemName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${masterItem.sku}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.size}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.pack}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.uom}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.color}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.category}"/></td>
					<c:if test="${masterItemFilter.showMfgInfo}">
                    <td class="cell cell-text"><c:out value="${masterItem.manufacturer}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.manufacturerSku}"/></td>
					</c:if>
					<c:if test="${masterItemFilter.showDistInfo}">
                    <td class="cell cell-text"><c:out value="${masterItem.distributor}"/></td>
                    <td class="cell cell-text"><c:out value="${masterItem.distributorSku}"/></td>
					</c:if>
                    <td class="cell cell-text"><app:message code="refcodes.ITEM_STATUS_CD.${masterItem.status}" text="${masterItem.status}"/></td>
                    <td class="cell cell-text">
                        <form:radiobutton path="cloneItemId" cssClass="radio" focusable="true" value="${masterItem.itemId}" onchange="setItemCloneId(${masterItem.itemId})" />
                    </td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
		</form:form>
    </c:if>

</div>


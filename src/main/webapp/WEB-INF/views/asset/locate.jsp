<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/asset/filter"/>
<app:url var="sortUrl" value="/locate/asset/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateAssetFilterResultId', 'assetLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateAssetFilter" id="locateAssetFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

        <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first">
                                <label><app:message code="admin.global.filter.label.assetId" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">
                                <form:input tabindex="1" id="assetId" path="assetId"/>
                            </td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.assetName" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;">
                                <form:input tabindex="2" id="filterValue" path="assetName" cssClass="filterValue"/>
                            </td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="assetNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/>
                                <span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="4" path="assetNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/>
                                <span class="label"><app:message code="admin.global.filter.label.contains"/></span>
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
                                <form:button  id="search"  cssClass="button" tabindex="8"  onclick="$('form:first').submit(); return false;">
                                    <app:message code="admin.global.button.search"/>
                                </form:button>
                            </td>
                            <td colspan="2" class="cell"></td>
                        </tr>
                        <tr>
                            <td colspan="4"><br><br></td>
                        </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>

        <c:if test="${locateAssetFilterResult.result != null}">
            <hr style="margin-right: 15px;"/>
            <table class="searchResult" width="95%" >
                <tr>
                    <td class="resultCount label">
                        <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                        <label class="value"></label>${fn:length(locateAssetFilterResult.result)}
                    </td>
                    <td align="right" style="padding:0;vertical-align: top">
                        <form:button style="margin-top:-5px" cssClass="button" tabindex="9" onclick="return ${doReturnSelected}">
                            <app:message code="admin.global.button.returnSelected"/>
                        </form:button>
                    </td>
                </tr>
            </table>
        </c:if>

        <form:hidden path="siteId"/>

    </form:form>

    <form:form modelAttribute="locateAssetFilterResult" id="locateAssetFilterResultId"  method="POST">
        <c:if test="${locateAssetFilterResult.result != null}">

            <table class="searchResult" width="95%">
                <colgroup>
                    <col width="5%"/>
                    <col width="20%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="5%"/>
                    <col width="20%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/assetId"><app:message code="admin.global.filter.label.assetId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/assetName"><app:message code="admin.global.filter.label.assetName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/assetType"><app:message code="admin.global.filter.label.assetType" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/assetNum"><app:message code="admin.global.filter.label.assetNum" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/serialNum"><app:message code="admin.global.filter.label.serialNum" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteId"><app:message code="admin.global.filter.label.locationID" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.locationName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/assetStatus"><app:message code="admin.global.filter.label.assetStatus" /></a></th>
                    <th class="cell cell-text cell-element">
                        <a href="javascript:checkAll('locateAssetFilterResultId', 'selectedAssets.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('locateAssetFilterResultId', 'selectedAssets.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="asset" items="${locateAssetFilterResult.result}" varStatus="i">
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${asset.value.assetId}"/></td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateAssetFilterResultId', 'selectedAssets.selectableObjects','asset_${asset.value.assetId}');return ${doReturnSelected}"><c:out value="${asset.value.assetName}"/></a></td>
                        <td class="cell cell-text"><c:out value="${asset.value.assetType}"/></td>
                        <td class="cell cell-text"><c:out value="${asset.value.assetNum}"/></td>
                        <td class="cell cell-text"><c:out value="${asset.value.serialNum}"/></td>
                        <td class="cell cell-text"><c:out value="${asset.value.siteId}"/></td>
                        <td class="cell cell-text"><c:out value="${asset.value.siteName}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.ASSET_STATUS_CD.${asset.value.assetStatus}" text="${asset.value.assetStatus}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="asset_${asset.value.assetId}" path="selectedAssets.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>

                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>
    </form:form>
</div>


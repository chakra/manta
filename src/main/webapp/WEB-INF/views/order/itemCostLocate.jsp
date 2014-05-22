<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/itemCost/filter"/>
<app:url var="searchAssignedDistUrl" value="$('form:first').attr('action','${baseUrl}/locate/itemCost/filter/assignedDist'); $('form:first').submit(); return false;" />
<app:url var="searchAllDistUrl" value="$('form:first').attr('action','${baseUrl}/locate/itemCost/filter/allDist'); $('form:first').submit(); return false;"/>
<app:url var="sortUrl" value="/locate/itemCost/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateItemCostFilterResultId', 'cwCostLayer');"/>

<script>
    function changeActionAndSubmit(value) {
        var form = $('form:first').attr('action','${baseUrl}/locate/itemCost/filter/' + value);
        $('form:first').submit();
        return false;
    }
</script> 

<div class="search locate">

    <form:form modelAttribute="locateItemCostFilter" id="locateItemCostFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.order.label.SKUNum" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <c:out value="${locateItemCostFilter.itemSkuNum}"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.order.label.assignedDistributor" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <c:out value="${locateItemCostFilter.distErpNum}"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="4"><br></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td colspan="2" class="cell label">
                        <form:button cssClass="button" tabindex="1" onclick="changeActionAndSubmit('assignedDist');">
                            <app:message code="admin.order.label.assignedDistributor"/>
                        </form:button>
                        &nbsp;
                        <form:button cssClass="button" tabindex="2" onclick="changeActionAndSubmit('allDist');">
                            <app:message code="admin.order.label.allDistributors"/>
                        </form:button>
                    </td>
                    <td>&nbsp;</td>
                </tr>
            
            </table>
        </div>

        <c:if test="${locateItemCostFilterResult.result != null}">
            <hr style="margin-right: 15px;"/>
            <table class="searchResult" width="95%" >
                <tr>
                    <td class="resultCount label">
                        <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                        <label class="value"></label>${fn:length(locateItemCostFilterResult.result)}
                    </td>
                </tr>
            </table>
        </c:if>

    </form:form>

    <form:form modelAttribute="locateItemCostFilterResult" id="locateItemCostFilterResultId"  method="POST">

        <c:if test="${locateItemCostFilterResult.result != null}">
            <table class="searchResult" width="95%">

                <colgroup>
                    <col width="10%"/>
                    <col width="40%"/>
                    <col width="15%"/>
                    <col width="40%"/>
                    <col width="5%"/>
                </colgroup>

                <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/distId"><app:message code="admin.order.label.distributorId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/distDesc"><app:message code="admin.order.label.distributorName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/itemCost"><app:message code="admin.order.label.distributorCost" /></a></th>
                        <th colspan="2" class="cell cell-text"><app:message code="admin.order.label.Contracts" /></th>
                    </tr>
                </thead>

                <tbody class="body">
                    <c:forEach var="itemCost" items="${locateItemCostFilterResult.result}" varStatus="i">
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${itemCost.value.distId}"/></td>
                            <td class="cell cell-text"><c:out value="${itemCost.value.distDesc}"/></td>
                            <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateItemCostFilterResultId', 'selectedItemCosts.selectableObjects','itemCost_${itemCost.value.distId}');return ${doReturnSelected}"><c:out value="${itemCost.value.itemCost}"/></a></td>
                            <td class="cell cell-text"><c:out value="${itemCost.value.contractDesc}"/></td>
                            <td class="cell cell-element">
                                <span style="display:none;">
                                    <form:checkbox cssClass="checkbox" id="itemCost_${itemCost.value.distId}" path="selectedItemCosts.selectableObjects[${i.index}].selected"/>
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


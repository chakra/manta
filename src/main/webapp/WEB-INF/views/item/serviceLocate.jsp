<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/service/filter"/>
<app:url var="sortUrl" value="/locate/service/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateServiceFilterResultId', 'serviceLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateItemFilter" id="locateServiceFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

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
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;">
                                <form:input tabindex="2"  id="filterValue"   path="itemName" cssClass="filterValue"/>
                            </td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="itemNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="4" path="itemNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
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

    <c:if test="${locateServiceFilterResult.result != null}">
        <hr style="margin-right: 15px;"/>
        <table class="searchResult" width="95%" >
            <tr>
                <td class="resultCount label">
                    <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value"></label>${fn:length(locateServiceFilterResult.result)}
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
    <form:hidden path="contractId"/>
    <form:hidden path="assetId"/>
    
    </form:form>

    <form:form modelAttribute="locateServiceFilterResult" id="locateServiceFilterResultId"  method="POST">
        <c:if test="${locateServiceFilterResult.result != null}">

            <table class="searchResult" width="95%">
                <colgroup>
                    <col width="10%"/>
                    <col width="20%"/>
                    <col width="70%"/>
                </colgroup>

                <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/serviceId"><app:message code="admin.global.filter.label.itemId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/serviceName"><app:message code="admin.global.filter.label.itemName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/serviceStatus"><app:message code="admin.global.filter.label.status" /></a></th>
                        <th class="cell cell-text cell-element">
                            <a href="javascript:checkAll('locateServiceFilterResultId', 'selectedServices.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('locateServiceFilterResultId', 'selectedServices.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                    </tr>
                </thead>

                <tbody class="body">
                    <c:forEach var="service" items="${locateServiceFilterResult.result}" varStatus="i">
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${service.value.serviceId}"/></td>
                            <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateServiceFilterResultId', 'selectedItems.selectableObjects','service_${service.value.serviceId}');return ${doReturnSelected}"><c:out value="${service.value.serviceName}"/></a></td>
                            <td class="cell cell-text"><app:message code="refcodes.ITEM_STATUS_CD.${service.value.serviceStatus}" text="${service.value.serviceStatus}"/></td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="service_${service.value.serviceId}" path="selectedServices.selectableObjects[${i.index}].selected"/></td>
                        </tr>
                    </c:forEach>
                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


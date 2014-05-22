<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/catalog/filter"/>
<app:url var="sortUrl" value="/locate/catalog/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateCatalogFilterResultId', 'catalogLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateCatalogFilter" id="locateCatalogFilterId" method="POST" focus="filterValue" action="${searchUrl}" >
        <form:hidden path="catalogType" value="${param['catalogType']}"/>
        <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first">
                                <label><app:message code="admin.global.filter.label.catalogId" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">
                                <form:input tabindex="1" id="catalogId" path="catalogId" cssClass="filterValue"/>
                            </td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.catalogName" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="catalogName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="catalogNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="3" path="catalogNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <span>
                                    <form:checkbox tabindex="5" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" />
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
                                <form:button id="search"  cssClass="button" tabindex="6"  onclick="$('form:first').submit(); return false;">
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

    <c:if test="${locateCatalogFilterResult.result != null}">
        <hr style="margin-right: 15px;"/>
        <table class="searchResult" width="95%" >
            <tr>
                <td class="resultCount label">
                    <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value"></label>${fn:length(locateCatalogFilterResult.result)}
                </td>
                <td align="right" style="padding:0;vertical-align: top">
                    <form:button style="margin-top:-5px" cssClass="button" tabindex="14" onclick="return ${doReturnSelected}">
                        <app:message code="admin.global.button.returnSelected"/>
                    </form:button>
                </td>
            </tr>
        </table>
    </c:if>

    </form:form>

    <form:form id="locateCatalogFilterResultId" modelAttribute="locateCatalogFilterResult" method="POST">
        <c:if test="${locateCatalogFilterResult.result != null}">

            <table class="searchResult" width="95%">
                <colgroup>
                    <col width="8%"/>
                    <col width="21%"/>
                    <col width="11%"/>
                    <col width="10%"/>
                    <col width="18%"/>
                    <col width="12%"/>
                    <col width="10%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/catalogId"><app:message code="admin.global.filter.label.catalogId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogName"><app:message code="admin.global.filter.label.catalogName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.global.filter.label.catalogType" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.catalogStatus" /></a></th>
                    <th class="cell cell-text cell-element">
                        <a href="javascript:checkAll('locateCatalogFilterResultId', 'selectedCatalogs.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('locateCatalogFilterResultId', 'selectedCatalogs.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="catalog" items="${locateCatalogFilterResult.result}" varStatus="i">
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${catalog.value.catalogId}"/></td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateCatalogFilterResultId', 'selectedCatalogs.selectableObjects','catalog_${catalog.value.catalogId}');return ${doReturnSelected}"><c:out value="${catalog.value.catalogName}"/></a></td>
                        <td class="cell cell-text"><c:out value="${catalog.value.type}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${catalog.value.status}" text="${catalog.value.status}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="catalog_${catalog.value.catalogId}" path="selectedCatalogs.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>

                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


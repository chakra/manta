<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/costCenter/${costCenterHeader.costCenterId}/catalog" />


<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#costCenterCatalog').submit(); return false;"/>


<script type="text/javascript" language="JavaScript">

</script>

<div class="canvas">

    <div class="search">

        <form:form id="search" modelAttribute="costCenterCatalogFilter" method="GET" action="${searchUrl}" focus="costCenterId">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                        <td colspan="4">
                            <div class="subHeader"><app:message code="admin.costCenter.catalog.label.catalogAssociation"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td colspan="4" class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.costCenter.catalog.label.catalogName" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="2" id="catalogName" path="catalogName" cssClass="filterValue"/>
                        </td>
                        <td class="cell" style="padding-top: 0;">                            
                            <form:radiobutton tabindex="3" path="catalogNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                            <br>
                            <form:radiobutton tabindex="3" path="catalogNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell label">
                            <form:checkbox tabindex="4" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.costCenter.catalog.label.associatedCatalogOnly" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell label">
                            <form:checkbox tabindex="5" cssClass="checkbox"  path="showInactive"/>
                            <label><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell label">
                            <form:button  id="search"  tabindex="6" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="costCenterCatalog" modelAttribute="costCenterCatalogFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${costCenterCatalogFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(costCenterCatalogFilterResult.result)}</div>
                        </td>
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${costCenterCatalogFilterResult.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="15%"/>
                        <col width="30%"/>
                        <col width="20%"/>
                        <col width="15%"/>
                        <col width="20%"/>
                    </colgroup>
                    <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/catalogId"><app:message code="admin.costCenter.catalog.label.catalogId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogName"><app:message code="admin.costCenter.catalog.label.catalogName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.costCenter.catalog.label.status" /></a></th>
                        <th class="cell cell-text cell-element">
                            <a href="javascript:checkAll('catalogSelectedResultId', 'catalogs.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('catalogSelectedResultId', 'catalogs.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="body">
                    <c:forEach var="catalog" varStatus="i" items="${costCenterCatalogFilterResult.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${catalog.value.catalogId}"/> </td>
                            <td class="cell cell-text"><c:out value="${catalog.value.catalogName}"/> </td>
                            <td class="cell cell-text"><c:out value="${catalog.value.status}"/> </td>
                            <td class="cell cell-element">
                             <form:checkbox cssClass="checkbox" id="catalog_${catalog.value.catalogId}" path="catalogs.selectableObjects[${i.index}].selected"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr><td colspan="9"><br><br></td></tr>
                    <tr>
                        <td colspan="9">&nbsp;</td>
                        <td align="right"><form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button></td>
                    </tr>
                    </tbody>

                </table>
            </c:if>
        </form:form>

</div>
</div>


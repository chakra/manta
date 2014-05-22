<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/location/${siteHeader.siteId}/catalog" />


<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#siteCatalog').submit(); return false;"/>


<script type="text/javascript" language="JavaScript">

</script>

<div class="canvas">

    <div class="search">

        <form:form id="search" modelAttribute="siteCatalogFilter" method="GET" action="${searchUrl}" focus="siteId">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                        <td colspan="4">
                            <div class="subHeader"><app:message code="admin.site.catalog.label.catalogAssociation"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td colspan="4" class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.site.catalog.label.catalogName" /><span class="colon">:</span></label>
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
                        <td  class="cell">
                            <form:checkbox tabindex="4" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.site.catalog.label.associatedCatalogOnly" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="5" cssClass="checkbox"  path="showInactive"/>
                            <label><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:button  id="search"  tabindex="6" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="siteCatalog" modelAttribute="siteCatalogFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${siteCatalogFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(siteCatalogFilterResult.result)}</div>
                        </td>
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${siteCatalogFilterResult.result != null}">
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
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/catalogId"><app:message code="admin.site.catalog.label.catalogId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogName"><app:message code="admin.site.catalog.label.catalogName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogStatus"><app:message code="admin.site.catalog.label.status" /></a></th>
                        <th class="cell cell-element"><app:message code="admin.site.catalog.label.select" /></th>
                    </tr>
                    </thead>

                    <tbody class="body">
                    <c:forEach var="catalog" varStatus="i" items="${siteCatalogFilterResult.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${catalog.catalogId}"/> </td>
                            <td class="cell cell-text"><c:out value="${catalog.catalogName}"/> </td>
                            <td class="cell cell-text"><c:out value="${catalog.catalogStatus}"/> </td>
                            <td class="cell cell-element"><form:radiobutton tabindex="2" id="catalogId" path="catalogId" cssClass="radio" focusable="true" value="${catalog.catalogId}"/></td>
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


<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/site/filter"/>
<app:url var="sortUrl" value="/locate/site/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateSiteFilterResultId', 'siteLayer');"/>

<div class="search locate">

    <form:form id="locateSiteFilterId" modelAttribute="locateSiteFilter" method="POST" action="${searchUrl}"  focus="filterValue">
        <table>
            <tr>
                <td class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter">
                        <table>
                            <tbody>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.siteId" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value ">
                                    <form:input tabindex="1" id="locationId" path="locationId" cssClass="filterValue"/>
                                </td>
                                <td class="cell">&nbsp;</td>
                            </tr>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.siteName" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value">
                                    <form:input tabindex="2" id="filterValue" path="siteName" cssClass="filterValue"/>
                                </td>
                                <td class="cell" style="padding-top: 0;">                            
                                    <form:radiobutton tabindex="3" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                                    <br>
                                    <form:radiobutton tabindex="3" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.referenceNumber" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value">
                                    <form:input tabindex="4" id="referenceNumber" path="referenceNumber" cssClass="filterValue" size="14"/>
                                </td>
                                <td class="cell" style="padding-top: 0;">                            
                                    <form:radiobutton tabindex="5" path="referenceNumberFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                                    <br>
                                    <form:radiobutton tabindex="5" path="referenceNumberFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.City" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value">
                                    <form:input tabindex="6" id="city" path="city" cssClass="filterValue" size="30"/>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.stateProvince" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value">
                                    <form:input tabindex="7" id="stateProvince" path="stateProvince" cssClass="filterValue" size="30"/>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td class="cell cell-text">&nbsp;</td>
                                <td class="cell label">
                                    <label><app:message code="admin.global.filter.label.postalCode" /><span class="colon">:</span></label>
                                </td>
                                <td class="cell value">
                                    <form:input tabindex="7" id="postalCode" path="postalCode" cssClass="filterValue" size="30"/>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="2">&nbsp;</td>
                                <td class="cell label first">
                                    <span>
                                        <form:checkbox tabindex="8" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" />
                                    </span>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr><td colspan="4">&nbsp;</td></tr>
                            <tr>
                                <td colspan="2">&nbsp;</td>
                                <td  class="cell label first">
                                    <form:button  id="search" tabindex="9" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
        </table>

        <c:if test="${locateSiteFilterResult.result != null}">
            <hr style="margin-right: 15px;"/>
            <table class="searchResult" width="95%" >
                <tr>
                    <td class="resultCount label">
                        <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                        <label class="value"></label>${fn:length(locateSiteFilterResult.result)}
                    </td>
                    <td align="right" style="padding:0; vertical-align: top">
                        <form:button style="margin-top:-5px" cssClass="button" tabindex="10" onclick="return ${doReturnSelected}">
                            <app:message code="admin.global.button.returnSelected"/>
                        </form:button>
                    </td>
                </tr>
            </table>
        </c:if>

    </form:form>

    <form:form id="locateSiteFilterResultId" modelAttribute="locateSiteFilterResult" method="POST" action="${updateUrl}">
        <c:if test="${locateSiteFilterResult.result != null}">
            <table class="searchResult" width="100%">
                <colgroup>
                    <col width="5%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="5%"/>
                    <col width="10%"/>
                    <col width="5%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                </colgroup>
                <thead class="header">
                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/siteId"><app:message code="admin.global.filter.label.siteId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.siteName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.streetAddress" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/state"><app:message code="admin.global.filter.label.state" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.postalCode" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.accountName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.siteStatus" /></a></th>
                    
                    <th class="cell cell-text cell-element" nowrap>
                        <a href="javascript:checkAll('locateSiteFilterResultId', 'selectedSites.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('locateSiteFilterResultId', 'selectedSites.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>
                </thead>

                <tbody class="body">
                <c:forEach var="site" varStatus="i" items="${locateSiteFilterResult.result}" >
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${site.value.siteId}"/> </td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateSiteFilterResultId', 'selectedSites.selectableObjects','site_${site.value.siteId}');return ${doReturnSelected}"><c:out value="${site.value.siteName}"/></a> </td>
                        <td class="cell cell-text"><c:out value="${site.value.address1}"/> </td>
                        <td class="cell cell-text"><c:out value="${site.value.city}"/> </td>
                        <td class="cell cell-text"><c:out value="${site.value.state}"/> </td>
                        <td class="cell cell-text"><c:out value="${site.value.postalCode}"/> </td>
                        <td class="cell cell-text"><c:out value="${site.value.accountName}"/> </td>
                        <td class="cell cell-text"><c:out value="${site.value.status}"/> </td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="site_${site.value.siteId}" path="selectedSites.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


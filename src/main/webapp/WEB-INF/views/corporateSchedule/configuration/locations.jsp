<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/corporateSchedule/${corporateScheduleHeader.id}/location"/>
<app:url var="siteEditUrl" value="/location"/>

<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#scheduleSites').submit(); return false;"/>
<c:set var="updateAllAction" value="$('form:#search').attr('action','${baseUrl}/update/all');$('form:#search').attr('method','POST');$('form:#search').submit(); return false;"/>

<div class="canvas">

    <div class="search">

        <form:form id="search" modelAttribute="corporateScheduleLocationFilter" method="GET" action="${searchUrl}" focus="siteName">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                        <td colspan="4">
                            <div class="subHeader"><app:message code="admin.user.configuration.location.locationAssociations"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                        <td class="cell label">
                            <label><app:message code="admin.user.configuration.location.locationId" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="10" id="siteId" path="siteId" cssClass="filterValue" size="14"/>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.user.configuration.location.locationName" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="1" id="siteName" path="siteName" cssClass="filterValue"/>
                        </td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton tabindex="2" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                            <br>
                            <form:radiobutton tabindex="2" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.user.configuration.location.referenceNumber" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="3" id="referenceNumber" path="referenceNumber" cssClass="filterValue" size="14"/>
                        </td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton focusable="true" tabindex="4" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton focusable="true" tabindex="4" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.user.configuration.location.city" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="5" id="city" path="city" cssClass="filterValue" size="30"/>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.user.configuration.location.stateProvince" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="6" id="stateProvince" path="stateProvince" cssClass="filterValue" size="30"/>
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
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="8" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.user.configuration.location.associatedOnly" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="9" cssClass="checkbox"  path="showInactive"/>
                            <label><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:button  tabindex="11" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="scheduleSites" modelAttribute="corporateScheduleLocationFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${corporateScheduleLocationFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(corporateScheduleLocationFilterResult.result)}</div>
                        </td>
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${corporateScheduleLocationFilterResult.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="10%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="15%"/>
                        <col width="5%"/>
                        <col width="10%"/>
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
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.siteStatus" /></a></th>

                        <th class="cell cell-text cell-element" nowrap>
                            <a href="javascript:checkAll('scheduleSites', 'sites.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('scheduleSites', 'sites.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="body">
                    <c:forEach var="site" varStatus="i" items="${corporateScheduleLocationFilterResult.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${site.value.siteId}"/> </td>
                            <td class="cell cell-text"><a href="${siteEditUrl}/${site.value.siteId}"><c:out value="${site.value.siteName}"/></a> </td>
                            <td class="cell cell-text"><c:out value="${site.value.address1}"/> </td>
                            <td class="cell cell-text"><c:out value="${site.value.city}"/> </td>
                            <td class="cell cell-text"><c:out value="${site.value.state}"/> </td>
                            <td class="cell cell-text"><c:out value="${site.value.postalCode}"/> </td>
                            <td class="cell cell-text"><c:out value="${site.value.status}"/> </td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="sites.selectableObjects[${i.index}].selected"/></td>
                            
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


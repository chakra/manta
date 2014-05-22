<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/manufacturer/filter"/>
<app:url var="sortUrl" value="/manufacturer/filter/sortby"/>
<app:url var="editUrl" value="/manufacturer"/>
<app:url var="reloadUrl" value="/manufacturer"/>
<app:url var="baseUrl"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>


<div class="search">
        <div class="filter" >
            <form:form modelAttribute="manufacturerFilter" id="manufacturerFlterFormId" method="GET" action="${searchUrl}" focus="filterValue">
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.manufacturerId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="4" focusable="true" id="manufacturerId" path="manufacturerId" cssClass="id filterValue"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.manufacturerName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="filterValue" path="manufacturerName" cssClass="filterValue"/>
                    </td>
                    <td colspan="2" class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="6" path="manufacturerNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="6" path="manufacturerNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="cell label">
                        <br> <span><form:checkbox tabindex="14" cssClass="checkbox" focusable="true" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /></span><br><br>
                    </td>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="cell action">
                        <form:button  id="search" cssClass="button" tabindex="15"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;
                        <form:button  tabindex="16" onclick="${createAction}"><app:message code="admin.global.button.newManufacturer"/></form:button >
                        <br><br><br>
                    </td>
                    <td colspan="2">&nbsp;</td>
                </tr>
            </table>
        </div>
    </form:form>

    <c:if test="${manufacturerFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(manufacturerFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${manufacturerFilterResult.result != null}">

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="8%"/>
                <col width="21%"/>
                <col width="10%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/manufacturerId"><app:message code="admin.global.filter.label.manufacturerId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/manufacturerName"><app:message code="admin.global.filter.label.manufacturerName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.manufacturerStatus" /></a></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="manufacturer" items="${manufacturerFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${manufacturer.manufacturerId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${manufacturer.manufacturerId}"><c:out value="${manufacturer.manufacturerName}"/></a></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${manufacturer.status}" text="${manufacturer.status}"/></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


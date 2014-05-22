<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/distributor/filter"/>
<app:url var="sortUrl" value="/distributor/filter/sortby"/>
<app:url var="editUrl" value="/distributor"/>
<app:url var="reloadUrl" value="/distributor"/>
<app:url var="baseUrl"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>


<div class="search">
        <div class="filter" >
            <form:form modelAttribute="distributorFilter" id="distributorFilterFormId" method="GET" action="${searchUrl}" focus="filterValue">
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.distributorId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="4" focusable="true" id="distributorId" path="distributorId" cssClass="filterValue"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.distributorName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="filterValue" path="distributorName" cssClass="filterValue"/>
                    </td>
                    <td colspan="2" class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="6" path="distributorNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="6" path="distributorNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="3"><label><br><form:checkbox tabindex="14" cssClass="checkbox" focusable="true" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="3">
                        <form:button id="search"  cssClass="button" tabindex="15"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="16" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        <br><br><br>
                    </td>
                </tr>
            </table>
        </div>
    </form:form>

    <c:if test="${distributorFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(distributorFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${distributorFilterResult.result != null}">

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="8%"/>
                <col width="21%"/>
                <col width="17%"/>
                <col width="10%"/>
                <col width="8%"/>
                <col width="8%"/>
                <col width="10%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/distributorId"><app:message code="admin.global.filter.label.distributorId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/distributorName"><app:message code="admin.global.filter.label.distributorName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.streetAddress" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/state"><app:message code="admin.global.filter.label.state" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.postalCode" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.distributorStatus" /></a></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="distributor" items="${distributorFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${distributor.distributorId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${distributor.distributorId}"><c:out value="${distributor.distributorName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${distributor.address1}"/></td>
                    <td class="cell cell-text"><c:out value="${distributor.city}"/></td>
                    <td class="cell cell-text"><c:out value="${distributor.state}"/></td>
                    <td class="cell cell-text"><c:out value="${distributor.postalCode}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.DISTRIBUTOR_STATUS_CD.${distributor.status}" text="${distributor.status}"/></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/account/filter"/>
<app:url var="sortUrl" value="/account/filter/sortby"/>
<app:url var="editUrl" value="/account"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<div class="search">
    <form:form modelAttribute="accountFilter" method="GET" action="${searchUrl}" focus="filterValue">
        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.accountId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="2" focusable="true" id="filterId" path="filterId" cssClass="filterValue"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
               <tr>
                    <td class="cell label first"><label><app:message code="admin.account.label.accountName" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="3" focusable="true" id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell"  style="padding-top: 0;">
                        <form:radiobutton tabindex="4" path="filterType" cssClass="radio"  focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="4" path="filterType" cssClass="radio"  focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td  class="cell label first"><label><app:message code="admin.account.label.distrReferenceNumber" /><span class="colon">:</span></td>
                    <td class="cell value"><form:input tabindex="3" path="distrRefNumber" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell" style="padding-top: 0;">
                        <form:radiobutton path="distrRefNumberFilterType" tabindex="4" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton path="distrRefNumberFilterType" tabindex="5" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="2"><label><br><form:checkbox tabindex="5" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="2" align="left"><form:button  id="search" cssClass="button" tabindex="6"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;<form:button  tabindex="7" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                    </td>
                </tr>
            </table><br><br>
        </div>
    </form:form>

    <c:if test="${accountFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(accountFilterResult.result)}</div>
    </c:if>
    <c:if test="${accountFilterResult.result != null}">

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="8%"/>
                <col width="21%"/>
                <col width="21%"/>
                <col width="13%"/>
                <col width="12%"/>
                <col width="15%"/>
                <col width="10%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/accountId"><app:message code="admin.global.filter.label.accountId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.accountName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/billingAddress1"><app:message code="admin.global.filter.label.billingAddress1" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/billingCity"><app:message code="admin.global.filter.label.billingCity" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/billingState"><app:message code="admin.global.filter.label.billingState" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.global.filter.label.accountType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.accountStatus" /></a></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="account" items="${accountFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${account.accountId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${account.accountId}"><c:out value="${account.accountName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${account.billingAddress1}"/></td>
                    <td class="cell cell-text"><c:out value="${account.billingCity}"/></td>
                    <td class="cell cell-text"><c:out value="${account.billingState}"/></td>
                    <td class="cell cell-text"><c:out value="${account.type}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${account.status}" text="${account.status}"/></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


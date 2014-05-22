<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/location/filter"/>
<app:url var="sortUrl" value="/location/filter/sortby"/>
<app:url var="editUrl" value="/location"/>
<app:url var="baseUrl"/>

<%
	//MANTA-867 - clera the site id field on the create action, to prevent a non-numeric value from
	//causing an exception to be thrown.  None of the values entered into the form are carried over
	//into the location creation functionality so it's safe to just clear this field.  This is the
	//only one that causes a problem, since all other fields are mapped to string values on the form.
%>
<c:set var="createAction" value="$('#siteId').val('');$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearFieldsAction" value="$('form:first').attr('action','${baseUrl}/location/filter/clear');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearLocateAccount" value="$('form:first').attr('action','${baseUrl}/location/filter/clear/account');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('accountFilterInputNames');}"/>

<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account'
                 action="${baseUrl}/locate/account/selected?filter=siteFilter.setFilteredAccounts"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountFilterInputNames"
                 targetIds="accountFilterInputIds"
                 finallyHandler="${finallyHandler}"/>

<div class="search">
    <div>
        <br>
        <div class="subHeader"><app:message code="admin.global.filter.header.searchlocations"/></div>
        <br>
    </div>
        <div class="filter" >
            <table class="locate">
                <tr>
                    <td  class="cell label first locate"><label>
                        <app:message code="admin.global.filter.label.filterByAccounts" /><span class="colon">:</span></label><br>
                        <button tabindex="1" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button><br>
                        <button tabindex="2" onclick="${clearLocateAccount}"><app:message code="admin.global.filter.label.clearAccounts"/></button>
                    </td>
                    <td class="cell locate">
                        <textarea id="accountFilterInputNames"
                                  focusable="true"
                                  tabindex="3"
                                  readonly="true"
                                  class="readonly">${siteFilter.filteredAccountCommaNames}
                        </textarea>
                    </td>
                </tr>
                <tr><td  class="cell label first"></td><td class="cell value"></td></tr>
            </table>
            <form:form modelAttribute="siteFilter" method="GET" action="${searchUrl}" focus="siteName">
                <table>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.siteId" /><span class="colon">:</span></label></td>
                        <td class="cell value"><form:input tabindex="4" id="siteId" path="siteId" focusable="true"/></td>
                        <td class="cell"></td>
                    </tr>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.siteName" /><span class="colon">:</span></label></td>
                        <td class="cell value"><form:input tabindex="5" id="siteName" path="siteName" focusable="true"/></td>
                        <td class="cell" style="padding-top: 0;padding-bottom: 0">
                            <form:radiobutton tabindex="6" focusable="true" path="siteNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton tabindex="6" focusable="true" path="siteNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.siteRefNumber" /><span class="colon">:</span></td>
                        <td class="cell value"><form:input tabindex="7" path="refNumber" focusable="true"/></td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton focusable="true" tabindex="8" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton focusable="true" tabindex="8" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td class="cell"> </td>
                    </tr>
                    <tr>
                        <td  class="cell label first"><label><app:message code="admin.global.filter.label.city" /><span class="colon">:</span></td>
                        <td class="cell value"><form:input tabindex="9" path="city" focusable="true" /></td>
                        <td class="cell"> </td>
                    </tr>
                    <tr>
                        <td  class="cell label first"><label><app:message code="admin.global.filter.label.state" /><span class="colon">:</span></td>
                        <td class="cell value"><form:input tabindex="10" path="state" focusable="true"/></td>
                        <td class="cell"> </td>
                    </tr>
                    <tr>
                        <td  class="cell label first"><label><app:message code="admin.global.filter.label.postalCode" /><span class="colon">:</span></td>
                        <td class="cell value"><form:input tabindex="11" path="postalCode" focusable="true" /></td>
                        <td class="cell"> </td>
                    </tr>
                    <tr>
                        <td class="cell label first"></td>
                        <td class="cell"><label><br><form:checkbox tabindex="12" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                        <td ></td>
                    </tr>
                    <tr>
                        <td class="cell label first"></td>
                        <td colspan="3" class="cell">
                            <form:button  id="search" cssClass="button" tabindex="13"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:button focusable="true" tabindex="14" onclick="${clearFieldsAction}"><app:message code="admin.global.button.clearFields"/></form:button >
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:button focusable="true" tabindex="15" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        </td>

                    </tr>
                </table> <br><br>
                <form:hidden id="accountFilterInputIds" path="accountFilter" value="${siteFilter.filteredAccountCommaIds}"/>
            </form:form>
        </div>

    <c:if test="${siteFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(siteFilterResult.result)}</div>
    </c:if>
    <c:if test="${siteFilterResult.result != null}">

        <table class="searchResult" width="100%">

            <colgroup>
                <col width="8%"/>
                <col width="15%"/>
                <col width="17%"/>
                <col width="17%"/>
                <col width="10%"/>
                <col width="8%"/>
                <col width="8%"/>
                <col width="8%"/>
                <col/>
            </colgroup>

            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/siteId"><app:message code="admin.global.filter.label.siteId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.siteName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.accountName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.streetAddress" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/state"><app:message code="admin.global.filter.label.state" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.postalCode" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.siteStatus" /></a></th>
                <th></th>
            </tr>
            </thead>

            <tbody class="body">

            <c:forEach var="site" items="${siteFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${site.siteId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${site.siteId}"><c:out value="${site.siteName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${site.accountName}"/></td>
                    <td class="cell cell-text"><c:out value="${site.address1}"/></td>
                    <td class="cell cell-text"><c:out value="${site.city}"/></td>
                    <td class="cell cell-text"><c:out value="${site.state}"/></td>
                    <td class="cell cell-text"><c:out value="${site.postalCode}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${site.status}" text="${site.status}"/></td>
                    <td></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


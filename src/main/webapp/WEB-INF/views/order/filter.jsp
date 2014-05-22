<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/order/filter"/>
<app:url var="sortUrl" value="/order/filter/sortby"/>
<app:url var="editUrl" value="/order"/>
<app:url var="reloadUrl" value="/order"/>
<app:url var="baseUrl"/>

<c:set var="clearFilteredAccounts" value="$('form:first').attr('action','${baseUrl}/order/filter/clear/account');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearFilteredDistributors" value="$('form:first').attr('action','${baseUrl}/order/filter/clear/distributor');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearFilteredSites" value="$('form:first').attr('action','${baseUrl}/order/filter/clear/site');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearFilteredUsers" value="$('form:first').attr('action','${baseUrl}/order/filter/clear/user');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandlerAccount" value="function(value) {f_setFocus('accountFilterInputNames');}"/>
<c:set var="finallyHandlerDistributor" value="function(value) {f_setFocus('distributorFilterInputNames');}"/>
<c:set var="finallyHandlerSite" value="function(value) {f_setFocus('siteFilterInputNames');}"/>
<c:set var="finallyHandlerUser" value="function(value) {f_setFocus('userFilterInputNames');}"/>

<app:dateIncludes/>

<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account'
                 action="${baseUrl}/locate/account/selected?filter=orderFilter.setFilteredAccounts"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountFilterInputNames"
                 targetIds="accountFilterInputIds"
                 finallyHandler="${finallyHandlerAccount}"/>
<app:locateLayer var="distrLayer"
                 titleLabel="admin.global.filter.label.locateDistr.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/distributor/multi'
                 action="${baseUrl}/locate/distributor/selected?filter=orderFilter.setFilteredDistributors"
                 idGetter="distributorId"
                 nameGetter="distributorName"
                 targetNames="distributorFilterInputNames"
                 targetIds="distributorFilterInputIds"
                 finallyHandler="${finallyHandlerDistributor}"/>
<app:locateLayer var="siteLayer"
                 titleLabel="admin.global.filter.label.locateSite.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/site/multi'
                 action="${baseUrl}/locate/site/selected?filter=orderFilter.setFilteredSites"
                 idGetter="siteId"
                 nameGetter="siteName"
                 targetNames="siteFilterInputNames"
                 targetIds="siteFilterInputIds"
                 finallyHandler="${finallyHandlerSite}"/>
<app:locateLayer var="userLayer"
                 titleLabel="admin.global.filter.label.locateUser.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/user/multi'
                 action="${baseUrl}/locate/user/selected?filter=orderFilter.setFilteredUsers"
                 idGetter="userId"
                 nameGetter="userName"
                 targetNames="userFilterInputNames"
                 targetIds="userFilterInputIds"
                 finallyHandler="${finallyHandlerUser}" />
<div class="search">
    <div class="filter" >
        <table class="locate">
            <tr>
                <td class="cell label first locate"><label>
                    <app:message code="admin.global.filter.label.filterByAccounts" /><span class="colon">:</span></label><br>
                    <button tabindex="1" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button><br>
                    <button tabindex="2" onclick="${clearFilteredAccounts}"><app:message code="admin.global.filter.label.clearAccounts"/></button>
                </td>
                <td class="cell locate">
                    <textarea id="accountFilterInputNames"
                              focusable="true"
                              tabindex="3"
                              readonly="true"
                              class="readonly">${orderFilter.filteredAccountCommaNames}
                    </textarea>
                </td>
            </tr>
            <tr><td class="cell label first"></td><td class="cell value"></td></tr>
        </table>

        <table class="locate">
            <tr>
                <td class="cell label first locate"><label>
                    <app:message code="admin.global.filter.label.distributors" /><span class="colon">:</span></label><br>
                    <button tabindex="1" onclick="${distrLayer}"><app:message code="admin.global.filter.text.searchDistributors"/></button><br>
                    <button tabindex="2" onclick="${clearFilteredDistributors}"><app:message code="admin.global.filter.text.clearDistributors"/></button>
                </td>
                <td class="cell locate">
                    <textarea id="distributorFilterInputNames"
                              focusable="true"
                              tabindex="3"
                              readonly="true"
                              class="readonly">${orderFilter.filteredDistributorCommaNames}
                    </textarea>
                </td>
            </tr>
            <tr><td class="cell label first"></td><td class="cell value"></td></tr>
        </table>

        <table class="locate">
            <tr>
                <td class="cell label first locate"><label>
                    <app:message code="admin.global.filter.label.sites" /><span class="colon">:</span></label><br>
                    <button tabindex="1" onclick="${siteLayer}"><app:message code="admin.global.filter.text.searchSite"/></button><br>
                    <button tabindex="2" onclick="${clearFilteredSites}"><app:message code="admin.global.filter.text.clearSite"/></button>
                </td>
                <td class="cell locate">
                    <textarea id="siteFilterInputNames"
                              focusable="true"
                              tabindex="3"
                              readonly="true"
                              class="readonly">${orderFilter.filteredSiteCommaNames}
                    </textarea>
                </td>
            </tr>
            <tr><td class="cell label first"></td><td class="cell value"></td></tr>
        </table>
        
        <table class="locate">
            <tr>
                <td class="cell label first locate"><label>
                    <app:message code="admin.global.filter.label.users" /><span class="colon">:</span></label><br>
                    <button tabindex="1" onclick="${userLayer}"><app:message code="admin.global.filter.text.searchUsers"/></button><br>
                    <button tabindex="2" onclick="${clearFilteredUsers}"><app:message code="admin.global.filter.text.clearUsers"/></button>
                </td>
                <td class="cell locate">
                    <textarea id="userFilterInputNames"
                              focusable="true"
                              tabindex="3"
                              readonly="true"
                              class="readonly">${orderFilter.filteredUserCommaNames}
                    </textarea>
                </td>
            </tr>
            <tr><td class="cell label first"></td><td class="cell value"></td></tr>
        </table>

        <%
            //request method changed to POST due to MANTA-625
        %>
        <form:form modelAttribute="orderFilter" id="orderFilterFormId" method="POST" action="${searchUrl}" focus="filterValue">
            <table>
                <tr>
                    <td width="48%" style="vertical-align:top;">
                        <table>
                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.orderFromDate"/><span class="colon">:</span></label></td>
                                <td class="cell value">
                                    <input type="text" style="width: 0; height: 0; top: -1000px; position: absolute;"/>
                                    <form:input tabindex="4" path="orderFromDate" cssClass="datepicker2Col standardCal standardActiveCal"/>
                                </td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.orderToDate"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="5" path="orderToDate" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.outboundPONum"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="6" id="filterValue" path="outboundPONum" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.customerPONum"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="7" path="customerPONum" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.erpPONum"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="8" path="erpPONum" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.webOrderNum"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="9" path="webOrderConfirmationNum" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.refOrderNum"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="11" path="refOrderNum" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.locationPostalCode"/><span class="colon">:</span></label></td>
                                <td class="cell value"><form:input tabindex="12" path="siteZipCode" cssClass="filterValue" maxlength="255"/></td>
                            </tr>

                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.orderMethod"/><span class="colon">:</span></label></td>
                                <td class="cell value">
                                    <form:select tabindex="13" path="orderMethod" focusable="true">
                                        <form:option value=""><app:message code="admin.global.select"/></form:option>
                                        <c:forEach var="source" items="${orderFilter.orderSources}">
                                            <form:option value="${source.object1}">${source.object2}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="4%">&nbsp;</td>
                    <td width="48%">
                        <table cellspacing="0" cellpadding="0" border="0">
                            <tr>
                                <td class="cell label first"><label><app:message code="admin.order.label.orderStatus"/><span class="colon">:</span></label></td>
                                <td class="cell value" nowrap>
                                    <a href="javascript:checkAll('orderFilterFormId', 'searchOrderStatuses.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                                    <a href="javascript:checkAll('orderFilterFormId', 'searchOrderStatuses.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                                </td>
                            </tr>
                            <c:forEach var="obj" varStatus="i" items="${orderFilter.searchOrderStatuses.values}">
                                <tr class="row">
                                    <td class="cell value"><label>${obj.object2}</label></td>
                                    <td class="cell cell-element" align="center"><form:checkbox cssClass="checkbox" path="searchOrderStatuses.selectableObjects[${i.index}].selected"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <form:button id="search"  cssClass="button" tabindex="14"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table>
    
            <form:hidden id="accountFilterInputIds" path="accountFilter" value="${orderFilter.filteredAccountCommaIds}"/>
            <form:hidden id="distributorFilterInputIds" path="distributorFilter" value="${orderFilter.filteredDistributorCommaIds}"/>
            <form:hidden id="userFilterInputIds" path="userFilter" value="${orderFilter.filteredUserCommaIds}"/>
            <form:hidden id="siteFilterInputIds" path="siteFilter" value="${orderFilter.filteredSiteCommaIds}"/>
        </form:form>
    </div>
    <c:if test="${orderFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(orderFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${orderFilterResult.result != null}">
        <table class="searchResult" width="100%">

            <thead class="header">
                <tr class="row">
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.orderAccountName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/distName"><app:message code="admin.global.filter.label.orderDistributorName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/webOrderNum"><app:message code="admin.global.filter.label.orderWebOrderNum" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderDate"><app:message code="admin.global.filter.label.orderOrderDate" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/customerPoNum"><app:message code="admin.global.filter.label.orderCustomerPO" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.orderLocationName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.orderAddress" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.orderCity" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/stateProvince"><app:message code="admin.global.filter.label.orderState" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.orderPostalCode" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderStatus"><app:message code="admin.global.filter.label.orderStatus" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderSource"><app:message code="admin.global.filter.label.orderMethod" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/placedBy"><app:message code="admin.global.filter.label.orderPlacedBy" /></a></th>
                </tr>
            </thead>

            <tbody class="body">
                <c:forEach var="order" items="${orderFilterResult.result}" >
                    <tr class="row">
                        <td class="cell cell-text"><c:out value="${order.accountName}"/></td>
                        <td class="cell cell-text"><c:out value="${order.distName}"/></td>
                        <td class="cell cell-text"><a href="${editUrl}/${order.orderId}"><c:out value="${order.webOrderNum}"/></a></td>
                        <td class="cell cell-date"><app:formatDate value="${order.orderDate}"/></td>
                        <td class="cell cell-text"><c:out value="${order.customerPoNum}"/></td>
                        <td class="cell cell-text"><c:out value="${order.siteName}"/></td>
                        <td class="cell cell-text"><c:out value="${order.address1}"/></td>
                        <td class="cell cell-text"><c:out value="${order.city}"/></td>
                        <td class="cell cell-text"><c:out value="${order.stateProvince}"/></td>
                        <td class="cell cell-text"><c:out value="${order.postalCode}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.ORDER_STATUS_CD.${order.orderStatus}" text="${order.orderStatus}"/></td>
                        <td class="cell cell-text"><c:out value="${order.orderSource}"/></td>
                        <td class="cell cell-text"><c:out value="${order.placedBy}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>


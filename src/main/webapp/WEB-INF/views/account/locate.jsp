<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/account/filter"/>
<app:url var="sortUrl" value="/locate/account/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateAccountFilterResultId', 'accountLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateAccountFilter" id="locateAccountFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

        <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first">
                                <label><app:message code="admin.global.filter.label.accountId" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">
                                <form:input tabindex="1" id="accountId" path="accountId"/>
                            </td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.accountName" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="accountName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="accountNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="4" path="accountNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td  class="cell label first"><label><app:message code="admin.global.filter.label.distrRefNumber" /><span class="colon">:</span></td>
                            <td class="cell value" style="padding-top: 0;padding-bottom: 5px;"><form:input tabindex="5" path="distrRefNumber"/></td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                            <c:if test="${not empty locateAccountFilter.accountsGroups}">
                                <tr>
                                    <td  class="cell label first"><label><app:message code="admin.global.filter.label.groups" /><span class="colon">:</span></td>
                                    <td class="cell value" colspan="3">  <form:select tabindex="6" path="filteredAccountsGroups">
                                        <form:option value=""><app:message code="admin.global.none"/></form:option>
                                        <c:forEach var="obj" items="${locateAccountFilter.accountsGroups}">
                                            <form:option value="${obj.object1}"><c:out value="${obj.object2}"/></form:option>
                                        </c:forEach>
                                    </form:select></td>
                                </tr>
                            </c:if>
                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <span>
                                    <form:checkbox tabindex="7" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" />
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
                                <form:button  id="search"  cssClass="button" tabindex="8"  onclick="$('form:first').submit(); return false;">
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

    <c:if test="${locateAccountFilterResult.result != null}">
        <hr style="margin-right: 15px;"/>
        <table class="searchResult" width="95%" >
            <tr>
                <td class="resultCount label">
                    <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value"></label>${fn:length(locateAccountFilterResult.result)}
                </td>
                <td align="right" style="padding:0;vertical-align: top">
                    <form:button style="margin-top:-5px" cssClass="button" tabindex="9" onclick="return ${doReturnSelected}">
                        <app:message code="admin.global.button.returnSelected"/>
                    </form:button>
                </td>
            </tr>
        </table>
    </c:if>

    </form:form>

    <form:form id="locateAccountFilterResultId" modelAttribute="locateAccountFilterResult" method="POST">
        <c:if test="${locateAccountFilterResult.result != null}">

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
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/accountId"><app:message code="admin.global.filter.label.accountId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.accountName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/billingCity"><app:message code="admin.global.filter.label.billingCity" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/billingState"><app:message code="admin.global.filter.label.billingState" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.global.filter.label.accountType" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.accountStatus" /></a></th>
                    <th class="cell cell-text cell-element">
                        <a href="javascript:checkAll('locateAccountFilterResultId', 'selectedAccounts.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('locateAccountFilterResultId', 'selectedAccounts.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="account" items="${locateAccountFilterResult.result}" varStatus="i">
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${account.value.accountId}"/></td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateAccountFilterResultId', 'selectedAccounts.selectableObjects','account_${account.value.accountId}');return ${doReturnSelected}"><c:out value="${account.value.accountName}"/></a></td>
                        <td class="cell cell-text"><c:out value="${account.value.billingCity}"/></td>
                        <td class="cell cell-text"><c:out value="${account.value.billingState}"/></td>
                        <td class="cell cell-text"><c:out value="${account.value.type}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${account.value.status}" text="${account.value.status}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="account_${account.value.accountId}" path="selectedAccounts.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>

                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


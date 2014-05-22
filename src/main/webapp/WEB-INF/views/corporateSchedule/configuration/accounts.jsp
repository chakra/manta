<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/corporateSchedule/${corporateScheduleHeader.id}/account"/>

<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<app:url var="accountEditUrl" value="/account"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#corporateScheduleAccounts').submit(); return false;"/>
<c:set var="updateAllAction" value="$('form:#search').attr('action','${baseUrl}/update/all');$('form:#search').attr('method','POST');$('form:#search').submit(); return false;"/>

<div class="canvas">

    <div class="search">

        <form:form id="search" modelAttribute="corporateScheduleAccountFilter" method="GET" action="${searchUrl}" focus="accountName">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                        <td colspan="2">
                            <div class="subHeader"><app:message code="admin.user.configuration.account.accountAssociations"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="2">&nbsp;</td></tr>
                    <tr>
                        <td class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                        <td class="cell label">
                            <label><app:message code="admin.global.filter.label.accountId" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="1" id="accountId" path="accountId" cssClass="filterValue"/>
                        </td>
                        <td width="15%" class="cell label">&nbsp;</td>
                        <td rowspan="4" style="vertical-align: top;">
                            <table width="100%">
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="box">
                                            <div class="boxTop">
                                                <div class="topWrapper"><span class="left">&nbsp;</span>
                                                    <span class="center" style=""><span class="boxName"><app:message code="admin.user.label.actions"/></span></span>
                                                    <span class="right">&nbsp;</span>
                                                </div>
                                            </div>
                                            <div class="boxMiddle">
                                                <div class="middleWrapper">
                                                    <span class="left">&nbsp;</span>
                                                    <div class="boxContent">
                                                        <table cellpadding="0" cellspacing="0" border="0">
                                                            <tbody>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <form:checkbox tabindex="7" cssClass="checkbox"  path="associateAllAccounts"/>
                                                                        <label><app:message code="admin.user.configuration.account.associateAllAccounts" /></label>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <form:checkbox tabindex="8" cssClass="checkbox"  path="associateAllSites"/>
                                                                        <label><app:message code="admin.user.configuration.account.associateAllSitesOfAccounts" /></label>
                                                                    </td>
                                                                </tr>
                                                                </tr><td>&nbsp;</td></tr>
                                                                <tr>
                                                                    <td align="center">
                                                                        <form:button style="display:none;" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                                                                        <form:button tabindex="9" onclick="${updateAllAction}"><app:message code="admin.global.button.save" /></form:button>
                                                                    </td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <span class="right">&nbsp;</span></div>
                                            </div>
                                            <div class="boxBottom">
                                                <div class="bottomWrapper"><span class="left">&nbsp;</span>
                                                    <span class="center">&nbsp;</span>
                                                    <span class="right">&nbsp;</span>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.global.filter.label.accountName" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="2" id="accountName" path="accountName" cssClass="filterValue"/>
                        </td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton tabindex="3" path="accountNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                            <br>
                            <form:radiobutton tabindex="3" path="accountNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="4" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.user.configuration.account.associatedOnly" /></label>
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
                    <tr><td colspan="2">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:button  tabindex="6" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        </td>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="corporateScheduleAccounts" modelAttribute="corporateScheduleAccountFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${corporateScheduleAccountFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(corporateScheduleAccountFilterResult.result)}</div>
                        </td>
                        <td align="right">
                            <form:button tabindex="3" onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td width="30%" align="right">
                            <form:checkbox tabindex="3" cssClass="checkbox"  path="removeSitesWithAccounts"/>&nbsp;
                            <label><app:message code="admin.user.configuration.account.removeSitesOfAccounts" /></label>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${corporateScheduleAccountFilterResult.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="10%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="30%"/>
                    </colgroup>
                    <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/busEntityId"><app:message code="admin.global.filter.label.accountId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/shortDesc"><app:message code="admin.global.filter.label.accountName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/busEntityStatusCd"><app:message code="admin.global.filter.label.accountStatus" /></a></th>
                        <th class="cell cell-text cell-element" nowrap>
                            <a href="javascript:checkAll('corporateScheduleAccounts', 'accounts.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('corporateScheduleAccounts', 'accounts.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody class="body">
                    <c:forEach var="account" varStatus="i" items="${corporateScheduleAccountFilterResult.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${account.value.busEntityId}"/> </td>
                            <td class="cell cell-text"><a href="${accountEditUrl}/${account.value.busEntityId}"><c:out value="${account.value.shortDesc}"/></a> </td>

                            <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${account.value.busEntityStatusCd}" text="${account.value.busEntityStatusCd}"/></td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="accounts.selectableObjects[${i.index}].selected"/></td>
                            <td>&nbsp;</td>
                        </tr>
                    </c:forEach>
                    <tr><td colspan="5"><br><br></td></tr>
                    <tr>
                        <td colspan="5">&nbsp;</td>
                        <td align="right"><form:button tabindex="3" onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button></td>
                    </tr>
                    </tbody>

                </table>
            </c:if>
        </form:form>

</div>
</div>


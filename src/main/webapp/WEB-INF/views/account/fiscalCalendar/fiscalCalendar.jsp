<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/account/${accountFiscalCalendar.accountId}"/>

<c:set var="fiscalCalendaridToEdit" value="${(empty  accountFiscalCalendar.calendarToEdit.fiscalCalendarId ? 0 : accountFiscalCalendar.calendarToEdit.fiscalCalendarId)}"/>
<c:set var="createAction" value="$('form:first').attr('action','${baseUrl}/fiscalCalendar/create');$('form:first').submit(); return false; "/>
<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/fiscalCalendar/${fiscalCalendaridToEdit}/save');$('form:first').submit(); return false; "/>
<c:set var="setForAllAction" value="$('form:first').attr('action','${baseUrl}/fiscalCalendar/${fiscalCalendaridToEdit}/setForAll');$('form:first').submit(); return false; "/>
<c:set var="cloneAction" value="$('form:first').attr('action','${baseUrl}/fiscalCalendar/${fiscalCalendaridToEdit}/clone');$('form:first').submit(); return false; "/>
<c:set var="copyBudgetAction" value="$('form:first').attr('action','${baseUrl}/fiscalCalendar/copyBudget');$('form:first').submit(); return false; "/>


<c:set var="readonly"  value="${!accountFiscalCalendar.calendarToEdit.isEditable}"/>
<c:set var="readonlyCss" value="${(readonly == true ? 'readonly':'')}"/>

<div class="canvas">
    <form:form modelAttribute="accountFiscalCalendar" action="" method="POST">

        <div class="search">
            <br>
            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="7%"/>
                    <col width="13%"/>
                    <col width="4%"/>
                    <col width="13%"/>
                    <col width="13%"/>
                    <col width="50%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">

                    <th class="cell cell-number"><app:message code="admin.account.fiscalCalendar.label.fiscalYear" /></th>
                    <th class="cell cell-number"><app:message code="admin.account.fiscalCalendar.label.numberOfPeriods" /></th>
                    <th></th>
                    <th class="cell cell-date"><app:message code="admin.account.fiscalCalendar.label.effDate" /></th>
                        <th class="cell cell-date">
                            <c:if test="${accountFiscalCalendar.isServiceScheduleCalendar}">
                                <app:message code="admin.account.fiscalCalendar.label.expDate" />
                            </c:if>
                        </th>
                    <th></th>
                </tr>

                </thead>

                <tbody class="body">
                <c:forEach var="calendar" items="${accountFiscalCalendar.calendars}">

                    <tr class="row">
                        <td class="cell cell-number">
                            <a href="${baseUrl}/fiscalCalendar/${calendar.fiscalCalendarId}">
                                <c:choose>
                                    <c:when test="${calendar.fiscalYear==0}"><app:message code="admin.account.fiscalCalendar.all"/></c:when>
                                    <c:otherwise><c:out value="${calendar.fiscalYear}"/></c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td class="cell cell-number"><c:out value="${calendar.numOfPeriods}"/></td>
                        <td></td>
                        <td class="cell cell-date"><app:formatDate value="${calendar.effDate}"/></td>
                        <td class="cell cell-date">
                            <c:if test="${accountFiscalCalendar.isServiceScheduleCalendar}">
                                <app:formatDate value="${calendar.expDate}"/>
                            </c:if>
                        </td>
                        <td class="cell cell-date">
                            <c:if test="${(calendar.fiscalYear == accountFiscalCalendar.currentFiscalYear) || (fn:length(accountFiscalCalendar.calendars) == 1 && calendar.fiscalYear == 0)}">
                                <form:button tabindex="1" disabled="${!accountFiscalCalendar.showCopyBudget}" onclick="${copyBudgetAction}"><app:message code="admin.account.fiscalCalendar.button.copyBudgetForward"/></form:button>
                            </c:if>
                        </td>
                    </tr>

                </c:forEach>
                <tr>
                    <td colspan="5"><br>
                        <hr class="compact-bottom"/>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="5" valign="top" align="right" style="white-space: nowrap;">
                        <div class="label"><label><app:message code="admin.account.fiscalCalendar.label.numberOfPeriods"/><span class="colon">:</span><span class="reqind">*</span></label>
                            &nbsp;&nbsp;&nbsp;<form:input path="numberOfBudgetPeriods" size="5" onkeypress="$('form:first').attr('action','${baseUrl}/fiscalCalendar/create');"/> &nbsp;&nbsp;&nbsp; <form:button onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button></div>
                    </td>
                    <td></td>
                </tr>
                </tbody>
            </table>
            <br><br>

        </div>


       <c:if test="${ accountFiscalCalendar.isNew || accountFiscalCalendar.calendarToEdit.fiscalCalendarId > 0}">

        <c:set var="periodNumbers" value="${fn:length(accountFiscalCalendar.calendarToEdit.periodNumbers) - (accountFiscalCalendar.isNew ? 0 : accountFiscalCalendar.calendarToEdit.isServiceScheduleCalendar ? 1 : 0)}"/>
        <c:set var="periodRowSize" value="${(fn:length(accountFiscalCalendar.calendarToEdit.periodNumbers)<=13) ? 13 : 12 }"/>


           <div class="details">


            <app:box name="admin.account.fiscalCalendar.panel.label">
                <table width="100%">

                    <tr>
                        <td>
                            <table  width="100%">

                                <tbody>


                                <tr>

                                    <td width="120px"  nowrap="nowrap">
                                        <form:label path="calendarToEdit.fiscalYear"><app:message code="admin.account.fiscalCalendar.label.fiscalYear"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                        <form:hidden path="calendarToEdit.fiscalCalendarId"/>
                                        <form:hidden path="calendarToEdit.editable"/>
                                        <form:hidden path="calendarToEdit.serviceScheduleCalendar"/>
                                    </td>

                                    <td class="labelValue"  style="padding-top: 0">

                                        <table width="60%">

                                            <colgroup>
                                                <col width="15%"/>
                                                <col width="15%"/>
                                                <col width="10%"/>
                                                <col width="10%"/>
                                                <col width="20%"/>
                                                <col width="10%"/>
                                                <col width="20%"/>
                                            </colgroup>

                                            <tbody>
                                            <tr>

                                                <td  class="labelValue">
                                                    <app:message var="yearAll" code="admin.account.fiscalCalendar.all"/>
                                                    <c:set var="year" value="${accountFiscalCalendar.calendarToEdit.fiscalYear=='0'?yearAll:accountFiscalCalendar.calendarToEdit.fiscalYear}"/>
                                                    <form:input  tabindex="1" size="4" path="calendarToEdit.fiscalYear" cssClass="${readonlyCss}" readonly="${readonly}" value="${year}"/>
                                                </td>

                                                <td><div class="label"><label><app:message code="admin.account.fiscalCalendar.label.numberOfPeriods"/></label><span class="colon">:</span></div></td>
                                                <td  class="labelValue"><c:out value="${periodNumbers}"/><form:hidden path="calendarToEdit.periodCd"/></td>

                                                <td><div class="label"><form:label path="calendarToEdit.effDate"><app:message code="admin.account.fiscalCalendar.label.effDate"/><span class="colon">:</span></form:label><span class="reqind">*</span></div><div class="label">(<app:datePrompt/>)</div></td>
                                                <td class="labelValue"><form:input tabindex="2" cssClass="${readonlyCss}" readonly="${readonly}" size="10" path="calendarToEdit.effDate"/> </td>
                                                <c:if test="${accountFiscalCalendar.calendarToEdit.isServiceScheduleCalendar}">
                                                    <td><div class="label"><form:label path="calendarToEdit.expDate"><app:message code="admin.account.fiscalCalendar.label.expDate"/><span class="colon">:</span></form:label><span class="reqind">*</span></div><div class="label">(<app:datePrompt/>)</div></td>
                                                    <td class="labelValue"><form:input tabindex="3" cssClass="${readonlyCss}" readonly="${readonly}" size="10" path="calendarToEdit.expDate"/></td>
                                                </c:if>
                                            </tr>
                                            </tbody>

                                        </table>
                                    </td>
                                </tr>


                                <tr>
                                    <td colspan="2" style="padding: 0">

                                        <table>

                                            <c:set var="rows" value="${(periodNumbers/periodRowSize + (periodNumbers%periodRowSize > 0 ? 1 : 0))}"/>

                                            <c:if test="${rows>0}">

                                                <c:forEach var="i" begin="0" end="${rows>=1 ? rows - 1 : 0}" step="1">

                                                <c:set var="lineBegin" value="${periodRowSize * i}" />
                                                <c:set var="lineEnd" value="${((periodRowSize * i) + periodRowSize > periodNumbers ? periodNumbers:(periodRowSize*i) + periodRowSize)}" />
                                                <c:set var="lineEnd" value="${lineEnd < 0 ? 0 :lineEnd }"/>

                                                    <c:if test="${lineEnd>0}">
                                                    <tbody>

                                            <tr>


                                                <c:forEach var="periodNum"
                                                           begin="${lineBegin}"
                                                           end="${lineEnd>=1 ? lineEnd - 1 : 0}"
                                                           items="${accountFiscalCalendar.calendarToEdit.periodNumbers}"
                                                           varStatus="j">
                                                    <td></td>
                                                    <td class="labelValue"><div class="label" style="text-align: center"><label><c:out value="${periodNum}"/></label></div></td>
                                                </c:forEach>

                                            </tr>

                                            <tr>

                                                <c:forEach var="periodNum"
                                                           begin="${periodRowSize*i}"
                                                           end="${lineEnd>=1 ? lineEnd - 1 : 0}"
                                                           items="${accountFiscalCalendar.calendarToEdit.periodNumbers}"
                                                           varStatus="j">

                                                    <c:if test="${i == 0 && j.index == 0}">
                                                        <td  width="120px"><div class="label">
                                                            <app:message code="admin.account.fiscalCalendar.label.periodStartDate"/>
                                                            <span class="colon">:</span><span class="reqind">*</span>
                                                            <div>(<app:dayMonthPrompt/>)</div>
                                                        </div>
                                                        </td>
                                                    </c:if>

                                                    <c:if test="${i != 0 || j.index !=0}"><td></td></c:if>

                                                    <td class="labelValue">  <form:input tabindex="${3+periodNum}" size="4" path="calendarToEdit.periods[${periodNum}]" cssClass="${readonlyCss}" readonly="${readonly}"/></td>

                                                </c:forEach>

                                            </tr>

                                            </tbody>
                                                </c:if>

                                            </c:forEach>

                                            </c:if>

                                            <c:if test="${!accountFiscalCalendar.isNew && accountFiscalCalendar.calendarToEdit.isServiceScheduleCalendar}"> <form:hidden path="calendarToEdit.periods[${periodNumbers + 1}]"/> </c:if>

                                            <tbody>
                                            <tr>
                                                <td colspan="${ 2 * periodRowSize}" align="center">&nbsp;</td>
                                            </tr>

                                            </tbody>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" colspan="2">
                                        <div  style="padding-right: 150px;">
                                            <form:button tabindex="${3+periodNumbers+2}"   disabled="${readonly}" onclick="${updateAction}"><app:message code="admin.global.button.save"/></form:button>
                                            <c:if test="${accountFiscalCalendar.calendarToEdit.fiscalCalendarId > 0}">&nbsp;&nbsp;&nbsp;
                                            <form:button tabindex="${3+periodNumbers+3}" onclick="${cloneAction}"><app:message code="admin.global.button.clone"/></form:button>
                                            </c:if>
                                            &nbsp;&nbsp;&nbsp;
                                            <form:button tabindex="${3+periodNumbers+4}"   disabled="${readonly}" onclick="${setForAllAction}"><app:message code="admin.account.fiscalCalendar.button.setForAll"/></form:button>
                                        </div>
                                    </td>
                                </tr>

                                </tbody>

                            </table>
                        </td>
                    </tr>

                </table>
            </app:box>



        </div>
       </c:if>
    </form:form>
</div>


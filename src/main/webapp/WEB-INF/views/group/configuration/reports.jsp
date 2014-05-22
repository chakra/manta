<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="storebbaseUrl"/>
<app:url var="baseUrl" value="/group/${groupHeader.groupId}/report"/>
<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateAction" value="$('form:#groupReportFilterResult').submit(); return false;"/>

<div class="canvas">


<div class="search">
    <form:form id="groupReportFilter" modelAttribute="groupReportFilter" method="GET" action="${searchUrl}" focus="reportName">
        <div class="filter">
            <table>
                <tbody>
                <tr>
                    <td colspan="5">
                        <div class="subHeader"><app:message code="admin.group.configuration.label.reports"/><span class="colon">:</span></div>
                    </td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>
                <tr>
                    <td width="70px">&nbsp;</td>
                    <td>
                        <label><app:message code="admin.group.configuration.label.reportId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <form:input tabindex="1" id="reportId" path="reportId" cssClass="filterValue"/>
                    </td>
                    <td>&nbsp;</td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td >
                        <label><app:message code="admin.group.configuration.label.reportName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <form:input tabindex="2" id="reportName" path="reportName" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td style="padding-top: 0; text-align: left">
                        <form:radiobutton tabindex="3" path="reportNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                        <form:radiobutton tabindex="3" path="reportNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="3" style="text-align:left; ">
                        <br>
                        <form:checkbox tabindex="4"  cssStyle="margin-left:0"  path="showConfiguredOnly"/>&nbsp;
                        <label><app:message code="admin.global.filter.label.showConfiguredOnly" /></label>
                    </td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>
                <tr>
                    <td  class="cell action" colspan="2">&nbsp;</td>
                    <td class="cell">
                        <form:button id="search"   tabindex="5"><app:message code="admin.global.button.search"/></form:button>
                        <br>
                        <br>
                        <br>
                    </td>
                    <td colspan="3">&nbsp;</td>
                </tr>
                </tbody>
            </table>
        </div>
    </form:form>
    <form:form id="groupReportFilterResult" modelAttribute="groupReportFilterResult" method="POST" action="${updateUrl}">
    <c:if test="${groupReportFilterResult.result != null}">
        <hr/>
        <table width="100%">
            <tr>
                <td align="left">
                    <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value">${fn:length(groupReportFilterResult.result)}</label></div>
                </td>
                <td align="right">
                <c:if test="${fn:length(groupReportFilterResult.result) > 0}">
                    <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
        <table class="searchResult" width="100%">
            <colgroup>
            	<col width="5%"/>
                <col width="10%"/>
                <col width="25%"/>
                <col width="60%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-text cell-element">
                    <a href="javascript:checkAll('reportSelectedResultId', 'groupReports.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                    <a href="javascript:checkAll('reportSelectedResultId', 'groupReports.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/reportId"><app:message code="admin.group.configuration.label.reportId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/reportName"><app:message code="admin.group.configuration.label.reportName" /></a></th>
            </tr>
            </thead>

            <tbody class="body">
            <c:forEach var="report" varStatus="i" items="${groupReportFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-element">
                      <form:checkbox cssClass="checkbox" id="report_${report.value.reportId}" path="groupReports.selectableObjects[${i.index}].selected"/>
                    </td>
                    <td class="cell cell-number"><c:out value="${report.value.reportId}"/> </td>
                    <td class="cell cell-text"><c:out value="${report.value.reportName}"/> </td>
                </tr>
            </c:forEach>
            <tr><td colspan="9"><br><br></td></tr>
            <tr>
                <td colspan="9">&nbsp;</td>
                <td align="right">
                <c:if test="${fn:length(groupReportFilterResult.result) > 0}">
            		<form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
        		</c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </c:if>
    </form:form>
</div>
</div>

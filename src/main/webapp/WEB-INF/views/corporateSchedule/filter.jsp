<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/corporateSchedule/filter"/>
<app:url var="sortUrl" value="/corporateSchedule/filter/sortby"/>
<app:url var="editUrl" value="/corporateSchedule"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<app:dateIncludes/>

<div class="search">
    <form:form modelAttribute="corporateScheduleFilter" method="GET" action="${searchUrl}" focus="filterValue">
        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.corporateSchedule.label.corporateScheduleId" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="2" focusable="true" id="filterId" path="filterId" cssClass="filterValue"/></td>
					<td colspan="2" class="cell">&nbsp;</td>
                </tr>

                 <tr>
                    <td class="cell label first"><label><app:message code="admin.corporateSchedule.label.corporateScheduleName" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input  tabindex="3" focusable="true" id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell">
                    	<form:radiobutton   tabindex="4" path="filterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton   tabindex="4" path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.corporateSchedule.label.dateFrom" /><span class="colon">:</span></td>
                    <td class="cell value"><form:input  tabindex="5" path="corporateScheduleDateFrom" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                 <tr>
                    <td class="cell label first"><label><app:message code="admin.corporateSchedule.label.dateTo" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input  tabindex="6" path="corporateScheduleDateTo" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr> <td >&nbsp;</td>  <td ></td>&nbsp; </tr>
                <br><br>
                <tr>
					<td class="cell label first"></td>
                    <td class="cell" align="left" colspan="3"><form:button  tabindex="7" id="search"  cssClass="button"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;<form:button tabindex="8" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
					</td>
                </tr>
            </table><br><br> </div>
    </form:form>

    <c:if test="${corporateScheduleFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(corporateScheduleFilterResult.result)}</div>
    </c:if>
    <c:if test="${corporateScheduleFilterResult.result != null}">

        <table class="searchResult" width="100%">
        
             <colgroup>
                <col width="10%"/>
                <col width="45%"/>
                <col width="35%"/>
                <col width="10%"/>
  	        </colgroup>
            <thead class="header">
	            <tr class="row">
	                <th class="cell cell-number"><a class="sort" href="${sortUrl}/scheduleId"><app:message code="admin.corporateSchedule.label.scheduleId" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/scheduleName"><app:message code="admin.corporateSchedule.label.corporateScheduleName" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/nextDeliveryDate"><app:message code="admin.corporateSchedule.label.scheduleDate" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/scheduleStatus"><app:message code="admin.global.filter.label.status" /></a></th>
	  			</tr>
   			</thead>
  			
            <tbody class="body">
            <c:forEach var="schedule" items="${corporateScheduleFilterResult.result}" >

                <tr class="row">
                    <td class="cell cell-number"><c:out value="${schedule.scheduleId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${schedule.scheduleId}"><c:out value="${schedule.scheduleName}"/></a></td>
					<%
						//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
						//String nextDel = (schedule.getNextDelivery() == null) ? "&nbsp;": sdf.format(schedule.getNextDelivery());
					%>
<%-- 					<c:set var="nextDel" value="${(schedule.nextDelivery == null ? ' ': AppI18nUtil.parseDateNN(locale, schedule.nextDelivery) )}"/> --%>

                    <td class="cell cell-text"><app:formatDate value="${schedule.nextDeliveryDate}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.CORPORATE_SCHEDULE_STATUS_CD.${schedule.scheduleStatus}"/></td>
               </tr>
            </c:forEach>

            </tbody>

        </table>   </c:if>

 </div>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.alert.AppLocale" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ page import="com.espendwise.manta.util.format.AppI18nFormatter" %>
<%@ page import="com.espendwise.manta.history.HistoryRecord" %>

<app:url var="historyUrl" value="/history/filter"/>
<app:url var="sortUrl" value="/history/filter/sortby"/>
<app:url var="excelExportUrl" value="/history/filter/excelExport"/>

<app:dateIncludes/>

<script language="JavaScript">
	function viewExcelFormat() {
		var location = "${excelExportUrl}";
		//JEE TO DO - figure out how to avoid manually adding the request parameters
		location = location + "?objectId=";
		var	objectId = document.getElementById('objectId').value;
		if (objectId != null && objectId.length > 0) {
			location = location + objectId;
		}
		location = location + "&objectType=";
		var	objectType = document.getElementById('objectType').value;
		if (objectType != null && objectType.length > 0) {
			location = location + objectType;
		}
		location = location + "&transactionType=";
		var	transactionType = document.getElementById('transactionType').value;
		if (transactionType != null && transactionType.length > 0) {
			location = location + transactionType;
		}
		location = location + "&startDate=";
		var	startDate = document.getElementById('startDate').value;
		if (startDate != null && startDate.length > 0) {
			location = location + startDate;
		}
		location = location + "&endDate=";
		var	endDate = document.getElementById('endDate').value;
		if (endDate != null && endDate.length > 0) {
			location = location + endDate;
		}
		prtwin = window.open(location,"excelExport",
	    "menubar=yes,resizable=yes,scrollbars=yes,toolbar=yes,status=yes,height=500, width=775,left=100,top=165");
	  	prtwin.focus();
	}
</script>


<div class="search">
	<form:form modelAttribute="historyFilter" id="historyFilterFormId" method="GET" action="${historyUrl}" focus="filterValue">
        <div class="filter" >
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.objectId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="1" focusable="true" id="objectId" path="objectId" cssClass="filterValue"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.objectType" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                    	<form:select tabindex="2" cssClass="std" id="objectType" path="objectType">
                        	<form:option value="">
                            	<app:message code="admin.global.select"/>
                            </form:option>
                        		<app:i18nRefCodes var="code" items="${historyFilter.objectTypeChoices}" i18nprefix="">
                            		<form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        		</app:i18nRefCodes>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="history.label.shortDescription" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                    	<form:select tabindex="3" cssClass="std" id="transactionType" path="transactionType">
                        	<form:option value="">
                            	<app:message code="admin.global.select"/>
                            </form:option>
                        	<app:i18nRefCodes var="code" items="${historyFilter.transactionTypeChoices}" i18nprefix="">
                            	<form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        	</app:i18nRefCodes>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.global.filter.label.startDate" /><span class="colon">:</span></td>
                    <td class="cell value"><form:input tabindex="4" path="startDate" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                 <tr>
                    <td class="cell label first"><label><app:message code="admin.global.filter.label.endDate" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="5" path="endDate" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="3">
                        <form:button  id="search" cssClass="button" tabindex="6"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                    </td>
                </tr>
            </table><br><br>
        </div>
	</form:form>
	<form:form modelAttribute="historyFilter" id="historyFilterFormId" method="POST" action="${historyUrl}" focus="filterValue">
    	<c:if test="${historyFilterResult.result != null}">
        	<hr/>
        	<table width="100%">
        		<tr>
					<td>
		        		<div class="resultCount label">
        		    		<label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            				<label class="value"></label>${fn:length(historyFilterResult.result)}
        				</div>
        			</td>
        			<td align="right">
        				<form:button cssClass="button" tabindex="7"  onclick="javascript:viewExcelFormat(); return false;">
        					<app:message code="admin.global.button.exportExcel"/>
        				</form:button>
        			</td>
        		</tr>
        	</table>

        	<table class="searchResult" width="100%">
            	<colgroup>
                	<col width="20%"/>
                	<col width="10%"/>
                	<col width="20%"/>
                	<col width="50%"/>
            	</colgroup>
            	<thead class="header">
            		<tr class="row">
                		<th class="cell cell-text"><a class="sort" href="${sortUrl}/activityDate"><app:message code="history.label.activityDate" /></a></th>
                		<th class="cell cell-text"><a class="sort" href="${sortUrl}/userId"><app:message code="history.label.userId" /></a></th>
                		<th class="cell cell-text"><a class="sort" href="${sortUrl}/historyTypeCd"><app:message code="history.label.shortDescription" /></a></th>
                		<th class="cell cell-text"><app:message code="history.label.longDescription" /></th>
            		</tr>
            	</thead>
            	<tbody class="body">
            	<%
            		AppI18nFormatter formatter = new AppI18nFormatter(new AppLocale(Auth.getAppUser().getLocale()));
            	%>
            		<c:forEach var="history" items="${historyFilterResult.result}" >
                		<tr class="row">
                		<%
                			HistoryRecord record = (HistoryRecord) pageContext.getAttribute("history");
                		%>
                    		<td class="cell cell-text"><%=formatter.formatDate(record.getActivityDate()) + "&nbsp;" + formatter.formatTime(record.getActivityDate()) %></td>
                    		<td class="cell cell-text"><c:out value="${history.userId}"/></td>
                    		<td class="cell cell-text"><c:out value="${history.shortDescription}"/></td>
                    		<td class="cell cell-text">${history.longDescriptionAsHtml}</td>
                		</tr>
            		</c:forEach>
            	</tbody>
        	</table>
    	</c:if>
    </form:form>
</div>



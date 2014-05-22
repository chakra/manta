<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/uploadFile/filter"/>
<app:url var="editUrl" value="/uploadFile"/>
<app:url var="sortUrl" value="/uploadFile/filter/sortby"/>
<app:url var="baseUrl"/>

<c:set var="uploadAction" value="$('form:first').attr('action','${editUrl}/0/upload');$('form:first').attr('method','POST');$('form:first').submit(); return false; "/>
<c:set var="exportTemplateAction" value="$('form:first').attr('action','${editUrl}/0/exportTemplate');$('form:first').submit(); return false; "/>

<app:dateIncludes/>

<div class="search">

        <div class="filter" >
		<form:form modelAttribute="uploadFileFilter" id="uploadFileFlterFormId" method="GET" action="${searchUrl}" focus="filterValue">
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.uploadId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="1" focusable="true" id="uploadId" path="uploadId" cssClass="filterValue"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.uploadFileName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="2" focusable="true" id="filterValue" path="uploadFileName" cssClass="filterValue"/>
                    </td>
                    <td class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="3" path="uploadFileNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="4" path="uploadFileNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.uploadAddDate" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="addDate" path="addDate" cssClass="datepicker2Col standardCal standardActiveCal"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.uploadModifiedDate" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="6" focusable="true" id="modifiedDate" path="modifiedDate" cssClass="datepicker2Col standardCal standardActiveCal"/>
                    </td>
                </tr>


                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.status" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell">
                        <label><br><form:checkbox tabindex="7" cssClass="checkbox" focusable="true" path="processingStatus"/><app:message code="admin.global.filter.label.processing" /><br></label>
                        <label><br><form:checkbox tabindex="8" cssClass="checkbox" focusable="true" path="processedStatus"/><app:message code="admin.global.filter.label.processed" /><br><br></label>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="5">
                        <form:button id="search"  cssClass="button" tabindex="9"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="10" onclick="${exportTemplateAction}"><app:message code="admin.global.button.exportTemplate"/></form:button >
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="10" onclick="${uploadAction}"><app:message code="admin.global.button.upload"/></form:button >
                        <br><br><br>
                    </td>
                </tr>
            </table>
        </div>
	</form:form>

    <c:if test="${uploadFileFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(uploadFileFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${uploadFileFilterResult.result != null}">
		<form:form modelAttribute="uploadFileFilterResult" id="uploadFileFilterResultFormId" method="GET">
        <table class="searchResult" width="100%">
            <colgroup>
                <col width="15%"/>
                <col width="25%"/>
                <col width="20%"/>
                <col width="20%"/>
                <col width="20%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/uploadId"><app:message code="admin.global.filter.label.uploadId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/uploadFileName"><app:message code="admin.global.filter.label.uploadFileName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/addDate"><app:message code="admin.global.filter.label.uploadAddDate" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/modifiedDate"><app:message code="admin.global.filter.label.uploadModifiedDate" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.status" /></a></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="uploadFile" items="${uploadFileFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${uploadFile.uploadId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${uploadFile.uploadId}"><c:out value="${uploadFile.uploadFileName}"/></a></td>
                    <td class="cell cell-text"><app:formatDate value="${uploadFile.addDate}"/></td>
                    <td class="cell cell-text"><app:formatDate value="${uploadFile.modifiedDate}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.UPLOAD_STATUS_CD.${uploadFile.status}" text="${uploadFile.status}"/></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
		</form:form>
    </c:if>

</div>


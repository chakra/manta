<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/account/${accountWorkflow.accountId}"/>

<c:set var="createAction" value="$('form:first').attr('action','${baseUrl}/workflow/create');$('form:first').submit(); return false; "/>
<app:url var="sortUrl" value="/account/${accountWorkflow.accountId}/workflow/sortby"/>

<div class="canvas">
    <form:form modelAttribute="accountWorkflow" action="" method="POST">
	    <app:refcdMap var="workflowTypeMap" code="WORKFLOW_TYPE_CD" invert="true"/>

        <div class="search">
            <br>
		    <c:if test="${accountWorkflow.result != null && fn:length(accountWorkflow.result) > 0}">
		        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(accountWorkflow.result)}</div>
		    </c:if>
            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="7%"/>
                    <col width="23%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="40%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">

                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/workflowId"><app:message code="admin.account.workflow.label.workflowId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/shortDesc"><app:message code="admin.account.workflow.label.workflowName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/workflowTypeCd"><app:message code="admin.account.workflow.label.workflowType" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/workflowStatusCd"><app:message code="admin.account.workflow.label.workflowStatus" /></a></th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="workflow" items="${accountWorkflow.result}">

                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${workflow.workflowId}"/></td>
                        <td class="cell cell-text"><a href="${baseUrl}/workflow/${workflow.workflowId}/${workflow.workflowTypeCd}"><c:out value="${workflow.shortDesc}"/></a></td>
                        <td class="cell cell-text"><app:message code="refcodes.WORKFLOW_TYPE_CD.${workflowTypeMap[workflow.workflowTypeCd]}" text="${workflow.workflowTypeCd}"/> </td>
                        <td class="cell cell-text"><app:message code="refcodes.WORKFLOW_STATUS_CD.${workflow.workflowStatusCd}" text="${workflow.workflowStatusCd}"/></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="4"><br>
                        <hr class="compact-bottom"/> 
                    </td>
                </tr>
               <tr>
                    <td colspan="4" valign="top" align="center" style="white-space: nowrap;">
                        <form:button onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button>
                    </td>
                </tr>
                </tbody>
            </table>
            <br><br>

        </div>

    </form:form>
</div>


<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>

<c:set var="workflowIdToEdit" value="${(empty  accountWorkflowDetail.workflowId ? 0 : accountWorkflowDetail.workflowId)}"/>
<c:set var="workflowTypeToEdit" value="${(empty  accountWorkflowDetail.workflowTypeCd ? 'new' : accountWorkflowDetail.workflowTypeCd)}"/>

<c:set var="userGroupMap"  value="${accountWorkflowDetail.userGroupMap}"/>
<c:set var="applySkipGroupMap"  value="${accountWorkflowDetail.applySkipGroupMap}"/>


<app:url var="baseUrl" value="/account/${accountWorkflowDetail.accountId}/workflow/${workflowIdToEdit}/${workflowTypeToEdit}"/>

<%-- <c:set var="createAction" value="$('form:first').attr('action','${baseUrl}/workflow/create');$('form:first').submit(); return false; "/> --%>
<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/save');$('form:first').submit(); return false; "/>
<c:set var="assignAction" value="$('form:first').attr('action','${baseUrl}/sites');$('form:first').submit(); return false; "/>
<%-- <c:set var="addRuleAction" value="$('form:first').attr('action','${baseUrl}/createRule');$('form:first').submit(); return false; "/> --%>

<c:set var="newRuleUrl" value="function(value) {window.location.href='${baseUrl}/rule/0'}"/>

<c:set var="deleteUrl" value="${baseUrl}/delete"/>
<c:set var="deleteAction" value="$('form:#workflowRules').submit(); return false;"/>

<app:locateLayer var="ruleLayer"
                 titleLabel="admin.global.filter.label.locateRule.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/addRule'
                 action="${baseUrl}/ruleType/createRule"
                 postHandler="${newRuleUrl}"/>
 <link href="<c:url value="/resources/ocean/css/message_preview.css"/>" rel="Stylesheet" type="text/css" media="all" /> <%--trying to change size of popup page--%>


<div class="canvas">
<div class="details">
    <form:form modelAttribute="accountWorkflowDetail" action="" method="POST">

	<form:hidden  path="workflowRuleTypeCdToEdit"/>
    <table >
        <tbody>
        <tr>
            <td><div class="label"><form:label path="workflowId"><app:message code="admin.account.workflowEdit.label.workflowId"/><span class="colon">:</span></form:label></div></td>
            <td ><div class="labelValue"><c:out value="${accountWorkflowDetail.workflowId}" default="0"/></div><form:hidden  path="workflowId"/></td>
       </tr>
        </tbody>

        <tbody>

        <tr>

            <td><div class="label"><form:label  path="workflowName"><app:message code="admin.account.workflowEdit.label.workflowName"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td ><form:input tabindex="1" path="workflowName" cssClass="inputLong"/></td>
        </tr>
        <tr>
            <td><div class="label"><form:label path="workflowTypeCd"><app:message code="admin.account.workflowEdit.label.workflowType"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="2" path="workflowTypeCd" cssClass="selectLong"><form:option value=""><app:message code="admin.global.select"/></form:option>
              	 <form:option value="<%=RefCodeNames.WORKFLOW_TYPE_CD.ORDER_WORKFLOW%>"><app:message code="refcodes.WORKFLOW_TYPE_CD.ORDER_WORKFLOW"/></form:option>
 <%-- // MANTA-381
                    <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowTypeCds}" i18nprefix="refcodes.WORKFLOW_TYPE_CD.">
                        <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                    </app:i18nRefCodes>
 --%>
                </form:select>
            </td>
        </tr>

        <tr>
            <td><div class="label"><form:label path="workflowStatus"><app:message code="admin.account.workflowEdit.label.workflowStatus"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="3" path="workflowStatus" cssClass="selectLong"><form:option value=""><app:message code="admin.global.select"/></form:option>
  				    <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowStatusCds}" i18nprefix="refcodes.WORKFLOW_STATUS_CD.">
                        <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                    </app:i18nRefCodes>

                </form:select>
            </td>
         </tr>

        <tr>
            <td colspan="2" style="height:15px"></td>
        </tr>
		<tr>
			<td  colspan="2" align="center">
<!-- 			<div  style="padding-right: 150px;"> -->

	             <form:button tabindex="27" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>

                 <c:if test="${accountWorkflowDetail.workflowId > 0}">&nbsp;&nbsp;&nbsp;
					<form:button tabindex="27" onclick="${assignAction} return false;"><app:message code="admin.global.button.assignLocations"/></form:button>
					&nbsp;&nbsp;&nbsp;
<%--  					<form:button tabindex="27" onclick="${addRuleAction} return false;"><app:message code="admin.global.button.addRule"/></form:button>  --%>
 	        		<button tabindex="1" onclick="${ruleLayer}"><app:message code="admin.global.button.addRule"/></button>
                 </c:if>

<!-- 	    	</div> -->
	    	</td>
	    </tr>
        </tbody>
    </table>
   </form:form>
   <div  style="padding-right: 150px;">
    <c:if test="${accountWorkflowDetail.workflowId>0}">&nbsp;&nbsp;&nbsp;
    <form:form id="workflowRules" modelAttribute="accountWorkflowRuleFilterResult" method="POST" action="${deleteUrl}">
       <div class="search">
            <br>
            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="6%"/>
                    <col width="4%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="10%"/>
                    <col width="15%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="5%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">

                    <th class="cell cell-number"><app:message code="admin.account.workflow.rule.label.rule" /></th>
                    <th class="cell cell-number"><app:message code="admin.account.workflow.rule.label.group" /></th>
                    <th colspan="3" class="cell cell-text"><app:message code="admin.account.workflow.rule.label.expression" /></th>
                    <th class="cell cell-text"><app:message code="admin.account.workflow.rule.label.action" /></th>
                    <th class="cell cell-text"><app:message code="admin.account.workflow.rule.label.applySkip" /></th>
                    <th class="cell cell-text"><app:message code="admin.account.workflow.rule.label.approverGroup" /></th>
                    <th class="cell cell-text"><app:message code="admin.account.workflow.rule.label.emailGroup" /></th>
                    <th class="cell cell-text cell-element" nowrap>
                         <a href="javascript:checkAll('workflowRules', 'rules.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                         <a href="javascript:checkAll('workflowRules', 'rules.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                     </th>
                </tr>

                </thead>

                <tbody class="body">
			    <app:refcdMap var="ruleTypeMap" code="WORKFLOW_RULE_TYPE_CD" invert="true"/>
			    <app:refcdMap var="actionMap" code="WORKFLOW_RULE_ACTION" invert="true"/>
			    <app:refcdMap var="nextActionMap" code="WORKFLOW_NEXT_RULE_ACTION" invert="true"/>

		        <c:if test="${accountWorkflowRuleFilterResult.result != null}">

                <c:forEach var="rule" varStatus="i" items="${accountWorkflowRuleFilterResult.result}">

                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${rule.value.ruleSeq}"/></td>
                        <td class="cell cell-number"><c:out value="${rule.value.ruleGroup}"/></td>

                        <td class="cell cell-text">
	                        <a href="${baseUrl}/rule/${rule.value.workflowRuleId}">
 		    					<app:message code="refcodes.WORKFLOW_RULE_TYPE_CD.${ruleTypeMap[rule.value.ruleTypeCd]}" text="${rule.value.ruleExp}"/>
<%-- 	                        	<c:out  value="${rule.value.ruleTypeCd}"/>  --%>
	                        </a>
                        </td>
                        <td class="cell cell-text"><c:out  value="${rule.value.ruleExp}"/></td>
                        <td class="cell cell-text"><c:out  value="${rule.value.ruleExpValue}"/></td>

                        <td class="cell cell-text">
	    					<app:message code="refcodes.WORKFLOW_RULE_ACTION.${actionMap[rule.value.ruleAction]}" text="${rule.value.ruleAction}"/>
<%--                         	<c:out  value="${rule.value.ruleAction}"/> --%>
                        	<c:if test="${! empty rule.value.nextActionCd }">
                        		<br>(
		    						<app:message code="refcodes.WORKFLOW_NEXT_RULE_ACTION.${nextActionMap[rule.value.nextActionCd]}" text="${rule.value.nextActionCd}"/>
<%--                         		<c:out  value="${rule.value.nextActionCd}"/> --%>
                        		)
                        	</c:if>
                        </td>
                        <td class="cell cell-text"><c:out  value="${applySkipGroupMap[rule.value.workflowRuleId]}"/> </td>
                        <td class="cell cell-text"><c:out  value="${userGroupMap[rule.value.approverGroupId]}"/></td>
                        <td class="cell cell-text"><c:out  value="${userGroupMap[rule.value.emailGroupId]}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="rules.selectableObjects[${i.index}].selected"/></td>
                    </tr>
                </c:forEach>
              	<c:if test="${accountWorkflowRuleFilterResult.result.size() > 0}">
                 <tr>
                    <td colspan="10" valign="top" align="center" style="white-space: nowrap;">
 			             <form:button tabindex="27" onclick="${deleteAction}"><app:message code="admin.global.button.deleteSelected"/></form:button>
                    </td>
                </tr>
              	</c:if>
                </c:if>
                </tbody>
            </table>
            <br><br>

        </div>
    </form:form>
    </c:if>

 </div>
</div>
</div>

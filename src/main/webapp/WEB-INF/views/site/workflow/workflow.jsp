<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/location/${siteWorkflow.siteId}"/>

<c:set var="saveAction" value="$('form:first').attr('action','${baseUrl}/workflow/save');$('form:first').attr('method','POST');$('form:first').submit(); return false; "/>
<c:set var="clearAction" value="$('form:first').attr('action','${baseUrl}/workflow/clear');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<app:url var="sortUrl" value="/location/${siteWorkflow.siteId}/workflow/sortby"/>

<c:set var="userGroupMap"  value="${siteWorkflow.userGroupMap}"/>
<c:set var="applySkipGroupMap"  value="${siteWorkflow.applySkipGroupMap}"/>

<app:refcdMap var="ruleTypeMap" code="WORKFLOW_RULE_TYPE_CD" invert="true"/>

<script type="text/javascript" language="JavaScript">


    function show_level(id) {
    	if (eval("document.getElementById(id)")!=null){
	        if(eval("document.getElementById(id)").style.display=='none'){
	            eval("document.getElementById(id)").style.display='block';
	            eval("document.getElementById('viewrules'+id)").className="expandedArrow";
	        } else {
	            eval("document.getElementById(id)").style.display='none';
	            eval("document.getElementById('viewrules'+id)").className="collapsedArrow";
	        }
    	}
    }

    function expand_level(id)  {
    	if (eval("document.getElementById(id)")!=null){
	        eval("document.getElementById(id)").style.display='block';
	        eval("document.getElementById('viewrules'+id)").className="expandedArrow";
    	}
    }

    function collapse_level(id) {
    	if (eval("document.getElementById(id)")!=null){
	        eval("document.getElementById(id)").style.display='none';
	        eval("document.getElementById('viewrules'+id)").className="collapsedArrow";
    	}
    }

    function show_levels(action) {
        var elems = document.getElementsByTagName('table');
        var k = 0;
        for(var i=0; i<elems.length ; i++) {
            var idStr = elems[i].id;
            if (idStr != 'undefined' && idStr.match('wId')){
                k++;
                if (action.match('Expand') && (k <= 100)) {  expand_level(idStr);}
                if (action.match('Collapse')) {collapse_level(idStr);}
            }
        }
    }


</script>

<div class="canvas">
    <form:form modelAttribute="siteWorkflow" action="" method="POST">
	    <app:refcdMap var="workflowTypeMap" code="WORKFLOW_TYPE_CD" invert="true"/>

        <div class="search">
            <br>
		    <c:if test="${siteWorkflow.workflows != null && fn:length(siteWorkflow.workflows) > 0}">
		        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(siteWorkflow.workflows)}</div>
		    </c:if>
		    <br>

            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="7%"/>
                    <col width="15%"/>
                    <col width="25%"/>
                    <col width="53%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">

                    <th style="text-align:left">
                        <nobr><a href="javascript:show_levels('Expand');" onClick=""><app:message code="admin.site.workflow.rule.label.expandAll" /></a>&nbsp;|</nobr><br>
                        <nobr><a href="javascript:show_levels('Collapse');" onClick=""><app:message code="admin.site.workflow.rule.label.collapseAll" /></a></nobr>
                    </th>
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/workflowId"><app:message code="admin.site.workflow.label.workflowId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/workflowName"><app:message code="admin.site.workflow.label.workflowName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/assign"><app:message code="admin.site.workflow.label.assign" /></a></th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="workflow" items="${siteWorkflow.workflows}">
                   <c:set var="eleid" value="${workflow.workflowId}"/>
                   <c:set var="displayAssign" value="${ (siteWorkflow.selectedWorkflow != null &&  siteWorkflow.selectedWorkflow == eleid ) ? 'visible' : 'none'}" />
                   <c:set var="arrow" value="${displayAssign == 'none' ? 'collapsedArrow' : 'expandedArrow'}"/>
                    <tr class="row">
                        <td class="cell cell-text"><span class="${arrow}" id="viewruleswId${eleid}" style="cursor:hand;cursor:pointer;" onClick="show_level('wId${eleid}');">&nbsp;</span></td>
                        <td class="cell cell-number"><c:out value="${workflow.workflowId}"/></td>
                        <td class="cell cell-text"><c:out value="${workflow.workflowName}"/></td>
                        <td class="cell cell-text"><form:radiobutton tabindex="2" path="selectedWorkflow" cssClass="radio" focusable="true" value="${workflow.workflowId}"/></td>
                    </tr>
<%--                    <c:if test="${workflow.rules != null && fn:length(workflow.rules) > 0}"> --%>

                    <tr>
                        <td class="cell cell-text"></td>
                        <td class="cell cell-text" colspan=3>

                                <table class="tableWRule" width="100%"  id="wId${eleid}" style="display:${displayAssign}">
                                    <colgroup>
                                        <col width="1%"/>
                                        <col width="5%"/>
                                        <col width="15%"/>
                                        <col width="15%"/>
                                        <col width="15%"/>
                                        <col width="20%"/>
                                        <col width="15%"/>
                                        <col width="10%"/>
                                        <col width="1%"/>
                                    </colgroup>

                                  <tr class="headerWRow">
                                      <td class="borderLeftTopCorner"></td>
                                      <td class="borderHTop" colspan=7>&nbsp;</td>
                                      <th class="borderRightTopCorner"></th>
                                  </tr>
                   <c:if test="${workflow.rules != null && fn:length(workflow.rules) > 0}">
                                  <tr class="headerWRow">
                                      <th class="cell cell-text borderVL"></th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.rule" /></th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.expression" /></th>
                                      <th class="cell cell-text"> </th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.action" /></th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.applySkip" /></th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.approverGroup" /></th>
                                      <th class="cell cell-text"><app:message code="admin.site.workflow.rule.label.emailGroup" /></th>
                                      <th class="cell cell-text borderVR"></th>
                                  </tr>

                                  <c:forEach var="rule" items="${workflow.rules}">
                                  <tr class="row">
                                      <td class="cell cell-text borderVL"></td>
                                      <td class="cell cell-number"><c:out value="${rule.ruleSeq}"/></td>
                                      <td class="cell cell-text"><app:message code="refcodes.WORKFLOW_RULE_TYPE_CD.${ruleTypeMap[rule.ruleTypeCd]}" text="${rule.ruleExp}"/> </td>
                                      <td class="cell cell-text"><c:out value="${rule.ruleExp} ${rule.ruleExpValue}"/></td>
                                      <td class="cell cell-text">
                                        <app:message code="refcodes.WORKFLOW_RULE_ACTION.${actionMap[rule.ruleAction]}" text="${rule.ruleAction}"/>
                                        <c:if test="${! empty rule.nextActionCd }">
                                            <br>(
                                                <app:message code="refcodes.WORKFLOW_NEXT_RULE_ACTION.${nextActionMap[rule.nextActionCd]}" text="${rule.nextActionCd}"/>
                                            )
                                        </c:if>
                                      </td>
                                      <td class="cell cell-text"><c:out  value="${applySkipGroupMap[rule.workflowRuleId]}"/> </td>
                                      <td class="cell cell-text"><c:out  value="${userGroupMap[rule.approverGroupId]}"/></td>
                                      <td class="cell cell-text"><c:out  value="${userGroupMap[rule.emailGroupId]}"/></td>
                                      <td class="cell cell-text  borderVR"></td>
                                  </tr>
                                  </c:forEach>
                     </c:if>
                     <c:if test="${empty workflow.rules}" >
                                  <tr class="row">
                                    <td class="cell cell-text borderVL"></td>
								  	<td colspan=7>
								  	    <div style="text-align: left;padding: 10px" class="label"><app:message code="admin.site.workflow.text.noRulesDefinedYet"/></div>
								  	</td>
                                    <td class="cell cell-text  borderVR"></td>
                                  </tr>
                     </c:if>
                   
                                  <tr class="headerWRow">
                                      <td class="borderLeftBottomCorner"></td>
                                      <td class="borderHBottom" colspan=7>&nbsp;</td>
                                      <th class="borderRightBottomCorner"></th>
                                  </tr>

                                </table>

                          </td>
                    </tr>
<%--                     </c:if> --%>
                </c:forEach>
                <tr>
                    <td colspan="4"><br>
                        <hr class="compact-bottom"/>
                    </td>
                </tr>
               <tr>
                    <td colspan="4" valign="top" align="right" style="white-space: nowrap;">
                        <form:button onclick="${clearAction}" disabled="${siteWorkflow.workflows == null || fn:length(siteWorkflow.workflows) == 0}"><app:message code="admin.global.button.clear"/></form:button>&nbsp;&nbsp;&nbsp; 
                        <form:button onclick="${saveAction}" disabled="${siteWorkflow.workflows == null || fn:length(siteWorkflow.workflows) == 0}"><app:message code="admin.global.button.save"/></form:button>
                    </td>
                </tr>
                </tbody>
            </table>
            <br><br>

        </div>

    </form:form>
</div>


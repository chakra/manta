<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>

<script language="JavaScript1.2">

$(document).ready(function() {
	f_hideAll(); 
	if ($('#ruleActionId') !='undefined') {
		$('#ruleActionId').trigger('change');
	}
	f_clearUserButton() ;
	f_onkeypress ();
});


function f_clearUserButton() {
	if ($('#clearLocateUserFilter') !='undefined') { 
		if ($('#emailUserId') !='undefined' &&  $('#emailUserId').val() > 0) {
			$('#clearLocateUserFilter').show();
			$('#emailUserId').focus();
		} else {
			$('#clearLocateUserFilter').hide();
		}
	}
}

function f_onkeypress (){
	$("form input:text").bind('keypress', function(e) {
		if(e.keyCode==13){
			return false;
		}
	});
	$("form textarea").bind('keypress', function(e) {
		if(e.keyCode==13){
			return false;
		}
	});
}

function f_clearUser() {
	if ($('#clearLocateUserFilter') !='undefined' && 
		$('#emailUserId') !='undefined' &&
		$('#emailUserId') !='undefined' ) { 
	
	  $('#emailUserName').attr("value", "");	
	  $('#emailUserId').val("");	
	
	  $('#clearLocateUserFilter').hide();
	}
}

function f_hideBox(boxid) {
  var el = document.getElementById(boxid);
  if (el != null) {	
  	document.getElementById(boxid).style.display = 'none';
  }
}

function f_hideAll() {
  f_hideBox("BOX_userlocate");
  f_hideBox("BOX_agerule");
}

function f_showBox(boxid) {
  var el = document.getElementById(boxid);
  if (el != null) {	
	  document.getElementById(boxid).style.display = 'block';
  }
}

function f_userbox1(actionVal) {
 	 switch (actionVal) {
	  case "<%=RefCodeNames.WORKFLOW_RULE_ACTION.FWD_FOR_APPROVAL%>": 
	    f_hideBox("BOX_userlocate");
	    f_showBox("BOX_agerule");
	  break;
	  case "<%=RefCodeNames.WORKFLOW_RULE_ACTION.STOP_ORDER%>": 
	    f_hideBox("BOX_userlocate");
	    f_showBox("BOX_agerule");
	  break;
	  case "<%=RefCodeNames.WORKFLOW_RULE_ACTION.SEND_EMAIL%>": 
	    f_showBox("BOX_userlocate");
	    f_hideBox("BOX_agerule");
	  break;
	  default:
	    f_hideBox("BOX_userlocate");
	    f_hideBox("BOX_agerule");
	  }
}


</script>

<c:set var="workflowIdToEdit" value="${(empty  accountWorkflowDetail.workflowId ? 0 : accountWorkflowDetail.workflowId)}"/>
<c:set var="workflowTypeToEdit" value="${(empty  accountWorkflowDetail.workflowTypeCd ? 'new' : accountWorkflowDetail.workflowTypeCd)}"/>

<app:url var="baseUrl" value="/account/${accountWorkflowDetail.accountId}/workflow/${accountWorkflowDetail.workflowId}/${accountWorkflowDetail.workflowTypeCd}/rule/${accountWorkflowRuleDetail.workflowRuleId}"/>
<app:url var="detailUrl" value="/account/${accountWorkflowDetail.accountId}/workflow/${accountWorkflowDetail.workflowId}/${accountWorkflowDetail.workflowTypeCd}"/>

<c:set var="saveAction" value="$('form:first').attr('action','${baseUrl}/save');$('form:first').submit(); return false; "/>

<c:set var="cancelAction" value="$('form:first').attr('action','${baseUrl}/cancel');$('form:first').submit(); return false; "/>

<c:set var="clearLocateUserFilter" value="javascript: f_clearUser(); return false; "/>

<app:url var="baseUrlLocate"/>

<c:set var="finallyAct" value="function(value) {f_clearUserButton()}"/>

<app:locateLayer var="userLayer"
    titleLabel="admin.global.filter.label.locateUser.title"
    closeLabel='admin.global.button.close'
    layer='${baseUrlLocate}/locate/user/single'
    action="${baseUrlLocate}/locate/user/selected?filter=accountWorkflowRuleDetail.setEmailUsers"
    idGetter="userId"
    nameGetter="notifyUserName"
    targetNames="emailUserName"
    targetIds="emailUserId"
    finallyHandler="${finallyAct}" />
    

<div class="canvas">

<div class="details">
    <form:form modelAttribute="accountWorkflowRuleDetail" action="" method="POST">
	<div style="font-size: 18px;font-weight: bold;" >
<%-- 	<c:out value="${accountWorkflowRuleDetail.workflowRuleTypeCd}" default=""/> --%>
	    <app:refcdMap var="ruleTypeMap" code="WORKFLOW_RULE_TYPE_CD" invert="true"/>
	    <app:refcdMap var="workflowTypeMap" code="WORKFLOW_TYPE_CD" invert="true"/>
	    <app:message code="refcodes.WORKFLOW_RULE_TYPE_CD.${ruleTypeMap[accountWorkflowRuleDetail.workflowRuleTypeCd]}" text="${accountWorkflowRuleDetail.workflowRuleTypeCd}"/>
	</div>
	<form:hidden  path="workflowRuleTypeCd"/>
	<br>
	<hr>
    <table >
      <tbody>
        <tr >
            <td colspan="4" nowrap>
			 <table width="100%">
			 <tr><td>
            	<form:label path="workflowId"><app:message code="admin.account.workflowEdit.label.workflowId"/><span class="colon">:</span></form:label>
          		&nbsp;&nbsp;
          		<c:out value="${accountWorkflowRuleDetail.workflowId}" default="0"/><form:hidden  path="workflowId"/>
          		&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td>
	            <form:label path="workflowName"><app:message code="admin.account.workflowEdit.label.workflowName"/><span class="colon">:</span></form:label>
	            &nbsp;&nbsp;
	            <a href="${detailUrl}"><c:out value="${accountWorkflowRuleDetail.workflowName}" default=""/></a><form:hidden  path="workflowName"/>
          		&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td>
	            <form:label path="workflowTypeCd"><app:message code="admin.account.workflowEdit.label.workflowType"/><span class="colon">:</span></form:label>
	            &nbsp;&nbsp;
	            <app:message code="refcodes.WORKFLOW_TYPE_CD.${workflowTypeMap[accountWorkflowRuleDetail.workflowTypeCd]}" text="${workflow.workflowTypeCd}"/>
			</td></tr>
			</table>
           </td>
    	</tr>
		<tr><td>&nbsp;</td></tr>
        </tbody>
		
        <tbody>
        <tr>
            <td width="20%"><div class="label"><form:label  path="ruleNumber"><app:message code="admin.account.workflowRuleEdit.label.ruleNumber"/><span class="colon">:</span></form:label></div></td>
            <td ><form:input tabindex="1" path="ruleNumber"  size="5" cssClass="id"/></td>
 
            <td><div class="label"><form:label  path="approverGroupId"><app:message code="admin.account.workflowRuleEdit.label.approversGroup"/><span class="colon">:</span></form:label></div></td>
            <td>
            	<form:select tabindex="2" style="width:200px" path="approverGroupId">
            		<form:option value=""><app:message code="admin.global.select"/></form:option>
                    <form:options items="${accountWorkflowRuleDetail.userGroups}" itemValue="groupId" itemLabel="shortDesc"></form:options>
 	            </form:select>
            </td>    
         </tr>
        <tr>
            <td width="20%"><div class="label"><form:label  path="ruleGroup"><app:message code="admin.account.workflowRuleEdit.label.ruleGroup"/><span class="colon">:</span></form:label></div></td>
            <td ><form:input tabindex="1" path="ruleGroup"  size="5" cssClass="id filterValue"/></td>
 
            <td><div class="label"><form:label  path="emailGroupId"><app:message code="admin.account.workflowRuleEdit.label.emailUsersGroup"/><span class="colon">:</span></form:label></div></td>
            <td>
          	  <form:select tabindex="2" style="width:200px" path="emailGroupId">
  	        		<form:option value=""><app:message code="admin.global.select"/></form:option>
                    <form:options items="${accountWorkflowRuleDetail.userGroups}" itemValue="groupId" itemLabel="shortDesc"></form:options>
                 </form:select>
            </td>    
        </tr>

        <tr>
            <td colspan="4">
                 <form:label path="applySkipGroupId"><app:message code="admin.account.workflowRuleEdit.label.applySkipGroup"/><span class="colon">:</span></form:label>
     	    	 &nbsp;
     	    	 <form:select tabindex="2" style="width:200px" path="applySkipGroupId">
  	        		<form:option value=""><app:message code="admin.global.select"/></form:option>
                    <form:options items="${accountWorkflowRuleDetail.userGroups}" itemValue="groupId" itemLabel="shortDesc"></form:options>
                 </form:select>
    			&nbsp;&nbsp;
                <form:select tabindex="3" style="width:80px" path="applySkipActionTypeCd">
                	<form:option value=""><app:message code="admin.global.select"/></form:option>
  				    <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowAssocActionTypeCds}" i18nprefix="refcodes.WORKFLOW_ASSOC_ACTION_TYPE_CD.">
                        <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                	</app:i18nRefCodes>
                 </form:select>
                 &nbsp;
                 <form:label path="applySkipGroupId"><app:message code="admin.account.workflowRuleEdit.label.theRule"/></form:label>
            </td>
         </tr>
        <tr>
            <td>
              <div class="label"><form:label path="ruleMessage"><app:message code="admin.account.workflowRuleEdit.label.warningMessage"/><span class="colon">:</span></form:label></div>
            </td>
            <td colspan="3" >
              <form:input tabindex="1" path="ruleMessage" size="130" />
            </td>
        </tr>
        <tr>
            <td colspan="4" style="height:15px"></td>
        </tr>
 </table>  
<!--     begin:    SPECIAL PART FOR RULE TYPE -->
		<hr>
<table>
	<tr><td colspan="4">
	    <c:if test="${accountWorkflowRuleDetail.isOrderTotalRuleType}">
 	   		 <jsp:include  page="editOrderTotal.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isBreakWorkflowRuleType}">
 	   		 <jsp:include  page="editBreakWorkflow.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isEveryOrderRuleType}">
 	   		 <jsp:include  page="editEveryOrder.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isBudgetYTDSpendingRuleType}">
 	   		 <jsp:include  page="editBudgetYTD.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isOrderSkuRuleType}">
 	   		 <jsp:include  page="editOrderSku.jsp"/>
 	    </c:if>

	    <c:if test="${accountWorkflowRuleDetail.isOrderExcludedFromBudgetRuleType}">
 	   		 <jsp:include  page="editOrderExcludedFromBudget.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isUserLimitRuleType}">
 	   		 <jsp:include  page="editUserLimit.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isCostCenterBudgetRuleType}">
 	   		 <jsp:include  page="editCostCenterBudget.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isOrderSkuQtyRuleType}">
 	   		 <jsp:include  page="editOrderSkuQty.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isOrderVelocityRuleType}">
 	   		 <jsp:include  page="editOrderVelocity.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isItemCategoryRuleType}">
 	   		 <jsp:include  page="editItemCategory.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isItemRuleType}">
 	   		 <jsp:include  page="editItem.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isCategoryTotalRuleType}">
 	   		 <jsp:include  page="editCategoryTotal.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isShoppingControlsRuleType}">
 	   		 <jsp:include  page="editShoppingControls.jsp"/>
 	    </c:if>
	    <c:if test="${accountWorkflowRuleDetail.isNonOrderGuideItemRuleType}">
 	   		 <jsp:include  page="editItemsNotOnShoppingList.jsp"/>
 	    </c:if>
 	    
 	</td></tr>    
<!--     end:    SPECIAL PART FOR RULE TYPE -->

  <c:if test="${accountWorkflowRuleDetail.isNextRuleSectionRequired}">
	<%String display = "none"; %>
   	<tr><td colspan="4">
		<div id="BOX_agerule" >	
		<form:label path="daysUntilNextAction"><app:message code="admin.account.workflowRuleEdit.label.ifOrderNotUpdated"/><span class="colon">:</span><span class="reqind">*</span></form:label>
  
	      <form:select tabindex="2" style="width:200px" path="daysUntilNextAction">
 			<form:option value="1">1</form:option>
			<form:option value="2">2</form:option>
			<form:option value="3">3</form:option>
			<form:option value="4">4</form:option>
			<form:option value="5">5</form:option>
			<form:option value="6">6</form:option>
			<form:option value="7">7</form:option>
			<form:option value="14">14</form:option>
			<form:option value="30">30</form:option>
			<form:option value="60">60</form:option>
			<form:option value="90">90</form:option>
		</form:select>
		
		<form:label path="nextActionCd"><app:message code="admin.account.workflowRuleEdit.label.daysThen"/><span class="colon">:</span></form:label>
        <form:select tabindex="2" style="width:200px" path="nextActionCd">
 	      	 <form:option value=""><app:message code="admin.global.select"/></form:option>
            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowNextRuleActions}" i18nprefix="refcodes.WORKFLOW_NEXT_RULE_ACTION.">
                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
            </app:i18nRefCodes>
		</form:select>
		</div>	
	</td></tr>
  	<tr><td colspan="4">
		<div id="BOX_userlocate">
		<span class="wkrule">
		<table>
		<tr>
			<td>
				<form:label path="emailUserName"><app:message code="admin.account.workflowRuleEdit.label.notify"/><span class="colon">:</span></form:label>
			</td>
			<td colspan="2">
				<form:input tabindex="1" path="emailUserName" cssClass="noborder"  readonly="true" size="130"/>
			</td>
		</tr>
		<tr>
			<td>
				<form:label path="emailUserId"><app:message code="admin.account.workflowRuleEdit.label.userId"/><span class="colon">:</span></form:label>
			</td>
			<td colspan="2">
				<form:input tabindex="1" path="emailUserId"  readonly="true" cssClass="readonly"/>
<!-- 			</td>
			<td>
 -->	 		  <button tabindex="1" onclick="${userLayer}"><app:message code="admin.global.filter.label.search"/></button>
<%-- 			  <c:if test="${accountWorkflowRuleDetail.emailUserId > 0}" > --%>
	          	&nbsp;&nbsp;<button id="clearLocateUserFilter" tabindex="2" onclick="${clearLocateUserFilter}"><app:message code="admin.global.filter.label.clearUser"/></button>
<%-- 			  </c:if>     --%>
			</td>
		</tr>           			
		</table>
		</span>
		</div>
	</td></tr>
   </c:if>

	<tr><td  colspan="6" align="center">
	             <form:button tabindex="27" onclick="${saveAction} return false;"><app:message code="admin.global.button.save"/></form:button>
	            &nbsp;&nbsp;&nbsp;
				<form:button tabindex="27" onclick="${cancelAction} return false;"><app:message code="admin.global.button.cancel"/></form:button>
	</td></tr>
        </tbody>
    </table>    	
 
   </form:form>
      

    
 </div>
</div>


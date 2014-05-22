<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>

		<div id="BOX_userLimit">
           <form:label path="totalValue"><app:message code="admin.account.workflowRuleEdit.label.ifUserOrderTotal"/></form:label>
  			&nbsp;
			<form:input tabindex="1" path="totalValue"  cssClass="id filterValue"/>
            <form:label path="totalExp"><app:message code="admin.account.workflowRuleEdit.label.withinNumberOfDays"/></form:label>
			<form:input tabindex="1" path="totalExp"  cssClass="id filterValue"/>
			
            <form:label path="ruleAction"><app:message code="admin.account.workflowRuleEdit.label.then"/><span class="colon">:</span></form:label>
			&nbsp;
            <form:select id ="ruleActionId" tabindex="2" style="width:200px" path="ruleAction"  onchange="f_userbox1(this.value);">
            	 <form:option value=""><app:message code="admin.global.select"/></form:option>
            	 <form:option value="<%=RefCodeNames.WORKFLOW_RULE_ACTION.FWD_FOR_APPROVAL%>"><app:message code="refcodes.WORKFLOW_RULE_ACTION.FWD_FOR_APPROVAL"/></form:option>
            	 
<%--             <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowRuleActions}" i18nprefix="refcodes.WORKFLOW_RULE_ACTION.">
                     <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                 </app:i18nRefCodes>
 --%>
              </form:select>
		</div>

 
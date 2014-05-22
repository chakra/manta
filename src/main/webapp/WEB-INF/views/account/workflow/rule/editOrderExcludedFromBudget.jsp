<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>

		<div id="BOX_orderTotal">
           <form:label path="totalValue"><app:message code="admin.account.workflowRuleEdit.label.ifOrderExcluded"/></form:label>
  			&nbsp;
            <form:label path="ruleAction"><app:message code="admin.account.workflowRuleEdit.label.then"/><span class="colon">:</span></form:label>
			&nbsp;
            <form:select id ="ruleActionId" tabindex="2" style="width:200px" path="ruleAction"  onchange="f_userbox1(this.value);">
            	 <form:option value=""><app:message code="admin.global.select"/></form:option>
                 <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowRuleActions}" i18nprefix="refcodes.WORKFLOW_RULE_ACTION.">
                     <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                 </app:i18nRefCodes>
             </form:select>
			<input id="totalValue" name="totalValue" value="0" type="hidden"/>
			<input id="totalExp" name="totalExp" value="<%=RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_EXCLUDED_FROM_BUDGET %>" type="hidden"/>
		</div>

 
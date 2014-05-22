<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>
<%@ page import="com.espendwise.manta.util.Constants"%>

<app:url var="baseUrl" value="/account/${accountWorkflowDetail.accountId}/workflow/${accountWorkflowDetail.workflowId}/${accountWorkflowDetail.workflowTypeCd}/rule/${accountWorkflowRuleDetail.workflowRuleId}"/>

		<div id="BOX_itemCategory">
             <table class="locate">
                <tr>
                	<td >		
						<jsp:include  page="../../../locateDistributor.jsp">
						     <jsp:param name="baseViewName" value="accountWorkflowRuleDetail" />
						     <jsp:param name="pageUrl" value="${baseUrl}" />
						     <jsp:param name="filteredDistrCommaNames" value="${accountWorkflowRuleDetail.filteredDistrCommaNames}" />
			    		</jsp:include>
		    		</td>
                    <td class="cell label first">
                       <form:checkbox tabindex="5" cssClass="checkbox" path="splitOrder"/><app:message code="admin.account.workflowRuleEdit.label.splitOrder" />
                    </td>
                </tr>
                
               <tr><td  class="cell label first"></td><td class="cell value"></td></tr>
			   
			   <tr><td colspan ="2">	
		           <form:label path="totalExp"><app:message code="admin.account.workflowRuleEdit.label.ifItemCategory"/><span class="colon">:</span></form:label>
			       <form:select tabindex="2" style="width:200px" path="itemCategoryId">
		     			<form:option value=""><app:message code="admin.global.select"/></form:option>
<%-- 		              	<form:options items="${accountWorkflowRuleDetail.itemCategories}" itemValue="itemId" itemLabel="shortDesc"></form:options> --%>				
						<c:forEach var="category" varStatus="i" items="${accountWorkflowRuleDetail.itemCategories}">
		              		<c:if test="${empty category.longDesc}">
		              			<form:option value="${category.itemId}" ><c:out value="${category.shortDesc}"/></form:option> 
		              		</c:if>
		              		<c:if test="${not empty category.longDesc}">
		              			<form:option value="${category.itemId}" ><c:out value="${category.shortDesc}(${category.longDesc})"/></form:option>
		              		</c:if>
						</c:forEach>

		           </form:select>
		
		           <form:label path="totalExp"><app:message code="admin.account.workflowRuleEdit.label.hasPrice"/><span class="colon">:</span></form:label>
		           <form:select tabindex="2" style="width:80px" path="totalExp">
		           		<form:option value=""><app:message code="admin.global.select"/></form:option>
		                <form:option value="<%=RefCodeNames.WORKFLOW_RULE_EXPRESSION.LESS%>">&lt;</form:option> 
		                <form:option value="<%=RefCodeNames.WORKFLOW_RULE_EXPRESSION.LESS_OR_EQUAL%>">&lt;=</form:option> 
		                <form:option value="<%=RefCodeNames.WORKFLOW_RULE_EXPRESSION.GREATER%>">&gt;</form:option> 
		                <form:option value="<%=RefCodeNames.WORKFLOW_RULE_EXPRESSION.GREATER_OR_EQUAL%>">&gt;=</form:option> 
		                     
		            </form:select>
					&nbsp;
					<form:input tabindex="1" path="totalValue" cssClass="id filterValue"/>
		            <form:label path="ruleAction"><app:message code="admin.account.workflowRuleEdit.label.then"/><span class="colon">:</span></form:label>
					&nbsp;
		            <form:select id ="ruleActionId" tabindex="2" style="width:200px" path="ruleAction"  onchange="f_userbox1(this.value);">
		            	 <form:option value=""><app:message code="admin.global.select"/></form:option>
		                 <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowRuleActions}" i18nprefix="refcodes.WORKFLOW_RULE_ACTION.">
		                     <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
		                 </app:i18nRefCodes>
		             </form:select>
	            </td></tr> 

            </table>             
		</div>

 
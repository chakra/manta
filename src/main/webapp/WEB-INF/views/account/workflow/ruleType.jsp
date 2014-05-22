<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%
  String errNoRuleType = AppI18nUtil.getMessage("validation.web.error.workflow.error.noRuleTypeSelected");
// String errNoRuleType =  "validation.web.error.workflow.error.noRuleTypeSelected";
%>

<script>
function showError() {
		if ('undefined' == $(".webError") || $(".webError").text().length == 0 ) {
			$(".webErrors").text(
			"<%=errNoRuleType%>"
			);			
		}
} 
</script>

<app:url var="baseUrl"/>

<app:url var="ruleUrl" value="/account/${accountWorkflowRuleTypeFilter.accountId}/workflow/${accountWorkflowRuleTypeFilter.workflowId}/${accountWorkflowRuleTypeFilter.workflowType}/ruleType/createRule"/>
<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'ruleType', 'ruleLayer');"/>

<div class="search locate">

    <form:form modelAttribute="accountWorkflowRuleTypeFilter" id="accountWorkflowRuleTypeFilterId" method="POST" focus="filterValue" action="" >

        <table>
            <tr>
<%--                 <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.addWorkflowRule" /></td> --%>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first" align ="right">
                                <label><app:message code="admin.global.filter.label.rule" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">

			   				<c:if test="${accountWorkflowRuleTypeFilter.orca}">
				                 <form:select tabindex="2" style="width:200px" path="ruleType"><form:option value=""><app:message code="admin.global.select"/></form:option>
				                    <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.woWorkflowRuleTypeCds}" i18nprefix="refcodes.WO_WORKFLOW_RULE_TYPE_CD.">
				                        <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
				                    </app:i18nRefCodes>
				                </form:select>
							</c:if>
			   				<c:if test="${!accountWorkflowRuleTypeFilter.orca}">
				                 <form:select tabindex="2" style="width:200px" path="ruleType"><form:option value=""><app:message code="admin.global.select"/></form:option>
				                    <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.workflowRuleTypeCds}" i18nprefix="refcodes.WORKFLOW_RULE_TYPE_CD.">
				                        <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
				                    </app:i18nRefCodes>
				                </form:select>
							</c:if>

                            </td>
                            <td  class="cell">
<%--                                 <form:button cssClass="button" tabindex="6"  onclick="window.parent.locateLayerManager.layer('ruleLayer').closePopUp();  $('form:first').submit(); return false; "> --%>
                                <form:button cssClass="button" tabindex="6"  onclick="if ($('#ruleType').find('option:selected').attr('value')=='') {showError(); return false;} else {return ${doReturnSelected}}">
                                    <app:message code="admin.global.button.ok"/>
                                </form:button>
                            
                            </td>
                        </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>

    </form:form>

</div>


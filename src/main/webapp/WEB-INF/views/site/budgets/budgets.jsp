<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/location/${siteHeader.siteId}/budgets"/>

<c:set var="changeYear" value="window.location.href='${baseUrl}/'+this.value"/>

<c:set var="periodNumbers"  value="${fn:length(siteBudget.fiscalYearPeriods)}"/>
<%-- <c:set var="periodsInline"  value="${periodNumbers > 13 ? 10 : 12}"/> --%>
<c:set var="periodsInline"  value="${periodNumbers > 13 ? 12 : 13}"/>
<c:set var="rows"  value="${(periodNumbers/periodsInline + (periodNumbers%periodsInline > 0 ? 1 : 0))}"/>
<c:set var="rows"  value="${rows < 0 ? 0 : rows }"/>


<app:message var="unlimited" code="admin.site.budgets.pattern.unlimited"/>

<script type="text/javascript">function amountSet(key){try{$(document).find('input[cckey='+key+']').each(function() { var obj =$('#amountSet'+key); this.value=obj.attr('value');obj.focus()});} catch(e){} return false;}</script>

<div class="canvas">

<form:form modelAttribute="siteBudget" id="siteBudget" action="${baseUrl}/${siteBudget.year}" method="POST">

<form:hidden path="currentPeriod"/>

<form:hidden path="enableCopyBudgets"/>
<form:hidden path="usedSiteBudgetThreshold"/>

<c:forEach varStatus="i" items="${siteBudget.fiscalYears}"><form:hidden path="fiscalYears[${i.index}]"/></c:forEach>
<c:forEach varStatus="i" items="${siteBudget.fiscalYearPeriods}"><form:hidden path="fiscalYearPeriods[${i.index}]"/></c:forEach>


<div class="details">

<table width="100%" class="locationBudgets">
<colgroup>
    <col width="80%">
    <col width="20%">
</colgroup>
<thead>
<tr>
    <td  style="padding: 0"><table style="padding: 0" width="100%">
        <tr>
            <td valign="top"  style="padding: 0;vertical-align: top"><div class="subHeader" style="vertical-align: top"><app:message code="admin.site.budgets.header.title"/></div></td>
            <td style="padding: 0" align="right">
                <table>
                    <tr>

                        <td valign="top" style="vertical-align: top;padding-top: 0;" class="label labelValue"><label style="padding-right: 5px; "><app:message code="admin.site.budgets.label.year"/><span class="colon">:</span></label>

                            <c:if test="${fn:length(siteBudget.fiscalYears) > 0}">
                                <form:select onchange="${changeYear}"  path="year" style="width: 100px;">
                                    <form:option value="0" ><app:message code="admin.global.select"/> </form:option>
                                    <c:forEach var="year" items="${siteBudget.fiscalYears}">
                                        <form:option value="${year.value}"><c:out value="${year.value}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </c:if>

                        </td>

                        <c:if test="${fn:length(siteBudget.costCenterBudget) > 0}">
                            <td style="padding-left: 20px">
                                <app:box name="admin.global.panel.actions" cssClass="white">
                                    <table>
                                        <tr><td style="padding:2px;padding-top:4px;"><form:radiobutton disabled="${!siteBudget.editable}"  cssClass="radio" path="selectedAction" value="/saveChanges" style="padding:0;"/></td><td class="label" style="padding:0;margin:0"><app:message  code="admin.site.budgets.action.saveChanges"/></td></tr>
                                        <tr><td style="padding:2px;padding-top:4px;"><form:radiobutton disabled="${!siteBudget.editable}" cssClass="radio" path="selectedAction" value="/setUnlimited" style="padding:0;"/></td><td class="label" style="padding:0;margin:0"><app:message code="admin.site.budgets.action.setUnlimited"/></td></tr>
                                        <tr><td style="padding:2px;padding-top:4px;"><form:radiobutton  disabled="${!siteBudget.enableCopyBudgets}" cssClass="radio" path="selectedAction" value="/copyLastYear" style="padding:0;"/></td><td class="label" style="padding:0;margin:0"><app:message code="admin.site.budgets.action.copyLastYear"/></td></tr>
                                        <tr><td style="padding:2px;margin-top:5px;" colspan="2" align="center"><br><form:button disabled="${!siteBudget.editable}" ><app:message code="admin.global.button.save"/></form:button></td></tr>
                                    </table>
                                </app:box>
                            </td>
                        </c:if>
                    </tr>

                </table>
            </td>
        </tr>
    </table>
    </td>
    <td style="padding-top: 0;vertical-align: bottom"><label class="label">                        <c:if test="${fn:length(siteBudget.costCenterBudget) > 0}">
        <app:message code="admin.site.budgets.label.budgetTotal"/><span class="colon">:</span></label> <c:out value='${siteBudget.budgetsTotal == null ? (siteBudget.unlimitedBudget ? unlimited : "") :siteBudget.budgetsTotal}'/> </c:if><br></td>
</tr>
</thead>
<c:if test="${fn:length(siteBudget.costCenterBudget) > 0}">

<c:if test="${rows > 0}">

<c:forEach var="costcenterId" items="${siteBudget.displayCostCenters}">

<c:set var="costcenter" value="${siteBudget.costCenterBudget[costcenterId]}"/>

<form:hidden path="costCenterBudget[${costcenter.costCenterId}].costCenterId"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].costCenterName"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].costCenterType"/>

<c:forEach var="budget" items="${costcenter.budgets}">


<c:set var="readonly" value="${budget.key =='ACCOUNT BUDGET'}"/>
<c:set var="showonly" value="${costcenter.costCenterType =='ACCOUNT BUDGET'}"/>

<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].budgetId"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].budgetType"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].costCenterBudgetSpentForm.spent"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].costCenterBudgetSpentForm.allocated"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].costCenterBudgetSpentForm.remaining"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].costCenterBudgetSpentForm.period"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].costCenterBudgetSpentForm.year"/>
<form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].budgetTotal"/>
<c:if test="${siteBudget.usedSiteBudgetThreshold == false}">
    <form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].thresholdPercent"/>
</c:if>
<tbody>
<tr>
    <td>


        <div style="padding-top: 5px;">

            <c:forEach varStatus="row" begin="0" end="${rows>=1?rows - 1 : 0}" step="1">

                <c:set var="lineBegin" value="${periodsInline * row.index}" />
                <c:set var="lineEnd" value="${((periodsInline * row.index)+periodsInline>periodNumbers?periodNumbers:(periodsInline * row.index)+periodsInline)}" />
                <c:set var="lineEnd" value="${lineEnd < 0 ? 0 :lineEnd }"/>

                <c:if test="${lineEnd > 0}">

                    <c:set var="lineEnd" value="${lineEnd>=1 ? lineEnd-1 : 0 }"/>


                    <table width="100%">

                        <tr>

                            <td class="costCenter" style="padding-left: 0;padding-top: 0;">

                                <table width="100%">

                                    <colgroup>
                                        <col width="8%"/>
                                        <c:forEach varStatus="line" begin="${lineBegin}" end="${lineEnd}" step="1">
                                            <col width="7%"/>
                                        </c:forEach>
                                        <col>
                                    </colgroup>

                                    <thead class="noborder" >
                                    <c:if test="${row.index == 0}">
                                        <tr>
                                            <td colspan="${lineEnd + 3}" style="padding-left:0"><table width="100%">
                                                <tr>
                                                    <td style="padding:0">
                                                        <label><app:message code="admin.site.budgets.label.costCenter"/><span class="colon">:</span></label> <c:out value="${costcenter.costCenterName}"/>
                                                        <label style="padding-left: 20px"><app:message code="admin.site.budgets.label.costCenterType"/><span class="colon">:</span></label>
                                                        <app:refcd var="costCenterType" code="BUDGET_TYPE_CD" value="${costcenter.costCenterType}"/>
                                                        <app:message code="refcodes.COST_CENTER_TYPE_CD.${costCenterType}" text="${costCenterType}"/>
                                                    </td>
                                                    <td style="padding:0" align="right"><label><app:message code="admin.site.budgets.label.costCenterId"/><span class="colon">:</span></label> <c:out value="${costcenter.costCenterId}"/>
                                                    </td>
                                                </tr>
                                            </table>
                                            </td>
                                        </tr>
                                    </c:if>
                                    </thead>

                                    <thead   style="background: rgb(240, 236, 220)">
                                    <tr>
                                        <td></td>
                                        <c:forEach varStatus="line" begin="${lineBegin}" end="${lineEnd}" step="1">
                                            <c:set var="period"  value="${siteBudget.fiscalYearPeriods[line.index]}"/>
                                            <td><strong><c:out value="${budget.value.periodAmount[period].period}"/></strong></td>
                                            <form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].periodAmount[${period}].period"/>
                                        </c:forEach>
                                        <td style="border: 0;background: #ffffff"></td>

                                    </tr>
                                    </thead>


                                    <tbody>
                                    <tr>
                                        <td class="label"><label><app:message code="admin.site.budgets.label.startDate"/></label></td>
                                        <c:forEach varStatus="line" begin="${lineBegin}" end="${lineEnd}" step="1">
                                            <c:set var="period"  value="${siteBudget.fiscalYearPeriods[line.index]}"/>
                                            <td><app:formatMmdd value="${budget.value.periodAmount[period].mmdd}"/></td>
                                            <form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].periodAmount[${period}].mmdd"/>
                                        </c:forEach>
                                        <td style="border: 0;background: #ffffff"></td>
                                    </tr>
                                    </tbody>

                                    <tbody>
                                    <tr>
                                        <td class="label"><label><app:message code="admin.site.budgets.label.amount"/></label></td>
                                        <c:forEach varStatus="line" begin="${lineBegin}" end="${lineEnd}" step="1">
                                            <c:set var="period"  value="${siteBudget.fiscalYearPeriods[line.index]}"/>
                                            <c:set var="value" value="${siteBudget.costCenterBudget[costcenter.costCenterId].budgets[budget.key].periodAmount[period].inputForm}" />
                                            <c:set var="value1" value="${siteBudget.costCenterBudget[costcenter.costCenterId].budgets[budget.key].budgetId>0?unlimited:null }"/>
          									<c:set var="value" value="${value==null ? value1:value }"/>                                  
                                            <td>

                                                <c:choose>
                                                    <c:when test="${showonly == true}">
                                                        <form:hidden path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].periodAmount[${period}].inputForm"
                                                                     value='${value}'
                                                                />
                                                        <c:out value="${value}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:input class='smallAmount${readonly?" readonly":""}'
                                                                    cckey="${readonly ? null : costcenter.costCenterId}"
                                                                    readonly="${readonly}"
                                                                    path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].periodAmount[${period}].inputForm"
                                                                    value='${value}'
                                                                />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:forEach>
                                        <td style="border: 0;background: #ffffff"></td>
                                    </tr>
                                    </tbody>

                                </table>
                            </td>
                        </tr>
                    </table>

                </c:if>

            </c:forEach>


        </div>

        <c:if test="${!showonly}">

            <c:if test="${siteBudget.usedSiteBudgetThreshold}">
                <div class="labelValue" style="padding-bottom: 10px">
                    <label><app:message code="admin.site.budgets.label.threshold"/> (%)<span class="colon">:</span></label>&nbsp;&nbsp;
                    <form:input class='smallAmount${readonly?" readonly":""}'
                                readonly="${readonly}"
                                path="costCenterBudget[${costcenter.costCenterId}].budgets[${budget.key}].thresholdPercent"/>
                </div>
            </c:if>
            <div class="labelValue">


                <label><app:message code="admin.site.budgets.label.setAllPeriods"/><span class="colon">:</span></label>&nbsp;&nbsp;
                <c:if test="${readonly}">
                    <input class="smallAmount${readonly?" readonly":""}" readonly="readonly" id="amountSet${costcenter.costCenterId}" type="text"/>
                    <button style="margin-left:20px" class="button" disabled="disabled">
                        <app:message code="admin.global.button.set"/>
                    </button>
                </c:if>
                <c:if test="${!readonly}">
                    <script type="text/javascript">$(document).ready(function () {
                        $('#amountSet${costcenter.costCenterId}').keypress(function (e) {
                            if ((e.keyCode ? e.keyCode : e.which) == 13) {
                                amountSet('${costcenter.costCenterId}');
                                return false;
                            }
                        })
                    });</script>
                    <input class="smallAmount${readonly?" readonly":""}" id="amountSet${costcenter.costCenterId}" type="text"/>
                    <button style="margin-left:20px"
                            class="button"
                            onclick="return amountSet('${costcenter.costCenterId}')">
                        <app:message code="admin.global.button.set"/>
                    </button>
                </c:if>
            </div>

        </c:if>

    </td>

    <td style="padding-top:35px;vertical-align: top" >
        <table class="spent" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:40px"><label><app:message code="admin.site.budgets.label.spent.currentPeriod"/><span class="colon">:</span></label></td>
                <td style="vertical-align: bottom;"><c:out value="${budget.value.costCenterBudgetSpentForm.period}"/></td>
                <td></td>
            </tr>
            <tr><td colspan="3" style="width:120px"><hr></td></tr>
            <tr>
                <td><label><app:message code="admin.site.budgets.label.spent.remaining"/><span class="colon">:</span></label></td>
                <td style="text-align: right;vertical-align: bottom;">
                    <c:out value="${!budget.value.costCenterBudgetSpentForm.unlimitedBudget ? budget.value.costCenterBudgetSpentForm.remaining:unlimited}"/>
                </td>
                <td></td>
            </tr>
        </table>
    </td>

</tr>
<tr><td colspan="2"><div><hr></div> </td></tr>
</tbody>

</c:forEach>
</c:forEach>

</c:if>
</c:if>

</table>
<c:if test="${fn:length(siteBudget.costCenterBudget) <= 0}">

    <c:if test="${siteBudget.year>0}">
        <div style="text-align: center;padding: 40px" class="label"><app:message code="admin.site.budgets.text.locationIsNotConfiguredForYear" arguments="${siteBudget.year}"/></div>
    </c:if>

    <c:if test="${siteBudget.year==0}">
        <c:if test="${fn:length(siteBudget.fiscalYears) > 0}">
            <div style="text-align: center;padding: 40px" class="label"><app:message code="admin.site.budgets.text.selectYearTo" arguments="${siteBudget.year}"/></div>
        </c:if>
        <c:if test="${fn:length(siteBudget.fiscalYears) == 0}">
            <div style="text-align: center;padding: 40px" class="label"><app:message code="admin.site.budgets.text.locationIsNotConfigured" arguments="${siteBudget.year}"/></div>
        </c:if>
    </c:if>

</c:if>

</div>
</form:form>

</div>

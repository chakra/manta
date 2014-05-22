<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/costCenter/${costCenter.costCenterId > 0?costCenter.costCenterId: 0}'); "/>

<app:dateIncludes/>

<div class="canvas">

<div class="details std">

<form:form modelAttribute="costCenter" action="" method="POST">

<table width="100%">


<tr>
    <td>
        <table>

            <tr>
                <td><div class="label"><form:label path="costCenterId"><app:message code="admin.costCenter.label.costCenterId"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><c:out value="${costCenter.costCenterId}" default="0"/></div><form:hidden path="costCenterId"/> </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="costCenterName"><app:message code="admin.costCenter.label.costCenterName"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td><div class="value"><form:input cssClass="inputLong" tabindex="2" path="costCenterName"/></div></td>
                <td></td>
                <td><div class="label"><form:label path="costCenterCode"><app:message code="admin.costCenter.label.costCenterCode"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input cssClass="std numberCode" tabindex="6" path="costCenterCode"/></div></td>

            </tr>
            <tr>
                <td><div class="label"><form:label path="status"><app:message code="admin.costCenter.label.type"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td>
                    <div class="value">
                        <form:select tabindex="3" cssClass="selectLong" path="costCenterType"><form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.costCenterTypes}">
                                <form:option value="${type.object2}"><app:message code="refcodes.COST_CENTER_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="status"><app:message code="admin.costCenter.label.costCenterTaxType"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td>
                    <div class="value">
                        <form:select tabindex="3" cssClass="selectLong" path="costCenterTaxType">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.costCenterTaxTypes}" i18nprefix="refcodes.COST_CENTER_TAX_TYPE.">
                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                            </app:i18nRefCodes>
                        </form:select>
                    </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="status"><app:message code="admin.costCenter.label.status"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td>
                    <div class="value">
                        <form:select tabindex="3" cssClass="selectLong" path="status"><form:option value=""><app:message code="admin.global.select"/></form:option>
                            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}" i18nprefix="refcodes.BUS_ENTITY_STATUS_CD.">
                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                            </app:i18nRefCodes>
                        </form:select>
                    </div>
                </td>
                <td></td>
                <td></td>
                <td></td>
            </tr>

        </table>
    </td>
</tr>


<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>

    <td  colspan="2"  style="padding-top: 0">

        <table class="compact">

            <tbody>

            <tr><td colspan="4" style="padding-top: 0"><div class="subHeader" style="padding-top: 0"><app:message code="admin.costCenter.subheader.options"/></div></td></tr>

            <tr>
                <td width="70px"></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="22" path="allocateFreight" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="allocateFreight"><app:message code="admin.costCenter.subheader.options.allocateFreight"/></form:label>
                    </div>
                </td>
                <td></td>
                <td></td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox  cssClass="checkbox" tabindex="23"  path="allocateDiscount"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="allocateDiscount"><app:message code="admin.costCenter.subheader.options.allocateDiscount"/></form:label>
                    </div>
                </td>
                <td></td>
                <td></td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="24"  path="doNotApplyBudget"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="doNotApplyBudget"><app:message code="admin.costCenter.subheader.options.doNotApplyBudget"/></form:label>
                    </div>
                </td>
                <td></td>
                <td></td>
            </tr>

            </tbody>
        </table>

    </td>


</tr>


<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>
    <td td colspan="2" align="center" style="text-align: center;vertical-align:top;padding-bottom:0;">
        <table width="100%">
            <tr>
                <td style="padding-right: 30px;"><form:button tabindex="30" onclick="${updateAction}"><app:message code="admin.global.button.save"/> </form:button></td>
            </tr>
        </table>
    </td>
</tr>

</table>

</form:form>

</div>

</div>
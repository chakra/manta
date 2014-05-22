<%@ page import="java.util.Date" %>
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/distributor/${distributorConfiguration.distributorId}"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/configuration/save');$('form:first').submit(); return false; "/>

<div class="canvas">

<div class="details">

<form:form modelAttribute="distributorConfiguration" action="" method="POST">
<table>
<tr><td>
    <table>
        <tbody>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="performSalesTaxCheck">
                			<app:message code="admin.distributor.label.configuration.performSalesTaxCheck"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="1" class="radio" path="performSalesTaxCheck" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="2" class="radio" path="performSalesTaxCheck" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                    <form:radiobutton tabindex="3" class="radio" path="performSalesTaxCheck" value="<%=Constants.TRUE_FOR_RESALE %>"/>
                    <app:message code="admin.distributor.configuration.performSalesTaxCheck.option.yesForResale"/>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="exceptionOnOverchargedFreight">
                			<app:message code="admin.distributor.label.configuration.exceptionOnOverchargedFreight"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="4" class="radio" path="exceptionOnOverchargedFreight" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="5" class="radio" path="exceptionOnOverchargedFreight" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="invoiceLoadingPricingModel">
                			<app:message code="admin.distributor.label.configuration.invoiceLoadingPricingModel"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<form:select tabindex="6" cssClass="selectShort" path="invoiceLoadingPricingModel">
						<form:option value="">
							<app:message code="admin.global.select"/>
						</form:option>
                        <app:i18nRefCodes var="code" items="${distributorConfiguration.invoiceLoadingPricingModelChoices}" i18nprefix="">
                            <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        </app:i18nRefCodes>
					</form:select>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="allowFreightOnBackOrders">
                			<app:message code="admin.distributor.label.configuration.allowFreightOnBackorders"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="7" class="radio" path="allowFreightOnBackOrders" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="8" class="radio" path="allowFreightOnBackOrders" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="cancelBackorderedLines">
                			<app:message code="admin.distributor.label.configuration.cancelBackorderedLines"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="9" class="radio" path="cancelBackorderedLines" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="10" class="radio" path="cancelBackorderedLines" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="disallowInvoiceEdits">
                			<app:message code="admin.distributor.label.configuration.disallowInvoiceEdits"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="11" class="radio" path="disallowInvoiceEdits" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="12" class="radio" path="disallowInvoiceEdits" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="receivingSystemTypeCode">
                			<app:message code="admin.distributor.label.configuration.receivingSystemTypeCode"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<form:select tabindex="13" cssClass="selectShort" path="receivingSystemTypeCode">
						<form:option value="">
							<app:message code="admin.global.select"/>
						</form:option>
                        <app:i18nRefCodes var="code" items="${distributorConfiguration.receivingSystemTypeCodeChoices}" i18nprefix="">
                            <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        </app:i18nRefCodes>
					</form:select>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="rejectedInvoiceEmailNotification">
                			<app:message code="admin.distributor.label.configuration.rejectedInvoiceEmailNotification"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:input cssClass="inputShort" tabindex="14" path="rejectedInvoiceEmailNotification" size="35"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="ignoreOrderMinimumForFreight">
                			<app:message code="admin.distributor.label.configuration.ignoreOrderMinimumForFreight"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="15" class="radio" path="ignoreOrderMinimumForFreight" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="16" class="radio" path="ignoreOrderMinimumForFreight" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="invoiceAmountPercentUndercharge">
                			<app:message code="admin.distributor.label.configuration.invoiceAmountPercentAllowanceUndercharge"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:input cssClass="inputShort" tabindex="17" path="invoiceAmountPercentUndercharge" size="35"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="invoiceAmountPercentOvercharge">
                			<app:message code="admin.distributor.label.configuration.invoiceAmountPercentAllowanceOvercharge"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:input cssClass="inputShort" tabindex="18" path="invoiceAmountPercentOvercharge" size="35"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="invoiceMaximumFreightAllowance">
                			<app:message code="admin.distributor.label.configuration.maximumInvoiceFreightAllowed"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:input cssClass="inputShort" tabindex="19" path="invoiceMaximumFreightAllowance" size="35"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="inboundInvoiceHoldDays">
                			<app:message code="admin.distributor.label.configuration.holdInboundInvoice"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:input cssClass="inputShort" tabindex="20" path="inboundInvoiceHoldDays" size="35"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="printCustomerContactInfoOnPurchaseOrder">
                			<app:message code="admin.distributor.label.configuration.printCustomerContactInfo"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="21" class="radio" path="printCustomerContactInfoOnPurchaseOrder" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="22" class="radio" path="printCustomerContactInfoOnPurchaseOrder" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="requireManualPurchaseOrderAcknowledgement">
                			<app:message code="admin.distributor.label.configuration.manualPurchaseOrderAcknowledgementRequired"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td>
                    <form:radiobutton tabindex="23" class="radio" path="requireManualPurchaseOrderAcknowledgement" value="<%=Constants.TRUE %>"/>
                    <app:message code="admin.global.text.yes"/>
                </td>
                <td>
                    <form:radiobutton tabindex="24" class="radio" path="requireManualPurchaseOrderAcknowledgement" value="<%=Constants.FALSE %>"/>
                    <app:message code="admin.global.text.no"/>
                </td>
                <td>
                	&nbsp;
                </td>
            </tr>
            <tr>
                <td>
                	<div class="label">
                		<form:label path="purchaseOrderComments">
                			<app:message code="admin.distributor.label.configuration.purchaseOrderComments"/>
                			<span class="colon">:</span>
                		</form:label>
                	</div>
                </td>
                <td colspan="3">
					<div class="value">
						<form:textarea cssClass="inputShort" tabindex="25" path="purchaseOrderComments"/>
                    </div>
                </td>
            </tr>
			<tr align=center>
			    <td colspan="4" style="vertical-align:top;text-align: center;white-space: nowrap;">
			    	<table width="100%">
			        	<tbody>
			            	<tr>
			                	<td style="text-align: center">
			                    	<div class="actions">
            							<form:button tabindex="26" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                        			</div>
                    			</td>
                			</tr>
            			</tbody>
        			</table>
     			</td>
			</tr>
         </tbody>
    </table>

</td></tr>
</table>
<form:hidden path="distributorId" value="${distributorConfiguration.distributorId}"/>
</form:form>

</div>
</div>


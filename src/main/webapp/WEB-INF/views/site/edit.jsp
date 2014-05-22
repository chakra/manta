<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/location/${site.siteId > 0?site.siteId: 0}'); "/>
<c:set var="applyAction" value="$('form:first').attr('action','${baseUrl}/location/${site.siteId > 0?site.siteId: 0}'+$('input[name=selectedAction]:checked').attr('value'));"/>
<c:set var="getQRCode" value="$('form:first').attr('action','${baseUrl}/location/${site.siteId > 0?site.siteId: 0}'/getQRCode); "/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('siteName');}"/>

<app:dateIncludes/>

<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account/single'
                 action="${baseUrl}/locate/account/selected"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountName"
                 targetIds="accountId"
                 finallyHandler="${finallyHandler}"/>

<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState($('#countryCd').attr("value"),'#state', jscountries, true, 'invisible')});
</script>

<div class="canvas">

<div class="details std">

<form:form modelAttribute="site" action="" method="POST" focus="siteName">

<table width="100%">


<tr>
    <td>
        <table>

            <c:if test="${site.isNew}">
                <tr>
                    <td><div class="label"><label><app:message code="admin.site.label.account"/><span class="colon">:</span><span class="required">*</span></label></div></td>
                    <td colspan="4">
                        <div class="value">
                            <form:input cssClass="std numberCode" tabindex="1" path="accountId"/><form:hidden path="accountName"/>
                            <button  style="margin-left:20px" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button>
                        </div>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td><div class="label"><form:label path="siteId"><app:message code="admin.site.label.siteId"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><c:out value="${site.siteId}" default="0"/></div><form:hidden path="siteId"/> <c:if test="${site.isNew == false}"><form:hidden path="accountId"/><form:hidden path="accountName"/></c:if></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="siteName"><app:message code="admin.site.label.siteName"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td><div class="value"><form:input cssClass="std name" tabindex="2" path="siteName"  focusable="true" id="siteName" style="width:280px"/></div></td>
                <td></td>
                <td><div class="label"><form:label path="locationBudgetRefNum"><app:message code="admin.site.label.locationBudgetRefNum"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input cssClass="std numberCode" tabindex="6" path="locationBudgetRefNum" style="width:170px"/></div></td>

            </tr>
            <tr>
                <td><div class="label"><form:label path="status"><app:message code="admin.site.label.status"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td>
                    <div class="value">
                        <form:select tabindex="3" cssClass="std" path="status" style="width:287px"><form:option value=""><app:message code="admin.global.select"/></form:option>
                            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}" i18nprefix="refcodes.BUS_ENTITY_STATUS_CD.">
                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                            </app:i18nRefCodes>
                        </form:select>
                    </div>
                </td>
                <td></td>
                <td><div class="label"><form:label path="locationDistrRefNum"><app:message code="admin.site.label.locationDistrRefNum"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input tabindex="7" path="locationDistrRefNum" cssClass="std numberCode" style="width:170px"/></div></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="effDate"><app:message code="admin.site.label.effDate"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input path="effDate" tabindex="4" cssClass="datepicker2Col standardCal standardActiveCal" maxlength="10" id="effDate" style="width:280px"/></div></td>
                <td></td>
                <td><div class="label"><form:label path="targetFicilityRank"><app:message code="admin.site.label.targetFicilityRank"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input tabindex="8" path="targetFicilityRank" cssClass="std numberCode" style="width:170px"/></div></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="expDate"><app:message code="admin.site.label.expDate"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input path="expDate" tabindex="5" cssClass="datepicker2Col standardCal standardActiveCal" maxlength="10" id="expDate" style="width:280px"/></div></td>
                <td></td>
                <td><div class="label"><form:label path="locationLineLevelCode"><app:message code="admin.site.label.locationLineLevelCode"/><span class="colon">:</span></form:label></div></td>
                <td><div class="value"><form:input tabindex="9" path="locationLineLevelCode" cssClass="std numberCode" style="width:170px"/></div></td>
            </tr>
            -
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td><div><form:label path="productBundle"><app:message code="admin.site.label.productBundle"/><span class="colon">:</span></form:label></div></td>
                <td colspan="2"><div class="value">
                    <form:select tabindex="10" cssClass="std" path="productBundle" style="width:180px"><form:option value=""><app:message code="admin.global.select"/></form:option>
                        <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.locationProductBundles}" i18nprefix="refcodes.LOCATION_PRODUCT_BUNDLE.">
                            <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        </app:i18nRefCodes>
                    </form:select>
                </div></td>
            </tr>

        </table>
    </td>
    <td align="right" style="text-align: right;vertical-align:top;padding-bottom:0;">

        <table width="100%">

            <tr>
                <td style="padding-right: 30px;">  <form:button onclick="${updateAction}"><app:message code="admin.global.button.save"/> </form:button></td>
            </tr>

            <tr><td>
                <c:if test="${site.isNew == false}">  <table width="100%" style="padding:0">

                    <tr><td align="right"><table style="padding:0;"><tr><td  style="padding:0">
                        <app:box name="admin.global.panel.actions" cssClass="white">
                            <table>
                                <tr><td style="padding:2px;"><form:radiobutton cssClass="radio" path="selectedAction" value="/delete" style="padding:0;"/></td><td  class="label" style="padding:0;margin:0"><app:message code="admin.global.action.delete"/></td> </tr>
                                <tr><td style="padding:2px;"><form:radiobutton  cssClass="radio"  path="selectedAction" value="/clone" style="padding:0"/></td><td   class="label" style="padding:0;margin:0"><app:message code="admin.global.action.clone"/></td> </tr>
                                <tr><td style="padding:2px"><form:radiobutton  cssClass="radio"  path="selectedAction" value="/cloneWAssoc" style="padding:0"/></td><td  class="label" style="padding:0;margin:0"><app:message code="admin.global.action.cloneWithAssoc"/></td> </tr>
                                <tr><td style="padding:2px;margin-top:5px;" colspan="2" align="center"><br><form:button onclick="${applyAction}"><app:message code="admin.global.button.ok"/></form:button></td></tr>
                            </table>
                        </app:box>
                    </td></tr></table></td></tr>

                </table>
                </c:if>
                <c:if test="${site.isNew == true}">
                    <form:hidden path="selectedAction"/>
                </c:if>
            </td></tr>

        </table>
    </td>
</tr>

<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>

    <td  colspan="2"  style="padding-top: 0">

        <table style="padding-top: 0">
            <tbody>

            <tr><td colspan="5" style="padding-top: 0"><div class="subHeader" style="padding-top: 0"><app:message code="admin.site.subheader.contact"/></div></td></tr>

            <tr>
                <td><div class="label"><form:label path="contact.firstName"><app:message code="admin.site.label.contact.firstName"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="11" path="contact.firstName" style="width:255px"/></td>
                <td></td>
                <td><div class="label"><form:label path="contact.address.city"><app:message code="admin.site.label.contact.address.city"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td><form:input tabindex="17" path="contact.address.city" style="width:255px"/></td>
            </tr>

            <tr>
                <td><div class="label"><form:label path="contact.lastName"><app:message code="admin.site.label.contact.lastName"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="12" path="contact.lastName" style="width:255px"/></td>
                <td></td>
                <td><div class="label"><form:label path="contact.address.stateProvinceCd"><app:message code="admin.site.label.contact.address.state"/><span class="colon">:</span><span id="state" class="reqind invisible">*</span></form:label></div></td>
                <td><form:input tabindex="18"  path="contact.address.stateProvinceCd" style="width:255px"/></td>
            </tr>

            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td><div class="label"><form:label path="contact.address.postalCode"><app:message code="admin.site.label.contact.address.postalCode"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td><form:input path="contact.address.postalCode" tabindex="19" style="width:255px"/></td>
            </tr>

            <tr>
                <td><div class="label"><form:label path="contact.address.address1"><app:message code="admin.site.label.contact.address.address1"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td><form:input tabindex="13" path="contact.address.address1" style="width:255px"/></td>
                <td></td>
                <td><div class="label"><form:label path="contact.address.countryCd"><app:message code="admin.site.label.contact.address.country"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
                <td>
                    <form:select tabindex="20" id="countryCd"  path="contact.address.countryCd" onchange="countryUsesState(this.value,'#state', jscountries,false,'invisible')" style="width:264px"><form:option value=""><app:message code="admin.global.select"/></form:option>
                        <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">
                            <form:option value="${country.shortDesc}">${country.uiName}</form:option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="contact.address.address2"><app:message
                        code="admin.site.label.contact.address.address2"/><span
                        class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="14" path="contact.address.address2" style="width:255px"/></td>
                <td></td>
                <td><div class="label"><form:label path="contact.telephone"><app:message code="admin.site.label.contact.telephone"/><span
                        class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="21" path="contact.telephone" style="width:255px"/></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="contact.address.address3"><app:message code="admin.site.label.contact.address.address3"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="15" path="contact.address.address3" style="width:255px"/></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><div class="label"><form:label path="contact.address.address4"><app:message code="admin.site.label.contact.address.address4"/><span class="colon">:</span></form:label></div></td>
                <td><form:input tabindex="16" path="contact.address.address4" style="width:255px"/></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            </tbody>
        </table>

    </td>

</tr>

<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>

    <td  colspan="2"  style="padding-top: 0">

        <table class="compact">

            <tbody>

            <tr><td colspan="4" style="padding-top: 0"><div class="subHeader" style="padding-top: 0"><app:message code="admin.site.subheader.options"/></div></td></tr>

            <tr>
                <td width="70px"></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="22" path="options.allowCorporateScheduledOrder" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.allowCorporateScheduledOrder"><app:message code="refcodes.PROPERTY_TYPE_CD.ALLOW_CORPORATE_SCHED_ORDER" text="ALLOW_CORPORATE_SCHED_ORDER"/></form:label>
                    </div>
                </td>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="26" path="options.enableInventory" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.enableInventory"><app:message code="refcodes.PROPERTY_TYPE_CD.INVENTORY_SHOPPING" text="ENABLE_INV"/></form:label>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox  cssClass="checkbox" tabindex="23"  path="options.showRebillOrder"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.showRebillOrder"><app:message code="refcodes.PROPERTY_TYPE_CD.SHOW_REBILL_ORDER" text="SHOW_REBILL_ORDER"/></form:label>
                    </div>
                </td>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox  cssClass="checkbox" tabindex="27"  path="options.shareBuyerOrderGuides"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.shareBuyerOrderGuides"><app:message code="refcodes.PROPERTY_TYPE_CD.SHARE_BUYER_ORDER_GUIDES" text="SHARE_BUYER_ORDER_GUIDES"/></form:label>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="24"  path="options.byPassOrderRouting"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.byPassOrderRouting"><app:message code="refcodes.PROPERTY_TYPE_CD.BYPASS_ORDER_ROUTING" text="BYPASS_ORDER_ROUTING"/></form:label>
                    </div>
                </td>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox  cssClass="checkbox" tabindex="28" path="options.taxable"  value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.taxable"><app:message code="refcodes.PROPERTY_TYPE_CD.TAXABLE" text="TAXABLE"/></form:label>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div class="label">
                        <form:checkbox cssClass="checkbox" tabindex="25" path="options.consolidatedOrderWarehouse" value="true"/>
                        <form:label cssStyle="padding-left: 5px" path="options.consolidatedOrderWarehouse"><app:message code="refcodes.PROPERTY_TYPE_CD.CONSOLIDATED_ORDER_WAREHOUSE" text="CONSOLIDATED_ORDER_WAREHOUSE"/></form:label>
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

    <td  colspan="2"  style="padding-top: 0">
        <table style="padding-top: 0">

            <tbody>

            <tr><td colspan="5" style="padding-top: 0"><div class="subHeader" style="padding-top: 0"><app:message code="admin.site.subheader.scheduledOrder"/></div></td></tr>

            <c:if test="${site.corporateSchedule!= null}">

                <tr>
                    <td><div class="label"><form:label path="corporateSchedule.scheduleName"><app:message code="admin.site.label.scheduledOrder.name"/><span class="colon">:</span></form:label></div></td>
                    <td><c:out value="${site.corporateSchedule.scheduleName}"/><form:hidden path="corporateSchedule.scheduleName"/></td>
                    <td></td>
                    <td><div class="label"><form:label path="corporateSchedule.intervalHour"><app:message code="admin.site.label.scheduledOrder.intervalHours"/><span class="colon">:</span></form:label></div></td>
                    <td><c:out value="${site.corporateSchedule.intervalHour}"/><form:hidden path="corporateSchedule.intervalHour"/></td>
                </tr>
                <tr>
                    <td><div class="label"><form:label path="corporateSchedule.scheduleId"><app:message code="admin.site.label.scheduledOrder.scheduleId"/><span class="colon">:</span></form:label></div></td>
                    <td><c:out value="${site.corporateSchedule.scheduleId}"/><form:hidden path="corporateSchedule.scheduleId"/></td>
                    <td></td>
                    <td><div class="label"><form:label path="corporateSchedule.cutoffTime"><app:message code="admin.site.label.scheduledOrder.cutoffTime"/><span class="colon">:</span></form:label></div></td>
                    <td><c:out value="${site.corporateSchedule.cutoffTime}"/><form:hidden path="corporateSchedule.cutoffTime"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td><div class="label"><form:label path="corporateSchedule.nextDelivery"><app:message code="admin.site.label.scheduledOrder.nextDelivery"/><span class="colon">:</span></form:label></div></td>
                    <td><c:out value="${site.corporateSchedule.nextDelivery}"/><form:hidden path="corporateSchedule.nextDelivery"/> </td>
                </tr>
            </c:if>

            <c:if test="${site.corporateSchedule== null}">
                <tr><td colspan="5" class="label">(<app:message code="admin.site.text.scheduledOrder.notSetup"/>)</td></tr>
            </c:if>

            </tbody>

        </table>
    </td>
</tr>



<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>

    <td  colspan="2">

        <table>

            <tbody>

            <tr><td><label><app:message code="admin.site.label.orderGuideComments"/><span class="colon">:</span></label></td> </tr>
            <tr><td><form:textarea tabindex="29" path="locationComments" style="width:750px;height:100px"/></td></tr>

            <tr><td style="padding-top: 25px"><label><app:message code="admin.site.label.shippingMessage"/><span class="colon">:</span></label></td> </tr>
            <tr><td><form:textarea tabindex="30" path="locationShipMsg" style="width:750px;height:100px"/></td></tr>

            </tbody>
        </table>

    </td>


</tr>

<!-- QR Code Begin -->
<c:if  test="${site.siteId>0}">

<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>

    <td  colspan="2">

        <table width="100%" >

            <tbody>
			<tr>
				<td>
					<table >
						<tr>
							<td colspan="2"><div class="subHeader"><span style="color:black"><app:message code="admin.site.label.rq.locationQRCode"/></span></div></td>
						</tr>
						<tr>
							<td><div class="label"><span style="color:black"><app:message code="admin.site.label.rq.siteName"/><span class="colon">:</span></span></div></td>
			                <td><div class="value"><span style="color:black"><c:out value="${site.siteName}" default="0"/></div></td>
						</tr>
						<tr>
			                <td><div class="label"><span style="color:black"><app:message code="admin.site.label.rq.referenceNum"/><span class="colon">:</span></span></div></td>
			                <td><div class="value"><span style="color:black"><c:out value="${site.locationBudgetRefNum}" default="0"/></span></div></td>
						</tr>
						<tr>
			                <td><div class="label"><span style="color:black"><app:message code="admin.site.label.rq.address.streetAddress"/><span class="colon">:</span></span></div></td>
			                <td><div class="value"><span style="color:black"><c:out value="${site.contact.address.address1}" default="0"/></span></div></td>
						</tr>
						<tr>
			                <td><div class="label"><span style="color:black"><app:message code="admin.site.label.rq.address.city"/><span class="colon">:</span></span></div></td>
			                <td><div class="value"><span style="color:black"><c:out value="${site.contact.address.city}" default="0"/></span></div><td>
						</tr>

					</table>
				</td>

				<td>
				     <img  src="${baseUrl}/location/${site.siteId}/getQRCode?desc=${site.siteName}" border="0"/>
				</td>

				<td width="30%">
					<table class="boxContent">
						<tr><td><div class="label"><span style="color:black"><app:message code="admin.site.label.rq.instructions"/></span></div></td></tr>
						<tr><td><div class="value"><span style="color:black"><app:message code="admin.site.label.rq.print"/></span></div></td></tr>
						<tr><td><div class="value"><span style="color:black"><app:message code="admin.site.label.rq.affix"/></span></div></td></tr>
						<tr><td><div class="value"><span style="color:black"><app:message code="admin.site.label.rq.scan"/><span class="required">*</span></span></div></td></tr>
						<tr><td><div class="value"><span style="color:black"><span class="required">*</span><app:message code="admin.site.label.rq.scanMessage"/></span></div></td></tr>
					</table>
				</td>

			</tr>
            </tbody>

        </table>

    </td>


</tr>
</c:if>
<!-- QR Code End -->


<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>

<tr>
    <td></td>
    <td align="right" style="text-align: right;vertical-align:top;padding-bottom:0;">
        <table width="100%">
            <tr>
                <td style="padding-right: 30px;"><form:button tabindex="30" onclick="${updateAction}"><app:message code="admin.global.button.save"/> </form:button></td>
            </tr>
        </table>
    </td>
</tr>

</table>

<form:hidden path="cloneCode"/>
<form:hidden path="cloneId"/>

</form:form>

</div>

</div>
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.auth.AppUser" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/distributor/${distributor.distributorId > 0 ? distributor.distributorId: 0}');$('form:first').submit(); return false; "/>
<c:set var="resetAction"  value="$('form:first').attr('action','${baseUrl}/distributor/${distributor.distributorId > 0 ? distributor.distributorId: 0}/reset');$('form:first').submit(); return false;"/>

<app:message var="dateFormat" code="format.prompt.dateFormat" />

<app:dateIncludes/>

<script type="text/javascript">
    var  jscountries = []; <c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">jscountries["<c:out  value='${country.shortDesc}'/>"] = "${country.usesState}";</c:forEach>
    $(document).ready(function(){ countryUsesState($('#countryCd').attr("value"),'#state', jscountries, true, 'invisible')});
</script>

<div class="canvas">

<div class="details">

<form:form modelAttribute="distributor" id="distributor" action="" method="POST">
<table width="100%">

<tr>
    <td>
        <table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: left">
                    <div class="row">
                        <div class="cell">
                            <table>
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorId">
                                            	<app:message code="admin.distributor.label.id"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                    	<div class="labelValue">
                                    		<c:out value="${distributor.distributorId}" default="0"/>
                                    	</div>
                                    	<form:hidden path="distributorId" value="${distributor.distributorId}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorName">
                                            	<app:message code="admin.distributor.label.name"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            	<span class="required">
                                            		*
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                    	<div class="value">
                                        	<form:input tabindex="1" path="distributorName" cssClass="inputMid"/>
                                        </div>
                                    </td>
					                <td>
					                	<div class="label">
					                		<form:label path="distributorDisplayName">
					                			<app:message code="admin.distributor.label.displayName"/>
					                			<span class="colon">
					                				:
					                			</span>
					                		</form:label>
					                	</div>
					                </td>
					                <td>
					                	<div class="value">
					                		<form:input tabindex="5" path="distributorDisplayName" cssClass="inputMid"/>
					                	</div>
					                </td>
                                </tr>

                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorType">
                                            	<app:message code="admin.distributor.label.type"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="2" path="distributorType" cssClass="selectMid">
                                            <form:option value="">
                                            	<app:message code="admin.global.select"/>
                                            </form:option>
				                            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.distributorTypeCds}" i18nprefix="refcodes.DISTRIBUTOR_TYPE_CD.">
				                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
				                            </app:i18nRefCodes>
                                        </form:select>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorStatus">
                                            	<app:message code="admin.distributor.label.status"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            	<span class="required">
                                            		*
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="6" path="distributorStatus" cssClass="selectMid">
                                            <form:option value="">
                                            	<app:message code="admin.global.select"/>
                                            </form:option>
				                            <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.distributorStatusCds}" i18nprefix="refcodes.DISTRIBUTOR_STATUS_CD.">
				                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
				                            </app:i18nRefCodes>
                                        </form:select>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorCallCenterHours">
                                            	<app:message code="admin.distributor.label.callCenterHours"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                    	<div class="value">
                                        	<form:input tabindex="3" path="distributorCallCenterHours"  cssClass="inputMid"/>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorLocale">
                                            	<app:message code="admin.distributor.label.locale"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="7" path="distributorLocale" cssClass="selectMid">
                                            <form:option value="<%=Constants.UNK%>">
                                            	<app:message code="admin.global.select.default"/>
                                            </form:option>
				                            <app:i18nRefCodes var="code" items="${distributor.localeChoices}" i18nprefix="">
				                                <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
				                            </app:i18nRefCodes>
                                        </form:select>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorCompanyCode">
                                            	<app:message code="admin.distributor.label.companyCode"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                    	<div class="value">
                                        	<form:input tabindex="4" path="distributorCompanyCode"  cssClass="inputMid"/>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="label">
                                            <form:label path="distributorCustomerReferenceCode">
                                            	<app:message code="admin.distributor.label.customerReferenceCode"/>
                                            	<span class="colon">
                                            		:
                                            	</span>
                                            </form:label>
                                        </div>
                                    </td>
                                    <td>
                                    	<div class="value">
                                        	<form:input tabindex="8" path="distributorCustomerReferenceCode" cssClass="inputMid"/>
                                        </div>
                                    </td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                    </div>

                </td>
            </tr>

        </table>
    </td>
</tr>

<tr><td style="padding-bottom: 0"><hr/></td></tr>

<tr>
    <td style="padding-top: 0">
        <table style="padding-top: 0">
            <tbody>
            	<tr>
            		<td colspan="5" style="padding-top: 0">
            			<div class="subHeader" style="padding-top: 0">
            				<app:message code="admin.distributor.subheader.contact"/>
            			</div>
            		</td>
            	</tr>
	            <tr>
    	            <td>
    	            	<div class="label">
    	            		<form:label path="contact.firstName">
    	            			<app:message code="admin.distributor.label.contact.firstName"/>
    	            			<span class="colon">
    	            				:
    	            			</span>
    	            		</form:label>
    	            	</div>
    	            </td>
                	<td>
                		<form:input tabindex="9" path="contact.firstName" cssClass="inputMid"/>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.city">
                				<app:message code="admin.distributor.label.contact.address.city"/>
                				<span class="colon">
                					:
                				</span>
                				<span class="required">
                					*
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="14" path="contact.address.city" cssClass="inputMid"/>
                	</td>
            	</tr>

	            <tr>
    	            <td>
    	            	<div class="label">
    	            		<form:label path="contact.lastName">
    	            			<app:message code="admin.distributor.label.contact.lastName"/>
    	            			<span class="colon">
    	            				:
    	            			</span>
    	            		</form:label>
    	            	</div>
    	            </td>
                	<td>
                		<form:input tabindex="10" path="contact.lastName" cssClass="inputMid"/>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.stateProvinceCd">
                				<app:message code="admin.distributor.label.contact.address.state"/>
                				<span class="colon">
                					:
                				</span>
                				<span id="state" class="reqind invisible">
                					*
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="15"  path="contact.address.stateProvinceCd" cssClass="inputMid"/>
                	</td>
            	</tr>

            	<tr>
                	<td>
                	</td>
                	<td>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.postalCode">
                				<app:message code="admin.distributor.label.contact.address.postalCode"/>
                				<span class="colon">
                					:
                				</span>
                				<span class="required">
                					*
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input path="contact.address.postalCode" tabindex="16" cssClass="inputMid"/>
                	</td>
            	</tr>

            	<tr>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.address1">
                				<app:message code="admin.distributor.label.contact.address.address1"/>
                				<span class="colon">
                					:
                				</span>
                				<span class="required">
                					*
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="11" path="contact.address.address1" cssClass="inputMid"/>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.countryCd">
                				<app:message code="admin.distributor.label.contact.address.country"/>
                				<span class="colon">
                					:
                				</span>
                				<span class="required">
                					*
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                    	<form:select tabindex="17" id="countryCd"  path="contact.address.countryCd" onchange="countryUsesState(this.value,'#state', jscountries,false,'invisible')" cssClass="selectMid">
                    		<form:option value="">
                    			<app:message code="admin.global.select"/>
                    		</form:option>
                        	<c:forEach var="country" items="${requestScope['appResource'].dbConstantsResource.countries}">
                            	<form:option value="${country.shortDesc}">${country.uiName}</form:option>
                        	</c:forEach>
                    	</form:select>
                	</td>
            	</tr>
            	<tr>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.address2">
                				<app:message code="admin.distributor.label.contact.address.address2"/>
                				<span class="colon">
                					:
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="12" path="contact.address.address2" cssClass="inputMid"/>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.telephone">
                				<app:message code="admin.distributor.label.contact.telephone"/>
                				<span class="colon">
                					:
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="18" path="contact.telephone" cssClass="inputMid"/>
                	</td>
            	</tr>
            	<tr>
                	<td>
                		<div class="label">
                			<form:label path="contact.address.address3">
                				<app:message code="admin.distributor.label.contact.address.address3"/>
                				<span class="colon">
                					:
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="13" path="contact.address.address3" cssClass="inputMid"/>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.fax">
                				<app:message code="admin.distributor.label.contact.fax"/>
                				<span class="colon">
                					:
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="19" path="contact.fax" cssClass="inputMid"/>
                	</td>
            	</tr>
            	<tr>
<%
					//Commented out due to MANTA-353, replaced by two empty <td> tags below
                	//<td>
                		//<div class="label">
                			//<form:label path="contact.address.address4">
                				//<app:message code="admin.distributor.label.contact.address.address4"/>
                				//<span class="colon">
                					//:
                				//</span>
                			//</form:label>
                		//</div>
                	//</td>
                	//<td>
                		//<form:input tabindex="20" path="contact.address.address4" style="width:255px"/>
                	//</td>
%>
                	<td>
                	</td>
                	<td>
                	</td>
                	<td>
                	</td>
                	<td>
                		<div class="label">
                			<form:label path="contact.email">
                				<app:message code="admin.distributor.label.contact.email"/>
                				<span class="colon">
                					:
                				</span>
                			</form:label>
                		</div>
                	</td>
                	<td>
                		<form:input tabindex="21" path="contact.email" cssClass="inputMid"/>
                	</td>
            	</tr>
            </tbody>
        </table>
    </td>
</tr>

<tr align=center>
    <td style="vertical-align:top;text-align: center;white-space: nowrap;">
    	<table width="100%">
        	<tbody>
            	<tr>
                	<td style="text-align: center">
                    	<div class="actions">
                        	<form:button tabindex="1000" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                            <c:if test="${distributor.distributorId != null && distributor.distributorId > 0}">
                            	&nbsp;&nbsp;&nbsp; <form:button tabindex="1001" onclick="${resetAction} return false;"><app:message code="admin.global.button.reset"/></form:button>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
     </td>
</tr>

<%
	AppUser appUser = Auth.getAppUser();
    if (appUser.isAdmin() || appUser.isSystemAdmin()) {
%>
<c:if test="${distributor.distributorId != null && distributor.distributorId > 0}">
<tr>
	<td style="padding-bottom: 0">
		<hr/>
	</td>
</tr>
<tr>
	<td style="padding-bottom: 0">
        <div class="resultCount label">
            <label><app:message code="admin.distributor.label.tradingPartners.header"/><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(distributor.tradingPartners)}
        </div>
	</td>
</tr>
<c:if test="${distributor.tradingPartners != null}">
	<tr>
		<td style="padding-bottom: 0">
			<div class="search">
        	<table class="searchResult" width="100%">
            	<colgroup>
                	<col width="10%"/>
                	<col width="10%"/>
                	<col width="50%"/>
                	<col width="10%"/>
                	<col width="20%"/>
            	</colgroup>
            	<thead class="header">
            		<tr class="row">
                		<th class="cell cell-number"><strong><app:message code="admin.tradingPartner.label.id"/></strong></th>
                		<th class="cell cell-number">&nbsp;</th>
                		<th class="cell cell-text"><strong><app:message code="admin.tradingPartner.label.name"/></strong></th>
                		<th class="cell cell-number">&nbsp;</th>
                		<th class="cell cell-text"><strong><app:message code="admin.tradingPartner.label.status"/></strong></th>
            		</tr>
            	</thead>
            	<tbody class="body">
            	<c:forEach var="tradingPartner" items="${distributor.tradingPartners}" >
                	<tr class="row">
                		<td class="cell cell-number"><c:out value="${tradingPartner.tradingPartnerId}"/></td>
                		<td class="cell cell-text">&nbsp;</td>
                    	<td class="cell cell-text"><c:out value="${tradingPartner.shortDesc}"/></td>
                		<td class="cell cell-text">&nbsp;</td>
                    	<td class="cell cell-text"><app:message code="refcodes.TRADING_PARTNER_STATUS_CD.${tradingPartner.tradingPartnerStatusCd}" text="${tradingPartner.tradingPartnerStatusCd}"/></td>
                	</tr>
            	</c:forEach>
            	</tbody>
        	</table>
        	</div>
		</td>
	</tr>
</c:if>
</c:if>
<%
    }
%>

</table>
</form:form>

</div>
</div>


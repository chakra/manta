<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/storeMessage/${storeMessageConfiguration.storeMessageId}/configuration"/>

<c:set var="searchUrl" value="${baseUrl}/accounts"/>
<c:set var="sortUrl" value="${baseUrl}/accounts/sortby"/>
<c:set var="updateAllAction" value="$('form#storeMessageAccounts').attr('action','${baseUrl}/accounts/update/all');;$('form:#storeMessageAccounts').submit(); return false; "/>
<c:set var="updateUrl" value="${baseUrl}/accounts/update"/>
<c:set var="updateAction" value="$('form:#storeMessageAccounts').submit(); return false;"/>
<c:set var="customerManagedMessage" value="${storeMessageConfiguration.messageManagedByCustomer}"/>

<div class="canvas">
    <div class="search">
        <c:if test="${customerManagedMessage}">
        <table width="100%">
            <tr>
                <td align="left">
                    <label><span class="reqind"><app:message code="admin.message.configuration.text.customerManagedMessage" /></span></label><label class="value"></label>
                </td>
            </tr>
            <tr>
                <td align="left">
                    &nbsp;
                </td>
            </tr>
        </table>
        </c:if>
        <c:if test="${storeMessageConfiguration.messageManagedByCustomer != true}">
        <form:form id="search" modelAttribute="storeMessageConfigurationFilter" method="GET" action="${searchUrl}" focus="filterValue">
            <div class="filter" >
                <table>
                    <tr>
                        <td class="cell label"><label><app:message code="admin.message.configuration.label.accounts" /><span class="colon">:</span></label></td>
                        <td class="cell value"><form:input tabindex="1" id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                        <td class="cell">&nbsp;&nbsp;&nbsp;<form:radiobutton tabindex="2" path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.ID%>"/><span class="label"><app:message code="admin.global.filter.label.ID"/></span>
                            <form:radiobutton tabindex="2" cssClass="radio" path="filterType" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.nameStartWith"/></span>
                            <form:radiobutton tabindex="2" cssClass="radio" path="filterType" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.nameContains"/></span>
                        </td>
                    </tr>
                    <tr>
						<td  class="cell label"></td>
                        <td  class="cell" cospan="2"><br>
                            <label><form:checkbox tabindex="3" cssClass="checkbox" path="showConfiguredOnly"/><app:message code="admin.global.configuration.label.showConfiguredOnly" /></label>
							<br>
                            <label><form:checkbox tabindex="4" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
					</tr>
					<tr>
						<td  class="cell label"></td>
                        <td  class="cell" colspan="2"><br>
							<form:button  id="search"  tabindex="5" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
							&nbsp;&nbsp;&nbsp;&nbsp;<form:button  tabindex="6" onclick="${updateAllAction}"><app:message code="admin.message.configuration.button.configureAllAccounts"/></form:button>
						</td>
                    </tr>
                </table>
            </div>
        </form:form>
        </c:if>
        <form:form   id="storeMessageAccounts"  modelAttribute="storeMessageConfiguration" method="POST" action="${updateUrl}">
        <c:if test="${storeMessageConfiguration.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label">
                            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(storeMessageConfiguration.result)}</div>
                        </td>
                        <td align="right">
                        	<c:if test="${fn:length(storeMessageConfiguration.result) > 0}">
                            <form:button onclick="${updateAction}" disabled="${customerManagedMessage}"><app:message code="admin.global.button.save" /></form:button>
                            </c:if>
                        </td>
                    </tr>
                </table>

        </c:if>


            <c:if test="${storeMessageConfiguration.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="10%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="30%"/>
                    </colgroup>
                    <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/busEntityId"><app:message code="admin.global.filter.label.accountId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/shortDesc"><app:message code="admin.global.filter.label.accountName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/busEntityStatusCd"><app:message code="admin.global.filter.label.accountStatus" /></a></th>
                        <th class="cell cell-text cell-element">
                            <a href="javascript:checkAll('storeMessageAccounts', 'accounts.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('storeMessageAccounts', 'accounts.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody class="body">
                    <c:forEach var="account" varStatus="i" items="${storeMessageConfiguration.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${account.value.busEntityId}"/> </td>
                            <td class="cell cell-text"><c:out value="${account.value.shortDesc}"/> </td>
                            <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${account.value.busEntityStatusCd}" text="${account.value.busEntityStatusCd}"/></td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="accounts.selectableObjects[${i.index}].selected" disabled="${customerManagedMessage}"/></td>
                            <td>&nbsp;</td>
                        </tr>
                    </c:forEach>
                    <tr><td colspan="5"><br><br></td></tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                        <td align="right">
                        	<c:if test="${fn:length(storeMessageConfiguration.result) > 0}">
                            <form:button onclick="${updateAction}" disabled="${customerManagedMessage}"><app:message code="admin.global.button.save" /></form:button>
                            </c:if>
                        </td><td></td>
                    </tr>
                    </tbody>

                </table>
            </c:if>
        </form:form>

</div>
</div>


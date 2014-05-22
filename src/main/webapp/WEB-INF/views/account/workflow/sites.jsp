<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="workflowIdToEdit" value="${(empty  accountWorkflowSiteFilter.workflowId ? 0 : accountWorkflowSiteFilter.workflowId)}"/>
<c:set var="workflowTypeToEdit" value="${(empty  accountWorkflowSiteFilter.workflowTypeCd ? 'new' : accountWorkflowSiteFilter.workflowTypeCd)}"/>

<app:url var="baseUrl" value="/account/${accountWorkflowSiteFilter.accountId}/workflow/${workflowIdToEdit}/${workflowTypeToEdit}/sites"/>
<app:url var="detailUrl" value="/account/${accountWorkflowSiteFilter.accountId}/workflow/${workflowIdToEdit}/${workflowTypeToEdit}"/>

<%-- <c:set var="searchUrl" value="${baseUrl}/filter"/> --%>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>

<c:set var="searchAction" value="$('form:first').attr('action','${baseUrl}/filter');$('form:first').submit(); return false; "/>
<c:set var="assignAllAction" value="$('form:first').attr('action','${baseUrl}/assignAll');$('form:first').submit(); return false; "/>

<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#accountWorkflowSites').submit(); return false;"/>

<app:url var="editUrl" value="/location"/>


<!-- <div class="canvas"> -->

    <div class="search">

        <form:form id="search" modelAttribute="accountWorkflowSiteFilter" method="GET" focus="siteName">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                    	<td colspan="2">
	                       <div class="cell label"><label><app:message code="admin.account.workflow.label.workflowId" /><span class="colon">:</span></label>&nbsp;&nbsp;</div>
	                       <div class="cell"><c:out value="${accountWorkflowSiteFilter.workflowId}" default="0"/></div><form:hidden  path="workflowId"/>
                    	</td>
                    	<td colspan="2">
	                       <div class="cell label"><label><app:message code="admin.account.workflow.label.workflowName" /><span class="colon">:</span></label>&nbsp;&nbsp;</div>
	                       <div class="cell"><a href="${detailUrl}"><c:out value="${accountWorkflowSiteFilter.workflowName}" default=""/></a></div>
                    	</td>
                    	<td></td>
                    </tr>
                    <tr><td colspan="5">&nbsp;</td></tr>
                    <tr>
                        <td colspan="5">
                            <div class="subHeader"><app:message code="admin.account.workflow.sites.label.workflowLocationAssociations"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="5">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
   					    <td class="cell label"><label><app:message code="admin.global.filter.label.siteId" /><span class="colon">:</span></label></td>
                        <td colspan="3" class="cell value"><form:input tabindex="2" focusable="true" id="siteId" path="siteId" cssClass="filterValue" size="14"/></td>
<!--                         <td >&nbsp;</td> -->
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label"><label><app:message code="admin.global.filter.label.siteName" /><span class="colon">:</span></label></td>
                        <td class="cell value"><form:input tabindex="3" focusable="true" id="siteName" path="siteName" cssClass="filterValue"/></td>
                        <td class="cell" style="padding-top: 0;padding-bottom: 0">
                            <form:radiobutton tabindex="4" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton tabindex="4" path="siteNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label"><label><app:message code="admin.global.filter.label.siteRefNumber" /><span class="colon">:</span></td>
                        <td class="cell value"><form:input tabindex="5" path="refNumber" focusable="true" cssClass="filterValue"/></td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton focusable="true" tabindex="6" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton focusable="true" tabindex="6" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td class="cell" > </td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td  class="cell label"><label><app:message code="admin.global.filter.label.city" /><span class="colon">:</span></td>
                        <td colspan="3" class="cell value"><form:input tabindex="7" path="city" focusable="true" cssClass="filterValue" /></td>
<!--                         <td colspan="1" class="cell" > </td> -->
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label"><label><app:message code="admin.global.filter.label.state" /><span class="colon">:</span></td>
                        <td colspan="3" class="cell value"><form:input tabindex="8" path="state" focusable="true" cssClass="filterValue"/></td>
<!--                         <td colspan="2" class="cell"> </td>  -->
                    </tr>
                                        <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label"><label><app:message code="admin.global.filter.label.postalCode" /><span class="colon">:</span></td>
                        <td colspan="3" class="cell value"><form:input tabindex="9" path="postalCode" focusable="true" cssClass="filterValue"/></td>
                    </tr>


                    <tr>
						<td class="cell cell-text">&nbsp;</td>
                        <td  class="cell label">&nbsp;</td>
                        <td  colspan="3" class="cell"><form:checkbox tabindex="10" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.account.workflow.sites.label.associatedSitesOnly" /></label>
                        </td>
<!--                         <td colspan="2">&nbsp;</td> -->
                    </tr>
                    <tr>
						<td class="cell cell-text">&nbsp;</td>
                        <td  class="cell label">&nbsp;</td>
                        <td  colspan="3" class="cell"><form:checkbox tabindex="11" cssClass="checkbox"  path="showInactive"/>
                            <label><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
<!--                         <td colspan="2">&nbsp;</td> -->
                    </tr>
                    <tr><td colspan="5">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell" colspan="3" >
                            <form:button  tabindex="12" onclick="${searchAction} return false;"><app:message code="admin.global.button.search"/></form:button>
							&nbsp;&nbsp;&nbsp;
                            <form:button  tabindex="13" onclick="${assignAllAction} return false;"><app:message code="admin.account.workflow.sites.label.assignAllLocations"/></form:button>
                        </td>
<!--                         <td >&nbsp;</td> -->
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="accountWorkflowSites" modelAttribute="accountWorkflowSiteFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${accountWorkflowSiteFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(accountWorkflowSiteFilterResult.result)}</div>
                        </td>
                       <c:if test="${accountWorkflowSiteFilterResult.result.size() > 0}">
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                       </c:if>
                    </tr>
                </table>
            </c:if>

            <c:if test="${accountWorkflowSiteFilterResult.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="8%"/>
                        <col width="15%"/>
                        <col width="17%"/>
                        <col width="17%"/>
                        <col width="10%"/>
                        <col width="8%"/>
                        <col width="8%"/>
                        <col width="8%"/>
                    </colgroup>
                    <thead class="header">
                    <tr class="row">
		                <th class="cell cell-number"><a class="sort" href="${sortUrl}/siteId"><app:message code="admin.global.filter.label.siteId" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.siteName" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.streetAddress" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/state"><app:message code="admin.global.filter.label.state" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.postalCode" /></a></th>
		                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.siteStatus" /></a></th>
                        <th class="cell cell-text cell-element" nowrap>
                            <a href="javascript:checkAll('accountWorkflowSites', 'sites.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('accountWorkflowSites', 'sites.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="body">
                    <c:forEach var="site" varStatus="i" items="${accountWorkflowSiteFilterResult.result}" >
		                <tr class="row">
		                    <td class="cell cell-number"><c:out value="${site.value.siteId}"/></td>
 		                    <td class="cell cell-text"><a href="${editUrl}/${site.value.siteId}"><c:out value="${site.value.siteName}"/></a></td>
		                    <td class="cell cell-text"><c:out value="${site.value.address1}"/></td>
		                    <td class="cell cell-text"><c:out value="${site.value.city}"/></td>
		                    <td class="cell cell-text"><c:out value="${site.value.state}"/></td>
		                    <td class="cell cell-text"><c:out value="${site.value.postalCode}"/></td>
		                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${site.value.status}" text="${site.value.status}"/></td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="sites.selectableObjects[${i.index}].selected"/></td>
 		                </tr>
		            </c:forEach>
                    <c:if test="${accountWorkflowSiteFilterResult.result.size() > 0}">
                    	<tr>
                    		<td colspan="8"><br><br>
                    		</td>
                    	</tr>
	                    <tr>
	                        <td colspan="7">&nbsp;</td>
	                        <td align="right"><form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button></td>
	                    </tr>
	                </c:if>
                    </tbody>

                </table>
            </c:if>
        </form:form>

</div>
<!-- </div> -->


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>

<app:url var="searchUrl" value="/emailTemplate/filter"/>
<app:url var="sortUrl" value="/emailTemplate/filter/sortby"/>
<app:url var="sortByTypeUrl" value="/emailTemplate/filter/sortbyType"/>
<app:url var="editUrl" value="/emailTemplate"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<div class="search">

    <form:form modelAttribute="emailTemplateFilter" method="GET" action="${searchUrl}" focus="filterValue">

        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.template.email.label.templateId" /><span class="colon">:</span></label></td>
                    <td class="cell value">
                   	 <form:input tabindex="1" id="filterId" path="filterId" cssClass="filterValue"/>
                    </td>
                </tr>
                 <tr>
                    <td class="cell label first"><label><app:message code="admin.template.email.label.templateName" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="2" id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell" style="padding-top: 0;">
                        <form:radiobutton tabindex="3" path="filterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="3" path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>

                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="2"><form:button  id="search" cssClass="button" tabindex="6"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;<form:button  tabindex="7" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                    </td>
                </tr>
            </table><br><br>
        </div>
    </form:form>

    <c:if test="${emailTemplateFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(emailTemplateFilterResult.result)}</div>
    </c:if>

    <c:if test="${emailTemplateFilterResult.result != null}">
    	<c:set var="emailTypeMap" value=""/>
	    <c:forEach var="emailTemplate" items="${emailTemplateFilterResult.result}" >
	    	<c:set var="emailType" value="${emailTemplate.emailType}"/>
	    	<c:set var="emailType" value="<%=AppI18nUtil.getMessageOrDefault(\"admin.template.email.emailType.\"+pageContext.getAttribute(\"emailType\"), (String)pageContext.getAttribute(\"emailType\"))%>"/>
	    	<c:set var="emailTypeMap" value="${emailTypeMap}${emailTemplate.templateId}:${emailType},"/>
	    </c:forEach>

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="8%"/>
                <col width="25%"/>
                <col width="20%"/>
                <col width="25%"/>
                <col/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/templateId"><app:message code="admin.global.filter.label.emailTemplateId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/name"><app:message code="admin.global.filter.label.emailTemplateName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/locale"><app:message code="admin.global.filter.label.locale" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortByTypeUrl}/${emailTypeMap}"><app:message code="admin.global.filter.label.emailType" /></a></th>
                <th></th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="emailTemplate" items="${emailTemplateFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${emailTemplate.templateId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${emailTemplate.templateId}"><c:out value="${emailTemplate.name}"/></a></td>
                    <td class="cell cell-text"><c:out value="${emailTemplate.locale}"/></td>
                    <td class="cell cell-text"><c:out value="${emailTemplate.emailType}"/></td>
                    <td></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


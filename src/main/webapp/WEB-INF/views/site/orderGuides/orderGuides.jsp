<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/location/${siteHeader.siteId}"/>

<app:url var="baseUtl"/>

<app:url var="sortUrl" value="/location/${siteHeader.siteId}/orderGuides/sortby"/>
<app:url var="editUrl" value="/location/${siteHeader.siteId}/orderGuides"/>


<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<div class="canvas">
	<div class="search">
    <div>
        <br>
        <div class="subHeader"><app:message code="admin.global.filter.header.orderGuides"/></div>
        <br>
    </div>

    <form:form id="siteOrderGuide" modelAttribute="siteOrderGuide" method="GET">
      <form:hidden path="siteId" value="${siteHeader.siteId}"/>
                <table>
                    <tr>
                        <td class="cell">
                            <form:button focusable="true" tabindex="15" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        </td>

                    </tr>
                </table> <br><br>


    <c:if test="${siteOrderGuide.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(siteOrderGuide.result)}</div>
    </c:if>
    <c:if test="${siteOrderGuide.result != null}">

        <table class="searchResult" width="100%">

            <colgroup>
                <col width="15%"/>
                <col width="30%"/>
                <col width="25%"/>
                <col width="15%"/>
                <col width="15%"/>
            </colgroup>

            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/orderGuideId"><app:message code="admin.global.filter.label.orderGuideId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderGuideName"><app:message code="admin.global.filter.label.orderGuideName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogName"><app:message code="admin.global.filter.label.catalogName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/catalogStatus"><app:message code="admin.global.filter.label.ogCatalogStatus" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/orderGuideType"><app:message code="admin.global.filter.label.orderGuideType" /></a></th>
            </tr>
            </thead>

            <tbody class="body">

            <c:forEach var="orderGuide" items="${siteOrderGuide.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${orderGuide.orderGuideId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${orderGuide.orderGuideId}"><c:out value="${orderGuide.orderGuideName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${orderGuide.catalogName}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${orderGuide.catalogStatus}" text="${orderGuide.catalogStatus}"/></td>
                    <td class="cell cell-text"><c:out value="${orderGuide.orderGuideType}"/></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
    </form:form>
</div>
</div>

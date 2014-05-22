<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/storeMessage/filter"/>
<app:url var="sortUrl" value="/storeMessage/filter/sortby"/>
<app:url var="editUrl" value="/storeMessage"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<app:dateIncludes/>

<div class="search">
    <form:form modelAttribute="storeMessageFilter" method="GET" action="${searchUrl}" focus="filterValue">
        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.message.label.storeMessageId" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="2" focusable="true" id="filterId" path="filterId" cssClass="filterValue"/></td>
					<td colspan="2" class="cell">&nbsp;</td>
                </tr>

                 <tr>
                    <td class="cell label first"><label><app:message code="admin.message.label.storeMessageName" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell">
                    	<form:radiobutton path="filterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.message.label.storeMessageTitle" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input id="title" path="title" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell">
                    	<form:radiobutton path="titleFilterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton path="titleFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.message.label.datePostedFrom" /><span class="colon">:</span></td>
                    <td class="cell value"><form:input path="postedDateFrom" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                 <tr>
                    <td class="cell label first"><label><app:message code="admin.message.label.datePostedTo" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input path="postedDateTo" cssClass="datepicker2Col standardCal standardActiveCal"/></td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="3"><label><br><form:checkbox cssClass="checkbox" path="showPublished"/><app:message code="admin.global.filter.label.publishedOnly" /></label></td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="3"><label><form:checkbox cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                </tr>
                <tr>
					<td class="cell label first"></td>
                    <td class="cell" align="left" colspan="3"><form:button  id="search" cssClass="button"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;<form:button  onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
					</td>
                </tr>
            </table><br><br> </div>
    </form:form>

    <c:if test="${storeMessageFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(storeMessageFilterResult.result)}</div>
    </c:if>
    <c:if test="${storeMessageFilterResult.result != null}">

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="10%"/>
                <col width="15%"/>
                <col width="20%"/>
                <col width="10%"/>
                <col width="10%"/>
                <col width="10%"/>
                <col width="10%"/>
                <col width="5%"/>
                <col width="5%"/>
                <col width="5%"/>
          </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/storeMessageId"><app:message code="admin.global.filter.label.messageId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/shortDesc"><app:message code="admin.message.label.storeMessageName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/messageTitle"><app:message code="admin.global.filter.label.messageTitle" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/postedDate"><app:message code="admin.global.filter.label.datePosted" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/messageType"><app:message code="admin.message.label.messageType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/published"><app:message code="admin.global.filter.label.messagePublished" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/modBy"><app:message code="admin.global.filter.label.lastModBy" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/languageCd"><app:message code="admin.global.filter.label.language" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/country"><app:message code="admin.global.filter.label.country" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/storeMessageStatusCd"><app:message code="admin.global.filter.label.status" /></a></th>
  </tr>

    </thead>
            <tbody class="body">
            <c:forEach var="message" items="${storeMessageFilterResult.result}" >

                <tr class="row">
                    <td class="cell cell-number"><c:out value="${message.storeMessageId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${message.storeMessageId}"><c:out value="${message.shortDesc}"/></a></td>
                    <td class="cell cell-text"><c:out value="${message.messageTitle}"/></td>
                    <td class="cell cell-text"><app:formatDate value="${message.postedDate}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.MESSAGE_TYPE_CD.${message.messageType}"/></td>
                    <td class="cell cell-text"><c:out value="${message.published}"/></td>
                    <td class="cell cell-text"><c:out value="${message.modBy}"/></td>
                    <td class="cell cell-text"><c:out value="${message.languageCd}"/></td>
                    <td class="cell cell-text"><c:out value="${message.country}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.STORE_MESSAGE_STATUS_CD.${message.storeMessageStatusCd}"/></td>
               </tr>
            </c:forEach>

            </tbody>

        </table>   </c:if>
</div>


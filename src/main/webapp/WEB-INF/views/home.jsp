<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/home"/>
<c:set var="changeInstanceUrl" value="${baseUrl}/changeInstance"/>
<c:set var="findInstanceUrl" value="${baseUrl}/findInstance"/>

<app:message var="notYetImpl" code="admin.global.text.notYetImplemented"/>

<div class="search">

    <c:if test="${instance.isSearchView == true}">
        <form:form modelAttribute="instance" method="GET" action="${findInstanceUrl}" focus="filterValue" >
            <div class="filter">
                <table>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.home.label.findInstance" /><span class="colon">:</span></label></td>
                        <td class="cell value"><form:input id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                        <td  class="cell">&nbsp;&nbsp;&nbsp;<form:radiobutton cssClass="radio" path="filterType" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.ID%>"/><span class="label"><app:message code="admin.global.filter.label.ID"/></span>
                            <form:radiobutton path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.nameStartWith"/></span>
                            <form:radiobutton path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.nameContains"/></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="cell label first"><span class="showInactive"><form:checkbox cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /></span></td>
                        <td class="cell action"><form:button id="search"  onclick="$('form:first').submit()"><app:message code="admin.global.button.search"/></form:button>
                            <c:if test="${requestScope['appUser'].isSystemAdmin}">&nbsp;
                                &nbsp;
                                <form:button onclick="alert('${notYetImpl}');"><app:message code="admin.global.button.create"/></form:button>
                            </c:if>
                        </td>
                        <td></td>
                    </tr>

                </table>
            </div>

        </form:form>

        <c:if test="${instance.result != null}">
            <hr/>
            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(instance.result)}</div>
        </c:if>
    </c:if>

    <c:if test="${instance.result != null}">
        <table class="searchResult" width="100%">

            <c:choose>
                <c:when test="${requestScope['appUser'].isAdmin || requestScope['appUser'].isSystemAdmin}">
                    <col width="7%"/>
                    <col width="22%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="8%"/>
               </c:when>
                <c:otherwise>
                    <col width="7%"/>
                    <col width="22%"/>
                    <col width="15%"/>
                    <col width="15%"/>
                    <col width="10%"/>
                    <col width="10%"/>
                    <col width="8%"/>
                </c:otherwise>
            </c:choose>

            <thead class="header">
            <c:set var="sortUrl" value="${baseUrl}/instances/sortby"/>
            <tr class="row">

                <th class="cell cell-number"><a class="sort" href="${sortUrl}/primaryEntityId"><app:message code="admin.global.filter.label.primaryEntityId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/primaryEntityName"><app:message code="admin.global.filter.label.primaryEntityName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.address1" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/stateProvinceCd"><app:message code="admin.global.filter.label.state" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.status" /></a></th>
                 </tr>
            </thead>
            <tbody class="body">
            <c:forEach var="store" items="${instance.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${store.store.storeId}"/></td>
                    <td class="cell cell-text"><a href="${changeInstanceUrl}/${store.dataSourceIdent.dataSourceName}/${store.store.storeId}"><c:out value="${store.store.storeName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${store.store.addresses[0].address1}"/></td>
                    <td class="cell cell-text"><c:out value="${store.store.addresses[0].city}"/></td>
                    <td class="cell cell-text"><c:out value="${store.store.addresses[0].stateProvinceCd}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${store.store.status}" text="${store.store.status}"/></td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </c:if>
</div>


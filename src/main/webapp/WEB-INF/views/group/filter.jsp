<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/group/filter"/>
<app:url var="sortUrl" value="/group/filter/sortby"/>
<app:url var="editUrl" value="/group"/>
<app:url var="baseUrl"/>

<script type="text/javascript">
</script>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<div class="search">
  <div class="filter" >
<table>

<form:form modelAttribute="groupFilter" method="GET" action="${searchUrl}" focus="groupName">
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.groupId" /><span class="colon">:</span></label></td>
                        <td colspan="2" class="cell value"><form:input tabindex="1" id="groupId" path="groupId" focusable="true" cssClass="filterValue"/></td>
                        <td colspan="2" class="cell"></td>
                    </tr>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.groupName" /><span class="colon">:</span></label></td>
                        <td   colspan="2" class="cell value"><form:input tabindex="2" id="groupName" path="groupName" focusable="true" cssClass="filterValue"/></td>
                        <td class="cell" style="padding-top: 0;padding-bottom: 0">
                            <form:radiobutton tabindex="3" focusable="true" path="groupNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton tabindex="4" focusable="true" path="groupNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
					    <td class="cell label first"><label><app:message code="admin.global.filter.label.groupType" /><span class="colon">:</span></label></td>
					    <td colspan="2" class="cell value">
					     <form:select tabindex="5" path="groupType" focusable="true">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.groupTypes}">
                                <form:option value="${type.object2}"><app:message code="refcodes.GROUP_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
                            </c:forEach>
                        </form:select>
					    </td>
					    <td colspan="2" class="cell"></td>
					</tr>

                    <tr>
                        <td class="cell label first"></td>
                        <td colspan="4" class="cell"><label><br><form:checkbox tabindex="6" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /></label></td>
                    </tr>
                    <c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
                    <c:set var="administrator" value="<%=RefCodeNames.USER_TYPE_CD.ADMINISTRATOR%>" />
                    <c:set var="system_administrator" value="<%=RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR%>" />
                    <c:if test="${currentUserType==administrator || currentUserType==system_administrator}">
                    <tr>
                        <td class="cell label first"></td>
                        <td class="cell" colspan="4"><label><form:checkbox tabindex="7" cssClass="checkbox" path="showAll"/><app:message code="admin.global.filter.label.showAll" /><br><br></label></td>
                    </tr>
                    </c:if>
                    <tr>
                        <td class="cell label first"></td>
                        <td colspan="4" class="cell" align="left">
                            <form:button   id="search" cssClass="button" tabindex="10"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                            &nbsp;&nbsp;&nbsp;<form:button  focusable="true" tabindex="12" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        </td>

                    </tr>
                </table> <br><br>
            </form:form>
        </div>

    <c:if test="${groupFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(groupFilterResult.result)}</div>
    </c:if>
    <c:if test="${groupFilterResult.result != null}">

        <table class="searchResult" width="100%">

            <colgroup>
                <col width="10%"/>
                <col width="30%"/>
                <col width="10%"/>
                <col width="10%"/>
                <col width="40%"/>
            </colgroup>

            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/groupId"><app:message code="admin.global.filter.label.groupId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/groupName"><app:message code="admin.global.filter.label.groupName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/groupType"><app:message code="admin.global.filter.label.groupType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/groupStatus"><app:message code="admin.global.filter.label.status" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/assocStoreName"><app:message code="admin.global.filter.label.groupAssociation" /></a></th>
                <th></th>
            </tr>
            </thead>

            <tbody class="body">

            <c:forEach var="group" items="${groupFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${group.groupId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${group.groupId}"><c:out value="${group.groupName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${group.groupType}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.GROUP_STATUS_CD.${group.groupStatus}" text="${group.groupStatus}"/></td>
                    <td class="cell cell-text"><c:out value="${group.assocStoreName}"/></td>
                    <td></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


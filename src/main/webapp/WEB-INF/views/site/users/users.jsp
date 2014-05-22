<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/location/${siteHeader.siteId}/users" />
<app:url var="userEditUrl" value="/user"/>

<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#siteUsers').submit(); return false;"/>


<script type="text/javascript" language="JavaScript">

function clearAllRadios() {
    //alert("clearAllRadios");

    $('form:#siteUsers input[name="primaryUser"]').removeAttr("checked");
    $('#primaryUserId').attr('checked', true);
}
</script>

<div class="canvas">

    <div class="search">

        <form:form id="search" modelAttribute="siteUserFilter" method="GET" action="${searchUrl}" focus="userName">
            <div class="filter" >
                <table>
                    <tbody>
                    <tr>
                        <td colspan="4">
                            <div class="subHeader"><app:message code="admin.site.users.label.userAssociations"/></div>
                        </td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td colspan="4" class="cell cell-text boldLabel"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.site.users.label.userId" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="1" id="userId" path="userId" cssClass="filterValue" size="14"/>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">
                            <label><app:message code="admin.site.users.label.userName" /><span class="colon">:</span></label>
                        </td>
                        <td class="cell value">
                            <form:input tabindex="2" id="userName" path="userName" cssClass="filterValue"/>
                        </td>
                        <td class="cell" style="padding-top: 0;">
                            <form:radiobutton tabindex="3" path="userNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                            <br>
                            <form:radiobutton tabindex="3" path="userNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                    </tr>
                    <tr>
						<td class="cell cell-text">&nbsp;</td>
                        <td class="cell label">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="4" cssClass="checkbox"  path="showConfiguredOnly"/>
                            <label><app:message code="admin.site.users.label.associatedUsersOnly" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:checkbox tabindex="5" cssClass="checkbox"  path="showInactive"/>
                            <label><app:message code="admin.global.filter.label.showInactive" /></label>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td  class="cell action" colspan="2">&nbsp;</td>
                        <td  class="cell">
                            <form:button  id="search"  tabindex="6" onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        </td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </form:form>

        <form:form id="siteUsers" modelAttribute="siteUserFilterResult" method="POST" action="${updateUrl}">
            <c:if test="${siteUserFilterResult.result != null}">
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                            <label class="value"></label>${fn:length(siteUserFilterResult.result)}</div>
                        </td>
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <form:button onclick="clearAllRadios(); return false;"><app:message code="admin.global.button.clearPrimaryUser" /></form:button>
                        </td>
                    </tr>
                </table>
            </c:if>

            <c:if test="${siteUserFilterResult.result != null}">
                <table class="searchResult" width="100%">
                    <colgroup>
                        <col width="10%"/>
                        <col width="15%"/>
                        <col width="15%"/>
                        <col width="15%"/>
                        <col width="15%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                        <col width="10%"/>
                    </colgroup>
                    <thead class="header">
                    <tr class="row">
                        <th class="cell cell-number"><a class="sort" href="${sortUrl}/userId"><app:message code="admin.site.users.label.userId" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/userName"><app:message code="admin.site.users.label.userName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/lastName"><app:message code="admin.site.users.label.lastName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/firstName"><app:message code="admin.site.users.label.firstName" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.site.users.label.type" /></a></th>
                        <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.site.users.label.status" /></a></th>

                        <th class="cell cell-text">
                            <app:message code="admin.site.users.label.primaryUser" />
                            <form:radiobutton tabindex="2" id="primaryUserId" path="primaryUser" cssClass="radio" focusable="true" value="" style="visibility: hidden;" />
                        </th>
                        <th class="cell cell-text cell-element" nowrap>
                            <a href="javascript:checkAll('siteUsers', 'users.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                            <a href="javascript:checkAll('siteUsers', 'users.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                        </th>
                    </tr>
                    </thead>

                    <tbody class="body">
                    <c:forEach var="user" varStatus="i" items="${siteUserFilterResult.result}" >
                        <tr class="row">
                            <td class="cell cell-number"><c:out value="${user.value.userId}"/> </td>
                            <td class="cell cell-text"><a href="${userEditUrl}/${user.value.userId}"><c:out value="${user.value.userName}"/></a> </td>
                            <td class="cell cell-text"><c:out value="${user.value.lastName}"/> </td>
                            <td class="cell cell-text"><c:out value="${user.value.firstName}"/> </td>
                            <td class="cell cell-text"><c:out value="${user.value.type}"/> </td>
                            <td class="cell cell-text"><c:out value="${user.value.status}"/> </td>
                            <td class="cell cell-element"><form:radiobutton tabindex="2" id="primaryUserId" path="primaryUser" cssClass="radio" focusable="true" value="${user.value.userId}"/></td>
                            <td class="cell cell-element"><form:checkbox cssClass="checkbox" path="users.selectableObjects[${i.index}].selected"/></td>
                        </tr>
                    </c:forEach>
                    <tr><td colspan="9"><br><br></td></tr>
                    <tr>
                        <td colspan="9">&nbsp;</td>
                        <td align="right"><form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button></td>
                    </tr>
                    </tbody>

                </table>
            </c:if>
        </form:form>

</div>
</div>


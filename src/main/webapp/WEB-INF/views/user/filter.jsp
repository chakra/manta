<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.auth.AppUser" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames" %>

<app:url var="searchUrl" value="/user/filter"/>
<app:url var="sortUrl" value="/user/filter/sortby"/>
<app:url var="editUrl" value="/user"/>
<app:url var="reloadUrl" value="/user"/>
<app:url var="baseUrl"/>
<app:url var="historyUrl" value="/history/filter"/>

<c:set var="createAction" value="window.location.href='${baseUrl}/user/0/create'; return false;"/>
<c:set var="clearFilteredAccounts" value="$('form:first').attr('action','${baseUrl}/user/filter/clear/account');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('accountFilterInputNames');}"/>
<c:set var="importAction" value="window.location.href='${baseUrl}/user/loader'; return false;"/>

<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account'
                 action="${baseUrl}/locate/account/selected?filter=userFilter.setFilteredAccounts"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountFilterInputNames"
                 targetIds="accountFilterInputIds"
                 finallyHandler="${finallyHandler}"/>


<div class="search">
        <div class="filter" >
            <table class="locate">
                <tr>
                    <td  class="cell label first locate"><label>
                        <app:message code="admin.global.filter.label.filterByAccounts" /><span class="colon">:</span></label><br>
                        <button tabindex="1" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button><br>
                        <button tabindex="2" onclick="${clearFilteredAccounts}"><app:message code="admin.global.filter.label.clearAccounts"/></button>
                    </td>
                    <td class="cell locate">
                        <textarea id="accountFilterInputNames"
                                  focusable="true"
                                  tabindex="3"
                                  readonly="true"
                                  class="readonly">${userFilter.filteredAccountCommaNames}
                        </textarea>
                    </td>
                </tr>
                <tr><td  class="cell label first"></td><td class="cell value"></td></tr>
            </table>
            <form:form modelAttribute="userFilter" id="userFlterFormId" method="GET" action="${searchUrl}" focus="filterValue">
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.userId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="4" focusable="true" id="userId" path="userId" cssClass="filterValue" maxlength="38"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.userName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="filterValue" path="userLoginName" cssClass="filterValue" maxlength="255"/>
                    </td>
                    <td colspan="2" class="cell "  style="padding-top: 0;">
                        <form:radiobutton tabindex="6" path="userNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="6" path="userNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.userLastName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="7" id="lastName" path="lastName" cssClass="filterValue" maxlength="255"/>
                    </td>
                    <td class="cell value second" colspan="2">
                        <label><app:message code="admin.global.filter.label.userFirstName" /><span class="colon">:</span></label>
                        <form:input style="margin-left:10px;width:190px;" tabindex="8" focusable="true" path="firstName" maxlength="255"/>
                    </td>
                </tr>
                <tr>
                    <td  class="cell label first">
                        <div>
                            <label><app:message code="admin.global.filter.label.userUserType"/><span class="colon">:</span></label>
                        </div>
                    </td>
                    <td class="cell value">
                        <c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
                        <form:select tabindex="9" path="userType" focusable="true">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.getAllowedUserTypes(currentUserType)}">
                                <form:option value="${type.object2}"><app:message code="refcodes.USER_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.userEmail" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input  tabindex="10" id="email" path="email" focusable="true" maxlength="255"/>
                    </td>
                    <td colspan="2" class="cell"  style="padding-top: 0;padding-bottom: 0">
                        <form:radiobutton tabindex="11"  focusable="true" path="emailFilterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="11" focusable="true" path="emailFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"><div><label><app:message code="admin.global.filter.label.userRole"/><span class="colon">:</span></label></div></td>
                    <td class="cell value">
                        <form:select tabindex="12"  path="userRole" focusable="true">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${userFilter.userRoleChoices}">
                                <form:option value="${type.object1}">${type.object2}</form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first"><div><label><app:message code="admin.global.filter.label.userLanguage"/><span class="colon">:</span></label></div></td>
                    <td class="cell value" >
                        <form:select tabindex="13"  path="language" focusable="true">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="type" items="${userFilter.availableLanguages}">
                                <form:option value="${type.languageCode}">${type.uiName}</form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" colspan="3"><label><br><form:checkbox tabindex="14" cssClass="checkbox" focusable="true" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                </tr>
                <tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="3">
                        <form:button  id="search" cssClass="button" tabindex="15"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="16" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        <%
	                    AppUser appUser = Auth.getAppUser();
	                    if (appUser.isQneOfAdmin()) {
	                	%>
                        &nbsp;&nbsp;&nbsp;
                        <form:button  tabindex="16" onclick="${importAction}"><app:message code="admin.global.button.import"/></form:button >
                        <% } %>
                    </td>
                </tr>
            </table><br><br>
        </div>
        <form:hidden id="accountFilterInputIds" path="accountFilter" value="${userFilter.filteredAccountCommaIds}"/>
    </form:form>

    <c:if test="${userFilterResult.result != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(userFilterResult.result)}
        </div>
    </c:if>
    <c:if test="${userFilterResult.result != null}">

        <table class="searchResult" width="100%">
            <colgroup>
                <col width="8%"/>
                <col width="16%"/>
                <col width="16%"/>
                <col width="13%"/>
                <col width="12%"/>
                <col width="15%"/>
                <col width="10%"/>
                <col width="10%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/userId"><app:message code="admin.global.filter.label.userId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/userName"><app:message code="admin.global.filter.label.userName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/lastName"><app:message code="admin.global.filter.label.userLastName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/firstName"><app:message code="admin.global.filter.label.userFirstName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/role"><app:message code="admin.global.filter.label.userRole" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.global.filter.label.userType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.userStatus" /></a></th>
	            <th class="cell cell-text">
	            	<%
	                    AppUser appUser = Auth.getAppUser();
	                    boolean hasHistoryAccess = appUser.isAdmin() || appUser.isSystemAdmin() ||
		                Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY);
	                    if (hasHistoryAccess) {
	                %>
	                    	<app:message code="admin.user.label.showHistory" />
	                <%
	                    }
	                    else {
	                %>
	                    	&nbsp;
	                <%
	                    }
	                %>
	            </th>
            </tr>

            </thead>

            <tbody class="body">

            <c:forEach var="user" items="${userFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${user.userId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${user.userId}"><c:out value="${user.userName}"/></a></td>
                    <td class="cell cell-text"><c:out value="${user.lastName}"/></td>
                    <td class="cell cell-text"><c:out value="${user.firstName}"/></td>
                    <td class="cell cell-text"><c:out value="${user.role}"/></td>
                    <td class="cell cell-text"><c:out value="${user.type}"/></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${user.status}" text="${user.status}"/></td>
                    <td class="cell cell-text">
	                	<%
                        	if (hasHistoryAccess) {
	                    %>
								<a class="sort" href="${historyUrl}?objectId=${user.userId}&objectType=<%=RefCodeNames.HISTORY_OBJECT_TYPE_CD.USER %>">
									<app:message code="admin.user.label.showHistory"/>
								</a>
	                    <%
	                    	}
	                        else {
	                    %>
	                    		&nbsp;
	                    <%
	                    	}
	                    %>
                    </td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


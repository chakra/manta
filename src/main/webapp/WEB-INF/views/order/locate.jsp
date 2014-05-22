<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/locate/user/filter"/>
<app:url var="sortUrl" value="/locate/user/filter/sortby"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'locateUserFilterResultId', 'userLayer');"/>

<div class="search locate">

    <form:form modelAttribute="locateUserFilter" id="locateUserFilterId" method="POST" focus="filterValue" action="${searchUrl}" >

        <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                        <table>
                        <tr>
                            <td class="cell label first">
                                <label><app:message code="admin.global.filter.label.userId" /><span class="colon">:</span></label>
                            </td>
                            <td class="cell value ">
                                <form:input tabindex="1" id="userId" path="userId" cssClass="id"/>
                            </td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="cell label first"><label><app:message code="admin.global.filter.label.userName" /><span class="colon">:</span></label></td>
                            <td class="cell value" style="padding-top: 0; padding-botom: 0;"><form:input tabindex="2"  id="filterValue"   path="userName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell" style="padding-top: 0; padding-botom: 0;">
                                <form:radiobutton tabindex="3" path="userNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                <form:radiobutton tabindex="3" path="userNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                            </td>
                        </tr>
                        <tr>
                            <td  class="cell label first"><label><app:message code="admin.global.filter.label.userFirstName" /><span class="colon">:</span></td>
                            <td class="cell value" style="padding-top: 0;padding-bottom: 5px;"><form:input tabindex="4" path="firstName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                        <tr>
                            <td  class="cell label first"><label><app:message code="admin.global.filter.label.userLastName" /><span class="colon">:</span></td>
                            <td class="cell value" style="padding-top: 0;padding-bottom: 5px;"><form:input tabindex="5" path="lastName" cssClass="filterValue"/></td>
                            <td colspan="2" class="cell">&nbsp;</td>
                        </tr>
                                <tr>
                                    <td  class="cell label first"><label><app:message code="admin.global.filter.label.userUserType" /><span class="colon">:</span></td>
                                    <td class="cell value" colspan="3">  <form:select tabindex="6" path="userType" style="width:290px">
						            	 <form:option value=""><app:message code="admin.global.select"/></form:option>
						                 <app:i18nRefCodes var="code" items="${requestScope['appResource'].dbConstantsResource.userTypeCds}" i18nprefix="refcodes.USER_TYPE_CD.">
						                     <form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
						                 </app:i18nRefCodes>
                                    </form:select></td>
                                </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <span>
                                    <form:checkbox tabindex="7" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" />
                                </span>
                            </td>

                            <td colspan="2" class="cell">&nbsp;<br><br></td>
                        </tr>
                            <tr>
                                <td colspan="4"><br><br></td>
                            </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td class="cell label first">
                                <form:button  id="search" cssClass="button" tabindex="8"  onclick="$('form:first').submit(); return false;">
                                    <app:message code="admin.global.button.search"/>
                                </form:button>
                            </td>
                            <td colspan="2" class="cell">
                            </td>
                        </tr>   <tr>
                            <td colspan="4"><br><br></td>
                        </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>

    <c:if test="${locateUserFilterResult.result != null}">
        <hr style="margin-right: 15px;"/>
        <table class="searchResult" width="95%" >
            <tr>
                <td class="resultCount label">
                    <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value"></label>${fn:length(locateUserFilterResult.result)}
                </td>
                <td align="right" style="padding:0;vertical-align: top">
                   <c:if test="${locateUserFilterResult.multiSelected==true}">
                    <form:button style="margin-top:-5px" cssClass="button" tabindex="14" onclick="return ${doReturnSelected}">
                        <app:message code="admin.global.button.returnSelected"/>
                    </form:button>
				   </c:if>	
                </td>
            </tr>
        </table>
    </c:if>

    </form:form>

    <form:form id="locateUserFilterResultId" modelAttribute="locateUserFilterResult" method="POST">
        <c:if test="${locateUserFilterResult.result != null}">

            <table class="searchResult" width="95%">
                <colgroup>
                    <col width="8%"/>
                    <col width="21%"/>
                    <col width="11%"/>
                    <col width="10%"/>
                    <col width="18%"/>
                    <col width="12%"/>
                    <col width="10%"/>
                </colgroup>

                <thead class="header">

                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/userId"><app:message code="admin.global.filter.label.userId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/userName"><app:message code="admin.global.filter.label.userName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/firstName"><app:message code="admin.global.filter.label.userFirstName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/lastName"><app:message code="admin.global.filter.label.userLastName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/type"><app:message code="admin.global.filter.label.userUserType" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.userStatus" /></a></th>
                    <th class="cell cell-text cell-element">
	                    <c:if test="${locateUserFilterResult.multiSelected==true}">
	                        <a href="javascript:checkAll('locateUserFilterResultId', 'selectedUsers.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
	                        <a href="javascript:checkAll('locateUserFilterResultId', 'selectedUsers.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
           				</c:if>
	                    <c:if test="${locateUserFilterResult.multiSelected==false}">
<%-- 	                        <app:message code="admin.global.filter.label.select" /> --%>
           				</c:if>
                    </th>
                </tr>

                </thead>

                <tbody class="body">

                <c:forEach var="user" items="${locateUserFilterResult.result}" varStatus="i">
                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${user.value.userId}"/></td>
                        <td class="cell cell-text"><a href="javascript:void(0);" onclick="checkOneOfAll('locateUserFilterResultId', 'selectedUsers.selectableObjects','user_${user.value.userId}');return ${doReturnSelected}"><c:out value="${user.value.userName}"/></a></td>
                        <td class="cell cell-text"><c:out value="${user.value.firstName}"/></td>
                        <td class="cell cell-text"><c:out value="${user.value.lastName}"/></td>
                        <td class="cell cell-text"><c:out value="${user.value.type}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.USER_STATUS_CD.${user.value.status}" text="${user.value.status}"/></td>
                        <td class="cell cell-element">
	                        <c:if test="${locateUserFilterResult.multiSelected==true}">
		                        <form:checkbox cssClass="checkbox" id="user_${user.value.userId}" path="selectedUsers.selectableObjects[${i.index}].selected"/>
	           				</c:if>
	                        <c:if test="${locateUserFilterResult.multiSelected==false}">
		                        <form:checkbox cssClass="checkbox hide" id="user_${user.value.userId}" path="selectedUsers.selectableObjects[${i.index}].selected"/>
	           				</c:if>
           				</td>
                    </tr>
                </c:forEach>

                </tbody>

            </table>
        </c:if>

        <form:hidden path="multiSelected"/>

    </form:form>
</div>


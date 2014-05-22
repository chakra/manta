<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="storebbaseUrl"/>
<app:url var="baseUrl" value="/user/${userGroupFilter.userId}/group"/>
<app:url var="groupDetailBaseUrl" value="${storebbaseUrl}/group"/>
<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="sortInfUrl" value="${baseUrl}/groupInformation/sortby"/>

<div class="canvas">

<app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_GROUP%>">
    <c:set var="authForGroup" value="${true}"/>
</app:authorizedForFunction>

<div class="search">
    <form:form id="userGroupFilter" modelAttribute="userGroupFilter" method="GET" action="${searchUrl}" focus="groupName">
        <div class="filter" >
            <table>
                <tbody>
                <tr>
                    <td colspan="5">
                        <div class="subHeader"><app:message code="admin.user.configuration.group.groupAssociations"/></div>
                    </td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>
                <tr>
                    <td class="first boldLabel" style="padding-left: 15px"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                    <td>
                        <label><app:message code="admin.global.filter.label.groupId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <form:input tabindex="1" id="groupId" path="groupId" cssClass="filterValue"/>
                    </td>
                    <td>&nbsp;</td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td >
                        <label><app:message code="admin.global.filter.label.groupName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <form:input tabindex="3" id="groupName" path="groupName" cssClass="filterValue"/>
                    </td>
                    <td style="padding-top: 0; text-align: left">
                        <form:radiobutton tabindex="2" path="groupNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                        <form:radiobutton tabindex="2" path="groupNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="3" style="text-align:left; ">
                        <br>
                        <form:checkbox tabindex="3"  cssStyle="margin-left:0"  path="onlyAssociatedGroups"/>
                        <label><app:message code="admin.user.configuration.group.associatedOnly" /></label>
                    </td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>
                <tr>
                    <td  class="cell action" colspan="2">&nbsp;</td>
                    <td class="cell">
                        <form:button  id="search"  tabindex="5"><app:message code="admin.global.button.search"/></form:button>
                        <br>
                        <br>
                        <br>
                    </td>
                    <td colspan="3">&nbsp;</td>
                </tr>
                </tbody>
            </table>
        </div>
    </form:form>
    <c:if test="${userGroupFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value">${fn:length(userGroupFilterResult.result)}</label></div>
    </c:if>
</div>


<c:if test="${userGroupFilterResult.result != null}">

<%--
    <app:refcdMap  var="woFuncMap" code="APPLICATION_WO_FUNCTIONS" invert="true"/>
    <app:refcdMap  var="stJohnFuncMap" code="APPLICATION_FUNCTIONS" invert="true" />

--%>

    <form:form id="userGroupFilterResult" modelAttribute="userGroupFilterResult" method="POST" action="${updateUrl}">

    <c:set var="grouplength" value="${fn:length(userGroupFilterResult.result)}"/>
	<c:set var="columnCount" value="${(grouplength - (grouplength % 5)) / 5}" />
	<c:set var="columnCount" value="${(columnCount == 0) ? 1 : columnCount}" />
	<c:set var="columnCount" value="${columnCount < 5 ? columnCount : 4 }" />
    <c:set var="colElementsCount" value="${(grouplength - (grouplength % columnCount)) / columnCount}" />


    <div class="details">

        <table  width="100%">

            <thead>
            <tr><td style="text-align: left"><div class="subHeader"><app:message code="admin.user.configuration.group.subheader.memberOfGroup"/></div></td>

                <td align="right">
                <c:if test="${grouplength > 0 }"><form:button><app:message code="admin.global.button.save" /></form:button></c:if>

            </tr>
            </thead>


            <tbody>

            <tr>
                <td align="left" colspan="2" style="padding-left: 20px">
                    <table>
                        <tr>
							<c:forEach var="i" begin="0" end="${columnCount-1}">
                            <td style="vertical-align: top">

									<c:set var="startInd" value="${i*colElementsCount}"/>
									<c:set var="endInd" value="${i<(columnCount-1) ? (startInd + colElementsCount - 1) : (grouplength+1)}"/>

									<c:forEach var="value" varStatus="valueStatus" items="${userGroupFilterResult.result}" begin="${startInd}" end="${endInd}">

									<label class="label">
										<c:set var="disabled" value="${value.value.object2 == userGroupFilterResult.userType || requestScope['appResource'].dbConstantsResource.defaultUserGroup == value.value.object2}"/>
											<c:if test="${disabled == true}"> <form:hidden path="groups.selectableObjects[${valueStatus.index}].selected" /></c:if>
											<form:checkbox disabled="${disabled }" path="groups.selectableObjects[${valueStatus.index}].selected"/>
											&nbsp;&nbsp;
										<c:out value="${value.value.object2}"/>
										</label>
										<br>
									</c:forEach>
                            </td>
								</c:forEach>

                        </tr>
                    </table>


                </td>
               </tr>

            </tbody>

            <tfoot>
            <tr><td style="text-align: left"></td>
                <td align="right">
                    <c:if test="${grouplength >0 }"><form:button><app:message code="admin.global.button.save" /></form:button></c:if>
                </td>
            </tr>
            </tfoot>


        </table>
        <hr/>


        <table width="100%">

            <thead>
            <tr><td style="text-align: left"><div class="subHeader"><app:message code="admin.user.configuration.group.subheader.groupInformation"/></div></td></tr>
            </thead>

            <tbody class="search">
             <tr><td>&nbsp;</td></tr>
            <tr>

                <td align="left">

                    <table class="searchResult" width="100%">
                        <colgroup>
                            <col width="8%"/>
                            <col width="21%"/>
                           <%-- <col width="15%"/>--%>
                            <col width="25%"/>
                            <col width="21%"/>
                            <col width=""/>
                        </colgroup>
                        <thead class="header">
                        <tr class="row">
                            <th class="cell cell-number"><a href="${sortInfUrl}/groupId"><app:message code="admin.user.configuration.group.label.groupId" /></a></th>
                            <th class="cell cell-text"><a href="${sortInfUrl}/groupDescription"><app:message code="admin.user.configuration.group.label.groupName" /></a></th>
                        <%--    <th class="cell cell-text"><a class="sort" href="${sortInfUrl}/system"><app:message code="admin.user.configuration.group.label.system" /></a></th>
                         --%>   <th class="cell cell-text"><a href="${sortInfUrl}/groupPermission"><app:message code="admin.user.configuration.group.label.group.permission" /></a></th>
                            <th class="cell cell-text"><a href="${sortInfUrl}/reportName"><app:message code="admin.user.configuration.group.label.group.reportName" /></a></th>
                            <th></th>
                        </tr>

                        </thead>


                        <c:forEach var="groupsInformation" varStatus="i" items="${userGroupFilterResult.groupsInformation.result}" >

                        <tr class="row">

                            <td class="cell cell-number"><c:out value="${groupsInformation.groupId}"/></td>
                            <td class="cell cell-text">
                            	<c:out value="${groupsInformation.groupDescription}"/>
                            </td>
                                <%--    <td class="cell cell-text">
                                        <c:if test="${not empty stJohnFuncMap[permission]}"><app:message code="admin.user.configuration.group.label.systemCode.STJOHN" /></c:if>&nbsp;&nbsp;
                                        <c:if test="${not empty woFuncMap[permission]}"><app:message code="admin.user.configuration.group.label.systemCode.ORCA" /></c:if>
                                    </td>--%>
                            <td class="cell cell-text"><c:out value="${groupsInformation.groupPermission}"/></td>
                            <td class="cell cell-text"><c:out value="${groupsInformation.reportName}"/></td>
                            <td class="cell cell-text"></td>
                        </tr>

            </tbody>

            </c:forEach>



                    </table>



                </td>

            </tr>

            </tbody>

        </table>
    </div>

</form:form>
</c:if>


</div>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<app:url var="baseUrl" value="/account/${accountSiteHierarchyManage.accountId}/locationHierarchy/${accountSiteHierarchyManage.levelIdUrlPath}${not empty accountSiteHierarchyManage.levels?'/':''}${accountSiteHierarchyManage.levelId}"/>
<app:url var="locateUrl" value="/account/${accountSiteHierarchyManage.accountId}/locationHierarchy/configuration/${accountSiteHierarchyManage.levelIdUrlPath}"/>

<c:set var="addAction" value="$('#accountSiteHierarchyManage #action').attr('value','addline'); $('#accountSiteHierarchyManage').submit();return false;"/>
<c:set var="saveAction" value="$('#accountSiteHierarchyManage #action').attr('value','save'); $('#accountSiteHierarchyManage').submit();return false;"/>

<app:url var="layerlink" value="/account/${accountSiteHierarchyManage.accountId}/locationHierarchy/"/>
<c:set var="actionUtl" value=""/>
<c:forEach var="levellayer" varStatus="i" items="${accountSiteHierarchyManage.layerLevels}">
    <c:if test="${i.first}"><c:set var="actionUtl" value="${levellayer.levelId}"/></c:if>
    <c:if test="${!i.first}"><c:set var="actionUtl" value="${actionUtl}/${levellayer.levelId}"/></c:if>
</c:forEach>
<c:set var="actionUtl" value="${actionUtl}${not empty actionUtl?'/manage/':''}${accountSiteHierarchyManage.levelId}"/>

<br><div style="font-size: 18px;font-weight: bold;" ><app:message code="admin.account.siteHierarchy.subheader.siteHierarchyDetail"/>&nbsp;<c:out value="${accountSiteHierarchyManage.levelNum + 1}"/></div>  <br>
<hr>
<div class="search details">

    <br>


    <form:form modelAttribute="accountSiteHierarchyManageFilter" method="GET"  action="${layerlink}${actionUtl}/filter" focus="filterValue">
        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.account.siteHierarchy.label.levelId" /><span class="colon">:</span></label></td>
                    <td class="cell value">
                        <form:input tabindex="1" id="filterId" path="filterId" cssClass="filterValue"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first"><label><app:message code="admin.account.siteHierarchy.label.levelName" /><span class="colon">:</span></label></td>
                    <td class="cell value"><form:input tabindex="2" id="filterValue" path="filterValue" cssClass="filterValue"/></td>
                    <td colspan="2" class="cell" style="padding-top: 0;">
                        <form:radiobutton tabindex="3" path="filterType" cssClass="radio"  value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span>
                        <br><form:radiobutton tabindex="3" path="filterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
					<td class="cell label first"></td>
                    <td class="cell" ><form:button cssClass="button"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button><br> <br>
                        &nbsp;</td>
                    <td class="cell label first"></td>
                </tr>
            </table>
        </div>
    </form:form>

    <hr/>



    <form:form id="accountSiteHierarchyManage" modelAttribute="accountSiteHierarchyManage" action="${layerlink}${actionUtl}" method="POST">

        <form:hidden id="action" path="action"/>
        <form:hidden  path="accountId"/>
        <form:hidden  path="levelId"/>
        <form:hidden  path="configure"/>
        <form:hidden  path="levelName"/>
        <form:hidden  path="levelNum"/>
        <form:hidden  path="layerName"/>
        <form:hidden  path="layerId"/>
        <form:hidden  path="parentLevelId"/>
        <c:forEach varStatus="i" items="${accountSiteHierarchyManage.layerLevels}">
            <form:hidden path="layerLevels[${i.index}].layerId"/>
            <form:hidden path="layerLevels[${i.index}].levelId"/>
            <form:hidden path="layerLevels[${i.index}].layerName"/>
            <form:hidden path="layerLevels[${i.index}].levelName"/>
        </c:forEach>
        <c:forEach varStatus="i" items="${accountSiteHierarchyManage.levels}">
            <form:hidden path="levels[${i.index}]"/>
        </c:forEach>



        <div>

            <c:forEach var="levellayer" varStatus="i" items="${accountSiteHierarchyManage.layerLevels}">
                <c:if test="${i.first}"><c:set var="layerpath" value="${levellayer.levelId}"/></c:if>
                <c:if test="${levellayer.layerId != accountSiteHierarchyManage.layerId}">
                    <a href="${layerlink}${layerpath}${!i.first?"/manage/":""}${!i.first?levellayer.levelId:''}" style="font-size:18px;}"><c:out value="${levellayer.layerName}"/></a><span style="font-size:18px;color: #000000;">&nbsp;>></span>
                </c:if>
                <c:if test="${!i.first}"><c:set var="layerpath" value="${layerpath}/${levellayer.levelId}"/></c:if>
            </c:forEach>
            <span style="font-size:18px;color: black;"><c:out value="${accountSiteHierarchyManage.layerName}"/></span>

            <br>   <br><br>
            <div class="canvas">
                <div class="row clearfix">
                    <div class="cell">&nbsp</div>
                    <div class="cell label subHeader" style="font-size: 14px"><app:message code="admin.account.siteHierarchy.label.siteHierarchyId"/><span class="colon">:</span></div>
                    <div class="cell  subHeader"><div class="labelValue" style="font-weight: normal;"><c:out value="${accountSiteHierarchyManage.layerId}" default="0"/></div></div>
                    <div class="cell cell-space" style="width: 200px">&nbsp;</div>
                    <div class="cell label subHeader" style="font-size: 14px"><app:message code="admin.account.siteHierarchy.label.siteHierarchyName"/><span class="colon">:</span></div>
                    <div class="cell label subHeader"><div class="labelValue" style="font-weight: normal;"><c:out value="${accountSiteHierarchyManage.layerName}"/></div></div>
                </div>
            </div>
            <br><app:box cssClass="white" >

            <table width="80%" class="searchResult">
                <colgroup>
                    <col width="10%"/>
                    <col width="21%"/>
                    <col/>
                </colgroup>
                <thead class="header">
                <tr class="row">
                    <th class="cell cell-number"><app:message code="admin.account.siteHierarchy.label.levelId"/></th>
                    <th class="cell cell-element"><div class="label"><app:message code="admin.account.siteHierarchy.label.levelName"/></div></th>
                    <th></th>
                </tr>

                </thead>
                <tbody class="body" style="padding-top: 35px;">

                <c:forEach var="level" varStatus="i" items="${accountSiteHierarchyManage.items}">
                    <tr class="row">
                        <form:hidden path="items[${i.index}].busEntityId"/>
                        <form:hidden path="items[${i.index}].longDesc"/>
                        <form:hidden path="items[${i.index}].level"/>
                        <form:hidden path="items[${i.index}].number"/>
                        <td class="cell cell-number"><c:out value="${level.busEntityId}"/></td>
                        <td class="cell cell-element">
                            <div class=" value "><form:input cssStyle="width:255px" path="items[${i.index}].name"/></div>
                        </td>
                        <td valign="middle">
                            <div style="padding-left: 50px">

                                <c:set var="manageAction" value="window.location.href='${baseUrl}/manage/${level.busEntityId}';"/>
                                <c:set var="configuration" value="window.location.href='${baseUrl}/configure/${level.busEntityId}';"/>
                                <c:if test="${!accountSiteHierarchyManage.configure}">
                                    <form:button  disabled="${!(level.busEntityId > 0)}" onclick="${manageAction} return false" ><app:message code="admin.global.button.manage"/></form:button>
                                </c:if>
                                <c:if test="${(level.busEntityId > 0) && accountSiteHierarchyManage.configure}">
                                    <form:button  onclick="${configuration} return false"><app:message code="admin.global.button.configureSites"/></form:button>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>

                <tr>
                    <td></td>
                    <td  align="center"><br><br><br>

                        <form:button onclick="${saveAction}" disabled="${empty accountSiteHierarchyManage.items}"><app:message code="admin.global.button.save"/></form:button>&nbsp;&nbsp;&nbsp;
                        <form:button  onclick="${addAction}"><app:message code="admin.global.button.add"/></form:button>
                    </td>
                    <td></td>
                </tr>

            </table>
        </app:box>
            <br><br>
        </div>
    </form:form>

</div>


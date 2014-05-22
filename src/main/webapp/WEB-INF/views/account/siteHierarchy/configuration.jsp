<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>




<app:url var="baseUrl"/>
<app:url var="searchUrl" value="/account/${accountSiteHierarchyConfiguration.accountId}/locationHierarchy/configuration/filter"/>


<app:url var="layerlink" value="/account/${accountSiteHierarchyConfiguration.accountId}/locationHierarchy/"/>
<c:set var="actionUtl" value=""/>
<c:forEach var="levellayer" varStatus="i" items="${accountSiteHierarchyConfiguration.layerLevels}">
    <c:if test="${i.first}"><c:set var="actionUtl" value="${levellayer.layerId}"/></c:if>
    <c:if test="${!i.last}"><c:set var="actionUtl" value="${actionUtl}/${levellayer.levelId}"/></c:if>
</c:forEach>
<c:set var="actionUtl" value="${actionUtl}${not empty actionUtl?'/configure/':''}${accountSiteHierarchyConfiguration.levelId}"/>
<c:set var="sortUrl" value="${layerlink}${actionUtl}/filter/sortby"/>

<c:set var="updateAction" value="$('form:first').attr('action','${layerlink}${actionUtl}/save');$('form:first').attr('method','POST');$('form:first').submit(); return false;" />

<br><div style="font-size: 18px;font-weight: bold;" ><app:message code="admin.account.siteHierarchy.subheader.configureSites"/></div>  <br>
<hr>
<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'accountSiteHierarchyConfigurationId', 'locationLayer');"/>

<div  class="details">
<div class="search locate">
    <form:form modelAttribute="accountSiteHierarchyConfiguration" id="accountSiteHierarchyConfigurationId" method="POST"  action="${layerlink}${actionUtl}/filter" focus="siteName">


        <form:hidden  path="accountId"/>
        <form:hidden  path="levelId"/>
        <form:hidden  path="levelName"/>
        <form:hidden  path="levelNum"/>
        <form:hidden  path="layerName"/>
        <form:hidden  path="layerId"/>
        <c:forEach varStatus="i" items="${accountSiteHierarchyConfiguration.layerLevels}">
            <form:hidden path="layerLevels[${i.index}].layerId"/>
            <form:hidden path="layerLevels[${i.index}].levelId"/>
            <form:hidden path="layerLevels[${i.index}].layerName"/>
            <form:hidden path="layerLevels[${i.index}].levelName"/>
        </c:forEach>
        <c:forEach varStatus="i" items="${accountSiteHierarchyConfiguration.levels}">
            <form:hidden path="levels[${i.index}]"/>
        </c:forEach>


        <div  class="details">
            &nbsp;&nbsp;&nbsp; &nbsp;&nbsp; <div class="canvas" style="font-size: 16px">
            <div class="row clearfix">
                <div class="cell">&nbsp;</div>
                <div class="cell label subHeader" style="font-size: 16px"><app:message code="admin.account.siteHierarchy.label.siteHierarchyId"/><span class="colon">:</span></div>
                <div class="cell  subHeader"><div class="labelValue" style="font-weight: normal;"><c:out value="${accountSiteHierarchyConfiguration.levelId}" default="0"/></div></div>
                <div class="cell cell-space" style="width: 200px">&nbsp;</div>
                <div class="cell label subHeader" style="font-size: 16px"><app:message code="admin.account.siteHierarchy.label.siteHierarchyName"/><span class="colon">:</span></div>
                <div class="cell  subHeader"><div class="labelValue" style="font-weight: normal;"><c:out value="${accountSiteHierarchyConfiguration.levelName}"/></div></div>
            </div>
        </div>
        <br>
            <app:url var="layerlink" value="/account/${accountSiteHierarchyConfiguration.accountId}/locationHierarchy"/>
            <c:forEach var="levellayer" varStatus="i" items="${accountSiteHierarchyConfiguration.layerLevels}">
                <c:if test="${i.first}"><c:set var="layerpath" value="${levellayer.layerId}"/></c:if>
                <a href="${layerlink}/${layerpath}${!i.first?'/manage/':''}${!i.first?accountSiteHierarchyConfiguration.layerLevels[i.index-1].levelId:''}" style="font-size:18px;}">
                    <c:out value="${levellayer.layerName}"/>(<c:out value="${levellayer.levelName}"/>)
                </a>
                <c:if test="${!i.last}"><span style="font-size:18px;color: #000000;">&nbsp;>></span></c:if>
                <c:if test="${!i.first}"><c:set var="layerpath" value="${layerpath}/${accountSiteHierarchyConfiguration.layerLevels[i.index-1].levelId}"/></c:if>
            </c:forEach>
        <br>

            <br><hr>
         <table>
            <tr>
                <td  class="locateFilterHeader"><app:message code="admin.global.filter.text.searchCriteria" /></td>
                <td>
                    <div class="filter" >
                            <table>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.siteId" /><span class="colon">:</span></label></td>
                                    <td class="cell value"><form:input tabindex="4" id="siteId" path="siteId" focusable="true" cssClass="filterValue"/></td>
                                    <td colspan="2" class="cell"></td>
                                </tr>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.siteName" /><span class="colon">:</span></label></td>
                                    <td class="cell value"><form:input tabindex="5" id="siteName" path="siteName" focusable="true" cssClass="filterValue"/></td>
                                    <td class="cell" style="padding-top: 0;padding-bottom: 0">
                                        <form:radiobutton tabindex="6" focusable="true" path="siteNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                        <form:radiobutton tabindex="6" focusable="true" path="siteNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                                    </td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.siteRefNumber" /><span class="colon">:</span></td>
                                    <td class="cell value"><form:input tabindex="7" path="refNumber" focusable="true" cssClass="filterValue"/></td>
                                    <td class="cell" style="padding-top: 0;">
                                        <form:radiobutton focusable="true" tabindex="8" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                                        <form:radiobutton focusable="true" tabindex="8" path="refNumberFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                                    </td>
                                    <td class="cell" colspan="2"> </td>
                                </tr>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.city" /><span class="colon">:</span></td>
                                    <td class="cell value"><form:input tabindex="9" path="city" focusable="true" cssClass="filterValue" /></td>
                                    <td class="cell" colspan="2"> </td>
                                </tr>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.state" /><span class="colon">:</span></td>
                                    <td class="cell value"><form:input tabindex="10" path="state" focusable="true" /></td>
                                    <td colspan="2" class="cell"> </td>
                                </tr>
                                <tr>
                                    <td class="cell label first"><label><app:message code="admin.global.filter.label.postalCode" /><span class="colon">:</span></td>
                                    <td class="cell value"><form:input tabindex="11" path="postalCode" focusable="true" cssClass="filterValue"/></td>
                                    <td colspan="2" class="cell"> </td>
                                </tr>
                                <tr>
                                    <td class="cell label first"></td>
                                    <td class="cell"><label><br><form:checkbox tabindex="12" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /></label></td>
                                    <td colspan="2"></td>
                                </tr>
                                <tr>
                                    <td class="cell label first"></td>
                                    <td class="cell"><label><form:checkbox tabindex="13" cssClass="checkbox" path="showConfiguredOnly"/><app:message code="admin.global.filter.label.showConfiguredOnly" /><br><br></label></td>
                                    <td colspan="2"></td>
                                </tr>
                                <tr>
                                    <td class="cell label first"></td>
                                    <td class="cell">
                                        <form:button  id="search" cssClass="button" tabindex="14"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                                    </td>
                                    <td colspan="2"></td>
                                </tr>
                            </table> <br><br>
                    </div>
                </td>
            </tr>
        </table>
         </div>


        <c:if test="${accountSiteHierarchyConfiguration.result != null}">
            <br>
                <hr/>
                <table width="100%">
                    <tr>
                        <td align="left">
                            <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(accountSiteHierarchyConfiguration.result)}</div>
                        </td>
                        <td align="right">
                            <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                        </td>
                    </tr>
                </table>

        </c:if>
        <c:if test="${accountSiteHierarchyConfiguration.result != null}">

            <table class="searchResult" width="100%">

                <colgroup>
                    <col width="8%"/>
                    <col width="15%"/>
                    <col width="17%"/>
                    <col width="17%"/>
                    <col width="10%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col width="8%"/>
                    <col/>
                </colgroup>

                <thead class="header">
                <tr class="row">
                    <th class="cell cell-number"><a class="sort" href="${sortUrl}/siteId"><app:message code="admin.global.filter.label.siteId" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/siteName"><app:message code="admin.global.filter.label.siteName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/accountName"><app:message code="admin.global.filter.label.accountName" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/address1"><app:message code="admin.global.filter.label.streetAddress" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.global.filter.label.city" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/state"><app:message code="admin.global.filter.label.state" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.global.filter.label.postalCode" /></a></th>
                    <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.siteStatus" /></a></th>
                    <th class="cell cell-element">
                        <a href="javascript:checkAll('accountSiteHierarchyConfigurationId', 'siteObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                        <a href="javascript:checkAll('accountSiteHierarchyConfigurationId', 'siteObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                    </th>
                </tr>
                </thead>

                <tbody class="body">

                <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_SITE%>"><c:set var="authForLocations" value="${true}"/></app:authorizedForFunction>

                <c:forEach varStatus="j" var="site" items="${accountSiteHierarchyConfiguration.result}" >

                    <form:hidden path="siteObjects[${j.index}].value.siteId"/>
                    <form:hidden path="siteObjects[${j.index}].value.siteName"/>
                    <form:hidden path="siteObjects[${j.index}].value.accountId"/>
                    <form:hidden path="siteObjects[${j.index}].value.accountName"/>
                    <form:hidden path="siteObjects[${j.index}].value.status" />
                    <form:hidden path="siteObjects[${j.index}].value.address1"/>
                    <form:hidden path="siteObjects[${j.index}].value.address2"/>
                    <form:hidden path="siteObjects[${j.index}].value.address3"/>
                    <form:hidden path="siteObjects[${j.index}].value.address3"/>
                    <form:hidden path="siteObjects[${j.index}].value.address4"/>
                    <form:hidden path="siteObjects[${j.index}].value.countryCd"/>
                    <form:hidden path="siteObjects[${j.index}].value.countyCd" />
                    <form:hidden path="siteObjects[${j.index}].value.city" />
                    <form:hidden path="siteObjects[${j.index}].value.state"/>
                    <form:hidden path="siteObjects[${j.index}].value.postalCode"/>

                    <tr class="row">
                        <td class="cell cell-number"><c:out value="${site.value.siteId}"/></td>
                        <td class="cell cell-text">
                            <c:if test="${not empty authForLocations && authForLocations == true}">
                                <a href="${baseUrl}/location/${site.value.siteId}"> <c:out value="${site.value.siteName}"/></a>
                            </c:if>
                            <c:if test="${empty authForLocations || authForLocations == false}">
                                <c:out value="${site.value.siteName}"/>
                            </c:if>
                        </td>
                        <td class="cell cell-text"><c:out value="${site.value.accountName}"/></td>
                        <td class="cell cell-text"><c:out value="${site.value.address1}"/></td>
                        <td class="cell cell-text"><c:out value="${site.value.city}"/></td>
                        <td class="cell cell-text"><c:out value="${site.value.state}"/></td>
                        <td class="cell cell-text"><c:out value="${site.value.postalCode}"/></td>
                        <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${site.value.status}" text="${site.value.status}"/></td>
                        <td class="cell cell-element"><form:checkbox cssClass="checkbox" id="location_${site.value.siteId}" path="siteObjects[${j.index}].selected"/></td>
                    </tr>
                </c:forEach>

                </tbody>
                <tr><td colspan="9"><br><br></td></tr>
                <tr>
                    <td colspan="8">&nbsp;</td>
                    <td align="right"><form:button   onClick="${updateAction}"><app:message code="admin.global.button.save"/></form:button></td>
                </tr>
            </table>
        </c:if>

    </form:form>
</div>  </div>


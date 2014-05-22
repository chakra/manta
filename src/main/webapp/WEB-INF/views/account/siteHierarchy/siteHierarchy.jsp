<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql_rt" uri="http://java.sun.com/jstl/sql_rt" %>

<app:url var="baseUrl" value="/account/${accountSiteHierarchy.accountId}/locationHierarchy"/>
<c:set var="reportAction" value="$('form:first').attr('action','${baseUrl}/report');"/>
<c:set var="saveAction" value="$('form:first').attr('action','${baseUrl}');"/>

<br><div style="font-size: 18px;font-weight: bold;" ><app:message code="admin.account.siteHierarchy.subheader.siteHierarchyDetail"/> </div>  <br>
<hr>
<form:form modelAttribute="accountSiteHierarchy" method="POST" action="${baseUrl}">

<div class="canvas">
<br>
<br>

<div class="search">
    <div style="padding-left: 20px" class="details std filter">

        <table class="searchResult" width="80%">
            <colgroup>
                <col width="10%"/>
                <col width="21%"/>
                <col width="21%"/>
                <col/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><div class="label"><app:message code="admin.account.siteHierarchy.label.siteHierarchyId"/></div></th>
                <th class="cell cell-text"><div class="label"><app:message code="admin.account.siteHierarchy.label.siteHierarchyLevel"/></div></th>
                <th class="cell cell-element"><div class="label"><app:message code="admin.account.siteHierarchy.label.siteHierarchyName"/></div></th>
                <th class="cell cell-text"></th>
            </tr>

            </thead>
            <tbody class="searchResult" style="padding-top: 35px;">
            <c:forEach varStatus="i" items="${accountSiteHierarchy.levels}">
                <form:hidden path="levels[${i.index}].object1"/>
                <form:hidden path="levels[${i.index}].object2"/>
            </c:forEach>

            <c:forEach  varStatus="i" begin="0" end="${requestScope['appResource'].dbConstantsResource.maxSizeHierarchyLevels - 1}">

                <c:set var="level" value="${accountSiteHierarchy.levelCollection[i.index]}"/>
                <c:set var="busEntityId" value="${level.busEntityId}"/>
                <c:choose>
                    <c:when test="${busEntityId > 0}">
                        <c:set var="longDesc" value="${level.longDesc}"/>
                        <c:set var="levelNumber" value="${level.level}"/>
                        <c:set var="number" value="${level.number}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set  var="longDesc" value="${i.index+1}"/>
                        <c:set var="levelNumber" value="0"/>
                        <c:set var="number" value="${i.index+1}"/>
                    </c:otherwise>
                </c:choose>

                <form:hidden path="levelCollection[${i.index}].busEntityId"/>
                <form:hidden path="levelCollection[${i.index}].longDesc" value="${longDesc}"/>
                <form:hidden path="levelCollection[${i.index}].level" value="${levelNumber}"/>
                <form:hidden path="levelCollection[${i.index}].number" value="${number}"/>


                <tr class="row">

                            <td class="cell cell-number"><c:out value="${level.busEntityId}"/></td>
                            <td class="cell cell-text"><app:message code="admin.account.siteHierarchy.label.siteHierarchy"/>&nbsp;<c:out value="${i.index+1}"/></td>
                            <td class="cell cell-element">
                                <div class=" value "><form:input cssStyle="width:255px" path="levelCollection[${i.index}].name"/></div>
                            </td>
                            <td class="cell cell-text" valign="middle"><c:if test="${i.index == 0}">
                                <div style="padding-left: 50px">
                                    <c:set var="manageAction" value="window.location.href='${baseUrl}/${level.busEntityId}';"/>
                                    <form:button  onclick="${manageAction} return false"  disabled="${!accountSiteHierarchy.canBeManaged}"><app:message code="admin.global.button.manage"/></form:button>
                                </div>
                            </c:if></td>

                </tr>
            </c:forEach>

            </tbody>

            <tr>
                <td colspan="2" align="center"></td>
                <td align="center"><br>
                    <form:button onclick="${saveAction}"><app:message code="admin.global.button.save"/></form:button></td>
                <td></td>
            </tr>

        </table>

    </div>
</div>

<hr>
<script type="text/javascript" language="JavaScript">


    function show_level(id) {
        if(eval("document.getElementById(id)").style.display=='none'){
            eval("document.getElementById(id)").style.display='block';
            eval("document.getElementById('viewdetail'+id)").innerHTML="[-]";
        } else {
            eval("document.getElementById(id)").style.display='none';
            eval("document.getElementById('viewdetail'+id)").innerHTML="[+]";
        }
    }

    function expand_level(id)  {
        eval("document.getElementById(id)").style.display='block';
        eval("document.getElementById('viewdetail'+id)").innerHTML="[-]";
    }

    function collapse_level(id) {
        eval("document.getElementById(id)").style.display='none';
        eval("document.getElementById('viewdetail'+id)").innerHTML="[+]";
    }

    function show_levels(action) {
        var elems = document.getElementsByTagName('table');
        var k = 0;
        for(var i=0; i<elems.length ; i++) {
            var idStr = elems[i].id;
            if (idStr != 'undefined' && idStr.match('wId')){
                k++;
                if (action.match('Expand') && (k <= 100)) {  expand_level(idStr);}
                if (action.match('Collapse')) {collapse_level(idStr);}
            }
        }
    }


</script>
<div class="details search">

    <br>
    <c:if test="${not empty accountSiteHierarchy.hierarchyReport}">

        <table width="100%"><tr><td align="right"><span class="label"><app:message code="admin.account.siteHierarchy.label.fullSiteHierarchy"/></span> &nbsp;&nbsp; <form:button onclick="${reportAction}"><app:message code="admin.global.button.downloadReport"/></form:button></td></tr></table>

        <app:box>

            <table width="100%" style="padding: 0" border="0"><tr> <td align="right"  style="padding: 0"><span style="float:right; margin-bottom:3px;"><a href="javascript:show_levels('Expand');" onClick=""><app:message code="admin.account.siteHierarchy.label.expandAll" /></a>  |  <a href="javascript:show_levels('Collapse');" onClick=""><app:message code="admin.account.siteHierarchy.label.collapseAll" /></a></span></td></tr></table>

            <table>

                <c:set var="colHref0" value=""/>

                <c:forEach var="reportItem" items="${accountSiteHierarchy.hierarchyReport}">

                    <c:set var="itemNameLevel1" value="${0}"/>

                    <c:if test="${not empty reportItem.value}">
                        <c:set var="itemNameLevel1" value="${reportItem.value[0].siteHierarchyLevel1Id}"/>
                    </c:if>

                    <c:set var="eleid" value="${itemNameLevel1}"/>
                    <c:set var="pathBase" value="${eleid>0?baseUrl:''}${eleid>0?'/':''}${eleid>0?accountSiteHierarchy.levels[0].object1:''}"/>

                    <c:choose>
                        <c:when test="${not empty accountSiteHierarchy.levels && fn:length(accountSiteHierarchy.levels) == 1}">
                            <c:set var="colHref0" value="${pathBase}${eleid>0?'/configure/':''}${eleid>0?reportItem.value[0].siteHierarchyLevel1Id:''}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="colHref0" value="${pathBase}${eleid>0?'/manage/':''}${eleid>0?reportItem.value[0].siteHierarchyLevel1Id:''}"/>
                        </c:otherwise>
                    </c:choose>


                    <tr>
                        <td  width="30px"><strong  style="font-size: 12px;cursor:hand;cursor:pointer" id="viewdetailwId${eleid}"  onClick="show_level('wId${eleid}');">[+]</strong></td>
                        <td colspan="5" style="font-size: 12px;" align="left">
                            <strong><span style="color:black"><c:out value="${accountSiteHierarchy.levels[0].object2}"/></span>&nbsp;
                                <a href="${colHref0}">${reportItem.key}</a>
                            </strong>
                        </td>
                    </tr>


                    <tr>

                        <td colspan="6">

                            <table id="wId${eleid}"  class="searchResult" border="0"  width="100%" style="cursor:hand;cursor:pointer;display:none" onClick="show_level('wId${eleid}');">

                                <thead class="header">

                                <tr class="row">
                                    <c:forEach begin="1" var="levelPair" items="${accountSiteHierarchy.levels}">
                                        <th class="cell cell-text"><nobr><c:out value="${levelPair.object2}"/></nobr> </th>
                                    </c:forEach>
                                    <th  class="cell cell-number"><nobr><app:message code="admin.account.siteHierarchy.label.totalOfLocations"/></nobr></th>
                                </tr>
                                </thead>

                                <tbody class="body">
                                <c:set var="preColId2" value="${0}"/>
                                <c:set var="preColId3" value="${0}"/>

                                <c:forEach var="item" varStatus="xx" items="${reportItem.value}">

                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel1Id"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel1Name"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel2Id"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel2Name"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel3Id"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel3Name"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel4Id"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].siteHierarchyLevel4Name"/>
                                    <form:hidden path="hierarchyReport[${reportItem.key}][${xx.index}].totalOfSites"/>

                                    <c:set var="colValue" value=""/>
                                    <c:set var="colHref" value=""/>
                                    <c:set var="colId" value="${0}"/>
                                    <c:set var="managePath" value=""/>
                                    <c:set var="configurePath" value=""/>
                                    <tr class="row">

                                        <c:forEach begin="1" varStatus="x" var="level" items="${accountSiteHierarchy.levels}">
                                            <c:choose>
                                                <c:when test="${x.index == 1}">
                                                    <c:set var="colId" value="${item.siteHierarchyLevel2Id}"/>
                                                    <c:set var="colValue" value="${colId != preColId2?item.siteHierarchyLevel2Name:''}"/>
                                                    <c:set var="managePath" value="${pathBase}/${item.siteHierarchyLevel1Id}/manage/${item.siteHierarchyLevel2Id}"/>
                                                    <c:set var="configurePath" value="${pathBase}/${item.siteHierarchyLevel1Id}/configure/${item.siteHierarchyLevel2Id}"/>
                                                </c:when>
                                                <c:when test="${x.index == 2}">
                                                    <c:set var="colId" value="${item.siteHierarchyLevel3Id}"/>
                                                    <c:set var="colValue" value="${colId != preColId3?item.siteHierarchyLevel3Name:''}"/>
                                                    <c:set var="managePath" value="${pathBase}/${item.siteHierarchyLevel1Id}/${item.siteHierarchyLevel2Id}/manage/${item.siteHierarchyLevel3Id}"/>
                                                    <c:set var="configurePath" value="${pathBase}/${item.siteHierarchyLevel1Id}/${item.siteHierarchyLevel2Id}/configure/${item.siteHierarchyLevel3Id}"/>
                                                </c:when>
                                                <c:when test="${x.index == 3}">
                                                    <c:set var="colValue" value="${item.siteHierarchyLevel4Name}"/>
                                                    <c:set var="colId" value="${item.siteHierarchyLevel4Id}"/>
                                                    <c:set var="managePath" value=""/>
                                                    <c:set var="configurePath" value="${pathBase}/${item.siteHierarchyLevel1Id}/${item.siteHierarchyLevel2Id}/${item.siteHierarchyLevel3Id}/configure/${item.siteHierarchyLevel4Id}"/>
                                                </c:when>
                                            </c:choose>

                                            <c:set var="colHref" value="${fn:length(accountSiteHierarchy.levels)>x.index+1?managePath:configurePath}"/>

                                            <td class="cell cell-text">
                                                <c:if test="${not empty colValue}">
                                                    <a href="${colHref}"><c:out value="${colValue}"/></a>
                                                </c:if>
                                                <c:if test="${ empty colValue}">
                                                    &nbsp;
                                                </c:if>


                                            </td>

                                        </c:forEach>
                                        <td class="cell cell-number"><c:out value="${item.totalOfSites}"/></td>
                                    </tr>

                                    <c:set var="preColId2" value="${item.siteHierarchyLevel2Id}"/>
                                    <c:set var="preColId3" value="${item.siteHierarchyLevel3Id}"/>

                                </c:forEach>
                                </tbody>

                            </table>
                        </td>
                    </tr>


                </c:forEach>

            </table>

        </app:box>

    </c:if>

</div>



</div>

</form:form>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql_rt" uri="http://java.sun.com/jstl/sql_rt" %>

<%@ page  import="java.io.File,java.io.FilenameFilter,java.util.Arrays"%>
<app:url var="baseUrl" value="/location/${siteSiteHierarchy.locationId}/locationHierarchy"/>

<script type="text/javascript" src="<c:url value="/resources/js/hierarchy.js"/>"></script>

<br><div style="font-size: 18px;font-weight: bold;" ><app:message code="admin.site.siteHierarchy.subheader.siteHierarchyDetail"/> </div>  <br>
<hr>

<c:if test="${not empty siteSiteHierarchy.levels}">

<script language="JavaScript1.2">

    $(document).ready(function () {
        siteHierarchyInitChange()
    });

    function siteHierarchyInit(array, currentValue, emptyMessage, selectElementName, levelIdx) {
        dynamicBoxes.init(array, currentValue, emptyMessage, selectElementName, levelIdx);
    }

    function siteHierarchyInitChange() {

        dynamicBoxes.initDynamicBoxArray(${fn:length(siteSiteHierarchy.levels)});

    <c:forEach var="l" varStatus="i" items="${siteSiteHierarchy.levels}">

        var elArray = new Array();
        var elIdx = 0;
    <c:set var="items" value="${siteSiteHierarchy.levelItems[i.index]}"/>
    <c:forEach var="item" items="${items}">
        elArray[elIdx] = new Array();
        elArray[elIdx][0] = '${item.object1}';
        elArray[elIdx][1] = '${item.object2}';
        elIdx++;
    </c:forEach>

        siteHierarchyInit(elArray,
                "${siteSiteHierarchy.selectedLevelItems[i.index]}",
                "<app:message code="admin.global.select"/>                                                                            ",
                "hierarchy_${i.index}",
                "${i.index}"
        );
    </c:forEach>

    }
</script>
<form:form modelAttribute="siteSiteHierarchy" method="POST" action="${baseUrl}">

    <div class="canvas">
        <br>

        <c:forEach var="l" varStatus="i" items="${siteSiteHierarchy.levels}">
            <form:hidden path="levels[${i.index}].name"/>
            <form:hidden path="levels[${i.index}].level"/>
            <form:hidden path="levels[${i.index}].number"/>
            <form:hidden path="levels[${i.index}].longDesc"/>
        </c:forEach>
        <c:forEach var="li"  varStatus="i" items="${siteSiteHierarchy.levelItems}">
            <c:forEach var="item"  varStatus="j" items="${li.value}">
                <form:hidden path="levelItems[${li.key}][${j.index}].object1"/>
                <form:hidden  path="levelItems[${li.key}][${j.index}].object2"/>
            </c:forEach>
        </c:forEach>
        <c:forEach var="sh"  varStatus="i" items="${siteSiteHierarchy.siteHierarchy}">
            <form:hidden  path="siteHierarchy[${i.index}].siteHierarchyId"/>
            <form:hidden  path="siteHierarchy[${i.index}].siteHierarchyNum"/>
            <form:hidden  path="siteHierarchy[${i.index}].siteHierarchyName"/>
            <form:hidden  path="siteHierarchy[${i.index}].valueId"/>
            <form:hidden  path="siteHierarchy[${i.index}].valueName"/>
        </c:forEach>

        <div class="">
            <table>
                <c:forEach var="l" varStatus="i" items="${siteSiteHierarchy.levels}">

                    <tr>
                        <td style="padding-bottom:5px;padding-left: 30px;"  class="label"><c:out value="${l.name}"/> </td>
                        <td class="labelValue" style="padding-bottom:5px;padding-left: 30px;">
                            <c:set var="onchange" value="$.post('${baseUrl}/change-level/${i.index}?level='+this.value, {}, function(data) {selectableObject.update(data, $('#hierarchy_${i.index}'))});"/>
                            <form:select id="hierarchy_${i.index}" path="selectedLevelItems[${i.index}]" cssStyle="width:auto;" onchange="${onchange}">
                            </form:select>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br>

        </div>

        <div class="search">


            <div style="padding-left: 20px" class="details filter">
                <c:if test="${not empty siteSiteHierarchy.siteHierarchy}">
                    <table width="100%">
                        <tr><td>
                            <app:box cssClass="white">
                                <table class="searchResult" width="100%">

                                    <colgroup>
                                        <col width="21%"/>
                                        <col width="21%"/>
                                        <col width="10%"/>
                                        <col width="10%"/>
                                        <col/>
                                    </colgroup>
                                    <thead class="header">
                                    <tr class="row">

                                        <th class="cell cell-text"><app:message code="admin.site.siteHierarchy.label.siteHierarchyLevel"/></th>
                                        <th class="cell cell-text"><app:message code="admin.site.siteHierarchy.label.siteHierarchyLevelValue"/></th>
                                        <th class="cell cell-number"><app:message code="admin.site.siteHierarchy.label.siteHierarchyLevelId"/></th>
                                        <th class="cell cell-number"><app:message code="admin.site.siteHierarchy.label.siteHierarchyLevelValueId"/></th>
                                        <th></th>
                                    </tr>

                                    </thead>
                                    <tbody class="searchResult" style="padding-top: 35px;">

                                    <c:forEach var="level" varStatus="i" items="${siteSiteHierarchy.siteHierarchy}">
                                        <tr class="row">
                                            <td class="cell cell-text"><c:out value="${level.siteHierarchyName}"/></td>
                                            <td class="cell cell-text"><c:out value="${level.valueName}"/></td>
                                            <td class="cell cell-number"><c:out value="${level.siteHierarchyId}"/></td>
                                            <td class="cell cell-number"><c:out value="${level.valueId}"/></td>
                                            <td></td>
                                        </tr>
                                    </c:forEach>

                                    </tbody>

                                </table></app:box>
                            <br></td></tr>

                    </table>
                </c:if>
                <hr>
                <table width="100%" style="padding-top: 0">
                    <tr><td  align="center" style="padding-top: 0">
                        <form:button><app:message code="admin.global.button.save"/></form:button>
                        <br> <br> </td>
                    </tr>
                </table>

            </div>

        </div>
    </div>
</form:form>
</c:if>
<c:if test="${empty siteSiteHierarchy.levels}">
    <div style="text-align: center;padding: 40px" class="label"><app:message code="admin.site.siteHierarchy.text.noSiteHierarchyDefinedYet"/></div>
</c:if>

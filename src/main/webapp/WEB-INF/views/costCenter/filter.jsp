<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="searchUrl" value="/costCenter/filter"/>
<app:url var="sortUrl" value="/costCenter/filter/sortby"/>
<app:url var="editUrl" value="/costCenter"/>
<app:url var="baseUrl"/>

<script type="text/javascript">
function changeFType(f) {
    if ('CATALOG' == document.getElementById("costCenterAccCatFilterType2").value) {
        document.getElementById("accFType").style.visibility = "hidden";
        document.getElementById("catFType").style.visibility = "visible";
        document.forms['costCenterFilter'].elements['costCenterAccCatFilterType'].value='CATALOG';
    } else {
        document.getElementById("accFType").style.visibility = "visible";
        document.getElementById("catFType").style.visibility = "hidden";
        document.forms['costCenterFilter'].elements['costCenterAccCatFilterType'].value='ACCOUNT';
    }
}
</script>
<c:set var="accfType" value="${costCenterFilter.costCenterAccCatFilterType == 'ACCOUNT' ? 'visible' : 'invisible'}"/>
<c:set var="catfType" value="${costCenterFilter.costCenterAccCatFilterType == 'CATALOG' ? 'visible' : 'invisible'}"/>

<c:set var="createAction" value="$('form:first').attr('action','${editUrl}/0/create');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="clearFieldsAction" value="$('form:first').attr('action','${baseUrl}/costCenter/filter/clear');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>

<div class="search">
  <div class="filter" >
<table>
<tr>
    <td class="cell label first"><label><app:message code="admin.global.filter.label.searchBy" /><span class="colon">:</span></label></td>
    <td colspan="2" class="cell value">


     <select tabindex="1" id="costCenterAccCatFilterType2" name="costCenterAccCatFilterType2" focusable="true" onchange="changeFType(this.form);">
        <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.costCenterAccCatFilterType}">
            <c:set var="selected" value="${costCenterFilter.costCenterAccCatFilterType == type.object2 ? 'selected' : ''}"/>
            <option value="${type.object2}" ${selected}><app:message code="refcodenames.COST_CENTER_FILTER_ACC_CAT_TYPE.${type.object1}" text="${type.object2}"/></option>
        </c:forEach>
     </select>
    </td>
    <td colspan="2" class="cell"></td>
</tr>


<tr>
<td colspan=5>
<div style="position:relative; height:90px;">
<div id="accFType" class="${accfType}" style="position:absolute; top:0px; left:0px;">
<jsp:include page="../locateAccount.jsp">
    <jsp:param name="baseViewName" value="costCenter"/>
    <jsp:param name="filteredAccountCommaNames" value="${costCenterFilter.filteredAccountCommaNames}" />
</jsp:include>
</div>
<div id="catFType" class="${catfType}" style="position:absolute; top:0px; left:0px;">
<jsp:include page="../locateCatalog.jsp">
    <jsp:param name="baseViewName" value="costCenter"/>
    <jsp:param name="catalogType" value="<%=RefCodeNames.CATALOG_TYPE_CD.ACCOUNT%>"/>
    <jsp:param name="filteredCatalogCommaNames" value="${costCenterFilter.filteredCatalogCommaNames}" />
</jsp:include>
</div>
</div>
</td>
</tr>
<form:form modelAttribute="costCenterFilter" method="GET" action="${searchUrl}" focus="costCenterName">
   <form:hidden path="costCenterAccCatFilterType"/>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.costCenterId" /><span class="colon">:</span></label></td>
                        <td colspan="2" class="cell value"><form:input tabindex="5" id="costCenterId" path="costCenterId" focusable="true" cssClass="filterValue"/></td>
                        <td colspan="2" class="cell"></td>
                    </tr>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.costCenterName" /><span class="colon">:</span></label></td>
                        <td   colspan="2" class="cell value"><form:input tabindex="6" id="costCenterName" path="costCenterName" focusable="true" cssClass="filterValue"/></td>
                        <td class="cell" style="padding-top: 0;padding-bottom: 0">
                            <form:radiobutton tabindex="7" focusable="true" path="costCenterNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                            <form:radiobutton tabindex="8" focusable="true" path="costCenterNameFilterType" cssClass="radio" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="cell label first"></td>
                        <td colspan="2" class="cell"><label><br><form:checkbox tabindex="9" cssClass="checkbox" path="showInactive"/><app:message code="admin.global.filter.label.showInactive" /><br><br></label></td>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <td class="cell label first"></td>
                        <td colspan="4" class="cell action">
                            <form:button  id="search"  cssClass="button" tabindex="10"  onclick="$('form:first').submit(); return false;"><app:message code="admin.global.button.search"/></form:button>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:button focusable="true" tabindex="11" onclick="${clearFieldsAction}"><app:message code="admin.global.button.clearFields"/></form:button >
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<form:button  focusable="true" tabindex="12" onclick="${createAction}"><app:message code="admin.global.button.create"/></form:button >
                        </td>

                    </tr>
                </table> <br><br>
                <form:hidden id="accountFilterInputIds" path="accountFilter" value="${costCenterFilter.filteredAccountCommaIds}"/>
                <form:hidden id="catalogFilterInputIds" path="catalogFilter" value="${costCenterFilter.filteredCatalogCommaIds}"/>
            </form:form>
        </div>

    <c:if test="${costCenterFilterResult.result != null}">
        <hr/>
        <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label><label class="value"></label>${fn:length(costCenterFilterResult.result)}</div>
    </c:if>
    <c:if test="${costCenterFilterResult.result != null}">

        <table class="searchResult" width="100%">

            <colgroup>
                <col width="8%"/>
                <col/>
                <col width="8%"/>
                <col width="15%"/>
                <col width="20%"/>
                <col width="10%"/>
                <col width="10%"/>
            </colgroup>

            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/costCenterId"><app:message code="admin.global.filter.label.costCenterId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/costCenterName"><app:message code="admin.global.filter.label.costCenterName" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.costCenterStatus" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/costCenterType"><app:message code="admin.global.filter.label.costCenterType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/taxType"><app:message code="admin.global.filter.label.taxType" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/allocateFreight"><app:message code="admin.global.filter.label.allocateFreight" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/allocateDiscount"><app:message code="admin.global.filter.label.allocateDiscount" /></a></th>
                <th></th>
            </tr>
            </thead>

            <tbody class="body">

            <c:forEach var="costCenter" items="${costCenterFilterResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${costCenter.costCenterId}"/></td>
                    <td class="cell cell-text"><a href="${editUrl}/${costCenter.costCenterId}"><c:out value="${costCenter.costCenterName}"/></a></td>
                    <td class="cell cell-text"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${costCenter.status}" text="${costCenter.status}"/></td>
                    <td class="cell cell-text"><c:out value="${costCenter.costCenterType}"/></td>
                    <td class="cell cell-text"><c:out value="${costCenter.taxType}"/></td>
                    <td class="cell cell-text"><c:out value="${costCenter.allocateFreight}"/></td>
                    <td class="cell cell-text"><c:out value="${costCenter.allocateDiscount}"/></td>
                    <td></td>
                </tr>
            </c:forEach>

            </tbody>

        </table>
    </c:if>
</div>


<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
<!--
.box {
 background-color: #F4F4F4;
 border: 1px solid #CCC;
 height: 100px;
 width: 200px;
 padding: 5px;
 display:none;
 position:absolute;
}

-->
</style>
<script type="text/javascript" language="JavaScript">
var cX = 0; var cY = 0; var rX = 0; var rY = 0;
function UpdateCursorPosition(e){ cX = e.pageX; cY = e.pageY;}
function UpdateCursorPositionDocAll(e){ cX = event.clientX; cY = event.clientY;}
if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
else { document.onmousemove = UpdateCursorPosition; }
function AssignPosition(d) {
if(self.pageYOffset) {
 rX = self.pageXOffset;
 rY = self.pageYOffset;
 }
else if(document.documentElement && document.documentElement.scrollTop) {
 rX = document.documentElement.scrollLeft;
 rY = document.documentElement.scrollTop;
 }
else if(document.body) {
 rX = document.body.scrollLeft;
 rY = document.body.scrollTop;
 }
if(document.all) {
 cX += rX; 
cY += rY;
 }
d.style.left = (cX+5) + "px";
d.style.top = (cY+5) + "px";
}
function HideText(d) {
if(d.length < 1) { return; }
document.getElementById(d).style.display = "none";
}
function ShowText(d) {
if(d.length < 1) { return; }
var dd = document.getElementById(d);
AssignPosition(dd);
dd.style.display = "block";
}
function ReverseContentDisplay(d) {
if(d.length < 1) { return; }
var dd = document.getElementById(d);
AssignPosition(dd);
if(dd.style.display == "none") { dd.style.display = "block"; }
else { dd.style.display = "none"; }
}
//-->
</script>

<app:url var="storebbaseUrl"/>
<app:url var="baseUrl" value="/group/${groupHeader.groupId}/configuration"/>
<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="searchUrlAllAssoc" value="${baseUrl}/filterAllAssociations"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="sortAllAssocUrl" value="${baseUrl}/filterAllAssociations/sortby"/>
<c:set var="updateAction" value="$('form:#groupConfigurationResult').submit(); return false;"/>

<c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
<c:set var="administrator" value="<%=RefCodeNames.USER_TYPE_CD.ADMINISTRATOR%>" />
<c:set var="system_administrator" value="<%=RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR%>" />
<c:set var="groupType" value="${groupHeader.groupType}" />

<c:set var="USER" value="<%=RefCodeNames.GROUP_TYPE_CD.USER%>" />
<c:if test="${groupType == USER}">
<c:set var="headerLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.users\")%>" />
<c:set var="idLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.userId\")%>" />
<c:set var="nameLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.userName\")%>" />
</c:if>
	        	
<c:set var="ACCOUNT" value="<%=RefCodeNames.GROUP_TYPE_CD.ACCOUNT%>" />
<c:if test="${groupType == ACCOUNT}">
<c:set var="headerLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.accounts\")%>" />
<c:set var="idLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.accountId\")%>" />
<c:set var="nameLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.accountName\")%>" />
</c:if>

<c:set var="DISTRIBUTOR" value="<%=RefCodeNames.GROUP_TYPE_CD.DISTRIBUTOR%>" />
<c:if test="${groupType == DISTRIBUTOR}">
<c:set var="headerLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.distributors\")%>" />
<c:set var="idLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.distributorId\")%>" />
<c:set var="nameLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.distributorName\")%>" />
</c:if>

<c:set var="MANUFACTURER" value="<%=RefCodeNames.GROUP_TYPE_CD.MANUFACTURER%>" />
<c:if test="${groupType == MANUFACTURER}">
<c:set var="headerLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.manufacturers\")%>" />
<c:set var="idLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.manufacturerId\")%>" />
<c:set var="nameLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.manufacturerName\")%>" />
</c:if>

<c:set var="STORE" value="<%=RefCodeNames.GROUP_TYPE_CD.STORE%>" />
<c:if test="${groupType == STORE}">
<c:set var="headerLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.primaryEntities\")%>" />
<c:set var="idLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.primaryEntityId\")%>" />
<c:set var="nameLabel" value="<%=AppI18nUtil.getMessage(\"admin.global.filter.label.primaryEntityName\")%>" />
</c:if>

<div class="canvas">
<div class="search">
    <form:form id="groupConfiguration" modelAttribute="groupConfiguration" method="GET" action="${searchUrl}" focus="searchName">
        <div class="filter">
            <table>
                <tbody>	        	
                <tr>
                    <td colspan="4">
                        <div class="subHeader">${headerLabel}</div>
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>                
                
                <tr>
                    <td width="70px">&nbsp;</td>
                    <td >
                        <label>${idLabel}<span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="1" id="searchId" path="searchId" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td colspan="1">&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td >
                        <label>${nameLabel}<span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="2" id="searchName" path="searchName" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td style="padding-top: 0; text-align: left">
                        <form:radiobutton tabindex="3" path="searchNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                        <form:radiobutton tabindex="3" path="searchNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="2" style="text-align:left; ">
                        <br>
                        <form:checkbox tabindex="4"  cssStyle="margin-left:0"  path="showConfiguredOnly"/>&nbsp;
                        <label><app:message code="admin.global.filter.label.showConfiguredOnly" /></label>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="2" style="text-align:left; ">
                        <br>
                        <form:checkbox tabindex="5"  cssStyle="margin-left:0"  path="showInactive"/>&nbsp;
                        <label><app:message code="admin.global.filter.label.showInactive"/></label>
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td  class="cell action" colspan="2">&nbsp;</td>
                    <td class="cell">
                        <form:button  id="search"  tabindex="6"><app:message code="admin.global.button.search"/></form:button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <hr/>
        <form:hidden path="groupType" value="${groupType}"/>
    </form:form>
    <form:form id="groupConfigurationResult" modelAttribute="groupConfigurationResult" method="POST" action="${updateUrl}"> 
    <c:if test="${groupConfigurationResult.groupConfigs != null}">    
    	<table width="100%">
            <tr>
                <td align="left">
                    <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value">${fn:length(groupConfigurationResult.result)}</label></div>
                </td>
                <td align="right">
                <c:if test="${fn:length(groupConfigurationResult.result) > 0}">   
                    <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
        <table class="searchResult" width="100%">
            <colgroup>
            	<col width="10%"/>
                <col width="25%"/>                
            	<c:choose>
	                <c:when test="${groupType == USER}">
	                	<col width="20%"/>
		                <col width="20%"/>
		                <col width="15%"/>
	                </c:when>
	                <c:otherwise>
		                <col width="20%"/>
		                <col width="15%"/>
		                <col width="10%"/>
		                <col width="10%"/>
	                </c:otherwise>
                </c:choose>	                
            	<col width="10%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/id">${idLabel}</a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/name">${nameLabel}</a></th>
                <c:choose>
	                <c:when test="${groupType == USER}">
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/firstName"><app:message code="admin.user.label.firstName" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/lastName"><app:message code="admin.user.label.lastName" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/userType"><app:message code="admin.user.label.userType" /></a></th>
	                </c:when>
	                <c:otherwise>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/address"><app:message code="admin.user.label.address1" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/city"><app:message code="admin.user.label.city" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/stateProvince"><app:message code="admin.user.label.stateProvince" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortUrl}/postalCode"><app:message code="admin.user.label.postalCode" /></a></th>                
	                </c:otherwise>
                </c:choose>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/status"><app:message code="admin.global.filter.label.status" /></a></th>
                <th class="cell cell-text cell-element">
                    <a href="javascript:checkAll('id', 'groupConfigs.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                    <a href="javascript:checkAll('id', 'groupConfigs.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
            </tr>
            </thead>
            <tbody class="body">
            <c:forEach var="config" varStatus="i" items="${groupConfigurationResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${config.value.id}"/> </td>
                    <td class="cell cell-text"><c:out value="${config.value.name}"/> </td>
                    <c:choose>
	                <c:when test="${groupType == USER}">
	                <td class="cell cell-text"><c:out value="${config.value.firstName}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.value.lastName}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.value.userType}"/> </td>
	                </c:when>
	                <c:otherwise>
	                <td class="cell cell-text"><c:out value="${config.value.address}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.value.city}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.value.stateProvince}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.value.postalCode}"/> </td>
	                </c:otherwise>
                </c:choose>
	                <td class="cell cell-text"><c:out value="${config.value.status}"/> </td>
                    <td class="cell cell-element">
                      <form:checkbox cssClass="checkbox" id="config_${config.value.id}" path="groupConfigs.selectableObjects[${i.index}].selected"/>
                    </td>
                </tr>
            </c:forEach>
            <tr><td colspan="9"><br><br></td></tr>
            <tr>
                <td colspan="9">&nbsp;</td>
                <td align="right">
                <c:if test="${fn:length(groupConfigurationResult.result) > 0}">   
            		<form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
        		</c:if>                        
                </td>
            </tr>
            </tbody>
        </table>
        <hr/>    
    </c:if>    
    </form:form>
    <c:if test="${currentUserType==administrator || currentUserType==system_administrator}">
        <form:form id="groupConfigurationAll" modelAttribute="groupConfigurationAll" method="GET" action="${searchUrlAllAssoc}">
        <div class="filter">
            <table>
                <tbody>	        	
                <tr>
                    <td colspan="5">
                        <div class="subHeader"><app:message code="admin.group.configuration.label.allAssociations"/></div>
                    </td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>                
                <tr>
                    <td width="70px">&nbsp;</td>
                    <td >
                        <label>${idLabel}<span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="7" id="searchId" path="searchId" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td width="100px">&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td >
                        <label>${nameLabel}<span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="8" id="searchName1" path="searchName" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td style="padding-top: 0; text-align: left">
                        <form:radiobutton tabindex="9" path="searchNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                        <form:radiobutton tabindex="9" path="searchNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="2">
                    	&nbsp;
                    </td>
                    <td class="cell">
                        <form:button  id="search"  tabindex="10"><app:message code="admin.global.button.search"/></form:button>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr><td colspan="5">&nbsp;</td></tr>
                </tbody>
            </table>
        </div>
        <hr/>
        <form:hidden path="groupType" value="${groupType}"/>
    </form:form>
    <c:if test="${groupConfigurationResult.groupAllEntityConfigs != null}">    
    	<table width="100%">
            <tr>
                <td align="left">
                    <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value">${fn:length(groupConfigurationResult.groupAllEntityConfigs)}</label></div>
                </td>
            </tr>
        </table>
        <table class="searchResult" width="100%">
            <colgroup>
            	<col width="10%"/>
                <c:choose>
	                <c:when test="${groupType == USER}">
	                	<col width="15%"/>
	                	<col width="15%"/>
		                <col width="15%"/>
		                <col width="15%"/>
	                </c:when>
	                <c:otherwise>
		                <col width="20%"/>
		                <col width="15%"/>
		                <col width="10%"/>
		                <col width="10%"/>
		                <col width="10%"/>
	                </c:otherwise>
                </c:choose>
				<col width="10%"/>   
            	<col width="20%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortAllAssocUrl}/id">${idLabel}</a></th>
                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/name">${nameLabel}</a></th>
                <c:choose>
	                <c:when test="${groupType == USER}">
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/firstName"><app:message code="admin.user.label.firstName" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/lastName"><app:message code="admin.user.label.lastName" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/userType"><app:message code="admin.user.label.userType" /></a></th>
	                </c:when>
	                <c:otherwise>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/address"><app:message code="admin.user.label.address1" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/city"><app:message code="admin.user.label.city" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/stateProvince"><app:message code="admin.user.label.stateProvince" /></a></th>
	                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/postalCode"><app:message code="admin.user.label.postalCode" /></a></th>                
	                </c:otherwise>
                </c:choose>
                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/status"><app:message code="admin.global.filter.label.status" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortAllAssocUrl}/assocStoreName"><app:message code="admin.group.configuration.label.primaryEntity" /></a></th>
                </th>
            </tr>
            </thead>
             <tbody class="body">
             <c:forEach var="config" varStatus="i" items="${groupConfigurationResult.groupAllEntityConfigs}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${config.groupConfigView.id}"/> </td>
                    <td class="cell cell-text"><c:out value="${config.groupConfigView.name}"/> </td>
                    <c:choose>
	                <c:when test="${groupType == USER}">
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.firstName}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.lastName}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.userType}"/> </td>
	                </c:when>
	                <c:otherwise>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.address}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.city}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.stateProvince}"/> </td>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.postalCode}"/> </td>
	                </c:otherwise>
                </c:choose>
	                <td class="cell cell-text"><c:out value="${config.groupConfigView.status}"/> </td>
	                <td>
	                <c:if test="${config.assocStoreName == config.assocStoreNames}">
                    	<c:out value="${config.assocStoreName}"/> 
                    </c:if>
                    
                    <c:if test="${config.assocStoreName != config.assocStoreNames}">
                    	<c:set var="assocStoreNames" value="assocStoreNames${i.index}" />
                    	<a title="${config.assocStoreNames}" href="#"><c:out value="${config.assocStoreName}"/></a>
                    </c:if>
                    </td>
                </tr>
            </c:forEach>
             </tbody>
        </table>
    </c:if>    
    
    </c:if> 
    
</div>
</div>

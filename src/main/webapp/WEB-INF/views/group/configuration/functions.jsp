<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="storebbaseUrl"/>
<app:url var="baseUrl" value="/group/${groupHeader.groupId}/function"/>
<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateAction" value="$('form:#groupFunctionFilterResult').submit(); return false;"/>

<script type="text/javascript">

function expandCollapseAll(hideId){
	var isHide = (eval("document.getElementById(hideId)").value=='false');
	document.getElementById(hideId).value = isHide;
	
	<c:if test="${groupFunctionFilterResult.functionsByType != null}">
	<c:forEach var="functionGroup" items="${groupFunctionFilterResult.functionsByType}">
		<c:set var="functionType" value="${functionGroup.key}"/>
		toggleView("${functionType}", isHide);
	</c:forEach>
	</c:if>
	
}

function expandCollapse(functionType){
	var detailDivId = "detail"+functionType;
	var isHide = !(eval("document.getElementById(detailDivId)").style.display=='none');
	toggleView(functionType, isHide);
}

function toggleView(functionType, isHide){
	var id = "view"+functionType;
	var detailDivId = "detail"+functionType;
	if(!isHide){
		eval("document.getElementById(detailDivId)").style.display='block';
	    eval("document.getElementById(id)").innerHTML="[-]";
	}else{
		eval("document.getElementById(detailDivId)").style.display='none';
	    eval("document.getElementById(id)").innerHTML="[+]";
	}
}
</script>

<div class="canvas">


<div class="search">
    <form:form id="groupFunctionFilter" modelAttribute="groupFunctionFilter" method="GET" action="${searchUrl}" focus="functionName">
        <div class="filter">
            <table>
                <tbody>
                <tr>
                    <td colspan="7">
                        <div class="subHeader"><app:message code="admin.group.configuration.label.applicationFunctions"/><span class="colon">:</span></div>
                    </td>
                </tr>
                <tr><td colspan="7">&nbsp;</td></tr>
                <tr>
                    <td width="70px">&nbsp;</td>
                    <td >
                        <label><app:message code="admin.message.label.name" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="2" id="functionName" path="functionName" focusable="true" cssClass="filterValue"/>
                    </td>
                    <td style="padding-top: 0; text-align: left">
                        <form:radiobutton tabindex="3" path="functionNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.START_WITH%>"/><span class="label"><app:message code="admin.global.filter.label.startWith"/></span><br>
                        <form:radiobutton tabindex="4" path="functionNameFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.FILTER_TYPE.CONTAINS%>"/><span class="label"><app:message code="admin.global.filter.label.contains"/></span>
                    </td>
                    <td><div class="cell cell-space">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</div></td>                    
                    <td >
                        <label><app:message code="admin.message.label.type" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                    	<form:select tabindex="5" path="functionType" focusable="true">
		                     <form:option value=""><app:message code="admin.global.select"/></form:option>
		                     <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.applicationFunctionTypes}">
		                         <form:option value="${type.object2}"><app:message code="refcodes.APPLICATION_FUNCTIONS_TYPE.${type.object1}" text="${type.object2}"/></form:option>
		                     </c:forEach>
		                 </form:select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                    <td colspan="5" style="text-align:left; ">
                        <br>
                        <form:checkbox tabindex="6"  cssStyle="margin-left:0"  path="showConfiguredOnly"/>&nbsp;
                        <label><app:message code="admin.global.filter.label.showConfiguredOnly" /></label>
                    </td>
                </tr>
                <tr><td colspan="7">&nbsp;</td></tr>
                <tr>
                    <td  class="cell action" colspan="2">&nbsp;</td>
                    <td class="cell">
                        <form:button  id="search"  tabindex="7"><app:message code="admin.global.button.search"/></form:button>
                        <br>
                        <br>
                        <br>
                    </td>
                    <td colspan="5">&nbsp;</td>
                </tr>
                </tbody>
            </table>
        </div>
    </form:form>
    <form:form id="groupFunctionFilterResult" modelAttribute="groupFunctionFilterResult" method="POST" action="${updateUrl}">
    
    <c:if test="${groupFunctionFilterResult.functionsByType != null && groupFunctionFilterResult.functionsByType.size()>0}">
    	<hr/> 
    	<table width="100%">
        	<tr>
                <td align="right">
                    <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                </td>
            </tr>
       	</table>  
       	<hr/>      
    	<table width="100%">
    		<colgroup>
            	<col width="60%"/>
                <col width="40%"/>
            </colgroup>
       		<tr>
       			<td>&nbsp;</td>
                <td>
                	<input type="hidden" name="hideDetail" id="hideDetail" value="false">
                   <div class="subHeader" align="center" style="cursor: pointer;"><a href="javascript:expandCollapseAll('hideDetail')"><span>Expand/Collapse All</span></a></div>
                </td>
           </tr>
      	</table>    
        <c:forEach var="functionGroup" items="${groupFunctionFilterResult.functionsByType}">
        	<c:set var="functionType" value="${functionGroup.key}"/>
        	<c:set var="detailDivId" value="detail${functionType}"/>
        	<c:set var="expandCollapseId" value="view${functionType}"/>
        	<table width="100%">
	        	<tr>
	                <td align="left">
	                    <div class="subHeader">${functionType}&nbsp;&nbsp;<a href="javascript:expandCollapse('${functionType}')"><span id="${expandCollapseId}">[-]</span></a></div>
	                </td>
	            </tr>
        	</table>
        	
        	<div id="${detailDivId}" class="view">
        	<table class="searchResult" width="100%">        	
            <colgroup>
            	<col width="5%"/>
                <col width="10%"/>
                <col width="85%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
            	<th/>
            	<% Object functionType = pageContext.getAttribute("functionType");
            	if (functionType.equals("SERVICES/ASSETS")) 
            		functionType = "SERVICES_ASSETS";
            	pageContext.setAttribute("functionType1", functionType);
            	%>
                <th class="cell cell-text cell-element">
                    <a href="javascript:checkAll('functionSelectedResultId', 'functionsByType[\'${functionType}\'].selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                    <a href="javascript:checkAll('functionSelectedResultId', 'functionsByType[\'${functionType}\'].selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/${functionType1}"><app:message code="admin.group.configuration.label.functionName" /></a></th>
            </tr>
            </thead>
            <tbody class="body">
            <c:forEach var="function" varStatus="i" items="${functionGroup.value.selectableObjects}" >
                <tr class="row">
                	<td/>
                    <td class="cell cell-element">                    
                      <form:checkbox cssClass="checkbox" id="function_${function.value.functionName}" path="functionsByType['${functionType}'].selectableObjects[${i.index}].selected"/>
                    </td>
                    <td class="cell cell-text"><c:out value="${function.value.functionName}"/> </td>
                </tr>
            </c:forEach>
            <tr><td colspan="3"><br><br></td></tr>            
            </tbody>
        </table>
        </div>
        <hr/>         	
        </c:forEach>
        <table width="100%">
        	<tr>
                <td align="right">
                    <form:button onclick="${updateAction}"><app:message code="admin.global.button.save" /></form:button>
                </td>
            </tr>
       	</table>
    </c:if>
    </form:form>    
</div>
</div>

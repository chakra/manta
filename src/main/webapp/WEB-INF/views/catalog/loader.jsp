<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<app:url var="loaderUrl" value="/catalogManager/loader"/>
<app:url var="cancelUrl" value="/catalogManager/loader/cancel"/>
<app:url var="sortUrl" value="/catalogManager/loader/sortby"/>

<c:set var="saveAction" value="$('form:first').submit(); return false; "/>
<c:set var="cancelAction" value="$('form:#catalogLoaderResult').submit(); return false;"/>
<c:set var="downloadAction" value="$('form:#catalogLoaderResult').attr('action','${baseUrl}/catalogManager/loader/download');$('form:#catalogLoaderResult').submit(); return false; "/>
<c:set var="displayAllAction" value="window.location.href='${baseUrl}/catalogManager/loader/displayAll'; return false; "/>
<c:set var="exportErrorsAction" value="window.location.href='${baseUrl}/catalogManager/loader/exportErrors'; return false; "/>
<c:set var="processNowAction" value="$('form:first').attr('action','${baseUrl}/catalogManager/loader/processNow');$('form:first').submit(); return false; "/>
<app:dateIncludes/>

<script type="text/javascript">
        function download(id, index) {
            var frm = document.getElementById('downloadForm');
            var fn = document.getElementById('fileName' + index);
            if (frm && fn) {
                frm.elements['id'].value = id;
                frm.elements["name"].value = fn.value;
                frm.submit();
            }
        }
</script>



<div class="canvas">

<div class="details std">

<form:form modelAttribute="catalogLoader" action="${loaderUrl}" enctype="multipart/form-data" focus="selectedFile" method="POST">              	

<table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    	<td style="text-align: left">
			<table>
	        	<tbody>
	        		<tr>
	        			<td></td>
	        			<td></td>
	        			<td></td>
	        		</tr>     
	                <tr>
	                    <td class="cell label first"><label><app:message code="catalogManager.loader.label.selectFile"/><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td  colspan="2" class="cell value"><form:input tabindex="1" path="uploadedFile" type="file" size="32"/></td> 
	                </tr>					
	                <tr>
	                    <td class="cell label first"><label><app:message code="catalogManager.loader.label.effectiveDate" /><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td colspan="2" class="cell value"><form:input tabindex="4" path="effectiveDate" cssClass="datepicker2Col standardCal standardActiveCal inputShort"/></td>
	                </tr>
	                <tr>
	                    <td class="cell label first"><label><app:message code="catalogManager.loader.label.timeZone" /><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td colspan="2" class="cell value">
	                    	<form:select tabindex="5" path="timeZone" cssClass="selectMid">
			                   <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.timeZones}">
			                        <form:option value="${type.object2}"><app:message code="refcodes.TIME_ZONE_CD.${type.object1}" text="${type.object2}"/></form:option>
			                    </c:forEach>
	                		</form:select>
                		</td>
	                </tr>
	                <tr>
	                	<td style="padding-right: 30px;"><form:button tabindex="6" onclick="${saveAction}"><app:message code="admin.global.button.save"/> </form:button></td>
	                	<td style="padding-right: 10px;"><form:button tabindex="7" onclick="${displayAllAction}"><app:message code="admin.global.button.displayAll"/> </form:button></td>
	                	<td style="padding-right: 10px;"><form:button tabindex="8" onclick="${processNowAction}"><app:message code="admin.global.button.processNow"/> </form:button></td>
	    			</tr>
	    		</tbody>
	    	</table>
 		</td>
 	</tr>
</table>
<hr/>
</form:form>
</div>
<div class="search">

<form:form id="catalogLoaderResult" modelAttribute="catalogLoaderResult" method="POST" action="${cancelUrl}"> 
    <c:if test="${catalogLoaderResult.catalogs != null}">    
    	<table width="100%">
            <tr>                
            	<td align="left">
                    <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value">${fn:length(catalogLoaderResult.result)}</label></div>
                </td>
                <td align="right">
                <c:if test="${fn:length(catalogLoaderResult.result) > 0}">   
                    <form:button onclick="${cancelAction}"><app:message code="admin.global.button.cancel" /></form:button>
                    <form:button onclick="${downloadAction}"><app:message code="admin.global.button.download" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
        <table class="searchResult" width="100%">
            <colgroup>
            	<col width="15%"/>
                <col width="25%"/>
               	<col width="15%"/>
               	<col width="15%"/>
               	<col width="15%"/>
            	<col width="15%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/eventId"><app:message code="catalogManager.loader.label.eventId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/fileName"><app:message code="catalogManager.loader.label.fileName" /></a></th>
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/catalogCount"><app:message code="catalogManager.loader.label.count" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/effectiveDate"><app:message code="catalogManager.loader.label.effectiveDate" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/timeZone"><app:message code="catalogManager.loader.label.timeZone" /></a></th>
                <th class="cell cell-text cell-element">
                    <a href="javascript:checkAll('catalogLoaderResult', 'catalogs.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                    <a href="javascript:checkAll('catalogLoaderResult', 'catalogs.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
            </tr>
            </thead>
            <tbody class="body">
             
            <c:forEach var="catalog" varStatus="i" items="${catalogLoaderResult.result}" >
                <tr class="row">
                
                    <td class="cell cell-number"><c:out value="${catalog.value.eventId}"/> </td>
                    <td class="cell cell-text"><c:out value="${catalog.value.fileName}"/> </td>
                    <td class="cell cell-number"><c:out value="${catalog.value.catalogCount}"/> </td>
<%-- 	                <td class="cell cell-text"><c:out value="${catalog.value.effectiveDate}"/> </td> --%>
	                <td class="cell cell-text"><app:formatDate value="${catalog.value.effDateD}"/></td>
	                <td class="cell cell-text"><c:out value="${catalog.value.timeZone}"/> </td>
                    <td class="cell cell-element">
                      <form:checkbox cssClass="checkbox" id="catalog_${catalog.value.eventId}" path="catalogs.selectableObjects[${i.index}].selected"/>
                    </td>
                </tr>
            </c:forEach>            
            </tbody>
        </table>
        <table width="100%">
        	<tr><td>&nbsp;</td></tr>
            <tr>                
            	<td align="right">
                <c:if test="${fn:length(catalogLoaderResult.result) > 0}">   
                    <form:button onclick="${cancelAction}"><app:message code="admin.global.button.cancel" /></form:button>
                    <form:button onclick="${downloadAction}"><app:message code="admin.global.button.download" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
    </c:if>    
</form:form>
</div>
</div>
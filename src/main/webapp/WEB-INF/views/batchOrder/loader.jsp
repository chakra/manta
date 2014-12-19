<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<app:url var="loaderUrl" value="/batchOrder/loader"/>
<app:url var="cancelUrl" value="/batchOrder/loader/cancel"/>
<app:url var="sortUrl" value="/batchOrder/loader/sortby"/>

<c:set var="saveAction" value="$('form:first').submit(); return false; "/>
<c:set var="cancelAction" value="$('form:#batchOrderLoaderResult').submit(); return false;"/>
<c:set var="displayAllAction" value="window.location.href='${baseUrl}/batchOrder/loader/displayAll'; return false; "/>
<c:set var="exportErrorsAction" value="window.location.href='${baseUrl}/batchOrder/loader/exportErrors'; return false; "/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('siteName');}"/>
<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account/single'
                 action="${baseUrl}/locate/account/selected"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountName"
                 targetIds="accountId"
                 finallyHandler="${finallyHandler}"/>
                 
<app:dateIncludes/>

<div class="canvas">

<div class="details std">

<form:form modelAttribute="batchOrderLoader" action="${loaderUrl}" enctype="multipart/form-data" focus="selectedFile" method="POST">              	

<table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    	<td style="text-align: left">
			<table>
	        	<tbody>
	        		<tr>
	        			<td></td>
	        			<td></td>
	        		</tr>     
	                <tr>
	                    <td class="cell label first"><label><app:message code="batchOrder.loader.label.selectFile"/><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td class="cell value"><form:input tabindex="1" path="uploadedFile" type="file" size="32"/></td> 
	                </tr>
                    <tr>
                        <td class="cell label first"><label><app:message code="admin.global.filter.label.accountName"/><span class="colon">:</span></label><span class="reqind">*</span></td>
                        <td class="cell value">
                        <div class="value">
                            <form:input cssClass="inputShort readonly" readonly="true" path="accountName"/><form:hidden path="accountId"/>
                            <button  style="margin-left:20px" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button>
                        </div>
                        </td> 
                    </tr>
					<tr>
	                    <td class="cell label first" colspan="2">
							<form:checkbox tabindex="2" cssClass="checkbox" path="applyToBudget"/>
	                    	<span class="label"><app:message code="batchOrder.loader.label.applyToBudget"/></span>
	                    </td>
	                </tr>
	                <tr>
	                	<td class="cell label first" colspan="2">
							<form:checkbox tabindex="3" cssClass="checkbox" path="sendConfirmation"/>
	                    	<span class="label"><app:message code="batchOrder.loader.label.sendConfirmation"/></span>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="cell label first"><label><app:message code="batchOrder.loader.label.processOn" /><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td class="cell value"><form:input tabindex="4" path="processOn" cssClass="datepicker2Col standardCal standardActiveCal inputShort"/></td>
	                </tr>
	                <tr>
	                    <td class="cell label first"><label><app:message code="batchOrder.loader.label.processWhen" /><span class="colon">:</span></label><span class="reqind">*</span></td>
	                    <td class="cell value">
	                    	<form:select tabindex="5" path="processWhen" cssClass="selectShort">
	                    		<app:i18nRefCodes var="code" items="${batchOrderLoader.processWhenChoices}" i18nprefix="">
	                            		<form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
	                        	</app:i18nRefCodes>
	                		</form:select>
                		</td>
	                </tr>
	                <tr>
	                	<td style="padding-right: 30px;"><form:button tabindex="6" onclick="${saveAction}"><app:message code="admin.global.button.save"/> </form:button></td>
	                	<td style="padding-right: 30px;"><form:button tabindex="7" onclick="${displayAllAction}"><app:message code="admin.global.button.displayAll"/> </form:button></td>
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

<form:form id="batchOrderLoaderResult" modelAttribute="batchOrderLoaderResult" method="POST" action="${cancelUrl}"> 
    <c:if test="${batchOrderLoaderResult.batchOrders != null}">    
    	<table width="100%">
            <tr>                
            	<td align="left">
                    <div class="resultCount label"><label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
                    <label class="value">${fn:length(batchOrderLoaderResult.result)}</label></div>
                </td>
                <td align="right">
                <c:if test="${fn:length(batchOrderLoaderResult.result) > 0}">   
                    <form:button onclick="${cancelAction}"><app:message code="admin.global.button.cancel" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
        <table class="searchResult" width="100%">
            <colgroup>
            	<col width="10%"/>
                <col width="15%"/>
               	<col width="10%"/>
               	<col width="10%"/>
               	<col width="10%"/>                
                <col width="10%"/>
                <col width="10%"/>
                <col width="10%"/>
            	<col width="15%"/>
            </colgroup>
            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/eventId"><app:message code="batchOrder.loader.label.eventId" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/fileName"><app:message code="batchOrder.loader.label.fileName" /></a></th>
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/orderCount"><app:message code="batchOrder.loader.label.orderCount" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/applyToBudget"><app:message code="batchOrder.loader.label.applyToBudget" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/sendConfirmation"><app:message code="batchOrder.loader.label.sendConfirmation" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/processOn"><app:message code="batchOrder.loader.label.processOn" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/processWhen"><app:message code="batchOrder.loader.label.processWhen" /></a></th>
                <th class="cell cell-text"><a class="sort" href="${sortUrl}/processWhen"><app:message code="admin.global.filter.label.accountId" /></a></th>
                <th class="cell cell-text cell-element">
                    <a href="javascript:checkAll('id', 'batchOrders.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                    <a href="javascript:checkAll('id', 'batchOrders.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
            </tr>
            </thead>
            <tbody class="body">
            <c:forEach var="batchOrder" varStatus="i" items="${batchOrderLoaderResult.result}" >
                <tr class="row">
                    <td class="cell cell-number"><c:out value="${batchOrder.value.eventId}"/> </td>
                    <td class="cell cell-text"><c:out value="${batchOrder.value.fileName}"/> </td>
                    <td class="cell cell-number"><c:out value="${batchOrder.value.orderCount}"/> </td>
	                <td class="cell cell-text"><c:out value="${batchOrder.value.applyToBudget}"/> </td>
	                <td class="cell cell-text"><c:out value="${batchOrder.value.sendConfirmation}"/> </td>
	                <td class="cell cell-text"><c:out value="${batchOrder.value.processOn}"/> </td>
	                <td class="cell cell-text"><c:out value="${batchOrder.value.processWhen}"/> </td>
                    <td class="cell cell-text"><c:out value="${batchOrder.value.accountId}"/> </td>
                    <td class="cell cell-element">
                      <form:checkbox cssClass="checkbox" id="batchOrder_${batchOrder.value.eventId}" path="batchOrders.selectableObjects[${i.index}].selected"/>
                    </td>
                </tr>
            </c:forEach>            
            </tbody>
        </table>
        <table width="100%">
        	<tr><td>&nbsp;</td></tr>
            <tr>                
            	<td align="right">
                <c:if test="${fn:length(batchOrderLoaderResult.result) > 0}">   
                    <form:button onclick="${cancelAction}"><app:message code="admin.global.button.cancel" /></form:button>
                </c:if>
                </td>
            </tr>
        </table>
    </c:if>    
</form:form>
</div>
</div>
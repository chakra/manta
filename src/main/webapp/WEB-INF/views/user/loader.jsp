<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.RefCodeNamesKeys" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%@ page import="com.espendwise.manta.util.*"%>
<%@ page import="com.espendwise.manta.web.forms.*"%>
<%@ page import="com.espendwise.manta.model.data.UploadSkuData"%>
<%@ page import="com.espendwise.manta.model.view.UploadSkuView"%>

<app:url var="baseUrl"/>
<c:set var="processingMessage" value="<%=AppI18nUtil.getMessage(\"admin.global.text.fileProcessing\")%>"/>
<c:set var="uploadAction" value="$('form:first').attr('action','${baseUrl}/user/loader/loadFile');showFileProcessingMessage('${processingMessage}');$('form:first').submit(); return false; "/>
<c:set var="exportTemplateAction" value="$('form:first').attr('action','${baseUrl}/user/loader/exportTemplate');$('form:first').submit(); return false; "/>
<script type="text/javascript">
	var processingMessageId = 0;
	var count = 0;
	function showFileProcessingMessage(message) {
		writemessage("webErrorsDiv",message);
		writemessage("webSuccessMessagesDiv","");
        processingMessageId = setInterval ("showProcessingMessage()",2000); 
    };
    function showProcessingMessage(){
    	if (count == 5){ 
    		count = 0;
    	}else{
    		count += 1;
    	}
    	
    	var processingMessage = document.getElementById("processingMessageHidden").value;
    	if (processingMessage.length==20)
    		processingMessage = processingMessage.substring(0,processingMessage.indexOf('.'));
    	else
    		processingMessage += ".";
    	
 		writemessage("webErrorsDiv",processingMessage);
    	
	}
	
	function writemessage(id, message) {
		var messageDiv = document.getElementById(id);
		if (!messageDiv)
		   return;
		if (message == ""){
			messageDiv.style.display = "none"; //to hide it
		}else{
			messageDiv.style.display = "block"; //to show it 
			document.getElementById(id).innerHTML = "<p>" + message + "<br/></p>";
			document.getElementById("processingMessageHidden").value = message;
		}
    }
    
</script>
<div class="search">
<div class="filter" >

<form:form id="userLoader" modelAttribute="userLoader" action="" method="POST"  enctype="multipart/form-data">
<input type="hidden" id="processingMessageHidden" name="processingMessageHidden" value=""/>

<table class="user-filter">
<tr>
	<td class="cell first">
	    <span class="label"><form:label path="uploadedFile">
	    <app:message code="admin.uploadFile.label.selectFile"/>:</form:label></span>
    </td>
    <td class="cell">
    	<form:input tabindex="1" path="uploadedFile" type="file" size="32"/>
    </td>
</tr>
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td colspan="2">&nbsp;</td></tr>
<tr>
	<td>&nbsp;</td>
    <td class="cell">
    	<form:button id="uploadedFile"  cssClass="button" tabindex="3" onclick="${uploadAction}"><app:message code="admin.global.button.save"/></form:button>
    	&nbsp;&nbsp;&nbsp;
    	<form:button id="exportTemplate"  cssClass="button" tabindex="4"  onclick="${exportTemplateAction}"><app:message code="admin.global.button.exportTemplate"/></form:button>
    </td>
</tr>
</table>
</form:form>
</div>
</div>


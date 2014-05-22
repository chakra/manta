<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/profile/passwordManagement'); "/>

<app:dateIncludes/>

<div class="canvas">

<div class="details std">

<form:form modelAttribute="profilePasswordManagement" action="" focus="reqResetOnInitLogin" method="POST">              	

<table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
    <tr>
    	<td style="text-align: left">
			<table>
	        	<tbody>
	        		<tr>
	        			<td width="10%"></td>
	        			<td></td>
	        		</tr>
	                <tr><td>&nbsp;</td></tr>
	                <tr>
	                	<td>&nbsp;</td>
	                    <td class="cell label">
	                       <form:checkbox tabindex="1" cssClass="checkbox" path="allowChangePassword"/>
	                       <span class="label"><app:message code="profile.passwordManagement.label.allUserToChangePassword" /></span>
	                   	</td>
	        		</tr>
	        		<tr>
	                	<td>&nbsp;</td>
	                    <td class="cell label">
	                       <form:checkbox tabindex="2" cssClass="checkbox" path="reqPasswordResetUponInitLogin"/>
	                       <span class="label"><app:message code="profile.passwordManagement.label.requireResetUponInitailLogin" /></span>
	                   	</td>
	        		</tr>
	        		<tr>
	                	<td>&nbsp;</td>
	                    <td class="cell label">
	                    	<form:checkbox tabindex="3" cssClass="checkbox" path="reqPasswordResetInDays"/>
	                     <span class="label"><app:message code="profile.passwordManagement.label.requireResetEvery" />&nbsp;</span>	                    
		            	 <form:input cssClass="std name" tabindex="4" path="expiryInDays" size="2" maxlength="3" focusable="true"/>&nbsp;
		            	 <form:label path="expiryInDays"><app:message code="profile.passwordManagement.label.days"/></form:label>&nbsp;
		            	 <form:label path="expiryInDays"><app:message code="profile.passwordManagement.label.notifyWithin"/></form:label>&nbsp;
		            	 <form:input cssClass="std name" tabindex="5" path="notifyExpiryInDays" size="2" maxlength="3" focusable="true"/>&nbsp;
		            	 <app:message code="profile.passwordManagement.label.days2"/>
			 			</td>
	        		</tr>	        		
 					<tr><td colspan="2">&nbsp;</td></tr>
 					<tr><td colspan="2">&nbsp;</td></tr>
	        		<tr><td colspan="2" align="center" style="text-align: center;vertical-align:top;padding-bottom:0;">		
	        			<table width="100%">
	            		<tr>
	                		<td style="padding-right: 30px;"><form:button tabindex="5" onclick="${updateAction}"><app:message code="admin.global.button.save"/> </form:button></td>
	            		</tr>
	        			</table>
	    				</td>
	    			</tr>
	    		</tbody>
	    	</table>
 		</td>
 	</tr>
</table>
</form:form>
</div>
</div>
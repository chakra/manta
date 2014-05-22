<%@ page import="com.espendwise.manta.i18n.I18nUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>
<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/corporateSchedule/${corporateSchedule.scheduleId > 0 ? corporateSchedule.scheduleId: 0}');$('form:first').submit(); return false; "/>
<c:set var="deleteAction" value="$('form:first').attr('action','${baseUrl}/corporateSchedule/${corporateSchedule.scheduleId}/delete');$('form:first').submit();"/>

<div class="canvas">

<div class="details">

<form:form modelAttribute="corporateSchedule" id="corporateSchedule" action="" method="POST">
<table width="100%">
	<tr>
    	<td>
        	<table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
            	<tr>
                	<td style="text-align: left">
                    	<div class="row">
                        	<div class="cell">
                            	<table>
                                	<tbody>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleId">
                                            			<app:message code="admin.corporateSchedule.label.corporateScheduleId"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                        		</div>
                                    		</td>
                                    		<td>
                                    			<div class="labelValue">
                                    				<c:out value="${corporateSchedule.scheduleId}" default="0"/>
                                    			</div>
                                    			<form:hidden path="scheduleId" value="${corporateSchedule.scheduleId}"/>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleName">
                                            			<app:message code="admin.corporateSchedule.label.corporateScheduleName"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                            		<span class="reqind">*</span>
                                        		</div>
                                    		</td>
                                    		<td>
                                        		<form:input tabindex="2" path="scheduleName" size="35"/>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleStatus">
                                            			<app:message code="admin.corporateSchedule.label.status"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                            		<span class="reqind">*</span>
                                            	</div>
                                    		</td>
                                    		<td>
                                        		<form:select tabindex="3" style="width:200px" path="scheduleStatus">
                                            		<form:option value="">
                                            			<app:message code="admin.global.select"/>
                                            		</form:option>
			                        				<app:i18nRefCodes var="code" items="${corporateSchedule.statusChoices}" i18nprefix="">
			                            				<form:option value="${code.object2}">
			                            					<c:out value="${code.object1}"/>
			                            				</form:option>
			                        				</app:i18nRefCodes>
                                        		</form:select>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleIntervalHours">
                                            			<app:message code="admin.corporateSchedule.label.intervalHours"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                            		<span class="reqind">*</span>
                                        		</div>
                                    		</td>
                                    		<td>
                                        		<form:input tabindex="4" path="scheduleIntervalHours" maxlength="3" size="35"/>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleCutoffTime">
                                            			<app:message code="admin.corporateSchedule.label.cutoffTime"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                            		<span class="reqind">*</span>
                                        		</div>
                                        		<div style="font-size:10px;">
                                            		<app:message code="admin.corporateSchedule.label.cutoffTimeFormat"/>
                                        		</div>
                                    		</td>
                                    		<td>
                                        		<form:input tabindex="5" path="scheduleCutoffTime" size="35"/>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="scheduleAlsoDates">
                                            			<app:message code="admin.corporateSchedule.label.scheduleDates"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                        		</div>
                                        		<div style="font-size:10px;">
                                            		<app:message code="admin.corporateSchedule.label.scheduleDatesFormat"
                                            		arguments="<%=I18nUtil.getDatePatternPrompt()%>"/>
                                        		</div>
                                    		</td>
                                    		<td>
						                        <form:textarea tabindex="6" path="scheduleAlsoDates" cols="60" rows="4"/>
                                    		</td>
                                		</tr>
                                		<tr>
                                    		<td>
                                        		<div class="label">
                                            		<form:label path="schedulePhysicalInventoryDates">
                                            			<app:message code="admin.corporateSchedule.label.physicalInventoryDates"/>
                                            			<span class="colon">:</span>
                                            		</form:label>
                                        		</div>
                                        		<div style="font-size:10px;">
                                            		<app:message code="admin.corporateSchedule.label.physicalInventoryDatesFormat"
                                            		arguments="<%=I18nUtil.getDatePatternPrompt()%>"/>
                                        		</div>
                                    		</td>
                                    		<td>
						                        <form:textarea tabindex="7" path="schedulePhysicalInventoryDates" cols="60" rows="4"/>
                                    		</td>
                                		</tr>
                                	</tbody>
                            	</table>
                        	</div>
                    	</div>
                	</td>
           		</tr>
        	</table>
    	</td>
	</tr>
	<tr>
		<td>
			<hr/>
		</td>
	</tr>
    <tr align=center>
    	<td style="vertical-align:top;text-align: center;white-space: nowrap;">
	        <table width="100%">
            	<tbody>
                	<tr>
                    	<td style="text-align: center">
                        	<div class="actions">
                            	<form:button tabindex="1000" onclick="${updateAction} return false;">
                            		<app:message code="admin.global.button.save"/>
                            	</form:button>
                                <c:if test="${corporateSchedule.scheduleId!=null && corporateSchedule.scheduleId>0}">
                                    <form:button style="margin-left:20px" tabindex="11"  onclick="${deleteAction} return false;">
                                    	<app:message code="admin.global.button.delete"/>
                                    </form:button>
                                </c:if>
                            </div>
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
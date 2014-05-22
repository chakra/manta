<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/user/${userNotification.userId}/notification"/>

<div class="canvas">
    <div class="details">
        <br>
        <div class="subHeader"><app:message code="admin.user.configuration.notif.notif"/></div>
        <br>
        <form:form modelAttribute="userNotification" action="${baseUrl}" method="POST">
            <div>
                <table width="100%">
                    <tr>
                        <td class="subitem">
                            <div class="row" style="width:100%">
                                <div class="cell" style="width:70%">
                                    <table width="100%">

                                        <colgroup>
                                            <col width="50%"/>
                                            <col width="50%"/>
                                        </colgroup>

                                        <thead><tr><td colspan="2"> <div class="subHeader"><app:message code="admin.user.configuration.notif.label.purchasing"/></div><br></td></tr></thead>

                                        <tbody>
                                        <tr>
                                            <td class="cell label"><div class="label"><app:message code="admin.user.configuration.notif.label.goodPurchasingEmails" /></div></td>
                                            <td class="cell label"><div class="label"><app:message code="admin.user.configuration.notif.label.corporateOrderNotifications" /></div></td>
                                        </tr>
                                        <tr>
                                            <td class="cell label" style="vertical-align: top;padding-left: 5px">
                                                <table>
                                                    <c:forEach var="notifName" items="${userNotification.foodPurchasingEmail}">
                                                        <tr>
                                                            <td class="cell label">
                                                                <form:checkbox tabindex="5" cssClass="checkbox" path="emailNotificationMapping[${notifName}].value" value="true"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].shortDesc"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].typeCd"/>
                                                                <span class="label"><app:message code="admin.template.email.emailType.${notifName}" text="${notifName}"/></span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </td>
                                            <td class="cell label" style="vertical-align: top;padding-left: 5px">
                                                <table>
                                                    <c:forEach var="notifName" items="${userNotification.corporateOrderNotifications}">
                                                        <tr>
                                                            <td class="cell label">

                                                                <c:if test="${notifName == userNotification.cutoffTimeReminder}">
                                                                    <form:checkbox id="cutoffTimeReminder"
                                                                                   onclick="$(document).find('input[linked=cutoffTimeReminder]').each(function(index) { $(this).attr('value',''); $(this).attr('disabled', $('#cutoffTimeReminder').attr('checked')?'':'disabled')});"
                                                                                   cssClass="checkbox"
                                                                                   path="emailNotificationMapping[${notifName}].value"
                                                                                   value="true"/>
                                                                </c:if>

                                                                <c:if test="${notifName != userNotification.cutoffTimeReminder}">
                                                                    <form:checkbox cssClass="checkbox" path="emailNotificationMapping[${notifName}].value" value="true"/>
                                                                </c:if>

                                                                <form:hidden  path="emailNotificationMapping[${notifName}].shortDesc"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].typeCd"/>

                                                                <span class="label"><app:message code="admin.template.email.emailType.${notifName}" text="${notifName}"/></span>

                                                                <c:if test="${notifName == userNotification.cutoffTimeReminder}">
                                                                    <br>
                                                                    <div style="padding-left: 20px">
                                                                        <table>
                                                                            <tr>
                                                                                <td><span class="label"><app:message code="admin.user.configuration.notif.label.numberofTimes"/></span> </td>
                                                                                <td><form:input
                                                                                        disabled="${userNotification.emailNotificationMapping[notifName].value !=true}"
                                                                                        linked="cutoffTimeReminder"
                                                                                        path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.cutoffTimeEmailReminderCntPropertyName}].value"
                                                                                        cssClass="smallNumber"/></td>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.cutoffTimeEmailReminderCntPropertyName}].typeCd"/>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.cutoffTimeEmailReminderCntPropertyName}].shortDesc"/>
                                                                            </tr>
                                                                        </table>
                                                                    </div>
                                                                </c:if>

                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr><td><br><br><hr style="padding-bottom: 0"></td></tr>
                    <tr>
                        <td class="subitem">

                            <div class="row" style="width:100%">

                                <div class="cell" style="width:70%">
                                    <table width="100%">

                                        <colgroup>
                                            <col width="50%"/>
                                            <col width="50%"/>
                                        </colgroup>

                                        <thead><tr><td colspan="2"> <div class="subHeader"><app:message code="admin.user.configuration.notif.label.services"/></div><br></td></tr></thead>

                                        <tbody>
                                        <tr>
                                            <td class="cell label"><div class="label"><app:message code="admin.user.configuration.notif.label.serviceTypeNotifications" /></div></td>
                                            <td class="cell label"><div class="label"><app:message code="admin.user.configuration.notif.label.allTicketNotifications" /></div></td>
                                        </tr>
                                        <tr>
                                            <td class="cell label" style="vertical-align: top;padding-left: 5px">
                                                <table>
                                                    <c:forEach var="notifName" items="${userNotification.serviceTypesNotifications}">
                                                        <tr>
                                                            <td class="cell label">

                                                                <c:if test="${notifName == userNotification.serviceReminder}">
                                                                    <form:checkbox id="serviceReminder"
                                                                                   tabindex="5"
                                                                                   onclick="$(document).find('input[linked=serviceReminder]').each(function(index) { $(this).attr('value',''); $(this).attr('disabled', $('#serviceReminder').attr('checked')?'':'disabled')});"
                                                                                   cssClass="checkbox"
                                                                                   path="emailNotificationMapping[${notifName}].value"
                                                                                   value="true"/>
                                                                </c:if>
                                                                <c:if test="${notifName != userNotification.serviceReminder}">
                                                                    <form:checkbox tabindex="5" cssClass="checkbox" path="emailNotificationMapping[${notifName}].value" value="true"/>
                                                                </c:if>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].shortDesc"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].typeCd"/>
                                                                <span class="label"><app:message code="admin.template.email.emailType.${notifName}" text="${notifName}"/></span>
                                                                <c:if test="${notifName == userNotification.serviceReminder}">
                                                                    <br>
                                                                    <div style="padding-left: 20px">
                                                                        <table>
                                                                            <tr>
                                                                                <td><div class="clear clearfix label"><app:message code="admin.user.configuration.notif.label.numberofWeeklyReminders"/></div><div style="padding-top:5px;" class="clear clearfix label">(<app:message code="admin.user.configuration.notif.text.beforeScheduledDate"/>)</div></td>
                                                                                <td>
                                                                                    <form:input
                                                                                            disabled="${userNotification.emailNotificationMapping[notifName].value !=true}"
                                                                                            linked="serviceReminder"
                                                                                            path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberWeeklyPropertyName}].value"
                                                                                            cssClass="smallNumber"/>
                                                                                </td>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberWeeklyPropertyName}].typeCd"/>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberWeeklyPropertyName}].shortDesc"/>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><div class="clear clearfix label"><app:message code="admin.user.configuration.notif.label.numberofDailyReminders"/></div><div style="padding-top:5px;" class="clear clearfix label">(<app:message code="admin.user.configuration.notif.text.beforeScheduledDate"/>)</div></td>
                                                                                <td>
                                                                                    <form:input
                                                                                            disabled="${userNotification.emailNotificationMapping[notifName].value !=true}"
                                                                                            linked="serviceReminder"
                                                                                            path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberDailyPropertyName}].value"
                                                                                            cssClass="smallNumber"/>
                                                                                </td>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberDailyPropertyName}].typeCd"/>
                                                                                <form:hidden  path="emailNotificationMapping[${notifName}].linkedMap[${userNotification.serviceReminderNumberDailyPropertyName}].shortDesc"/>
                                                                            </tr>
                                                                        </table>
                                                                    </div>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </td>
                                            <td class="cell label" style="vertical-align: top;padding-left: 5px">
                                                <table>
                                                    <c:forEach var="notifName" items="${userNotification.allServiceTicketNotifications}">
                                                        <tr>
                                                            <td class="cell label">
                                                                <form:checkbox tabindex="5" cssClass="checkbox" path="emailNotificationMapping[${notifName}].value" value="true"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].shortDesc"/>
                                                                <form:hidden  path="emailNotificationMapping[${notifName}].typeCd"/>
                                                                <span class="label"><app:message code="admin.template.email.emailType.${notifName}" text="${notifName}"/></span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr><td><hr></td></tr>
                    <tr><td align="center"><form:button><app:message code="admin.global.button.save"/></form:button></td></tr>
                </table>
            </div>
        </form:form>

    </div>

</div>


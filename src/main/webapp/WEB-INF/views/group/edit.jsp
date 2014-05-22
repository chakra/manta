<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/group/${group.groupId > 0?group.groupId: 0}'); "/>

<app:dateIncludes/>

<div class="canvas">

<div class="details std">

<form:form modelAttribute="group" action="" focus="groupName" method="POST">

<table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td style="text-align: left">
            <div class="row">
                <div class="cell">
                    <table>
                        <tbody>
                        <tr>
                            <td><div class="label"><form:label path="groupId"><app:message code="admin.group.label.groupId"/><span class="colon">:</span></form:label></div></td>
				             <td><div class="value"><c:out value="${group.groupId}" default="0"/></div><form:hidden path="groupId"/> </td>
				         </tr>
				         <tr>
				             <td><div class="label"><form:label path="groupName"><app:message code="admin.group.label.groupName"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
				             <td><div class="value"><form:input cssClass="inputLong" tabindex="1" path="groupName" focusable="true"/></div></td>
				         </tr>
				         <tr>
				             <td><div class="label"><form:label path="groupType"><app:message code="admin.group.label.groupType"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
				             <td><div class="value">
				             <c:if test="${group.groupId > 0}">
				             	<c:out value="${group.groupType}"/><form:hidden path="groupType"/>
			                 </c:if>
			                 <c:if test="${group.groupId == null || group.groupId == 0}">
				             	<form:select tabindex="2" path="groupType" focusable="true" cssClass="selectLong">
				                     <form:option value=""><app:message code="admin.global.select"/></form:option>
				                     <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.groupTypes}">
				                         <form:option value="${type.object2}"><app:message code="refcodes.GROUP_TYPE_CD.${type.object1}" text="${type.object2}"/></form:option>
				                     </c:forEach>
				                 </form:select>
			                 </c:if>
				             </div></td>
				         </tr>
				         <tr>
				             <td><div class="label"><form:label path="groupStatus"><app:message code="admin.group.label.groupStatus"/><span class="colon">:</span><span class="required">*</span></form:label></div></td>
				             <td><div class="value">
								<form:select tabindex="3" path="groupStatus" focusable="true" cssClass="selectLong">
				                     <form:option value=""><app:message code="admin.global.select"/></form:option>
				                     <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.groupStatuses}">
				                         <form:option value="${type.object2}"><app:message code="refcodes.GROUP_STATUS_CD.${type.object1}" text="${type.object2}"/></form:option>
				                     </c:forEach>
				                 </form:select>
				                </div></td>
				            </tr>
				           </tbody>
                      </table>
                  </div>
              </div>
          </td>
          <td style="vertical-align:top;text-align: right;white-space: nowrap;">
			<table width="100%">
			<tbody>
                <tr>
                    <td align="right">
                    <c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
                    <c:set var="administrator" value="<%=RefCodeNames.USER_TYPE_CD.ADMINISTRATOR%>" />
                    <c:set var="system_administrator" value="<%=RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR%>" />
                    <c:if test="${currentUserType==administrator || currentUserType==system_administrator}">
                        <br>
                        <table>
                            <tr>
                                <td>
                                    <app:box name="admin.group.label.groupAssociation" cssClass="white">
                                        <table cellpadding="0" cellspacing="0" border="0">
                                            <tbody>
                                                <tr>
                                                    <td nowrap>
                                                        <form:radiobutton tabindex="4" focusable="true" path="assocStoreId" cssClass="radio" value="0"/><span class="label"><app:message code="admin.global.text.none"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><br>
                                                    </td>
                                                </tr>
                                                <tr>
                                                <c:set var="currentUserType" value="${requestScope['appUser'].userTypeCd}" />
                                                    <td nowrap>
                                                        <span class="label">
                                                        <form:hidden path="storeIdCanAssoc"/>
                                                        <form:hidden path="assocStoreName"/>
                                                        	<form:radiobutton tabindex="4" focusable="true" path="assocStoreId" cssClass="radio" value="${group.storeIdCanAssoc}"/>${group.assocStoreName}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </app:box>
                                </td>
                            </tr>
                        </table>
                        </c:if>
                        <c:set var="store_administrator" value="<%=RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR%>" />
                        <c:if test="${currentUserType==store_administrator}">
                        	<form:hidden path="assocStoreId" value="${group.storeIdCanAssoc}"/>
                        </c:if>
                    </td>
                </tr>
                </tbody>
			</table>
 		  </td>
 	</tr>
 	<tr><td colspan="2" style="padding-bottom: 0"><hr/></td></tr>
 	<tr>
    <td td colspan="2" align="center" style="text-align: center;vertical-align:top;padding-bottom:0;">
        <table width="100%">
            <tr>
                <td style="padding-right: 30px;"><form:button tabindex="5" onclick="${updateAction}"><app:message code="admin.global.button.save"/> </form:button></td>
            </tr>
        </table>
    </td>
</tr>


</table>


</form:form>

</div>

</div>
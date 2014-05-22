<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.util.Utility" %>
<%@ page import="com.espendwise.manta.i18n.I18nResource" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>

<c:url var="home" value="/" />
<c:url var="logo" value="/display/logo.do"/>




<div>
    <div class="rightColumn">
        <div class="rightColumnIndent">

            <table width="100%">
                <tr>
                    <td>
                        <c:if test="${requestScope['appUser'].isSystemAdmin || requestScope['appUser'].isAdmin}">
                            <span style="padding-right:150px;white-space: nowrap;">
                                  
                                <a href="<app:url/>/home/services" onClick="window.name = 'manta'" target="orca"><app:message code="admin.global.button.services"/></a>
                                &nbsp;&nbsp;
                                <a href="<app:url/>/home/procurement" onClick="window.name = 'manta'" target="stjohn"><app:message code="admin.global.button.procurement"/></a>
                                
                            </span>
                        </c:if>
                    </td>
                    <td>
<c:set var="userTypeKey" value="${requestScope['appUser'].userTypeCd}"/>
<c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.userTypeCds}">
	<c:set var="value" value="${type.object2}"/>
	<c:if test="${userTypeKey == value}">
		<c:set var="userTypeKey" value="${type.object1}"/>
	</c:if>
</c:forEach>
                        <div class="content"  style="white-space: nowrap;">
                            <span class="label" style="white-space: nowrap;"><app:message code="admin.global.label.userType.${userTypeKey}"/></span> : <span class="label">${requestScope['appUser'].userName}</span>
                            <button onclick="location.href='<c:url value="/j_spring_security_logout"/>'" name="action">
                                <app:message code="admin.global.button.logout"/>
                            </button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="right">   <div class="content serverInfo" style="white-space: nowrap;">
                        <div><%= new java.util.Date().toString() %></div>
                        <div><% try { %> <strong><app:message code="admin.serverInfo.label.server"/></strong><span class="colon">:</span><%=java.net.InetAddress.getLocalHost()%> <% } catch (Exception e) { %> <strong><app:message code="admin.serverInfo.label.server"/></strong>
                            <app:message code="admin.global.label.na"/>  <% }  %></div>
                        <div><strong><app:message code="admin.serverInfo.label.branchVersion"/></strong><span class="colon">:</span><%=Utility.strNN(System.getProperty(Constants.MANTA_BRANCH_VERSION_PROPERTY), I18nResource.getUserResource().getMessage("admin.global.label.na"))%>
                        <strong>   <app:message code="admin.serverInfo.label.buildVersion"/></strong><span class="colon">:</span><%=Utility.strNN(System.getProperty(Constants.MANTA_BUILD_VERSION_PROPERTY), I18nResource.getUserResource().getMessage("admin.global.label.na"))%> </div>
                    </div></td>
                </tr>
            </table>


        </div>
    </div>
    <div class="leftColumn">
        <div class="leftColumnIndent">
            <div id="logo"><a href="${home}"><img  src="${logo}" border="0"/></a></div>
            <div id="logoName"><label><a href="${home}">${requestScope['storeContext'].storeName}</a></label></div>
        </div>
    </div>
    <hr>
</div>

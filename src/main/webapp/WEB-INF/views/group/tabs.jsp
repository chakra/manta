<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/group/*">
    <app:tabrow>
        <app:tabcell name="admin.group.tabs.detail"
                     path="/WEB-INF/views/group/edit.jsp"
                     href=""
                />
        <c:if test="${groupHeader.groupId > 0}">
            <app:tabcell name="admin.group.tabs.config"
                         path="/WEB-INF/views/group/configuration/configuration.jsp"
                         href="/configuration"/>
            <c:set var="user" value="<%=RefCodeNames.GROUP_TYPE_CD.USER%>" />
	        <c:if test="${groupHeader.groupType == user}">
	            <app:tabcell name="admin.group.tabs.reports"
	                         path="/WEB-INF/views/group/configuration/reports.jsp"
	                         href="/report"/>
	            <app:tabcell name="admin.group.tabs.functions"
	                         path="/WEB-INF/views/group/configuration/functions.jsp"
	                         href="/function"/>
	        </c:if>
        </c:if>        
    </app:tabrow>

</app:tabs>

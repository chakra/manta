<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/account/*">
    <app:tabrow>
        <app:tabcell name="admin.account.tabs.detail"
                     path="/WEB-INF/views/account/edit.jsp"
                     href=""
                />
        <c:if test="${accountHeader.id > 0 || not empty accountFiscalCalendar}"> 
            <app:tabcell
                    name="admin.account.tabs.fiscalCalendar"
                    path="/WEB-INF/views/account/fiscalCalendar/fiscalCalendar.jsp"
                    href="/fiscalCalendar/"
                    />
        </c:if>
        <c:if test="${accountHeader.id > 0 || not empty accountContentManagement}"> 
        	<app:tabcell name="admin.account.tabs.contentManagement"
                     path="/WEB-INF/views/account/contentManagement/contentManagement.jsp"
                     href="/contentManagement/"
        	/>
        </c:if>
        <c:if test="${accountHeader.id > 0 || not empty accountSiteHierarchy}">
            <app:tabcell
                    name="admin.account.tabs.locationHierarchy"
                    path="/WEB-INF/views/account/siteHierarchy"
                    href="/locationHierarchy/"
                    />
        </c:if>
        <c:if test="${accountHeader.id > 0 || not empty accountWorkflow}"> 
            <app:tabcell
                    name="admin.account.tabs.workflow"
                    path="/WEB-INF/views/account/workflow/"
                    href="/workflow/"
                    />
        </c:if>
        <c:if test="${accountHeader.id > 0}"> 
            <app:tabcell
                    name="admin.account.tabs.shoppingControl"
                    path="/WEB-INF/views/account/shoppingControl/"
                    href="/shoppingControl/"
                    />
        </c:if>
        <c:if test="${accountHeader.id > 0}"> 
            <app:tabcell
                    name="admin.account.tabs.properties"
                    path="/WEB-INF/views/account/properties/"
                    href="/properties/"
                    />
        </c:if>
        
    </app:tabrow>

</app:tabs>

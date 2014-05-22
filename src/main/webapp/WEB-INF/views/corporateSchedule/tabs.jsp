<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl" />

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/corporateSchedule/*">
    <app:tabrow>
        <c:if test="${corporateSchedule.isNew}">
	        <app:tabcell name="admin.corporateSchedule.tabs.detail"
	                     path="/WEB-INF/views/corporateSchedule/edit.jsp"
	                     href=""
	                     renderLink="false"
	                />
        </c:if>
        <c:if test="${!corporateSchedule.isNew}">
	        <app:tabcell name="admin.corporateSchedule.tabs.detail"
	                     path="/WEB-INF/views/corporateSchedule/edit.jsp"
	                     href=""
	                />
        </c:if>
        <c:if test="${!corporateSchedule.isNew}">
            <app:tabcell
                    name="admin.corporateSchedule.tabs.accounts"
                    path="/WEB-INF/views/corporateSchedule/configuration/accounts.jsp"
                    href="/account"
                    />
        </c:if>
        <c:if test="${!corporateSchedule.isNew}">
            <app:tabcell
                    name="admin.corporateSchedule.tabs.locations"
                    path="/WEB-INF/views/corporateSchedule/configuration/locations.jsp"
                    href="/location"
                    />
        </c:if>
</app:tabrow>

</app:tabs>



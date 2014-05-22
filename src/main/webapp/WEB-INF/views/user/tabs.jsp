<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/user/*">
    <app:tabrow>
        <app:tabcell name="admin.user.tabs.detail"
                     path="/WEB-INF/views/user/edit.jsp"
                     href=""
                />
        <c:if test="${!user.isNew}">
            <app:tabcell
                    name="admin.user.tabs.accounts"
                    path="/WEB-INF/views/user/configuration/accounts.jsp"
                    href="/account/view"
                    />
            <app:tabcell
                    name="admin.user.tabs.groups"
                    path="/WEB-INF/views/user/configuration/groups.jsp"
                    href="/group"
                    />
            <app:tabcell
                    name="admin.user.tabs.notifications"
                    path="/WEB-INF/views/user/configuration/notifications.jsp"
                    href="/notification"
                    />
            <app:tabcell
                    name="admin.user.tabs.locations"
                    path="/WEB-INF/views/user/configuration/locations.jsp"
                    href="/location/view"
                    />
        </c:if>
    </app:tabrow>

</app:tabs>

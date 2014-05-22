<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl" />

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/storeMessage/*">
    <app:tabrow>
        <app:tabcell name="admin.storeMessage.tabs.detail"
                     path="/WEB-INF/views/storeMessage/edit.jsp"
                     href=""
                />
        <c:if test="${!storeMessage.isNew}">
            <app:tabcell
                    name="admin.storeMessage.tabs.configuration"
                    path="/WEB-INF/views/storeMessage/configuration/configurations.jsp"
                    href="/configuration/view"
                    />
        </c:if>
</app:tabrow>

</app:tabs>



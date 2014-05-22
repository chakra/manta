<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUrl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUrl}/order/*">
    <app:tabrow>
        <app:tabcell name="admin.order.tabs.detail"
                     path="/WEB-INF/views/order/edit.jsp"
                     href=""
                    />
    </app:tabrow>

</app:tabs>

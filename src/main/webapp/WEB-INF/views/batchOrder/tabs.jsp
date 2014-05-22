<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/batchOrder/*">

    <app:tabrow>
        <app:tabcell name="admin.batchOrder.tabs.loader"
                     path="/WEB-INF/views/batchOrder/loader.jsp"
                     href=""
                />
    </app:tabrow>

</app:tabs>


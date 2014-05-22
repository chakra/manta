<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/uploadFile/*">

    <app:tabrow> 
        <app:tabcell name="admin.uploadFile.tabs.detail"
                     path="/WEB-INF/views/uploadFile/edit.jsp"
                     href=""
                />
    </app:tabrow>

</app:tabs>


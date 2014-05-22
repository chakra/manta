<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/costCenter/*">
    <app:tabrow>
        <app:tabcell name="admin.costCenter.tabs.identification"
                     path="/WEB-INF/views/costCenter/edit.jsp"
                     href=""
                />
        <c:if test="${costCenterHeader.costCenterId > 0}">
            <app:tabcell name="admin.costCenter.tabs.catalog"
                         path="/WEB-INF/views/costCenter/catalog/catalog.jsp"
                         href="/catalog"/>
        </c:if>
    </app:tabrow>

</app:tabs>

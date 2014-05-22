<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/distributor/*">

    <app:tabrow>
        <app:tabcell name="admin.distributor.tabs.detail"
                     path="/WEB-INF/views/distributor/edit.jsp"
                     href=""/>
    
		<c:if test="${distributorHeader.id > 0 || not empty distributorConfiguration}"> 
    		<app:tabcell name="admin.distributor.tabs.configuration"
					 path="/WEB-INF/views/distributor/configuration/"
					 href="/configuration/"/>
		</c:if>
    </app:tabrow>

</app:tabs>


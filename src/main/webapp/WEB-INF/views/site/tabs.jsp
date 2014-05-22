<%@ page import="java.util.StringTokenizer" %>
<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<app:url var="baseUtl"/>

<app:tabs requestedPath="${param['jsp']}" tabPath="${baseUtl}/location/*">
    <app:tabrow>
        <app:tabcell name="admin.site.tabs.identification"
                     path="/WEB-INF/views/site/edit.jsp"
                     href=""
                />
        <c:if test="${siteHeader.siteId > 0 || not empty siteBudget}">
            <app:tabcell name="admin.site.tabs.budgets"
                         path="/WEB-INF/views/site/budgets/budgets.jsp"
                         href="/budgets"/>
        </c:if>
        <c:if test="${siteHeader.siteId > 0}">
            <app:tabcell name="admin.site.tabs.users"
                         path="/WEB-INF/views/site/users/users.jsp"
                         href="/users"/>
            <app:tabcell name="admin.site.tabs.catalog"
                         path="/WEB-INF/views/site/catalog/catalog.jsp"
                         href="/catalog"/>
        </c:if>
        <c:if test="${siteHeader.siteId > 0 || siteSiteHierarchy}">
            <app:tabcell name="admin.site.tabs.siteHierarchy"
                         path="/WEB-INF/views/site/siteHierarchy/siteHierarchy.jsp"
                         href="/locationHierarchy"/>
        </c:if>
        <c:if test="${siteHeader.siteId > 0}">
            <app:tabcell name="admin.site.tabs.siteWorkflow"
                         path="/WEB-INF/views/site/workflow/workflow.jsp"
                         href="/workflow"/>
        </c:if>
        <c:if test="${siteHeader.siteId > 0}">
            <app:tabcell name="admin.site.tabs.siteOrderGuides"
                         path="/WEB-INF/views/site/orderGuides/"
                         href="/orderGuides/"
                         />
        </c:if>
    </app:tabrow>

</app:tabs>

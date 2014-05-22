<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="canvas">


    <table>

        <tr class="row">
            <td><div class="cell">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.site.label.accountId"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${siteHeader.accountId}" default="0"/></div></div></td>
            <td><div class="cell cell-space">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.site.label.accountName"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${siteHeader.accountName}"/></div></div></td>
        </tr>

        <tr class="row">
            <td  style="padding-top:5px"><div class="cell">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.site.label.siteId"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${siteHeader.siteId}" default="0"/></div></div</td>
            <td><div class="cell cell-space">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.site.label.siteName"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${siteHeader.siteName}"/></div></div></td>
        </tr>

    </table>
</div>
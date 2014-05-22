<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="canvas">


    <table>

        <tr class="row">
            <td  style="padding-top:5px"><div class="cell">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.group.label.groupId"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${groupHeader.groupId}" default="0"/></div></div</td>
            <td><div class="cell cell-space">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.group.label.groupName"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${groupHeader.groupName}"/></div></div></td>
        </tr>

    </table>
</div>
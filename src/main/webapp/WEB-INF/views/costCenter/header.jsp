<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="canvas">


    <table>

        <tr class="row">
            <td  style="padding-top:5px"><div class="cell">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.costCenter.label.costCenterId"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${costCenterHeader.costCenterId}" default="0"/></div></div</td>
            <td><div class="cell cell-space">&nbsp</div></td>
            <td><div class="cell label"><app:message code="admin.costCenter.label.costCenterName"/><span class="colon">:</span></div></td>
            <td><div class="cell"><div class="labelValue"><c:out value="${costCenterHeader.costCenterName}"/></div></div></td>
        </tr>

    </table>
</div>
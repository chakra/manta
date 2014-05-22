<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<app:url var="baseUrl"/>
<app:url var="actionUrl" value="${baseUrl}/order/${orderPrintTempPo.orderId}/printTempPo"/>

<c:set var="doReturnSelected" value="doLayerReturnSelected( window.parent, 'orderPrintTempPoId', 'printTempPoLayer');"/>

<script>
    function changeActionAndSubmit() {
        var selectedValue = $('#selectedDistributor :selected').val();
        if (selectedValue.length > 0) {
            var form = $('form:first').attr('action', '${baseUrl}/order/${orderPrintTempPo.orderId}/printTempPo');
            $('form:first').submit();
        }
        return false;
    }
</script> 

<div class="search locate">

    <form:form modelAttribute="orderPrintTempPo" id="orderPrintTempPoId" method="POST" focus="filterValue" action="${actionUrl}" >

        <div class="filter" >
            <table>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.order.label.orderId" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <c:out value="${orderPrintTempPo.orderId}"/>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.order.label.assignedDistributor" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value ">
                        <form:select id="selectedDistributor" path="selectedDistributorId" focusable="true" onchange="changeActionAndSubmit()">
                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                            <c:forEach var="source" items="${orderPrintTempPo.distributorsInfo}">
                                <form:option value="${source.busEntityId}">
                                    <c:choose>
                                        <c:when test="${not empty source.shortDesc}">
                                            ${source.shortDesc}
                                        </c:when>
                                        <c:otherwise>
                                            - ${source.busEntityId} -
                                        </c:otherwise>
                                    </c:choose>
                                </form:option>
                            </c:forEach>
                        </form:select>
                    </td>
                    <td colspan="2" class="cell">&nbsp;</td>
                </tr>
            </table>
        </div>

    </form:form>

</div>


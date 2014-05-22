<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUrl}/manufacturer/${manufacturer.manufacturerId > 0 ? manufacturer.manufacturerId: 0}');$('form:first').submit(); return false; "/>

<app:message var="dateFormat" code="format.prompt.dateFormat" />

<app:dateIncludes/> 

<div class="canvas">

<div class="details">

<form:form modelAttribute="manufacturer" id="manufacturer" action="" method="POST">
<table width="100%">

<tr>
    <td>
        <table style="padding: 0;" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: left">
                    <div class="row">
                        <div class="cell">
                            <table>
                                <tbody>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="manufacturerId"><app:message code="admin.manufacturer.label.manufacturerId"/><span class="colon">:</span></form:label>
                                        </div>
                                    </td>
                                    <td><div class="labelValue"><c:out value="${manufacturer.manufacturerId}" default="0"/></div><form:hidden path="manufacturerId" value="${manufacturer.manufacturerId}"/></td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="manufacturerName"><app:message code="admin.manufacturer.label.manufacturerName"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                        </div>
                                    </td>
                                    <td>
                                        <form:input tabindex="2" path="manufacturerName" size="35"/>
                                    </td>
                                </tr>

                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="manufacturerStatus"><app:message code="admin.manufacturer.label.status"/><span class="colon">:</span></form:label><span class="reqind">*</span>
                                            </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="3" style="width:200px" path="manufacturerStatus">
                                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}">
                                                <form:option value="${type.object2}"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${type.object1}" text="${type.object2}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </td>
                                </tr>


                                <tr>
                                    <td>
                                        <div class="label">
                                            <form:label path="manufacturerMSDSPlugIn"><app:message code="admin.manufacturer.label.MSDSPlugIn"/><span class="colon">:</span></form:label>

                                        </div>
                                    </td>
                                    <td>
                                        <form:select tabindex="4" style="width:200px" path="manufacturerMSDSPlugIn">
                                            <form:option value=""><app:message code="admin.global.select"/></form:option>
                                            <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.MSDSPlugIns}">
                                                <form:option value="${type.object2}"><app:message code="refcodes.MSDS_PLUG_ID_CD.${type.object1}" text="${type.object2}"/></form:option>
                                            </c:forEach>
                                        </form:select>     
                                    </td>

                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </td>
            </tr>

        </table>
    </td>
</tr>

<tr><td><hr/></td></tr>

          <tr align=center>
                <td style="vertical-align:top;text-align: center;white-space: nowrap;">

                    <table width="100%">
                        <tbody>
                        <tr>
                            <td style="text-align: center">
                                <div class="actions">
                                    <form:button tabindex="1000" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                </td>

            </tr>

</table>
</form:form>

</div>
</div>


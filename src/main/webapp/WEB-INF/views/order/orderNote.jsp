<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>

<%
  String emptyNoteText = AppI18nUtil.getMessage("validation.web.error.order.noItemNote");
%>

<script>
function showError() {
        if ('undefined' == $(".webError") || $(".webError").text().length == 0 ) {
            $(".webErrors").text(
            "<%=emptyNoteText%>"
            );
        }
} 
</script>

<app:url var="baseUrl"/>


<c:set var="doReturnSelected" value="doLayerReturnSelected(window.parent, 'orderItemNoteField', 'orderItemNoteLayer');"/>

<div class="canvas">

    <form:form modelAttribute="orderItemNoteFilter" id="orderItemNoteId" method="POST" focus="filterValue" action="" >

    <c:set var="typeOrderNote" value="orderNote" />
    <c:set var="typeOrderItemNote" value="orderItemNote" />

    <c:set var="inViewMode" value="false" />
    <c:if test="${orderItemNoteFilter.view}">
        <c:set var="inViewMode" value="true" />
    </c:if>

    <div class="details">
        <table>
            <c:choose>
                <c:when test="${inViewMode}">
                    <c:if test="${typeOrderNote == orderItemNoteFilter.type && fn:length(orderItemNoteFilter.orderNotes) > 0}">
                        <tr>
                            <td colspan="2" class="primaryHeader">
                                <app:message code="admin.order.label.orderNotes" />
                            </td>
                            <tr><td colspan="2">&nbsp;</td></tr>
                        </tr>
                    </c:if>

                    <c:if test="${fn:length(orderItemNoteFilter.orderNotes) > 0}">
                        <c:forEach var="orderNote" varStatus="i" items="${orderItemNoteFilter.orderNotes}" >
                            <tr>
                                <td>
                                    <span class="cell label">
                                        <label><app:message code="admin.order.label.note" /><span class="colon">:</span></label>
                                    </span>

                                    &nbsp;
                                    <span class="labelValue">
                                        <c:out value="${i.index + 1}" />
                                    </span>
                                    <br>

                                    <span class="cell label">
                                        <label><app:message code="admin.order.label.addedBy" /><span class="colon">:</span></label>
                                    </span>

                                    &nbsp;
                                    <span class="labelValue">
                                        <c:out value="${orderNote.addBy}" />
                                    </span>
                                    <br>
                                    
                                    <span class="cell label">
                                        <label><app:message code="admin.order.label.addedDate" /><span class="colon">:</span></label>
                                    </span>

                                    &nbsp;
                                    <span class="labelValue">
                                        <app:formatDate value="${orderNote.addDate}"/>
                                    </span>
                                    <br>
                                </td>
                                <td style="padding-left:40px">
                                    <c:set var="noteShortDesc" value="${orderNote.shortDesc}" />
                                    <c:set var="noteValue" value="${orderNote.value}" />
                                    <c:if test="${(not empty noteShortDesc) && (not empty noteValue) && (noteShortDesc != noteValue)}">
                                        <label><c:out value="${orderNote.shortDesc}" /></label>
                                    </c:if>
                                    <br>

                                    <c:if test="${fn:contains(orderNote.value, 'This email') || fn:contains(orderNote.value, 'This Email')}">
                                        <pre>
                                    </c:if>
                                    <c:set var="noteValue" value="${orderNote.value}" />
                                    <%
                                        String n = (String) pageContext.getAttribute("noteValue");
                                        String convertedN = new String(n.getBytes(), "UTF-8");
                                    %>
                                    <span class="labelValue">
                                        <%=convertedN%>
                                    </span>
                                    <c:if test="${fn:contains(orderNote.value, 'This email') || fn:contains(orderNote.value, 'This Email')}">
                                        </pre>
                                    </c:if>
                                </td>
                            </tr>
                            <tr><td colspan="2">&nbsp;</td></tr>
                        </c:forEach>
                    </c:if>

                    <c:if test="${fn:length(orderItemNoteFilter.orderItemViews) > 0}">
                        <tr>
                            <td colspan="2" class="primaryHeader"><app:message code="admin.order.label.orderItemNotes" /></td>
                        </tr>
                        <tr><td colspan="2">&nbsp;</td></tr>

                        <c:forEach var="orderItemView" varStatus="i" items="${orderItemNoteFilter.orderItemViews}" >

                            <c:if test="${fn:length(orderItemView.orderItemNotes) > 0}">
                                <c:forEach var="orderItemNote" varStatus="j" items="${orderItemView.orderItemNotes}" >
                                    <tr>
                                        <td>
                                            <span class="cell label">
                                                <label><app:message code="admin.order.label.noteForItem" /><span class="colon">:</span></label>
                                            </span>
                                            &nbsp;
                                            <span class="labelValue">
                                                <c:out value="${orderItemView.orderItem.itemSkuNum}" />
                                            </span>
                                            <br>

                                            <span class="cell label">
                                                <label><app:message code="admin.order.label.addedBy" /><span class="colon">:</span></label>
                                            </span>
                                            &nbsp;
                                            <span class="labelValue">
                                                <c:out value="${orderItemNote.addBy}" />
                                            </span>
                                            <br>
                                            
                                            <span class="cell label">
                                                <label><app:message code="admin.order.label.addedDate" /><span class="colon">:</span></label>
                                            </span>
                                            &nbsp;
                                            <span class="labelValue">
                                                <app:formatDate value="${orderItemNote.addDate}"/>
                                            </span>
                                            <br>
                                        </td>
                                        <td style="padding-left:40px">
                                            <c:set var="itemNoteValue" value="${orderItemNote.value}" />
                                            <%
                                                String note = (String) pageContext.getAttribute("itemNoteValue");
                                                String convertedNote = new String(note.getBytes(), "UTF-8");
                                            %>
                                            <span class="labelValue">
                                                <%=convertedNote%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr><td colspan="2">&nbsp;</td></tr>
                                </c:forEach>
                            </c:if>

                        </c:forEach>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td class="primaryHeader">
                            <c:if test="${typeOrderNote == orderItemNoteFilter.type}">
                                <app:message code="admin.order.label.orderNote" />
                            </c:if>
                            <c:if test="${typeOrderItemNote == orderItemNoteFilter.type}">
                                <app:message code="admin.order.label.orderItemNote" />
                            </c:if>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr><td colspan="2">&nbsp;</td></tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td class="cell value">
                            <form:textarea tabindex="1" path="orderItemNoteField" cols="80" rows="10"/>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            
            <c:if test="${!inViewMode}">
                <tr>
                    <td>&nbsp;</td>
                    <td class="cell value">
                        <form:button cssClass="button" tabindex="2"  onclick="if ($('#orderItemNoteField').attr('value')=='') {showError(); return false;} else {return ${doReturnSelected}}">
                            <app:message code="admin.global.button.save"/>
                        </form:button>
                    </td>
                </tr>
            </c:if>
        </table>
    </div>


    </form:form>

</div>


<%@ page import="java.util.Date" %>
<%@ page import="com.espendwise.manta.auth.AppUser" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ page import="com.espendwise.manta.util.RefCodeNames" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<app:url var="baseUrl" value="/account/${accountHeader.id}/shoppingControl" />
<app:url var="historyUrl" value="/history/filter"/>

<c:set var="searchUrl" value="${baseUrl}/filter"/>
<c:set var="sortUrl" value="${baseUrl}/filter/sortby"/>
<c:set var="updateUrl" value="${baseUrl}/update"/>
<c:set var="updateAction" value="$('form:#accountShoppingControls').submit(); return false;"/>

<div class="canvas">
	<div class="details">
        <form:form modelAttribute="accountShoppingControlFilter" method="GET" action="${searchUrl}"  focus="search">
        	<div class="subHeader">
        		<app:message code="admin.account.tabs.shoppingControl"/>
        	</div>
			<br>
			<hr>
            <table>
            	<tbody>
                    <tr>
                        <td>
							<div id="BOX_item">
					             <table class="locate">
					                <tr>
					                	<td colspan="2">
											<jsp:include  page="../../locateItem.jsp">
											     <jsp:param name="baseViewName" value="accountShoppingControlFilter" />
											     <jsp:param name="baseViewProperty" value="setItems" />
											     <jsp:param name="pageUrl" value="${baseUrl}" />
											     <jsp:param name="filteredItemCommaNames" value="${accountShoppingControlFilter.filteredItemCommaSkus}" />
								    		</jsp:include>
							    		</td>
							    	</tr>
							    	<tr>
					                    <td class="cell label first" width="155px"></td>
										<td class="cell label first">
					                       <form:checkbox tabindex="2" cssClass="checkbox" path="showUncontrolledItems"/><app:message code="admin.account.shoppingControl.label.showUncontrolled" />
					                    </td>
					                </tr>
									<tr>
					                    <td class="cell label first" width="155px"></td>
										<td class="cell">
											<form:button  id="search" tabindex="3" onclick="$('form:first').submit(); return false;">
												<app:message code="admin.global.button.search"/>
											</form:button>
										</td>
									</tr>
					            </table>
							</div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form:form>
	</div>
</div>
<div class="search">
	<c:if test="${accountShoppingControlFilterResult.shoppingControls != null}">
		<c:choose>
			<c:when test="${fn:length(accountShoppingControlFilterResult.shoppingControls) <= 0}">
				<c:set var="saveButtonDisabled" value="disabled=true"/>
			</c:when>
			<c:otherwise>
				<c:set var="saveButtonDisabled" value=""/>
			</c:otherwise>
		</c:choose>


		<form:form id="accountShoppingControls" modelAttribute="accountShoppingControlFilterResult" method="POST" action="${updateUrl}">
			<hr/>
            <table width="100%">
                <tr>
                	<td align="left">
                    	<div class="resultCount label">
                    		<label>
                    			<app:message code="admin.global.filter.label.resultCount" />
                    			<span class="colon">
                    				:
                    			</span>
                    		</label>
                            <label class="value">
								${fn:length(accountShoppingControlFilterResult.shoppingControls)}
							</label>
						</div>
                    </td>
                </tr>
            </table>
            <table width="100%">
                <tr>
                    <td colspan="10" align="right">
                    	<button onclick="${updateAction}" type="submit" value="Submit" ${saveButtonDisabled}>
                        	<app:message code="admin.global.button.save" />
                        </button>
                    </td>
                </tr>
	            <tr>
	               	<td colspan="10">
	               		<br>
	                </td>
	            </tr>
            </table>
            <table class="searchResult" width="100%">
                <colgroup>
                    <col width="5%"/>
                    <col width="35%"/>
                    <col width="5%"/>
                    <col width="10%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="5%"/>
                    <col width="20%"/>
                </colgroup>
                <thead class="header">
	                <tr class="row">
	                    <th class="cell cell-number">
	                    	<a class="sort" href="${sortUrl}/itemId">
	                    		<app:message code="admin.global.filter.label.itemId" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<a class="sort" href="${sortUrl}/itemShortDesc">
	                    		<app:message code="admin.global.filter.label.itemName" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<a class="sort" href="${sortUrl}/itemSku">
	                    		<app:message code="admin.global.filter.label.itemSku" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<a class="sort" href="${sortUrl}/itemSize">
	                    		<app:message code="admin.global.filter.label.itemSize" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<a class="sort" href="${sortUrl}/itemUom">
	                    		<app:message code="admin.global.filter.label.itemUom" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<a class="sort" href="${sortUrl}/itemPack">
	                    		<app:message code="admin.global.filter.label.itemPack" />
	                    	</a>
	                    </th>
	                    <th class="cell cell-text">
	                    	<app:message code="admin.account.shoppingcontrol.label.maxOrderQuantity" />
	                    </th>
	                    <th class="cell cell-text">
	                    	<app:message code="admin.account.shoppingcontrol.label.restrictionDays" />
	                    </th>
	                    <th class="cell cell-text">
	                    	<app:message code="admin.account.shoppingcontrol.label.action" />
	                    </th>
	                    <th class="cell cell-text">
	                    	<%
	                    		AppUser appUser = Auth.getAppUser();
	                    		boolean hasHistoryAccess = appUser.isAdmin() || appUser.isSystemAdmin() ||
		                    		Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY);
	                        	if (hasHistoryAccess) {
	                    	%>
	                    			<app:message code="admin.account.shoppingcontrol.label.history" />
	                    	<%
	                    		}
	                        	else {
	                    	%>
	                    			&nbsp;
	                    	<%
	                    		}
	                    	%>
	                    </th>
	                </tr>
                </thead>
                <tbody class="body">
                <c:forEach var="shoppingControl" varStatus="i" items="${accountShoppingControlFilterResult.shoppingControls}" >
					<c:choose>
						<c:when test="${shoppingControl.shoppingControlId == null || shoppingControl.shoppingControlId <= 0}">
							<c:set var="rowClass" value="row black"/>
							<c:set var="showHistoryLink" value="false"/>
						</c:when>
						<c:otherwise>
							<c:set var="rowClass" value="row"/>
							<c:set var="showHistoryLink" value="true"/>
						</c:otherwise>
					</c:choose>
                    <tr class="${rowClass}">
                        <td class="cell cell-number">
                        	<form:hidden path="shoppingControls[${i.index}].shoppingControlId"/>
                            <form:hidden path="shoppingControls[${i.index}].shoppingControlLocationId"/>
                            <form:hidden path="shoppingControls[${i.index}].shoppingControlAccountId"/>
                            <form:hidden path="shoppingControls[${i.index}].itemId"/>
                            <form:hidden path="shoppingControls[${i.index}].itemShortDesc"/>
                            <form:hidden path="shoppingControls[${i.index}].itemSku"/>
                        	<form:hidden path="shoppingControls[${i.index}].shoppingControlOriginalMaxOrderQty"/>
                            <form:hidden path="shoppingControls[${i.index}].shoppingControlOriginalRestrictionDays"/>
                            <form:hidden path="shoppingControls[${i.index}].shoppingControlOriginalAction"/>
                            <c:out value="${shoppingControl.itemId}"/>
                        </td>
                        <td class="cell cell-text">
                        	<c:out value="${shoppingControl.itemShortDesc}"/>
                        </td>
                        <td class="cell cell-text">
                           	<c:out value="${shoppingControl.itemSku}"/>
                        </td>
                        <td class="cell cell-text">
                           	<c:out value="${shoppingControl.itemSize}"/>
                        </td>
                        <td class="cell cell-text">
                           	<c:out value="${shoppingControl.itemUom}"/>
                        </td>
                        <td class="cell cell-text">
                           	<c:out value="${shoppingControl.itemPack}"/>
                        </td>
                        <td class="cell cell-element">
                           	<form:input cssClass="std name" size="10" path="shoppingControls[${i.index}].shoppingControlMaxOrderQty"/>
                        </td>
                        <td class="cell cell-text">
                           	<form:input cssClass="std name" size="10" path="shoppingControls[${i.index}].shoppingControlRestrictionDays"/>
                        </td>
                        <td class="cell cell-text">
                        	<form:select cssClass="std" path="shoppingControls[${i.index}].shoppingControlAction">
                        		<form:option value="">
                            		<app:message code="admin.global.select"/>
                            	</form:option>
                        		<app:i18nRefCodes var="code" items="${accountShoppingControlFilterResult.shoppingControlActionChoices}" i18nprefix="">
                            		<form:option value="${code.object2}"><c:out value="${code.object1}"/></form:option>
                        		</app:i18nRefCodes>
                        	</form:select>
                        </td>
                        <td class="cell cell-text">
	                    	<%
                        		if (hasHistoryAccess) {
	                    	%>
                        			<c:choose>
										<c:when test="${showHistoryLink == 'true'}">
											<a class="sort" href="${historyUrl}?objectId=${shoppingControl.shoppingControlId}&objectType=<%=RefCodeNames.HISTORY_OBJECT_TYPE_CD.SHOPPING_CONTROL %>">
												<app:message code="admin.account.shoppingcontrol.label.showHistory"/>
											</a>
										</c:when>
									</c:choose>
	                    	<%
	                    		}
	                        	else {
	                    	%>
	                    			&nbsp;
	                    	<%
	                    		}
	                    	%>
                        </td>
                    </tr>
                </c:forEach>
	            <tr>
	               	<td colspan="10">
	               		<br>
	               		<br>
	               	</td>
	            </tr>
	            <tr>
	            	<td colspan="10" align="right">
                    	<button onclick="${updateAction}" type="submit" value="Submit" ${saveButtonDisabled}>
                        	<app:message code="admin.global.button.save" />
                        </button>
	                </td>
	            </tr>
            	</tbody>
        	</table>
    	</form:form>
    </c:if>
</div>
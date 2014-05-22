<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.RefCodeNamesKeys" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%@ page import="com.espendwise.manta.util.*"%>
<%@ page import="com.espendwise.manta.web.forms.*"%>
<%@ page import="com.espendwise.manta.model.data.UploadSkuData"%>
<%@ page import="com.espendwise.manta.model.view.UploadSkuView"%>

<app:url var="baseUrl"/>
<c:set var="sortUrl" value="${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId}/filter/sortby"/>

<!-- if upload step 1 -->
<c:set var="uploadAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/upload');$('form:first').submit(); return false; "/>
<c:set var="getSubtableAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/getSubtable');$('form:first').submit(); return false;"/>
<!-- end if -->

<!-- if upload step 2 -->
<c:set var="selectAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/select');$('form:first').submit(); return false; "/>
<c:set var="selectUpdateAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/selectUpdate');$('form:first').submit(); return false; "/>
<c:set var="reloadAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/reload');$('form:first').submit(); return false; "/>


<c:set var="saveAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/save');$('form:first').submit(); return false; "/>
<c:set var="matchAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/match');$('form:first').submit(); return false;"/>
<c:set var="showMatchedAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/showMatched');$('form:first').submit(); return false; "/>
<c:set var="assignSkusAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/assignSkus');$('form:first').submit(); return false; "/>
<c:set var="removeAssignmentAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/removeAssignment');$('form:first').submit(); return false; "/>
<c:set var="createSkusAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/createSkus');$('form:first').submit(); return false; "/>
<c:set var="updateSkusAction" value="$('form:first').attr('action','${baseUrl}/uploadFile/${uploadFile.uploadData.uploadId > 0?uploadFile.uploadData.uploadId: 0}/updateSkus');$('form:first').submit(); return false; "/>
<!-- end if -->


<div class="search">

<div class="filter" >

<form:form id="uploadFile" modelAttribute="uploadFile" action="" method="POST"  enctype="multipart/form-data">
<table >
<tr><td>
<!-- if upload step 1 -->
<c:if test="${uploadFile.step == 'createNew'}">
    <span class="label"><form:label path="uploadXlsFile">
    <app:message code="admin.uploadFile.label.selectFile"/>:</form:label></span>
    <form:input path="uploadXlsFile" type="file"/>  <form:button id="uploadXlsFile"  cssClass="button" tabindex="9"  onclick="${uploadAction}"><app:message code="admin.global.button.upload"/></form:button>
    <br/>
    <br/>
  <c:if test="${uploadFile.uploadData.fileName != null}">
    <c:out value="${uploadFile.uploadData.fileName}"/>
    <form:button id="getSubtable"  cssClass="button" tabindex="9"  onclick="${getSubtableAction}"><app:message code="admin.global.button.getSubtable"/></form:button>
    <br/>
  </c:if>
</c:if>
<!-- end if -->
<br/>
<!-- if upload step 2 -->
<c:if test="${uploadFile.step == 'edit'}">
	<br>
    <form:button tabindex="12" onclick="${saveAction} return false;"><app:message code="admin.global.button.save"/></form:button>
    <form:button tabindex="12" onclick="${matchAction} return false;"><app:message code="admin.global.button.match"/></form:button>
    <form:button tabindex="12" onclick="${showMatchedAction} return false;"><app:message code="admin.global.button.showMatched"/></form:button>
    <form:button tabindex="12" onclick="${assignSkusAction} return false;"><app:message code="admin.global.button.assignSkus"/></form:button>
    <form:button tabindex="12" onclick="${removeAssignmentAction} return false;"><app:message code="admin.global.button.removeAssignment"/></form:button>
    <form:button tabindex="12" onclick="${createSkusAction} return false;"><app:message code="admin.global.button.createSkus"/></form:button>
    <form:button tabindex="12" onclick="${updateSkusAction} return false;"><app:message code="admin.global.button.updateSkus"/></form:button>

	<br>

    <table>
        <tbody>
        <tr>
            <td class="cell"><div class="label"><form:label path="uploadData.uploadStatusCd"><app:message code="admin.uploadFile.label.tableStatus"/>:</form:label></div></td>
            <td class="cell">
                <form:select tabindex="11" style="width:158px" path="uploadData.uploadStatusCd">
                    <c:forEach var="code" items="${requestScope['appResource'].dbConstantsResource.uploadStatusesCds}">
                        <form:option value="${code.object2}"><app:message code="refcodes.UPLOAD_STATUS_CD.${code.object1}" text="${code.object2}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>
        </tr>
	</table>
	<br>
	<hr/>
	<table>
        <tr>
           <td>
            <table class="user-filter">
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.category" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="1" focusable="true" id="category" path="category" cssClass="filterValue"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.skuName" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="2" focusable="true" id="filterValue" path="skuName" cssClass="filterValue"/>
                    </td>
                </tr>
                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.manufacturer" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="5" focusable="true" id="manufName" path="manufName" cssClass="filterValue"/>
                    </td>
                </tr>

                <tr>
                    <td class="cell label first">
                        <label><app:message code="admin.global.filter.label.distributor" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value" colspan="2">
                        <form:input tabindex="5" focusable="true" id="distName" path="distName" cssClass="filterValue"/>
                    </td>
                </tr>

				<tr>
					<td class="cell label first">
                        <label><app:message code="admin.global.filter.label.skuNumber" /><span class="colon">:</span></label>
                    </td>
                    <td class="cell value">
                        <form:input tabindex="5" focusable="true" id="skuNumber" path="skuNumber" cssClass="filterValue"/>
                    </td>
					<td class="cell">
					<form:radiobutton tabindex="11" path="skuNumberTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.STORE%>"/><span class="label"><app:message code="admin.global.filter.label.store"/></span>&nbsp;<form:radiobutton tabindex="12" path="skuNumberTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.DISTRIBUTOR%>"/><span class="label"><app:message code="admin.global.filter.label.distributor"/></span>&nbsp;<form:radiobutton tabindex="13" path="skuNumberTypeFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.Constants.SKU_FILTER_TYPE.MANUFACTURER%>"/><span class="label"><app:message code="admin.global.filter.label.manufacturer"/></span>
					</td>
				</tr>

				<tr>
                    <td class="cell label first" ></td>
                    <td class="cell  label first" colspan="2">
						<form:radiobutton tabindex="11" path="matchedFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.ALL%>"/><span class="label"><app:message code="admin.uploadFile.label.all"/></span>&nbsp;<form:radiobutton tabindex="12" path="matchedFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.MATCHED%>"/><span class="label"><app:message code="admin.uploadFile.label.matched"/></span>&nbsp;<form:radiobutton tabindex="13" path="matchedFilterType" cssClass="radio" focusable="true" value="<%=com.espendwise.manta.util.RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.UNMATCHED%>"/><span class="label"><app:message code="admin.uploadFile.label.unMatched"/></span>
                    </td>
                </tr>

				<tr>
                    <td class="cell label first"></td>
                    <td class="cell" align="left" colspan="2">
                        <form:button id="search"  cssClass="button" tabindex="9"  onclick="${selectAction} return false;"><app:message code="admin.global.button.select"/></form:button>
                        <br><br><br>
                    </td>
                </tr>
           </table>
           </td>
		   <td valign="top" style="vertical-align:top">
		   <table>
			<tr>
				<td class="cell label first">
				<label><app:message code="admin.global.filter.label.columnToUpdate" /><span class="colon">:</span></label><br><br>
                <form:select tabindex="11" style="width:158px" path="columnToUpdate">
                    <c:forEach var="code" items="${uploadFile.itemProperties}">
                        <form:option value="${code.object1}"><c:out value="${code.object2}"/></form:option>
                    </c:forEach>
                </form:select>
			    </td>
			</tr>
			<tr>
				<td>
				<form:input tabindex="5" focusable="true" id="columnToUpdateFilterValue" path="columnToUpdateFilterValue" cssClass="filterValue"/>
				</td>
			</tr>
			<tr>
				<td>
				<form:button id="search"  cssClass="button" tabindex="9"  onclick="${selectUpdateAction} return false;"><app:message code="admin.global.button.selectUpdate"/></form:button>
				</td>
			</tr>
			<tr>
				<td>
				<br>
				<hr/>
				</td>
			</tr>
			<tr>
				<td>
				<form:button id="save"  cssClass="button" tabindex="9"  onclick="${saveAction} return false;"><app:message code="admin.global.button.save"/></form:button>
				<form:button id="reload"  cssClass="button" tabindex="9"  onclick="${reloadAction} return false;"><app:message code="admin.global.button.reload"/></form:button>

				</td>
			</tr>

		   </table>
		   </td>
        </tr>

        </table>

        <!-- end if -->
        </c:if>
  </td>
</tr>
</table>

<!-- item list -->

    <c:if test="${uploadFile.uploadSkuViewList != null}">
        <hr/>
        <div class="resultCount label">
            <label><app:message code="admin.global.filter.label.resultCount" /><span class="colon">:</span></label>
            <label class="value"></label>${fn:length(uploadFile.result)}
        </div>
		<br>

        <table class="searchResult" width="100%">

            <thead class="header">
            <tr class="row">
                <th class="cell cell-text cell-element" nowrap>
                  <a href="javascript:checkAll('uploadFile', 'uploadSkuViewList.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                  <a href="javascript:checkAll('uploadFile', 'uploadSkuViewList.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/rowNum"><app:message code="admin.global.filter.label.rowNum" /></a></th>
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/matchedSkuNum"><app:message code="admin.global.filter.label.matchedSkuNum" /></a></th>
                <c:forEach var="code" items="${uploadFile.itemProperties}">
                    <c:set var="colName" value="${code.object2}"/>
                    <th class="cell cell-text">
                        <a class="sort" href="${sortUrl}/${colName}"><c:out value="${colName}"/></a>
                    </th>
                </c:forEach>
            </tr>

            </thead>

            <tbody class="body">
            <% ItemLoaderForm uploadFile = (ItemLoaderForm) pageContext.getAttribute("uploadFile"); %>
            <c:forEach var="tableItem" varStatus="i" items="${uploadFile.result}" >
                <tr class="row">
                <td class="cell cell-text cell-element"><form:checkbox cssClass="checkbox" path="uploadSkuViewList.selectableObjects[${i.index}].selected"/></td>
                <td class="cell cell-number"><c:out value="${tableItem.value.uploadSkuData.rowNum}"/></td>
                <td class="cell cell-number"></td>
                <c:forEach var="code" items="${uploadFile.itemProperties}">
                    <c:set var="colName" value="${code.object2}"/>
                    <td class="cell cell-text">
                       <%
						SelectableObjects.SelectableObject<UploadSkuView> tableItem =
						    (SelectableObjects.SelectableObject<UploadSkuView>)pageContext.getAttribute("tableItem");
						%>
                        <%=uploadFile.getSkuProperty(tableItem, (String)pageContext.getAttribute("colName"))%>
                    </td>
                </c:forEach>
                </tr>
                <!-- item to match -->

                <c:if test="${uploadFile.itemsToMatch != null}">
                    <c:forEach var="itemToMatch" varStatus="k" items="${uploadFile.itemsToMatchList}" >
                        <c:if test="${tableItem.value.uploadSkuData.rowNum == itemToMatch.value.uploadSkuData.rowNum}">
                            <tr class="row grey">
                            <td class="cell cell-text cell-element"></td>
                            <td class="cell cell-number"><form:checkbox cssClass="checkbox" path="itemsToMatch.selectableObjects[${k.index}].selected"/></td>
                            <td class="cell cell-number"><a href="${editItemUrl}/${itemToMatch.value.uploadSkuData.itemId}"><c:out value="${itemToMatch.value.uploadSkuData.skuNum}"/></a></td>
                            <c:forEach var="code" items="${uploadFile.itemProperties}">
                                <c:set var="colName" value="${code.object2}"/>
                                <td class="cell cell-text">
                                   <%
                                    SelectableObjects.SelectableObject<UploadSkuView> itemToMatch =
                                        (SelectableObjects.SelectableObject<UploadSkuView>)pageContext.getAttribute("itemToMatch");
                                    %>
                                    <%=uploadFile.getSkuProperty(itemToMatch, (String)pageContext.getAttribute("colName"))%>
                                </td>
                            </c:forEach>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:if>

            </c:forEach>

            </tbody>

        </table>
    </c:if>

	<br/>

    <c:if test="${uploadFile.sourceTable != null}">
    <table class="searchResult" width="100%">

            <thead class="header">
            <tr class="row">
                <th class="cell cell-number"><a class="sort" href="${sortUrl}/rowNum"><app:message code="admin.global.filter.label.rowNum" /></a></th>
                <th class="cell cell-text cell-element" nowrap>
                  <a href="javascript:checkAll('uploadFileFormId', 'sourceTable.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
                  <a href="javascript:checkAll('uploadFileFormId', 'sourceTable.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
                </th>
                <c:forEach varStatus="j" var="code" items="${uploadFile.itemProperties}">
                    <c:set var="colName" value="${code.object2}"/>
                    <th class="cell cell-text">
                        <form:select tabindex="11" style="width:158px" path="columnTypes[${j.index}]">
                            <c:forEach var="code" items="${uploadFile.itemPropertiesAll}">
                                <c:set var="selected" value="${colName==code.object2?'selected':''}"/>
                                <form:option value="${code.object1}" selected="${selected}"><c:out value="${code.object2}"/></form:option>
                            </c:forEach>
                </form:select>


                    </th>
                </c:forEach>
            </tr>

            </thead>

            <tbody class="body">


		<c:forEach var="tableRow" varStatus="i" items="${uploadFile.sourceTableList}" >
			<tr class="row">
				<td class="cell cell-number"><c:out value="${i.index}"/></td>
				<td class="cell cell-text cell-element">
					<c:if test="${i.index > 0}">
					<form:checkbox cssClass="checkbox" id="tableRow_${i.index}" path="sourceTable.selectableObjects[${i.index}].selected"/>
					</c:if>
				</td>
				<c:forEach var="tableItem" varStatus="j" items="${tableRow.value}" >
					<td><c:out value="${tableItem}"/></td>
				</c:forEach>
			</tr>
		</c:forEach>

     </table>
    </c:if>

</form:form>

</div>
</div>


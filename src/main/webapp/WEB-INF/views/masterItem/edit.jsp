<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.util.RefCodeNamesKeys" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>

<app:url var="baseUtl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}');$('form:first').submit(); return false; "/>
<c:set var="clearAction" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/clear');$('form:first').submit(); return false;"/>
<c:set var="cloneAction" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/clone');$('form:first').submit(); return false; "/>
<c:set var="deleteImage" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/deleteImage');$('form:first').submit(); return false; "/>
<c:set var="deleteThumbnail" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/deleteThumbnail');$('form:first').submit(); return false; "/>
<c:set var="deleteDed" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/deleteDED');$('form:first').submit(); return false; "/>
<c:set var="deleteMsds" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/deleteMSDS');$('form:first').submit(); return false; "/>
<c:set var="deleteSpecs" value="$('form:first').attr('action','${baseUtl}/masterItem/${masterItem.itemId > 0?masterItem.itemId: 0}/deleteSpecs');$('form:first').submit(); return false; "/>
<c:set var="getAttachmentBaseUrl" value="${baseUtl}/masterItem/${masterItem.itemId>0?masterItem.itemId:(masterItem.cloneItemId>0?masterItem.cloneItemId:0)}"/>

<script type="text/javascript" language="JavaScript">

    function show_level(id) {
        if(eval("document.getElementById(id)").style.display=='none'){
            eval("document.getElementById(id)").style.display='block';
			eval("document.getElementById('v_'+id)").innerHTML="[-]";
        } else {
            eval("document.getElementById(id)").style.display='none';
			eval("document.getElementById('v_'+id)").innerHTML="[+]";
        }
    }

    function expand_level(id)  {
        eval("document.getElementById(id)").style.display='block';
		eval("document.getElementById('v_'+id)").innerHTML="[-]";
    }

    function collapse_level(id) {
        eval("document.getElementById(id)").style.display='none';
		eval("document.getElementById('v_'+id)").innerHTML="[+]";
    }

    function show_levels(action) {
        var elems = document.getElementsByTagName('table');
        var k = 0;
        for(var i=0; i<elems.length ; i++) {
            var idStr = elems[i].id;
            if (idStr != 'undefined' && idStr.match('vId')){
                k++;
                if (action.match('Expand') && (k <= 100)) {  expand_level(idStr);}
                if (action.match('Collapse')) {collapse_level(idStr);}
            }
        }
    }

	function toggle(object) {
		var togElement = document.getElementById('toggleDiv');
		var uomValue = document.getElementById('uomValue');
		if (object.value == '<%=RefCodeNames.ITEM_UOM_CD.UOM_OTHER%>')  {
			togElement.style.visibility = 'visible';
			uomValue.value = "";
			uomValue.focus();
		} else {
			togElement.style.visibility = 'hidden';
			uomValue.value = object.value;
		}
	}

</script>
<c:set var="uomOtherStr" value="<%=RefCodeNames.ITEM_UOM_CD.UOM_OTHER%>"/>
<c:set var="uomOther" value="${uomOtherStr.equals(masterItem.uomCd) ?'visible':'hidden'}" />

<div class="canvas">

<div class="details">

<form:form id="masterItem" modelAttribute="masterItem" action="" method="POST" enctype="multipart/form-data">
<table width=100%>
<tr><td>
    <table>
        <tbody>
        <tr>
            <td><div class="label"><form:label path="itemId"><app:message code="admin.masterItem.label.itemId"/><span class="colon">:</span></form:label></div></td>
            <td><div class="labelValue"><c:out value="${masterItem.itemId}" default="0"/></div><form:hidden path="itemId" value="${masterItem.itemId}"/></td>
            <td><div class="label"><app:message code="admin.masterItem.label.SKU"/><c:if test="${! masterItem.autoSkuFlag}"><span class="colon">:</span><span class="reqind">*</span></c:if></div></td>
            <td>
                <c:if test="${masterItem.autoSkuFlag}">
                   <c:out value="${masterItem.itemSku}"/><form:hidden path="itemSku"/>
                </c:if>
                <c:if test="${! masterItem.autoSkuFlag}">
                    <div class="labelValue"><form:input tabindex="1" path="itemSku" size="65" style="width:150px"/></div>
                </c:if>
            </td>
        </tr>
        <tr>

            <td><div class="label"><form:label  path="itemName"><app:message code="admin.masterItem.label.itemName"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td><form:input tabindex="2" path="itemName" size="65" style="width:355px"/></td>
            <td><div class="label"><form:label  path="uomCd"><app:message code="admin.masterItem.label.UOM"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="3" style="width:158px" path="uomCd" onchange="toggle(this);">
                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="type" items="${requestScope['appResource'].dbConstantsResource.itemUomTypes}">
                        <form:option value="${type.object2}"><c:out value="${type.object2}"/></form:option>
                    </c:forEach>
                </form:select>
			<span id="toggleDiv" style="visibility:${uomOther};">
					<form:input id="uomValue" tabindex="3" path="uomValue" style="width:100px" />
				</span>
            </td>

          </tr>
          <tr>
            <td  rowspan="3"  style="vertical-align: top;"><div class="label"><form:label path="longDesc"><app:message code="admin.masterItem.label.description"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td  rowspan="3" style="vertical-align: top;">
					<div class="value">
						<form:textarea cssClass="std name" tabindex="4" path="longDesc" style="width:360px; height:100px"/>
                    </div>
             </td>
            <td><div class="label"><form:label path="pack"><app:message code="admin.masterItem.label.pack"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td><form:input tabindex="5" path="pack" style="width:150px"/></td>
         </tr>
         <tr>
            <td><div class="label"><form:label path="size"><app:message code="admin.masterItem.label.size"/><span class="colon">:</span></form:label></div></td>
            <td><form:input tabindex="6" path="size" style="width:150px"/></td>
         </tr>
         <tr>
            <td><div class="label"><form:label path="size"><app:message code="admin.masterItem.label.varyByGeography"/><span class="colon">:</span></form:label></div></td>
            <td><form:checkbox tabindex="7" cssClass="checkbox" path="uomAndPackVaryByGeography"/></td>
         </tr>
         <tr>
            <td><div class="label"><form:label  path="manufacturerName"><app:message code="admin.masterItem.label.manufacturer"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="8" style="width:365px" path="manufacturerId">
                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="manuf" items="${masterItem.allManufacturerList}">
                        <form:option value="${manuf.busEntityId}"><c:out value="${manuf.shortDesc}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>
            <td><div class="label"><form:label path="manufacturerSku"><app:message code="admin.masterItem.label.manufacturerSku"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td><form:input tabindex="9" path="manufacturerSku" style="width:150px"/></td>
         </tr>
         <tr>
            <td><div class="label"><form:label  path="itemCategoryId"><app:message code="admin.masterItem.label.category"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="10" style="width:365px" path="itemCategoryId">
                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="categ" items="${masterItem.allCategories}">
                        <form:option value="${categ.itemId}"><c:out value="${categ.shortDesc}"/></form:option>
                    </c:forEach>
                </form:select>

            </td>
            <td><div class="label"><form:label path="status"><app:message code="admin.masterItem.label.status"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
            <td>
                <form:select tabindex="11" style="width:158px" path="status"><form:option value=""><app:message code="admin.global.select"/></form:option>
                    <c:forEach var="code" items="${requestScope['appResource'].dbConstantsResource.busEntityStatuseCds}">
                        <form:option value="${code.object2}"><app:message code="refcodes.BUS_ENTITY_STATUS_CD.${code.object1}" text="${code.object2}"/></form:option>
                    </c:forEach>
                </form:select>
            </td>
         </tr>
        </tbody>
      </table>
   </td>
</tr>

 <tr><td>
     <table >
         <tr>
			<td width="500px">&nbsp;</td>
             <td>
			 <form:button tabindex="12" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
             <c:if test="${masterItem.itemId!=null && masterItem.itemId>0}">
                     &nbsp;&nbsp;&nbsp;&nbsp;<form:button  tabindex="13" onclick="${cloneAction} return false;"><app:message code="admin.global.button.clone"/></form:button>
             </c:if>
			 </td>
         </tr>
     </table>

 </td></tr>


<tr><td>
<hr>
<table width="100%">
    <tr>
        <td><div class="subHeader"><app:message code="admin.masterItem.subheader.attachments"/> <span id="v_vIdAttachTable" style="cursor:hand;cursor:pointer;" onClick="show_level('vIdAttachTable');">[-]</span></div></td>
		<td><div class="subHeader" align="right">
            <nobr><a href="javascript:show_levels('Expand');"
				onClick=""><app:message code="admin.site.workflow.rule.label.expandAll"
				/></a>&nbsp;|<a href="javascript:show_levels('Collapse');"
					onClick=""><app:message code="admin.site.workflow.rule.label.collapseAll" /></a></nobr>
			</div>
		</td>
    </tr>
</td></tr>
</table>
<table id="vIdAttachTable" style="display:visible">
    <tr>
        <td><img src="${getAttachmentBaseUrl}/getItemImage?path=${masterItem.imgFileName}" border="0"/></td>
        <td>
            <div class="label"><form:label path="imgFileData"><app:message code="admin.masterItem.label.uploadImage"/></form:label></div>
            <br><c:if test="${masterItem.imgFileName!=null}"><c:out value="${masterItem.imgFileName}"/><br><br></c:if>
            <form:input tabindex="14" path="imgFileData" type="file"/>
            <c:if test="${masterItem.imgFileName!=null}">
            <br><form:hidden path="imgFileName" value="${masterItem.imgFileName}"/>
            <form:button tabindex="14" onclick="${deleteImage} return false;"><app:message code="admin.global.button.deleteImage"/></form:button>
            </c:if>
        </td>
		<td width="24px"></td>
        <td><img src="${getAttachmentBaseUrl}/getThumbnail?path=${masterItem.thumbnailFileName}" border="0"/></td>
        <td>
            <div class="label"><app:message code="admin.masterItem.label.uploadThumbnail"/></div>
            <br><c:if test="${masterItem.thumbnailFileName!=null}"><c:out value="${masterItem.thumbnailFileName}"/><br><br></c:if>
            <form:input tabindex="15" path="thumbnailFileData" type="file"/>
            <c:if test="${masterItem.thumbnailFileName!=null}">
            <br><form:hidden path="thumbnailFileName" value="${masterItem.thumbnailFileName}"/>
            <form:button tabindex="15" onclick="${deleteThumbnail} return false;"><app:message code="admin.global.button.deleteThumbnail"/></form:button>
            </c:if>
         </td>
        <td></td>
    </tr>

    <tr>
        <td><c:if test="${masterItem.MSDSFileName!=null}"><a class="pdf" href="${getAttachmentBaseUrl}/getItemAttachment?path=${masterItem.MSDSFileName}" target="_blank"><app:message code="admin.masterItem.label.msds"/></a></c:if></td>
        <td><br>
            <div class="label"><form:label path="MSDSFileName"><app:message code="admin.masterItem.label.uploadMSDS"/></form:label></div>
            <br><c:if test="${masterItem.MSDSFileName!=null}"><c:out value="${masterItem.MSDSFileName}"/><br><br></c:if>
            <form:input tabindex="16" path="MSDSFileData" type="file" accept="application/pdf"/>
            <c:if test="${masterItem.MSDSFileName!=null}">
            <br><form:hidden path="MSDSFileName" value="${masterItem.MSDSFileName}"/>
            <form:button tabindex="14" onclick="${deleteMsds} return false;"><app:message code="admin.global.button.deleteMSDS"/></form:button>
            </c:if>
        </td>
        <td></td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td><c:if test="${masterItem.DEDFileName!=null}"><a class="pdf" href="${getAttachmentBaseUrl}/getItemAttachment?path=${masterItem.DEDFileName}" target="_blank"><app:message code="admin.masterItem.label.ded"/></a></c:if></td>
        <td><br>
            <div class="label"><form:label path="DEDFileName"><app:message code="admin.masterItem.label.uploadDED"/></form:label></div>
            <br><c:if test="${masterItem.DEDFileName!=null}"><c:out value="${masterItem.DEDFileName}"/><br><br></c:if>
            <form:input tabindex="17" path="DEDFileData" type="file" accept="application/pdf"/>
            <c:if test="${masterItem.DEDFileName!=null}">
            <br><form:hidden path="DEDFileName" value="${masterItem.DEDFileName}"/>
            <form:button tabindex="14" onclick="${deleteDed} return false;"><app:message code="admin.global.button.deleteDED"/></form:button>
            </c:if>
        </td>
        <td></td>
        <td colspan="3"></td>
    </tr>

    <tr>
        <td><c:if test="${masterItem.specsFileName!=null}"><a class="pdf" href="${getAttachmentBaseUrl}/getItemAttachment?path=${masterItem.specsFileName}" target="_blank"><app:message code="admin.masterItem.label.specs"/></a></c:if></td>
        <td><br>
            <div class="label"><form:label path="specsFileName"><app:message code="admin.masterItem.label.uploadSpecs"/></form:label></div>
            <br><c:if test="${masterItem.specsFileName!=null}"><c:out value="${masterItem.specsFileName}"/><br><br></c:if>
            <form:input tabindex="18" path="specsFileData" type="file" accept="application/pdf"/>
            <c:if test="${masterItem.specsFileName!=null}">
            <br><form:hidden path="specsFileName" value="${masterItem.specsFileName}"/>
            <form:button tabindex="14" onclick="${deleteSpecs} return false;"><app:message code="admin.global.button.deleteSpecs"/></form:button>
            </c:if>
        </td>
        <td></td>
        <td></td>
		<td></td>
        <td></td>
    </tr>

    <tr>
        <td colspan="6">
			<table>
			<tr>
			<td width="500px">&nbsp;</td>
			<td>
            <form:button tabindex="19" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
             <c:if test="${masterItem.itemId!=null && masterItem.itemId>0}">
                     &nbsp;&nbsp;&nbsp;&nbsp;<form:button  tabindex="20" onclick="${cloneAction} return false;"><app:message code="admin.global.button.clone"/></form:button>
             </c:if>
			</td></tr>
			</table>
		</td>
    </tr>
   </table>
 </td>
</tr>


<tr>
  <td>
  <hr>
  <table>
    <tbody>
    <tr><td><nobr><div class="subHeader"><app:message code="admin.masterItem.subheader.otherProperties"/> <span id="v_vIdOtherTable" style="cursor:hand;cursor:pointer;" onClick="show_level('vIdOtherTable');">[-]</span></div></nobr></td></tr>
	</tbody>
  </table>

  <table  id="vIdOtherTable" style="display:visible">

    <tr>
      <td width="200px"><div class="label"><form:label path="productUPC"><app:message code="admin.masterItem.label.productUPC"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="21" path="productUPC" style="width:150px"/></td>
      <td width="100px"></td>
      <td><div class="label"><form:label path="color"><app:message code="admin.masterItem.label.itemColor"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="22" path="color" style="width:150px"/></td>
    </tr>

    <tr>
      <td><div class="label"><form:label path="packUPC"><app:message code="admin.masterItem.label.packUPC"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="23" path="packUPC" style="width:150px"/></td>
      <td></td>
      <td><div class="label"><form:label path="scent"><app:message code="admin.masterItem.label.scent"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="24" path="scent" style="width:150px"/></td>
    </tr>

    <tr>
      <td><div class="label"><form:label path="UNSPSCCode"><app:message code="admin.masterItem.label.UNSPSCCode"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="25" maxlength="10" path="UNSPSCCode" style="width:150px"/></td>
      <td></td>
      <td><div class="label"><form:label path="listPriceMSRP"><app:message code="admin.masterItem.label.listPriceMSRP"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="26" path="listPriceMSRP" style="width:150px"/></td>
    </tr>

    <tr>
      <td><div class="label"><form:label path="shippingCubicSize"><app:message code="admin.masterItem.label.shippingCubicSize"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="27" path="shippingCubicSize" style="width:150px"/></td>
      <td></td>
      <td><div class="label"><form:label path="NSN"><app:message code="admin.masterItem.label.NSN"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="28" path="NSN" style="width:150px"/></td>
    </tr>

    <tr>
      <td><div class="label"><form:label path="shippingWeight"><app:message code="admin.masterItem.label.shippingWeight"/><span class="colon">:</span></form:label></div></td>
      <td><form:input tabindex="29" path="shippingWeight" style="width:150px"/></td>
      <td></td>
      <td><div class="label"><form:label path="weightUnit"><app:message code="admin.masterItem.label.weightUnit"/><span class="colon">:</span></form:label></div></td>
      <td>
        <form:select tabindex="30" style="width:158px" path="weightUnit"><form:option value=""><app:message code="admin.global.select"/></form:option>
            <c:forEach var="code" items="${requestScope['appResource'].dbConstantsResource.weightUnitTypes}">
                <form:option value="${code.object2}"><app:message code="refcodes.WEIGHT_UNIT_CD.${code.object1}" text="${code.object2}"/></form:option>
            </c:forEach>
        </form:select>

      </td>
    </tr>
	<tr>
      <td><div class="label"><form:label path="hazmat"><app:message code="admin.masterItem.label.hazmat"/><span class="colon">:</span></form:label></div></td>
      <td><form:checkbox tabindex="31" cssClass="checkbox" focusable="true" path="hazmat"/></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>

  </table>
  </td>
</tr>


<tr>
  <td>
  <hr>
  <table>
    <tbody>
		<tr>
        <td><div class="subHeader"><app:message code="admin.masterItem.subheader.certifiedCompanies"/> <span id="v_vIdCertCompaniesTable" style="cursor:hand;cursor:pointer;" onClick="show_level('vIdCertCompaniesTable');">[-]</span></div></td>

        </tr>
	</tbody>
  </table>

	<table id="vIdCertCompaniesTable" style="display:visible">
        <c:if test="${masterItem.result != null}">
		<tr>
		<td width="50px">&nbsp;</td>
		<td width="300px">&nbsp;</td>
        <td class="cell cell-text cell-element">
			<span id="view_certCompamiesTable" style="display:visible">
			<a href="javascript:checkAll('masterItemFormId', 'allCertifiedCompanyList.selectableObjects', true)"><app:message code="admin.global.filter.label.selectAll" /></a><br>
            <a href="javascript:checkAll('masterItemFormId', 'allCertifiedCompanyList.selectableObjects', false)"><app:message code="admin.global.filter.label.clearAll" /></a>
			</span>
        </td>
		 </tr>
         <c:forEach var="certCompany" items="${masterItem.result}"  varStatus="i">
          <tr>
			<td></td>
            <td><div class="label"><c:out value="${certCompany.value.shortDesc}"/></div></td>
            <td>
                <form:checkbox tabindex="32" cssClass="checkbox" id="certCompany_${certCompany.value.busEntityId}" path="allCertifiedCompanyList.selectableObjects[${i.index}].selected"/>
			</td>
           </tr>
         </c:forEach>

         </c:if>

  </table>
  </td>
</tr>


<tr>
  <td>
  <hr>
  <table>
    <tbody>
    <tr><td colspan="2"><div class="subHeader"><app:message code="admin.masterItem.subheader.productQRCode"/> <span id="v_vIdQrCodeTable" style="cursor:hand;cursor:pointer;" onClick="show_level('vIdQrCodeTable');">[-]</span></div></td></tr>
	</tbody>
  </table>

	<table  id="vIdQrCodeTable" style="display:visible">
	<tr><td>
	<table>
    <tr>
        <td><div class="label"><app:message code="admin.masterItem.label.productName"/><span class="colon">:</span></div></td>
        <td><div class="label"><c:out value="${masterItem.itemName}"/></div></td>
    </tr>
    <tr>
        <td><div class="label"><app:message code="admin.masterItem.label.SKU"/><span class="colon">:</span></div></td>
        <td><div class="label"><c:out value="${masterItem.itemSku}"/></div></td>
    </tr>
    <tr>
        <td><div class="label"><app:message code="admin.masterItem.label.MFGSku"/><span class="colon">:</span></div></td>
        <td><div class="label"><c:out value="${masterItem.manufacturerSku}"/></div></td>
    </tr>
    <tr>
        <td><div class="label"><app:message code="admin.masterItem.label.pack"/><span class="colon">:</span></div></td>
        <td><div class="label"><c:out value="${masterItem.pack}"/></div></td>
    </tr>
    <tr>
        <td><div class="label"><app:message code="admin.masterItem.label.UOM"/><span class="colon">:</span></div></td>
        <td><div class="label"><c:out value="${masterItem.uomCd}"/></div></td>
    </tr>
  </table>
  </td></tr>
  <tr><td>
	<table>
	<tr>
	<td>
		<img  src="${baseUtl}/masterItem/${masterItem.itemId>0?masterItem.itemId:0}/getQRCode?desc=${masterItem.itemName}" border="0"/>
	</td>

	<td width="60%">
		<table class="boxContent">
			<tr><td><div class="label"><span style="color:black"><app:message code="admin.masterItem.label.rq.instructions"/></span></div></td></tr>
			<tr><td><div class="value"><span style="color:black"><app:message code="admin.masterItem.label.rq.print"/></span></div></td></tr>
			<tr><td><div class="value"><span style="color:black"><app:message code="admin.masterItem.label.rq.affix"/></span></div></td></tr>
			<tr><td><div class="value"><span style="color:black"><app:message code="admin.masterItem.label.rq.scan"/><span class="required">*</span></span></div></td></tr>
			<tr><td><div class="value"><span style="color:black"><app:message code="admin.masterItem.label.rq.onHandQty"/></span></div></td></tr>
			<tr><td><div class="value"><span style="color:black"><app:message code="admin.masterItem.label.rq.submit"/></span></div></td></tr>
		</table>
	</td>

	</tr>
	<tr>
		<td>
			<tr><td><nobr><div class="value"><span style="color:black"><span class="required">*</span><app:message code="admin.masterItem.label.rq.scanMessage"/></span></div></nobr></td></tr>
		</td>
	</tr>
	<tr>
        <td colspan="2">
		<table>
			<tr>
			<td width="500px">&nbsp;</td>
			<td>
			 <form:button tabindex="33" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
             <c:if test="${masterItem.itemId!=null && masterItem.itemId>0}">
                 &nbsp;&nbsp;&nbsp;<form:button  tabindex="34" onclick="${cloneAction} return false;"><app:message code="admin.global.button.clone"/></form:button>

             </c:if>
			 </td>
			 </tr>
		</table>
		</td>
    </tr>
	</table>
	</td></tr>
	</table>
  </td>
</tr>


</table>
<form:hidden path="cloneItemId"/>
</form:form>

</div>
</div>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<app:url var="baseUrlLocate"/>
<c:set var="baseViewName" value="${param['baseViewName']}"/>
<c:choose>
	<c:when test="${param['baseViewProperty'] != null}">
		<c:set var="baseViewProperty" value="${param['baseViewProperty']}"/>
	</c:when>
	<c:otherwise>
		<c:set var="baseViewProperty" value="setFilteredItem"/>
	</c:otherwise>
</c:choose>
<c:set var="detailUrl" value="${param['pageUrl']}"/>
<c:set var="clearLocateItem" value="$('form:first').attr('action','${detailUrl}/clear/item');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('itemFilterInputSkus');}"/>

<app:locateLayer var="itemLayer"
                 titleLabel="admin.global.filter.label.locateItem.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrlLocate}/locate/item/multi'
                 action="${baseUrlLocate}/locate/item/selected?filter=${baseViewName}.${baseViewProperty}"
                 idGetter="itemId"
                 nameGetter="itemSku"
                 targetNames="itemFilterInputSkus"
                 targetIds="itemFilterInputIds"
                 finallyHandler="${finallyHandler}"/>


             <table class="locate">
                <tr>
                    <td  class="cell label first locate">
                        <label><app:message code="admin.global.filter.label.items" /><span class="colon">:</span></label>
                    </td>
                    <td rowspan="3" class="cell locate">
                        <textarea id="itemFilterInputSkus"
                                  focusable="true"
                                  tabindex="3"
                                  cols="60" rows="4"
                                   readonly="true"
                                  class="readonly">${param['filteredItemCommaNames']}
                        </textarea>
<!--                              onkeypress="if(event) { if(event.keyCode == 13){$('form:first').submit(); return false;}}" -->

                    </td>
                 </tr>
                 <tr>
                    <td  class="cell label first locate">
                        <button tabindex="1" onclick="${itemLayer}" style="width:150px"><app:message code="admin.global.filter.text.searchItems"/></button>
                    </td>
                 </tr>
                 <tr>
                    <td  class="cell label first locate">
                        <button tabindex="2" onclick="${clearLocateItem}" style="width:150px"><app:message code="admin.global.filter.text.clearItems"/></button>
                    </td>
                 </tr>

            </table>

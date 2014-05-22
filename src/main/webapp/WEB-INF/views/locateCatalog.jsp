<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<app:url var="baseUrl"/>
<c:set var="baseViewName" value="${param['baseViewName']}"/>

<c:set var="clearLocateCatalog" value="$('form:first').attr('action','${baseUrl}/${baseViewName}/filter/clear/catalog');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('catalogFilterInputNames');}"/>

<app:locateLayer var="catalogLayer"
                 titleLabel="admin.global.filter.label.locateCatalog.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/catalog'
                 action="${baseUrl}/locate/catalog/selected?filter=${baseViewName}Filter.setFilteredCatalogs"
                 idGetter="catalogId"
                 nameGetter="catalogName"
                 targetNames="catalogFilterInputNames"
                 targetIds="catalogFilterInputIds"
                 finallyHandler="${finallyHandler}"/>

            <table class="locate">
                <tr>
                    <td  class="cell label first locate"><label>
                        <app:message code="admin.global.filter.label.filterByCatalogs" /><span class="colon">:</span></label><br>
                        <button tabindex="2" onclick="${catalogLayer}"><app:message code="admin.global.filter.label.searchCatalog"/></button><br>
                        <button tabindex="3" onclick="${clearLocateCatalog}"><app:message code="admin.global.filter.label.clearCatalogs"/></button>
                    </td>
                    <td class="cell locate">
                        <textarea id="catalogFilterInputNames"
                                  focusable="true"
                                  tabindex="4"
                                  readonly="true"
                                  class="readonly">${param['filteredCatalogCommaNames']}
                        </textarea>
                    </td>
                </tr>
                <tr><td  class="cell label first"></td><td class="cell value"></td></tr>
            </table>

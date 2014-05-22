<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<app:url var="baseUrlLocate"/>

<c:set var="baseViewName" value="${param['baseViewName']}"/>
<c:set var="detailUrl" value="${param['pageUrl']}"/>
<c:set var="clearLocateDistr" value="$('form:first').attr('action','${detailUrl}/clear/distr');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('distrFilterInputNames');}"/>

<app:locateLayer var="distrLayer"
                 titleLabel="admin.global.filter.label.locateDistr.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrlLocate}/locate/distributor/multi'
                 action="${baseUrlLocate}/locate/distributor/selected?filter=${baseViewName}.setFilteredDistr"
                 idGetter="distributorId"
                 nameGetter="distributorName"
                 targetNames="distrFilterInputNames"
                 targetIds="distrFilterInputIds"
                 finallyHandler="${finallyHandler}"/>

<%-- 	<form:hidden  path="toClearDist"/> --%>

             <table class="locate">
                <tr>
                    <td  class="cell label first locate">
                        <label><app:message code="admin.global.filter.label.distributors" /><span class="colon">:</span></label>
                    </td>
                    <td rowspan="3" class="cell locate">
                        <textarea id="distrFilterInputNames"
                                  focusable="true"
                                  tabindex="3"
                                  cols="60" rows="4"
                                  onkeypress="return false;"
                                  readonly="true"
                                  class="readonly">${param['filteredDistrCommaNames']}
                        </textarea>
<!--                              onkeypress="if(event) { if(event.keyCode == 13){$('form:first').submit(); return false;}}" -->
                    </td>
                 </tr>
                 <tr>
                    <td  class="cell label first locate">
                        <button style="width:200px" tabindex="1" onclick="${distrLayer}"><app:message code="admin.global.filter.text.searchDistributors"/></button>
                    </td>
                 </tr>
                 <tr>
                    <td  class="cell label first locate">
                        <button style="width:200px" tabindex="2" onclick="${clearLocateDistr}"><app:message code="admin.global.filter.text.clearDistributors"/></button>
                    </td>
                 </tr>

            </table>

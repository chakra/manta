<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<app:url var="baseUrl"/>
<c:set var="baseViewName" value="${param['baseViewName']}"/>

<c:set var="clearLocateAccount" value="$('form:first').attr('action','${baseUrl}/${baseViewName}/filter/clear/account');$('form:first').attr('method','POST');$('form:first').submit(); return false;"/>
<c:set var="finallyHandler" value="function(value) {f_setFocus('accountFilterInputNames');}"/>

<app:locateLayer var="accountLayer"
                 titleLabel="admin.global.filter.label.locateAccount.title"
                 closeLabel='admin.global.button.close'
                 layer='${baseUrl}/locate/account'
                 action="${baseUrl}/locate/account/selected?filter=${baseViewName}Filter.setFilteredAccounts"
                 idGetter="accountId"
                 nameGetter="accountName"
                 targetNames="accountFilterInputNames"
                 targetIds="accountFilterInputIds"
                 finallyHandler="${finallyHandler}"/>

            <table class="locate">
                <tr>
                    <td  class="cell label first locate"><label>
                        <app:message code="admin.global.filter.label.filterByAccounts" /><span class="colon">:</span></label><br>
                        <button tabindex="2" onclick="${accountLayer}"><app:message code="admin.global.filter.label.searchAccount"/></button><br>
                        <button tabindex="3" onclick="${clearLocateAccount}"><app:message code="admin.global.filter.label.clearAccounts"/></button>
                    </td>
                    <td class="cell locate">
                        <textarea id="accountFilterInputNames"
                                  focusable="true"
                                  tabindex="4"
                                  readonly="true"
                                  class="readonly">${param['filteredAccountCommaNames']}
                        </textarea>
                    </td>
                </tr>
                <tr><td  class="cell label first"></td><td class="cell value"></td></tr>
            </table>

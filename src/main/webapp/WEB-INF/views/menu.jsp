<%@ page import="com.espendwise.manta.util.RefCodeNames" %>
<%@ page import="com.espendwise.manta.auth.Auth" %>
<%@ page import="com.espendwise.manta.web.controllers.UrlPathKey" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="app" uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="wideScreen" value="/display/wideScreen.do"/>

<app:url var="baseUrl"/>

<c:set var="instancePrefix" value="<%=UrlPathKey.INSTANCE_PREFIX%>" />
<c:set var="basePath" value="${instancePrefix}/${requestScope['storeContext'].globalEntityId}"/>

<script type="text/javascript">

    $(document).ready(function() {

        $('ul#navi ul').each(function (i) { // Check each submenu:
            $(this).prev().addClass('collapsible').click(function() { // Attach an event listener
                if ($(this).next().css('display') == 'none') { $(this).next().slideDown(1, function () { $(this).removeClass("hide");   $(this).prev().removeClass('collapsed').addClass('expanded'); });
                }else { $(this).next().slideUp(1, function () { $(this).addClass("hide");  $(this).prev().removeClass('expanded').addClass('collapsed'); $(this).find('ul').each(function () { $(this).hide(0).prev().removeClass('expanded').addClass('collapsed'); }); });}
                return false; // Prohibit the browser to follow the link address
            });
        });
    });

</script>


<div id="menuWrapper" class="wrapper${requestScope['displaySettings'].wideScreen}">
    <div class="top">
        <div class="topWrapper"><span class="left">&nbsp;</span><span class="center">&nbsp;</span>
            <span class="right">
                <span id="tooggler" style="position:relative;float:left;z-index:1110;left:-11px;top:9px;"><a href="#" onclick="wideScreen('${wideScreen}');">
                    <span class="expandArrow expandArrow${requestScope['displaySettings'].wideScreen}"></span>
                </a>
                </span>
            </span>

        </div>
    </div>
    <div  id="menucontent" class="middle${requestScope['displaySettings'].wideScreen}">
        <div class="middleWrapper">
            <span class="left">&nbsp;</span>
            <div class="content">

                <app:menu basePath="${basePath}">

                    <app:menuitem name="admin.global.menu.entities" path="#">

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_ACCOUNT%>">
                            <app:menuitem href="${baseUrl}/account" name="admin.global.menu.accounts" path="/account"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_USERS%>">
                            <app:menuitem href="${baseUrl}/user" name="admin.global.menu.users" path="/user"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_SITE%>">
                            <app:menuitem href="${baseUrl}/location" name="admin.global.menu.sites" path="/location"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MANUFACTURER%>">
                            <app:menuitem href="${baseUrl}/manufacturer" name="admin.global.menu.manufacturers" path="/manufacturer"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_DISTRIBUTOR%>">
                            <app:menuitem href="${baseUrl}/distributor" name="admin.global.menu.distributors" path="/distributor"/>
                        </app:authorizedForFunction>

                    </app:menuitem>

                    <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MESSAGES%>">
                        <app:menuitem href="${baseUrl}/storeMessage" name="admin.global.menu.storeMessages" path="/storeMessage"/>
                    </app:authorizedForFunction>

                    <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_EMAIL_TEMPLATES%>">
                        <app:menuitem href="${baseUrl}/emailTemplate" name="admin.global.menu.emailTemplate" path="/emailTemplate"/>
                    </app:authorizedForFunction>

                    
                    <app:menuitem name="admin.global.menu.transactions" path="#">
                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_ORDERS%>">
                            <app:menuitem href="${baseUrl}/order" name="admin.global.menu.orders" path="/order"/>
                        </app:authorizedForFunction>
                        
                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_BATCH_ORDERS%>">
                            <app:menuitem href="${baseUrl}/batchOrder/loader" name="admin.global.menu.batchOrders" path="/batchOrder/loader"/>
                        </app:authorizedForFunction>
                    </app:menuitem>
                    

					<%
						//if the user is either an admin or sysadmin, or they are not an admin
						//or sysadmin but have the group function allowing them access to the
						//history functionality, render the history menu item
					%>
					<%
						List<String> roles = new ArrayList<String>();
						roles.add(RefCodeNames.USER_TYPE_CD.ADMINISTRATOR);
						roles.add(RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR);
					%>
                    <app:hasRole roles="<%=roles%>">
                        <app:menuitem href="${baseUrl}/history" name="admin.global.menu.history" path="/history"/>
                    </app:hasRole>
                    <app:notHasRole roles="<%=roles%>">
                    	<app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.ACCESS_HISTORY%>">
                        	<app:menuitem href="${baseUrl}/history" name="admin.global.menu.history" path="/history"/>
                    	</app:authorizedForFunction>
                    </app:notHasRole>

                    <app:menuitem name="admin.global.menu.productManagement" path="#">

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CATALOGS%>">
                            <app:menuitem href="${baseUrl}/catalogManager/loader" name="admin.global.menu.catalogs" path="/catalogManager/loader"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CATEGORIES%>">
                            <app:menuitem href="${baseUrl}/category" name="admin.global.menu.categories" path="/category"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_COST_CENTERS%>">
                            <app:menuitem href="${baseUrl}/costCenter" name="admin.global.menu.costCenters" path="/costCenter"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_MASTER_ITEMS%>">
                            <app:menuitem href="${baseUrl}/masterItem" name="admin.global.menu.masterItems" path="/masterItem"/>
                        </app:authorizedForFunction>

                    </app:menuitem>
				<%boolean isAnyChildItemAuthorized = Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_CMS)||
						Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CORPORATE_SCHED) ||
						Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_GROUPS) ||
						Auth.isAuthorizedForFunction(RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_PROFILE);

				%>
				<c:if test="${!isAnyChildItemAuthorized}">
                    <app:menuitem name="admin.global.menu.setup" path="#">

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.STORE_MGR_TAB_CMS   %>">
                            <app:menuitem href="${baseUrl}/cms/${requestScope['storeContext'].storeId}" name="admin.global.menu.cms" path="/cms"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_CORPORATE_SCHED   %>">
                            <app:menuitem href="${baseUrl}/corporateSchedule" name="admin.global.menu.corporateSchedule" path="/corporateSchedule"/>
                        </app:authorizedForFunction>

                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_GROUPS%>">
                            <app:menuitem href="${baseUrl}/group" name="admin.global.menu.groups" path="/group"/>
                        </app:authorizedForFunction>
                        
                        <app:authorizedForFunction name="<%=RefCodeNames.APPLICATION_FUNCTIONS.MANTA_STORE_MGR_TAB_PROFILE%>">
                            <app:menuitem href="${baseUrl}/profile/passwordManagement" name="admin.global.menu.profile" path="/profile"/>
                        </app:authorizedForFunction>

                      </app:menuitem>
				</c:if>                     
                </app:menu>


            </div>


            <span class="right">&nbsp;</span></div>
    </div>
    <div class="bottom">
        <div class="bottomWrapper"><span class="left">&nbsp;</span><span class="center">&nbsp;</span><span
                class="right">&nbsp;</span></div>
    </div>
</div>
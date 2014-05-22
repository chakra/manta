
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.util.Utility" %>
<%@ page import="com.espendwise.manta.i18n.I18nResource" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url var="resources" value="/resources" scope="request"/>

<c:set var="forgotPasswordAction" value="$('form:first').attr('action','forgotPassword');$('form:first').submit()"/>
<c:set var="loginAction" value=" $('form:first').submit()"/>

<c:url var="logo" value="/resources/${login.datasource}/${login.storeId}/logo?path=${login.uiOptions.logo}&country=${login.country}&language=${login.language}" />

<c:set var="title" value="${login.uiOptions.title}"/>
<c:set var="footer" value="${login.uiOptions.footer}"/>
<c:set var="storeId" value="${login.storeId}"/>


<head>
    <title><spring:message code="login.title.login"/> - ${title}</title>
    <jsp:include page="includes.jsp"/>
    <script type="text/javascript" src="<c:url value="/resources/js/scripts.js"/>"></script>
    <script>$(function () { $('input[name="j_username"]').keypress(function (event) { if (event.which == '13') { event.preventDefault();}})});</script>
    <script type="text/javascript">$(document).keyup( function(e) {
        if (e.keyCode == 13) {
            var p = $('input[name="j_password"]'), n = $('input[name="j_username"]'), isn = n.attr('value') && n.attr('value').length > 0, isp = p.attr('value') && p.attr('value').length > 0;
            if ((isn && isp) || (e.target && e.target.name == "j_password")) {  $('form:first').submit();  }
            else if (e.target && e.target.name == "j_username") {  p.focus(); }
            else if (e.target && e.target.name != "j_username" && e.target.name != "j_password") { if (isn) { p.focus(); } else{ n.focus(); }}
        }
    })</script>
</head>



<body class="login">

<div id="loginWrapper">

    <div class="top">&nbsp;</div>

    <div class="loginBox clearfix">

       <img  src="<c:url value="/resources//images/login/lock-Icon.png"/>" alt="Mascot" title="Mascot" class="mascot" />

        <img src='${logo}'/>
          <c:if test="${not empty requestScope['loginError']}">
               <c:set var="error" value="${requestScope['loginError'].message}"/>
              <div class="error">
                  <div class="errorMessage"><p><spring:message  message="${error}"/></p></div>
              </div>
         </c:if>

        <form name="f" id="loginForm" action="<c:url value='j_spring_security_check'/>" method="POST" focus="j_username">

            <label class="login"><spring:message code="login.label.username"/></label>
            <div class="inputWrapper"><input type='text' id="j_username" name='j_username' value='<c:if test="${not empty requestScope['loginError']}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></div>

            <label class="login"><spring:message code="login.label.password"/></label>
            <div class="inputWrapper"><input type='password' name='j_password'></div>

            <p class="forgotPassword"><%--<a href="javascript:${forgotPasswordAction}"><spring:message code="login.text.forgotPasswordQst"/></a>--%></p>

            <a href="#" onclick="${loginAction}" class="loginBtn"><span><spring:message code="login.button.login"/></span></a>
            <p><%--<label><input type="checkbox" name="_spring_security_remember_me"><spring:message code="login.label.rememberUserName"/> &nbsp;</label>--%></p>
            <input type="hidden" name="storeId" value="${storeId}"/>
        </form>

    </div>
    <div class="bottom">&nbsp;</div>
    <div id="footer">${footer}</div>
</div>

</body>




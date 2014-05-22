
<%@ page import="com.espendwise.manta.util.Constants" %>
<%@ page import="com.espendwise.manta.util.Utility" %>
<%@ page import="com.espendwise.manta.i18n.I18nResource" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<head>
    <title><spring:message code="admin.title.exception"/></title>
    <jsp:include page="declare.jsp"/>
    <jsp:include page="includes.jsp"/>
    <script type="text/javascript" src="${resources}/js/scripts.js"></script>
    <script type="text/javascript">$(document).ready(function(){$('a.back').click(function(){ parent.history.back();return false; });});</script>
</head>


<body>
<div id="exception">
    <div class="excWrapper clearfix">

        <div class="excTop clearfix"> <div class="excTopLeft"></div><div class="excTopRight"></div></div>

        <div class="excContent clearfix">

            <table  width="100%">
                <tr>
                    <td class=" excIcon"><img src="${resources}/images/ui/error/icon-error.png"/></td>
                    <td>
                        <div class="excTitle"><spring:message code="admin.global.text.errTitle"/>.</div>
                        <div class="excBody"><spring:message code="admin.global.text.errNote"/></div>
                        <div class="excInfo">
                        <c:if test="${exception.resolved == true}">
                            <table>
                                <tr>
                                    <td class="excMessage label errLabel"><spring:message code="admin.global.label.errorLabel"/>:</td>
                                    <td class="excMessage errValue"><spring:message message="${exception.message}"/></td>
                                </tr>
                                <tr>
                                    <td class="excReason label errLabel"><spring:message code="admin.global.label.reasonLabel"/>:</td>
                                    <td class="excReason errValue">
                                        <c:forEach var="reason" items="${exception.reasons}">
                                        <spring:message message="${reason}"/>
                                            <br>
                                    </c:forEach>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                        <c:if test="${exception.resolved == false}">
                            <table>
                                <tr>
                                    <td class="excMessage label errLabel"><spring:message code="admin.global.label.errorLabel"/>:</td>
                                    <td class="excMessage errValue"><spring:message message="${exception.message}"/></td>
                                </tr>
                            </table>
                        </c:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td align="right" style="padding-right: 15px"><br>

                        <table class="excButton back">
                            <tr>
                            <td class="excButtonLeft"></td>
                            <td class="excButtonBody"><a href="#" class="back"><strong><app:message code="admin.global.button.back"/></strong></a></td>
                            <td class="excButtonRight"></td>
                           </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>

        <div class="excBottom clearfix"> <div class="excBottomLeft"></div><div class="excBottomRight"></div></div>

    </div>
</div>
</body>
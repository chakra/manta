<%@ page import="java.util.StringTokenizer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tiles:importAttribute name="tabs"/>
<tiles:importAttribute name="jsp"/>
<tiles:importAttribute name="jspHeader"/>


<div class="tabbed">

    <div class="tabsContent clearfix">

        <div class="tabHeader clearfix"><jsp:include page="${pageScope['jspHeader']}"/></div>

        <table width="100%">
            <tr>
                <td>
                    <div class="tabs clearfix">
                        <jsp:include page="${pageScope['tabs']}">
                            <jsp:param name="jsp" value="${pageScope['jsp']}"/>
                        </jsp:include>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="content">
                        <jsp:include page="${pageScope['jsp']}"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>


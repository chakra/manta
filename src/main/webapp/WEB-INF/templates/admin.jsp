<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page  contentType="text/html; charset=UTF-8" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="app" uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" %>

<tiles:insertAttribute name="declare"/>
<tiles:importAttribute scope="page"/>

<c:set var="titleCode"><tiles:getAsString name="title"/></c:set>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title><app:message code="${titleCode}"/></title>
    <tiles:insertAttribute name="includes"/>
</head>

<body>

<div id="container">

    <div id="headerWrapper" class="clearfix">
        <div id="header">
           <tiles:insertAttribute name="header"/>
        </div>
    </div>

    <div id="contentWrapper" class="clearfix">
        <div class="rightColumn">
            <div class="rightColumnIndent${requestScope['displaySettings'].wideScreen}">
                <div id="content">
                   <%-- <app:webcontent>--%>
                       <tiles:insertAttribute name="errors"/>
                       <tiles:insertAttribute name="messages"/>
                       <tiles:insertAttribute name="content">
                            <c:if test="${pageScope['imports']!=null}">
                                <c:forEach var="_import" items="${pageScope['imports']}">
                                    <tiles:putAttribute name="${_import.value}" value="${pageScope[_import.value]}"/>
                                </c:forEach>
                            </c:if>
                        </tiles:insertAttribute>
                <%--    </app:webcontent>--%>
                </div>
            </div>
        </div>
        <div id="menuColumn" class="leftColumn${requestScope['displaySettings'].wideScreen}">
            <div class="leftColumnIndent">
                <div id="menu" class="${requestScope['displaySettings'].wideScreen}">
                    <tiles:insertAttribute name="menu"/>
                </div>
            </div>
        </div>


    </div>


    <div id="footerWrapper" class="clearfix">
        <div id="footer">
            <tiles:insertAttribute name="footer"/>
        </div>
    </div>

</div>

</body>
</html>
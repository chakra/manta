<%@ page import="com.espendwise.manta.util.alert.MessageType" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<app:webMessages var="successMessages" type="<%=MessageType.SUCCESS_MESSAGE%>"/>
<c:if test="${not empty successMessages}">
    <div class="info clearfix">
      <div class="webSuccessMessages" id="webSuccessMessagesDiv">
      <c:forEach var="message"  items="${successMessages}">
          <div class="webSuccessMessage"><c:out value="${message}"/></div>
      </c:forEach>
      </div>
    </div>
</c:if>
<app:webMessages var="messages" type="<%=MessageType.MESSAGE%>"/>
<c:if test="${not empty messages}">
    <div class="info clearfix">
      <div class="webMessages">
        <c:forEach var="message"  items="${messages}">
              <div class="webMessage"><c:out value="${message}"/></div>
        </c:forEach>
    </div>
   </div>
</c:if>
<app:webMessages var="messages" type="<%=MessageType.WARNING%>"/>
<c:if test="${not empty messages}">
    <div class="warning clearfix">
        <div class="webWarnings">
            <c:forEach var="message"  items="${messages}">
                <div class="webWarning"><c:out value="${message}"/></div>
            </c:forEach>
        </div>
    </div>
</c:if>
    
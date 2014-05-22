<%@ taglib uri="/WEB-INF/tld/application.tld/" prefix="app" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="canvas">

    <div class="row clearfix">
        <div class="cell">&nbsp</div>
        <div class="cell label"><app:message code="admin.template.email.label.templateId"/><span class="colon">:</span></div>
        <div class="cell"><div class="labelValue"><c:out value="${emailTemplateHeader.id}" default="0"/></div></div>
        <div class="cell cell-space">&nbsp</div>
        <div class="cell label"><app:message code="admin.template.email.label.templateName"/><span class="colon">:</span></div>
        <div class="cell"><div class="labelValue"><c:out value="${emailTemplateHeader.shortDesc}"/></div></div>
    </div>

</div>
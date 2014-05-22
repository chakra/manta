<%@ page import="com.espendwise.manta.util.AppResource" %>
<%@ page import="com.espendwise.manta.model.data.CountryData" %>
<%@ page import="com.espendwise.manta.model.data.LanguageData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.eSpendWise.com/taglibs/manta-application-1.0" prefix="app" %>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Comparator"%>


<c:set var="oceanWebHome" value="${resources}/ocean"/>

<app:url var="baseUtl"/>

<c:set var="updateAction" value="$('form:first').attr('action','${baseUtl}/emailTemplate/${emailTemplate.templateId > 0?emailTemplate.templateId: 0}');$('form:first').submit();"/>
<c:set var="deleteAction" value="$('form:first').attr('action','${baseUtl}/emailTemplate/${emailTemplate.templateId}/delete');$('form:first').submit();"/>
<c:set var="cloneAction" value="$('form:first').attr('action','${baseUtl}/emailTemplate/0/clone');$('form:first').submit(); "/>
<c:set var="changeEmailType" value="$('form:first').attr('action','${baseUtl}/emailTemplate/${emailTemplate.templateId > 0?emailTemplate.templateId: 0}/changeEmailType');$('form:first').submit();"/>
<c:set var="changeEmailObject" value="$('form:first').attr('action','${baseUtl}/emailTemplate/${emailTemplate.templateId > 0?emailTemplate.templateId: 0}/changeEmailObject');$('form:first').submit();"/>
<c:set var="preview" value="${baseUtl}/emailTemplate/${emailTemplate.templateId > 0?emailTemplate.templateId: 0}/preview"/>

<link href="${oceanWebHome}/css/email_preview.css" rel="Stylesheet" type="text/css" media="all">

<script type="text/javascript">

    function createPreviewLayerData() {

        var m = {};

        m.labelClose = '<app:message code="admin.global.button.close"/>';
        m.labelTitle = '<app:message code="admin.template.email.preview.title"/>';
        m.onFetch = emailPreviewFetch;
        m.layer= "${oceanWebHome}/html/email_preview.html";
        m.previewStyle="popUpEmailPreview";

        m.fetch = function (complete, fail) {

            $.ajax({ url:"${preview}", dataType:"json", cache:false, async:true,
                        data:$("#emailTemplate").serialize(),
                        type: 'POST',
                        success:function (value) {  if (complete) {complete(value);}},
                        error:function () { if (fail) {fail();} }
                    }
            );

        };

        return  m;
    }

</script>

<div class="canvas">

    <div class="details">

        <form:form modelAttribute="emailTemplate" action="" id="emailTemplate" method="POST">

            <form:hidden path="templateType" value="${emailTemplate.templateType}"/>
            <table>
                <tr><td style="vertical-align: top">
                    <table>
                        <tbody>
                        <tr>
                            <td><div class="label"><form:label path="templateId"><app:message code="admin.template.email.label.templateId"/><span class="colon">:</span></form:label></div></td>
                            <td><div class="labelValue"><c:out value="${emailTemplate.templateId}" default="0"/></div> <form:hidden path="templateId" value="${emailTemplate.templateId}"/></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="templateName"><app:message code="admin.template.email.label.templateName"/><span class="colon">:</span></form:label><span class="reqind">*</span></div></td>
                            <td><form:input tabindex="1" path="templateName" maxlength="128" style="width:282px"/></td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="emailObject"><app:message code="admin.template.email.label.emailObject"/><span class="colon">:</span><span class="reqind">*</span></form:label></div></td>
                            <td>
                                <form:select tabindex="2" path="emailObject" style="width:290px" onchange="${changeEmailObject}">
                                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                                    <c:forEach var="obj" items="${requestScope['appResource'].dbConstantsResource.emailObjects}">
                                        <form:option value="${obj.name}"><app:message code="admin.template.email.emailObject.${obj.simpleName}" text="${obj.simpleName}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        </tr>
                        <c:set var="emailTypes" value="${requestScope['appResource'].dbConstantsResource.emailTypesByMetaObject[(emailTemplate.emailObject!=null?emailTemplate.emailObject:'')]}"/>
                        <%
                        	Map emailTypeMap=new TreeMap(new Comparator<String>() { // case insensitive 
                                public int compare(String f1, String f2)
                                {
                                	return f1.compareToIgnoreCase(f2);
                                }        
                            });
                        	List<String> emailTypes = (List<String>)pageContext.getAttribute("emailTypes");
                        	if (emailTypes != null){
                        		for (String emailType : emailTypes){
                        			String emailTypeValue = AppI18nUtil.getMessageOrDefault("admin.template.email.emailType."+emailType, emailType);
                        			emailTypeMap.put(emailTypeValue, emailType);
                        		}
                        	}
                        	pageContext.setAttribute("emailTypeMap", emailTypeMap);
                        %>
                        <tr>
                            <td><div class="label"><form:label path="emailTypeCode"><app:message code="admin.template.email.label.emailTypeCode"/><span class="colon">:</span><span class="reqind">*</span></form:label></div></td>
                            <td>
                                <form:select tabindex="3" path="emailTypeCode" style="width:290px" onchange="${changeEmailType}">
                                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                                    <c:forEach var="emailType" items="${emailTypeMap}">
                                        <form:option value="${emailType.value}">${emailType.key}</form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="templateLocaleCode"><app:message code="admin.template.email.label.templateLocaleCode"/><span class="colon">:</span><span class="reqind">*</span></form:label></div></td>
                            <td>
                                <form:select tabindex="4" path="templateLocaleCode" style="width:290px">
                                    <form:option value=""><app:message code="admin.global.select"/></form:option>
                                    <form:option value="${requestScope['appResource'].dbConstantsResource.localeDefaultCd.object2}"><app:message code="admin.global.select.default"/></form:option>
                                    <c:forEach var="locale" items="${requestScope['appResource'].dbConstantsResource.locales}">
                                        <form:option value="${locale.object2}"><c:out value="${locale.object2}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </td>
                        </tr>
                        <tr>
                            <td><div class="label"><form:label path="templateSubject"><app:message code="admin.template.email.label.templateSubject"/><span class="colon">:</span></form:label></div></td>
                            <td><form:input tabindex="5" path="templateSubject"  size="110"/></td>
                        </tr>
                        <tr>
                            <td  style="vertical-align: top"><div class="areaLabel"><form:label path="templateContent"><app:message code="admin.template.email.label.templateBody"/><span class="colon">:</span><span class="reqind">*</span></form:label></div></td>
                            <td><form:textarea tabindex="6" path="templateContent"  cols="10" rows="10" cssStyle="width:100%;height:600px;"/></td>
                        </tr>

                        <tr>
                            <td colspan="2" align="right">
                                <c:if test="${emailTemplate.templateId!=null && emailTemplate.templateId>0}">
                                    <c:if test="${emailTemplate.isServiceTemplate == true}">
                                        <form:label path="emailTypeCode"><app:message code="admin.template.email.label.serviceTicketId"/><span class="colon">:</span></form:label>
                                        <form:input tabindex="7" path="previewId" />
                                        <form:button tabindex="8"  id="previewAction" onclick="return previewLayer.preview(this, createPreviewLayerData());">
                                            <app:message code="admin.global.button.preview"/>
                                        </form:button>
                                    </c:if>
                                </c:if>
                            </td>
                        </tr>
                        </tbody>
                        <tbody>
                        <tr>
                            <td colspan="2"> <br><br> </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td align="center">
                                <form:button  tabindex="9" onclick="${updateAction} return false;"><app:message code="admin.global.button.save"/></form:button>
                                <c:if test="${emailTemplate.templateId!=null && emailTemplate.templateId>0}">
                                    <form:button style="margin-left:20px" tabindex="10" onclick="${cloneAction} return false;"><app:message code="admin.global.button.clone"/></form:button>
                                    <form:button style="margin-left:20px" tabindex="11"  onclick="${deleteAction} return false;"><app:message code="admin.global.button.delete"/></form:button>
                                </c:if>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
                    <td style="vertical-align: top"><table>
                        <tr>
                            <td style="vertical-align: top" rowspan="7">
                                <c:if test ="${emailTemplate.emailObject!=null && not empty emailTemplate.emailObject}">
                                    <app:box name="admin.template.email.label.help">
                                        <app:emailObjectHelp target="${emailTemplate.emailObject}"/>
                                    </app:box>
                                </c:if>
                            </td>
                        </tr>
                    </table></td></tr>
                <tr>

            </table>
        </form:form>
    </div>



</div>


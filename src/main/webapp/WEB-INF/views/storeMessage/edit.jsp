<%@ page import="com.espendwise.manta.util.AppResource"%>
<%@ page import="com.espendwise.manta.model.data.CountryData"%>
<%@ page import="com.espendwise.manta.model.data.LanguageData"%>
<%@ page import="com.espendwise.manta.util.RefCodeNames"%>
<%@ page import="com.espendwise.manta.web.util.AppI18nUtil"%>
<%@ taglib
	uri="http://java.sun.com/jsp/jstl/core"
	prefix="c"%>
<%@ taglib
	prefix="fn"
	uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib
	prefix="form"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib
	uri="http://www.eSpendWise.com/taglibs/manta-application-1.0"
	prefix="app"%>

<app:dateIncludes />

<link
	href="${resources}/ocean/css/message_preview.css"
	rel="Stylesheet"
	type="text/css"
	media="all">

<script type="text/javascript">

	function isConfirmPublish(){

		var ack = $('input:radio[name=messageType]:checked').val();

		if(ack == '<%=RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED%>'){
			return confirm('<app:message code="admin.message.text.confirmPublish"/>');
		}
		return true;
	}

	function isConfirmDelete(){

		return confirm('<app:message code="admin.message.text.confirmDelete"/>');

	}

	function createPreviewLayerData(index) {

		var m = {};

		var data = {};
		data.title = $('#messageTitle'+index).attr('value');
		data.messageAbstract = $('#messageAbstract'+index).attr('value');
		data.messageBody = CKEDITOR.instances['messageBody'+index].getData();
		data.author = $('#messageAuthor'+index).attr('value');
		data.posted = $('#postedDate').attr('value');
		data.labelFrom = '<app:message code="admin.message.preview.label.from"/>';

		m.labelClose = '<app:message code="admin.global.button.close"/>';
		m.labelTitle = '<app:message code="admin.message.preview.title"/>';
		m.possitionTop = 70;
		m.showOnTop = true;

		m.previewStyle = "popUpMedium";
		m.layer = "${resources}/ocean/html/message_preview.html";

		m.fetch = function(complete, fail) {
			complete(data);
		};
		m.onFetch = messagePreviewFetch;

		return m;
	}

	function toggle(object) {
		var togElement = document.getElementById('toggleDiv');
		if (object.value == '<%=RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ%>')  {
			togElement.style.visibility = 'visible';
			var forceReadInputEl = document.getElementById('forcedReadCount');
			forceReadInputEl.value = "";
		} else
			togElement.style.visibility = 'hidden';
	}

</script>


<app:url var="baseUtl" />
<c:set
	var="ackStr"
	value="<%=RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED%>" />
<c:set
	var="forceReadStr"
	value="<%=RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ%>" />
<c:set
	var="updateAction"
	value="$('form:first').attr('action','${baseUtl}/storeMessage/${storeMessage.storeMessageId > 0?storeMessage.storeMessageId: 0}');$('form:first').submit();return false;" />
<c:set
	var="publishAction"
	value="$('form:first').attr('action','${baseUtl}/storeMessage/${storeMessage.storeMessageId}/publish'); $('form:first').submit(); return true;return false;" />
<c:set
	var="cloneAction"
	value="$('form:first').attr('action','${baseUtl}/storeMessage/0/clone');$('form:first').submit(); return false;" />
<c:set
	var="addTranslationAction"
	value="$('form:first').attr('action','${baseUtl}/storeMessage/${storeMessage.storeMessageId > 0?storeMessage.storeMessageId: 0}/addTranslation');$('form:first').submit();return false;" />

<c:set
	var="disabled"
	value="${ storeMessage.storeMessageId > 0 ? 'false':'true'}" />

<c:set
	var="readOnlyDate"
	value="${storeMessage.published==true}" />

<c:set
	var="calendarCss"
	value="${storeMessage.published==true?'standardReadOnlyCal':'standardActiveCal'}" />

<c:set
	var="readOnly"
	value="${storeMessage.published==true}" />

<c:set
	var="readonlyCss"
	value="${readOnly ? 'readonly':''}" />

<c:set
	var="forcedRead"
	value="${forceReadStr.equals(storeMessage.messageType) ?'visible':'hidden'}" />

<c:set
	var="ackMsg"
	value="${ackStr.equals(storeMessage.messageType) ? 'true':'false'}" />

<c:set
	var="ackPublished"
	value="${storeMessage.storeMessageId > 0 && storeMessage.published==true && ackStr.equals(storeMessage.messageType)}" />

<c:set
	var="ackReadonlyCss"
	value="${ackPublished ? 'readonly' : ''}" />

<c:set
	var="ackCalendarCss"
	value="${ackPublished ? 'standardReadOnlyCal' : 'standardActiveCal'}" />

<c:set
    var="addTranslationReadonlyClass"
    value="${(disabled || ackPublished)?'disabled':''}"/>


<%
String publishActionStr = (String)pageContext.getAttribute("publishAction");
String confirmPublishVal = "if(isConfirmPublish()) {"+publishActionStr + "} else {return false;};";
%>
<c:set
	var="confirmPublishAction"
	value="<%=confirmPublishVal %>" />

<div class="canvas">

	<div class="details">

		<form:form
			modelAttribute="storeMessage"
			action="${baseUtl}/storeMessage/${storeMessage.storeMessageId > 0?storeMessage.storeMessageId: 0}"
			method="POST">

			<table
				cellspacing="0"
				border="0"
				width="890px">

				<tr>
					<td width="1%">
						<div class="label">

							<!--<c:out value="${readonlyCss}" />-->
							<form:label path="storeMessageId">
								<app:message code="admin.message.label.storeMessageId" />
								<span class="colon">:</span>
							</form:label>
						</div>
					</td>
					<td>
						<div class="labelValue">
							<c:out
								value="${storeMessage.storeMessageId}"
								default="0" />
						</div>
						<form:hidden
							path="storeMessageId"
							value="${storeMessage.storeMessageId}" />
					</td>
					<td></td>
				</tr>

				<!--  ########  Name ############ -->
				<tr>
					<td>
						<div class="label">
							<form:label path="name">
								<app:message code="admin.message.label.name" />
								<span class="colon">:</span>
							</form:label>
							<span class="reqind">*</span>
						</div>
					</td>
					<td>
						<form:input
							id="name"
							tabindex="1"
							path="name"
							maxlength="128"
							cssClass="inputShort"
							readonly="${ackPublished}"/>
					</td>
					<td
						rowspan="4"
						style="vertical-align: top;">
						<table>
							<tbody>
								<tr>
									<td>
										<div class="box">
											<div class="boxTop">
												<div class="topWrapper">
													<span class="left">&nbsp;</span>
													<span
														class="center"
														style="">
														<span class="boxName">
															<app:message code="admin.message.label.messageType" />
															<span class="reqind">*</span>
														</span>
													</span>
													<span class="right">&nbsp;</span>
												</div>
											</div>
											<div class="boxMiddle">
												<div class="middleWrapper">
													<span class="left">&nbsp;</span>
													<div class="boxContent">
														<table
															cellpadding="0"
															cellspacing="0"
															border="0">
															<tbody>
																<c:forEach
																	var="messageType"
																	items="${requestScope['appResource'].dbConstantsResource.messageTypes}">
																	<tr>
																		<td>
																			<form:radiobutton
																				tabindex="6"
																				class="radio"
																				path="messageType"
																				value="${messageType.object2}"
																				onclick="toggle(this)"
																				disabled="${readOnly}" />
																		</td>
																		<td class="label">
																			<app:message
																				code="refcodes.MESSAGE_TYPE_CD.${messageType.object1}"
																				text="${messageType.object2}" />
																		</td>
																	</tr>
																</c:forEach>
																<c:if test="${readOnly  == true}">
																	<tr>
																		<td colspan="2">
																			<form:hidden path="messageType" />
																		</td>
																	</tr>
																</c:if>
															</tbody>
														</table>
													</div>
													<span class="right">&nbsp;</span>
												</div>
											</div>
											<div class="boxBottom">
												<div class="bottomWrapper">
													<span class="left">&nbsp;</span>
													<span class="center">&nbsp;</span>
													<span class="right">&nbsp;</span>
												</div>
											</div>
										</div>
									</td>

									<td>

										<div
											id="toggleDiv"
											class="boxMiddle"
											style="visibility:${forcedRead};">
											<app:message code="admin.message.label.forcedReadCount" />
											<span class="colon">:</span>
											<span class="reqind">*</span>
											<%--<c:out value="${forcedRead}" /> --%>
											<form:input
												id="forcedReadCount"
												tabindex="6"
												path="forcedReadCount"
												size="4" />

										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>


				<tr>
					<td>
						<div class="label">
							<form:label path="postedDate">
								<app:message code="admin.message.label.postingDate" />
								<span class="colon">:</span>
							</form:label>
						</div>
					</td>
					<td>
						<nobr>
							<form:input
								id="postedDate"
								tabindex="2"
								path="postedDate"
								cssClass="inputShort datepicker2Col standardCal ${calendarCss} ${readonlyCss}"
								maxlength="10"

								readonly="${readOnlyDate}" />
						</nobr>
					</td>


				</tr>
				<tr>
					<td>
						<div class="label">
							<form:label path="endDate">
								<app:message code="admin.message.label.endDate" />
								<span class="colon">:</span>
							</form:label>
						</div>
					</td>
					<td>
						<nobr>
							<form:input
								path="endDate"
								tabindex="3"
								cssClass="inputShort datepicker2Col standardCal ${ackCalendarCss} ${ackReadonlyCss}"
								maxlength="10"
								id="endDate"
								readonly="${ackPublished}"/>
						</nobr>
					</td>

				</tr>

				<%-- DISPLAY ORDER  --%>
				<tr>
					<td>
						<div class="label">
							<form:label path="displayOrder">
								<app:message code="admin.message.label.displayOrder" />
								<span class="colon">:</span>
							</form:label>
						</div>
					</td>
					<td>
						<form:input
							id="displayOrder"
							tabindex="4"
							path="displayOrder"
							maxlength="9"
							cssClass="inputShort"
							readonly="${ackPublished}"/>
					</td>

				</tr>


				<tr>
					<td>
						<div class="label">
							<form:label path="storeMessageStatusCd">
								<app:message code="admin.message.label.status" />
								<span class="colon">:</span>
							</form:label>
							<span class="reqind">*</span>
						</div>
					</td>
					<td>
						<form:select
							tabindex="5"
							path="storeMessageStatusCd" cssClass="selectShort">
							<!--
							<form:option value="">
								<app:message code="admin.global.select" />
							</form:option>
 -->
							<form:option value="ACTIVE">
								<app:message code="refcodes.STORE_MESSAGE_STATUS_CD.ACTIVE" />
							</form:option>
							<form:option value="INACTIVE">
								<app:message code="refcodes.STORE_MESSAGE_STATUS_CD.INACTIVE" />
							</form:option>

						</form:select>
					</td>

				</tr>

				<!--  ########  buttons ############ -->

				<tr>
					<td
						colspan="3"
						align="center">
						<form:button
							tabindex="14"
							onclick="${updateAction}">
							<app:message code="admin.global.button.save" />
						</form:button>&nbsp;&nbsp;&nbsp;

					<c:if
						test="${ storeMessage.storeMessageId!=null && storeMessage.storeMessageId>0}">
							<c:if test="${storeMessage.published  == true}">
								<form:button
									tabindex="15"
									onclick="return false"
									class="disabled"
									disabled="true">
									<app:message code="admin.global.button.publish" />
								</form:button>
							</c:if>
							<c:if test="${storeMessage.published  == false}">
									<form:button
                                        type="button"
										tabindex="16"
										onclick="${confirmPublishAction}">
										<app:message code="admin.global.button.publish" />
									</form:button>
							</c:if>

					</c:if>

					</td>

				</tr>
				<tr>
					<td
						colspan="3"
						align="center">
						<hr />

					</td>

				</tr>

				<c:forEach
					varStatus="i"
					var="detail"
					items="${storeMessage.detail}">
					<jsp:include page="../ckeditor.jsp"><jsp:param
							name="id"
							value="messageBody${i.index}" /><jsp:param
							name="readonly"
							value="${ackPublished}" /></jsp:include>
					<tr>
						<td colspan="3">
							<form:hidden
								path="detail[${i.index}].storeMessageDetailId"
								value="${detail.storeMessageDetailId}" />

							<form:hidden
								path="detail[${i.index}].messageDetailTypeCd"
								value="${detail.messageDetailTypeCd}" />
						</td>

					</tr>

					<!--  ########  Abstract.. ############ -->
					<tr>
						<td colspan="3">
							<table width="100%">
								<tr>
									<td
										width="50%"
										rowspan="2"
										align="left">
										<div class="areaLabel">
											<form:label path="detail[${i.index}].messageAbstract">
												<app:message code="admin.message.label.messageAbstract" />
												<span class="colon">:</span>
												<span class="reqind">*</span>
											</form:label>
										</div>

										<form:textarea
											id="messageAbstract${i.index}"
											tabindex="7"
											rows="3"
											path="detail[${i.index}].messageAbstract"
											cssStyle="width: 100%;height: 100px"
											readonly="${ackPublished}"/>

									</td>

									<td width="1%">
										<div class="label">
											<form:label path="detail[${i.index}].messageTitle">
												<app:message code="admin.message.label.storeMessageTitle" />
												<span class="colon">:</span>
											</form:label>
											<span class="reqind">*</span>
										</div>
									</td>
									<td>
										<form:input
											id="messageTitle${i.index}"
											tabindex="8"
											path="detail[${i.index}].messageTitle"
											maxlength="128"
											size="25"
											readonly="${ackPublished}"/>
									</td>

									<c:if test="${storeMessage.storeMessageId > 0}">
									<c:if test="${empty detail.messageDetailTypeCd}">
									<td>
										<div class="label">
											<form:label path="detail[${i.index}].languageCd">
												<app:message code="admin.message.label.language" />
												<span class="colon">:</span>
												<span class="reqind">*</span>
											</form:label>
										</div>
									</td>
									<td>
										<form:select
											cssClass="${readonlyCss}"
											tabindex="10"
											path="detail[${i.index}].languageCd"
											readonly="${storeMessage.published  == true}">
											<form:option value="">
												<app:message code="admin.global.select" />
											</form:option>
											<c:forEach
												var="language"
												items="${requestScope['appResource'].dbConstantsResource.languages}">
												<form:option value="${language.languageCode}">${language.uiName}</form:option>
											</c:forEach>
										</form:select>

									</td>
									</c:if>
									</c:if>

								</tr>
								<tr>

									<td width="1%">
										<div class="label">
											<form:label path="detail[${i.index}].messageAuthor">
												<app:message code="admin.message.label.author" />
												<span class="colon">:</span>
												<span class="reqind">*</span>
											</form:label>
										</div>
									</td>
									<td>
										<form:input
											id="messageAuthor${i.index}"
											tabindex="9"
											path="detail[${i.index}].messageAuthor"
											maxlength="60"
											size="25"
											readonly="${ackPublished}"/>

									</td>

									<c:if test="${storeMessage.storeMessageId > 0}">
									<c:if test="${empty detail.messageDetailTypeCd}">
									<td>
										<div class="label">
											<form:label path="detail[${i.index}].country">
												<app:message code="admin.message.label.country" />
												<span class="colon">:</span>
										</div>
										</form:label>
									</td>
									<td>
										<form:select
											cssClass="${readonlyCss}"
											tabindex="11"
											path="detail[${i.index}].country"
											readonly="${storeMessage.published  == true}">
											<form:option value="">
												<app:message code="admin.global.select" />
											</form:option>
											<c:forEach
												var="country"
												items="${requestScope['appResource'].dbConstantsResource.countries}">
												<form:option value="${country.countryCode}">${country.uiName}</form:option>
											</c:forEach>
										</form:select>

									</td>
									</c:if>
									</c:if>
								</tr>
							</table>
						</td>

					</tr>

					<tr>
						<td
							colspan="2"
							align="left">
							<div class="areaLabel">
								<form:label path="detail[${i.index}].messageBody">
									<app:message code="admin.message.label.messageBody" />
									<span class="colon">:</span>
								</form:label>
							</div>
						</td>


						<td align="right">




							<c:if test="${storeMessage.published  == true}">
								<form:button
									tabindex="17"
									onclick="return false"
									class="disabled"
									disabled="true">
									<app:message code="admin.global.button.preview" />
								</form:button>
							</c:if>
							<c:if test="${storeMessage.published  == false}">
								<form:button
									tabindex="18"
									id="previewAction"
									disabled="${disabled}"
									onclick="return previewLayer.preview(this, createPreviewLayerData(${i.index}))">
									<app:message code="admin.global.button.preview" />
								</form:button>
							</c:if>



						</td>


					</tr>
					<tr>
						<td colspan="3">

							<form:textarea
								id="messageBody${i.index}"
								tabindex="13"
								path="detail[${i.index}].messageBody"
								cols="10"
								rows="10"
								cssStyle="width:100%;height:350px;"/>
						</td>

					</tr>


					<tr>
						<td
							colspan="3"
							align="left">


							<c:if test="${i.index  == fn:length(storeMessage.detail)  -1 }">
								<form:button
									tabindex="14"
									class="${addTranslationReadonlyClass}"
									disabled="${disabled || ackPublished}"
									onclick="${addTranslationAction} return false;">
									<app:message code="admin.global.button.addTranslation" />
								</form:button>&nbsp;&nbsp;&nbsp;
							</c:if>

							<c:if test="${i.index  > 0}">
							<c:set
							var="deleteAction"
							value="$('form:first').attr('action','${baseUtl}/storeMessage/${storeMessage.storeMessageId > 0?storeMessage.storeMessageId: 0}/deleteTranslation/${i.index}'); $('form:first').submit(); return true;" />

							<%
							String deleteActionStr = (String)pageContext.getAttribute("deleteAction");
							String confirmDeleteVal = "if(isConfirmDelete()) {"+deleteActionStr + "} else {return false;};";
							%>
							<c:set
								var="confirmDeleteAction"
								value="<%=confirmDeleteVal %>" />

								<form:button
									tabindex="18"
									id="deleteAction"
									class="${readonlyCss}"
									disabled="${readOnly}"
									onclick="${confirmDeleteAction}">
									<app:message code="admin.global.button.deleteTranslation" />
								</form:button>
							</c:if>

						</td>

					</tr>


					<tr>
						<td
							colspan="3"
							align="center">
							<hr />

						</td>

					</tr>
				</c:forEach>
				<tr>
					<td
						colspan="3"
						align="center">
						<form:button
							tabindex="14"
							onclick="${updateAction}">
							<app:message code="admin.global.button.save" />
						</form:button>&nbsp;&nbsp;&nbsp;

						<c:if
							test="${ storeMessage.storeMessageId!=null && storeMessage.storeMessageId>0}">
							<c:if test="${storeMessage.published  == true}">
								<form:button
									tabindex="15"
									onclick="return false"
									class="disabled"
									disabled="true">
									<app:message code="admin.global.button.publish" />
								</form:button>
							</c:if>
							<c:if test="${storeMessage.published  == false}">

								<c:if test="${ackMsg == false}">
									<form:button
										tabindex="16"
										onclick="${publishAction}">
										<app:message code="admin.global.button.publish" />
									</form:button>
								</c:if>
								<c:if test="${ackMsg == true}">
									<form:button
                                        type="button"
										tabindex="16"
										onclick="${confirmPublishAction}">
										<app:message code="admin.global.button.publish" />
									</form:button>
								</c:if>

							</c:if>

							<%--
							<form:button
								onclick="${cloneAction} return false;"
								tabindex="19">
								<app:message code="admin.global.button.clone" />
							</form:button>
							--%>
						</c:if>

					</td>

				</tr>
			</table>
			<form:hidden path="published" />
		</form:form>
	</div>

</div>


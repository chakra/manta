package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.TemplateData;
import com.espendwise.manta.model.data.TemplatePropertyData;
import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.service.TemplateService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.EmailTemplateForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.resolver.EmailTemplateWebUpdateExceptionResolver;
import com.espendwise.ocean.common.emails.EmailValue;
import com.espendwise.ocean.common.emails.GenerateEmailError;
import com.espendwise.ocean.common.emails.GenerateEmailException;
import com.espendwise.ocean.common.emails.TemplateEmailValue;
import com.espendwise.ocean.common.webaccess.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(UrlPathKey.EMAIL_TEMPLATE.IDENTIFICATION)
public class EmailTemplateController extends BaseController {

    private static final String ORCA_SERVICE_TICKET_EMAIL_PREVIEW_PATH_TEMPLATE = "/instance/%s/%s/serviceticket/%s/email/%s/preview/%s";
    private static final Logger logger = Logger.getLogger(EmailTemplateController.class);

    private TemplateService templateService;

    @Autowired
    public EmailTemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @SuccessMessage
    @Clean(SessionKey.EMAIL_TEMPLATE_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm emailTemplateForm) throws Exception {

        logger.info("save()=> BEGIN, emailTemplateForm: " + emailTemplateForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(emailTemplateForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "emailTemplate/edit";
        }

        EmailTemplateIdentView emailTemplateView = new EmailTemplateIdentView();
        if (!emailTemplateForm.getIsNew()) {
            emailTemplateView = templateService.findEmailTemplateIdent(getStoreId(), emailTemplateForm.getTemplateId());
        }

        emailTemplateView.setTemplateData(WebFormUtil.createTemplateData(getStoreId(), emailTemplateView.getTemplateData(), emailTemplateForm));
        emailTemplateView.setProperties(WebFormUtil.createEmailTemplateIdentProperties(emailTemplateView.getProperties(), emailTemplateForm));

        try {
           
            emailTemplateView = templateService.saveEmailTemplateIdent(getStoreId(), emailTemplateView);
       
        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("redirect()=> redirect to: " + emailTemplateView.getTemplateData().getTemplateId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(emailTemplateView.getTemplateData().getTemplateId()));
    }

    @SuccessMessage
    @Clean(SessionKey.EMAIL_TEMPLATE_HEADER)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(WebRequest request, @ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm emailTemplateForm) throws Exception {

        logger.info("delete()=> BEGIN, emailTemplateForm: " + emailTemplateForm);

        WebErrors webErrors = new WebErrors(request);

        if (!emailTemplateForm.getIsNew()) {

            EmailTemplateIdentView emailTemplateView = templateService.findEmailTemplateIdent(getStoreId(), emailTemplateForm.getTemplateId());

            emailTemplateView.setTemplateData(WebFormUtil.createTemplateData(getStoreId(), emailTemplateView.getTemplateData(), emailTemplateForm));
            emailTemplateView.setProperties(WebFormUtil.createEmailTemplateIdentProperties(emailTemplateView.getProperties(), emailTemplateForm));

            Long templateId = emailTemplateView.getTemplateData().getTemplateId();

            if (!templateService.deleteEmailTemplate(getStoreId(), templateId)) {
                webErrors.putError("validation.web.error.emailTemplateCantBeDeleted");
                return "emailTemplate/edit";
            }

            WebFormUtil.removeObjectFromFilterResult(
                    request,
                    SessionKey.EMAIL_TEMPALTE_FILTER_RESULT,
                    templateId
            );

            logger.info("delete()=> END. redirect to filter");
            return redirect("../");

        }

        logger.info("delete()=> END.");
        return "emailTemplate/edit";

    }

    private List<AsynchError> handleWebAccessException(WebAccessException e) {

        if (e != null) {

            List<AsynchError> errors = new ArrayList<AsynchError>();

            if(e.getStatus() == BasicResponseValue.STATUS.ACCESS_DENIED){

                String errTitle = AppI18nUtil.getMessage("admin.template.email.preview.error");
                errors.add(new AsynchError(errTitle, AppI18nUtil.getMessage("admin.template.email.preview.error.accessDenied")));

            }   else if (Utility.isSet(e.getErrors())) {

                for (ResponseError err : e.getErrors()) {

                    String  errTitle;

                    if (GenerateEmailException.class.getName().equalsIgnoreCase(err.getExcClassName())) {

                        GenerateEmailError errorImpl = GenerateEmailError.fromPoperties(err.getErrorProperties());

                        String field = GenerateEmailError.Component.SUBJECT.name().equals(errorImpl.getComponent())
                                ? AppI18nUtil.getMessage("admin.template.email.label.templateSubject")
                                : GenerateEmailError.Component.BODY.name().equals(errorImpl.getComponent())
                                ? AppI18nUtil.getMessage("admin.template.email.label.templateBody")
                                : null;

                        errTitle = AppI18nUtil.getMessage("admin.template.email.preview.error.syntaxErrorIn", Args.i18nTyped(field));

                        errors.add(new AsynchError(errTitle, errorImpl.getMessage()));

                    } else {

                        errTitle = AppI18nUtil.getMessage("admin.template.email.preview.error");
                        errors.add(new AsynchError(errTitle, err.getMessage()));
                        
                    }

                }
          
            } else {

                String errTitle = AppI18nUtil.getMessage("admin.template.email.preview.error");
                errors.add(new AsynchError(errTitle, e.getMessage()));

            }

            return errors;
        }

        return null;
    }

    private String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new EmailTemplateWebUpdateExceptionResolver());

        return "emailTemplate/edit";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm form,
                       @PathVariable("emailTemplateId") Long emailTemplateId,
                       Model model) {

        logger.info("show()=> BEGIN");

        EmailTemplateIdentView emailTemplate = templateService.findEmailTemplateIdent(getStoreId(), emailTemplateId);

        logger.info("show()=> emailTemplate: " + emailTemplate);

        if (emailTemplate != null) {

            TemplateData templateData = emailTemplate.getTemplateData();
            List<TemplatePropertyData> props = emailTemplate.getProperties();

            form.setTemplateId(templateData.getTemplateId());
            form.setTemplateName(templateData.getName());
            form.setTemplateContent(templateData.getContent());
            form.setTemplateType(templateData.getType());

            form.setTemplateLocaleCode(PropertyUtil.toValueNN(props, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.LOCALE));
            form.setTemplateSubject(PropertyUtil.toValueNN(props, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.SUBJECT));
            form.setEmailTypeCode(PropertyUtil.toValueNN(props, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_TYPE));
            form.setEmailObject(PropertyUtil.toValueNN(props, RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_OBJECT));

        }

        logger.info("show()=> form: " + form);

        model.addAttribute(SessionKey.EMAIL_TEMPLATE, form);

        logger.info("show()=> END.");

        return "emailTemplate/edit";

    }

    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.EMAIL_TEMPLATE)  EmailTemplateForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new EmailTemplateForm();
        form.setTemplateType(RefCodeNames.TEMPLATE_TYPE_CD.EMAIL);
        form.initialize();

        model.addAttribute(SessionKey.EMAIL_TEMPLATE, form);

        logger.info("create()=> END.");

        return "emailTemplate/edit";

    }

    @ResponseBody
    @RequestMapping(value = "/preview", produces="application/json; charset=UTF-8")
    public String preview(@ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm form, Model model) throws Exception {

        logger.info("preview()=> BEGIN");

        String response;

        form.setPreviewRequest(true);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {

            List<AsynchError> errors = handleValidationErrorsForAsync(validationErrors);
            response = new Gson().toJson(new Pair<EmailValue, List<AsynchError>>(null, errors));

        } else {

            try {

                LoginCredential orcaLoginCredential = getAppUser().getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();
                if (orcaLoginCredential == null) {
                    getUserManager().logonToOrca(getAuthUser());
                }
                
                EmailValue emailValue = serviceTicketEmailPreview(form);
                response = new Gson().toJson(new Pair<EmailValue, List<AsynchError>>(emailValue, null));

            } catch (WebAccessException e) {

                List<AsynchError> errors = handleWebAccessException(e);
                response = new Gson().toJson(new Pair<EmailValue, List<AsynchError>>(null, errors));

            } catch (WebAccessResponseException e) {

                List<AsynchError> errors = handleWebAccessResponseException(e);
                response = new Gson().toJson(new Pair<EmailValue, List<AsynchError>>(null, errors));

            }
        }

        logger.info("preview()=> END, response: " + response);

        return response;
    }

    private List<AsynchError> handleWebAccessResponseException(WebAccessResponseException e) {

        AsynchError error = new AsynchError();

        error.setErrorTitle(AppI18nUtil.getMessage("admin.template.email.preview.error"));
        error.setErrorMessages(new String[]{AppI18nUtil.getMessage("admin.template.email.preview.error.previewNotAvailable")});

        return Utility.toList(error);
    }

    private List<AsynchError> handleValidationErrorsForAsync(List<? extends ArgumentedMessage> validationErrors) {

        AsynchError error = new AsynchError();

        error.setErrorTitle(AppI18nUtil.getMessage("admin.template.email.preview.error.formError"));

        List<String> msgList = Utility.emptyList(String.class);
        for (ArgumentedMessage e : validationErrors) {
            msgList.add(new WebError(e).resolve(getAppLocale()));
        }

        error.setErrorMessages(msgList.toArray(new String[msgList.size()]));

        return Utility.toList(error);
    }


    public EmailValue serviceTicketEmailPreview(EmailTemplateForm form) throws Exception {

        logger.info("serviceTicketEmailPreview()=> BEGIN");

        EmailValue emailValue = new EmailValue(Constants.EMPTY, Constants.EMPTY);

        AppUser user = getAppUser();
        
        LoginCredential loginCredential = user.getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();
        logger.info("serviceTicketEmailPreview()=> loginCredential: "+loginCredential);

        if (loginCredential != null) {

            AppStoreContext storeContext = user.getContext(AppCtx.STORE);

            String dsUnit = storeContext.getDataSource().getDataSourceIdent().getDataSourceName();
            Long storeId = storeContext.getStoreId();

            String requestPath = String.format(ORCA_SERVICE_TICKET_EMAIL_PREVIEW_PATH_TEMPLATE,
                    dsUnit,
                    storeId.toString(),
                    form.getPreviewId(),
                    form.getEmailTypeCode(),
                    form.getTemplateLocaleCode()
            );

            logger.info("serviceTicketEmailPreview()=> hostAddress:  " + loginCredential.getHostAddress());
            logger.info("serviceTicketEmailPreview()=> requestPath:  " + requestPath);
            logger.info("serviceTicketEmailPreview()=> accessToken:  " + loginCredential.getAccessToken());

            RestRequest request = new RestRequest(loginCredential.getHostAddress(), requestPath);

            try {

                BasicRequestValue<TemplateEmailValue> requestObject = new BasicRequestValue<TemplateEmailValue>();

                requestObject.setLoginCredential(loginCredential);
                requestObject.setObject(new TemplateEmailValue(form.getTemplateSubject(), form.getTemplateContent()));

                emailValue = request.doPut(requestObject, new ObjectTokenType<BasicResponseValue<EmailValue>>(){});

            } catch (WebAccessException e) {

                logger.info("serviceTicketEmailPreview()=> error:  " + e.getMessage());

                if (e.getStatus() == BasicResponseValue.STATUS.ACCESS_TOKEN_EXPIRED) {

                    logger.info("serviceTicketEmailPreview()=> relogin ... ");

                    getUserManager().logonToOrca(getAuthUser());

                    loginCredential =  user.getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();

                    if (loginCredential != null) {

                        logger.info("serviceTicketEmailPreview()=> relogin, OK!");
                        logger.info("serviceTicketEmailPreview()=> try again get email value");

                        BasicRequestValue<TemplateEmailValue> requestObject = new BasicRequestValue<TemplateEmailValue>();

                        requestObject.setLoginCredential(loginCredential);
                        requestObject.setObject(new TemplateEmailValue(form.getTemplateSubject(), form.getTemplateContent()));

                        request = new RestRequest(loginCredential.getHostAddress(), requestPath);

                        emailValue = request.doPut(requestObject, new ObjectTokenType<BasicResponseValue<EmailValue>>() {});
                    }

                } else {

                    throw e;
                }
            }

        }

        logger.info("serviceTicketEmailPreview()=> END, emailValue: " + emailValue);

        return emailValue;

    }


    @RequestMapping(value = "/clone")
    public String clone(@ModelAttribute(SessionKey.EMAIL_TEMPLATE)  EmailTemplateForm form, Model model) throws Exception {

        logger.info("clone()=> BEGIN");
        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getTemplateName()});

        form.setTemplateId(null);
        form.setTemplateName(title);

        model.addAttribute(SessionKey.EMAIL_TEMPLATE, form);

        logger.info("clone()=> END.");

        return "emailTemplate/edit";

    }

    @RequestMapping(value = "/changeEmailObject")
    public String changeEmailObject(@ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm form, Model model) {

        logger.info("changeEmailObject()=> BEGIN");

        form.setEmailTypeCode(null);
        model.addAttribute(SessionKey.EMAIL_TEMPLATE, form);

        logger.info("changeEmailObject()=> END.");

        return "emailTemplate/edit";

    }

    @RequestMapping(value = "/changeEmailType")
    public String changeEmailType(@ModelAttribute(SessionKey.EMAIL_TEMPLATE) EmailTemplateForm form, Model model) {

        logger.info("changeEmailType()=> BEGIN");

        form.setEmailObject(null);

        Map<String, List<String>> emailTypesEntry = AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getEmailTypesByMetaObject();

        for (Map.Entry<String, List<String>> e : emailTypesEntry.entrySet()) {
            if (new HashSet<String>(e.getValue()).contains(form.getEmailTypeCode())) {
                if (Utility.isSet(e.getKey())) {
                    form.setEmailObject(e.getKey());
                    break;
                }
            }
        }

        model.addAttribute(SessionKey.EMAIL_TEMPLATE, form);

        logger.info("changeEmailType()=> END.");

        return "emailTemplate/edit";

    }


    @ModelAttribute(SessionKey.EMAIL_TEMPLATE)
    public EmailTemplateForm initModel() {

        EmailTemplateForm form = new EmailTemplateForm();
        form.setPreviewRequest(false);
        form.initialize();

        return form;

    }
}

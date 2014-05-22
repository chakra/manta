package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.web.forms.SiteSiteHierarchyForm;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class SiteSiteHierarchyFormValidator extends AbstractFormValidator{
    private static final Logger logger = Logger.getLogger(SiteSiteHierarchyFormValidator.class);

    @Override
    public boolean supports(Class aClass) {
        logger.info("supports()=> aClass:" + aClass + "(" + aClass.isAssignableFrom(SiteSiteHierarchyForm.class) + ")");
        return aClass.isAssignableFrom(SiteSiteHierarchyForm.class);
    }

    @Override
    public ValidationResult validate(Object obj) {

        logger.info("validate()=> BEGIN");

        WebErrors errors = new WebErrors();

        SiteSiteHierarchyForm form =(SiteSiteHierarchyForm)obj;

        logger.info("validate()=> selected level items: " + form.getSelectedLevelItems());
        logger.info("validate()=> selected key: " + form.getSelectedLevelItems().lastKey());
        logger.info("validate()=> selected value: " + form.getSelectedLevelItems().get(form.getSelectedLevelItems().lastKey()));
        logger.info("validate()=> level.size: " + form.getLevels().size());

        if (form.getLevels().size() != form.getSelectedLevelItems().size()) {
            errors.add(new WebError("validation.web.error.siteHierarchy.error.site.subLevelNotConfigured"));
        } else if (!(Utility.longNN(form.getSelectedLevelItems().get(form.getSelectedLevelItems().lastKey())) > 0)) {
            errors.add(new WebError("validation.web.error.siteHierarchy.error.site.subLevelNotConfigured"));
        }


        logger.info("validate()=> END, errors: "+errors);

        return new MessageValidationResult(errors.get());
    }


}

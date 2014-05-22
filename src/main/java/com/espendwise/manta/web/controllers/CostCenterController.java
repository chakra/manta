package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.CostCenterView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.CostCenterService;
import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CostCenterForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.CostCenterWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;


import java.util.List;


@Controller
@RequestMapping(UrlPathKey.COST_CENTER.IDENTIFICATION)
public class CostCenterController extends BaseController {

    private static final Logger logger = Logger.getLogger(CostCenterController.class);

    private CostCenterService costCenterService;

    @Autowired
    public CostCenterController(CostCenterService costCenterService) {
        this.costCenterService = costCenterService;
    }


    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new CostCenterWebUpdateExceptionResolver());

        return "costCenter/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "costCenter/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.COST_CENTER_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.COST_CENTER) CostCenterForm costCenterForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, costCenterForm: "+costCenterForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(costCenterForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.COST_CENTER, costCenterForm);
            return "costCenter/edit";
        }

        CostCenterView costCenterView = null;

        if (!costCenterForm.isNew()) {
            costCenterView = costCenterService.findCostCenterToEdit(getStoreId(),
                    costCenterForm.getCostCenterId()
                    );
        }
        if (costCenterView == null) {
            costCenterView = new CostCenterView();
            costCenterView.setCostCenterData(new CostCenterData());
        }

        CostCenterData costCenterData = costCenterView.getCostCenterData();

        costCenterData.setShortDesc(costCenterForm.getCostCenterName());
        costCenterData.setCostCenterCode(costCenterForm.getCostCenterCode());
        costCenterData.setCostCenterTypeCd(costCenterForm.getCostCenterType());
        costCenterData.setCostCenterTaxType(costCenterForm.getCostCenterTaxType());
        costCenterData.setCostCenterStatusCd(costCenterForm.getStatus());
        costCenterData.setAllocateFreight(Utility.isTrue(costCenterForm.getAllocateFreight()) ? costCenterForm.getAllocateFreight() : "false");
        costCenterData.setAllocateDiscount(Utility.isTrue(costCenterForm.getAllocateDiscount()) ? costCenterForm.getAllocateDiscount() : "false");
        costCenterData.setNoBudget(Utility.isTrue(costCenterForm.getDoNotApplyBudget()) ? costCenterForm.getDoNotApplyBudget() : "false");

        try {

            costCenterView = costCenterService.saveCostCenter(
                    getStoreId(),
                    costCenterView);

        } catch (ValidationException e) {
        e.printStackTrace();
            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e) {
           e.printStackTrace();
            return handleDatabaseUpdateException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + costCenterView.getCostCenterData().getCostCenterId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(costCenterView.getCostCenterData().getCostCenterId()));
    }



      /*
    @SuccessMessage
    @Clean(SessionKey.COST_CENTER_HEADER)
    @RequestMapping(value = CostCenterForm.ACTION.DELETE, method = RequestMethod.POST)
    public String delete(WebRequest request, @ModelAttribute(SessionKey.COST_CENTER) CostCenterForm costCenterForm) throws Exception {

        logger.info("delete()=> BEGIN, costCenterForm: " + costCenterForm);

        if (!costCenterForm.getIsNew()) {

            try {

                costCenterService.deleteCostCenter(getStoreId(), costCenterForm.getCostCenterId());

            } catch (DatabaseUpdateException e) {

                return handleDatabaseUpdateException(e, request);

            }

            WebFormUtil.removeObjectFromFilterResult(
                    request,
                    SessionKey.COST_CENTER_FILTER_RESULT,
                    costCenterForm.getCostCenterId()
            );

            logger.info("delete()=> END. redirect to filter");
            return redirect("../");

        }

        logger.info("delete()=> END.");
        return "emailTemplate/edit";

    }
            */

            
    @Clean(SessionKey.COST_CENTER_HEADER)
    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.COST_CENTER) CostCenterForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new CostCenterForm();
        form.initialize();

        model.addAttribute(SessionKey.COST_CENTER, form);

        logger.info("create()=> END.");

        return "costCenter/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.COST_CENTER) CostCenterForm form, @PathVariable(IdPathKey.COST_CENTER_ID) Long costCenterId, Model model) {

        logger.info("show()=> BEGIN");

        CostCenterView costCenter = costCenterService.findCostCenterToEdit(getStoreId(),
                costCenterId
        );

        logger.info("show()=> costCenter: "+costCenter);

        if (costCenter   != null) {

            form.setCostCenterId(costCenter.getCostCenterData().getCostCenterId());
            form.setCostCenterName(costCenter.getCostCenterData().getShortDesc());
            form.setStatus(costCenter.getCostCenterData().getCostCenterStatusCd());
            form.setCostCenterType(costCenter.getCostCenterData().getCostCenterTypeCd());
            form.setCostCenterCode(costCenter.getCostCenterData().getCostCenterCode());
            form.setCostCenterTaxType(costCenter.getCostCenterData().getCostCenterTaxType());
            form.setAllocateFreight(Utility.isTrue(costCenter.getCostCenterData().getAllocateFreight()) ? costCenter.getCostCenterData().getAllocateFreight() : "false");
            form.setAllocateDiscount(Utility.isTrue(costCenter.getCostCenterData().getAllocateDiscount()) ? costCenter.getCostCenterData().getAllocateDiscount() : "false");
            form.setDoNotApplyBudget(Utility.isTrue(costCenter.getCostCenterData().getNoBudget()) ? costCenter.getCostCenterData().getNoBudget() : "false");

        }
        model.addAttribute(SessionKey.COST_CENTER, form);

        logger.info("show()=> END.");

        return "costCenter/edit";

    }


    @ModelAttribute(SessionKey.COST_CENTER)
    public CostCenterForm initModel() {

        CostCenterForm form = new CostCenterForm();
        form.initialize();

        return form;

    }

}

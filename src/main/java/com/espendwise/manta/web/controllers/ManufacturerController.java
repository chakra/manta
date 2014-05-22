package com.espendwise.manta.web.controllers;

import java.util.List;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.ManufacturerIdentView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.ManufacturerService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.ManufacturerForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.ManufacturerWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;


@Controller
@RequestMapping(UrlPathKey.MANUFACTURER.IDENTIFICATION)
public class ManufacturerController extends BaseController {

    private static final Logger logger = Logger.getLogger(ManufacturerController.class);

    private ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }


    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new ManufacturerWebUpdateExceptionResolver());

        return "manufacturer/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "manufacturer/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.MANUFACTURER_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.MANUFACTURER) ManufacturerForm manufacturerForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, manufacturerForm: "+manufacturerForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(manufacturerForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.MANUFACTURER, manufacturerForm);
            return "manufacturer/edit";
        }

        ManufacturerIdentView manufacturerIdentView = new ManufacturerIdentView();

        if (!manufacturerForm.getIsNew()) {
            manufacturerIdentView = manufacturerService.findManufacturerToEdit(getStoreId(),
                    manufacturerForm.getManufacturerId()
            );
        } else {

        }

        manufacturerIdentView.setProperties(WebFormUtil.createManufacturerProperties(manufacturerIdentView.getProperties(), manufacturerForm));
        manufacturerIdentView.setBusEntityData(WebFormUtil.createBusEntityData(manufacturerIdentView.getBusEntityData(), manufacturerForm));

        try {

            manufacturerIdentView = manufacturerService.saveManufacturer(
                    getStoreId(),
                    getUserId(),
                    manufacturerIdentView
            );

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e) {

            return handleDatabaseUpdateException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + manufacturerIdentView.getBusEntityData().getBusEntityId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(manufacturerIdentView.getBusEntityData().getBusEntityId()));
    }



    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.MANUFACTURER)ManufacturerForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new ManufacturerForm();
        form.initialize();

        model.addAttribute(SessionKey.MANUFACTURER, form);

        logger.info("create()=> END.");

        return "manufacturer/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.MANUFACTURER) ManufacturerForm form, @PathVariable(IdPathKey.MANUFACTURER_ID) Long manufactureId, Model model) {

        logger.info("show()=> BEGIN");

        ManufacturerIdentView manufacturer = manufacturerService.findManufacturerToEdit(getStoreId(),
                manufactureId
        );

        logger.info("show()=> manufacturer: "+manufacturer);

        if (manufacturer != null) {

            form.setManufacturerId(manufacturer.getBusEntityData().getBusEntityId());
            form.setManufacturerName(manufacturer.getBusEntityData().getShortDesc());
            form.setManufacturerStatus(manufacturer.getBusEntityData().getBusEntityStatusCd());

            String msdsPluginProp = PropertyUtil.toValueNN(manufacturer.getProperties().getMsdsPlugin());
            form.setManufacturerMSDSPlugIn(
                    Utility.isSet(msdsPluginProp) ?
                    msdsPluginProp :
                    RefCodeNames.MSDS_PLUGIN_CD.DEFAULT);
        }

        model.addAttribute(SessionKey.MANUFACTURER, form);

        logger.info("show()=> END.");
         
        return "manufacturer/edit";

    }


    @ModelAttribute(SessionKey.MANUFACTURER)
    public ManufacturerForm initModel() {

        ManufacturerForm form = new ManufacturerForm();
        form.initialize();

        return form;

    }
    @ResponseBody
  //  @RequestMapping(value = ManufacturerForm.ACTION.GET_QRCODE)
    public byte[] getQRCode( @PathVariable(IdPathKey.LOCATION_ID) Long locationId) throws Exception {

        logger.info("getQRCode()=> BEGIN storeId = " + getStoreId() + ", locationId = " + locationId);

        byte[] stream = ((QRCode.from("https://LO/" + getStoreId() +"/"+ locationId).to(ImageType.GIF).withSize(175, 175).stream()).toByteArray() );
 
        logger.info("getQRCode()=> END. stream.length = " + (stream!= null ? stream.length : 0));

        return stream;

    }


}

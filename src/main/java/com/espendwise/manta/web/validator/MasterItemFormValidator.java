package com.espendwise.manta.web.validator;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.validation.LongValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.DoubleValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.MasterItemForm;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;

public class MasterItemFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(MasterItemFormValidator.class);


    public MasterItemFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        MasterItemForm valueObj = (MasterItemForm) obj;

        ValidationResult vr;
        TextValidator longDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.LONG_DESC_LENGTH);
        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH);
        TextValidator notRequiredShortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DESC_LENGTH, false);
        DoubleValidator doubleValidator = Validators.getDoubleValidator();
        LongValidator longValidator = Validators.getLongValidator();

        // item name
        vr = shortDescValidator.validate(valueObj.getItemName(), new TextErrorWebResolver("admin.masterItem.label.itemName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // description
        vr = longDescValidator.validate(valueObj.getLongDesc(), new TextErrorWebResolver("admin.masterItem.label.description"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // sku
        if (!valueObj.getAutoSkuFlag()) {
            vr = shortDescValidator.validate(valueObj.getItemSku(), new TextErrorWebResolver("admin.masterItem.label.SKU"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        // uom
        vr = shortDescValidator.validate(valueObj.getUomValue(), new TextErrorWebResolver("admin.masterItem.label.UOM"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // pack
        vr = shortDescValidator.validate(valueObj.getPack(), new TextErrorWebResolver("admin.masterItem.label.pack"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // size
        vr = notRequiredShortDescValidator.validate(valueObj.getSize(), new TextErrorWebResolver("admin.masterItem.label.size"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // manufacturer
        vr =  shortDescValidator.validate(Utility.strNN(valueObj.getManufacturerId()),
            new TextErrorWebResolver("admin.masterItem.label.manufacturer"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // manuf sku
        vr = shortDescValidator.validate(valueObj.getManufacturerSku(), new TextErrorWebResolver("admin.masterItem.label.manufacturerSku"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // categoryId
        vr = shortDescValidator.validate(Utility.strNN(valueObj.getItemCategoryId()), 
            new TextErrorWebResolver("admin.masterItem.label.category"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // status
        vr = shortDescValidator.validate(valueObj.getStatus(), new TextErrorWebResolver("admin.masterItem.label.status"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        
        //other properties
        vr = notRequiredShortDescValidator.validate(valueObj.getProductUPC(), new TextErrorWebResolver("admin.masterItem.label.productUPC"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getPackUPC(), new TextErrorWebResolver("admin.masterItem.label.packUPC"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        String unspscCode = valueObj.getUNSPSCCode();
        if (Utility.isSet(unspscCode)) {
            vr = notRequiredShortDescValidator.validate(unspscCode, new TextErrorWebResolver("admin.masterItem.label.UNSPSCCode"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
	        vr = longValidator.validate(unspscCode, new NumberErrorWebResolver("admin.masterItem.label.UNSPSCCode"));
	        if (vr != null) {
	            errors.putErrors(vr.getResult());
	        }
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getShippingCubicSize(), new TextErrorWebResolver("admin.masterItem.label.shippingCubicSize"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        String shippingWeight = valueObj.getShippingWeight();
        if (Utility.isSet(shippingWeight)) {
            vr = notRequiredShortDescValidator.validate(shippingWeight, new TextErrorWebResolver("admin.masterItem.label.shippingWeight"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            vr = doubleValidator.validate(shippingWeight, new NumberErrorWebResolver("admin.masterItem.label.shippingWeight"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getColor(), new TextErrorWebResolver("admin.masterItem.label.itemColor"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getScent(), new TextErrorWebResolver("admin.masterItem.label.scent"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        String listPrice = valueObj.getListPriceMSRP();
        if (Utility.isSet(listPrice)) {
            vr = notRequiredShortDescValidator.validate(listPrice, new TextErrorWebResolver("admin.masterItem.label.listPriceMSRP"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            vr = doubleValidator.validate(listPrice, new NumberErrorWebResolver("admin.masterItem.label.listPriceMSRP"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getNSN(), new TextErrorWebResolver("admin.masterItem.label.NSN"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        vr = notRequiredShortDescValidator.validate(valueObj.getWeightUnit(), new TextErrorWebResolver("admin.masterItem.label.weightUnit"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        // format of files
        CommonsMultipartFile thFile = valueObj.getThumbnailFileData();
        if (thFile != null && thFile.getSize() > 0) {
            String contentType = thFile.getContentType();
            if (!Utility.isSet(contentType) ||
                !contentType.startsWith(Constants.IMAGE_CONTENT_TYPE)) {
                errors.putError("validation.web.error.wrongFileFormat", new StringArgument(thFile.getOriginalFilename()));
            }
        }
        CommonsMultipartFile imgFile = valueObj.getImgFileData();
        if (imgFile != null && imgFile.getSize() > 0) {
            String contentType = imgFile.getContentType();
            if (!Utility.isSet(contentType) ||
                !contentType.startsWith(Constants.IMAGE_CONTENT_TYPE)) {
                errors.putError("validation.web.error.wrongFileFormat", new StringArgument(imgFile.getOriginalFilename()));
            }
        }


        return new MessageValidationResult(errors.get());
    }




}

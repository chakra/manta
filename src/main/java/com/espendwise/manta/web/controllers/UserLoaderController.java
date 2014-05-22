package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.UserLoaderForm;
import com.espendwise.manta.web.resolver.LoaderWebExceptionResolver;
import com.espendwise.manta.web.util.*;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(UrlPathKey.USER.LOADER)
@SessionAttributes({SessionKey.USER_LOADER})
@AutoClean(SessionKey.USER_LOADER)
public class UserLoaderController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserLoaderController.class);

    private UserService userService;

    @Autowired
    public UserLoaderController(UserService userService, StoreService storeService) {
        this.userService = userService;
    }
    
    @ModelAttribute(SessionKey.USER_LOADER)
    public UserLoaderForm initModel() {

    	UserLoaderForm form = new UserLoaderForm();
        form.initialize();
        return form;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String show(@ModelAttribute(SessionKey.USER_LOADER) UserLoaderForm loaderForm, Model model) {
    	loaderForm.reset();
        model.addAttribute(SessionKey.USER_LOADER, loaderForm);
        return "user/loader";
    }
    
    @RequestMapping(value = "/exportTemplate", method = RequestMethod.POST)
    public String exportTemplate(WebRequest request, HttpServletResponse response) throws Exception {
        logger.info("exportTemplate()=> BEGIN");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

	    try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<Pair<String, String>> headers = RefCodeNamesKeys.getRefCodeValues(Constants.USER_LOADER_PROPERTY.class, false);
            HSSFSheet sheet = workbook.createSheet("User Loader Template");
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
            for(int i=0; i<headers.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                Pair<String, String> h = headers.get(i);
                cell.setCellValue(h.getObject2());
            }
	    	workbook.write(out);

	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }

        try {
            ReportingUtil.initializeResponseForExcelExport(request, response, "userLoaderTemplate.xls");
	        response.setContentLength(out.size());
	        out.writeTo(response.getOutputStream());
	        response.flushBuffer();
	        response.getOutputStream().close();
        }
        catch(IOException e){
        	response.getOutputStream().close();
        }
        logger.info("exportTemplate()=> END");
        return null;
    }
    
    @RequestMapping(value = "/loadFile", method = {RequestMethod.POST})
    public String uploadUser(WebRequest request,
                         @ModelAttribute(SessionKey.USER_LOADER) UserLoaderForm form,
                         Model model) throws Exception {
        logger.info("uploadUser()=> BEGIN");
        WebErrors webErrors = new WebErrors(request);
        if (form.getUploadedFile() == null || form.getUploadedFile().getSize()==0){
        	webErrors.putError("validation.web.error.emptyValue", Args.i18nTyped("admin.uploadFile.label.selectFile"));
            return "user/loader";
        }
        
        AppUser user = getAppUser();
        if (!user.isQneOfAdmin()) {
            webErrors.putError("validation.web.error.unauthorizedAccess");
            return "user/loader";
        }
             
        String streamType = form.getUploadedFile().getOriginalFilename().endsWith(".xls") ? "xls" : 
        		form.getUploadedFile().getOriginalFilename().endsWith(".xlsx") ? "xlsx" :
        		form.getUploadedFile().getOriginalFilename().endsWith(".csv") ? "csv" : null;
        if (streamType == null){
        	webErrors.putError("validation.web.error.mustBeCsvOrXlsOrXlsxFileFormat");
            return "user/loader";
        }
        
        try{        	
        	SuccessActionMessage message = userService.processUserUpload(getAppUser().getLocale(), getStoreId(), form.getUploadedFile().getInputStream(), streamType);
        	WebAction.success(request, message);
        }catch (ValidationException e) {
        	logger.info("save() =======> handleValidationException ");
            return handleValidationException(e, request);
            
        }catch(Exception e){
        	webErrors.putError(new WebError("dummyMessageKey", null, e.getMessage()));
            return "user/loader";
        }

        logger.info("uploadUser()=> END.");

        return "user/loader";
    }    
        
    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new LoaderWebExceptionResolver());
        
        return "user/loader";
    }    
}



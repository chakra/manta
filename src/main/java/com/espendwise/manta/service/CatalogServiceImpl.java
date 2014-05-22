package com.espendwise.manta.service;


import java.util.List;
import java.io.InputStream;
import java.util.Locale;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.CatalogDAO;
import com.espendwise.manta.dao.CatalogDAOImpl;
import com.espendwise.manta.model.data.CatalogData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.CatalogView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.ServiceListView;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogSearchCriteria;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.criteria.ServiceListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationIllegalArgumentException;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.web.util.SuccessActionMessage;
import com.espendwise.manta.loader.UpdateCatalogLoader;


@Service
public class CatalogServiceImpl extends DataAccessService implements CatalogService {

    private static final Logger logger = Logger.getLogger(CatalogServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CatalogView> findCatalogsByCriteria(CatalogSearchCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

        return catalogDao.findCatalogsByCriteria(criteria);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CatalogListView> findCatalogsByCriteria(CatalogListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

        return catalogDao.findCatalogsByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CatalogStructureListView> findCatalogStructuresByCriteria(CatalogStructureListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

        return catalogDao.findCatalogStructuresByCriteria(criteria);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemView> findCatalogCategories(List<Long> catalogIds){

    	EntityManager entityManager = getEntityManager();
	    CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
	
	    return catalogDao.findCatalogCategories(catalogIds);
	}
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemData> findProducts(Long pStoreId, List<Long> pSkuNumbers) {

    	EntityManager entityManager = getEntityManager();
	    CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
	
	    return catalogDao.findProducts(pStoreId, pSkuNumbers);
	}
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ProductListView> findProductsByCriteria(ProductListViewCriteria criteria) {

    EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

        return catalogDao.findProductsByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ServiceListView> findServicesByCriteria(ServiceListViewCriteria criteria) {
        EntityManager entityManager = getEntityManager();
        CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

        return catalogDao.findServicesByCriteria(criteria);
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemView> findItems(ProductListViewCriteria criteria){

    	EntityManager entityManager = getEntityManager();
	    CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
	
	    return catalogDao.findItems(criteria);
	}

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int processCatalogUpload(Locale locale, Long currStoreId, InputStream inputStream,String streamType){
    	EntityManager entityManager = getEntityManager();
    	logger.info("processCatalogUpload()==> entityManager:"+entityManager);
    	UpdateCatalogLoader loader = new UpdateCatalogLoader(entityManager, locale, currStoreId, inputStream, streamType);
    	loader.translate();
    	logger.info("processCatalogUpload()==> itemCount:"+loader.getLineCount());
    	ValidationRuleResult result = loader.getValidationResult();
        if (result == null) {
            throw new ApplicationIllegalArgumentException(ExceptionReason.SystemReason.ILLEGAL_VALIDATION_RESULT);
        }

        if (result.isFailed()) {
            List<ApplicationExceptionCode> codes = result.getCodes();
            
            ApplicationExceptionCode[] codesArray = codes.toArray(new ApplicationExceptionCode[codes.size()]);
            throw new ValidationException(codesArray);
        }

//    	return new SuccessActionMessage("admin.catalogManager.successMessage.fileProcessedWithCount", 
//    			new NumberArgument[]{new NumberArgument(loader.getAddedCount()),new NumberArgument(loader.getModifiedCount()),new NumberArgument(loader.getDeletedCount())});
    	return loader.getLineCount();
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CatalogStructureData> findCatalogStructuresUsingCriteria(CatalogStructureListViewCriteria criteria) {
    	
    	 EntityManager entityManager = getEntityManager();
         CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);

         List<CatalogStructureData>  catalogStructureList =  catalogDao.findCatalogStructuresUsingCriteria(criteria);
         
         return catalogStructureList;
    }
    
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CatalogData> findCatalogDataUsingCriteria(CatalogListViewCriteria criteria){
    	 EntityManager entityManager = getEntityManager();
         CatalogDAO catalogDao = new CatalogDAOImpl(entityManager);
         
         List<CatalogData>  catalogDataList =  catalogDao.findCatalogDataUsingCriteria(criteria);
         
         return catalogDataList;
    }
    
}

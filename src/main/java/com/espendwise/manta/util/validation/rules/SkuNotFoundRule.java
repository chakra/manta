package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.WorkflowAssocData;
import com.espendwise.manta.model.data.WorkflowQueueData;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.web.controllers.LocateAccountFilterController;

import java.util.*;

import org.apache.log4j.Logger;


public class SkuNotFoundRule implements ValidationRule {


    private List<Long> skuNumbers;
    private Long storeId;
    private static final Logger logger = Logger.getLogger(SkuNotFoundRule.class);

    public SkuNotFoundRule(Long storeId, List<Long> skuNumbers) {
        this.skuNumbers = skuNumbers;
        this.storeId = storeId;
    }
 
    public List<Long> getSkuNumbers() {
		return skuNumbers;
	}

	public Long getStoreId() {
		return storeId;
	}

	@Override
    public ValidationRuleResult apply() {

        ValidationRuleResult vr = new ValidationRuleResult();
//        String[] list = getSkuNumbers().split(",");
//        List<String> skus = Utility.toList(list);
        
        List<ItemData> products = ServiceLocator.getCatalogService().findProducts(
                getStoreId(),
                getSkuNumbers()
        );

        Set<String> errors = new HashSet<String>();
        logger.info("SkuNotFoundRule() == > apply(): products=" + products + ", skuNumbers= "+ getSkuNumbers());
        if (products != null) {

        	Set<Long> foundSku = new HashSet<Long>();
        	for (ItemData p : products) {
        		foundSku.add(p.getSkuNum());
            }
        	
        	for (Long sku : getSkuNumbers()) {
        		if (!foundSku.contains(sku)){
                    errors.add(sku.toString());
        		}
            }
        } else {
           	for (Long sku : getSkuNumbers()) {
                    errors.add(sku.toString());
            }
      	
        }
        logger.info("SkuNotFoundRule() == > apply(): errors.isEmpty()=" + errors.isEmpty());

        if (!errors.isEmpty()) {

            vr.failed(
                    ExceptionReason.WorflowRuleUpdateReason.WORKFLOW_RULE_SKU_NOT_FOUND,
                    new ObjectArgument<Set>(errors)
            );

            return vr;

        }

        vr.success();

        return vr;

    }

}

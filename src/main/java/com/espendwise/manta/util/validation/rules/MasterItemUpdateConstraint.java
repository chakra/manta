package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.service.ItemService;
import com.espendwise.manta.support.spring.ServiceLocator;
import org.apache.log4j.Logger;

import java.util.List;

public class MasterItemUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(MasterItemUpdateConstraint.class);

    private ItemIdentView itemView;
    private Long storeId;

    public MasterItemUpdateConstraint(Long storeId, ItemIdentView itemView) {
        this.itemView = itemView;
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public ItemIdentView getItem() {
        return itemView;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if (getItem()== null) {
            return null;
        }


        ValidationRuleResult result = new ValidationRuleResult();

        checkUnique(result);
        if (result.isFailed()) {
            return result;
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }

    private void checkUnique(ValidationRuleResult result) {
        ItemService itemService = ServiceLocator.getItemService();

        ItemIdentView itemView =  getItem();
        if (itemView == null) {
            return;
        }
        CatalogStructureData catStr = itemView.getCatalogStructureData();
        if (catStr == null) {
            return;
        }
        if (catStr.getCatalogId() == null || !Utility.isSet(catStr.getCustomerSkuNum())) {
            return;
        }

        ItemListViewCriteria criteria = new ItemListViewCriteria();
        criteria.setCatalogId(catStr.getCatalogId());
        criteria.setItemSku(catStr.getCustomerSkuNum());

        CatalogStructureData dbCatStr = itemService.getItemCatalogStructure(criteria);

        if (Utility.isSet(dbCatStr)) {

            if (catStr.getCatalogStructureId() == null ||
                (catStr.getCatalogStructureId().longValue() != dbCatStr.getCatalogStructureId().longValue())) {

                	result.failed(ExceptionReason.MasterItemUpdateReason.CUSTOMER_SKU_NUM_MUST_BE_UNIQ,
                				new ObjectArgument<ItemIdentView>(itemView));
                }
        }
    }

   
}

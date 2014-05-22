package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;

import java.util.Date;

public class StoreMessageDateRangeRule implements ValidationRule {

    private StoreMessageData storeMessageData;

    @Override
    public ValidationRuleResult apply() {

        StoreMessageData storeMessageData = getStoreMessageData();

        ValidationRuleResult result = new ValidationRuleResult();
        
        result.success();

        if (getStoreMessageData() == null ) {
            return null;
        }
        
       // if (getStoreMessageData() == null || !RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE.equals(storeMessageData.getStoreMessageStatusCd())) {
       //     return null;
       // }

       // if (!RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE.equals(storeMessageData.getStoreMessageStatusCd())) {
       //     return result;
      //  }


        Date endDate = storeMessageData.getEndDate();
        Date postedDate = storeMessageData.getPostedDate();

        Date now = Utility.setToMidnight(new Date());

        if (postedDate != null && endDate != null) {

            if (endDate.compareTo(postedDate) < 0) {
                result.failed(
                        ExceptionReason.StoreMessageUpdateReason.STORE_MESSAGE_END_DATE_BEFORE_POSTED_DATE,
                        Args.typed(endDate, postedDate)
                );
            }
        }

        if (postedDate != null) {
            if (!Utility.isTrue(storeMessageData.getPublished()) && postedDate.compareTo(now) < 0) {
                result.failed(
                        ExceptionReason.StoreMessageUpdateReason.STORE_MESSAGE_POSTED_DATE_BEFORE_CURRENT_DATE,
                        Args.typed(postedDate, now)
                );
            }
        }

        if (endDate != null) {
            if (endDate.compareTo(now) < 0) {
                result.failed(
                        ExceptionReason.StoreMessageUpdateReason.STORE_MESSAGE_END_DATE_BEFORE_CURRENT_DATE,
                        Args.typed(endDate, now)
                );
            }
        }

        

        return result;

    }

    public StoreMessageData getStoreMessageData() {
        return storeMessageData;
    }

    public StoreMessageDateRangeRule(StoreMessageData storeMessageData) {
        this.storeMessageData = storeMessageData;
    }
}

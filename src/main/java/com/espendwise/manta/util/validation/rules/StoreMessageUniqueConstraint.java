package com.espendwise.manta.util.validation.rules;


import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class StoreMessageUniqueConstraint implements ValidationRule {

   private static final Logger logger = Logger.getLogger(StoreMessageUniqueConstraint.class);

    private Long storeId;
    private StoreMessageData storeMessageData;

    public StoreMessageUniqueConstraint(Long storeId, StoreMessageData storeMessageData) {
        this.storeId = storeId;
        this.storeMessageData = storeMessageData;
    }

    public Long getStoreId() {
        return storeId;
    }

    public StoreMessageData getStoreMessageData() {
        return storeMessageData;
    }

    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");
        if(getStoreMessageData() == null) {
            return null;
        }

        ValidationRuleResult result = new ValidationRuleResult();

        StoreMessageDataCriteria criteria = new StoreMessageDataCriteria();
        criteria.setStoreId(getStoreId());
        criteria.setCountry(getStoreMessageData().getCountry());
        criteria.setLanguageCd(getStoreMessageData().getLanguageCd());
        //criteria.setMessageTitle(getStoreMessageData().getMessageTitle());
        //criteria.setMessageTitleFilterType(Constants.FILTER_TYPE.EXACT_MATCH);
        criteria.setName(getStoreMessageData().getShortDesc());
        criteria.setNameFilterType(Constants.FILTER_TYPE.EXACT_MATCH);
        
        StoreMessageService storeMessageService = getStoreMessageService();

        logger.info("check()=> storeMessageService: " + storeMessageService);

        List<StoreMessageListView> storeMessages = storeMessageService.findMessagesByCriteria(criteria);
        if (storeMessages != null && storeMessages.size() > 0) {

            for (StoreMessageListView storeMessage : storeMessages) {

                if (!Utility.isSet(getStoreMessageData().getCountry()) && Utility.isSet(storeMessage.getCountry())) {
                    continue;
                }

                if (getStoreMessageData().getStoreMessageId() == null || (storeMessage.getStoreMessageId().longValue() != getStoreMessageData().getStoreMessageId())) {

                    result.failed(
                            ExceptionReason.StoreMessageUpdateReason.STORE_MESSAGE_NAME_MUST_BE_UNIQUE,
                            new ObjectArgument<StoreMessageData>(getStoreMessageData())
                    );

                    return result;
                }
            }
        }

        logger.info("apply()=> END, SUCCESS");

        result.success();

        return result;
    }


    public StoreMessageService getStoreMessageService() {
        return ServiceLocator.getStoreMessageService();
    }
}

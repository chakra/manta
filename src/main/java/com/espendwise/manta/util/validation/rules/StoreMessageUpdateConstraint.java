package com.espendwise.manta.util.validation.rules;

import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.RefCodeNames.STORE_MESSAGE_STATUS_CD;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import org.apache.log4j.Logger;

import java.util.List;

public class StoreMessageUpdateConstraint implements ValidationRule {

	private static final Logger logger = Logger
			.getLogger(StoreMessageUniqueConstraint.class);

	private Long storeId;
	private StoreMessageData storeMessageData;

	public StoreMessageUpdateConstraint(Long storeId,
			StoreMessageData storeMessageData) {
		this.storeId = storeId;
		this.storeMessageData = storeMessageData;
	}

	public Long getStoreId() {
		return storeId;
	}

	public StoreMessageData getStoreMessageData() {
		return storeMessageData;
	}

	@Override
	public ValidationRuleResult apply() {

		logger.info("apply()=> BEGIN");

		if (getStoreMessageData() == null) {
			return null;
		}

		ValidationRuleResult vrr = new ValidationRuleResult();

		checkForceReadCount(vrr, getStoreMessageData(), getStoreId());
		if (vrr.isFailed()) {
			return vrr;
		}

		vrr.success();

		logger.info("apply()=> END.");

		return vrr;
	}

	/*
	 * User cannot change the 'force read count' to less than original number,
	 * while updating a 'published' message.
	 */
	public ValidationRuleResult checkForceReadCount(ValidationRuleResult result,
			StoreMessageData pMessage, Long pStoreId) {

		if (pMessage == null) {
			return null;
		}

		//ValidationRuleResult result = new ValidationRuleResult();

		Long messageId = pMessage.getStoreMessageId();

		if ( messageId != null && messageId > 1
				&& pMessage.getMessageType().equals(
						RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ)) {

			StoreMessageService storeMessageService = getStoreMessageService();

			logger.info("check()=> storeMessageService: " + storeMessageService);

			StoreMessageData message = storeMessageService.findStoreMessage(
					pStoreId, messageId);
			if (Utility.isTrue(message.getPublished())
					&& pMessage.getForcedReadCount().compareTo(
							message.getForcedReadCount()) < 0) {
				result.failed(
						ExceptionReason.StoreMessageUpdateReason.FORCE_READ_COUNT_LESS_THAN_ORIGINAL_VALUE,
						new ObjectArgument<StoreMessageData>(
								getStoreMessageData()));

				return result;

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

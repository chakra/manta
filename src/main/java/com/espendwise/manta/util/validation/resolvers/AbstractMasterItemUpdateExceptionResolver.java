package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.ItemIdentView;

import java.util.List;
import java.util.ArrayList;

public abstract class AbstractMasterItemUpdateExceptionResolver implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.MasterItemUpdateReason) {

                    switch ((ExceptionReason.MasterItemUpdateReason) code.getReason()) {
                        case ITEM_MUST_BE_UNIQUE:
                            errors.add(isItemNameNotUnique(code, (ItemData) code.getArguments()[0].get()));
                            break;

                        case CUSTOMER_SKU_NUM_MUST_BE_UNIQ:
                            errors.add(isCustomerSkuNotUnique(code, (ItemIdentView)code.getArguments()[0].get())) ;
                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isItemNameNotUnique(ApplicationExceptionCode code, ItemData item);

    protected abstract ArgumentedMessage isCustomerSkuNotUnique(ApplicationExceptionCode code, ItemIdentView itemView);

}

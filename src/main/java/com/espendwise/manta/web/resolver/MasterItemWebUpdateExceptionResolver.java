package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.validation.resolvers.AbstractMasterItemUpdateExceptionResolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.ItemIdentView;

public class MasterItemWebUpdateExceptionResolver extends AbstractMasterItemUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isItemNameNotUnique(ApplicationExceptionCode code, ItemData item) {
        return new ArgumentedMessageImpl("validation.web.error.itemNotUnique", Args.typed(item.getShortDesc()));
    }

    @Override
    protected ArgumentedMessage isCustomerSkuNotUnique(ApplicationExceptionCode code, ItemIdentView itemView) {
        return new ArgumentedMessageImpl("validation.web.error.customerSkuNotUnique",
                            Args.typed( itemView.getCatalogStructureData().getCustomerSkuNum()));
    }
}

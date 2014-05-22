package com.espendwise.manta.util.binder;


import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.StoreUiOptionView;
import com.espendwise.manta.util.HomeViewType;
import com.espendwise.manta.util.StoreUiPropertyTypeCode;

import java.util.List;

public class StoreUiOptionsBinder {

    public static StoreUiOptionView bindUiOptions(StoreUiOptionView uiOptions,
                                                  Long storeId,
                                                  List<PropertyData> uiProperties,
                                                  HomeViewType homeViewType) {

        uiOptions.setStoreId(storeId);
        uiOptions.setHomeViewType(homeViewType);

        if (uiProperties != null) {
            for (PropertyData prop : uiProperties) {
                if (StoreUiPropertyTypeCode.MANTA_UI_LOGO.toString().equals(prop.getPropertyTypeCd())) {
                    uiOptions.setLogo(prop.getValue());
                } else if (StoreUiPropertyTypeCode.UI_PAGE_TITLE.toString().equals(prop.getPropertyTypeCd())) {
                    uiOptions.setTitle(prop.getValue());
                } else if (StoreUiPropertyTypeCode.UI_FOOTER.toString().equals(prop.getPropertyTypeCd())) {
                    uiOptions.setFooter(prop.getValue());
                }
            }
        }

        return uiOptions;

    }
}

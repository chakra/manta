package com.espendwise.manta.util;


import com.espendwise.manta.auth.ApplicationDataSource;
import com.espendwise.manta.auth.AuthDatabaseAccessUnit;
import com.espendwise.manta.auth.AuthMainStoreIdent;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.InstanceView;
import com.espendwise.manta.web.util.WebSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConvX {


    public static List<AllStoreIdentificationView> convertToAllStoreIdent(ApplicationDataSource ds, List<BusEntityData> stores) {

        List<AllStoreIdentificationView> x = new ArrayList<AllStoreIdentificationView>();

        if (Utility.isSet(stores)) {

            AllStoreIdentificationView element;

            for (BusEntityData adminStore : stores) {

                element = new AllStoreIdentificationView();
                element.setStoreId(adminStore.getBusEntityId());
                element.setStoreName(adminStore.getShortDesc());
                element.setMainStoreId(adminStore.getBusEntityId());
                element.setDsName(ds.getDataSourceIdent().getDataSourceName());
                element.setAlive(true);

                x.add(element);

            }

            WebSort.sort(x, AllStoreIdentificationView.STORE_NAME);
        }

        return x;

    }

    public static List<AllStoreIdentificationView> convertToAllStoreIdent(List<InstanceView> adminStores, boolean multiStoreDb) {

        List<AllStoreIdentificationView> x = new ArrayList<AllStoreIdentificationView>();

        if (Utility.isSet(adminStores)) {

            AllStoreIdentificationView element;

            for (InstanceView adminStore : adminStores) {

                element = new AllStoreIdentificationView();
                element.setStoreId(adminStore.getPrimaryEntityId());
                element.setStoreName(adminStore.getPrimaryEntityName());
                element.setMainStoreId(multiStoreDb?adminStore.getMainStoreIdent().getId():adminStore.getPrimaryEntityId());
                element.setDsName(adminStore.getDataSourceIdent().getDataSourceName());
                element.setAlive(adminStore.isAlive());

                x.add(element);

            }
            WebSort.sort(x, AllStoreIdentificationView.STORE_NAME);
        }

        return x;

    }

    public static List<AllStoreIdentificationView> convertToAllStoreIdent(Map<String, AuthDatabaseAccessUnit> m) {

        List<AllStoreIdentificationView> x = new ArrayList<AllStoreIdentificationView>();

        if (!m.isEmpty()) {

            List<AuthMainStoreIdent> idents;
            AllStoreIdentificationView element;

            boolean alive;

            for(AuthDatabaseAccessUnit userStore : m.values()) {

                alive = userStore.isAlive();
                idents = userStore.getMainStoreIdents();

                if (Utility.isSet(idents)) {

                    for(AuthMainStoreIdent ident : idents) {

                        element = new AllStoreIdentificationView();
                        element.setStoreId(ident.getStoreId());
                        element.setStoreName(ident.getMainStoreName());
                        element.setMainStoreId(ident.getMainStoreId());
                        element.setDsName(ident.getDatasource());
                        element.setAlive(alive);

                        x.add(element);

                    }
                    WebSort.sort(x, AllStoreIdentificationView.STORE_NAME);
                }
            }
        }

        return x;

    }
}

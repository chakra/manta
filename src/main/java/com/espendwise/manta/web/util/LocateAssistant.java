package com.espendwise.manta.web.util;


import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.ManufacturerListView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.util.Utility;

import java.util.List;

public class LocateAssistant {


    public static String getFilteredAccountCommaNames(List<AccountListView> filteredAccounts) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredAccounts)) {
            for (AccountListView value : filteredAccounts) {
                builder.append((builder.length() > 0) ? ", " + value.getAccountName() : value.getAccountName());
            }
        }
        return builder.toString();
    }

    public static String getFilteredAccountCommaIds(List<AccountListView> filteredAccounts) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredAccounts)) {
            for (AccountListView value :filteredAccounts) {
                builder.append((builder.length() > 0) ? ", " + value.getAccountId() : value.getAccountId());
            }
        }
        return builder.toString();
    }

    public static String getFilteredUserCommaNames(List<UserListView> filteredUsers) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredUsers)) {
            for (UserListView value : filteredUsers) {
                builder.append((builder.length() > 0) ? ", " + value.getNotifyUserName() : value.getNotifyUserName());
            }
        }
        return builder.toString();
    }

    public static String getFilteredUserCommaIds(List<UserListView> filteredUsers) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredUsers)) {
            for (UserListView value :filteredUsers) {
                builder.append((builder.length() > 0) ? ", " + value.getUserId() : value.getUserId());
            }
        }
        return builder.toString();
    }
    public static String getFilteredDistrCommaNames(List<DistributorListView> filteredDistr) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredDistr)) {
            for (DistributorListView value : filteredDistr) {
                builder.append((builder.length() > 0) ? ", " + value.getDistributorName() : value.getDistributorName());
            }
        }
        return builder.toString();
    }

    public static String getFilteredDistrCommaIds(List<DistributorListView> filteredDistr) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredDistr)) {
            for (DistributorListView value :filteredDistr) {
                builder.append((builder.length() > 0) ? ", " + value.getDistributorId() : value.getDistributorId());
            }
        }
        return builder.toString();
    }

    public static String getFilteredManufacturerCommaIds(List<ManufacturerListView> filteredManufacturers) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredManufacturers)) {
            for (ManufacturerListView value :filteredManufacturers) {
                builder.append((builder.length() > 0) ? ", " + value.getManufacturerId() : value.getManufacturerId());
            }
        }
        return builder.toString();
    }

    public static String getFilteredCatalogCommaNames(List<CatalogListView> filteredCatalogs) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredCatalogs)) {
            for (CatalogListView value : filteredCatalogs) {
                builder.append((builder.length() > 0) ? ", " + value.getCatalogName() : value.getCatalogName());
            }
        }
        return builder.toString();
    }

    public static String getFilteredCatalogCommaIds(List<CatalogListView> filteredCatalogs) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredCatalogs)) {
            for (CatalogListView value :filteredCatalogs) {
                builder.append((builder.length() > 0) ? ", " + value.getCatalogId() : value.getCatalogId());
            }
        }
        return builder.toString();
    }
    public static String getFilteredItemCommaSkus(List<ProductListView> filteredItem) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredItem)) {
            for (ProductListView value : filteredItem) {
                builder.append((builder.length() > 0) ? ", " + value.getItemSku() : value.getItemSku());
            }
        }
        return builder.toString();
    }

    public static String getFilteredItemCommaIds(List<ProductListView> filteredItem) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredItem)) {
            for (ProductListView value : filteredItem) {
                builder.append((builder.length() > 0) ? ", " + value.getItemId() : value.getItemId());
            }
        }
        return builder.toString();
    }
    
    public static String getFilteredSiteCommaNames(List<SiteListView> filteredSites) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredSites)) {
            for (SiteListView value : filteredSites) {
                builder.append((builder.length() > 0) ? ", " + value.getSiteName() : value.getSiteName());
            }
        }
        return builder.toString();
    }

    public static String getFilteredSiteCommaIds(List<SiteListView> filteredSites) {
        StringBuilder builder = new StringBuilder();
        if (Utility.isSet(filteredSites)) {
            for (SiteListView value : filteredSites) {
                builder.append((builder.length() > 0) ? ", " + value.getSiteId() : value.getSiteId());
            }
        }
        return builder.toString();
    }
}

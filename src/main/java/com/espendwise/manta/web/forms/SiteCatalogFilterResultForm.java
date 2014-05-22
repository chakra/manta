package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.CatalogView;
import java.util.List;

public class SiteCatalogFilterResultForm  extends AbstractFilterResult<CatalogView> {

    private List<CatalogView> catalogs;
    private Long catalogId;

    public List<CatalogView> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogView> catalogs) {
        this.catalogs = catalogs;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    @Override
    public List<CatalogView> getResult() {
        return catalogs;
    }

    @Override
    public void reset() {
        super.reset();
        this.catalogs = null;
    }

}

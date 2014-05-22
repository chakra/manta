package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.AssetListView;
import com.espendwise.manta.util.SelectableObjects;
import java.util.List;

public class LocateAssetFilterResultForm  extends AbstractFilterResult<SelectableObjects.SelectableObject<AssetListView>> {

    private SelectableObjects<AssetListView> selectedAssets;
    private boolean multiSelected;

    public SelectableObjects<AssetListView> getSelectedAssets() {
        return selectedAssets;
    }

    public void setSelectedAssets(SelectableObjects<AssetListView> selectedAssets) {
        this.selectedAssets = selectedAssets;
    }

    @Override
    public List<SelectableObjects.SelectableObject<AssetListView>> getResult() {
        return selectedAssets != null ? selectedAssets.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        selectedAssets = null;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean getMultiSelected() {
        return multiSelected;
    }
}

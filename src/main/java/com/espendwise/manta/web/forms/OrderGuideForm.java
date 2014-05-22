package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.AppComparator;
//import com.espendwise.manta.web.validator.OrderGuideFormValidator;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.model.view.OrderGuideItemView;

import java.math.BigDecimal;
import java.util.*;

import org.apache.log4j.Logger;

//@Validation(OrderGuideFormValidator.class)
public class OrderGuideForm extends AbstractFilterResult<SelectableObjects.SelectableObject<OrderGuideItemView>> implements Initializable {

    private static final Logger logger = Logger.getLogger(OrderGuideForm.class);

    private Long orderGuideId;

    private Long siteId;
    private String orderGuideName;
    private String orderGuideTypeCd;

    private Long catalogId;
    private String catalogName;

    private Boolean shareBuyerOrderGuide;
    private BigDecimal totalAmount;
       
    private boolean initialize;

    public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getOrderGuideId() {
		return orderGuideId;
	}

	public Long getCatalogId() {
        return this.catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Boolean getShareBuyerOrderGuide() {
        return this.shareBuyerOrderGuide;
    }

    public void setShareBuyerOrderGuide(Boolean shareBuyerOrderGuide) {
        this.shareBuyerOrderGuide = shareBuyerOrderGuide;
    }

	public void setOrderGuideId(Long orderGuideId) {
		this.orderGuideId = orderGuideId;
	}

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderGuideName() {
		return orderGuideName;
	}

	public void setOrderGuideName(String orderGuideName) {
		this.orderGuideName = orderGuideName;
	}

	public String getOrderGuideTypeCd() {
		return orderGuideTypeCd;
	}

	public void setOrderGuideTypeCd(String orderGuideTypeCd) {
		this.orderGuideTypeCd = orderGuideTypeCd;
	}

	public boolean isInitialize() {
		return initialize;
	}

	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}
   @Override
    public void initialize() {
        initialize = true;
    }
   @Override
   public boolean isInitialized() {
       return  initialize;
   }
   public boolean getIsNew() {
       return isNew();
   }

   public boolean isNew() {
     return isInitialized() && (orderGuideId  == null || orderGuideId == 0);
   }

    private SelectableObjects<OrderGuideItemView> items;


    public SelectableObjects<OrderGuideItemView> getItems() {
		return items;
	}

	public void setItems(SelectableObjects<OrderGuideItemView> items) {
		this.items = items;
	}

	@Override
    public List<SelectableObjects.SelectableObject<OrderGuideItemView>> getResult() {
        return items != null ? items.getSelectableObjects() : null;
    }

    @Override
    public void reset() {
        super.reset();
        this.items = null;
    }
    

    public void addNewItems(List<OrderGuideItemView> itemsNewList) {
        if (itemsNewList != null && itemsNewList.size() > 0) {
            List<OrderGuideItemView> oldItems = this.items != null ?
                                                this.items.getValues() :
                                                new ArrayList<OrderGuideItemView>();
            for (OrderGuideItemView itemNew : itemsNewList) {
                boolean found = false;
                for (OrderGuideItemView itemF : oldItems) {
                    if (itemF.getItemId().equals(itemNew.getItemId())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    oldItems.add(itemNew);
                }
            }
            SelectableObjects<OrderGuideItemView> selectableObj = new SelectableObjects<OrderGuideItemView>(
                    oldItems,
                    null,
                    AppComparator.ORDER_GUIDE_ITEMS_COMPARATOR);
        	setItems(selectableObj);
        }
    }

}

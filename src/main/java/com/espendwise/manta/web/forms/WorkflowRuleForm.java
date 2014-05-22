package com.espendwise.manta.web.forms;


import java.util.ArrayList;
import java.util.Set;
import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.IdNamePair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.LocateAssistant;
//import com.espendwise.manta.web.validator.WorkflowRuleFormValidator;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.data.WorkflowRuleData;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.ProductListView;
import com.espendwise.manta.model.view.ItemView;


//@Validation(WorkflowRuleFormValidator.class)
public class WorkflowRuleForm  extends WebForm  implements Resetable, Initializable {

    private Long workflowId;

    private Long accountId;
    private String workflowName;
    private String workflowTypeCd;

    private Long workflowRuleId;
    private String workflowRuleTypeCd;
    private String ruleNumber;
    private String ruleGroup;
    private Long approverGroupId;
    private Long emailGroupId;
    private Long applySkipGroupId;
    private String applySkipActionTypeCd;
    private List<GroupData> userGroups = null;
    private String ruleMessage;
    private WorkflowRuleData ruleToEdit = null;
    
    private List<UserData> approverGroups = null;
    private List<UserData> emailGroups = null;
    private String totalExp;
    private String totalValue;
    private String ruleAction;
    private Integer daysUntilNextAction= 0;
    private String nextActionCd;
    private String emailUserId;
    private String emailUserName;
    private List<UserListView> emailUsers = new ArrayList<UserListView>();
    private List<DistributorListView> filteredDistr;
    private String distrFilter;
    private List<ProductListView> filteredItem;
    private String itemFilter;
    private Boolean splitOrder;
    private Boolean includeBuyerList;
    
    private String itemCategoryId;
    private List<ItemView> itemCategories = null;
    
    private String skuNames;
 //   private String daysNumber;
    
	private boolean initialize;
//	private boolean isOrderTotalRuleType ;


	
	public String getSkuNames() {
		return skuNames;
	}
 

	public List<ProductListView> getFilteredItem() {
		return filteredItem;
	}


	public void setFilteredItem(List<ProductListView> filteredItem) {
		this.filteredItem = filteredItem;
	}


	public String getItemFilter() {
		return itemFilter;
	}


	public void setItemFilter(String itemFilter) {
		this.itemFilter = itemFilter;
	}


	public Boolean getSplitOrder() {
		return splitOrder;
	}


	public void setSplitOrder(Boolean splitOrder) {
		this.splitOrder = splitOrder;
	}


	public String getDistrFilter() {
		return distrFilter;
	}

	public void setDistrFilter(String distrFilter) {
		this.distrFilter = distrFilter;
	}


	public List<DistributorListView> getFilteredDistr() {
		return filteredDistr;
	}


	public void setFilteredDistr(List<DistributorListView> filteredDistr) {
		this.filteredDistr = filteredDistr;
	}


	public String getItemCategoryId() {
		return itemCategoryId;
	}

	public void setSkuNames(String skuNames) {
		this.skuNames = skuNames;
	}

	public void setItemCategoryId(String itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public List<ItemView> getItemCategories() {
		return itemCategories;
	}

	public void setItemCategories(List<ItemView> itemCategories) {
		this.itemCategories = itemCategories;
	}

    public List<UserListView> getEmailUsers() {
		return emailUsers;
	}

   public void setEmailUsers(List<UserListView> emailUsers) {
		this.emailUsers = emailUsers;
	}

	public String getEmailUserName() {
		return (Utility.isSet(emailUsers)) ? LocateAssistant.getFilteredUserCommaNames(emailUsers): this.emailUserName;
	}

	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}

	public Long getWorkflowRuleId() {
		return workflowRuleId;
	}

	public void setWorkflowRuleId(Long workflowRuleId) {
		this.workflowRuleId = workflowRuleId;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(String totalExp) {
		this.totalExp = totalExp;
	}

	public String getRuleAction() {
		return ruleAction;
	}

	public void setRuleAction(String ruleAction) {
		this.ruleAction = ruleAction;
	}

	public Integer getDaysUntilNextAction() {
		return daysUntilNextAction;
	}

	public void setDaysUntilNextAction(Integer daysUntilNextAction) {
		this.daysUntilNextAction = daysUntilNextAction;
	}

	public String getNextActionCd() {
		return nextActionCd;
	}

	public void setNextActionCd(String nextActionCd) {
		this.nextActionCd = nextActionCd;
	}

	public String getEmailUserId() {	
		return (Utility.isSet(emailUsers)) ? LocateAssistant.getFilteredUserCommaIds(emailUsers) : this.emailUserId;
	}

	public void setEmailUserId(String emailUserId) {
		this.emailUserId = emailUserId;
		this.emailUsers=null;
	}

	public WorkflowRuleData getRuleToEdit() {
		return ruleToEdit;
	} 
	
	public String getWorkflowRuleTypeCd() {
		return workflowRuleTypeCd;
	}

	public void setWorkflowRuleTypeCd(String workflowRuleTypeCd) {
		this.workflowRuleTypeCd = workflowRuleTypeCd;
	}

	public String getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public String getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(String ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

	public Long getApproverGroupId() {
		return approverGroupId;
	}

	public void setApproverGroupId(Long approverGroupId) {
		this.approverGroupId = approverGroupId;
	}

	public Long getEmailGroupId() {
		return emailGroupId;
	}

	public void setEmailGroupId(Long emailGroupId) {
		this.emailGroupId = emailGroupId;
	}

	public Long getApplySkipGroupId() {
		return applySkipGroupId;
	}

	public void setApplySkipGroupId(Long applySkipGroupId) {
		this.applySkipGroupId = applySkipGroupId;
	}

	public String getApplySkipActionTypeCd() {
		return applySkipActionTypeCd;
	}

	public void setApplySkipActionTypeCd(String applySkipActionTypeCd) {
		this.applySkipActionTypeCd = applySkipActionTypeCd;
	}

	public List<GroupData> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<GroupData> userGroups) {
		this.userGroups = userGroups;
	}

	public String getRuleMessage() {
		return ruleMessage;
	}

	public void setRuleMessage(String ruleMessage) {
		this.ruleMessage = ruleMessage;
	}

	public void setRuleToEdit(WorkflowRuleData ruleToEdit) {
		this.ruleToEdit = ruleToEdit;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

    public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getWorkflowTypeCd() {
		return workflowTypeCd;
	}

	public void setWorkflowTypeCd(String workflowTypeCd) {
		this.workflowTypeCd = workflowTypeCd;
	}


	public Boolean getIncludeBuyerList() {
		return includeBuyerList;
	}


	public void setIncludeBuyerList(Boolean includeBuyerList) {
		this.includeBuyerList = includeBuyerList;
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
     return isInitialized() && (workflowRuleId  == null || workflowRuleId == 0);
   }

   public boolean getIsOrderTotalRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_TOTAL);
   }
   public boolean getIsBreakWorkflowRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.BREAK_POINT);
   }
   public boolean getIsEveryOrderRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.EVERY_ORDER);
   }
   public boolean getIsBudgetYTDSpendingRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.BUDGET_YTD);
   }

   public boolean getIsNextRuleSectionRequired() {
       return !getIsBreakWorkflowRuleType();
   }
   public boolean getIsOrderSkuRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU);
   }
   public boolean getIsItemCategoryRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM_CATEGORY);
   }
   public boolean getIsOrderSkuQtyRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_SKU_QTY);
   }
   public boolean getIsOrderVelocityRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_VELOCITY);
   }
   public boolean getIsCostCenterBudgetRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.BUDGET_REMAINING_PER_CC);
   }
   public boolean getIsUserLimitRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.USER_LIMIT);
   }
   public boolean getIsOrderExcludedFromBudgetRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ORDER_EXCLUDED_FROM_BUDGET);
   }
   public boolean getIsItemRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.ITEM);
   }
   public boolean getIsCategoryTotalRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.CATEGORY_TOTAL);
   }
   public boolean getIsShoppingControlsRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.SHOPPING_CONTROLS);
   }
   public boolean getIsNonOrderGuideItemRuleType() {
       return isRuleType(RefCodeNames.WORKFLOW_RULE_TYPE_CD.NON_ORDER_GUIDE_ITEM);
   }

   public boolean getIsMultyExpRuleType() {
       return getIsItemCategoryRuleType() || 
       		  getIsOrderSkuQtyRuleType() ||
       		  getIsItemRuleType() ||
       		  getIsOrderTotalRuleType() ||
       		  getIsShoppingControlsRuleType() ||
       		  getIsNonOrderGuideItemRuleType() ||
       		  getIsCategoryTotalRuleType();
   }
   public boolean getIsRuleTypeWithDetails() {
       return getIsItemCategoryRuleType() ||
       		  getIsOrderTotalRuleType() ||
       		  getIsItemRuleType() ||
       		  getIsCategoryTotalRuleType();
   }
  
   public boolean isRuleType(String refCdValue) {
     return refCdValue.equals(workflowRuleTypeCd);
   }
   public String getFilteredUserCommaIds() {
       return LocateAssistant.getFilteredUserCommaIds(
               getEmailUsers()
       );
   }
   
   public String getFilteredDistrCommaNames() {
       return LocateAssistant.getFilteredDistrCommaNames(
               getFilteredDistr()
       );
   }
   public void setFilteredDistrCommaNames(String names) {
	   System.out.println("WorkflowRuleForm ====> setFilteredDistrCommaNames(): names="+ names);
   }

   public String getFilteredDistrCommaIds() {
       return LocateAssistant.getFilteredDistrCommaIds(
               getFilteredDistr()
       );
   }
   public String getFilteredItemCommaSkus() {
       return LocateAssistant.getFilteredItemCommaSkus(
               getFilteredItem()
       );
   }

   public String getFilteredItemCommaIds() {
       return LocateAssistant.getFilteredItemCommaIds(
               getFilteredItem()
       );
   }
   
   @Override
   public void reset() {
       this.applySkipActionTypeCd = null;
       this.applySkipGroupId = null;
       this.approverGroupId = null;
       this.daysUntilNextAction = null;
       this.emailGroupId = null;
       this.emailUserId= null;
       this.emailUserName = null;
       this.emailUsers = null;
       this.nextActionCd = null;
       this.ruleAction = null;
       this.ruleGroup = null;
       this.ruleNumber = null;
       this.totalExp=null;
       this.totalValue=null;
       this.itemCategoryId = null;
       this.splitOrder = null;
       this.includeBuyerList = null;
   }

}

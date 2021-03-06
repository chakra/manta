package com.espendwise.manta.model.data;
// Generated by Hibernate Tools


import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * OrderData generated by hbm2java
*/
@Entity
@Table(name="CLW_ORDER")
public class OrderData extends ValueObject implements TableObject,java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String ORDER_ID = "orderId";
    public static final String EXCEPTION_IND = "exceptionInd";
    public static final String ORDER_NUM = "orderNum";
    public static final String REF_ORDER_NUM = "refOrderNum";
    public static final String COST_CENTER_ID = "costCenterId";
    public static final String COST_CENTER_NAME = "costCenterName";
    public static final String WORKFLOW_IND = "workflowInd";
    public static final String WORKFLOW_STATUS_CD = "workflowStatusCd";
    public static final String ACCOUNT_ERP_NUM = "accountErpNum";
    public static final String SITE_ERP_NUM = "siteErpNum";
    public static final String REQUEST_PO_NUM = "requestPoNum";
    public static final String USER_ID = "userId";
    public static final String USER_FIRST_NAME = "userFirstName";
    public static final String USER_LAST_NAME = "userLastName";
    public static final String ORDER_SITE_NAME = "orderSiteName";
    public static final String ORDER_CONTACT_NAME = "orderContactName";
    public static final String ORDER_CONTACT_PHONE_NUM = "orderContactPhoneNum";
    public static final String ORDER_CONTACT_EMAIL = "orderContactEmail";
    public static final String ORDER_CONTACT_FAX_NUM = "orderContactFaxNum";
    public static final String CONTRACT_ID = "contractId";
    public static final String CONTRACT_SHORT_DESC = "contractShortDesc";
    public static final String ORDER_TYPE_CD = "orderTypeCd";
    public static final String ORDER_SOURCE_CD = "orderSourceCd";
    public static final String ORDER_STATUS_CD = "orderStatusCd";
    public static final String TAX_NUM = "taxNum";
    public static final String ORIGINAL_AMOUNT = "originalAmount";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String TOTAL_FREIGHT_COST = "totalFreightCost";
    public static final String TOTAL_MISC_COST = "totalMiscCost";
    public static final String TOTAL_TAX_COST = "totalTaxCost";
    public static final String TOTAL_CLEANWISE_COST = "totalCleanwiseCost";
    public static final String GROSS_WEIGHT = "grossWeight";
    public static final String ORIGINAL_ORDER_DATE = "originalOrderDate";
    public static final String ORIGINAL_ORDER_TIME = "originalOrderTime";
    public static final String REVISED_ORDER_DATE = "revisedOrderDate";
    public static final String REVISED_ORDER_TIME = "revisedOrderTime";
    public static final String COMMENTS = "comments";
    public static final String INCOMING_TRADING_PROFILE_ID = "incomingTradingProfileId";
    public static final String LOCALE_CD = "localeCd";
    public static final String CURRENCY_CD = "currencyCd";
    public static final String ERP_ORDER_NUM = "erpOrderNum";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String SITE_ID = "siteId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String STORE_ID = "storeId";
    public static final String ERP_SYSTEM_CD = "erpSystemCd";
    public static final String PRE_ORDER_ID = "preOrderId";
    public static final String REF_ORDER_ID = "refOrderId";
    public static final String TOTAL_RUSH_CHARGE = "totalRushCharge";
    public static final String ORDER_BUDGET_TYPE_CD = "orderBudgetTypeCd";
    public static final String ERP_ORDER_DATE = "erpOrderDate";

    private Long orderId;
    private String exceptionInd;
    private String orderNum;
    private String refOrderNum;
    private Long costCenterId;
    private String costCenterName;
    private String workflowInd;
    private String workflowStatusCd;
    private String accountErpNum;
    private String siteErpNum;
    private String requestPoNum;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String orderSiteName;
    private String orderContactName;
    private String orderContactPhoneNum;
    private String orderContactEmail;
    private String orderContactFaxNum;
    private Long contractId;
    private String contractShortDesc;
    private String orderTypeCd;
    private String orderSourceCd;
    private String orderStatusCd;
    private String taxNum;
    private BigDecimal originalAmount;
    private BigDecimal totalPrice;
    private BigDecimal totalFreightCost;
    private BigDecimal totalMiscCost;
    private BigDecimal totalTaxCost;
    private BigDecimal totalCleanwiseCost;
    private BigDecimal grossWeight;
    private Date originalOrderDate;
    private Date originalOrderTime;
    private Date revisedOrderDate;
    private Date revisedOrderTime;
    private String comments;
    private Long incomingTradingProfileId;
    private String localeCd;
    private String currencyCd;
    private Integer erpOrderNum;
    private Date addDate;
    private String addBy;
    private Date modDate;
    private String modBy;
    private Long siteId;
    private Long accountId;
    private Long storeId;
    private String erpSystemCd;
    private Long preOrderId;
    private Long refOrderId;
    private BigDecimal totalRushCharge;
    private String orderBudgetTypeCd;
    private Date erpOrderDate;

    public OrderData() {
    }

    public OrderData(String exceptionInd, String orderNum, String refOrderNum, Long costCenterId, String costCenterName, String workflowInd, String workflowStatusCd, String accountErpNum, String siteErpNum, String requestPoNum, Long userId, String userFirstName, String userLastName, String orderSiteName, String orderContactName, String orderContactPhoneNum, String orderContactEmail, String orderContactFaxNum, Long contractId, String contractShortDesc, String orderTypeCd, String orderSourceCd, String orderStatusCd, String taxNum, BigDecimal originalAmount, BigDecimal totalPrice, BigDecimal totalFreightCost, BigDecimal totalMiscCost, BigDecimal totalTaxCost, BigDecimal totalCleanwiseCost, BigDecimal grossWeight, Date originalOrderDate, Date originalOrderTime, Date revisedOrderDate, Date revisedOrderTime, String comments, Long incomingTradingProfileId, String localeCd, String currencyCd, Integer erpOrderNum, Date addDate, String addBy, Date modDate, String modBy, Long siteId, Long accountId, Long storeId, String erpSystemCd, Long preOrderId, Long refOrderId, BigDecimal totalRushCharge, String orderBudgetTypeCd, Date erpOrderDate) {
        this.setExceptionInd(exceptionInd);
        this.setOrderNum(orderNum);
        this.setRefOrderNum(refOrderNum);
        this.setCostCenterId(costCenterId);
        this.setCostCenterName(costCenterName);
        this.setWorkflowInd(workflowInd);
        this.setWorkflowStatusCd(workflowStatusCd);
        this.setAccountErpNum(accountErpNum);
        this.setSiteErpNum(siteErpNum);
        this.setRequestPoNum(requestPoNum);
        this.setUserId(userId);
        this.setUserFirstName(userFirstName);
        this.setUserLastName(userLastName);
        this.setOrderSiteName(orderSiteName);
        this.setOrderContactName(orderContactName);
        this.setOrderContactPhoneNum(orderContactPhoneNum);
        this.setOrderContactEmail(orderContactEmail);
        this.setOrderContactFaxNum(orderContactFaxNum);
        this.setContractId(contractId);
        this.setContractShortDesc(contractShortDesc);
        this.setOrderTypeCd(orderTypeCd);
        this.setOrderSourceCd(orderSourceCd);
        this.setOrderStatusCd(orderStatusCd);
        this.setTaxNum(taxNum);
        this.setOriginalAmount(originalAmount);
        this.setTotalPrice(totalPrice);
        this.setTotalFreightCost(totalFreightCost);
        this.setTotalMiscCost(totalMiscCost);
        this.setTotalTaxCost(totalTaxCost);
        this.setTotalCleanwiseCost(totalCleanwiseCost);
        this.setGrossWeight(grossWeight);
        this.setOriginalOrderDate(originalOrderDate);
        this.setOriginalOrderTime(originalOrderTime);
        this.setRevisedOrderDate(revisedOrderDate);
        this.setRevisedOrderTime(revisedOrderTime);
        this.setComments(comments);
        this.setIncomingTradingProfileId(incomingTradingProfileId);
        this.setLocaleCd(localeCd);
        this.setCurrencyCd(currencyCd);
        this.setErpOrderNum(erpOrderNum);
        this.setAddDate(addDate);
        this.setAddBy(addBy);
        this.setModDate(modDate);
        this.setModBy(modBy);
        this.setSiteId(siteId);
        this.setAccountId(accountId);
        this.setStoreId(storeId);
        this.setErpSystemCd(erpSystemCd);
        this.setPreOrderId(preOrderId);
        this.setRefOrderId(refOrderId);
        this.setTotalRushCharge(totalRushCharge);
        this.setOrderBudgetTypeCd(orderBudgetTypeCd);
        this.setErpOrderDate(erpOrderDate);
    }

    @SequenceGenerator(name="generator", sequenceName="CLW_ORDER_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="ORDER_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
        setDirty(true);
    }

    
    @Column(name="EXCEPTION_IND", length=1)
    public String getExceptionInd() {
        return this.exceptionInd;
    }

    public void setExceptionInd(String exceptionInd) {
        this.exceptionInd = exceptionInd;
        setDirty(true);
    }

    
    @Column(name="ORDER_NUM", length=50)
    public String getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        setDirty(true);
    }

    
    @Column(name="REF_ORDER_NUM", length=50)
    public String getRefOrderNum() {
        return this.refOrderNum;
    }

    public void setRefOrderNum(String refOrderNum) {
        this.refOrderNum = refOrderNum;
        setDirty(true);
    }

    
    @Column(name="COST_CENTER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getCostCenterId() {
        return this.costCenterId;
    }

    public void setCostCenterId(Long costCenterId) {
        this.costCenterId = costCenterId;
        setDirty(true);
    }

    
    @Column(name="COST_CENTER_NAME", length=30)
    public String getCostCenterName() {
        return this.costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
        setDirty(true);
    }

    
    @Column(name="WORKFLOW_IND", length=30)
    public String getWorkflowInd() {
        return this.workflowInd;
    }

    public void setWorkflowInd(String workflowInd) {
        this.workflowInd = workflowInd;
        setDirty(true);
    }

    
    @Column(name="WORKFLOW_STATUS_CD", length=30)
    public String getWorkflowStatusCd() {
        return this.workflowStatusCd;
    }

    public void setWorkflowStatusCd(String workflowStatusCd) {
        this.workflowStatusCd = workflowStatusCd;
        setDirty(true);
    }

    
    @Column(name="ACCOUNT_ERP_NUM", length=30)
    public String getAccountErpNum() {
        return this.accountErpNum;
    }

    public void setAccountErpNum(String accountErpNum) {
        this.accountErpNum = accountErpNum;
        setDirty(true);
    }

    
    @Column(name="SITE_ERP_NUM", length=30)
    public String getSiteErpNum() {
        return this.siteErpNum;
    }

    public void setSiteErpNum(String siteErpNum) {
        this.siteErpNum = siteErpNum;
        setDirty(true);
    }

    
    @Column(name="REQUEST_PO_NUM")
    public String getRequestPoNum() {
        return this.requestPoNum;
    }

    public void setRequestPoNum(String requestPoNum) {
        this.requestPoNum = requestPoNum;
        setDirty(true);
    }

    
    @Column(name="USER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        setDirty(true);
    }

    
    @Column(name="USER_FIRST_NAME", length=50)
    public String getUserFirstName() {
        return this.userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
        setDirty(true);
    }

    
    @Column(name="USER_LAST_NAME", length=50)
    public String getUserLastName() {
        return this.userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
        setDirty(true);
    }

    
    @Column(name="ORDER_SITE_NAME")
    public String getOrderSiteName() {
        return this.orderSiteName;
    }

    public void setOrderSiteName(String orderSiteName) {
        this.orderSiteName = orderSiteName;
        setDirty(true);
    }

    
    @Column(name="ORDER_CONTACT_NAME", length=80)
    public String getOrderContactName() {
        return this.orderContactName;
    }

    public void setOrderContactName(String orderContactName) {
        this.orderContactName = orderContactName;
        setDirty(true);
    }

    
    @Column(name="ORDER_CONTACT_PHONE_NUM", length=30)
    public String getOrderContactPhoneNum() {
        return this.orderContactPhoneNum;
    }

    public void setOrderContactPhoneNum(String orderContactPhoneNum) {
        this.orderContactPhoneNum = orderContactPhoneNum;
        setDirty(true);
    }

    
    @Column(name="ORDER_CONTACT_EMAIL")
    public String getOrderContactEmail() {
        return this.orderContactEmail;
    }

    public void setOrderContactEmail(String orderContactEmail) {
        this.orderContactEmail = orderContactEmail;
        setDirty(true);
    }

    
    @Column(name="ORDER_CONTACT_FAX_NUM", length=30)
    public String getOrderContactFaxNum() {
        return this.orderContactFaxNum;
    }

    public void setOrderContactFaxNum(String orderContactFaxNum) {
        this.orderContactFaxNum = orderContactFaxNum;
        setDirty(true);
    }

    
    @Column(name="CONTRACT_ID", columnDefinition="number", precision=38, scale=0)
    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
        setDirty(true);
    }

    
    @Column(name="CONTRACT_SHORT_DESC", length=30)
    public String getContractShortDesc() {
        return this.contractShortDesc;
    }

    public void setContractShortDesc(String contractShortDesc) {
        this.contractShortDesc = contractShortDesc;
        setDirty(true);
    }

    
    @Column(name="ORDER_TYPE_CD", length=30)
    public String getOrderTypeCd() {
        return this.orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
        setDirty(true);
    }

    
    @Column(name="ORDER_SOURCE_CD", length=30)
    public String getOrderSourceCd() {
        return this.orderSourceCd;
    }

    public void setOrderSourceCd(String orderSourceCd) {
        this.orderSourceCd = orderSourceCd;
        setDirty(true);
    }

    
    @Column(name="ORDER_STATUS_CD", length=30)
    public String getOrderStatusCd() {
        return this.orderStatusCd;
    }

    public void setOrderStatusCd(String orderStatusCd) {
        this.orderStatusCd = orderStatusCd;
        setDirty(true);
    }

    
    @Column(name="TAX_NUM", length=15)
    public String getTaxNum() {
        return this.taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
        setDirty(true);
    }

    
    @Column(name="ORIGINAL_AMOUNT", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getOriginalAmount() {
        return this.originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
        setDirty(true);
    }

    
    @Column(name="TOTAL_PRICE", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        setDirty(true);
    }

    
    @Column(name="TOTAL_FREIGHT_COST", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalFreightCost() {
        return this.totalFreightCost;
    }

    public void setTotalFreightCost(BigDecimal totalFreightCost) {
        this.totalFreightCost = totalFreightCost;
        setDirty(true);
    }

    
    @Column(name="TOTAL_MISC_COST", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalMiscCost() {
        return this.totalMiscCost;
    }

    public void setTotalMiscCost(BigDecimal totalMiscCost) {
        this.totalMiscCost = totalMiscCost;
        setDirty(true);
    }

    
    @Column(name="TOTAL_TAX_COST", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalTaxCost() {
        return this.totalTaxCost;
    }

    public void setTotalTaxCost(BigDecimal totalTaxCost) {
        this.totalTaxCost = totalTaxCost;
        setDirty(true);
    }

    
    @Column(name="TOTAL_CLEANWISE_COST", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalCleanwiseCost() {
        return this.totalCleanwiseCost;
    }

    public void setTotalCleanwiseCost(BigDecimal totalCleanwiseCost) {
        this.totalCleanwiseCost = totalCleanwiseCost;
        setDirty(true);
    }

    
    @Column(name="GROSS_WEIGHT", columnDefinition="number", precision=15, scale=3)
    public BigDecimal getGrossWeight() {
        return this.grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ORIGINAL_ORDER_DATE", length=7)
    public Date getOriginalOrderDate() {
        return this.originalOrderDate;
    }

    public void setOriginalOrderDate(Date originalOrderDate) {
        this.originalOrderDate = originalOrderDate;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ORIGINAL_ORDER_TIME", length=7)
    public Date getOriginalOrderTime() {
        return this.originalOrderTime;
    }

    public void setOriginalOrderTime(Date originalOrderTime) {
        this.originalOrderTime = originalOrderTime;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REVISED_ORDER_DATE", length=7)
    public Date getRevisedOrderDate() {
        return this.revisedOrderDate;
    }

    public void setRevisedOrderDate(Date revisedOrderDate) {
        this.revisedOrderDate = revisedOrderDate;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REVISED_ORDER_TIME", length=7)
    public Date getRevisedOrderTime() {
        return this.revisedOrderTime;
    }

    public void setRevisedOrderTime(Date revisedOrderTime) {
        this.revisedOrderTime = revisedOrderTime;
        setDirty(true);
    }

    
    @Column(name="COMMENTS", length=1000)
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        setDirty(true);
    }

    
    @Column(name="INCOMING_TRADING_PROFILE_ID", columnDefinition="number", precision=38, scale=0)
    public Long getIncomingTradingProfileId() {
        return this.incomingTradingProfileId;
    }

    public void setIncomingTradingProfileId(Long incomingTradingProfileId) {
        this.incomingTradingProfileId = incomingTradingProfileId;
        setDirty(true);
    }

    
    @Column(name="LOCALE_CD", length=30)
    public String getLocaleCd() {
        return this.localeCd;
    }

    public void setLocaleCd(String localeCd) {
        this.localeCd = localeCd;
        setDirty(true);
    }

    
    @Column(name="CURRENCY_CD", length=30)
    public String getCurrencyCd() {
        return this.currencyCd;
    }

    public void setCurrencyCd(String currencyCd) {
        this.currencyCd = currencyCd;
        setDirty(true);
    }

    
    @Column(name="ERP_ORDER_NUM", columnDefinition="number", precision=8, scale=0)
    public Integer getErpOrderNum() {
        return this.erpOrderNum;
    }

    public void setErpOrderNum(Integer erpOrderNum) {
        this.erpOrderNum = erpOrderNum;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", length=7)
    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
        setDirty(true);
    }

    
    @Column(name="ADD_BY")
    public String getAddBy() {
        return this.addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", length=7)
    public Date getModDate() {
        return this.modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
        setDirty(true);
    }

    
    @Column(name="MOD_BY")
    public String getModBy() {
        return this.modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
        setDirty(true);
    }

    
    @Column(name="SITE_ID", columnDefinition="number", precision=32, scale=0)
    public Long getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
        setDirty(true);
    }

    
    @Column(name="ACCOUNT_ID", columnDefinition="number", precision=32, scale=0)
    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
        setDirty(true);
    }

    
    @Column(name="STORE_ID", columnDefinition="number", precision=38, scale=0)
    public Long getStoreId() {
        return this.storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
        setDirty(true);
    }

    
    @Column(name="ERP_SYSTEM_CD", length=30)
    public String getErpSystemCd() {
        return this.erpSystemCd;
    }

    public void setErpSystemCd(String erpSystemCd) {
        this.erpSystemCd = erpSystemCd;
        setDirty(true);
    }

    
    @Column(name="PRE_ORDER_ID", columnDefinition="number", precision=38, scale=0)
    public Long getPreOrderId() {
        return this.preOrderId;
    }

    public void setPreOrderId(Long preOrderId) {
        this.preOrderId = preOrderId;
        setDirty(true);
    }

    
    @Column(name="REF_ORDER_ID", columnDefinition="number", precision=32, scale=0)
    public Long getRefOrderId() {
        return this.refOrderId;
    }

    public void setRefOrderId(Long refOrderId) {
        this.refOrderId = refOrderId;
        setDirty(true);
    }

    
    @Column(name="TOTAL_RUSH_CHARGE", columnDefinition="number", precision=22, scale=8)
    public BigDecimal getTotalRushCharge() {
        return this.totalRushCharge;
    }

    public void setTotalRushCharge(BigDecimal totalRushCharge) {
        this.totalRushCharge = totalRushCharge;
        setDirty(true);
    }

    
    @Column(name="ORDER_BUDGET_TYPE_CD")
    public String getOrderBudgetTypeCd() {
        return this.orderBudgetTypeCd;
    }

    public void setOrderBudgetTypeCd(String orderBudgetTypeCd) {
        this.orderBudgetTypeCd = orderBudgetTypeCd;
        setDirty(true);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ERP_ORDER_DATE", length=7)
    public Date getErpOrderDate() {
        return this.erpOrderDate;
    }

    public void setErpOrderDate(Date erpOrderDate) {
        this.erpOrderDate = erpOrderDate;
        setDirty(true);
    }




}



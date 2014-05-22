package com.espendwise.manta.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.ContactView;
import com.espendwise.manta.model.view.DistributorConfigurationView;
import com.espendwise.manta.model.view.DistributorIdentView;
import com.espendwise.manta.model.view.DistributorInfoView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DistributorPropertyExtraCode;
import com.espendwise.manta.util.DistributorPropertyTypeCode;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.DistributorListViewCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;
import com.espendwise.manta.util.parser.Parse;

public class DistributorDAOImpl extends DAOImpl implements DistributorDAO{

    private static final Logger logger = Logger.getLogger(DistributorDAOImpl.class);

    public DistributorDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<DistributorListView> findDistributorsByCriteria(DistributorListViewCriteria criteria) {

        logger.info("findDistributorsByCriteria()=> BEGIN, criteria: "+criteria);

        StringBuilder baseQuery = new StringBuilder("select distinct new com.espendwise.manta.model.view.DistributorListView(");
        baseQuery.append(" distributor.busEntityId, distributor.shortDesc, distributor.busEntityStatusCd,");
        baseQuery.append(" address.address1, address.address2, address.address3, address.address4,"); 
        baseQuery.append(" address.countryCd, address.countyCd, address.city, address.stateProvinceCd, address.postalCode) ");
        baseQuery.append(" from com.espendwise.manta.model.fullentity.BusEntityFullEntity distributor ");
        baseQuery.append(" left outer join distributor.addresses address with address.addressTypeCd = (:primaryContactAddressCd) ");
        baseQuery.append(" inner join distributor.busEntityAssocsForBusEntity1Id storeDistributor ");
        baseQuery.append(" where storeDistributor.busEntity2Id.busEntityId = (:storeId) and storeDistributor.busEntityAssocCd = (:storeDistributorAssocCd)");

        if (Utility.isSet(criteria.getDistributorId())) {
            baseQuery.append(" and distributor.busEntityId = ").append(Parse.parseLong(criteria.getDistributorId()));
        }

        if (Utility.isSet(criteria.getDistributorName())) {
        	if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getDistributorNameFilterType())) {
                baseQuery.append(" and UPPER(distributor.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getDistributorName().toUpperCase()))
                        .append("'");
            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getDistributorNameFilterType())) {
                baseQuery.append(" and UPPER(distributor.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getDistributorName().toUpperCase()))
                        .append("'");
            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and distributor.busEntityStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }

        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("primaryContactAddressCd", RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("storeDistributorAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE);

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<DistributorListView> r = q.getResultList();

        logger.info("findDistributorsByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;

    }

     @Override
    public DistributorIdentView findDistributorToEdit(Long storeId, Long distributorId) {

        StoreDistributorCriteria criteria = new StoreDistributorCriteria();
        criteria.setStoreId(storeId);
        criteria.setDistributorIds(Utility.toList(distributorId));

        List<BusEntityData> distributors = findDistributors(criteria);

        if(!distributors.isEmpty())  {
            return  pickupDistributorIdentView(distributors.get(0));
        } else {
           return null;
        }

    }

     @Override
    public List<BusEntityData> findDistributors(StoreDistributorCriteria criteria) {

        logger.info("findDistributors()=> BEGIN, criteria "+criteria);

        StringBuilder baseQuery = new StringBuilder("Select distributor from BusEntityData distributor");

        String andOrWhere = " where ";
        
        if (criteria.getStoreId() != null) {
            baseQuery.append(", BusEntityAssocData distributorStore");
            baseQuery.append(andOrWhere);
            andOrWhere = " and ";
            baseQuery.append(" distributor.busEntityId = distributorStore.busEntity1Id")
                    .append(" and distributorStore.busEntity2Id = (:storeId)")
                    .append(" and distributorStore.busEntityAssocCd = (:distributorStoreCd)") ;
        }

        if (Utility.isSet(criteria.getDistributorNames())) {
            baseQuery.append(andOrWhere);
            andOrWhere = " and ";
            baseQuery.append(" upper(distributor.shortDesc) in (:distributorNames)");
        }

        if (Utility.isSet(criteria.getDistributorIds())) {
            baseQuery.append(andOrWhere);
            andOrWhere = " and ";
            baseQuery.append(" distributor.busEntityId in (:distributorIds)");
        }

        Query q = em.createQuery(baseQuery.toString());

        if (criteria.getStoreId() != null) {
            q.setParameter("storeId", criteria.getStoreId());
            q.setParameter("distributorStoreCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE);
        }

        if (Utility.isSet(criteria.getDistributorNames())) {
        	//uppercase the distributor names so the comparison is case-insensitive
        	List<String> upperCaseNames = new ArrayList<String>();
        	Iterator<String> distributorNameIterator = criteria.getDistributorNames().iterator();
        	while (distributorNameIterator.hasNext()) {
        		upperCaseNames.add(distributorNameIterator.next().toUpperCase());
        	}
            q.setParameter("distributorNames", upperCaseNames);
        }

        if (Utility.isSet(criteria.getDistributorIds())) {
            q.setParameter("distributorIds", criteria.getDistributorIds());
        }

        return (List<BusEntityData>) q.getResultList();
    }

    @Override
    public DistributorIdentView saveDistributor(Long storeId, DistributorIdentView distributorView) throws DatabaseUpdateException {

        logger.info("saveDistributor()=> BEGIN, DistributorIdentView "+distributorView);

        BusEntityData be = distributorView.getBusEntityData();
        ContactView contact = distributorView.getContact();
        List<PropertyData> properties = distributorView.getProperties();

        if (be.getBusEntityId() == null) {

            be = saveDistributorBusEntity(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {

                if (createDistributorOfStoreAssoc(storeId, be.getBusEntityId())) {

                    contact = createOrUpdateDistributorContact(be.getBusEntityId(),
                            contact
                    );

                    properties = createOrUpdateDistributorProperties(be.getBusEntityId(),
                            properties
                    );

                } else {

                    throw new DatabaseUpdateException();

                }

            } else {

                throw new DatabaseUpdateException();

            }

        } else {

            be = saveDistributorBusEntity(be);

            contact = createOrUpdateDistributorContact(be.getBusEntityId(), contact);
            properties = createOrUpdateDistributorProperties(be.getBusEntityId(), properties);

        }


        distributorView.setBusEntityData(be);
        distributorView.setContact(contact);
        distributorView.setProperties(properties);

        logger.info("saveDistributor()=> END");
        return distributorView;
    }


    private boolean createDistributorOfStoreAssoc(Long storeId, Long busEntityId) {

        BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);

        BusEntityAssocData assoc = busEntityAssocDao.createAssoc(busEntityId,
                storeId,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE
        );

        return assoc != null && Utility.longNN(assoc.getBusEntityAssocId()) > 0;
    }


    private BusEntityData saveDistributorBusEntity(BusEntityData be) {
        if (be.getBusEntityId() == null) {
          be = super.create(be);
          //set the ERP num field, since that is what St.John did
          be.setErpNum(Constants.CHARS.POUND + be.getBusEntityId());
          super.update(be);
        } else {
            be = super.update(be);
        }
        return be;
    }


   @Override
    public EntityHeaderView findDistributorHeader(Long distributorId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(distributor.busEntityId, distributor.shortDesc)" +
                " from  BusEntityData distributor where distributor.busEntityId = (:distributorId) "
        );

        q.setParameter("distributorId", distributorId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

   public DistributorConfigurationView findDistributorConfigurationInformation(Long distributorId) {
	   DistributorConfigurationView returnValue = new DistributorConfigurationView(distributorId);
       PropertyDAO propertyDao = new PropertyDAOImpl(em);
       List<PropertyData> properties = propertyDao.findEntityProperties(distributorId, Utility.typeCodes(DistributorPropertyTypeCode.values()));
       //only return those properties created/modified via the configuration page
       Iterator<PropertyData> propertyIterator = properties.iterator();
       while (propertyIterator.hasNext()) {
    	   PropertyData property = propertyIterator.next();
    	   boolean isConfigurationProperty = RefCodeNames.PROPERTY_TYPE_CD.EXCEPTION_ON_TAX_DIFFERENCE.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.ERROR_ON_OVERCHARGED_FREIGHT.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.INVOICE_LOADING_PRICE_MODEL_CD.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.ALLOW_FREIGHT_ON_BACKORDERS.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.CANCEL_BACKORDERED_LINES.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.DO_NOT_ALLOW_INVOICE_EDITS.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.RECEIVING_SYSTEM_INVOICE_CD.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.IGNORE_ORDER_MIN_FOR_FREIGHT.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.INVOICE_AMT_PERCNT_ALLOW_LOWER.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.INVOICE_AMT_PERCNT_ALLOW_UPPER.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.DIST_MAX_INVOICE_FREIGHT.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.HOLD_INVOICE.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.PRINT_CUST_CONTACT_ON_PO.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.MAN_PO_ACK_REQUIERED.equalsIgnoreCase(property.getShortDesc()) ||
       				RefCodeNames.PROPERTY_TYPE_CD.PURCHASE_ORDER_COMMENTS.equalsIgnoreCase(property.getShortDesc());
    	   if (!isConfigurationProperty) {
       			propertyIterator.remove();
    	   }
       }
       
       EmailData rejectedEmail = null;
       Query q = em.createQuery("select emails from EmailData emails" +
               " where emails.busEntityId = (:distributorId)" +
    		   " and emails.emailTypeCd = (:emailTypeCd)");
       q.setParameter("distributorId", distributorId);
       q.setParameter("emailTypeCd", RefCodeNames.EMAIL_TYPE_CD.REJECTED_INVOICE);
       List<EmailData> r = (List<EmailData>) q.getResultList();
       if (!r.isEmpty()) {
    	   rejectedEmail = r.get(0);
       }

	   //populate returnvalue from properties and email
       propertyIterator = properties.iterator();
       while (propertyIterator.hasNext()) {
    	   PropertyData property = propertyIterator.next();
       		if (RefCodeNames.PROPERTY_TYPE_CD.EXCEPTION_ON_TAX_DIFFERENCE.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setPerformSalesTaxCheck(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.ERROR_ON_OVERCHARGED_FREIGHT.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setExceptionOnOverchargedFreight(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.INVOICE_LOADING_PRICE_MODEL_CD.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setInvoiceLoadingPricingModel(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.ALLOW_FREIGHT_ON_BACKORDERS.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setAllowFreightOnBackOrders(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.CANCEL_BACKORDERED_LINES.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setCancelBackorderedLines(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.DO_NOT_ALLOW_INVOICE_EDITS.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setDisallowInvoiceEdits(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.RECEIVING_SYSTEM_INVOICE_CD.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setReceivingSystemTypeCode(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.IGNORE_ORDER_MIN_FOR_FREIGHT.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setIgnoreOrderMinimumForFreight(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.INVOICE_AMT_PERCNT_ALLOW_LOWER.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setInvoiceAmountPercentUndercharge(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.INVOICE_AMT_PERCNT_ALLOW_UPPER.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setInvoiceAmountPercentOvercharge(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.DIST_MAX_INVOICE_FREIGHT.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setInvoiceMaximumFreightAllowance(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.HOLD_INVOICE.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setInboundInvoiceHoldDays(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.PRINT_CUST_CONTACT_ON_PO.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setPrintCustomerContactInfoOnPurchaseOrder(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.MAN_PO_ACK_REQUIERED.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setRequireManualPurchaseOrderAcknowledgement(property);
       		}
       		else if (RefCodeNames.PROPERTY_TYPE_CD.PURCHASE_ORDER_COMMENTS.equalsIgnoreCase(property.getShortDesc())) {
       			returnValue.setPurchaseOrderComments(property);
       		}
       }
       returnValue.setRejectedInvoiceEmailNotification(rejectedEmail);
       return returnValue;
   }

   public DistributorConfigurationView saveDistributorConfigurationInformation(Long distributorId, DistributorConfigurationView configuration) {
	   //save the properties
	   List<PropertyData> properties = new ArrayList<PropertyData>();
	   if (Utility.isSet(configuration.getPerformSalesTaxCheck())) {
		   properties.add(configuration.getPerformSalesTaxCheck());
	   }
	   if (Utility.isSet(configuration.getExceptionOnOverchargedFreight())) {
		   properties.add(configuration.getExceptionOnOverchargedFreight());
	   }
	   if (Utility.isSet(configuration.getInvoiceLoadingPricingModel())) {
		   properties.add(configuration.getInvoiceLoadingPricingModel());
	   }
	   if (Utility.isSet(configuration.getAllowFreightOnBackOrders())) {
		   properties.add(configuration.getAllowFreightOnBackOrders());
	   }
	   if (Utility.isSet(configuration.getCancelBackorderedLines())) {
		   properties.add(configuration.getCancelBackorderedLines());
	   }
	   if (Utility.isSet(configuration.getDisallowInvoiceEdits())) {
		   properties.add(configuration.getDisallowInvoiceEdits());
	   }
	   if (Utility.isSet(configuration.getReceivingSystemTypeCode())) {
		   properties.add(configuration.getReceivingSystemTypeCode());
	   }
	   if (Utility.isSet(configuration.getIgnoreOrderMinimumForFreight())) {
		   properties.add(configuration.getIgnoreOrderMinimumForFreight());
	   }
	   if (Utility.isSet(configuration.getInvoiceAmountPercentUndercharge())) {
		   properties.add(configuration.getInvoiceAmountPercentUndercharge());
	   }
	   if (Utility.isSet(configuration.getInvoiceAmountPercentOvercharge())) {
		   properties.add(configuration.getInvoiceAmountPercentOvercharge());
	   }
	   if (Utility.isSet(configuration.getInvoiceMaximumFreightAllowance())) {
		   properties.add(configuration.getInvoiceMaximumFreightAllowance());
	   }
	   if (Utility.isSet(configuration.getInboundInvoiceHoldDays())) {
		   properties.add(configuration.getInboundInvoiceHoldDays());
	   }
	   if (Utility.isSet(configuration.getPrintCustomerContactInfoOnPurchaseOrder())) {
		   properties.add(configuration.getPrintCustomerContactInfoOnPurchaseOrder());
	   }
	   if (Utility.isSet(configuration.getRequireManualPurchaseOrderAcknowledgement())) {
		   properties.add(configuration.getRequireManualPurchaseOrderAcknowledgement());
	   }
	   if (Utility.isSet(configuration.getPurchaseOrderComments())) {
		   properties.add(configuration.getPurchaseOrderComments());
	   }
       createOrUpdateDistributorProperties(distributorId,properties);

       //save the rejected invoice email address
       if (Utility.isSet(configuration.getRejectedInvoiceEmailNotification())) {
    	   EmailDAO emailDao = new EmailDAOImpl(em);
    	   emailDao.updateEntityAddress(distributorId, configuration.getRejectedInvoiceEmailNotification());
       }

	   return findDistributorConfigurationInformation(distributorId);
   }

    private DistributorIdentView pickupDistributorIdentView(BusEntityData busEntityData) {

        if (busEntityData == null || !(Utility.longNN(busEntityData.getBusEntityId()) > 0)) {
            return null;
        }

        ContactView contact = findContact(busEntityData.getBusEntityId());
        List<PropertyData> properties = findDistributorProperty(busEntityData.getBusEntityId());

        DistributorIdentView distributorIdentView = new DistributorIdentView();
        distributorIdentView.setBusEntityData(busEntityData);
        distributorIdentView.setContact(contact);
        distributorIdentView.setProperties(properties);
  
        return distributorIdentView;
    }

    private  List<PropertyData> findDistributorProperty(Long busEntityId) {
        PropertyDAO propertyDao = new PropertyDAOImpl(em);
        List<PropertyData> properties = propertyDao.findEntityProperties(busEntityId, Utility.typeCodes(DistributorPropertyTypeCode.values()));
        //only return those properties created when a distributor is created, not those created via the configuration page
        Iterator<PropertyData> propertyIterator = properties.iterator();
        while (propertyIterator.hasNext()) {
        	PropertyData property = propertyIterator.next();
        	if (!RefCodeNames.PROPERTY_TYPE_CD.CALL_HOURS.equalsIgnoreCase(property.getShortDesc()) &&
        		!RefCodeNames.PROPERTY_TYPE_CD.DIST_MAX_INVOICE_FREIGHT.equalsIgnoreCase(property.getShortDesc()) &&
    			!RefCodeNames.PROPERTY_TYPE_CD.RUNTIME_DISPLAY_NAME.equalsIgnoreCase(property.getShortDesc()) &&
        		!RefCodeNames.PROPERTY_TYPE_CD.EXCEPTION_ON_TAX_DIFFERENCE.equalsIgnoreCase(property.getShortDesc()) &&
            	!RefCodeNames.PROPERTY_TYPE_CD.IGNORE_ORDER_MIN_FOR_FREIGHT.equalsIgnoreCase(property.getShortDesc()) &&
                !RefCodeNames.PROPERTY_TYPE_CD.DISTRIBUTORS_COMPANY_CODE.equalsIgnoreCase(property.getShortDesc()) &&
                !RefCodeNames.PROPERTY_TYPE_CD.HOLD_INVOICE.equalsIgnoreCase(property.getShortDesc()) &&
                !RefCodeNames.PROPERTY_TYPE_CD.CUSTOMER_REFERENCE_CODE.equalsIgnoreCase(property.getShortDesc())) {
        		propertyIterator.remove();
        	}
        }
        List<PropertyData> extraProperties = propertyDao.findEntityNamedProperties(busEntityId, Utility.nameExtraCodes(DistributorPropertyExtraCode.values()));
        return PropertyUtil.joins(properties, extraProperties);
    }


     private ContactView findContact(Long distributorId) {

            logger.info("findContact=> BEGIN, distributorId: " + distributorId);

            StringBuilder query = new StringBuilder(100);
            query.append("Select new com.espendwise.manta.model.view.ContactView(");
            query.append("address, phone, faxPhone, mobilePhone, email)");
            query.append(" from  EntityContactEntity contact ");
            query.append(" left join contact.address address  with address.addressTypeCd in (:addressTypeProperty)");
            query.append(" left join contact.phone phone with phone.phoneTypeCd  = (:phoneTypeProperty) and phone.primaryInd = (:phonePrimaryInd)");
            query.append(" left join contact.phone faxPhone with faxPhone.phoneTypeCd  = (:faxTypeProperty)");
            query.append(" left join contact.phone mobilePhone with mobilePhone.phoneTypeCd  = (:mobileTypeProperty)");
            query.append(" left join contact.email email with email.emailTypeCd  = (:emailTypeProperty)");
            query.append(" where contact.busEntityId = (:distributorId)");
            Query q = em.createQuery(query.toString());

            q.setParameter("addressTypeProperty",  Utility.toList(RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT));
            q.setParameter("phoneTypeProperty", RefCodeNames.PHONE_TYPE_CD.PHONE);
            q.setParameter("phonePrimaryInd", true);
            q.setParameter("faxTypeProperty", RefCodeNames.PHONE_TYPE_CD.FAX);
            q.setParameter("mobileTypeProperty", RefCodeNames.PHONE_TYPE_CD.MOBILE);
            q.setParameter("emailTypeProperty", RefCodeNames.EMAIL_TYPE_CD.PRIMARY_CONTACT);
            q.setParameter("distributorId", distributorId);

            List contacts = q.getResultList();

            return !contacts.isEmpty() ? (ContactView) contacts.get(0) : null;
        }


     private ContactView createOrUpdateDistributorContact(Long busEntityId, ContactView contact) {

         AddressData address = contact.getAddress();
         PhoneData phone = contact.getPhone();
         PhoneData fax = contact.getFaxPhone();
         //PhoneData mobile = contact.getMobilePhone();
         EmailData email = contact.getEmail();

         AddressDAO addressDao = new AddressDAOImpl(em);
         PhoneDAO phoneDao = new PhoneDAOImpl(em);
         EmailDAO emailDao = new EmailDAOImpl(em);

         address = addressDao.updateEntityAddress(busEntityId, address);
         phone = phoneDao.updateEntityPhone(busEntityId, phone);
         fax = phoneDao.updateEntityPhone(busEntityId, fax);
         //mobile = phoneDao.updateEntityPhone(busEntityId, mobile);
         email = emailDao.updateEntityAddress(busEntityId, email);

         contact.setAddress(address);
         contact.setPhone(phone);
         contact.setFaxPhone(fax);
         //contact.setMobilePhone(mobile);
         contact.setEmail(email);

         return contact;

     }

     private List<PropertyData> createOrUpdateDistributorProperties(Long distributorId, List<PropertyData> properties) {
         PropertyDAO propertyDAO = new PropertyDAOImpl(em);
         return propertyDAO.updateEntityProperties(distributorId, properties);
     }
     
    @Override
    public DistributorInfoView findDistributorInfo(Long distributorId) {
        DistributorInfoView distributorInfo = null;
        
        if (Utility.isSet(distributorId)) {
            Query q = em.createQuery("Select object(distributor) from BusEntityData distributor" +
                    " where distributor.busEntityId = (:distributorId)");

            q.setParameter("distributorId", distributorId);

            List<BusEntityData> distributors = (List<BusEntityData>) q.getResultList();

            if (Utility.isSet(distributors)) {
                distributorInfo = pickupDistributorInfoView(distributors.get(0));
            }
        }
    
        return distributorInfo;
    }
    
    private DistributorInfoView pickupDistributorInfoView(BusEntityData distributorData) {

        if (distributorData == null || !(Utility.longNN(distributorData.getBusEntityId()) > 0)) {
            return null;
        }        
        
        DistributorInfoView distributorInfoView = new DistributorInfoView();
        distributorInfoView.setBusEntityData(distributorData);

        List<AddressData> addresses = findDistributorAddresses(distributorData.getBusEntityId());
        if (Utility.isSet(addresses)) {
            distributorInfoView.setPrimaryAddress(addresses.get(0));
        }
        
        return distributorInfoView;
    }
    
    private List<AddressData> findDistributorAddresses(Long distributorId) {
        Query q = em.createQuery("Select object(address) from AddressData address" +
                " where address.addressStatusCd = (:addressStatusCd)" +
                " and address.busEntityId =(:distributorId)" + 
                " and address.addressTypeCd =(:addressTypeCd)");
        
        q.setParameter("distributorId", distributorId);
        q.setParameter("addressTypeCd", RefCodeNames.ADDRESS_TYPE_CD.PRIMARY_CONTACT);
        q.setParameter("addressStatusCd", RefCodeNames.ADDRESS_STATUS_CD.ACTIVE);

        List<AddressData> addresses = (List<AddressData>) q.getResultList();
        
        return addresses;
    }

}

package com.espendwise.manta.util;


import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

import com.espendwise.manta.service.ServiceTypeService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.web.controllers.WorkflowController;
import com.espendwise.manta.model.data.WoServiceTypeCategoryData;
import com.espendwise.manta.model.data.WoServiceTypeData;


public class ServiceTypeUtil {

    public final static String NAME_DIVIDER = "-=I=-";
    private static final Logger logger = Logger.getLogger(ServiceTypeUtil.class);

    /**
     * Builds a full name of the ServiceType with use of a short name
     * and a of long (admin) name of ServiceType.
     */
    public final static String buildFullServiceTypeName(String ServiceTypeName,
        String adminServiceTypeName) {
        String ServiceTypeNameEx = (ServiceTypeName == null) ? "" : ServiceTypeName;
        if (adminServiceTypeName != null) {
            if (adminServiceTypeName.length() > 0) {
                ServiceTypeNameEx = ServiceTypeNameEx + " (" +
                    adminServiceTypeName + ")";
            }
        }
        return ServiceTypeNameEx;
    }

    /**
     * Builds a string which contains a short and admin name of a ServiceType.
     * Such string is used in controls on JSP-pages.
     */
    public final static String encodeServiceTypeNames(String ServiceTypeName,
        String adminServiceTypeName) {
        if (ServiceTypeName == null) {
            return ServiceTypeName;
        }
        String ServiceTypeValue = ServiceTypeName;
        if (adminServiceTypeName != null && adminServiceTypeName.length() > 0) {
            ServiceTypeValue = ServiceTypeValue + NAME_DIVIDER + adminServiceTypeName;
        }
        return ServiceTypeValue;
    }

    /**
     * Finds in string (which should be constructed with use 'encodeServiceTypeNames(...)')
     * a short and admin name of a ServiceType.
     * The first element of a returned array contains a short name of a ServiceType.
     * The second element of a returned array contains a admin name of a ServiceType.
     */
    public final static String[] decodeServiceTypeNames(String ServiceTypeValue) {
        String[] arrayNames = new String[]{"", ""};
        if (ServiceTypeValue == null)
            return arrayNames;
        if (ServiceTypeValue.length() == 0)
            return arrayNames;
        int pos = ServiceTypeValue.indexOf(NAME_DIVIDER);
        if (pos >= 0) {
            arrayNames[0] = ServiceTypeValue.substring(0, pos);
            arrayNames[1] = ServiceTypeValue.substring(pos + NAME_DIVIDER.length(), ServiceTypeValue.length());
        }
        else {
            arrayNames[0] = ServiceTypeValue;
        }
        return arrayNames;
    }

    /**
     * Compare two serviceType by short and admin names
     */
    public final static int compareServiceTypesByNames(String shortName1, String adminName1,
        String shortName2, String adminName2) {
        ServiceTypeKey key1 = new ServiceTypeKey(shortName1, adminName1);
        ServiceTypeKey key2 = new ServiceTypeKey(shortName2, adminName2);
        ServiceTypeComparatorByKey ServiceTypeComparator = new ServiceTypeComparatorByKey();
        return ServiceTypeComparator.compare(key1, key2);
    }

    /**
     * Builds a full name of the ServiceType by 'ServiceType-value'.
     * The 'ServiceType-value' are used at building of full ServiceType name.
     * The 'ServiceType-value' should be constructed by 'encodeServiceTypeNames' function.
     */
    public final static String buildFullServiceTypeNameByValue(String ServiceTypeValue) {
        if (ServiceTypeValue == null || ServiceTypeValue.length() == 0) {
            return ServiceTypeValue;
        }
        String[] arrayNames = decodeServiceTypeNames(ServiceTypeValue);
        if (arrayNames == null || arrayNames.length == 0) {
            return ServiceTypeValue;
        }
        String ServiceTypeShortName = "";
        String ServiceTypeAdminName = "";
        if (arrayNames.length > 0) {
            ServiceTypeShortName = arrayNames[0];
        }
        if (arrayNames.length > 1) {
            ServiceTypeAdminName = arrayNames[1];
        }
        return buildFullServiceTypeName(ServiceTypeShortName, ServiceTypeAdminName);
    }

    public static String getInactiveCategoryPrefix( String typeCd, String catName) {
       final String prefix ="[ ";
       final String suffix = " ]";
       String label = (RefCodeNames.SERVICE_TYPE_CATEG_STATUS_CD.INACTIVE.equals(typeCd)) ?(prefix + catName +suffix) : catName;
       return  label;

   }

   public static final Comparator SERVICE_TYPE_COMPARATOR = new Comparator() {
       public int compare(Object o1, Object o2) {
           WoServiceTypeData c1 = (WoServiceTypeData) o1;
           WoServiceTypeData c2 = (WoServiceTypeData) o2;
           if (c1 != null && c2 != null) {
               String value1 = c1.getServiceTypeStatusCd()+ c1.getShortDesc() + c1.getAdminName();
               String value2 = c2.getServiceTypeStatusCd()+ c2.getShortDesc() + c2.getAdminName();
               return Utility.compareToIgnoreCase(value1, value2);
           }
           return 0;
       }
   };
   public static final Comparator SERVICE_TYPE_CATEGORY_COMPARATOR = new Comparator() {
       public int compare(Object o1, Object o2) {
           WoServiceTypeCategoryData c1 = (WoServiceTypeCategoryData) o1;
           WoServiceTypeCategoryData c2 = (WoServiceTypeCategoryData) o2;
           if (c1 != null && c2 != null) {
               String value1 = c1.getCategoryStatusCd()+ c1.getShortDesc() + c1.getAdminName();
               String value2 = c2.getCategoryStatusCd()+ c2.getShortDesc() + c2.getAdminName();
               return Utility.compareToIgnoreCase(value1, value2);
           }
           return 0;
       }
   };

    public static String getServiceTypeName( String serviceTypeIdStr) throws Exception {
        String stName ="";
        if (Utility.isSet(serviceTypeIdStr)){
            Long serviceTypeId = Long.parseLong(serviceTypeIdStr);
            
            WoServiceTypeData stD = getServiceTypeService().findServiceType(serviceTypeId);
            if (stD != null){
                stName = ServiceTypeUtil.getInactiveCategoryPrefix(stD.getServiceTypeStatusCd(),
                       ServiceTypeUtil.buildFullServiceTypeName(stD.getShortDesc(), stD.getAdminName()));
            }
        }
        return stName;
    }
 
    public static List<WoServiceTypeData> getServiceTypes( String serviceTypeIdsStr) throws Exception {
    	List<WoServiceTypeData> stDV = null;
        if (Utility.isSet(serviceTypeIdsStr)){
        	String[] els = serviceTypeIdsStr.split(",");
        	List<Long> ids = new ArrayList<Long>();
        	for (int i=0; i<els.length; i++){
        		int id = 0;
        		try {
        			id = Integer.parseInt(els[i]);
        		} catch (Exception e) {
        			id = 0;
        		}
        		if (id > 0) {
        			ids.add(new Long(id));
        		}
        	}
        	if (!ids.isEmpty()){
        		if (getServiceTypeService() != null){
	             stDV = getServiceTypeService().findServiceTypeCollection(ids);
        		} 
        	}
        }
        logger.info("getServiceTypes()==> stDV= " + stDV);
        return stDV;
    }
    public static String getServiceTypeNames( String serviceTypeIdsStr) throws Exception {
        String serviceTypeNamesStr ="";
        List<WoServiceTypeData> stDV = getServiceTypes(serviceTypeIdsStr);
        if (stDV != null){
        	for (WoServiceTypeData stD : stDV){
        		serviceTypeNamesStr += ((Utility.isSet(serviceTypeNamesStr)) ? ", " : "")+ stD.getShortDesc();
        		
        	}
       	}
        return serviceTypeNamesStr;
    }
    
    public static ServiceTypeService getServiceTypeService() {
        return ServiceLocator.getServiceTypeService();
    }

    /*##########################################################*/
        
    public static class ServiceTypeKey {
        public ServiceTypeKey(String shortName, String adminName) {
            _shortName = shortName;
            _adminName = adminName;
        }
        
        public ServiceTypeKey(String shortName) {
            this(shortName, "");
        }
        
        public ServiceTypeKey() {
            this("", "");
        }
        
        public String GetShortName() {
            return _shortName;
        }
        
        public String GetAdminName() {
            return _adminName;
        }
        
        public void SetShortName(String shortName) {
            _shortName = shortName;
        }
        
        public void SetAdminName(String adminName) {
            _adminName = adminName;
        }
        
        public int hashCode() {
            if (_shortName == null && _adminName == null) {
                return 0;
            }
            if (_shortName != null && _adminName == null) {
                return 15 * _shortName.hashCode();
            }
            if (_shortName == null && _adminName != null) {
                return _adminName.hashCode();
            }
            return 15 * _shortName.hashCode() + _adminName.hashCode();
        }
        
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof ServiceTypeKey) {
                ServiceTypeKey otherKey = (ServiceTypeKey)obj;
                ServiceTypeComparatorByKey ServiceTypeComparator = new ServiceTypeComparatorByKey();
                return (ServiceTypeComparator.compare(this, otherKey) == 0);
            }
            return false;
        }
        
        public String toString() {
            StringBuffer strBuff = new StringBuffer();
            strBuff.append((_shortName == null) ? "null" : _shortName);
            strBuff.append(",");
            strBuff.append((_adminName == null) ? "null" : _adminName);
            return strBuff.toString();
        }
        
        private String _shortName;
        private String _adminName;
    }
/*#################################################################*/
    public static class ServiceTypeComparatorByKey implements Comparator<ServiceTypeKey> {
        
        public ServiceTypeComparatorByKey() {
        }
        
        public int compare(ServiceTypeKey o1, ServiceTypeKey o2) {
            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null && o2 != null)
                return -1;
            if (o1 != null && o2 == null)
                return 1;
            if (!isStrEmpty(o1.GetShortName()) && !isStrEmpty(o2.GetShortName())) {
                int cmpShort = o1.GetShortName().compareToIgnoreCase(o2.GetShortName());
                if (cmpShort == 0) {
                    if (isStrEmpty(o1.GetAdminName()) && isStrEmpty(o2.GetAdminName())) {
                        return 0;
                    }
                    if (!isStrEmpty(o1.GetAdminName()) && isStrEmpty(o2.GetAdminName())) {
                        return 1;
                    }
                    if (isStrEmpty(o1.GetAdminName()) && !isStrEmpty(o2.GetAdminName())) {
                        return -1;
                    }
                    if (!isStrEmpty(o1.GetAdminName()) && !isStrEmpty(o2.GetAdminName())) {
                        return o1.GetAdminName().compareToIgnoreCase(o2.GetAdminName());
                    }
                }
                else {
                    return cmpShort;
                }
            }
            if (!isStrEmpty(o1.GetShortName()) && isStrEmpty(o2.GetShortName())) {
                return 1;
            }
            if (isStrEmpty(o1.GetShortName()) && !isStrEmpty(o2.GetShortName())) {
                return -1;
            }
            if (isStrEmpty(o1.GetShortName()) && isStrEmpty(o2.GetShortName())) {
                if (isStrEmpty(o1.GetAdminName()) && isStrEmpty(o2.GetAdminName())) {
                    return 0;
                }
                if (!isStrEmpty(o1.GetAdminName()) && isStrEmpty(o2.GetAdminName())) {
                    return 1;
                }
                if (isStrEmpty(o1.GetAdminName()) && !isStrEmpty(o2.GetAdminName())) {
                    return -1;
                }
                if (!isStrEmpty(o1.GetAdminName()) && !isStrEmpty(o2.GetAdminName())) {
                    return o1.GetAdminName().compareToIgnoreCase(o2.GetAdminName());
                }
            }
            return 0;
        }
        
        public boolean equals(Object obj) {
            if (obj instanceof ServiceTypeComparatorByKey) {
                return true;
            }
            return false;
        }
        
        private boolean isStrEmpty(String strValue) {
            if (strValue == null) {
                return true;
            }
            if (strValue.length() == 0) {
                return true;
            }
            return false;
        }
    }

}

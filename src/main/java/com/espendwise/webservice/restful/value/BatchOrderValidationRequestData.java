package com.espendwise.webservice.restful.value;

import java.io.Serializable;


public class BatchOrderValidationRequestData extends BaseRequestData implements Serializable, Cloneable {
    private static final long serialVersionUID = -5660836849901793035L;
    private Integer storeId; 
    private Integer accountId; 
    private String fileName;
    private byte[] dataContents;
    

    public BatchOrderValidationRequestData() {}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setDataContents(byte[] dataContents) {
		this.dataContents = dataContents;
	}

	public byte[] getDataContents() {
		return dataContents;
	}
	
	public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    
    @Override
    public Object clone() {
        if (this instanceof BatchOrderValidationRequestData) {
            BatchOrderValidationRequestData myClone = new BatchOrderValidationRequestData();
            myClone.setAccessToken(this.accessToken);
            myClone.setLoginData(this.loginData);
            myClone.setStoreId(this.storeId);
            myClone.setAccountId(this.accountId);
            myClone.setFileName(this.fileName);
            myClone.setDataContents(this.dataContents);

            return myClone;
        } else {
            try {
                return super.clone();
            } catch (CloneNotSupportedException cnse) {
                throw new RuntimeException(cnse);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Object expand(Class clazz) {
        Object exp = null;
        Class superClass = clazz.getSuperclass();
        if (superClass.equals(this.getClass())) {
            try {
                exp = clazz.newInstance();
                
                ((BatchOrderValidationRequestData)exp).setAccessToken(this.accessToken);
                ((BatchOrderValidationRequestData)exp).setLoginData(this.loginData);
                ((BatchOrderValidationRequestData)exp).setStoreId(this.storeId);
                ((BatchOrderValidationRequestData)exp).setAccountId(this.accountId);
                ((BatchOrderValidationRequestData)exp).setFileName(this.fileName);
                ((BatchOrderValidationRequestData)exp).setDataContents(this.dataContents);
            
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        return exp;
    }    
}



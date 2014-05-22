package com.espendwise.manta.util;


import org.apache.log4j.Logger;

import com.espendwise.ocean.util.StorageSystem;
import com.espendwise.ocean.util.StorageSystemBackedContent;
import com.espendwise.ocean.util.StorageSystemE3;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

public class IOUtility {

    private static final Logger logger = Logger.getLogger(IOUtility.class);
    private static String P_STORAGE_URL = "storage.url";
    private static String P_STORAGE_PASSW = "storage.password";
    private static String P_STORAGE_USER = "storage.user";
    public static String P_STORAGE__LOGO1_CONTENT_PREFIX="storage.logo1.content.prefix";

    public static String getFileExt(String pFileName) {
        int i = Utility.isSet(pFileName) ? pFileName.lastIndexOf(".") : -1;
        return  i < 0 ? Constants.EMPTY : pFileName.substring(i);
    }

    public static byte[] toBytes(InputStream pIn) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            copyStream(pIn, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            close(baos);
        }
    }

    public static byte[] toBytes(File file) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            copyStream(new FileInputStream(file), baos);
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            close(baos);
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            out.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
        out.flush();
    }

    public static void closeStream(OutputStream pStream) {
        close(pStream);
    }

    public static void closeStream(InputStream pStream) {
        close(pStream);
    }

    private static void close(InputStream pStream) {
        if (pStream != null) {
            try {
                pStream.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }


    private static void close(OutputStream pStream) {
        if (pStream != null) {
            try {
                pStream.close();
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
    }
    public static Object getContentFromStorage (String pContentPrefix, String pContentName, String pFileName) throws Exception {

    	//	  	 checkStorageParameters(pParameterMap);
    	 if (!Utility.isSet(pContentName)) {
    		 return (new ByteArrayOutputStream()).toByteArray();
    	 }
//         String storageUrl= System.getProperty(P_STORAGE_URL);
//	     String storageUser= System.getProperty(P_STORAGE_USER);
//	     String storagePassword= System.getProperty(P_STORAGE_PASSW);
	     String storageUrl = AppResourceHolder.getAppResource().getApplicationSettings().getSettings(null, Constants.APPLICATION_SETTINGS.STORAGE_URL);
	     String storageUser = AppResourceHolder.getAppResource().getApplicationSettings().getSettings(null, Constants.APPLICATION_SETTINGS.STORAGE_USER);
	     String storagePassword = AppResourceHolder.getAppResource().getApplicationSettings().getSettings(null, Constants.APPLICATION_SETTINGS.STORAGE_PASSWORD);
	     
	     logger.info("getContentFromStorage() =====> storageUrl=" + storageUrl+ ", storageUser="+ storageUser+ ", storagePassword="+ storagePassword);
		 StorageSystem store = new StorageSystemE3(storageUrl,storageUser,storagePassword);
		 
		 StorageSystemBackedContent content = StorageSystemBackedContent.createContentFromStore(store, pFileName, null, pContentName);
		 //Properties metaData = content.getMetaData();
		 Properties metaData = getMetaData(pContentName, store);
		 String refFileName = metaData.getProperty(StorageSystemBackedContent.REF_FILE_NAME_KEY);
  	     logger.info("Get content with file name: "+refFileName);
		 
		 if (!pFileName.equalsIgnoreCase(refFileName)){
			 throw new IOException("getDataContent(): ERROR: " + "Incompatible filename for contentName = " + pContentName + ". [Parameter 'filename': "+ pFileName + ", REF_FILE_NAME_KEY: "+ refFileName+ "]");
		 }
		 
		 InputStream input = content.getInputStream();
		 if(input == null){
			 throw new IOException("getDataContent(): ERROR: " + "NO IO STREAM for contentName = " + pContentName);
		 }
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 
		 byte[] buf = new byte[1024];
		 int len;
		 while ((len = input.read(buf)) > 0) {
			out.write(buf, 0, len);
		 }
		 out.flush();
		 out.close();
		 byte[] dataContents = out.toByteArray(); 

		 return dataContents;
    }
    
	public static String setContentToStorage(String pContentPrefix, Object pDataContents, String pFileName) throws Exception{
  	     logger.info("IOUtility.setContentToStorage()===========> Begin.");
//	     checkStorageParameters(sendParameterMap);
  	     
	     String fileName = pFileName; //(String)pParameterMap.get(P_FILE_NAME);	
	     String storageUrl=  System.getProperty(P_STORAGE_URL);
	     String storageUser=  System.getProperty(P_STORAGE_USER);
	     String storagePassword= System.getProperty(P_STORAGE_PASSW);
  	     logger.info("IOUtility.setContentToStorage()===========> storageUrl="+storageUrl+", storageUser="+ storageUser+ ", storagePassword="+ storagePassword);
	     ByteArrayInputStream input = new ByteArrayInputStream((byte[])pDataContents); 
	     
	     StorageSystem store = new StorageSystemE3(storageUrl,storageUser,storagePassword);
		 
		 StorageSystemBackedContent content = StorageSystemBackedContent.createContentFromInputStream(store, fileName, null, input, pContentPrefix);
		 logger.info("Sending content: "+content.getContentName());
		 content.saveToStore();
		 logger.info("Saved content with content name: "+content.getContentName());
		 String name = content.getContentName();
		 String refFileName = content.getMetaData().getProperty(StorageSystemBackedContent.REF_FILE_NAME_KEY);
  	     logger.info("Saved content with file name: "+refFileName);
		 input.close();
//		 sendEvent(storeUnit, name, sendParameterMap);
  	     logger.info("IOUtility.setContentToStorage()===========> End.");
		 return name;
	}
	
	public static String extractContentName (String delimiter, String name) {
		String val = "";
		if (Utility.isSet(name) && name.indexOf(delimiter)>=0){
			val= name.substring(name.indexOf(delimiter) + delimiter.length());
		}
 	     logger.info("IOUtility.extractContentName()=" + val);
		return val;
	}
	public static String extractRefFileName (String delimiter, String name) {
		String val = "";
		if (Utility.isSet(name) && name.indexOf(delimiter)>=0){
			val= name.substring(0,name.indexOf(delimiter)-1);
		}
	     logger.info("IOUtility.extractRefFileName()=" + val);
		return val;
	}
	
    private static Properties getMetaData(String contentName, StorageSystem store ) throws Exception {
	   	 final String META_DATA_PREFIX = "x-amz-meta-";
	   	 final String META_DATA_CONTENT_PREFIX = "_ESW_METADATE";
	   	 InputStream metaContent = store.getContent(contentName+META_DATA_CONTENT_PREFIX) ;
	   	 Properties prop = new Properties();
	   	 prop.load(metaContent);
	//   	 log.info("getMetaData() ===> prop.entrySet()="+ prop.entrySet());
	   	 Iterator it = prop.keySet().iterator();
	   	 Properties metaData = new Properties();
	   	 while(it.hasNext()){
	   		 String xKey =(String)it.next();
	   		 String key = (xKey).replace(META_DATA_PREFIX, "");
	   		 String value = prop.getProperty(xKey);
	   		 metaData.put(key, value);
	   	 }
	   	 return metaData;
     }
        
    public final static Object bytesToObject(byte[] pBytes) {
        Object obj = null;
        java.io.ByteArrayInputStream iStream = new java.io.ByteArrayInputStream(
                pBytes);
        try {
            java.io.ObjectInputStream is = new java.io.ObjectInputStream(
                    iStream);
            obj = is.readObject();
            is.close();
            iStream.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return obj;
    }   
}

package com.espendwise.tools.gencode.dbxml;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DbXmlAssist {

    private final static Log log = LogFactory.getLog(DbXmlAssist.class);

    public static com.espendwise.tools.gencode.dbxml.Database parseDbXml(String pXmlFile)  {

        Database dbObj;

        
        
        log.info("parse()=> BEGIN");
     
        String[] files = pXmlFile.split(",");

        List<Database> dataObjList = new ArrayList<Database>() ;

        try {

            for (String file : files) {

                log.info("parse()=> file '"+file+"' processing...");

                 JAXBContext context = JAXBContext.newInstance(Database.class.getPackage().getName(), ObjectFactory.class.getClassLoader());

                // marshall into XML via System.out
                Unmarshaller marshaller = context.createUnmarshaller();
                Object obj = marshaller.unmarshal(new FileInputStream(file));
                dataObjList.add(((JAXBElement<Database>) obj).getValue());

                log.info("parse()=> OK!");

            }

            dbObj = merge(dataObjList);

        } catch (Exception ex) {
            log.error("Exception: " + ex.getMessage(), ex);
            return null;
        }

        log.info("parse()=> END.");

        return dbObj;
    }

    private static Database merge(List<Database> dataObjList) {

        if (dataObjList == null || dataObjList.isEmpty()) {
            return null;
        }

        Database dbObj = dataObjList.get(0);
        if (dataObjList.size() > 1) {
            for (int i = 1; i < dataObjList.size(); i++) {
                dbObj.getTable().addAll(dataObjList.get(i).getTable());
                dbObj.getFkey().addAll(dataObjList.get(i).getFkey());
                dbObj.getIndex().addAll(dataObjList.get(i).getIndex());
                dbObj.getSequence().addAll(dataObjList.get(i).getSequence());
            }
        }
        return dbObj;

    }

}

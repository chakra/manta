package com.espendwise.tools.gencode.hbmxml.single;

import com.espendwise.tools.gencode.dbxml.Database;
import com.espendwise.tools.gencode.dbxml.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class HbmXmlAssist {
  
    private final static Log log = LogFactory.getLog(HbmXmlAssist.class);

    public static String createHbmXml(Database pDbmObj) throws Exception {

        ObjectFactory hbmObjFactory  = new ObjectFactory();

        HibernateMapping hbmMapping = hbmObjFactory.createHibernateMapping();

        for(Table t:pDbmObj.getTable()){
            Class clazz = hbmObjFactory.createClass();
            clazz.setTable(t.getName());
            //clazz.setName(namesStrategy.tableToClassName(t));
            hbmMapping.getClazz().add(clazz); 
        }

       JAXBContext context = JAXBContext.newInstance(HibernateMapping.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(hbmMapping, baos);

        baos.close();

        return new String(baos.toByteArray()) ;
    }

    public static HibernateMapping parseHbmXml(byte[] pXml) throws Exception {

        HibernateMapping dataObj;

        log.info("parse()=> BEGIN");

            JAXBContext context = JAXBContext.newInstance(HibernateMapping.class.getPackage().getName(), ObjectFactory.class.getClassLoader());

            log.info("parse()=> xdm package: " + HibernateMapping.class.getPackage().getName());

            // marshall into XML via System.out
            Unmarshaller marshaller = context.createUnmarshaller();
            Object obj = marshaller.unmarshal(new ByteArrayInputStream( pXml));
        log.info("parse()=> obj: " + obj);
  dataObj = (HibernateMapping) obj;


        log.info("parse()=> END.");

        return dataObj;
    }

}

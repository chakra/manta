package com.espendwise.tools.gencode.hbmxml;

import com.espendwise.tools.gencode.util.GCUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.AbstractExporter;
import org.hibernate.tool.hbm2x.ExporterException;

import java.io.*;
import java.util.*;


public class AppConfigurationExporter extends AbstractExporter {

    private static final Log log = LogFactory.getLog(AppConfigurationExporter.class);

    private Properties customProperties = new Properties();


    public AppConfigurationExporter(Configuration configuration, File outputdir) {
        super(configuration, outputdir);
    }

    public AppConfigurationExporter() {
    }

    public Properties getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Properties customProperties) {
        this.customProperties = customProperties;
    }

    public static Date addDays(Date pDate, int pNumberOfDays) {

        Calendar cal = GregorianCalendar.getInstance();

        cal.setTime(pDate);
        cal.add(Calendar.DAY_OF_YEAR, pNumberOfDays);

        return cal.getTime();
    }
    public void doStart() throws ExporterException {

        try {

            StringBuilder buffer = new StringBuilder();

            List<File> files = getHbmXmls(this.getOutputDirectory());
            for(File file : files){
               String xmlStr = GCUtility.loadFile(file);
                buffer.append(xmlStr);
                buffer.append("\n");

            }

            String hbmXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n" +
                    "                                   \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n" +
                    "<hibernate-mapping>"+
                    "\n"+buffer+"\n"+
                    "</hibernate-mapping>";

            FileWriter writer = new FileWriter(this.getProperties().getProperty("destdir")+"/"+this.getProperties().getProperty("entities.hbm.xml"));
            writer.write(hbmXml);
            writer.close();
            
        } catch (Exception e) {
           throw new  ExporterException(e) ;
        }

    }
 
    private List<File> getHbmXmls(File dir){
        List<File> files = new ArrayList<File>();
        pickupHbmXmls(dir, files);
        return files;
    }
    
    private void pickupHbmXmls(File file, List<File> files) {
        if(file.isDirectory()){
            File[] dirFiles = file.listFiles();
            for(File f : dirFiles){
                pickupHbmXmls(f, files);
            }
        } else {
            files.add(file);
        }
    }


}
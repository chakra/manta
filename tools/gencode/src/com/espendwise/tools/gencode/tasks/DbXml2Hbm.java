package com.espendwise.tools.gencode.tasks;


import com.espendwise.tools.gencode.dbxml.DbXmlAssist;
import com.espendwise.tools.gencode.dbxml.Database;
import com.espendwise.tools.gencode.hbmxml.single.HbmXmlAssist;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


public class DbXml2Hbm extends Task {

    private final static Log log = LogFactory.getLog(DbXml2Hbm.class);

    private String outFile;
    private String dbXmlFile;

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public String getDbXmlFile() {
        return dbXmlFile;
    }

    public void setDbXmlFile(String dbXmlFile) {
        this.dbXmlFile = dbXmlFile;
    }


    public void execute() throws BuildException {

        log("#########################################################");
        log("# [DbXml2Hbm] PROPERTIES");
        log("#           dbXmlFile: " + dbXmlFile);
        log("#             outfile: " + outFile);
        log("#########################################################");

        
        try {

            Database dbmObj = DbXmlAssist.parseDbXml(getDbXmlFile());
            String xmlStr =    HbmXmlAssist.createHbmXml(dbmObj);

            log.info("execute()=> xmlStr: "+xmlStr);

        } catch (Exception ex) {
            ex.printStackTrace();
            log("Exception: " + ex.getMessage());
        }
    }



}

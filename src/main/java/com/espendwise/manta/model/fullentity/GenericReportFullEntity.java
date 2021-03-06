package com.espendwise.manta.model.fullentity;

// Generated by Hibernate Tools

import com.espendwise.manta.model.TableObject;
import com.espendwise.manta.model.ValueObject;
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
 * GenericReportFullEntity generated by hbm2java
*/
@Entity
@Table(name="CLW_GENERIC_REPORT")
public interface GenericReportFullEntity  {

    public static final String GENERIC_REPORT_ID = "genericReportId";
    public static final String CATEGORY = "category";
    public static final String NAME = "name";
    public static final String PARAMETER_TOKEN = "parameterToken";
    public static final String REPORT_SCHEMA_CD = "reportSchemaCd";
    public static final String INTERFACE_TABLE = "interfaceTable";
    public static final String SQL_TEXT = "sqlText";
    public static final String SCRIPT_TEXT = "scriptText";
    public static final String ADD_DATE = "addDate";
    public static final String ADD_BY = "addBy";
    public static final String MOD_DATE = "modDate";
    public static final String MOD_BY = "modBy";
    public static final String GENERIC_REPORT_TYPE = "genericReportType";
    public static final String CLASSNAME = "classname";
    public static final String SUPPLEMENTARY_CONTROLS = "supplementaryControls";
    public static final String RUNTIME_ENABLED = "runtimeEnabled";
    public static final String LONG_DESC = "longDesc";
    public static final String USER_TYPES = "userTypes";


    public void setGenericReportId(Long genericReportId);
    @SequenceGenerator(name="generator", sequenceName="CLW_GENERIC_REPORT_SEQ")
    @Id 
    @GeneratedValue(strategy=javax.persistence.GenerationType.SEQUENCE, generator="generator")     
    @Column(name="GENERIC_REPORT_ID", nullable=false, columnDefinition="number", precision=38, scale=0)
    public Long getGenericReportId();

    public void setCategory(String category);
    
    @Column(name="CATEGORY", length=50)
    public String getCategory();

    public void setName(String name);
    
    @Column(name="NAME", length=2000)
    public String getName();

    public void setParameterToken(String parameterToken);
    
    @Column(name="PARAMETER_TOKEN", length=10)
    public String getParameterToken();

    public void setReportSchemaCd(String reportSchemaCd);
    
    @Column(name="REPORT_SCHEMA_CD", length=30)
    public String getReportSchemaCd();

    public void setInterfaceTable(String interfaceTable);
    
    @Column(name="INTERFACE_TABLE", length=50)
    public String getInterfaceTable();

    public void setSqlText(String sqlText);
    
    @Column(name="SQL_TEXT", columnDefinition="clob")
    public String getSqlText();

    public void setScriptText(String scriptText);
    
    @Column(name="SCRIPT_TEXT", columnDefinition="clob")
    public String getScriptText();

    public void setAddDate(Date addDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ADD_DATE", length=7)
    public Date getAddDate();

    public void setAddBy(String addBy);
    
    @Column(name="ADD_BY")
    public String getAddBy();

    public void setModDate(Date modDate);
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DATE", length=7)
    public Date getModDate();

    public void setModBy(String modBy);
    
    @Column(name="MOD_BY")
    public String getModBy();

    public void setGenericReportType(String genericReportType);
    
    @Column(name="GENERIC_REPORT_TYPE")
    public String getGenericReportType();

    public void setClassname(String classname);
    
    @Column(name="CLASSNAME")
    public String getClassname();

    public void setSupplementaryControls(String supplementaryControls);
    
    @Column(name="SUPPLEMENTARY_CONTROLS", length=1000)
    public String getSupplementaryControls();

    public void setRuntimeEnabled(String runtimeEnabled);
    
    @Column(name="RUNTIME_ENABLED")
    public String getRuntimeEnabled();

    public void setLongDesc(String longDesc);
    
    @Column(name="LONG_DESC", length=2000)
    public String getLongDesc();

    public void setUserTypes(String userTypes);
    
    @Column(name="USER_TYPES", length=1000)
    public String getUserTypes();

}


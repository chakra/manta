
package com.espendwise.tools.gencode.hbmxml.persistence;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://java.sun.com/xml/ns/persistence}persistence-unit"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "persistenceUnit"
})
@XmlRootElement(name = "persistence", namespace = "http://java.sun.com/xml/ns/persistence")
public class Persistence {

    @XmlElement(name = "persistence-unit", namespace = "http://java.sun.com/xml/ns/persistence", required = true)
    protected List<PersistenceUnit> persistenceUnit;
    @XmlAttribute
    protected String version;

    /**
     * Gets the value of the persistenceUnit property.
     * 
     * @return
     *     possible object is
     *     {@link PersistenceUnit }
     *     
     */
    public List<PersistenceUnit> getPersistenceUnit() {
        if (persistenceUnit == null) {
            persistenceUnit = new ArrayList<PersistenceUnit>();
        }
        return this.persistenceUnit;
    }

    /**
     * Sets the value of the persistenceUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersistenceUnit }
     *     
     */
    public void setPersistenceUnit(List<PersistenceUnit> value) {
        this.persistenceUnit = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}

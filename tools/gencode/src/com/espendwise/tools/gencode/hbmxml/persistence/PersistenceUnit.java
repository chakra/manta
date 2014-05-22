
package com.espendwise.tools.gencode.hbmxml.persistence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://java.sun.com/xml/ns/persistence}provider"/>
 *         &lt;element ref="{http://java.sun.com/xml/ns/persistence}non-jta-data-source"/>
 *         &lt;element ref="{http://java.sun.com/xml/ns/persistence}properties"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="transaction-type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "provider",
        "nonJtaDataSource",
        "validationMode",
        "properties"
})
@XmlRootElement(name = "persistence-unit", namespace = "http://java.sun.com/xml/ns/persistence")
public class PersistenceUnit {

    @XmlElement(namespace = "http://java.sun.com/xml/ns/persistence", required = true)
    protected String provider;
    @XmlElement(name = "non-jta-data-source", namespace = "http://java.sun.com/xml/ns/persistence", required = true)
    protected NonJtaDataSource nonJtaDataSource;
    @XmlElement(namespace = "http://java.sun.com/xml/ns/persistence", required = true)
    protected Properties properties;
    @XmlAttribute
    protected String name;
    @XmlAttribute(name = "transaction-type")
    protected String transactionType;
    @XmlElement(name = "validation-mode", namespace = "http://java.sun.com/xml/ns/persistence", required = false)
    private ValidationMode validationMode;

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the nonJtaDataSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public NonJtaDataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    /**
     * Sets the value of the nonJtaDataSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNonJtaDataSource(NonJtaDataSource value) {
        this.nonJtaDataSource = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link Properties }
     *     
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link Properties }
     *     
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }
    /**
     * Sets the value of the calidationMode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValidationMode(ValidationMode value) {
        this.validationMode = value;
    }

    public ValidationMode getValidationMode() {
        return validationMode;
    }
}

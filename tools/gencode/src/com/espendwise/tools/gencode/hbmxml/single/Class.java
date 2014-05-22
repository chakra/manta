
package com.espendwise.tools.gencode.hbmxml.single;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}meta" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}id"/>
 *         &lt;element ref="{}version" minOccurs="0"/>
 *         &lt;element ref="{}property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dynamic-insert" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dynamic-update" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mutable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="optimistic-lock" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="polymorphism" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="select-before-update" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="table" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "meta",
    "id",
    "version",
    "property"
})
@XmlRootElement(name = "class")
public class Class {

    protected List<Meta> meta;
    @XmlElement(required = true)
    protected Id id;
    protected Version version;
    protected List<Property> property;
    @XmlAttribute(name = "dynamic-insert")
    protected String dynamicInsert;
    @XmlAttribute(name = "dynamic-update")
    protected String dynamicUpdate;
    @XmlAttribute
    protected String mutable;
    @XmlAttribute
    protected String name;
    @XmlAttribute(name = "optimistic-lock")
    protected String optimisticLock;
    @XmlAttribute
    protected String polymorphism;
    @XmlAttribute(name = "select-before-update")
    protected String selectBeforeUpdate;
    @XmlAttribute
    protected String table;

    /**
     * Gets the value of the meta property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the meta property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeta().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Meta }
     * 
     * 
     */
    public List<Meta> getMeta() {
        if (meta == null) {
            meta = new ArrayList<Meta>();
        }
        return this.meta;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Id }
     *     
     */
    public Id getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Id }
     *     
     */
    public void setId(Id value) {
        this.id = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Version }
     *     
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Version }
     *     
     */
    public void setVersion(Version value) {
        this.version = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getProperty() {
        if (property == null) {
            property = new ArrayList<Property>();
        }
        return this.property;
    }

    /**
     * Gets the value of the dynamicInsert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicInsert() {
        return dynamicInsert;
    }

    /**
     * Sets the value of the dynamicInsert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicInsert(String value) {
        this.dynamicInsert = value;
    }

    /**
     * Gets the value of the dynamicUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicUpdate() {
        return dynamicUpdate;
    }

    /**
     * Sets the value of the dynamicUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicUpdate(String value) {
        this.dynamicUpdate = value;
    }

    /**
     * Gets the value of the mutable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMutable() {
        return mutable;
    }

    /**
     * Sets the value of the mutable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMutable(String value) {
        this.mutable = value;
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
     * Gets the value of the optimisticLock property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptimisticLock() {
        return optimisticLock;
    }

    /**
     * Sets the value of the optimisticLock property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptimisticLock(String value) {
        this.optimisticLock = value;
    }

    /**
     * Gets the value of the polymorphism property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolymorphism() {
        return polymorphism;
    }

    /**
     * Sets the value of the polymorphism property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolymorphism(String value) {
        this.polymorphism = value;
    }

    /**
     * Gets the value of the selectBeforeUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectBeforeUpdate() {
        return selectBeforeUpdate;
    }

    /**
     * Sets the value of the selectBeforeUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectBeforeUpdate(String value) {
        this.selectBeforeUpdate = value;
    }

    /**
     * Gets the value of the table property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the value of the table property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTable(String value) {
        this.table = value;
    }

}

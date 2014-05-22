
package com.espendwise.tools.gencode.hbmxml.persistence;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.espendwise.tools.gencode.hbmxml.persistence.mypackage3 package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Provider_QNAME = new QName("http://java.sun.com/xml/ns/persistence", "provider");
    private final static QName _NonJtaDataSource_QNAME = new QName("http://java.sun.com/xml/ns/persistence", "non-jta-data-source");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.espendwise.tools.gencode.hbmxml.persistence.mypackage3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersistenceUnit }
     * 
     */
    public PersistenceUnit createPersistenceUnit() {
        return new PersistenceUnit();
    }

    /**
     * Create an instance of {@link Persistence }
     * 
     */
    public Persistence createPersistence() {
        return new Persistence();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Properties }
     * 
     */
    public Properties createProperties() {
        return new Properties();
    }

    /**
     * Create an instance of {@link NonJtaDataSource }
     *
     */
    public NonJtaDataSource createNonJtaDataSource() {
        return new NonJtaDataSource();
    }

    /**
     * Create an instance of {@link ValidationMode }
     *
     */
    public ValidationMode createValidationMode() {
        return new ValidationMode();
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://java.sun.com/xml/ns/persistence", name = "provider")
    public JAXBElement<String> createProvider(String value) {
        return new JAXBElement<String>(_Provider_QNAME, String.class, null, value);
    }

}

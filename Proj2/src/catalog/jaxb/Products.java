//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.14 at 02:07:36 AM EST 
//


package catalog.jaxb;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="ProductID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="ProductName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SupplierID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="CategoryID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="QuantityPerUnit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UnitPrice" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="UnitsInStock" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="UnitsOnOrder" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="ReorderLevel" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="Discontinued" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "productID",
    "productName",
    "supplierID",
    "categoryID",
    "quantityPerUnit",
    "unitPrice",
    "unitsInStock",
    "unitsOnOrder",
    "reorderLevel",
    "discontinued"
})
@XmlRootElement(name = "Products")
public class Products {

    @XmlElement(name = "ProductID", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger productID;
    @XmlElement(name = "ProductName", required = true)
    protected String productName;
    @XmlElement(name = "SupplierID")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger supplierID;
    @XmlElement(name = "CategoryID")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger categoryID;
    @XmlElement(name = "QuantityPerUnit")
    protected String quantityPerUnit;
    @XmlElement(name = "UnitPrice")
    protected BigDecimal unitPrice;
    @XmlElement(name = "UnitsInStock")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger unitsInStock;
    @XmlElement(name = "UnitsOnOrder")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger unitsOnOrder;
    @XmlElement(name = "ReorderLevel")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger reorderLevel;
    @XmlElement(name = "Discontinued")
    protected boolean discontinued;

    /**
     * Gets the value of the productID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getProductID() {
        return productID;
    }

    /**
     * Sets the value of the productID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setProductID(BigInteger value) {
        this.productID = value;
    }

    /**
     * Gets the value of the productName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the value of the productName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName(String value) {
        this.productName = value;
    }

    /**
     * Gets the value of the supplierID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSupplierID() {
        return supplierID;
    }

    /**
     * Sets the value of the supplierID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSupplierID(BigInteger value) {
        this.supplierID = value;
    }

    /**
     * Gets the value of the categoryID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCategoryID() {
        return categoryID;
    }

    /**
     * Sets the value of the categoryID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCategoryID(BigInteger value) {
        this.categoryID = value;
    }

    /**
     * Gets the value of the quantityPerUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityPerUnit() {
        return quantityPerUnit;
    }

    /**
     * Sets the value of the quantityPerUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityPerUnit(String value) {
        this.quantityPerUnit = value;
    }

    /**
     * Gets the value of the unitPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the value of the unitPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitPrice(BigDecimal value) {
        this.unitPrice = value;
    }

    /**
     * Gets the value of the unitsInStock property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitsInStock() {
        return unitsInStock;
    }

    /**
     * Sets the value of the unitsInStock property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitsInStock(BigInteger value) {
        this.unitsInStock = value;
    }

    /**
     * Gets the value of the unitsOnOrder property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitsOnOrder() {
        return unitsOnOrder;
    }

    /**
     * Sets the value of the unitsOnOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitsOnOrder(BigInteger value) {
        this.unitsOnOrder = value;
    }

    /**
     * Gets the value of the reorderLevel property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReorderLevel() {
        return reorderLevel;
    }

    /**
     * Sets the value of the reorderLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReorderLevel(BigInteger value) {
        this.reorderLevel = value;
    }

    /**
     * Gets the value of the discontinued property.
     * 
     */
    public boolean isDiscontinued() {
        return discontinued;
    }

    /**
     * Sets the value of the discontinued property.
     * 
     */
    public void setDiscontinued(boolean value) {
        this.discontinued = value;
    }

}

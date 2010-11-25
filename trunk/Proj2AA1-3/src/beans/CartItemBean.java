// CartItemBean.java
// Class that maintains a Product and its quantity.
package beans;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import catalog.jaxb.Products;

public class CartItemBean implements Serializable {
   
	
	private Products product;
	private int quantity;
   
   // initialize a CartItemBean
   public CartItemBean(Products productToAdd, int number )
   {
      this.product = productToAdd;
      quantity = number;
   }
   
   
   public Products getProduct()
   {
      return this.product;
   }

   // set the quantity
   public void setQuantity( int number )
   {
      this.quantity = number;
   }

   // get the quantity
   public int getQuantity()
   {
      return quantity;
   }
   
   public BigInteger getProductID()
   {
	   return this.product.getProductID();
   }
   
   public String getProductName()
   {
	   return this.product.getProductName();
   }
   
   public String getQuantityPerUnit()
   {
	   return this.product.getQuantityPerUnit();
   }
   
   public BigDecimal getUnitPrice()
   {
	   return this.product.getUnitPrice();
   }
}


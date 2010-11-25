// CartItemBean.java
// Class that maintains a Product and its quantity.
package Beans;

import java.io.*;

import catalog.jaxb.Products;

public class CartItemBean implements Serializable {
   
	
	private Products product;
	private int quantity;
   
   // initialize a CartItemBean
   public CartItemBean( Products productToAdd, int number )
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
}


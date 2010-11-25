// AddToCartServlet.java
// Servlet to add a book to the shopping cart.
package ctrl;

// Java core packages
import java.io.*;
import java.math.BigInteger;
import java.util.*;

// Java extension packages
import javax.servlet.*;
import javax.servlet.http.*;

import Beans.CartItemBean;

import catalog.jaxb.Products;

public class AddToCartServlet extends HttpServlet {
   
	
	protected void doPost( HttpServletRequest request, HttpServletResponse response )
      throws ServletException, IOException
   {
      HttpSession session = request.getSession( false );
      RequestDispatcher dispatcher;
      
      // if session does not exist, forward to index.html
      if ( session == null ) {
         dispatcher = 
            //request.getRequestDispatcher( "/index.html" );
        	 //request.getRequestDispatcher("views/catalog.jspx");
        	 request.getRequestDispatcher("/index.jspx");
        dispatcher.forward( request, response );
      }
      
      // session exists, get cart HashMap and product to add
      Map cart = ( Map ) session.getAttribute( "cart" );
      Products product = (Products) session.getAttribute("productToAdd");
     
      
      if ( cart == null ) {
    	  cart = new HashMap <BigInteger, CartItemBean>();

    	 
    	  session.setAttribute( "cart", cart );
      }
      
      
      CartItemBean cartItem = 
         ( CartItemBean ) cart.get(product.getProductID() );
      
    
     
     
      if ( cartItem != null ) 
         cartItem.setQuantity( cartItem.getQuantity() + 1 );
      else 
    	  cart.put(product.getProductID(), new CartItemBean(product, 1));
      
      // send the user to viewCart.jsp
      dispatcher = request.getRequestDispatcher( "/views/viewCart.jsp" );
      
      //dispatcher = request.getRequestDispatcher("views\\cart.jspx");
      dispatcher.forward( request, response );
   }   
}


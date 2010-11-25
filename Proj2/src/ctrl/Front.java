package ctrl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import Beans.CartItemBean;

import model.CatalogModel;
import catalog.jaxb.Categories;
import catalog.jaxb.Products;

/**
 * Servlet implementation class Front
 */
//@WebServlet("/Front")
@WebServlet({ "/Front", "/index.html", "/*.html", "/index", "/eFoods" })
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Front() {
        super();
    
    }
    
    @Override
    public void init() throws ServletException
    {
    	String path = this.getServletConfig().getServletContext().getRealPath("/");
    	if(path.equals(null))
    		System.exit(-1);
    	
    	try {
			this.getServletContext().setAttribute("catalogModel", new CatalogModel(path));
			this.getServletContext().setAttribute("authenticated", new Boolean(false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
       
        CatalogModel model = (CatalogModel) this.getServletContext().getAttribute("catalogModel");
       
      //request.getServletContext().getNamedDispatcher("");
        //request.getRequestURL();
        String q = request.getParameter("q");
        System.out.println(request.getParameter("q"));
        String cart = request.getParameter("addToCart");
        String modify = request.getParameter("mod");
        String logout = request.getParameter("logout");
       
        
       
        if(logout != null){
        	
        	request.getServletContext().setAttribute("authenticated", new Boolean(false));
        	//System.out.println("is the authenticated == false? answear ="+ request.getServletContext().getAttribute("authenticated"));
        	
        }
        if(modify != null){
        	List<CartItemBean> cibs = (List<CartItemBean>) request.getSession().getAttribute("cartItems");
        	if(cibs == null)
        	{	
        		RequestDispatcher rd = request.getRequestDispatcher("/views/viewCategories.jspx");
        		rd.forward(request, response);         	
        	}else{
        		for(int i =0; i <cibs.size(); i++)
        		//for(CartItemBean cib : cibs)
        		{

        			String entry = request.getParameter(cibs.get(i).getProduct().getProductID().toString());
        			int modQuantity = 0;
        			if(entry != null && !entry.equals(""))
        			{
        				try{
        					modQuantity  = Integer.parseInt(entry);
        				}
        				catch(NumberFormatException nfe)
        				{
        					continue;
        				}
        				if(modQuantity >= 0){
        					
        					if(modQuantity == 0)
        						cibs.remove(i);
        					else
        						cibs.get(i).setQuantity(modQuantity);
        				}

        			}
        		}

        		RequestDispatcher rd = request.getRequestDispatcher("/views/viewCart.jspx");
        		rd.forward(request, response); 
        	}
        }
       
        else if(cart != null)
        {
        	 Collection <Products> allProducts = new ArrayList<Products>();
             allProducts =  model.getProducts().values();
        	Set <Products> productsToAdd = new HashSet<Products>();
        	List <Integer> quantityList = new ArrayList<Integer >();
             Map <Products, Integer> product2quantity = new HashMap<Products, Integer> ();
        	for(Products p : allProducts)
        	{
        		String pid = p.getProductID().toString();
        		System.out.println("pid :" + pid);
        		String entry = request.getParameter(pid);
        		int quantity= 0;
        		System.out.println("entry :" + entry);
        		if(entry != null && !entry.equalsIgnoreCase("0") && !entry.equals(""))
        		{
        			try{
        			quantity  = Integer.parseInt(entry);
        			}
        			catch(NumberFormatException nfe)
        			{
        				continue;
        			}
        			product2quantity.put(p, quantity);
        			
        		}
        	}
        	Iterator itr = product2quantity.keySet().iterator();
        	//List <CartItemBean> cartItems = new ArrayList<CartItemBean>();
        	List<CartItemBean> cartItems;
        	cartItems = (List<CartItemBean>) request.getSession().getAttribute("cartItems");
        	if(cartItems == null)
        		cartItems = new ArrayList<CartItemBean>();
        	//Iterator itr2 = product2quantity.entrySet().iterator();
        	
        	Map <BigInteger, CartItemBean> pidTocibMap = new HashMap<BigInteger, CartItemBean>();
        	for (CartItemBean cib : cartItems )
        	{
        		//Products pp = cib.getProduct();
        		BigInteger pid_bi = cib.getProduct().getProductID();
        		pidTocibMap.put(pid_bi, cib);
        		
        	}
        	while(itr.hasNext())
        	{
        		Products p = (Products) itr.next();
        		int quan = product2quantity.get(p);
        		
        		//check if cart already contains product.
        		        		        		
        		if(pidTocibMap.containsKey(p.getProductID())) //it's already in the cart.
        		{
        			CartItemBean sameCartItem = pidTocibMap.get(p.getProductID());        			
        			CartItemBean ptr = cartItems.get(cartItems.indexOf(sameCartItem));
        			int newQuan = ptr.getQuantity() + quan;
        			ptr.setQuantity(newQuan);
        			
        		}else
        		{
        			cartItems.add(new CartItemBean(p, quan));
        		}
        	}
        	
        	//productsToAdd.add((Products) product2quantity.keySet());
        	        	
        	//quantityList.addAll(product2quantity.values());
        	
//        	if(productsToAdd.size() != quantityList.size())
//        		System.err.println("Cart products and quantitites are not aligned");
        	

        	
        	request.getSession().setAttribute("cartItems", cartItems);
        	RequestDispatcher rd = request.getRequestDispatcher("/views/viewCart.jspx");
        	rd.forward(request, response);
        	
        }
        else if(q!=null) //push products and dispatch to viewProducts
        {
        	BigInteger categoryID = new BigInteger(q);
        	List<Products> productsList = model.getProductsByCategoryID(categoryID);
        	Categories category = model.getCategories().get(categoryID);
        	request.setAttribute("category", category);
        	request.setAttribute("productsList", productsList);
        	RequestDispatcher rd = request.getRequestDispatcher("/views/viewProducts.jspx");
        	rd.forward(request, response);
        }

        else{  				
        	Iterator <Categories> cat = model.getCategories().values().iterator();
        	/*List <Categories> catList =  new ArrayList<Categories>();
        List<String> names = new ArrayList<String >();

        for(Categories c : model.getCategories().values())
        {
            names.add(c.getCategoryName());
            catList.add(c);
        }
        if(!names.isEmpty())
            request.setAttribute("names", names);*/
        	String catImagesPath = this.getServletContext().getRealPath("/img/");
        	System.out.println(catImagesPath);
        	request.setAttribute("imgPath", catImagesPath);
        	request.setAttribute("categories", cat);
        	//request.setAttribute("catList", catList);
        	RequestDispatcher rd = request.getRequestDispatcher("/views/viewCategories.jspx");
        	rd.forward(request, response);

        }
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

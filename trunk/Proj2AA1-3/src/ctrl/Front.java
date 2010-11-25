package ctrl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.bind.JAXBException;

import PO.jaxb.CustomerType;

import catalog.jaxb.Categories;
import catalog.jaxb.Products;

import model.Cart;
import model.CatalogModel;

@WebServlet({"/Front","/*.html","/*.do"})
public class Front extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Front() 
    {
        super();
    }

    
    public void init() throws ServletException
    {
    	//System.out.println("I am HERE !!!!!!!!!!!!!!!!");
    	String catalogFile = this.getServletContext().getInitParameter("Catalog");
    	String realCatalogFile = this.getServletContext().getRealPath(catalogFile);
    	String accountsFile = this.getServletContext().getInitParameter("Accounts");
    	String realAccountsFile = this.getServletContext().getRealPath(accountsFile);
    	
    	System.out.println(accountsFile);
    	System.out.println(this.getServletContext().getRealPath("PO"));
    	try 
    	{
			CatalogModel model = new CatalogModel(realCatalogFile, realAccountsFile);
			this.getServletContext().setAttribute("model", model);
			Iterator <Categories> cat = model.getCategories().values().iterator();
			this.getServletContext().setAttribute("categories", cat);
			//System.out.println(model.toString());
			System.out.println(model.getAccounts());
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (JAXBException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		CatalogModel model = (CatalogModel) this.getServletContext().getAttribute("model");
		String query = request.getQueryString();
		String servletPath = request.getServletPath();
		HttpSession session = request.getSession();
		String target = "/views/viewCategories.jspx"; //This will be where we forward. Default is the first page.
		//System.out.println(servlet + query);	//test and debug purposes
		//System.out.println(session.getAttribute("cart"));	//test and debug purposes
		
		
		if (session.getAttribute("cart") == null) //Make sure we have a cart since the very beginning.
		{
			session.setAttribute("cart", new Cart());
		}
		if (session.getAttribute("loggedIn") == null) //Make sure we are aware whether the client has logged in.
		{
			session.setAttribute("loggedIn", false);
		}
		
		Iterator <Categories> cat = model.getCategories().values().iterator();
		session.setAttribute("categories", cat);
		
		
		//go to the index page if we get a Front or Start.do
		if (servletPath.equalsIgnoreCase("/Front") || servletPath.equalsIgnoreCase("/Start.do"))
		{ //empty because it is default
		} else if (servletPath.equalsIgnoreCase("/Category.do"))
		{
			//System.out.println(request.getParameter("q"));

			if (request.getParameter("q") != null) 
			{
				//Check if param is a number
				target = "/views/viewProducts.jspx";
				BigInteger catID = new BigInteger(request.getParameter("q"));
				session.setAttribute("productsList", model.getProductsByCategoryID(catID));
				request.setAttribute("category", model.getCategories().get(catID));
			} else
			{
				//do nothing. The target is already the front page.
			}
		} else if ((servletPath.equalsIgnoreCase("/Login.do")))
		{
			if (!(Boolean)session.getAttribute("loggedIn")) //if you ARE logged in you wont be able to get in and redirected to the homepage
			{
				if (request.getParameter("q") != null) //If there is no q paramater then nothing has been entered for id and pw
				{
					if (this.validate(request)) //userid and pw match
					{
						target = session.getAttribute("previousPage") != null ? (String) session
								.getAttribute("previousPage") : target;
						session.setAttribute("loggedIn", true);
						session.setAttribute("account", model
								.getAccount(request.getParameter("userid")));
						//System.out.println("Target is: " + target + " q is: " + request.getParameter("q"));

					} else //userid and pw dont match so return to login again
					{
						target = "/views/login.jspx";
						//System.out.println("Target is: " + target + " q is: " + request.getParameter("q"));
					}
				} else {
					target = "/views/login.jspx";
				}
			}
		} else if ((servletPath.equalsIgnoreCase("/Cart.do")))
		{
			target = "/views/viewCart.jspx";
		} else if ((servletPath.equalsIgnoreCase("/CheckOut.do")))
		{
			if (!(Boolean)session.getAttribute("loggedIn"))
			{
				target = "/views/login.jspx";
				session.setAttribute("previousPage", "/views/viewCheckout.jspx"); //This attribute will tell us where to go after logging in.
				
			} else
			{
				target = "/views/viewCheckout.jspx";
			}
		} else if(servletPath.equalsIgnoreCase("/Confirmation.do"))
		{
			if (!(Boolean)session.getAttribute("loggedIn"))
			{
				target = "/views/login.jspx";
			} else
			{
				String xmlFileName = model.makePO((Cart) session.getAttribute("cart"), (CustomerType) session.getAttribute("account"), 
						this.getServletContext().getRealPath("PO"));
				StringBuffer URL = request.getRequestURL();
				String xmlURL = URL.substring(0, URL.length() - 16) + "/PO.do?q=" + xmlFileName; 
				request.setAttribute("xmlFile",  xmlURL);
				target = "/views/viewConfirmation.jspx";
			}
		} else if(servletPath.equalsIgnoreCase("/PO.do"))
		{
			if (request.getParameter("q") != null)
			{
				String xmlFileName = request.getParameter("q");
				request.setAttribute("xmlFile",  "/PO" + xmlFileName);
				target = "/PO/viewPO.jspx";
			}
		} else if(servletPath.equalsIgnoreCase("/logout.do"))
		{
			target = session.getAttribute("previousPage") != null ? (String) session.getAttribute("previousPage") : target;
			session.setAttribute("loggedIn", false);
			session.setAttribute("account", null);
		}
		
		//This checks if we should add something to the cart
		if (request.getParameter("addBtn") != null)
		{
			System.out.println("On my way to the cart!");
			this.processToCart((Cart) session.getAttribute("cart"), request);
		}
		System.out.println("Target is: " + target + " q is: " + request.getParameter("q"));
		
		if (!target.contains("login"))
		{
			session.setAttribute("previousPage", target);
		}
		
		RequestDispatcher rd =  request.getRequestDispatcher(target);
		rd.forward(request, response);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		this.doGet(request, response);
	}
	
	private void processToCart(Cart cart, HttpServletRequest request)
	{
		Enumeration<String> paramNames = request.getParameterNames();
		System.out.println("Beginning to process the cart!");
		
		boolean valid = true;
		while (valid && paramNames.hasMoreElements())
		{
			String name = paramNames.nextElement();
			if (name.startsWith("CartInput"))
			{
				try 
				{
					Integer.parseInt(request.getParameter(name));
				} catch (NumberFormatException e) 
				{
					valid = false;
					request.setAttribute("inputError", "There was an error in your input");
				}
			}
		}
		
		if (valid) // the inputs are valid numbers so we can input them now
		{
			paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String name = paramNames.nextElement();
				System.out.println("name is :" + name + " parameter is: "
						+ request.getParameter(name)
						+ " name starts with addItem?: "
						+ name.startsWith("addItem"));
				if (name.startsWith("CartInput")) // in viewProducts the key word for each input was "add" + ProdID
				{
					System.out.println(request.getParameter(name));
					int qty = Integer.parseInt(request.getParameter(name));
					BigInteger id = new BigInteger((name.substring(9)));
					CatalogModel model = (CatalogModel) this
							.getServletContext().getAttribute("model");
					Products prod = model.getProducts().get(id);
					cart.add(prod, qty);
				}
			}
		}
	}
	
	private boolean validate (HttpServletRequest request)
	{
		boolean result = false;
		CatalogModel model = (CatalogModel) this.getServletContext().getAttribute("model");
		String userID = request.getParameter("userid");
		String pw = request.getParameter("pw");
		result = model.validate(userID, pw);
		if (!result)
		{
			request.setAttribute("inputError", "The UserName and Password do not match");
		}
		return result;
	}

}

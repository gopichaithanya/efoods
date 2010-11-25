package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import model.CatalogModel;

/**
 * Servlet Filter implementation class login
 */
@WebFilter("/login")
public class Login implements Filter {

    /**
     * Default constructor. 
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		StringBuffer loginMsg = new StringBuffer();
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String userid = httpRequest.getParameter("userid");
		String pw = httpRequest.getParameter("pw");
		RequestDispatcher rd = request.getRequestDispatcher("login.jspx");
		//System.out.println("show what intended target has" + httpRequest.getParameter("intendedTarget"));
		RequestDispatcher rdSuccess = request.getRequestDispatcher("viewCheckout.jspx");		
		if (((userid != null) && (userid != "")) && ((pw != null) && (pw != "")))
		{
			//System.out.println("pass and ID not Null");
			
			CatalogModel model = (CatalogModel) httpRequest.getServletContext().getAttribute("catalogModel");
			if (model == null)
				System.out.println("Model == null");
			if (model.validat(userid, pw))
			{
				String UserName = model.getAccount(userid, pw).getAccountName();
				httpRequest.getSession().setAttribute("UserName", UserName);
				
				httpRequest.getServletContext().setAttribute("authenticated", new Boolean (true));
				
				rdSuccess.forward(httpRequest, response);
				//System.out.println(httpRequest.getParameter("intendedTarget"));
			} else
			{
				loginMsg.append("User ID and Password do not match!");
				httpRequest.getSession().setAttribute("loginMsg", loginMsg);
				rd.forward(request, response);
			}
		} else
		{
			if ((userid == null) || (userid ==""))
			{
				loginMsg.append("User ID cannot be empty!\n");
			}
			if ((pw == null) || (pw == ""))
			{
				loginMsg.append("Password cannot be empty!\n");
			}
			httpRequest.setAttribute("loginMsg", loginMsg);
			System.out.println(loginMsg);
			rd.forward(request, response);
		}
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class Authenticate
 */
public class Authenticate implements Filter 
{

    /**
     * Default constructor. 
     */
    public Authenticate() 
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() 
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean auth = (Boolean) httpRequest.getServletContext().getAttribute("authenticated");
		System.out.println("is the user authenticated, answaer ="+auth);
		if (auth)
		{
			RequestDispatcher rd = request.getRequestDispatcher("/views/viewCheckout.jspx");
        	rd.forward(request, response);
			
		}
		else
		{

			RequestDispatcher rd = request.getRequestDispatcher("/views/login.jspx");
			rd.forward(request, response);

		}	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException 
	{
		// TODO Auto-generated method stub
	}

}

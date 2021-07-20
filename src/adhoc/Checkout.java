package adhoc;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class Checkout
 */
@WebFilter({"/Checkout","/Account"})
public class Checkout implements Filter {

    public Checkout() 
    {
    }

	public void destroy() 
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getSession().getAttribute("user")!=null)
		{
			chain.doFilter(request, response);			
		}
		else
		{
			((HttpServletResponse)response).sendRedirect("Login?login=true");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException 
	{
	}

}
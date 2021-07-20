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

import ctrl.CtrlUtil;

/**
 * Servlet Filter implementation class Logging
 */
@WebFilter({ "/Logging", "/*" })
public class Logging implements Filter 
{

    public Logging() 
    {
    }

	public void destroy() 
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		CtrlUtil.log_request(System.out, (HttpServletRequest) request, "Incoming");
		chain.doFilter(request, response);
		CtrlUtil.log_request(System.out, (HttpServletRequest) request, "Outgoing");
	}

	public void init(FilterConfig fConfig) throws ServletException 
	{
	}
}
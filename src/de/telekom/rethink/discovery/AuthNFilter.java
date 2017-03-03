/********************************************************************************************************************
 * Deutsche Telekom Laboratories																					*
 * Copyright (c) 2016 European Project reTHINK																		*
 * 																													*
 ********************************************************************************************************************/

package de.telekom.rethink.discovery;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import de.telekom.rethink.discovery.listener.*;



/**
 * Servlet Filter implementation class AuthNFilter
 */
@WebFilter("/AuthNFilter")
public class AuthNFilter implements Filter {

private String contextPath;	

	
    /**
     * Default constructor. 
     */
    public AuthNFilter() {
        // TODO Auto-generated constructor stub
    	System.out.print("reTHINK AuthenticationFilter intialized\n");
    	 //System.out.println(getCallerClassName(this.getClass()));
    	
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
				
		if(req.getSession().getAttribute("userID")!=null){
			// pass the request along the filter chain
			chain.doFilter(request, response);
		} else {
			//do nothing
			res.sendRedirect(contextPath+"/login.jsp");
			
		}
		

		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		contextPath=fConfig.getServletContext().getContextPath();
	}
	
	
	/*
	 public static String getCallerClassName(final Class<?> clazz) {
	        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	        final String className = clazz.getName();
	        boolean classFound = false;
	        for (int i = 1; i < stackTrace.length; i++) {
	            final StackTraceElement element = stackTrace[i];
	            final String callerClassName = element.getClassName();
	            // check if class name is the requested class
	            if (callerClassName.equals(className)) classFound = true;
	            else if (classFound) return callerClassName;
	        }
	        return null;
	    }
		*/
}

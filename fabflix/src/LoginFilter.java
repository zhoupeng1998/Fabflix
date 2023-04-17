/*         \\\\\\\\\\\\\\\\\\\\|||||||////////////////////  
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *             The Buddha Blesses Us There's Never Bugs!
 */

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.salt.StringFixedSaltGenerator;

import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
       HttpServletRequest hRequest = (HttpServletRequest) request;
       HttpServletResponse hResponse = (HttpServletResponse) response;
        
        // print in the console
       System.out.println("LoginFilter: " + hRequest.getRequestURI());
        
        // Check if the URL is allowed to be accessed without log in
       String url = hRequest.getRequestURI();
       if( this.isUrlAllowedWithoutLogin(url)) {
           System.out.println(hRequest.getSession().getAttribute("admin"));
           if( hRequest.getSession().getAttribute("admin") == null && (url.endsWith("/dashboard.html") || url.endsWith("/dashboard.js") || url.endsWith("/api/dashboard"))) {
              hResponse.sendRedirect("_dashboard.html");
              return;
           }
           else
              chain.doFilter(request, response);
           return;
       }
       // Redirect to login page if the "user" attribute doesn't exist (not logged in)
       // check for the user has logged in
       if( hRequest.getSession().getAttribute("user") == null ) {
           hResponse.sendRedirect("login.html");
           return;
       }
       else
           chain.doFilter(request, response);
       return;
    }
    
    private boolean isUrlAllowedWithoutLogin( String requestU ) {
        requestU = requestU.toLowerCase();
/*
        return requestU.endsWith("login") || requestU.endsWith("login.html") || requestU.endsWith("login.js") || requestU.endsWith("api/login") 
            || requestU.endsWith("api/_dashboard") || requestU.endsWith("/_dashboard.html") || requestU.endsWith("/_dashboard.js") || requestU.endsWith("/_dashboard.css")
            || requestU.endsWith("api/dashboard") || requestU.endsWith("/dashboard.html") || requestU.endsWith("/dashboard.js");
*/
        return true;
    }
    
    @Override
    public void destroy() {}


    @Override
    public void init(FilterConfig fCongig) throws ServletException {}
    
}

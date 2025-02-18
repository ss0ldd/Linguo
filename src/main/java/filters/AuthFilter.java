package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (session == null) {
            session = request.getSession(true); // Создаем новую сессию
            session.setAttribute("authenticated", false); // Устанавливаем значение по умолчанию
        }

        Boolean isAuthenticated = false;
        if (session != null) {
            isAuthenticated = (Boolean) session.getAttribute("authenticated");
            if (isAuthenticated == null) {
                isAuthenticated = false;
            }
        }

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        boolean isSignInPage = requestURI.equals(contextPath + "/signIn");
        boolean isSignUpPage = requestURI.equals(contextPath + "/signUp");

        if (isSignInPage || isSignUpPage) {
            filterChain.doFilter(request, response);
        } else if (isAuthenticated) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(contextPath + "/signIn");
        }
    }
    @Override
    public void destroy() {
    }
}
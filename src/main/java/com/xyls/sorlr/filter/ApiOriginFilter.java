package com.xyls.sorlr.filter;

import com.fasterxml.jackson.core.filter.TokenFilter;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class ApiOriginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("收到请求 " + new Date());
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT,OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        HttpServletRequest req = (HttpServletRequest) request;
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }

}

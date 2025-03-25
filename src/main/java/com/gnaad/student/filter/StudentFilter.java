package com.gnaad.student.filter;

import com.gnaad.student.util.AESUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class StudentFilter extends OncePerRequestFilter {

    private final Set<String> utilityEndpoint = Set.of("/encrypt", "/decrypt");

    @Value("${encryption.enabled}")
    private boolean encryptionEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!encryptionEnabled) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (utilityEndpoint.contains(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
            String secretKey;

            HttpSession session = request.getSession();
            secretKey = (String) session.getAttribute("sec");

            if (secretKey == null) {
                secretKey = AESUtil.generateAESKey();
                session.setAttribute("sec", secretKey);
            }

            RequestWrapper requestWrapper = new RequestWrapper(request, secretKey);
            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            filterChain.doFilter(requestWrapper, responseWrapper);
            responseWrapper.encryptAndSend(secretKey);
        } catch (Exception e) {
            throw new ServletException("Error processing request/response", e);
        }
    }
}

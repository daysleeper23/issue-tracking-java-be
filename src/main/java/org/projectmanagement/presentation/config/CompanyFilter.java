package org.projectmanagement.presentation.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CompanyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException, java.io.IOException {
        try {
            // Check if the user has access on the company
            String companyId = request.getRequestURI().split("/")[1];

            System.out.println("Filtering --- Company ID: " + companyId);
            if (companyId.equals("companies") || companyId.equals("auth")) {
                System.out.println("By passing company check for COMPANIES or AUTH!!!");
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                && !authentication.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().toString().equals(companyId))
            ) {
                throw new AccessDeniedException("Access denied");
            }

            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            System.out.println("Access denied");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getMessage());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}

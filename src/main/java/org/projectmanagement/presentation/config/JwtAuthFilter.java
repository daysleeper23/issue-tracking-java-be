package org.projectmanagement.presentation.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.projectmanagement.application.services.UserDetailsServiceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtHelper jwtHelper;

    public JwtAuthFilter(UserDetailsServiceImpl userDetailsService, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtHelper.extractUserEmail(token);
            }

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtHelper.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Check if the user has access on the company
            String companyId = request.getRequestURI().split("/")[1];
            System.out.println("Company ID: " + companyId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                && !authentication.getAuthorities().stream().anyMatch(
                    a -> a.getAuthority().equals(companyId.toString()))
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

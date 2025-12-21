package com.toolborrow.backend.config;

import com.toolborrow.backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (isValidToken(token)) {
                        Authentication auth = getAuthenticationFromToken(token);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                    break;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        try {
            JwtUtils.validateAndExtractEmail(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Authentication getAuthenticationFromToken(String token) {
        String email = JwtUtils.validateAndExtractEmail(token);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}


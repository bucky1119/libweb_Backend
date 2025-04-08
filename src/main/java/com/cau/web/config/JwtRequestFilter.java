package com.cau.web.config;

import com.cau.web.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // 打印 Authorization 请求头
        System.out.println("Authorization Header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);  // 去除 "Bearer " 前缀
                username = JwtUtil.extractUsername(jwt);
            }
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired: ", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
            return; // 停止请求继续
        } catch (Exception e) {
            logger.error("JWT token parsing error: ", e);
        }


        // 打印日志以调试 JWT 验证
        if (username != null) {
            logger.info("Extracted Username from JWT: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 验证 JWT Token 是否有效
            if (JwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                // 添加打印用户权限信息的代码
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("User Roles: " + authentication.getAuthorities());

                // 打印日志以确认设置了用户身份
                logger.info("User authenticated: " + username);
            } else {
                logger.warn("Invalid JWT Token for user: " + username);
            }
        }

        chain.doFilter(request, response);
    }
}

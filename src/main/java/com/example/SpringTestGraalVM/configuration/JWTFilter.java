package com.example.SpringTestGraalVM.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.SpringTestGraalVM.exceptions.UserOrgNotUpdatedException;
import com.example.SpringTestGraalVM.model.UserOrg;
import com.example.SpringTestGraalVM.repositories.UsersRepository;
import com.example.SpringTestGraalVM.security.JWTUtil;
import com.example.SpringTestGraalVM.security.UserOrgDetails;
import com.example.SpringTestGraalVM.service.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTUtil jwtUtil;

    private final PersonDetailsService personDetailsService;

    private final UsersRepository usersRepository;

    @Autowired
    public JWTFilter(AuthenticationEntryPoint authenticationEntryPoint, JWTUtil jwtUtil, PersonDetailsService personDetailsService, UsersRepository usersRepository) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
        this.usersRepository = usersRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank()) {
            String jwt = authHeader;

            if(authHeader.startsWith("Bearer ")){
                jwt = authHeader.substring(7);
            }

            if (jwt.isBlank()) {
                response.setHeader("error","Invalid JWT Token in Bearer Header");
                response.sendError(response.SC_BAD_REQUEST);
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);

                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());


                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        try { //обновление последней авторизации
                            UserOrgDetails userOrgDetails = (UserOrgDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            UserOrg userOrg = userOrgDetails.getPerson();
                            userOrg.setLastLogin(LocalDateTime.now());
                            usersRepository.save(userOrg);
                        } catch (Exception e){
                            throw new UserOrgNotUpdatedException("Failed to update last login date;" + e.getMessage());
                        }

                    }


                } catch (JWTVerificationException e) {
                    response.setHeader("error","Invalid JWT Token");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }

            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException(e.getMessage()));
        }


    }

}

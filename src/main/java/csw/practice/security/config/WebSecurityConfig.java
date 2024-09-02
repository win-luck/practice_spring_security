package csw.practice.security.config;

import csw.practice.security.auth.component.handler.AccessDeniedHandler;
import csw.practice.security.auth.component.handler.AuthenticationEntryPoint;
import csw.practice.security.auth.component.handler.HttpRequestEndpointChecker;
import csw.practice.security.auth.component.jwt.JwtAuthenticationFilter;
import csw.practice.security.auth.component.jwt.JwtUtil;
import csw.practice.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final DispatcherServlet dispatcherServlet;
    private final HttpRequestEndpointChecker endpointChecker;

    @Bean
    public HttpRequestEndpointChecker endpointChecker() {
        return new HttpRequestEndpointChecker(dispatcherServlet);
    }

    private static final String[] PERMIT_URL_ARRAY = {
           "/error", "/api/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailService customUserDetailService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AuthenticationEntryPoint(endpointChecker))
                        .accessDeniedHandler(new AccessDeniedHandler(endpointChecker))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(PERMIT_URL_ARRAY)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
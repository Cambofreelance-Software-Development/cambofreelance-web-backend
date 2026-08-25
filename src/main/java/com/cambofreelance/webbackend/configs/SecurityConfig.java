package com.cambofreelance.webbackend.configs;

import com.cambofreelance.webbackend.configs.oauth2.CookieOAuth2AuthorizationRequestRepository;
import com.cambofreelance.webbackend.configs.oauth2.OAuth2AuthenticationFailureHandler;
import com.cambofreelance.webbackend.configs.oauth2.OAuth2AuthenticationSuccessHandler;
import com.cambofreelance.webbackend.filters.AuthTokenFilter;
import com.cambofreelance.webbackend.filters.IpWhitelistFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final IpWhitelistFilter ipWhitelistFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    AntPathRequestMatcher.antMatcher("/oauth/token"),
                    AntPathRequestMatcher.antMatcher("/oauth/register"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/oauth/register/resolve"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/oauth/register/verify-otp"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/oauth/register/send-otp"),
                    AntPathRequestMatcher.antMatcher("/openapi/**"),
                    AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                    AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                    AntPathRequestMatcher.antMatcher("/actuator/**"),
                    AntPathRequestMatcher.antMatcher("/auth/openapi/swagger-ui.html"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/articles"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/articles/**"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/features"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/feature-categories"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/hardware"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/hardware/*"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/hardware-categories"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/products"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/products/*"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/product-categories"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/business-type-catalog"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/business-type-catalog/*"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/business-type-catalog-categories"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/help-center-categories"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/pricing"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/media/*/view"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/stats"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/partner-cta/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/homepage/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/page-heroes/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/page-ctas/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/cms/settings/hardware/public"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/testimonials"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/faqs"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/home-products"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/business-types"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/feature-tabs"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/app-releases"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/app-releases/latest"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/oauth/forgot-password"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/oauth/reset-password"),
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/contact"),
                    // Google login: initiation + Google's redirect-back callback
                    AntPathRequestMatcher.antMatcher("/oauth2/authorization/**"),
                    AntPathRequestMatcher.antMatcher("/login/oauth2/code/**"),
                    // ABA PayWay server-to-server pushback (verified against PayWay before any state change)
                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/payway/callback")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(ipWhitelistFilter, AuthTokenFilter.class)
            .exceptionHandling(ex ->
                ex.authenticationEntryPoint(customAuthenticationEntryPoint)
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(a -> a.authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
            );

        return http.build();
    }

    // Prevent Spring Boot from auto-registering the filter outside the security chain
    @Bean
    public FilterRegistrationBean<AuthTokenFilter> authTokenFilterRegistration(
        AuthTokenFilter filter) {
        FilterRegistrationBean<AuthTokenFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<IpWhitelistFilter> ipWhitelistFilterRegistration(
        IpWhitelistFilter filter) {
        FilterRegistrationBean<IpWhitelistFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}

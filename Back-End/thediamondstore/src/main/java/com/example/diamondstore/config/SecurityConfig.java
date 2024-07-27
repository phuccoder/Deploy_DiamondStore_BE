package com.example.diamondstore.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.diamondstore.filter.JwtRequestFilter;
import com.example.diamondstore.service.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/api/test/**", "/authenticate" };

    private static final String[] GUEST_URL = { "/api/guest/**", "/api/auth/**" };

    private static final String[] ADMIN_URL = { "/api/admin/**" };

    private static final String[] CUSTOMER_URL = { "/api/customer/**" };

    private static final String[] MANAGER_URL = { "/api/manager/**" };

    private static final String[] ADMIN_MANAGER_URL = { "/api/accounts/get-all", "/api/accounts/all-except-customer",
            "/api/accounts/get-by-role/**", "/api/certificates/get-all", "/api/certificates/**",
            "/api/certificates/get-certificate-image/**", "/api/diamonds/get-all", "/api/diamond-prices/get-all",
            "/api/gold-prices/get-all", "/api/jewelry/get-all", "/api/orders/totalRevenue",
            "/api/orders/totalOrder", "/api/warranty-management/**", "/api/warranties/get/warrantyImg/**", "/api/warranties/diamondIDIsNull", "/api/warranties/jewelryIDIsNull",
            "/api/accounts/**", "/api/customer/warranties/get-warranty-image/**",
            "/api/customer/certificates/get-certificate-image/**" };

    private static final String[] ADMIN_MANAGER_SALE_STAFF_URL = { "/api/accounts/get/**", "/api/accounts/**", "/api/order-management/**",
            "/api/order-management/orders/get-all", "/api/order-management/orders/get-order-have-transaction-no",
            "/api/promotion-management/**", "/api/accounts/update/**", "/api/order-management/orders/update/**", "/api/orderDetail-management/orderDetails/**" };

    @Autowired
    private final AccountService UserService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(AccountService UserService, JwtRequestFilter jwtRequestFilter) {
        this.UserService = UserService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers(SWAGGER_URL).permitAll()
                        .antMatchers(GUEST_URL).permitAll()
                        .antMatchers(ADMIN_URL).hasRole("ADMIN")
                        .antMatchers(CUSTOMER_URL).hasRole("CUSTOMER")
                        .antMatchers(MANAGER_URL).hasRole("MANAGER")
                        .antMatchers(ADMIN_MANAGER_URL).hasAnyRole("ADMIN", "MANAGER")
                        .antMatchers(ADMIN_MANAGER_SALE_STAFF_URL).hasAnyRole("ADMIN", "MANAGER", "SALE-STAFF")
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://the-diamond-store-demo.web.app",
                "https://www.thediamondstore.site"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

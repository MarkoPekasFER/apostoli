package com.apostoli.UnluckyApp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(reqMatchReg -> reqMatchReg
                        .requestMatchers("/api/v1/user/profile"
                                , "/api/v1/user/myReports"
                                ,"/api/v1/report/submit"
                                ,"/api/v1/user/joinOrg/{orgName}"
                                ,"/api/v1/user/leaveOrg/{orgName}",
                                "/api/v1/user/addCity/{cityName}",
                                "/api/v1/user/removeCity/{cityName}")
                        .hasAnyRole("USER", "ADMIN", "SUPER_ADMIN", "RESPONDER", "ORGANISATION")

                        .requestMatchers("/api/v1/org/allOrganizations","/api/v1/org/create")
                        .hasAnyRole("USER","ORGANISATION","ADMIN", "SUPER_ADMIN","RESPONDER")

                        .requestMatchers("/api/v1/org/**")
                        .hasAnyRole("ORGANISATION", "ADMIN", "SUPER_ADMIN", "RESPONDER")

                        .requestMatchers("/api/v1/report/reject/{reportId}"
                                ,"/api/v1/report/resolve/{reportId}"
                                ,"/api/v1/report/approve/{reportId}")
                        .hasAnyRole("ORGANISATION", "ADMIN", "SUPER_ADMIN", "RESPONDER")

                        .requestMatchers("/api/v1/shelter/create"
                                ,"api/v1/instruction/create"
                                ,"api/v1/instruction/delete/{instructionID}",
                                "/api/v1/shelter/delete/{shelterName}",
                                "/api/v1/user/statsByCity/{city}"
                                ,"/api/v1/user/statsByDisaster/{disasterType}"
                                ,"/api/v1/user/statsByDisasterAndCity/{city}/{disasterType}")

                        .hasAnyRole("SUPER_ADMIN", "ADMIN", "RESPONDER" )

                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/v1/user/login")
                        .defaultSuccessUrl("/api/v1/user/profile")
                        .userInfoEndpoint(userInfo -> userInfo.userService(new DefaultOAuth2UserService())))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // Allow your frontend origin
        configuration.addAllowedOrigin("https://apostoli.markopekas.com"); // Allow your frontend origin
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies/auth headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter JwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
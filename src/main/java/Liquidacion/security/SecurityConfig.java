package Liquidacion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final EmpleadoImp userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(EmpleadoImp userDetailsService,
                          LoginSuccessHandler loginSuccessHandler,
                          CustomLogoutSuccessHandler logoutSuccessHandler,
                          LoginFailureHandler loginFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/logout",
                                "/css/**", "/js/**", "/webjars/**", "/img/**").permitAll()
                        .requestMatchers("/ventas/actualizarEstado").hasRole("ADMIN")
                        .requestMatchers("/islas/**").hasRole("ADMINGESNNOVA")
                        .anyRequest().authenticated()
                )

                // 🔐 PASO 2: MANEJO DE SESIÓN EXPIRADA
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?expired")
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login-process")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}

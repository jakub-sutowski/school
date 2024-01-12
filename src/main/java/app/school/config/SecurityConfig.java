package app.school.config;

import app.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static app.school.type.Role.ADMIN;
import static app.school.type.Role.STUDENT;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    private static final String[] WHITE_LIST_URL = {"/register",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(mvc.pattern(PATCH, "/register/admin")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(POST, "/courses")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(GET, "/courses")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(GET, "/courses/filter")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(PATCH, "/courses/**")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(DELETE, "/courses/**")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(GET, "/users/courses")).hasAnyAuthority(ADMIN.name(), STUDENT.name())
                                .requestMatchers(mvc.pattern(GET, "/users/filter")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(GET, "/users/{email}")).hasAuthority(ADMIN.name())
                                .requestMatchers(mvc.pattern(PATCH, "/users")).hasAnyAuthority(ADMIN.name(), STUDENT.name())
                                .requestMatchers(mvc.pattern(PATCH, "/users/update")).hasAnyAuthority(ADMIN.name(), STUDENT.name())
                                .requestMatchers(mvc.pattern(DELETE, "/users/{email}")).hasAuthority(ADMIN.name())
                                .anyRequest()
                                .authenticated()
                );
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
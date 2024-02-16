package factoria.org.MyFavoriteImagesBackend.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Value("api/v1")
    private String baseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/{userId}/images/**").hasAnyAuthority("ROLE_user")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_user")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_user")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_user")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                //.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt())
                //.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}

package factoria.org.MyFavoriteImagesBackend.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import factoria.org.MyFavoriteImagesBackend.config.security.exceptions.CustomBasicAuthenticationEntryPoint;
import factoria.org.MyFavoriteImagesBackend.config.security.exceptions.CustomBearerTokenAccessDeniedHandler;
import factoria.org.MyFavoriteImagesBackend.config.security.exceptions.CustomBearerTokenAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Value("api/v1")
    private String baseUrl;

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfig(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler) throws NoSuchAlgorithmException {
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/images/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/{userId}/images/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/{userId}/images/{imageId}/**").hasAnyAuthority("ROLE_admin", "ROLE_user")
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/**").hasAnyAuthority("ROLE_admin")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults())
                                .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                                .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}

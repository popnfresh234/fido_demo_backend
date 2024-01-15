package com.example.apilogin.security;

import com.example.apilogin.security.filters.RoleFilter;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Log4j2
@Configuration
@RequiredArgsConstructor

public class WebSecurityConfig {
    private final RoleFilter roleFilter;
    private final CustomUserDetailService customUserDetailService;
    private final static String secret = "thisisatest";

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                roleFilter,
                BasicAuthenticationFilter.class);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(
                                "/",
                                "favicon.ico",
                                "error",
                                "/actuator/**",
                                "/auth/signup",
                                "/auth/login",
                                "/auth/recovery/**",
                                "/webauthn/**",
                                "/uaf/**",
                                "/logout").permitAll()
                        .anyRequest().authenticated()


                );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.oauth2ResourceServer(oauth2 -> oauth2.authenticationEntryPoint(new AuthFailureHandler()));
        http.cors(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
                AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService((customUserDetailService))
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();

    }

    //    1.  Create Key Pair
    @Bean
    KeyPair keyPair() {
        // Generate RSA keystore
        // keytool -genkey -alias webcomm.demo.backend -keyalg RSA -keypass 123456 -keystore jwt.jks -storepass 123456
        // Move jwt.jks on to classpath
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"),
                System.getenv("RSA_PASSWORD").toCharArray());
        return keyStoreKeyFactory.getKeyPair(
                "webcomm.demo.backend",
                System.getenv("RSA_PASSWORD").toCharArray());
    }

    //    2.  Create RSA Key using Key Pair
    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }


    //    3.  Create JWKSource (JSON Web Key Source)
    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }


    //     4.  Use RSA  Public Key for Decoding
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaKey.toPublicKey())
                .build();

        //  Default clock skew is 60 seconds, uncomment to change if needed for testing
        //        OAuth2TokenValidator<Jwt> withClockSkew = new DelegatingOAuth2TokenValidator<>(
        //                new JwtTimestampValidator(Duration.ofSeconds(1)));
        //        jwtDecoder.setJwtValidator(withClockSkew);
        return jwtDecoder;
    }

    //    Encoder
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {

        SecretKey key = new SecretKeySpec(
                secret.getBytes(),
                "HmacSHA256");
        return new NimbusJwtEncoder(jwkSource);
    }
}
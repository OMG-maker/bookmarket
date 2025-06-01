package com.example.bookmarket.config;

import com.example.bookmarket.auth.exception.CustomAccessDeniedHandler;
import com.example.bookmarket.auth.exception.CustomAuthenticationEntryPoint;
import com.example.bookmarket.auth.service.CustomUserDetailsService;
import com.example.bookmarket.auth.token.JwtAuthenticationFilter;
import com.example.bookmarket.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Spring Security의 필터 체인을 구성합니다.
     * JWT 인증 필터를 추가하고, 요청에 대한 권한을 설정합니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (REST API에서는 일반적으로 필요 없음)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음 (JWT 인증을 사용하기 때문)
                .exceptionHandling(ex -> ex                       // 인증 및 인가 예외 처리 설정
                        .authenticationEntryPoint(authenticationEntryPoint()) // 인증 실패 시 사용자에게 응답을 반환하는 EntryPoint 설정
                        .accessDeniedHandler(accessDeniedHandler()) // 인가 실패 시 사용자에게 응답을 반환하는 AccessDeniedHandler 설정
                )
                .authorizeHttpRequests(auth -> auth // 요청 권한 설정
                        .requestMatchers("/auth/**").permitAll() // 로그인, 회원가입 등 공개 API는 인증 없이 접근 허용
                        .requestMatchers("/books/**").permitAll() // 책 관련 API는 모두 접근 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-ui/index.html/**"
                        ).permitAll() // Swagger 관련 API는 모두 접근 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 API는 ADMIN 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN") // 사용자 API는 USER 또는 ADMIN 역할을 가진 사용자만 접근 가능
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService), // JWT 인증 필터 추가
                        UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정

        return http.build(); // SecurityFilterChain 객체 반환
    }

    /**
     * 인증 매니저를 Bean으로 등록합니다.
     * Spring Security의 인증 매니저를 사용하여 인증을 처리합니다.
     *
     * @param config AuthenticationConfiguration 객체
     * @return AuthenticationManager 객체
     * @throws Exception 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // AuthenticationConfiguration에서 인증 매니저를 가져옵니다.
    }

    /**
     * 비밀번호 인코더를 Bean으로 등록합니다.
     * BCryptPasswordEncoder를 사용하여 비밀번호를 안전하게 인코딩합니다.
     *
     * @return PasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 알고리즘을 사용하여 비밀번호를 인코딩합니다.
    }

    /**
     * 인증 실패 시 사용자에게 응답을 반환하는 AuthenticationEntryPoint를 Bean으로 등록합니다.
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됩니다.
     *
     * @return AuthenticationEntryPoint 객체
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * 접근 거부 시 사용자에게 응답을 반환하는 AccessDeniedHandler를 Bean으로 등록합니다.
     * 인증된 사용자가 권한이 없는 리소스에 접근할 때 호출됩니다.
     *
     * @return AccessDeniedHandler 객체
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}

package com.example.bookmarket.auth.token;

import com.example.bookmarket.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * HTTP 요청이 들어올 때마다 호출되는 필터 메서드입니다.
     * JWT 토큰을 검사하고, 유효한 경우 인증 정보를 SecurityContext에 설정합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 다음 필터 체인
     * @throws ServletException   서블릿 예외
     * @throws IOException        입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String header = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 토큰 추출

        if (header != null && header.startsWith("Bearer ")) { // 토큰이 존재하고 "Bearer "로 시작하는지 확인
            String token = header.substring(7); // "Bearer " 이후의 토큰 부분만 추출
            if (jwtTokenProvider.validateToken(token)) { // 토큰 유효성 검사
                String username = jwtTokenProvider.getUsername(token); // 토큰에서 사용자 이름 추출

                var userDetails = userDetailsService.loadUserByUsername(username); // 사용자 정보 로드
                var auth = new UsernamePasswordAuthenticationToken( // 인증 객체 생성
                        userDetails, null, userDetails.getAuthorities()); // 권한 정보 포함

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 인증 객체에 요청 세부 정보 추가

                SecurityContextHolder.getContext().setAuthentication(auth); // 인증 정보를 SecurityContext에 저장
            }
        }

        filterChain.doFilter(request, response); // 다음 필터 또는 리소스로 요청 전달
    }
}

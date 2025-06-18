package com.example.bookmarket.auth.token;

import com.example.bookmarket.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService,
                                   RedisTemplate<String, String> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
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

//        System.out.println("===== JwtAuthenticationFilter: Filtering request =====");

        String header = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 토큰 추출

        if (header != null && header.startsWith("Bearer ")) { // 토큰이 존재하고 "Bearer "로 시작하는지 확인
            String token = header.substring(7); // "Bearer " 이후의 토큰 부분만 추출
//            System.out.println("Extracted Token: " + token);

            // 🔐 Redis에 블랙리스트로 등록된 토큰인지 확인
            String isLoggedOut = redisTemplate.opsForValue().get("logout:" + token);

            if (isLoggedOut != null) {
                // 이미 로그아웃된 토큰인 경우
//                System.out.println("Token is blacklisted");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token is invalid (logged out)\"}");
                return;
            }

            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUsername(token);
//                System.out.println("[JwtAuthFilter] Valid token for email: " + email);

                var userDetails = userDetailsService.loadUserByUsername(email);
//                System.out.println("[JwtAuthFilter] Loaded UserDetails: " + userDetails.getUsername());

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
//                System.out.println("[JwtAuthFilter] Authentication set in SecurityContext");
            }
//            else {
//                System.out.println("[JwtAuthFilter] Invalid token");
//            }
        }
//        else {
//            System.out.println("No Authorization header or does not start with Bearer");
//        }

        filterChain.doFilter(request, response); // 다음 필터 또는 리소스로 요청 전달
    }
}

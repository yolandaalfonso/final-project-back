package dev.yol.final_project_back.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter{
    private static final String BEARER_PREFIX = "Bearer ";
    /* private static final String USER_ID_CLAIM = "user_id"; */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final FirebaseAuth firebaseAuth;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationFilter(FirebaseAuth firebaseAuth, ObjectMapper objectMapper) {
        this.firebaseAuth = firebaseAuth;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.replace(BEARER_PREFIX, "");
            Optional<String> userId = extractUserIdFromToken(token);

            if (userId.isPresent()) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        userId.get(), null, null);
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                setAuthErrorDetails(response);
                return;
            }
        }

        // 🔹 Continuar con el resto de filtros
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractUserIdFromToken(String token) {
        try {
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token, true);
            String userId = firebaseToken.getUid();
            return Optional.of(userId);
        } catch (FirebaseAuthException e) {
            return Optional.empty();
        }
    }

    private void setAuthErrorDetails(HttpServletResponse response) throws IOException {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        response.setStatus(unauthorized.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                unauthorized,
                "Authentication failure: Token missing, invalid or expired");

        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}

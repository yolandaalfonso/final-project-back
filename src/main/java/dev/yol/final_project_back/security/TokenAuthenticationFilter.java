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

        // 🔹 Si no hay token, dejamos pasar (podría ser endpoint público)
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            System.out.println("⚠️ No se encontró cabecera Authorization o no empieza con 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            // ✅ Verificamos el token con Firebase
            FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token, true);
            String uid = firebaseToken.getUid();
            String email = firebaseToken.getEmail();

            System.out.println("🟢 Token válido. UID: " + uid + " | Email: " + email);

            // ✅ Creamos la autenticación
            var authentication = new UsernamePasswordAuthenticationToken(uid, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // ✅ Guardamos la autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (FirebaseAuthException e) {
            System.out.println("❌ Error al verificar token Firebase: " + e.getMessage());
            setAuthErrorDetails(response);
            return;
        }

        // ✅ Continuar la ejecución de la cadena
        filterChain.doFilter(request, response);
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

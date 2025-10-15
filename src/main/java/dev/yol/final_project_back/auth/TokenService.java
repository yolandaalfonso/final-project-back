package dev.yol.final_project_back.auth;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {

        Instant now = Instant.now();

        // recuperar scopes de usuario
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                /* .filter(authority -> !authority.startsWith("ROLE")) */
                .collect(Collectors.joining(" "));

        System.out.println("<--------------" + scope.toString());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("scope", scope)
                .build();

        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }
}

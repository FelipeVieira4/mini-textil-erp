package com.minitextil.erp.components.core.security;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minitextil.erp.operador.model.Operador;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${app.jwt.secret}") String secretBase64,
                      @Value("${app.jwt.expiration-minutes:60}") long expirationMinutes) {
        // segredo em Base64, com pelo menos 32 bytes (HS256)
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
        this.expirationMinutes = expirationMinutes;
    }

    public String gerarToken(Operador operador) {
        Instant agora = Instant.now();
        return Jwts.builder()
                // loginName é o que o AutenticacaoService usa para buscar o operador
                // (Operador.getUsername() devolve o "nome", então não use ele aqui)
                .subject(operador.getLoginName())
                .claim("operadorId", operador.getId())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(expirationMinutes * 60)))
                .signWith(key)
                .compact();
    }

    /** Devolve o loginName se o token for válido e não estiver expirado. */
    public Optional<String> extrairLoginName(String token) {
        try {
            String subject = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Optional.ofNullable(subject);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public long getExpirationSeconds() {
        return expirationMinutes * 60;
    }
}

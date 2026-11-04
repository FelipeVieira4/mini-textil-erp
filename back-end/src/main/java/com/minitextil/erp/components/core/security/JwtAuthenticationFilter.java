package com.minitextil.erp.components.core.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.minitextil.erp.operador.service.AutenticacaoService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Não é @Component de propósito: é criado dentro do SecurityFilterChain,
 * para o Spring Boot não registrá-lo uma segunda vez como filtro de servlet.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AutenticacaoService autenticacaoService;

    public JwtAuthenticationFilter(JwtService jwtService, AutenticacaoService autenticacaoService) {
        this.jwtService = jwtService;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            jwtService.extrairLoginName(header.substring(7)).ifPresent(loginName -> {
                try {
                    // busca no banco a cada requisição: se o operador for desativado,
                    // o token deixa de funcionar na hora
                    UserDetails operador = autenticacaoService.loadUserByUsername(loginName);

                    if (operador.isEnabled()) {
                        var auth = new UsernamePasswordAuthenticationToken(
                                operador, null, operador.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // o principal continua sendo o Operador, então
                        // SecurityUtils.getOperadorLogado() segue funcionando
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (UsernameNotFoundException ignored) {
                    // token de um operador que não existe mais: segue sem autenticação (401)
                }
            });
        }

        chain.doFilter(request, response);
    }
}

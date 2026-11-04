package com.minitextil.erp.operador.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.minitextil.erp.components.core.security.JwtService;
import com.minitextil.erp.components.core.security.SecurityUtils;
import com.minitextil.erp.operador.model.Operador;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public record LoginRequest(@NotBlank String loginName, @NotBlank String senha) {}

    // ajuste o tipo de empresaId para o tipo real do id da sua Empresa
    public record OperadorDto(Long id, String nome, String loginName, Long empresaId) {}

    public record LoginResponse(String token, long expiresInSeconds, OperadorDto operador) {}

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            // o AuthenticationManager usa o AutenticacaoService + PasswordEncoder,
            // o mesmo caminho que o formulário do Vaadin usava
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.loginName(), request.senha()));

            Operador operador = (Operador) auth.getPrincipal();
            return new LoginResponse(jwtService.gerarToken(operador),
                                     jwtService.getExpirationSeconds(),
                                     toDto(operador));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }
    }

    /** O front chama ao abrir o app para validar o token guardado e recuperar o operador. */
    @GetMapping("/me")
    public OperadorDto me() {
        Operador operador = SecurityUtils.getOperadorLogado();
        if (operador == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return toDto(operador);
    }

    private OperadorDto toDto(Operador o) {
        Long empresaId = o.getEmpresa() != null ? o.getEmpresa().getId() : null;
        return new OperadorDto(o.getId(), o.getNome(), o.getLoginName(), empresaId);
    }
}

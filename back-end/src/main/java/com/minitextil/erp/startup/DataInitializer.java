package com.minitextil.erp.startup;

import com.minitextil.erp.components.properties.OperadorAdminProperties;
import com.minitextil.erp.operador.model.Operador;
import com.minitextil.erp.operador.repository.OperadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final OperadorRepository operadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final OperadorAdminProperties adminProperties;

    public DataInitializer(OperadorRepository operadorRepository, PasswordEncoder passwordEncoder,OperadorAdminProperties adminProperties) {
        this.operadorRepository = operadorRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties=adminProperties;
    }


    @Override
    public void run(String... args) {
        if (!adminProperties.isCreateNewUser()) {
            return;
        }

        if (adminProperties.getPassword() == null || adminProperties.getPassword().isBlank()) {
            log.warn("app.root-user.password is not set — skipping root user creation");
            return;
        }

        Operador operadorAdmin = new Operador();
        operadorAdmin.setAtivo(true);
        operadorAdmin.setNome(adminProperties.getName());
        operadorAdmin.setLoginName(adminProperties.getLoginName());
        operadorAdmin.setSenha(passwordEncoder.encode(adminProperties.getPassword()));

        operadorRepository.save(operadorAdmin);
        log.info("Operador Root '{}' criado com sucesso", adminProperties.getName());
    }
}
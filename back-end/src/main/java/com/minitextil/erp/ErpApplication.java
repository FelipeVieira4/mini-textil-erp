package com.minitextil.erp;

import com.minitextil.erp.components.properties.OperadorAdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SuppressWarnings("deprecation")
@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles.css")
@Theme("erp")
@EnableConfigurationProperties(OperadorAdminProperties.class)
public class ErpApplication implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
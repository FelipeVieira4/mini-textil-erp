package com.minitextil.erp.app.view;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends HorizontalLayout {
    private static final long serialVersionUID = 1L;
    
    public LoginView() {
        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);

        VerticalLayout loginContainer = new VerticalLayout();
        loginContainer.setWidth("30%");
        
        loginContainer.setHeightFull();
        loginContainer.setAlignItems(Alignment.CENTER);
        loginContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        
        LoginForm login = new LoginForm();
        login.setAction("login");
        
        login.setForgotPasswordButtonVisible(false);
        
        loginContainer.add(login);

        Image loginImage = new Image("images/login_image.jpg", "Imagem de Fundo");
        loginImage.setWidth("70%"); 
        loginImage.setHeight("100%");
        loginImage.getStyle().set("object-fit", "cover");

        add(loginContainer, loginImage);
    }
}
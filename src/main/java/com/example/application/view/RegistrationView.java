package com.example.application.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class RegistrationView extends VerticalLayout implements BeforeEnterObserver {

    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final Button registerButton = new Button("Register");

    public RegistrationView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        registerButton.addClickListener(e -> registerUser());

        add(new H1("Registration"), usernameField, passwordField, registerButton);
    }

    private void registerUser() {
        // Tutaj dodaj logikę rejestracji użytkownika, np. zapis do bazy danych
        // Możesz wykorzystać serwis do obsługi logiki biznesowej, np. UserService

        String username = usernameField.getValue();
        String password = passwordField.getValue();

        // ... logika rejestracji ...

        // Przekieruj użytkownika na stronę logowania po zakończeniu rejestracji
        UI.getCurrent().getPage().setLocation("/login");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Dodaj logikę wstępnego czyszczenia pól formularza, np. resetowanie wartości
        usernameField.setValue("");
        passwordField.setValue("");
    }
}

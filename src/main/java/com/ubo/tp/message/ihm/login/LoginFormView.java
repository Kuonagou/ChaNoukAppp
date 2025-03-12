package com.ubo.tp.message.ihm.login;

import javax.swing.*;
import java.awt.*;

public class LoginFormView extends JPanel {
   private final LoginController loginController;

    public LoginFormView(LoginController loginController) {
        this.loginController = loginController;
        this.init();
    }

    public void doLogin(String tag, String password) {
        this.loginController.doLogin(tag, password);
    }

    public void init(){
        this.setBorder(BorderFactory.createTitledBorder("Connexion"));
        this.setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champ Tag
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(new JLabel("Tag :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField connexionTagField = new JTextField(20);
        this.add(connexionTagField, gbc);

        // Champ Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField connexionPasswordField = new JPasswordField(20);
        this.add(connexionPasswordField, gbc);

        // Bouton Se connecter
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Se connecter");
        this.add(loginButton, new GridBagConstraints());

        // Action du bouton Se connecter
        loginButton.addActionListener(e -> {
            String tag = connexionTagField.getText();
            String password = new String(connexionPasswordField.getPassword());
            this.doLogin(tag, password);
        });

        // Ajouter le panel du formulaire de connexion à la fenêtre principale
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 3; // Placé sous le formulaire d'ajout d'utilisateur
        gbc3.weightx = 1.0;
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.insets = new Insets(10, 10, 10, 10);


        // Create scroll pane for user list
        JScrollPane loginPanel = new JScrollPane(this);
        loginPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        loginPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

}

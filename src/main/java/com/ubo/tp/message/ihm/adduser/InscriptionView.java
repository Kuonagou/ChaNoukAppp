package com.ubo.tp.message.ihm.adduser;

import com.ubo.tp.message.common.PropertiesManager;

import javax.swing.*;
import java.awt.*;

public class InscriptionView extends JPanel {

    protected final InscriptionController inscriptionController;

    public InscriptionView(InscriptionController inscriptionController) {
        this.inscriptionController = inscriptionController;
        this.init();
    }

    public void addUser(String nom, String password, String tag, String avatar) {
        this.inscriptionController.addUser(nom, password, tag, avatar);
    }

    public void init(){

        this.setBorder(BorderFactory.createTitledBorder(PropertiesManager.getString("AJOUT_UTILISATEUR")));
        this.setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Champ Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(new JLabel(PropertiesManager.getString("NOM")), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField nomTextField = new JTextField(20);
        this.add(nomTextField, gbc);

        // Champ Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(new JLabel(PropertiesManager.getString("MDP")), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        this.add(passwordField, gbc);

        // Champ Tag
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(new JLabel(PropertiesManager.getString("TAG")), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField tagField = new JTextField(20);
        this.add(tagField, gbc);

        // Champ Avatar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        this.add(new JLabel(PropertiesManager.getString("AVATAR")), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField avatarPathField = new JTextField(20);
        this.add(avatarPathField, gbc);

        // Bouton Ajouter Utilisateur
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addUserButton = new JButton(PropertiesManager.getString("AJOUT_UTILISATEUR"));
        this.add(addUserButton, gbc);

        addUserButton.addActionListener(e -> {
            this.addUser(nomTextField.getText(), passwordField.getText(), tagField.getText(), avatarPathField.getText());
        });

        // Create scroll pane for user list
        JScrollPane loginPanel = new JScrollPane(this);
        loginPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        loginPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Ajouter le panel du formulaire utilisateur à la fenêtre principale
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 2; // Placé sous la console
        gbc2.weightx = 1.0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 10, 10, 10);

    }
}

package com.ubo.tp.message.ihm.login;

import com.ubo.tp.message.common.PropertiesManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;

public class LoginFormView extends JPanel {
    private final LoginController loginController;
    private JFXPanel jfxPanel;

    // Composants JavaFX
    private TextField tagField;
    private PasswordField passwordField;

    public LoginFormView(LoginController loginController) {
        this.loginController = loginController;

        // Initialiser le panel Swing qui contiendra le contenu JavaFX
        this.setLayout(new BorderLayout());
        jfxPanel = new JFXPanel();
        this.add(jfxPanel, BorderLayout.CENTER);

        // Initialiser l'interface JavaFX
        Platform.runLater(this::init);
    }

    public void doLogin(String tag, String password) {
        this.loginController.doLogin(tag, password);
    }

    public void init() {
        // Conteneur principal
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFC0CB;"); // Équivalent à Color.PINK
        root.setPadding(new Insets(20));

        // Titre
        Text title = new Text(PropertiesManager.getString("CONNEXION"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        root.setTop(titleBox);

        // Formulaire de connexion
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 0, 20, 0));
        grid.setAlignment(Pos.CENTER);

        // Champ Tag
        Label tagLabel = new Label(PropertiesManager.getString("TAG"));
        grid.add(tagLabel, 0, 0);

        tagField = new TextField();
        tagField.setPromptText(PropertiesManager.getString("TAG"));
        tagField.setPrefColumnCount(20);
        grid.add(tagField, 1, 0);

        // Champ Mot de passe
        Label passwordLabel = new Label(PropertiesManager.getString("MDP"));
        grid.add(passwordLabel, 0, 1);

        passwordField = new PasswordField();
        passwordField.setPromptText(PropertiesManager.getString("MDP"));
        passwordField.setPrefColumnCount(20);
        grid.add(passwordField, 1, 1);

        // Bouton de connexion
        Button loginButton = new Button(PropertiesManager.getString("SE_CONNECTER"));
        HBox buttonBox = new HBox(loginButton);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 2, 2, 1);

        // Ajouter le formulaire au conteneur principal
        root.setCenter(grid);

        // Action du bouton de connexion
        loginButton.setOnAction(e -> {
            String tag = tagField.getText();
            String password = passwordField.getText();

            // Utiliser SwingUtilities pour interagir avec le code Swing
            SwingUtilities.invokeLater(() -> {
                doLogin(tag, password);
            });
        });

        // Créer et attacher la scène
        Scene scene = new Scene(root, 400, 300);
        jfxPanel.setScene(scene);
    }
}
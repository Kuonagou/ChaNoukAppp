package com.ubo.tp.message.ihm.userHome;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.ihm.Listener;
import com.ubo.tp.message.ihm.logout.LogoutController;

import javax.swing.*;
import java.awt.*;

public class UserHomeView extends JPanel {

    /**
     * Contrôleur de déconnexion.
     */
    protected LogoutController mLogoutController;

    /**
     * Listener pour les interactions.
     */
    protected Listener mListener;

    /**
     * Constructeur par défaut.
     */
    public UserHomeView() {
        this(null, null);
    }

    /**
     * Constructeur avec paramètres.
     *
     * @param logoutController Contrôleur de déconnexion
     * @param listener Listener pour les interactions
     */
    public UserHomeView(LogoutController logoutController, Listener listener) {
        this.mLogoutController = logoutController;
        this.mListener = listener;
    }

    /**
     * Initialise la vue.
     */
    public void init() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createTitledBorder(PropertiesManager.getString("WELCOME_BASIC")));
        this.setBackground(Color.PINK);

        // Panel du centre avec message de bienvenue
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.PINK);
        JLabel welcomeLabel = new JLabel(PropertiesManager.getString("MESSAGE_BIENVENUE_CONNEXION"));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(welcomeLabel);

        // Panel du bas avec boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.PINK);

        // Bouton pour consulter le profil
        JButton profileButton = new JButton(PropertiesManager.getString("MON_PROFIL"));
        profileButton.addActionListener(e -> {
            if (mListener != null) {
                mListener.showUserProfile();
            }
        });
        buttonPanel.add(profileButton);

        // Bouton de déconnexion
        JButton logoutButton = new JButton(PropertiesManager.getString("DECONNEXION"));
        logoutButton.addActionListener(e -> performLogout());
        buttonPanel.add(logoutButton);

        // Assemblage final
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Effectue la déconnexion.
     */
    private void performLogout() {
        if (mLogoutController != null && mLogoutController.isUserConnected()) {
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    PropertiesManager.getString("CHECK_DECONNEXION"),
                    PropertiesManager.getString("CONFIRM_DECONNEXION"),
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                mLogoutController.logout();

                if (mListener != null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Vous avez été déconnecté avec succès.",
                            "Déconnexion réussie",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        } else {
            // Fallback si le contrôleur n'est pas disponible
            if (mListener != null) {
                mListener.showConnection();
            }
        }
    }
}
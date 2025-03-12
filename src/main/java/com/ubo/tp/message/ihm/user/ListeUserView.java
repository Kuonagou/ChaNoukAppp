package com.ubo.tp.message.ihm.user;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.core.user.DisplayableUserInfo;
import com.ubo.tp.message.core.user.IUserObserver;
import com.ubo.tp.message.core.user.IUserDisplayObserver;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue pour l'affichage de la liste des utilisateurs.
 * Implémente IUserObserver pour être notifiée des changements dans la liste des utilisateurs.
 * Implémente IUserDisplayObserver pour être notifiée des changements dans les informations d'affichage.
 */
public class ListeUserView extends JPanel implements IUserObserver, IUserDisplayObserver {
    /**
     * Contrôleur pour les opérations liées aux utilisateurs.
     */
    private final UserController userController;

    /**
     * Panel contenant les utilisateurs.
     */
    private JPanel contentPanel;

    /**
     * Map des panneaux utilisateurs pour pouvoir les mettre à jour
     */
    private Map<User, JPanel> userPanels;

    /**
     * Map pour stocker localement les informations d'affichage
     */
    private Map<User, DisplayableUserInfo> displayInfoMap;

    /**
     * Constructeur.
     *
     * @param userController Contrôleur pour les opérations liées aux utilisateurs
     */
    public ListeUserView(UserController userController) {
        this.userController = userController;
        this.userPanels = new HashMap<>();
        this.displayInfoMap = new HashMap<>();
        initPanel();
    }

    /**
     * Initialise le panneau.
     */
    private void initPanel() {
        this.setLayout(new BorderLayout());

        // 📜 Panel contenant les utilisateurs (ajouté dans le JScrollPane)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 🔽 Ajout du scroll pane
        JScrollPane userScrollPane = new JScrollPane(contentPanel);
        userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // ✨ Définir une taille fixe pour le scrollPane pour s'assurer que la barre de défilement s'affiche
        this.setPreferredSize(new Dimension(500, 400));

        // 📌 Ajout du scroll dans le panel principal
        this.add(userScrollPane, BorderLayout.CENTER);
    }

    /**
     * Méthode appelée par le modèle lorsque la liste des utilisateurs change.
     * Met à jour l'interface avec la nouvelle liste.
     *
     * @param newUsersList Nouvelle liste des utilisateurs
     */
    @Override
    public void notifyUserListChanged(List<User> newUsersList) {

        // Vider le panel des utilisateurs et la map
        contentPanel.removeAll();
        userPanels.clear();

        if (newUsersList.isEmpty()) {
            // Afficher un message si la liste est vide
            JLabel emptyLabel = new JLabel(PropertiesManager.getString("NO_USER_FOUND"));
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 14));
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            contentPanel.add(emptyLabel);
        } else {
            // Parcourir la liste des utilisateurs
            int displayCount = 0;
            for (User user : newUsersList) {
                // Créer et ajouter le panel pour cet utilisateur
                JPanel userBox = createUserPanel(user);
                if (userBox != null) {
                    userPanels.put(user, userBox);
                    contentPanel.add(userBox);
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement
                    displayCount++;
                }
            }
        }

        // Mettre à jour la taille préférée du panel
        contentPanel.setPreferredSize(new Dimension(400, Math.max(300, newUsersList.size() * 70)));

        // Mettre à jour l'affichage
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Méthode appelée par le modèle lorsque les informations d'affichage d'un utilisateur sont mises à jour.
     *
     * @param user L'utilisateur dont les informations d'affichage ont été mises à jour
     * @param displayInfo Les informations d'affichage mises à jour
     */
    @Override
    public void notifyUserDisplayUpdated(User user, DisplayableUserInfo displayInfo) {
        // Stocker les informations d'affichage localement
        displayInfoMap.put(user, displayInfo);

        // Mettre à jour le panneau de l'utilisateur si nécessaire
        JPanel existingPanel = userPanels.get(user);
        if (existingPanel != null) {
            // Trouver l'index du panel existant
            int index = -1;
            Component[] components = contentPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] == existingPanel) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Retirer l'ancien panneau
                contentPanel.remove(existingPanel);

                // Créer un nouveau panneau avec les informations à jour
                JPanel updatedPanel = createUserPanel(user);
                userPanels.put(user, updatedPanel);

                // Ajouter le nouveau panneau à la même position
                contentPanel.add(updatedPanel, index);

                // Rafraîchir l'affichage
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        } else {
            // Si le panneau n'existe pas encore, ajouter un nouveau
            JPanel newPanel = createUserPanel(user);
            if (newPanel != null) {
                userPanels.put(user, newPanel);
                contentPanel.add(newPanel);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement

                // Rafraîchir l'affichage
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        }
    }

    /**
     * Crée un panneau pour un utilisateur.
     *
     * @param user Utilisateur à afficher
     * @return Panneau contenant les informations de l'utilisateur ou null si les informations ne sont pas disponibles
     */
    private JPanel createUserPanel(User user) {
        // Obtenir les informations d'affichage depuis notre stockage local
        DisplayableUserInfo userInfo = displayInfoMap.get(user);

        if (userInfo == null) {
            // Si les informations d'affichage ne sont pas encore disponibles
            JPanel loadingPanel = new JPanel(new BorderLayout());
            loadingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.PINK, 2, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            loadingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            loadingPanel.setBackground(Color.PINK);

            JLabel loadingLabel = new JLabel("Chargement des informations...");
            loadingPanel.add(loadingLabel, BorderLayout.CENTER);

            return loadingPanel;
        }

        JPanel userBox = new JPanel(new BorderLayout());
        userBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.PINK, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        userBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Hauteur fixe
        userBox.setBackground(Color.PINK);

        // Infos utilisateur
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.PINK);

        JLabel nameLabel = new JLabel(userInfo.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel tagLabel = new JLabel(userInfo.tag);
        tagLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(tagLabel);

        userBox.add(infoPanel, BorderLayout.CENTER);

        // Si un utilisateur est connecté, ajouter les boutons Suivre/Ne plus suivre
        if (userInfo.isConnected) {
            JButton followButton = new JButton(userInfo.isFollowing ?
                    PropertiesManager.getString("NE_PLUS_SUIVRE") :
                    PropertiesManager.getString("SUIVRE"));
            followButton.setFocusPainted(false);

            if (userInfo.isFollowing) {
                followButton.setBackground(new Color(255, 200, 200));
            } else {
                followButton.setBackground(new Color(200, 255, 200));
            }

            // Créer l'action du bouton ici, car c'est spécifique à la vue
            followButton.addActionListener(e -> {
                if (userInfo.isFollowing) {
                    userController.unfollowUser(user);
                    JOptionPane.showMessageDialog(this,
                            PropertiesManager.getString("MESSAGE_UNFOLLOW") + user.getName(),
                            PropertiesManager.getString("DESA"),
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    userController.followUser(user);
                    JOptionPane.showMessageDialog(this,
                            PropertiesManager.getString("MESSAGE_FOLLOW") + user.getName(),
                            PropertiesManager.getString("ABO"),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

            userBox.add(followButton, BorderLayout.EAST);
        }

        return userBox;
    }
}
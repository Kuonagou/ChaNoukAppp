package com.ubo.tp.message.ihm.user;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.core.user.DisplayableUserInfo;
import com.ubo.tp.message.core.user.IUserSearchObserver;
import com.ubo.tp.message.core.user.IUserDisplayObserver;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue de recherche des utilisateurs.
 * Implémente IUserSearchObserver pour être notifiée des résultats de recherche.
 * Implémente IUserDisplayObserver pour être notifiée des changements dans les informations d'affichage.
 */
public class UserSearchView extends JPanel implements IUserSearchObserver, IUserDisplayObserver {
    private final JTextField searchField;
    private final JButton searchButton;
    private final JPanel resultPanel;
    private final JScrollPane scrollPane;
    private final UserController userController;

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
    public UserSearchView(UserController userController) {
        this.userController = userController;
        this.userPanels = new HashMap<>();
        this.displayInfoMap = new HashMap<>();
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.PINK);

        // 🔍 Titre
        JLabel titleLabel = new JLabel(PropertiesManager.getString("RECHERCHE_USER"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.BLACK);

        // 🎯 Champ de recherche
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.PINK, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 🔘 Bouton de recherche
        searchButton = new JButton(PropertiesManager.getString("RECHERCHER"));
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(Color.PINK);
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);

        // 📌 Panneau de recherche (reste toujours visible)
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setBackground(Color.PINK);

        // 📜 Panneau des résultats
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.WHITE);

        // 🏆 Scroll pane pour afficher les résultats proprement
        scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.PINK, 1));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Fixe une hauteur max

        // 📌 Ajout des composants au panneau principal
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(searchPanel, BorderLayout.PAGE_START); // Recherche toujours visible
        this.add(scrollPane, BorderLayout.CENTER); // Résultats avec scroll

        // 🎯 Action sur le bouton rechercher
        searchButton.addActionListener(this::performSearch);
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur le bouton de recherche.
     * Délègue la recherche au contrôleur.
     *
     * @param e Événement d'action
     */
    private void performSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim();
        userController.searchUsers(searchTerm);
    }

    /**
     * Méthode appelée par le modèle lorsqu'une nouvelle recherche d'utilisateurs est effectuée.
     * Met à jour l'interface avec les résultats de recherche.
     *
     * @param searchUsersList Liste des utilisateurs trouvés
     */
    @Override
    public void notifyNewUserSearch(List<User> searchUsersList) {
        // Vider le panel des résultats et la map des panneaux
        // mais garder la map des informations d'affichage
        resultPanel.removeAll();
        userPanels.clear();

        if (searchUsersList.isEmpty()) {
            // Aucun résultat
            JPanel resultBox = new JPanel(new BorderLayout());
            resultBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.PINK, 2, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            resultBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            resultBox.setBackground(Color.PINK);

            JLabel nameLabel = new JLabel(PropertiesManager.getString("NO_USER_FOUND"));
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setHorizontalAlignment(JLabel.LEFT);

            resultBox.add(nameLabel, BorderLayout.CENTER);
            resultPanel.add(resultBox);
        } else {
            // Ajout des résultats trouvés
            for (User user : searchUsersList) {
                JPanel userBox = createUserBox(user);
                if (userBox != null) {
                    userPanels.put(user, userBox);
                    resultPanel.add(userBox);
                    resultPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre les utilisateurs
                }
            }
        }

        // Augmenter la hauteur en fonction du nombre d'utilisateurs pour permettre le défilement
        int panelHeight = Math.max(300, searchUsersList.size() * 80);
        resultPanel.setPreferredSize(new Dimension(450, panelHeight));

        // 🔄 Met à jour l'affichage
        resultPanel.revalidate();
        resultPanel.repaint();
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
            Component[] components = resultPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
                if (components[i] == existingPanel) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Retirer l'ancien panneau
                resultPanel.remove(existingPanel);

                // Créer un nouveau panneau avec les informations à jour
                JPanel updatedPanel = createUserBox(user);
                userPanels.put(user, updatedPanel);

                // Ajouter le nouveau panneau à la même position
                resultPanel.add(updatedPanel, index);

                // Rafraîchir l'affichage
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        }
    }

    /**
     * Crée un panneau pour un utilisateur.
     *
     * @param user Utilisateur à afficher
     * @return Panneau contenant les informations de l'utilisateur ou null si les informations ne sont pas disponibles
     */
    private JPanel createUserBox(User user) {
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

        JPanel resultBox = new JPanel(new BorderLayout());
        resultBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.PINK, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        resultBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        resultBox.setBackground(Color.PINK);

        // Infos utilisateur
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.PINK);

        JLabel nameLabel = new JLabel(userInfo.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel tagLabel = new JLabel(userInfo.tag);
        tagLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(tagLabel);

        resultBox.add(infoPanel, BorderLayout.CENTER);

        // Si un utilisateur est connecté, ajouter les boutons Suivre/Ne plus suivre
        if (userInfo.isConnected) {
            JButton followButton = new JButton(userInfo.isFollowing ?
                    PropertiesManager.getString("NE_PLUS_SUIVRE") :
                    PropertiesManager.getString("SUIVRE"));
            followButton.setFocusPainted(false);
            followButton.setBackground(userInfo.followButtonColor);

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

                // Actualiser la recherche pour mettre à jour l'interface
                String searchTerm = searchField.getText().trim();
                userController.searchUsers(searchTerm);
            });

            resultBox.add(followButton, BorderLayout.EAST);
        }

        return resultBox;
    }
}
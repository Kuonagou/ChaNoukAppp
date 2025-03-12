package com.ubo.tp.message.ihm.profil;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.Listener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Properties;

/**
 * Vue permettant l'affichage et la modification du profil utilisateur.
 */
public class UserProfileView extends JPanel {

    /**
     * Contrôleur pour le profil utilisateur.
     */
    protected final UserProfilController mProfileController;

    /**
     * Listener pour les interactions avec la vue.
     */
    protected Listener mListener;

    /**
     * Champ du nom d'affichage.
     */
    protected JTextField mNameField;

    /**
     * Champ du mot de passe.
     */
    protected JPasswordField mPasswordField;

    /**
     * Chemin vers l'avatar.
     */
    protected JTextField mAvatarPathField;

    /**
     * Label pour afficher l'avatar.
     */
    protected JLabel mAvatarLabel;

    /**
     * Label pour afficher le tag utilisateur.
     */
    protected JLabel mUserTagLabel;

    /**
     * Label pour afficher le nombre de followers.
     */
    protected JLabel mFollowersCountLabel;

    /**
     * Label pour afficher le nombre de messages.
     */
    protected JLabel mMessagesCountLabel;

    /**
     * Constructeur.
     *
     * @param profileController Contrôleur du profil utilisateur
     */
    public UserProfileView(UserProfilController profileController) {
        this(profileController, null);
    }

    /**
     * Constructeur.
     *
     * @param profileController Contrôleur du profil utilisateur
     * @param listener Listener pour les interactions
     */
    public UserProfileView(UserProfilController profileController, Listener listener) {
        this.mProfileController = profileController;
        this.mListener = listener;
        initView();
    }

    /**
     * Initialise la vue.
     */
    private void initView() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.PINK);

        // Titre
        JLabel titleLabel = new JLabel(PropertiesManager.getString("MON_PROFIL"), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // Panel principal (grille)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Avatar
        mAvatarLabel = new JLabel();
        mAvatarLabel.setPreferredSize(new Dimension(100, 100));
        mAvatarLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mAvatarLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        mainPanel.add(mAvatarLabel, gbc);

        // Informations utilisateur
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(new JLabel(PropertiesManager.getString("TAG")), gbc);

        gbc.gridx = 2;
        mUserTagLabel = new JLabel();
        mUserTagLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(mUserTagLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel(PropertiesManager.getString("NOM")), gbc);

        gbc.gridx = 2;
        mNameField = new JTextField(20);
        mainPanel.add(mNameField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(new JLabel(PropertiesManager.getString("MDP")), gbc);

        gbc.gridx = 2;
        mPasswordField = new JPasswordField(20);
        mainPanel.add(mPasswordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(new JLabel(PropertiesManager.getString("AVATAR")), gbc);

        gbc.gridx = 2;
        JPanel avatarPanel = new JPanel(new BorderLayout(5, 0));
        avatarPanel.setBackground(Color.PINK);
        mAvatarPathField = new JTextField(20);
        avatarPanel.add(mAvatarPathField, BorderLayout.CENTER);

        JButton browseButton = new JButton(PropertiesManager.getString("PARCOURIR"));
        browseButton.addActionListener(e -> browseAvatar());
        avatarPanel.add(browseButton, BorderLayout.EAST);
        mainPanel.add(avatarPanel, gbc);

        // Statistiques
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(new JLabel(PropertiesManager.getString("FOLLOWERS")), gbc);

        gbc.gridx = 2;
        mFollowersCountLabel = new JLabel();
        mainPanel.add(mFollowersCountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(new JLabel(PropertiesManager.getString("MESSAGES")+":"), gbc);

        gbc.gridx = 2;
        mMessagesCountLabel = new JLabel();
        mainPanel.add(mMessagesCountLabel, gbc);

        // Suivis
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        JButton showFollowedButton = new JButton(PropertiesManager.getString("MES_ABOS"));
        showFollowedButton.addActionListener(e -> {
            StringBuilder followedUsers = new StringBuilder(PropertiesManager.getString("SUIVI"));
            mProfileController.getFollowedUsers().forEach(user ->
                    followedUsers.append("- ").append(user.getName()).append(" (@").append(user.getUserTag()).append(")\n")
            );

            JTextArea textArea = new JTextArea(followedUsers.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 200));

            JOptionPane.showMessageDialog(this, scrollPane, PropertiesManager.getString("MES_ABOS"), JOptionPane.INFORMATION_MESSAGE);
        });
        mainPanel.add(showFollowedButton, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.PINK);
        JButton saveButton = new JButton(PropertiesManager.getString("ENREGISTRER"));
        saveButton.addActionListener(e -> saveProfile());
        buttonPanel.add(saveButton);

        JButton backButton = new JButton(PropertiesManager.getString("RETOUR"));
        backButton.addActionListener(e -> {
            if (mListener != null) {
                mListener.showUserHome();
            }
        });
        buttonPanel.add(backButton);

        // Assemblage final
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Initialiser les données
        refreshProfileData();
    }

    /**
     * Rafraîchit les données du profil depuis le modèle.
     */
    public void refreshProfileData() {
        User connectedUser = mProfileController.getConnectedUser();
        if (connectedUser != null) {
            mUserTagLabel.setText(connectedUser.getUserTag());
            mNameField.setText(connectedUser.getName());
            mPasswordField.setText(connectedUser.getUserPassword());
            mAvatarPathField.setText(connectedUser.getAvatarPath());
            mFollowersCountLabel.setText(String.valueOf(mProfileController.getFollowersCount()));
            mMessagesCountLabel.setText(String.valueOf(mProfileController.getMessagesCount()));

            // Affichage de l'avatar
            updateAvatarDisplay(connectedUser.getAvatarPath());
        } else {
            // Si aucun utilisateur n'est connecté, retourner à l'écran de connexion
            if (mListener != null) {
                mListener.showConnection();
            }
        }
    }

    /**
     * Met à jour l'affichage de l'avatar.
     *
     * @param avatarPath Chemin de l'avatar
     */
    private void updateAvatarDisplay(String avatarPath) {
        ImageIcon avatar = null;

        if (avatarPath != null && !avatarPath.isEmpty()) {
            File avatarFile = new File(avatarPath);
            if (avatarFile.exists()) {
                avatar = new ImageIcon(avatarPath);

                // Redimensionner si nécessaire
                if (avatar.getIconWidth() > 100 || avatar.getIconHeight() > 100) {
                    Image img = avatar.getImage();
                    Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    avatar = new ImageIcon(resizedImg);
                }
            }
        }

        if (avatar != null) {
            mAvatarLabel.setIcon(avatar);
            mAvatarLabel.setText("");
        } else {
            mAvatarLabel.setIcon(null);
            mAvatarLabel.setText(PropertiesManager.getString("NO_AVATAR"));
        }
    }

    /**
     * Sauvegarde les modifications apportées au profil.
     */
    private void saveProfile() {
        String newName = mNameField.getText();
        String newPassword = new String(mPasswordField.getPassword());
        String newAvatarPath = mAvatarPathField.getText();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, PropertiesManager.getString("NO_NAME"), PropertiesManager.getString("ERREUR"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, PropertiesManager.getString("NO_MDP"), PropertiesManager.getString("ERREUR"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mise à jour du profil
        mProfileController.updateDisplayName(newName);
        mProfileController.updatePassword(newPassword);
        mProfileController.updateAvatarPath(newAvatarPath);

        // Rafraîchir les données
        refreshProfileData();

        JOptionPane.showMessageDialog(this, PropertiesManager.getString("SUCCESS_UPDATE_PROFILE"), PropertiesManager.getString("SUCCESS"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Ouvre un dialogue pour sélectionner un avatar.
     */
    private void browseAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(PropertiesManager.getString("SELECT_AVATAR"));
        fileChooser.setFileFilter(new FileNameExtensionFilter(PropertiesManager.getString("IMAGES"), "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            mAvatarPathField.setText(selectedFile.getAbsolutePath());
            updateAvatarDisplay(selectedFile.getAbsolutePath());
        }
    }
}
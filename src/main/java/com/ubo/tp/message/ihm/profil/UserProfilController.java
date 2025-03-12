package com.ubo.tp.message.ihm.profil;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.User;
import java.util.Set;

/**
 * Contrôleur pour la gestion du profil utilisateur.
 */
public class UserProfilController {

    /**
     * Session de l'application.
     */
    protected final ISession mSession;

    /**
     * Base de données de l'application.
     */
    protected final IDatabase mDatabase;

    /**
     * Gestionnaire des entités.
     */
    protected final EntityManager mEntityManager;

    /**
     * Constructeur.
     *
     * @param mSession Session active
     * @param mDatabase Base de données
     * @param mEntityManager Gestionnaire d'entités
     */
    public UserProfilController(ISession mSession, IDatabase mDatabase, EntityManager mEntityManager) {
        this.mSession = mSession;
        this.mDatabase = mDatabase;
        this.mEntityManager = mEntityManager;
    }

    /**
     * Retourne l'utilisateur actuellement connecté.
     *
     * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté
     */
    public User getConnectedUser() {
        return mSession.getConnectedUser();
    }

    /**
     * Obtient le nombre de followers de l'utilisateur connecté.
     *
     * @return Le nombre de followers
     */
    public int getFollowersCount() {
        User user = getConnectedUser();
        if (user != null) {
            return mDatabase.getFollowersCount(user);
        }
        return 0;
    }

    /**
     * Obtient la liste des utilisateurs suivis par l'utilisateur connecté.
     *
     * @return La liste des utilisateurs suivis
     */
    public Set<User> getFollowedUsers() {
        User user = getConnectedUser();
        if (user != null) {
            return ((com.ubo.tp.message.core.database.Database)mDatabase).getFollowed(user);
        }
        return java.util.Collections.emptySet();
    }

    /**
     * Obtient le nombre de messages publiés par l'utilisateur connecté.
     *
     * @return Le nombre de messages
     */
    public int getMessagesCount() {
        User user = getConnectedUser();
        if (user != null) {
            return mDatabase.getUserMessages(user).size();
        }
        return 0;
    }

    /**
     * Met à jour le nom d'affichage de l'utilisateur.
     *
     * @param newName Le nouveau nom d'affichage
     */
    public void updateDisplayName(String newName) {
        User user = getConnectedUser();
        if (user != null && !newName.isEmpty()) {
            user.setName(newName);
            mDatabase.modifiyUser(user);
            mEntityManager.writeUserFile(user);
        }
    }

    /**
     * Met à jour le mot de passe de l'utilisateur.
     *
     * @param newPassword Le nouveau mot de passe
     */
    public void updatePassword(String newPassword) {
        User user = getConnectedUser();
        if (user != null && !newPassword.isEmpty()) {
            user.setUserPassword(newPassword);
            mDatabase.modifiyUser(user);
            mEntityManager.writeUserFile(user);
        }
    }

    /**
     * Met à jour le chemin de l'avatar de l'utilisateur.
     *
     * @param newAvatarPath Le nouveau chemin d'avatar
     */
    public void updateAvatarPath(String newAvatarPath) {
        User user = getConnectedUser();
        if (user != null) {
            user.setAvatarPath(newAvatarPath);
            mDatabase.modifiyUser(user);
            mEntityManager.writeUserFile(user);
        }
    }
}
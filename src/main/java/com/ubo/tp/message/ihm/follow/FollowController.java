package com.ubo.tp.message.ihm.follow;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.User;

/**
 * Contrôleur pour la gestion des abonnements à d'autres utilisateurs.
 */
public class FollowController {

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
     * @param session Session active
     * @param database Base de données
     * @param entityManager Gestionnaire d'entités
     */
    public FollowController(ISession session, IDatabase database, EntityManager entityManager) {
        this.mSession = session;
        this.mDatabase = database;
        this.mEntityManager = entityManager;
    }

    /**
     * Vérifie si l'utilisateur connecté est abonné à un utilisateur spécifique.
     *
     * @param user Utilisateur à vérifier
     * @return true si l'utilisateur connecté est abonné, false sinon
     */
    public boolean isFollowing(User user) {
        User connectedUser = mSession.getConnectedUser();
        if (connectedUser != null && user != null) {
            return connectedUser.isFollowing(user);
        }
        return false;
    }

    /**
     * Abonne l'utilisateur connecté à un utilisateur spécifique.
     *
     * @param userToFollow Utilisateur auquel s'abonner
     * @return true si l'opération est réussie, false sinon
     */
    public boolean followUser(User userToFollow) {
        User connectedUser = mSession.getConnectedUser();
        if (connectedUser != null && userToFollow != null && !connectedUser.equals(userToFollow)) {
            // Vérifier que l'utilisateur n'est pas déjà suivi
            if (!connectedUser.isFollowing(userToFollow)) {
                // Ajouter l'utilisateur à la liste des suivis
                connectedUser.addFollowing(userToFollow.getUserTag());

                // Mettre à jour dans la base de données
                mDatabase.modifiyUser(connectedUser);

                // Sauvegarder dans le fichier
                mEntityManager.writeUserFile(connectedUser);

                return true;
            }
        }
        return false;
    }

    /**
     * Désabonne l'utilisateur connecté d'un utilisateur spécifique.
     *
     * @param userToUnfollow Utilisateur duquel se désabonner
     * @return true si l'opération est réussie, false sinon
     */
    public boolean unfollowUser(User userToUnfollow) {
        User connectedUser = mSession.getConnectedUser();
        if (connectedUser != null && userToUnfollow != null) {
            // Vérifier que l'utilisateur est bien suivi
            if (connectedUser.isFollowing(userToUnfollow)) {
                // Retirer l'utilisateur de la liste des suivis
                connectedUser.removeFollowing(userToUnfollow.getUserTag());

                // Mettre à jour dans la base de données
                mDatabase.modifiyUser(connectedUser);

                // Sauvegarder dans le fichier
                mEntityManager.writeUserFile(connectedUser);

                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un utilisateur est connecté.
     *
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isUserConnected() {
        return mSession.getConnectedUser() != null;
    }

    /**
     * Obtient l'utilisateur connecté.
     *
     * @return l'utilisateur connecté ou null si aucun utilisateur n'est connecté
     */
    public User getConnectedUser() {
        return mSession.getConnectedUser();
    }
}
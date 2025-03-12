package com.ubo.tp.message.ihm.user;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.core.user.DisplayableUserInfo;
import com.ubo.tp.message.core.user.IUserModel;
import com.ubo.tp.message.datamodel.User;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Contrôleur pour la gestion des utilisateurs.
 * Agit comme intermédiaire entre les vues (UserSearchView, ListeUserView) et le modèle.
 */
public class UserController {

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
     * Modèle des utilisateurs.
     */
    protected final IUserModel mUserModel;

    /**
     * Constructeur.
     *
     * @param session       Session active
     * @param database      Base de données
     * @param entityManager Gestionnaire d'entités
     * @param userModel     Modèle des utilisateurs
     */
    public UserController(ISession session, IDatabase database, EntityManager entityManager, IUserModel userModel) {
        this.mSession = session;
        this.mDatabase = database;
        this.mEntityManager = entityManager;
        this.mUserModel = userModel;
    }

    /**
     * Récupère tous les utilisateurs.
     * Notifie le modèle de la modification de la liste des utilisateurs.
     */
    public void getAllUsers() {
        Set<User> userSet = mDatabase.getUsers();
        List<User> userList = new ArrayList<>(userSet);


        // Préparer les informations d'affichage pour chaque utilisateur
        for (User user : userList) {
            prepareUserDisplayInfo(user);
        }

        mUserModel.listUserChanged(userList);
    }

    /**
     * Effectue une recherche d'utilisateurs en fonction d'un terme de recherche.
     * Notifie le modèle des résultats de la recherche.
     *
     * @param searchTerm Terme de recherche
     */
    public void searchUsers(String searchTerm) {
        Set<User> allUsers = mDatabase.getUsers();
        List<User> searchResults = new ArrayList<>();
        User connectedUser = mSession.getConnectedUser();

        // Si le terme de recherche est vide, retourner tous les utilisateurs
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            for (User user : allUsers) {
                // Ne pas inclure l'utilisateur connecté dans les résultats
                if (connectedUser == null || !user.equals(connectedUser)) {
                    searchResults.add(user);
                }
            }
        } else {
            // Recherche par nom ou tag
            String lowerCaseSearchTerm = searchTerm.toLowerCase().trim();
            for (User user : allUsers) {
                // Ne pas inclure l'utilisateur connecté dans les résultats
                if (connectedUser != null && user.equals(connectedUser)) {
                    continue;
                }

                if (user.getName().toLowerCase().contains(lowerCaseSearchTerm) ||
                        user.getUserTag().toLowerCase().contains(lowerCaseSearchTerm)) {
                    searchResults.add(user);
                }
            }
        }

        // Préparer les informations d'affichage pour chaque utilisateur
        for (User user : searchResults) {
            prepareUserDisplayInfo(user);
        }

        // Notifier le modèle des résultats de recherche
        mUserModel.makeNewSearch(searchResults);
    }

    /**
     * Suit un utilisateur. L'utilisateur connecté s'abonne à l'utilisateur spécifié.
     *
     * @param userToFollow Utilisateur à suivre
     */
    public void followUser(User userToFollow) {
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

                // Mettre à jour les informations d'affichage
                prepareUserDisplayInfo(userToFollow);

                // Rafraîchir les listes d'utilisateurs
                getAllUsers();
            }
        }
    }

    /**
     * Ne plus suivre un utilisateur. L'utilisateur connecté se désabonne de l'utilisateur spécifié.
     *
     * @param userToUnfollow Utilisateur à ne plus suivre
     */
    public void unfollowUser(User userToUnfollow) {
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

                // Mettre à jour les informations d'affichage
                prepareUserDisplayInfo(userToUnfollow);

                // Rafraîchir les listes d'utilisateurs
                getAllUsers();
            }
        }
    }

    /**
     * Prépare et stocke les informations d'affichage pour un utilisateur.
     * Cette méthode met à jour le modèle avec les données préparées.
     *
     * @param user Utilisateur pour lequel préparer les informations d'affichage
     */
    public void prepareUserDisplayInfo(User user) {
        DisplayableUserInfo displayInfo = new DisplayableUserInfo();
        displayInfo.name = user.getName();
        displayInfo.tag = "@" + user.getUserTag();

        User connectedUser = mSession.getConnectedUser();
        displayInfo.isConnected = (connectedUser != null);

        if (displayInfo.isConnected) {
            displayInfo.isFollowing = connectedUser.isFollowing(user);
            displayInfo.followButtonText = displayInfo.isFollowing ? "Ne plus suivre" : "Suivre";
            displayInfo.followButtonColor = displayInfo.isFollowing ?
                    new Color(255, 200, 200) :
                    new Color(200, 255, 200);
        }

        // Stocke ces informations dans le modèle
        mUserModel.updateUserDisplayInfo(user, displayInfo);
    }
}
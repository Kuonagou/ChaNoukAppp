package com.ubo.tp.message.core.user;

import com.ubo.tp.message.datamodel.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du modèle utilisateur.
 * Gère la liste des utilisateurs et notifie les observateurs des changements.
 */
public class UserModel implements IUserModel {

    /**
     * Liste de tous les utilisateurs.
     */
    protected List<User> allUsers;

    /**
     * Liste des utilisateurs recherchés.
     */
    protected List<User> searchUsers;

    /**
     * Map associant chaque utilisateur à ses informations d'affichage
     */
    protected Map<User, DisplayableUserInfo> displayInfoMap;

    /**
     * Liste des observateurs de la liste des utilisateurs.
     */
    protected List<IUserObserver> usersObservers;

    /**
     * Liste des observateurs de recherche d'utilisateurs.
     */
    protected List<IUserSearchObserver> usersSearchObservers;

    /**
     * Liste des observateurs des informations d'affichage des utilisateurs
     */
    protected List<IUserDisplayObserver> userDisplayObservers;

    /**
     * Constructeur.
     * Initialise les listes.
     */
    public UserModel() {
        this.allUsers = new ArrayList<>();
        this.searchUsers = new ArrayList<>();
        this.displayInfoMap = new HashMap<>();
        this.usersObservers = new ArrayList<>();
        this.usersSearchObservers = new ArrayList<>();
        this.userDisplayObservers = new ArrayList<>();
    }

    @Override
    public void addObserver(IUserObserver observer) {
        this.usersObservers.add(observer);
    }

    @Override
    public void removeObserver(IUserObserver observer) {
        this.usersObservers.remove(observer);
    }

    @Override
    public void addUserSearchObserver(IUserSearchObserver observer) {
        this.usersSearchObservers.add(observer);
    }

    @Override
    public void removeUserSearchObserver(IUserSearchObserver observer) {
        this.usersSearchObservers.remove(observer);
    }

    @Override
    public void addUserDisplayObserver(IUserDisplayObserver observer) {
        this.userDisplayObservers.add(observer);
    }

    @Override
    public void removeUserDisplayObserver(IUserDisplayObserver observer) {
        this.userDisplayObservers.remove(observer);
    }

    @Override
    public void makeNewSearch(List<User> usersSearched) {
        this.searchUsers = usersSearched;

        // Notifier tous les observateurs
        for(IUserSearchObserver observer : usersSearchObservers) {
            observer.notifyNewUserSearch(this.searchUsers);
        }
    }

    @Override
    public void listUserChanged(List<User> newUsersList) {
        this.allUsers = newUsersList;

        // Notifier tous les observateurs
        for(IUserObserver observer : usersObservers) {
            observer.notifyUserListChanged(this.allUsers);
        }
    }

    @Override
    public void updateUserDisplayInfo(User user, DisplayableUserInfo displayInfo) {
        // Stocker les informations d'affichage
        this.displayInfoMap.put(user, displayInfo);

        // Notifier les observateurs que les informations d'affichage ont été mises à jour
        // en transmettant directement les informations d'affichage
        for (IUserDisplayObserver observer : userDisplayObservers) {
            observer.notifyUserDisplayUpdated(user, displayInfo);
        }
    }
}
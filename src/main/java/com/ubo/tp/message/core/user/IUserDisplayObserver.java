package com.ubo.tp.message.core.user;

import com.ubo.tp.message.datamodel.User;

/**
 * Interface pour les observateurs des informations d'affichage des utilisateurs.
 */
public interface IUserDisplayObserver {
    /**
     * Notifie qu'un utilisateur a mis à jour ses informations d'affichage.
     *
     * @param user L'utilisateur dont les informations d'affichage ont été mises à jour
     * @param displayInfo Les informations d'affichage mises à jour
     */
    void notifyUserDisplayUpdated(User user, DisplayableUserInfo displayInfo);
}
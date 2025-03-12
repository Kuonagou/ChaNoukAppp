package com.ubo.tp.message.ihm.logout;

import com.ubo.tp.message.core.session.ISession;

/**
 * Contrôleur pour la gestion de la déconnexion.
 */
public class LogoutController {

    /**
     * Session de l'application.
     */
    protected final ISession mSession;

    /**
     * Constructeur.
     *
     * @param session Session active
     */
    public LogoutController(ISession session) {
        this.mSession = session;
    }

    /**
     * Déconnecte l'utilisateur actuellement connecté.
     *
     * @return true si un utilisateur était connecté et a été déconnecté, false sinon
     */
    public boolean logout() {
        if (mSession.getConnectedUser() != null) {
            mSession.disconnect();
            return true;
        }
        return false;
    }

    /**
     * Vérifie si un utilisateur est actuellement connecté.
     *
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isUserConnected() {
        return mSession.getConnectedUser() != null;
    }
}
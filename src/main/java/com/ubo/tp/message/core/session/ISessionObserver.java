package com.ubo.tp.message.core.session;

import com.ubo.tp.message.datamodel.User;

/**
 * Interface d'observation de la session.
 *
 * @author S.Lucas
 */
public interface ISessionObserver {

	/**
	 * Notification de connexion d'un utilisateur.
	 *
	 * @param connectedUser, utilisateur nouvellement connecté.
	 */
	default void notifyLogin(User connectedUser){};

	/**
	 * Notification de déconnexion.
	 */
	void notifyLogout();
}

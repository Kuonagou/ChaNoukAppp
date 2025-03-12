package com.ubo.tp.message.core.user;

import com.ubo.tp.message.datamodel.User;

import java.util.List;

/**
 * Interface représentant le modèle utilisateur.
 * Gère la liste des utilisateurs et les recherches.
 */
public interface IUserModel {

	/**
	 * Ajoute un observateur de la liste des utilisateurs.
	 *
	 * @param observer Observateur à ajouter
	 */
	void addObserver(IUserObserver observer);

	/**
	 * Retire un observateur de la liste des utilisateurs.
	 *
	 * @param observer Observateur à retirer
	 */
	void removeObserver(IUserObserver observer);

	/**
	 * Ajoute un observateur de recherche d'utilisateurs.
	 *
	 * @param observer Observateur à ajouter
	 */
	void addUserSearchObserver(IUserSearchObserver observer);

	/**
	 * Retire un observateur de recherche d'utilisateurs.
	 *
	 * @param observer Observateur à retirer
	 */
	void removeUserSearchObserver(IUserSearchObserver observer);

	/**
	 * Ajoute un observateur des informations d'affichage des utilisateurs.
	 *
	 * @param observer Observateur à ajouter
	 */
	void addUserDisplayObserver(IUserDisplayObserver observer);

	/**
	 * Retire un observateur des informations d'affichage des utilisateurs.
	 *
	 * @param observer Observateur à retirer
	 */
	void removeUserDisplayObserver(IUserDisplayObserver observer);

	/**
	 * Notifie le modèle d'une nouvelle recherche d'utilisateurs.
	 * Le modèle notifiera ensuite les observateurs de recherche.
	 *
	 * @param usersSearched Liste des utilisateurs trouvés lors de la recherche
	 */
	void makeNewSearch(List<User> usersSearched);

	/**
	 * Notifie le modèle d'un changement dans la liste des utilisateurs.
	 * Le modèle notifiera ensuite les observateurs de la liste.
	 *
	 * @param newUsersList Nouvelle liste d'utilisateurs
	 */
	void listUserChanged(List<User> newUsersList);

	/**
	 * Met à jour les informations d'affichage d'un utilisateur.
	 *
	 * @param user L'utilisateur dont les informations d'affichage sont mises à jour
	 * @param displayInfo Les nouvelles informations d'affichage
	 */
	void updateUserDisplayInfo(User user, DisplayableUserInfo displayInfo);
}
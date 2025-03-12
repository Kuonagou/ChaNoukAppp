package com.ubo.tp.message;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.ihm.AppController;

/**
 * Classe de lancement de l'application.
 *
 * @author S.Lucas
 */
public class MessageAppLauncher {
	/**
	 * Main pour lancer l'application.
	 *
	 */
	public static void main(String[] args) {
		IDatabase database = new Database();
		EntityManager entityManager = new EntityManager(database);

		// Création et initialisation du contrôleur
		AppController appController = new AppController(database, entityManager);
		appController.init();
	}
}
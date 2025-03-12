package com.ubo.tp.message.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Classe utilitaire de gestion du chargement et de la sauvegarde des fichier properties
 *
 * @author S.Lucas
 */
public class PropertiesManager {

	/**
	 * Chemin du fichier de propriétés
	 */
	private static final String PROPERTIES_FILE_NAME = "strings.properties";

	/**
	 * Map contenant les propriétés chargées
	 */
	private static final Map<String, String> propertiesMap = new HashMap<>();

	// Bloc statique pour charger les propriétés une seule fois au chargement de la classe
	static {
		loadPropertiesString();
	}

	/**
	 * Charge le fichier de propriétés une seule fois dans une Map.
	 */
	private static void loadPropertiesString() {
		Properties properties = new Properties();
		try (InputStream inputStream = PropertiesManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
			if (inputStream == null) {
				System.err.println("Fichier de propriétés non trouvé : " + PROPERTIES_FILE_NAME);
				// Ajouter quelques propriétés par défaut
				properties.setProperty("WELCOME", "Bienvenue");
				properties.setProperty("NEW_MESSAGE", "Nouveau message");
			} else {
				properties.load(inputStream);
			}

			// Charger toutes les propriétés dans la map
			for (String key : properties.stringPropertyNames()) {
				propertiesMap.put(key, properties.getProperty(key));
			}
		} catch (IOException e) {
			System.err.println("Erreur lors du chargement du fichier de propriétés : " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Chargement d'un fichier de propriétés
	 */
	public static Properties loadProperties(String propertiesFilePath) {
		Properties properties = new Properties();

		// Si le fichier de configuration existe
		File file = new File(propertiesFilePath);
		if (file.exists()) {
			try (FileInputStream in = new FileInputStream(file)) {
				properties.load(in);
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println("Impossible de charger le fichier de propriétés");
			}
		}
		return properties;
	}

	/**
	 * Ecriture du fichier de propriétés.
	 *
	 * @param properties         propriétés à enregistrer
	 * @param propertiesFilePath Chemin du fichier de propriété à écrire.
	 */
	public static void writeProperties(Properties properties, String propertiesFilePath) {
		if (properties != null) {
			try (FileOutputStream out = new FileOutputStream(propertiesFilePath)) {
				properties.store(out, "");
			} catch (Throwable t) {
				t.printStackTrace();
				System.err.println("Impossible d'enregistrer les propriétés");
			}
		}
	}

	/**
	 * Récupère une valeur de propriété en fonction de la clé.
	 * @param key Clé de la propriété
	 * @return Valeur associée ou un message d'erreur si la clé n'existe pas.
	 */
	public static String getString(String key) {
		return propertiesMap.getOrDefault(key, "Clé introuvable : " + key);
	}
}
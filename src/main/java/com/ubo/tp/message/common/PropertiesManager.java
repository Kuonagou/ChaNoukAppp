package com.ubo.tp.message.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Classe utilitaire de gestion du chargement et de la sauvegarde des fichier
 * properties
 *
 * @author S.Lucas
 */
public class PropertiesManager {

	/**
	 * Chargement d'un fichier de propriétés
	 */
	public static Properties loadProperties(String propertiesFilePath) {
		Properties properties = new Properties();

		// Si le fichier de configuration existe
		if (new File(propertiesFilePath).exists()) {
			try (FileInputStream in = new FileInputStream(propertiesFilePath)) {
				properties.load(in);
			} catch (Throwable t) {
				t.printStackTrace();
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

	private static final String PROPERTIES_FILE_PATH = "C:\\Users\\utilisateur\\Documents\\Fac\\M2\\IHM Lucas\\Jour 1\\src\\main\\resources\\strings.properties";
	private static final Map<String, String> propertiesMap = new HashMap<>();
	static {
		loadPropertiesString();
	}

	/**
	 * Charge le fichier de propriétés une seule fois dans une Map.
	 */
	private static void loadPropertiesString() {
		Properties properties = new Properties();
		try (FileInputStream in = new FileInputStream(PROPERTIES_FILE_PATH)) {
			properties.load(in);
			for (String key : properties.stringPropertyNames()) {
				propertiesMap.put(key, properties.getProperty(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Impossible de charger le fichier de propriétés.");
		}
	}

	/**
	 * Récupère une valeur de propriété en fonction de la clé.
	 * @param key Clé de la propriété
	 * @return Valeur associée ou un message d'erreur si la clé n'existe pas.
	 */
	public static String getString(String key) {
		return propertiesMap.getOrDefault(key, "Clé introuvable !");
	}
}

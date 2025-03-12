package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.session.ISession;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Contrôleur pour la gestion des messages.
 */
public class MessageController {
    /**
     * Longueur maximale d'un message.
     */
    public static final int MAX_MESSAGE_LENGTH = 200;

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
    public MessageController(ISession session, IDatabase database, EntityManager entityManager) {
        this.mSession = session;
        this.mDatabase = database;
        this.mEntityManager = entityManager;
    }

    /**
     * Crée et envoie un nouveau message.
     *
     * @param messageText Texte du message
     * @return true si l'envoi est réussi, false sinon
     */
    public void sendMessage(String messageText) {
        // Vérifier si un utilisateur est connecté
        User connectedUser = mSession.getConnectedUser();
        if (connectedUser != null) {
            // Vérifier la longueur du message
            if (messageText != null || !messageText.trim().isEmpty() || messageText.length() <= MAX_MESSAGE_LENGTH) {
                // Créer le message
                Message newMessage = new Message(connectedUser, messageText);

                // Sauvegarder le message dans un fichier
                mEntityManager.writeMessageFile(newMessage);
            }
        }
    }

    /**
     * Recherche des messages selon les critères spécifiés.
     *
     * @param searchQuery Termes de recherche
     * @return Ensemble des messages correspondant aux critères
     */
    public Set<Message> searchMessages(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return new HashSet<>(mDatabase.getMessages());
        }

        Set<Message> result = new HashSet<>();
        String query = searchQuery.trim();

        // Recherche par tag utilisateur (@)
        if (query.contains("@")) {
            String[] parts = query.split("@");
            if (parts.length > 1) {
                // Extraire le tag utilisateur (prendre le premier mot après @)
                String userTag = parts[1].split(" ")[0];

                // Chercher les messages avec ce tag utilisateur
                result.addAll(mDatabase.getMessagesWithUserTag(userTag));

                // Chercher également les messages émis par cet utilisateur
                for (User user : mDatabase.getUsers()) {
                    if (user.getUserTag().equals(userTag)) {
                        result.addAll(mDatabase.getUserMessages(user));
                        break;
                    }
                }
            }
        }
        // Recherche par hashtag (#)
        else if (query.contains("#")) {
            String[] parts = query.split("#");
            if (parts.length > 1) {
                // Extraire le hashtag (prendre le premier mot après #)
                String tag = parts[1].split(" ")[0];

                // Chercher les messages avec ce hashtag
                result.addAll(mDatabase.getMessagesWithTag(tag));
            }
        }
        // Recherche générale (union des deux critères précédents)
        else {
            // Recherche par utilisateur
            for (User user : mDatabase.getUsers()) {
                if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                        user.getUserTag().toLowerCase().contains(query.toLowerCase())) {
                    result.addAll(mDatabase.getUserMessages(user));
                    result.addAll(mDatabase.getMessagesWithUserTag(user.getUserTag()));
                }
            }

            // Recherche par contenu du message
            for (Message message : mDatabase.getMessages()) {
                if (message.getText().toLowerCase().contains(query.toLowerCase())) {
                    result.add(message);
                }
            }
        }

        return result;
    }

    /**
     * Obtient tous les messages disponibles.
     *
     * @return Ensemble des messages
     */
    public Set<Message> getAllMessages() {
        return mDatabase.getMessages();
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
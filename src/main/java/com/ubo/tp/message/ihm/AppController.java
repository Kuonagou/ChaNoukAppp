package com.ubo.tp.message.ihm;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.core.directory.IWatchableDirectory;
import com.ubo.tp.message.core.directory.WatchableDirectory;
import com.ubo.tp.message.core.message.MessageModel;
import com.ubo.tp.message.core.session.ISessionObserver;
import com.ubo.tp.message.core.session.Session;
import com.ubo.tp.message.core.user.IUserModel;
import com.ubo.tp.message.core.user.UserModel;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.adduser.InscriptionController;
import com.ubo.tp.message.ihm.adduser.InscriptionView;
import com.ubo.tp.message.ihm.directory.DirectoryView;
import com.ubo.tp.message.ihm.user.ListeUserView;
import com.ubo.tp.message.ihm.user.UserController;
import com.ubo.tp.message.ihm.login.LoginController;
import com.ubo.tp.message.ihm.login.LoginFormView;
import com.ubo.tp.message.ihm.logout.LogoutController;
import com.ubo.tp.message.ihm.menu.ConnectedMenuView;
import com.ubo.tp.message.ihm.menu.NonConnectedMenuView;
import com.ubo.tp.message.ihm.message.MessageController;
import com.ubo.tp.message.ihm.message.MessageSearchView;
import com.ubo.tp.message.ihm.message.MessageSendView;
import com.ubo.tp.message.ihm.message.NotifyView;
import com.ubo.tp.message.ihm.profil.UserProfilController;
import com.ubo.tp.message.ihm.profil.UserProfileView;
import com.ubo.tp.message.ihm.userHome.UserHomeView;
import com.ubo.tp.message.ihm.user.UserSearchView;

import javax.swing.*;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe principale l'application.
 *
 * @author S.Lucas
 */
public class AppController implements ISessionObserver, Listener, IDatabaseObserver {

    // VIEW
    protected AProposView aProposView;
    protected LoginFormView loginView;
    protected UserHomeView userHomeView;
    protected InscriptionView inscriptionView;
    protected DirectoryView directoryView;
    protected ListeUserView listeUserView;
    protected UserSearchView userSearchView;
    protected UserProfileView userProfileView;
    protected ConnectedMenuView connectedMenuView;
    protected NonConnectedMenuView nonConnectedMenuView;
    protected MessageSendView messageSendView;
    protected MessageSearchView messageSearchView;
    protected NotifyView notifyView;


    // CONTROLLER
    protected LoginController loginController;
    protected InscriptionController inscriptionController;
    protected UserProfilController userProfileController;
    protected LogoutController logoutController;
    protected MessageController messageController;
    protected UserController userController;

    //MODEL
    protected MessageModel messageModelObserver;
    protected IUserModel userModel;

    /*
     * Base de données.
     */
    protected IDatabase mDatabase;

    /**
     * Gestionnaire des entités contenu de la base de données.
     */
    protected EntityManager mEntityManager;

    /**
     * Vue principale de l'application.
     */
    protected AppMainView mMainView;

    /**
     * Classe de surveillance de répertoire
     */
    protected IWatchableDirectory mWatchableDirectory;

    /**
     * Répertoire d'échange de l'application.
     */
    protected String mExchangeDirectoryPath;

    protected Session session;


    /**
     * Constructeur.
     *
     * @param entityManager
     * @param database
     */
    public AppController(IDatabase database, EntityManager entityManager) {
        this.mDatabase = database;
        this.mEntityManager = entityManager;
        this.mMainView = new AppMainView();
        this.session = new Session();
        this.session.addObserver(this);
        this.logoutController = new LogoutController(this.session);

        // Initialisation des contrôleurs de messages
        this.messageController = new MessageController(this.session, this.mDatabase, this.mEntityManager);
        this.messageSearchView = new MessageSearchView(this.messageController);
        this.messageSendView = new MessageSendView(this.messageController);
        this.notifyView = new NotifyView(this.messageController);
        this.messageModelObserver = new MessageModel(this.messageController);
        this.messageModelObserver.addObserver(this.notifyView);
        this.messageModelObserver.addObserver(this.messageSearchView);
        this.mDatabase.addObserver(this);

        // Initialisation du modèle et du contrôleur d'utilisateurs
        this.userModel = new UserModel();
        this.userController = new UserController(this.session, this.mDatabase, this.mEntityManager, this.userModel);
    }

    /**
     * Initialisation de l'application.
     */
    public void init() {
        // Init du look and feel de l'application
        this.initLookAndFeel();

        // Initialisation de l'IHM
        this.initGui();

        // Initialisation du répertoire d'échange
        this.initDirectory();
    }

    /**
     * Initialisation du look and feel de l'application.
     */
    protected void initLookAndFeel() {
    }

    /**
     * Initialisation de l'interface graphique.
     */
    protected void initGui() {
        this.mMainView.showGUI();

        // Initialisation des vues de menu
        this.connectedMenuView = new ConnectedMenuView(this);
        this.nonConnectedMenuView = new NonConnectedMenuView(this);

        // Par défaut, on affiche le menu non connecté
        this.mMainView.setMenu(this.nonConnectedMenuView.getMenuBar());

        this.aProposView = new AProposView();
        this.showConnection();
    }

    /**
     * Initialisation du répertoire d'échange (depuis la conf ou depuis un file
     * chooser). <br/>
     * <b>Le chemin doit obligatoirement avoir été saisi et être valide avant de
     * pouvoir utiliser l'application</b>
     */

    protected void initDirectory() {
        String savedDirectory = loadExchangeDirectoryFromConfig();
        mMainView.showGUI();
        if (savedDirectory.isEmpty() || !isValideExchangeDirectory(new File(savedDirectory))) {
            // Ouvre la fenêtre pour choisir un répertoire si la config est vide ou invalide
            showGetDirectory();
        }else{
            initReadDirectory(savedDirectory);
        }
    }

    private void showGetDirectory() {
        directoryView = new DirectoryView(this);
        mMainView.addToFrame(directoryView);
    }

    /**
     * Indique si le fichier donné est valide pour servir de répertoire d'échange
     *
     * @param directory , Répertoire à tester.
     */
    protected boolean isValideExchangeDirectory(File directory) {
        // Valide si répertoire disponible en lecture et écriture
        return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
                && directory.canWrite();
    }

    /**
     * Initialisation du répertoire d'échange.
     *
     * @param directoryPath
     */
    protected void initReadDirectory(String directoryPath) {
        mExchangeDirectoryPath = directoryPath;
        mWatchableDirectory = new WatchableDirectory(directoryPath);
        mEntityManager.setExchangeDirectory(directoryPath);

        mWatchableDirectory.initWatching();
        mWatchableDirectory.addObserver(mEntityManager);
    }

    /**
     * Charge le répertoire d'échange depuis le fichier de configuration.
     * Le fichier est recherché d'abord dans le répertoire courant, puis dans
     * le répertoire utilisateur.
     */
    protected String loadExchangeDirectoryFromConfig() {
        Properties properties = new Properties();

        // Liste des chemins où chercher le fichier de configuration
        String[] configPaths = {
                "./configuration.properties",
                System.getProperty("user.home") + "/appconfig/configuration.properties"
        };

        for (String path : configPaths) {
            File configFile = new File(path);
            if (configFile.exists() && configFile.canRead()) {
                try (FileInputStream inputStream = new FileInputStream(configFile)) {
                    properties.load(inputStream);
                    String directory = properties.getProperty("EXCHANGE_DIRECTORY", "");
                    if (!directory.isEmpty()) {
                        return directory;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Si aucun fichier n'est trouvé ou si le répertoire n'est pas défini
        return "";
    }

    /**
     * Sauvegarde le répertoire d'échange dans le fichier de configuration.
     * Si le fichier n'existe pas, il sera créé dans le répertoire utilisateur.
     *
     * @param directoryPath le chemin du répertoire à sauvegarder
     */
    protected void saveExchangeDirectoryToConfig(String directoryPath) {
        Properties properties = new Properties();

        // Définir le chemin du fichier de configuration dans le répertoire utilisateur
        String userConfigPath = System.getProperty("user.home") + "/appconfig";
        File configDir = new File(userConfigPath);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File configFile = new File(userConfigPath + "/configuration.properties");

        // Charger les propriétés existantes si le fichier existe
        if (configFile.exists() && configFile.canRead()) {
            try (FileInputStream inputStream = new FileInputStream(configFile)) {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mettre à jour la propriété et sauvegarder
        properties.setProperty("EXCHANGE_DIRECTORY", directoryPath);

        try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
            properties.store(outputStream, "Mise à jour du répertoire d'échange");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQuit() {
        System.exit(0);
    }

    @Override
    public void afficheUtilisateur() {
        // Création de la vue
        listeUserView = new ListeUserView(this.userController);

        // Centralisation de l'enregistrement des observateurs
        this.userModel.addObserver(listeUserView);
        this.userModel.addUserDisplayObserver(listeUserView);

        // Affichage de la vue
        mMainView.addToFrame(listeUserView);

        // Demander au contrôleur de charger les utilisateurs
        this.userController.getAllUsers();
    }

    @Override
    public void rechercheUtilisateur() {
        // Création de la vue
        userSearchView = new UserSearchView(this.userController);

        // Centralisation de l'enregistrement des observateurs
        this.userModel.addUserSearchObserver(userSearchView);
        this.userModel.addUserDisplayObserver(userSearchView);

        // Affichage de la vue
        mMainView.addToFrame(userSearchView);

        // S'assurer que la recherche initiale s'exécute après l'affichage
        SwingUtilities.invokeLater(() -> {
            this.userController.searchUsers("");
        });
    }

    @Override
    public void aPropos() {
        aProposView = new AProposView();
        mMainView.addToFrame(aProposView);
    }

    @Override
    public void showConnection() {
        this.loginController = new LoginController(this.mDatabase, this.session, this.mEntityManager);
        loginView = new LoginFormView(loginController);
        mMainView.addToFrame(loginView);
    }

    @Override
    public void updatePath() {
        saveExchangeDirectoryToConfig(directoryView.getDirectoryPath());
        initDirectory();
        showConnection();
    }

    @Override
    public void showInscription() {
        this.inscriptionController = new InscriptionController(this.mDatabase, this.mEntityManager);
        this.inscriptionView = new InscriptionView(this.inscriptionController);
        mMainView.addToFrame(inscriptionView);
    }

    @Override
    public void showUserProfile() {
        this.userProfileController = new UserProfilController(this.session, this.mDatabase, this.mEntityManager);
        this.userProfileView = new UserProfileView(this.userProfileController, this);
        mMainView.addToFrame(userProfileView);
    }

    @Override
    public void showUserHome() {
        if (userHomeView == null) {
            userHomeView = new UserHomeView(this.logoutController, this);
            userHomeView.init();
        }
        mMainView.addToFrame(userHomeView);
    }

    @Override
    public void notifyLogin(User connectedUser) {
        // Affichage du menu connecté
        this.mMainView.setMenu(this.connectedMenuView.getMenuBar());

        // Affichage de la page d'accueil
        userHomeView = new UserHomeView(this.logoutController, this);
        userHomeView.init();
        mMainView.addToFrame(userHomeView);
    }

    @Override
    public void performLogout() {
        // Nettoyer les ressources des vues qui pourraient être encore actives
        if (listeUserView != null) {
            this.userModel.removeObserver(listeUserView);
            this.userModel.removeUserDisplayObserver(listeUserView);
            listeUserView = null;
        }

        if (userSearchView != null) {
            this.userModel.removeUserSearchObserver(userSearchView);
            this.userModel.removeUserDisplayObserver(userSearchView);
            userSearchView = null;
        }

        // Effectuer la déconnexion
        if (this.logoutController.isUserConnected()) {
            this.logoutController.logout();
            showConnection();
        } else {
            showConnection();
        }
    }

    /**
     * Affiche la vue d'envoi de message.
     */
    public void showMessageSend() {
        if (this.session.getConnectedUser() != null) {
            this.messageSendView = new MessageSendView(this.messageController);
            mMainView.addToFrame(messageSendView);
        } else {
            JOptionPane.showMessageDialog(mMainView.getFrame(),
                    PropertiesManager.getString("SECURITE_MESSAGE"),
                    PropertiesManager.getString("ERREUR"), JOptionPane.ERROR_MESSAGE);
            showConnection();
        }
    }

    /**
     * Affiche la vue de recherche de messages.
     */
    public void showMessageSearch() {
        mMainView.addToFrame(messageSearchView);
    }

    @Override
    public void notifyLogout() {
        this.mMainView.setMenu(this.nonConnectedMenuView.getMenuBar());
        showConnection();
    }

    @Override
    public void notifyMessageAdded(com.ubo.tp.message.datamodel.Message addedMessage) {
        this.messageModelObserver.notifyAjoutMessage(addedMessage);
    }

    @Override
    public void notifyMessageDeleted(com.ubo.tp.message.datamodel.Message deletedMessage) {
        // Non implémenté
    }

    @Override
    public void notifyMessageModified(com.ubo.tp.message.datamodel.Message modifiedMessage) {
        // Non implémenté
    }

    @Override
    public void notifyUserAdded(User addedUser) {
        // Non implémenté
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
        // Non implémenté
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
        // Non implémenté
    }
}

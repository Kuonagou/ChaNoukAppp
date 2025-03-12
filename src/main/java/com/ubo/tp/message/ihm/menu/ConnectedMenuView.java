package com.ubo.tp.message.ihm.menu;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.ihm.Listener;

import javax.swing.*;
import java.util.Objects;

/**
 * Vue du menu pour un utilisateur connecté.
 */
public class ConnectedMenuView {

    private final Listener mListener;

    /**
     * Constructeur.
     *
     * @param listener Listener pour les interactions
     */
    public ConnectedMenuView(Listener listener) {
        this.mListener = listener;
    }

    /**
     * Initialise et retourne la barre de menu pour un utilisateur connecté.
     *
     * @return La barre de menu
     */
    public JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu principal (toujours présent)
        JMenu menuFile = new JMenu(PropertiesManager.getString("MENU"));
        JMenuItem itemQuitter = new JMenuItem(PropertiesManager.getString("QUITTER"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/exitIcon_20.png"))));
        JMenuItem itemAPropos = new JMenuItem(PropertiesManager.getString("A_PROPOS"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/editIcon_20.png"))));

        itemQuitter.addActionListener(e -> mListener.onQuit());
        itemAPropos.addActionListener(e -> mListener.aPropos());

        menuFile.add(itemQuitter);
        menuFile.add(itemAPropos);
        menuBar.add(menuFile);

        // Menu Mon Compte (spécifique aux utilisateurs connectés)
        JMenu menuProfile = new JMenu(PropertiesManager.getString("MON_COMPTE"));
        JMenuItem itemProfile = new JMenuItem(PropertiesManager.getString("MON_PROFIL"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/tete.png"))));
        JMenuItem itemLogout = new JMenuItem(PropertiesManager.getString("DECONNEXION"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/exitIcon_20.png"))));

        itemProfile.addActionListener(e -> mListener.showUserProfile());
        itemLogout.addActionListener(e -> mListener.performLogout());

        menuProfile.add(itemProfile);
        menuProfile.add(itemLogout);
        menuBar.add(menuProfile);

        // Menu Utilisateurs (spécifique aux utilisateurs connectés)
        JMenu menuUsers = new JMenu(PropertiesManager.getString("UTILISATEURS"));
        JMenuItem itemListe = new JMenuItem(PropertiesManager.getString("LISTE_USER"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/liste.png"))));
        JMenuItem itemRecherche = new JMenuItem(PropertiesManager.getString("RECHERCHE_USER"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/tete.png"))));

        itemListe.addActionListener(e -> mListener.afficheUtilisateur());
        itemRecherche.addActionListener(e -> mListener.rechercheUtilisateur());

        menuUsers.add(itemListe);
        menuUsers.add(itemRecherche);
        menuBar.add(menuUsers);

        // Menu Messages (spécifique aux utilisateurs connectés)
        JMenu menuMessages = new JMenu(PropertiesManager.getString("MESSAGES"));
        JMenuItem itemSendMessage = new JMenuItem(PropertiesManager.getString("NEW_MESSAGE"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/message.png"))));
        JMenuItem itemSearchMessages = new JMenuItem(PropertiesManager.getString("RECHERCHE_MESSAGE"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/conv.png"))));

        itemSendMessage.addActionListener(e -> mListener.showMessageSend());
        itemSearchMessages.addActionListener(e -> mListener.showMessageSearch());

        menuMessages.add(itemSendMessage);
        menuMessages.add(itemSearchMessages);
        menuBar.add(menuMessages);

        return menuBar;
    }
}
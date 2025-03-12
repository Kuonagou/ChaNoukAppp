package com.ubo.tp.message.ihm.menu;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.ihm.Listener;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Vue du menu pour un utilisateur non connecté.
 */
public class NonConnectedMenuView {

    private final Listener mListener;

    /**
     * Constructeur.
     *
     * @param listener Listener pour les interactions
     */
    public NonConnectedMenuView(Listener listener) {
        this.mListener = listener;
    }

    /**
     * Initialise et retourne la barre de menu pour un utilisateur non connecté.
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

        // Menu Compte (spécifique aux utilisateurs non connectés)
        JMenu menuConnexion = new JMenu(PropertiesManager.getString("COMPTE"));
        JMenuItem itemConnexion = new JMenuItem(PropertiesManager.getString("CONNEXION"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/liste.png"))));
        JMenuItem itemCreation = new JMenuItem(PropertiesManager.getString("INSCRIPTION"), new ImageIcon(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/liste.png"))));

        itemConnexion.addActionListener(e -> mListener.showConnection());
        itemCreation.addActionListener(e -> mListener.showInscription());

        menuConnexion.add(itemConnexion);
        menuConnexion.add(itemCreation);
        menuBar.add(menuConnexion);

        return menuBar;
    }
}
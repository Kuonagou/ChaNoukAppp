package com.ubo.tp.message.ihm;

public interface Listener {
    void onQuit();
    void afficheUtilisateur();
    void rechercheUtilisateur();
    void aPropos();
    void showConnection();
    void updatePath();
    void showInscription();
    void showUserProfile();
    void showUserHome();
    void performLogout();
    void showMessageSend();
    void showMessageSearch();
}
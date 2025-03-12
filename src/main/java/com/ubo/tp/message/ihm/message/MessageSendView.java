package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.common.PropertiesManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Vue pour l'envoi de messages.
 */
public class MessageSendView extends JPanel {

    /**
     * Contrôleur de messages.
     */
    protected final MessageController mMessageController;

    /**
     * Zone de texte pour le message.
     */
    protected JTextArea mMessageTextArea;

    /**
     * Label pour le compteur de caractères.
     */
    protected JLabel mCharCountLabel;

    /**
     * Bouton pour envoyer le message.
     */
    protected JButton mSendButton;

    /**
     * Constructeur.
     *
     * @param messageController Contrôleur de messages
     */
    public MessageSendView(MessageController messageController) {
        this.mMessageController = messageController;
        initView();
    }

    /**
     * Initialise la vue.
     */
    private void initView() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.PINK);

        // Titre
        JLabel titleLabel = new JLabel(PropertiesManager.getString("NEW_MESSAGE"), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(titleLabel, BorderLayout.NORTH);

        // Zone de texte du message
        mMessageTextArea = new JTextArea(5, 30);
        mMessageTextArea.setLineWrap(true);
        mMessageTextArea.setWrapStyleWord(true);
        mMessageTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Ajouter un écouteur pour compter les caractères
        mMessageTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCharCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCharCount();
            }
        });

        JScrollPane scrollPane = new JScrollPane(mMessageTextArea);
        this.add(scrollPane, BorderLayout.CENTER);

        // Panel du bas (compteur + bouton)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.PINK);

        // Compteur de caractères
        mCharCountLabel = new JLabel("0/" + MessageController.MAX_MESSAGE_LENGTH);
        bottomPanel.add(mCharCountLabel, BorderLayout.WEST);

        // Bouton d'envoi
        mSendButton = new JButton(PropertiesManager.getString("ENVOYER"));
        mSendButton.setEnabled(false);
        mSendButton.addActionListener(e -> sendMessage());
        bottomPanel.add(mSendButton, BorderLayout.EAST);

        this.add(bottomPanel, BorderLayout.SOUTH);

        // Vérifier si un utilisateur est connecté
        if (!mMessageController.isUserConnected()) {
            mMessageTextArea.setEnabled(false);
            mMessageTextArea.setText(PropertiesManager.getString("SECURITE_MESSAGE"));
            mSendButton.setEnabled(false);
        }
    }

    /**
     * Met à jour le compteur de caractères et active/désactive le bouton d'envoi.
     */
    private void updateCharCount() {
        int charCount = mMessageTextArea.getText().length();
        mCharCountLabel.setText(charCount + "/" + MessageController.MAX_MESSAGE_LENGTH);

        // Changer la couleur si la limite est dépassée
        if (charCount > MessageController.MAX_MESSAGE_LENGTH) {
            mCharCountLabel.setForeground(Color.RED);
            mSendButton.setEnabled(false);
        } else {
            mCharCountLabel.setForeground(Color.BLACK);
            mSendButton.setEnabled(charCount > 0 && mMessageController.isUserConnected());
        }
    }

    /**
     * Envoie le message.
     */
    private void sendMessage() {
        String messageText = mMessageTextArea.getText();

        mMessageController.sendMessage(messageText);

        // Réinitialiser la zone de texte
        mMessageTextArea.setText("");
        updateCharCount();
    }

}
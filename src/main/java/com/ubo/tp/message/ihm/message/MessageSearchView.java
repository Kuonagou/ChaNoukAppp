package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.core.message.IRefreshSearch;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Vue pour la recherche et l'affichage des messages.
 */
public class MessageSearchView extends JPanel implements IRefreshSearch {

    /**
     * Contrôleur de messages.
     */
    protected final MessageController mMessageController;

    /**
     * Champ de recherche.
     */
    protected JTextField mSearchField;

    /**
     * Panel contenant les résultats de recherche.
     */
    protected JPanel mResultPanel;

    /**
     * Format de date pour l'affichage des messages.
     */
    protected SimpleDateFormat mDateFormat;

    /**
     * Constructeur.
     *
     * @param messageController Contrôleur de messages
     */
    public MessageSearchView(MessageController messageController) {
        this.mMessageController = messageController;
        this.mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
        JLabel titleLabel = new JLabel(PropertiesManager.getString("RECHERCHE_MESSAGE"), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.add(titleLabel, BorderLayout.NORTH);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.PINK);
        searchPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        mSearchField = new JTextField();
        mSearchField.setToolTipText(PropertiesManager.getString("TEXTE_RECHERCHE"));
        searchPanel.add(mSearchField, BorderLayout.CENTER);

        JButton searchButton = new JButton(PropertiesManager.getString("RECHERCHE_MESSAGE"));
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton, BorderLayout.EAST);

        this.add(searchPanel, BorderLayout.NORTH);

        // Panel de résultats
        mResultPanel = new JPanel();
        mResultPanel.setLayout(new BoxLayout(mResultPanel, BoxLayout.Y_AXIS));
        mResultPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(mResultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        this.add(scrollPane, BorderLayout.CENTER);

        // Charger tous les messages au démarrage
        displayMessages(mMessageController.getAllMessages());
    }

    /**
     * Effectue une recherche de messages.
     */
    private void performSearch() {
        String searchQuery = mSearchField.getText();
        Set<Message> searchResults = mMessageController.searchMessages(searchQuery);
        displayMessages(searchResults);
    }

    /**
     * Affiche les messages dans le panel de résultats.
     *
     * @param messages Ensemble de messages à afficher
     */
    private void displayMessages(Set<Message> messages) {
        // Vider le panel de résultats
        mResultPanel.removeAll();

        if (messages.isEmpty()) {
            JLabel noResultLabel = new JLabel(PropertiesManager.getString("AUCUN_MESS"), JLabel.CENTER);
            noResultLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            mResultPanel.add(noResultLabel);
        } else {
            // Trier les messages par date (du plus récent au plus ancien)
            Message[] sortedMessages = messages.toArray(new Message[0]);
            java.util.Arrays.sort(sortedMessages, (m1, m2) -> Long.compare(m2.getEmissionDate(), m1.getEmissionDate()));

            for (Message message : sortedMessages) {
                mResultPanel.add(createMessagePanel(message));
                mResultPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement
            }
        }

        mResultPanel.revalidate();
        mResultPanel.repaint();
    }

    /**
     * Crée un panel pour afficher un message.
     *
     * @param message Message à afficher
     * @return Panel contenant le message formaté
     */
    private JPanel createMessagePanel(Message message) {
        JPanel messagePanel = new JPanel(new BorderLayout(10, 5));
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // En-tête du message (auteur + date)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        User sender = message.getSender();
        JLabel senderLabel = new JLabel(sender.getName() + " (@" + sender.getUserTag() + ")");
        senderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(senderLabel, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(mDateFormat.format(new Date(message.getEmissionDate())));
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        headerPanel.add(dateLabel, BorderLayout.EAST);

        messagePanel.add(headerPanel, BorderLayout.NORTH);

        // Contenu du message
        JTextArea textArea = new JTextArea(message.getText());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(null);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        messagePanel.add(textArea, BorderLayout.CENTER);

        // Afficher les tags s'il y en a
        Set<String> tags = message.getTags();
        Set<String> userTags = message.getUserTags();

        if (!tags.isEmpty() || !userTags.isEmpty()) {
            JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tagsPanel.setBackground(Color.WHITE);

            for (String tag : tags) {
                JLabel tagLabel = new JLabel("#" + tag);
                tagLabel.setFont(new Font("Arial", Font.BOLD, 12));
                tagLabel.setForeground(Color.BLUE);
                tagsPanel.add(tagLabel);
            }

            for (String userTag : userTags) {
                JLabel userTagLabel = new JLabel("@" + userTag);
                userTagLabel.setFont(new Font("Arial", Font.BOLD, 12));
                userTagLabel.setForeground(Color.RED);
                tagsPanel.add(userTagLabel);
            }

            messagePanel.add(tagsPanel, BorderLayout.SOUTH);
        }

        return messagePanel;
    }


    @Override
    public void refreshSearchPanel() {
        performSearch();
    }
}
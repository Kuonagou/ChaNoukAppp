package com.ubo.tp.message.ihm.directory;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.ihm.Listener;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DirectoryView extends JPanel {
     protected JTextField directoryPathField;
     protected File selectedDirectory;
     protected Listener listener;

    public DirectoryView(Listener listener) {
        this.listener = listener;
        initPanel();

    }

    private void initPanel() {
        this.setBackground(Color.PINK);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔹 Label
        JLabel directoryLabel = new JLabel(PropertiesManager.getString("REPERTOIRE_ECHANGE"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(directoryLabel, gbc);

        // 📂 Champ de texte (chemin)
        directoryPathField = new JTextField(30);
        directoryPathField.setEditable(false);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        this.add(directoryPathField, gbc);

        // 🔍 Bouton "Parcourir..."
        JButton browseButton = new JButton(PropertiesManager.getString("PARCOURIR"));
        gbc.gridx = 2;
        gbc.weightx = 0;
        this.add(browseButton, gbc);

        // ✅ Bouton "Valider"
        JButton validateButton = new JButton(PropertiesManager.getString("VALIDER"));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(validateButton, gbc);

        // Actions
        browseButton.addActionListener(e -> selectDirectory());
        validateButton.addActionListener(e -> validateSelection());
    }

    private void selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(PropertiesManager.getString("SELECTION_REP"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = fileChooser.getSelectedFile();
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void validateSelection() {
        if (selectedDirectory != null) {
            JOptionPane.showMessageDialog(this, PropertiesManager.getString("REP_SELECTIONNE") + selectedDirectory.getAbsolutePath());
            listener.updatePath();
        }
    }

    public String getDirectoryPath(){
        return directoryPathField.getText();
    }
}

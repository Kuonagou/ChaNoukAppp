package com.ubo.tp.message.ihm;

import com.ubo.tp.message.common.PropertiesManager;

import javax.swing.*;
import java.awt.*;

public class AProposView  extends JPanel{

    public AProposView() {
        aProposInit();
    }

    public void aProposInit(){
        this.setBorder(BorderFactory.createTitledBorder(PropertiesManager.getString("A_PROPOS")));
        this.setBackground(Color.PINK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextArea jTextArea = new JTextArea(5, 30);
        jTextArea.setText(PropertiesManager.getString("DESCRIPTION"));
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        jTextArea.setEditable(false);
        jTextArea.setBackground(Color.PINK);
        ImageIcon icon = new ImageIcon("src/main/resources/images/about.png");
        JLabel iconLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

        this.add(iconLabel, gbc);
        gbc.gridy++;
        this.add(jTextArea, gbc);
    }
}

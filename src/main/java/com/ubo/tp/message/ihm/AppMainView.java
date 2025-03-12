package com.ubo.tp.message.ihm;


import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AppMainView {
    private JFrame mFrame;
    public AppMainView() {
    }

    void initGUI() {
        mFrame = new JFrame(PropertiesManager.getString("WELCOME"));
        mFrame.getContentPane().setBackground(Color.PINK);
        Image iconchat = Toolkit.getDefaultToolkit().getImage(Objects.requireNonNull(PropertiesManager.class.getClassLoader().getResource("images/chat.png")));
        mFrame.setIconImage(iconchat);
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.PINK);
        mFrame.setSize(500, 400);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setVisible(true);
    }
    public void showGUI() {
        if (mFrame == null) {
            this.initGUI();
        }
        SwingUtilities.invokeLater(() -> mFrame.setVisible(true));
    }

    public void addToFrame(JPanel monComponent){
        mFrame.getContentPane().removeAll();
        mFrame.getContentPane().add(monComponent);
        mFrame.revalidate();
        mFrame.repaint();
    }
    public void setMenu(JMenuBar monComponent){
        mFrame.setJMenuBar(monComponent);
    }

    public JFrame getFrame() {
        return mFrame;
    }


    public void showUserAddedDialog(User user) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    mFrame,
                    "L'utilisateur " + user.getName() + " a été ajouté avec succes.",
                    "Ajout d'un nouvel utilisateur",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

}
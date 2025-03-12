package com.ubo.tp.message.ihm.message;

import com.ubo.tp.message.common.PropertiesManager;
import com.ubo.tp.message.core.message.INotifyObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import javax.swing.*;

public class NotifyView extends JPanel implements INotifyObserver {

    protected MessageController messageController;

    public NotifyView(MessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    public void notifyNewMessage(Message message) {
        User connectedUser = this.messageController.getConnectedUser();
        if (connectedUser != null && connectedUser.getFollows().contains(message.getSender().getUserTag())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bonjour "+connectedUser.getUserTag()+"! "+PropertiesManager.getString("NEW_MESSAGE_FROM") + message.getSender().getName() + " (@" + message.getSender().getUserTag() + ") :\n" + message.getText(),
                    PropertiesManager.getString("NEW_MESSAGE"),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}

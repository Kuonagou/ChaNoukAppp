package com.ubo.tp.message.core.database;

import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

public class DatabaseObserver implements IDatabaseObserver{
    @Override
    public void notifyMessageAdded(Message addedMessage) {
        System.out.println("Ajout d'un message : "+addedMessage.getText()+" par "+addedMessage.getSender());
    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {
        System.out.println("Suppression d'un message : "+deletedMessage.getText()+" par "+deletedMessage.getSender());
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {
        System.out.println("Modification d'un message : "+modifiedMessage.getText()+" par "+modifiedMessage.getSender());
    }

    @Override
    public void notifyUserAdded(User addedUser) {
        System.out.println("Ajout d'un utilisateur : "+addedUser.getName()+" "+addedUser.getUserTag());
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
        System.out.println("Suppression d'un utilisateur : "+deletedUser.getName()+" "+deletedUser.getUserTag());
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
        System.out.println("Modification d'un utilisateur : "+modifiedUser.getName()+" "+modifiedUser.getUserTag());
    }
}

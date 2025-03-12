package com.ubo.tp.message.core.message;


import com.ubo.tp.message.ihm.message.MessageController;

import java.util.ArrayList;
import java.util.List;

public class MessageModel implements IMessage {

    protected MessageController messageController;
    protected final List<INotifyObserver> mObservers = new ArrayList<>();
    protected final List<IRefreshSearch> mOberversR = new ArrayList<>();
    public MessageModel(MessageController messageController) {
        this.messageController=messageController;
    }

    @Override
    public void addObserver(INotifyObserver observer) {
        this.mObservers.add(observer);
    }

    @Override
    public void removeObserver(INotifyObserver observer) {
        this.mObservers.remove(observer);
    }

    @Override
    public void addObserver(IRefreshSearch observer) {
        this.mOberversR.add(observer);
    }

    @Override
    public void removeObserver(IRefreshSearch observer) {
        this.mOberversR.remove(observer);
    }

    public void notifyAjoutMessage(com.ubo.tp.message.datamodel.Message messageToAdd){
        for (INotifyObserver observer : mObservers) {
            observer.notifyNewMessage(messageToAdd);
        }
        for(IRefreshSearch observer : mOberversR){
            observer.refreshSearchPanel();
        }
    }


}

package com.ubo.tp.message.core.message;

import com.ubo.tp.message.datamodel.Message;

public interface INotifyObserver {
    /**
     * Méthode appelée lorsqu'un nouveau message est reçu.
     *
     * @param message Le nouveau message
     */
    void notifyNewMessage(Message message);
}

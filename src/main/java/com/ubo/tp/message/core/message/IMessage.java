package com.ubo.tp.message.core.message;

/**
 * Interface pour les observateurs de messages.
 */
public interface IMessage {

    void addObserver(INotifyObserver observer);

    void removeObserver(INotifyObserver observer);

    void addObserver(IRefreshSearch observer);

    void removeObserver(IRefreshSearch observer);

}
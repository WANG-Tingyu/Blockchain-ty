package com.comp4137.blockchain.listener;

import com.comp4137.blockchain.event.NewTxEvent;

import java.util.EventListener;

public interface ITxListener extends EventListener {
    public void newTxEvent(NewTxEvent event);
}

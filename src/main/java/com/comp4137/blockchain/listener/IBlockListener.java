package com.comp4137.blockchain.listener;

import com.comp4137.blockchain.event.NewBlockEvent;
import com.comp4137.blockchain.event.NewTxEvent;

import java.util.EventListener;

public interface IBlockListener extends EventListener {
    public void newBlockEvent(NewBlockEvent event);
}

package com.comp4137.blockchain.listener;

import com.comp4137.blockchain.event.NewTxEvent;

public class NewTxListener implements ITxListener {
    public void newTxEvent(NewTxEvent event) {
        //验证收到的新Tx
        if(event.getId() != null){
            System.out.println("当前Tx id： "+event.getId());
            //如果验证成功，加入Tx pool
        }
    }
}

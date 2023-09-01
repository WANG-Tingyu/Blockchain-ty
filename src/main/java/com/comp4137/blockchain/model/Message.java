package com.comp4137.blockchain.model;

import lombok.Data;


import java.io.Serializable;
@Data
public class Message implements Serializable {
    String req;
    Transaction tx;
    Block block;

    int type; //1: str, 2: tx, 3: block

    public Message(String req, int type) {
        if(type == 1) {
            this.req = req;
            this.type = 1;
        }
    }

    public Message(Transaction tx, int type) {
        if(type == 2) {
            this.tx = tx;
            this.type = 2;
        }
    }

    public Message(Block block, int type) {
        if(type == 3){
            this.block = block;
            this.type = 3;
        }
    }


}

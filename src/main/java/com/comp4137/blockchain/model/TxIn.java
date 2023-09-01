package com.comp4137.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString

public class TxIn implements Serializable {
    public String txOutId;
    public int txOutIndex;
    public String signature;

    public TxIn(TxIn txIn) {
        this.txOutId = txIn.txOutId;
        this.txOutIndex = txIn.txOutIndex;
        this.signature = txIn.signature;
    }

    public String oIdAndOIdx(){
        String out = this.txOutId + this.txOutIndex;
        return out;
    }

}

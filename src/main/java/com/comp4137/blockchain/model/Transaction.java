package com.comp4137.blockchain.model;

import com.comp4137.blockchain.utils.HashUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class Transaction implements Serializable {
    public String id;
    public TxIn[] txIns;
    public TxOut[] txOuts;

    public Transaction(TxIn[] txIns, TxOut[] txOuts) {
        this.txIns = txIns;
        this.txOuts = txOuts;
        //this.id = getTxId(this);
        this.id = setTxId();
    }

    public String setTxId(){
        String txInContent = "";
        String txOutContent = "";

        for (TxIn txIn : this.txIns) {
            txInContent = txInContent + txIn.oIdAndOIdx();
        }

        for (TxOut txOut : this.txOuts) {
            txOutContent = txOutContent + txOut.addressAndAmt();
        }
        String txContent = txInContent + txOutContent;
        String hash = HashUtil.getHashForStr(txContent);
        return hash;
    }
}

package com.comp4137.blockchain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@ToString

public class TxOut implements Serializable {
    public String address; //ECDSA public key
    public double amount;

    public String addressAndAmt(){
        String out = this.address + this.amount;
        return out;
    }
}

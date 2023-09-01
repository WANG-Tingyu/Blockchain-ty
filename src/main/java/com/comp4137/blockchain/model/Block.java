package com.comp4137.blockchain.model;


import com.comp4137.blockchain.utils.HashUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Block implements Serializable {

    public int index;
    public Long timestamp;
    public String hash;
    public String previousHash;
    public String rootHash;
    public int difficulty;
    public long nonce;
    public List<Transaction> transactionList = new ArrayList<Transaction>() {
    };


    public Block(int index, Long timestamp, String hash, String previousHash, String rootHash, int difficulty, long nonce) {
        this.index = index;
        this.timestamp = timestamp;
        this.hash = hash;
        this.previousHash = previousHash;
        this.rootHash = rootHash;
        this.difficulty = difficulty;
        this.nonce = nonce;
    }

    //测试用，提交时需删除
    public Block(int index, Long timestamp) {
        this.index = index;
        this.timestamp = timestamp;
    }



    private int confirmNum = 0;


    public void addTx(Transaction tx){
        transactionList.add(tx);
    }

    public String calculateHash() {
        String block = index + timestamp + previousHash + rootHash + difficulty + nonce;
        return HashUtil.getHashForStr(block);
    }
}

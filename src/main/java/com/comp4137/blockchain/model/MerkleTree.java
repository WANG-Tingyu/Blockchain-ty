package com.comp4137.blockchain.model;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    // transaction List
    List<String> txList;
    // Merkle Root
    String root;

    /**
     * constructor
     * @param txList transaction List
     */
//    public MerkleTree(List<String> txList) {
//        this.txList = txList;
//        root = "";
//    }

    public MerkleTree(List<Transaction> txs) {
        ArrayList<String> txList = new ArrayList<>();
        for (Transaction tx : txs) {
            txList.add(tx.toString());
        }
        this.txList = txList;
        generateMerkleTreeRoot();
    }

    /**
     * Get Root
     * @return
     */
    public String getRoot() {
        return this.root;
    }


    /**
     * execute merkle_tree and set root.
     */
    public void generateMerkleTreeRoot() {

        List<String> tempTxList = new ArrayList<String>();

        for (int i = 0; i < this.txList.size(); i++) {
            tempTxList.add(this.txList.get(i));
        }
        //System.out.println("tempTxList: " + tempTxList);

        List<String> newTxList = getNewTxList(tempTxList);
        //System.out.println("newTxList: " + newTxList);

        while (newTxList.size() != 1) {
            newTxList = getNewTxList(newTxList);
            System.out.println("newTxList in while loop: " + newTxList);
        }

        this.root = newTxList.get(0);

    }

    /**
     * return Node Hash List.
     * @param tempTxList
     * @return
     */
    private List<String> getNewTxList(List<String> tempTxList) {

        List<String> newTxList = new ArrayList<String>();
        int index = 0;
        while (index < tempTxList.size()) {
            // left node
            String left = tempTxList.get(index);
            index++;

            // right node
            String right = "";
            if (index != tempTxList.size()) {
                right = tempTxList.get(index);
            }

            // sha2 hex value
            String sha2HexValue = getSHA2HexValue(left + right);
            newTxList.add(sha2HexValue);
            index++;

        }

        return newTxList;
    }

    /**
     * Return hex string
     * @param str
     * @return
     */
    public String getSHA2HexValue(String str) {
        byte[] cipher_byte;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for(byte b: cipher_byte) {
                sb.append(String.format("%02x", b&0xff) );
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }



}

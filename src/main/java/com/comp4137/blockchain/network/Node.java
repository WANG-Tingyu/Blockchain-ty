package com.comp4137.blockchain.network;

import com.comp4137.blockchain.model.*;
import com.comp4137.blockchain.utils.ECDSAUtils;
import com.comp4137.blockchain.utils.HashUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Node {
    int index;
    int port;
    Boolean mine;
    int[] portList;

    public PublicKey pubKey;
    private PrivateKey priKey;
    ArrayList<Transaction> txPool = new ArrayList<Transaction>();

    ArrayList<Block> localChain = new ArrayList<Block>();
    HashMap<String, TxOut> utxo = new HashMap<>();

    MsgChannel msgChannel;
    public static final int DIFF_ADJ_INTERVAL = 3;
    public static final int BLOCK_GEN_INTERVAL = 2500;
    public static final int COINBASE_REWARD = 50;

    public Node(int index, int port, Boolean mine, int[] portList) throws Exception {
        this.index = index;
        this.port = port;
        this.mine = mine;
        this.portList = portList;

        KeyPair keyPair = ECDSAUtils.getKeyPair();
        this.pubKey = keyPair.getPublic();
        this.priKey = keyPair.getPrivate();
        msgChannel = new MsgChannel(port, portList);
    }



    @SneakyThrows
    public void initNode() {
        System.out.println(ECDSAUtils.getPubKeyStr(this.pubKey));
        System.out.println("Request Block from neighbor node");
        sendBlockReq();

        System.out.println("Start Receiver Thread");
        receiveMsg();

        if(mine) {
            System.out.println("Start Mining Thread");
            mining(); //持续挖矿
        }

        //Transaction test=testCoinbase(0);
        //Thread.sleep(30000);
        //System.out.println("begin transaction");
        //initTx(test.txIns,test.txOuts);
        //String[] s=utxo.keySet().toArray(new String[0]);
       // for(String s1:s){
            //System.out.println(s1);
        //}

    }

    @SneakyThrows
    public void sendBlockReq(){
        int idx = localChain.size();
        Message message = new Message("" + idx, 1);
        msgChannel.sendMsg(message);
    }

    public void receiveMsg() {
        Thread receiverThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Message msg = null;
                while (true) {
                    msg = msgChannel.receiveMsg();
                    processMsg(msg);
                }
            }
        });
        receiverThread.start();
    }

    public void processMsg(Message msg) throws Exception {
        //System.out.println(msg);
        int type = msg.getType(); //1: str, 2: tx, 3: block
        if(type == 1) processNewReq(msg.getReq());
        else if(type == 2) processNewTx(msg.getTx());
        else if(type == 3) processNewBlock(msg.getBlock());
    }

    @SneakyThrows
    public void processNewReq(String req){
        //neighbor node request block. Current node will send to it.
        int idx = Integer.valueOf(req);
        System.out.println("Node["+index +"]: receive block["+idx+"] request");
        while (idx < localChain.size()){
            Block reqBlock = localChain.get(idx);
            Message blockMsg = new Message(reqBlock, 3);
            msgChannel.sendMsg(blockMsg);
            System.out.println("Node["+index +"]: broadcast block["+ idx+"]");
            idx++;
        }
    }

    /*发起新的Tx*/
    @SneakyThrows
    public void initTx(TxIn[] txIns, TxOut[] txOuts){
        Transaction transaction = new Transaction(txIns, txOuts);
        if(isValidTx(transaction)){
            Message msg = new Message(transaction, 2);
            msgChannel.sendMsg(msg);
        }
    }
    public Transaction Test(String id){
        String inputTxOutId=id;
        String signature=ECDSAUtils.signECDSA(this.priKey,inputTxOutId);

        TxIn in=new TxIn(inputTxOutId,0,signature);
        TxOut out=new TxOut(1+ECDSAUtils.getPubKeyStr(this.pubKey),40);
        TxOut out1=new TxOut(1+ECDSAUtils.getPubKeyStr(this.pubKey),60);
        TxIn[] ins = {in};
        TxOut[] outs = {out,out1};
        Transaction transaction = new Transaction(ins, outs);
        return transaction;
    }

    public Transaction coinbaseTx(){
        TxIn in = new TxIn("0", localChain.size(), "coinbase");
        TxOut out = new TxOut(ECDSAUtils.getPubKeyStr(this.pubKey), COINBASE_REWARD);

        TxIn[] ins = {in};
        TxOut[] outs = {out};
        Transaction transaction = new Transaction(ins, outs);
        return transaction;
    }
    public Transaction testCoinbase(int i){
        TxIn in = new TxIn("0", i, "coinbase");
        TxOut out = new TxOut(ECDSAUtils.getPubKeyStr(this.pubKey), COINBASE_REWARD);

        TxIn[] ins = {in};
        TxOut[] outs = {out};
        Transaction coinbase = new Transaction(ins, outs);
        String inputTxOutId=coinbase.id;
        //System.out.println(coinbase.id);
        String signature=ECDSAUtils.signECDSA(this.priKey,inputTxOutId);

        TxIn in1=new TxIn(inputTxOutId,0,signature);
        TxOut out1=new TxOut(1+ECDSAUtils.getPubKeyStr(this.pubKey),40);
        TxOut out2=new TxOut(1+ECDSAUtils.getPubKeyStr(this.pubKey),60);
        TxIn[] ins1 = {in1};
        TxOut[] outs1 = {out1,out2};
        Transaction transaction = new Transaction(ins1, outs1);
        return transaction;

    }



    public void processNewTx(Transaction tx) throws Exception {
        //验证收到的tx，如果通过，add进tx pool
        //System.out.println("adding tx");


            if (isValidTx(tx))
            {
                txPool.add(tx);
                //System.out.println("txPool.size   " + txPool.size());
            }


    }

    public boolean isValidTx(Transaction tx) throws Exception {
        /*if(tx.txIns[0].txOutId.equalsIgnoreCase("0")){
            return true;
        }*/
        //1.验证txIn是否在utxo里
        if(!isUTXO(tx,utxo)){
            return false;
        }
        //2.验证签名
        for(TxIn txin : tx.txIns) {
            if (!isValidSignature(txin.signature,utxo.get(txin.txOutId+" "+txin.txOutIndex).address,txin.txOutId))
            {
                System.out.println("Wrong signature");
                return false;
            }
        }
        System.out.println("Valid Transaction");
        //System.out.println(tx);
        return true;
    }
    public boolean isUTXO(Transaction tx,HashMap<String,TxOut> utxo){
        if(tx.txIns[0].txOutId.equalsIgnoreCase("0")){
            return true;
        }
        int inputAmount=0;
        int outputAmount=0;
        for(TxIn txIn : tx.txIns){
            //for GesesBlock
            if(utxo.containsKey(txIn.txOutId+" "+txIn.txOutIndex)==false)
            {
                System.out.println("no address "+txIn.txOutId);
                return false;
            }
            TxOut input=utxo.get(txIn.txOutId+" "+txIn.txOutIndex);
            inputAmount+=input.amount;
        }
        for(TxOut txOut: tx.txOuts){
            outputAmount+=txOut.amount;
        }
        //if(inputAmount!=outputAmount){
        //    System.out.println("input!=output");
        //    return false;
        //}
        return true;
    }

    public Boolean isValidSignature(String signature, String publicKey, String message) throws Exception {
        return ECDSAUtils.verifyECDSA(ECDSAUtils.getPublicKey(publicKey),signature,message);
    }
    public Boolean allTxInBlockIsValid(List<Transaction> transactionList){
        //System.out.println(888);
        HashMap<String,TxOut> utxoEmulotor=new  HashMap<String,TxOut>();
        utxoEmulotor.putAll(utxo);
        for(Transaction tx: transactionList){
            if(!isUTXO(tx,utxoEmulotor)){
                return false;
            }
            for(TxIn txIn:tx.txIns){
                utxoEmulotor.remove(txIn.txOutId+" "+txIn.txOutIndex);
            }
            for(int i=0;i<tx.txOuts.length;i++){
                utxoEmulotor.put(tx.id+" "+i,tx.txOuts[i]);
            }
        }
        return true;
    }

    public void processNewBlock(Block block){
        //System.out.println("收到的："+block.toString());
        if(localChain.size() == block.index){
            if(block.index == 0){
                if (isValidGenesisBlock(block)) {
                    this.localChain.add(block);
                    //System.out.println("utxo size: "+utxo.size());
                    updateUTXO(block);
                    //System.out.println("utxo size: "+utxo.size());
                    updateTxPool(block);
                    System.out.println("Add Block["+block.index+"] into local copy");
                }
            } else{
                //System.out.println("现在的本地链长度"+localChain.size()+"收到的block.index"+block.index);
                Block prevBlock = localChain.get(block.index - 1);
                //System.out.println("上一个block："+prevBlock.toString());
                if (isValidNewBlock(block, prevBlock)){
                    this.localChain.add(block);
                    //System.out.println("utxo size: "+utxo.size());
                    updateUTXO(block);
                    //System.out.println("utxo size: "+utxo.size());
                    updateTxPool(block);
                    System.out.println("Add Block["+block.index+"] into local copy");
                }
            }
        }
    }

    public void updateTxPool(Block block){
        synchronized (txPool) {
            List<Transaction> list = block.getTransactionList();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < txPool.size(); j++) {
                    if (list.get(i).getId().equalsIgnoreCase(txPool.get(j).getId())) {
                        txPool.remove(j);
                    }
                }
            }
        }
    }
    //update utxo
    public void updateUTXO(Block block){
        List<Transaction> transactionList=block.transactionList;
        for(Transaction tx: transactionList){
            for(TxIn txIn:tx.txIns){
                utxo.remove(txIn.txOutId+" "+txIn.txOutIndex);
            }
            for(int i=0;i<tx.txOuts.length;i++){
                utxo.put(tx.id+" "+i,tx.txOuts[i]);
            }
        }

    }

    public boolean isValidNewBlock(Block newBlock, Block prevBlock){
        if(prevBlock.getIndex()+1 != newBlock.getIndex()) return false;
        else if(!prevBlock.getHash().equals(newBlock.getPreviousHash())) return false;
        else if(!calculateHash(newBlock).equals(newBlock.getHash())) return false;
        else if(!allTxInBlockIsValid(newBlock.getTransactionList())) return false;
        else return true;
    }
    public boolean isValidGenesisBlock(Block block){
        if(block.index != 0) return false;
        else if(!calculateHash(block).equals(block.getHash())) return false;
        else if(!allTxInBlockIsValid(block.getTransactionList())) return false;
        else return true;
    }

    public void mining() {
        Thread miningThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    int index = localChain.size();
                    String previousHash = null;
                    int diff = DIFF_ADJ_INTERVAL;
                    if(index > 0) {
                        previousHash = localChain.get(localChain.size() - 1).getHash();
                        diff = getDifficulty(localChain);
                    }
                    //add coinbaseTranscation for mining reward

                    Transaction coinbaseTx = coinbaseTx();
                    txPool.add(coinbaseTx);


                    //这里算root hash： txpool
                    //System.out.println(txPool.size());
                    String rootHash = new MerkleTree(txPool).getRoot();

                    //if receive the latest block from other, the current mining will immediately stop and return null.
                    //the block index will be updated in real time
                    Block newBlock = findBlock(index, System.currentTimeMillis(), previousHash, rootHash, diff);


                    //
                    /*List<Transaction> txList = new ArrayList<>();
                    for (Transaction tx : txPool) {
                        if(isValidTx(tx)) txList.add(tx);
                        newBlock.setTransactionList(txList);
                    }*/

                    if(newBlock == null){
                        //把别人发布的block重复的tx，从自己的tx pool移除
                        //更新自己的UTXO
                        txPool.remove(coinbaseTx);
                    }else {
                        for (Transaction tx : txPool) {
                            newBlock.addTx(tx);
                        }
                        if (!allTxInBlockIsValid(newBlock.transactionList)) {
                            newBlock = null;
                            txPool.clear();
                        }
                        //System.out.println(newBlock.toString());
                        localChain.add(newBlock);
                        //System.out.println("");
                        //System.out.println("utxo.size: " + utxo.size());
                        updateUTXO(newBlock);
                        txPool.clear();
                        //System.out.println("utxo.size: " + utxo.size());

                        Message message = new Message(newBlock, 3);
                        msgChannel.sendMsg(message);
                    }

                    //Thread.sleep(1000);
                }
            }
        });
        miningThread.start();
    }


    //模拟挖矿
    public Block genNewBlock(int index, String previousHash){
        /*填入信息，算Hash*/
        //previousHash = localChain.get(localChain.size() - 1).getHash();
        //diff = getDifficulty(localChain);

        return null;
    }

    public Block getLatestBlock(){
        return localChain.get(localChain.size()-1);
    }

    public int getDifficulty(ArrayList<Block> localChain){

        Block latestBlock = localChain.get(localChain.size() - 1);
        if(latestBlock.getIndex() % DIFF_ADJ_INTERVAL == 0 && latestBlock.getIndex() != 0){
            return getAdjustedDifficulty(latestBlock, localChain);
        }else {
            return latestBlock.getDifficulty();
        }
    }

    public int getAdjustedDifficulty(Block latestBlock, ArrayList<Block> localChain){
        Block prevAdjBlock = localChain.get(localChain.size() - DIFF_ADJ_INTERVAL);

        double timeExpected = BLOCK_GEN_INTERVAL * DIFF_ADJ_INTERVAL;
        double timeTaken = latestBlock.getTimestamp() - prevAdjBlock.getTimestamp();

        if(timeTaken < timeExpected/2) return prevAdjBlock.getDifficulty()+1;
        else if(timeTaken > timeExpected*2) return prevAdjBlock.getDifficulty()-1;
        else return prevAdjBlock.getDifficulty();

    }

    public Block findBlock(int index, long timestamp, String previousHash, String rootHash, int difficulty){
        String prefix0 = HashUtil.getPrefix0(difficulty);
        long nonce = 0;
        String str = index + timestamp + previousHash + rootHash + difficulty;
        String hash = HashUtil.getHashForStr(str);
        while(true){
            if(index != localChain.size()) return null;
            assert prefix0 != null;
            if(hash.startsWith(prefix0)){
                break;
            }else {
                nonce++;
                hash = HashUtil.getHashForStr(str + nonce);
            }
        }
        Block block = new Block(index, timestamp, hash, previousHash, rootHash, difficulty, nonce);
        System.out.println("Node["+this.index+"] mine block["+block.index + "]; difficulty: "+difficulty+"; Nonce: "+nonce);
        return block;
    }

    public String calculateHash(Block block){
        String str = block.index + block.timestamp + block.previousHash + block.rootHash + block.difficulty + block.nonce;
        String hash = HashUtil.getHashForStr(str);
        return hash;
    }
}

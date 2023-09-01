package com.comp4137.blockchain.gui;

import com.comp4137.blockchain.model.*;
import com.comp4137.blockchain.network.Node;
import com.comp4137.blockchain.utils.ECDSAUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class NodeGUI extends JFrame {
    Node node;

    Container container;
    JPanel mainPanel;

    JPanel showNodePanel;
    JPanel showBlockPanel;
    JPanel showTxPanel;
    JPanel showUTXOPanel;
    JPanel makeTxPanel;
    String utxoStr;
    JTextArea UTXOInfoTA;
    JScrollPane UTXOInfoJSP;

    Block targetBlock;



    public NodeGUI(Node node){
        this.node = node;

        this.showNodePanel = new JPanel();
        this.showBlockPanel = new JPanel();
        this.showTxPanel = new JPanel();
        this.showUTXOPanel = new JPanel();
        this.makeTxPanel = new JPanel();


        this.setTitle("Node: "+node.getPort());
        this.setLocation(500, 500);
        this.setSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        container.add(northPanel, BorderLayout.NORTH);
        addButtons(northPanel);

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 500));
        container.add(mainPanel, BorderLayout.CENTER);

        showUTXOPanel.setVisible(false);
        setShowUTXOPanel();
        setShowNodePanel();

        this.setVisible(true);
    }

    // NodeInfo page
    public void setShowNodePanel() {
        showNodePanel.setPreferredSize(new Dimension(500, 500));
        showNodePanel.setLayout(null);

        JLabel NodeIndex = new JLabel("Node Index");
        NodeIndex.setBounds(30, 30, 300, 20);
        showNodePanel.add(NodeIndex);
        JTextField NodeIndexTF = new JTextField(String.valueOf(node.getIndex()));
        NodeIndexTF.setBounds(200, 30, 300, 20);
        NodeIndexTF.setEditable(false);
        showNodePanel.add(NodeIndexTF);

        JLabel NodePort = new JLabel("Port");
        NodePort.setBounds(30, 80, 300, 20);
        showNodePanel.add(NodePort);
        JTextField NodePortTF = new JTextField(String.valueOf(node.getPort()));
        NodePortTF.setBounds(200, 80, 300, 20);
        NodePortTF.setEditable(false);
        showNodePanel.add(NodePortTF);

        JLabel NodeAddress = new JLabel("Address");
        NodeAddress.setBounds(30, 130, 300, 20);
        showNodePanel.add(NodeAddress);
        JTextArea NodeAddressTA = new JTextArea(ECDSAUtils.getPubKeyStr(node.getPubKey()));
        NodeAddressTA.setBounds(200, 130, 300, 80);
        NodeAddressTA.setSelectedTextColor(Color.RED);
        NodeAddressTA.setLineWrap(true);
        NodeAddressTA.setEditable(false);
        showNodePanel.add(NodeAddressTA);

        JLabel Miner = new JLabel("Miner");
        Miner.setBounds(30, 240, 300, 20);
        showNodePanel.add(Miner);
        JTextField MinerTF = new JTextField(String.valueOf(node.getMine()));
        MinerTF.setBounds(200, 240, 300, 20);
        MinerTF.setEditable(false);
        showNodePanel.add(MinerTF);

        mainPanel.add(showNodePanel);;
    }

    public void setShowBlockPanel() {
        showBlockPanel.setPreferredSize(new Dimension(500, 500));
        showBlockPanel.setLayout(null);

        JLabel BlockIDX = new JLabel("Block idx");
        BlockIDX.setBounds(30, 30, 300, 20);
        showBlockPanel.add(BlockIDX);
        JTextField blockIDXTF = new JTextField();
        blockIDXTF.setText("Please input here...");
        blockIDXTF.setForeground(Color.GRAY);
        blockIDXTF.setBounds(200, 30, 300, 20);
        showBlockPanel.add(blockIDXTF);

        JLabel Timestamp = new JLabel("Timestamp");
        Timestamp.setBounds(30, 80, 300, 20);
        showBlockPanel.add(Timestamp);
        JTextField timestampTF = new JTextField();
        timestampTF.setBounds(200, 80, 300, 20);
        timestampTF.setEditable(false);
        showBlockPanel.add(timestampTF);

        JLabel Hash = new JLabel("Hash");
        Hash.setBounds(30, 130, 300, 20);
        showBlockPanel.add(Hash);
        JTextField hashTF = new JTextField();
        hashTF.setBounds(200, 130, 300, 20);
        hashTF.setEditable(false);
        showBlockPanel.add(hashTF);

        JLabel PreviousHash = new JLabel("Previous Hash");
        PreviousHash.setBounds(30, 180, 300, 20);
        showBlockPanel.add(PreviousHash);
        JTextField previousHashTF = new JTextField();
        previousHashTF.setBounds(200, 180, 300, 20);
        previousHashTF.setEditable(false);
        showBlockPanel.add(previousHashTF);

        JLabel rootHash = new JLabel("Root Hash");
        rootHash.setBounds(30, 230, 300, 20);
        showBlockPanel.add(rootHash);
        JTextField rootHashTF = new JTextField();
        rootHashTF.setBounds(200, 230, 300, 20);
        rootHashTF.setEditable(false);
        showBlockPanel.add(rootHashTF);

        JLabel Difficulty = new JLabel("Difficulty");
        Difficulty.setBounds(30, 280, 300, 20);
        showBlockPanel.add(Difficulty);
        JTextField difficultyTF = new JTextField();
        difficultyTF.setBounds(200, 280, 300, 20);
        difficultyTF.setEditable(false);
        showBlockPanel.add(difficultyTF);

        JLabel nonce = new JLabel("Nonce");
        nonce.setBounds(30, 330, 300, 20);
        showBlockPanel.add(nonce);
        JTextField nonceTF = new JTextField();
        nonceTF.setBounds(200, 330, 300, 20);
        nonceTF.setEditable(false);
        showBlockPanel.add(nonceTF);

        JButton findNode = new JButton("Find");
        findNode.setBounds(210, 380, 80, 30);
        showBlockPanel.add(findNode);

        mainPanel.add(showBlockPanel);

        findNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idxStr = blockIDXTF.getText();
                int idx = Integer.parseInt(idxStr);
                targetBlock = node.getLocalChain().get(idx);
                timestampTF.setText(String.valueOf(targetBlock.getTimestamp()));
                hashTF.setText(targetBlock.getHash());
                previousHashTF.setText(targetBlock.getPreviousHash());
                rootHashTF.setText(targetBlock.getRootHash());
                difficultyTF.setText(String.valueOf(targetBlock.getDifficulty()));
                nonceTF.setText(String.valueOf(targetBlock.getNonce()));
            }
        });
    }

    public void setShowTxPanel() {
        showTxPanel.setPreferredSize(new Dimension(500, 500));
        showTxPanel.setLayout(null);


        JLabel blockIdx = new JLabel("Block index:");
        blockIdx.setBounds(30, 10, 300, 20);
        showTxPanel.add(blockIdx);
        JTextField blockIdxTF = new JTextField();
        blockIdxTF.setBounds(200, 10, 300, 20);
        showTxPanel.add(blockIdxTF);

        JLabel txIdx = new JLabel("Transaction index:");
        txIdx.setBounds(30, 35, 300, 20);
        showTxPanel.add(txIdx);
        JTextField txIdxTF = new JTextField();
        txIdxTF.setBounds(200, 35, 300, 20);
        showTxPanel.add(txIdxTF);

        JButton find = new JButton("Find");
        find.setBounds(200, 60, 100, 20);
        showTxPanel.add(find);

        JLabel txId = new JLabel("Tx Id");
        txId.setBounds(30, 90, 300, 20);
        showTxPanel.add(txId);
        JTextField txIdTF = new JTextField("",20);
        txIdTF.setEditable(false);
        txIdTF.setBounds(200, 90, 300, 20);
        showTxPanel.add(txIdTF);

        JLabel txInput = new JLabel("Tx Input");
        txInput.setBounds(30, 120, 100, 20);
        showTxPanel.add(txInput);

        JLabel txOutput = new JLabel("Tx Output");
        txOutput.setBounds(270, 120, 100, 20);
        showTxPanel.add(txOutput);


        JLabel InputID = new JLabel("id:");
        InputID.setBounds(30, 160, 100, 20);
        showTxPanel.add(InputID);
        JTextField InputIDTF = new JTextField("",20);
        InputIDTF.setEditable(false);
        InputIDTF.setBounds(30, 190, 200, 20);
        showTxPanel.add(InputIDTF);


        JLabel InputIndex = new JLabel("Index");
        InputIndex.setBounds(30, 230, 100, 20);
        showTxPanel.add(InputIndex);
        JTextField InputIndexTF = new JTextField();
        InputIndexTF.setEditable(false);
        InputIndexTF.setBounds(30, 260, 200, 20);
        showTxPanel.add(InputIndexTF);



        JLabel InputSignature = new JLabel("Signature");
        InputSignature.setBounds(30, 300, 100, 20);
        showTxPanel.add(InputSignature);
        JTextField InputSignatureTF = new JTextField();
        InputSignatureTF.setEditable(false);
        InputSignatureTF.setBounds(30, 330, 200, 20);
        showTxPanel.add(InputSignatureTF);

        JLabel OutputAddress = new JLabel("Address");
        OutputAddress.setBounds(270, 160, 100, 20);
        showTxPanel.add(OutputAddress);
        JTextField OutputAddressTF = new JTextField();
        OutputAddressTF.setBounds(270, 190, 200, 20);
        OutputAddressTF.setEditable(false);
        showTxPanel.add(OutputAddressTF);

        JLabel OutputAmount = new JLabel("Amount");
        OutputAmount.setBounds(270, 230, 100, 20);
        showTxPanel.add(OutputAmount);
        JTextField OutputAmountTF = new JTextField();
        OutputAmountTF.setBounds(270, 260, 200, 20);
        OutputAmountTF.setEditable(false);
        showTxPanel.add(OutputAmountTF);

        mainPanel.add(showTxPanel);

        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //拿到tx
                int blockIdx = Integer.parseInt(blockIdxTF.getText());
                int txIdx = Integer.parseInt(txIdxTF.getText());
                Block block = node.getLocalChain().get(blockIdx);
                Transaction transaction = block.getTransactionList().get(txIdx);
                txIdTF.setText(transaction.getId());
                InputIDTF.setText(transaction.getTxIns()[0].txOutId);
                InputIndexTF.setText(String.valueOf(transaction.getTxIns()[0].txOutIndex));
                InputSignatureTF.setText(transaction.getTxIns()[0].signature);

                OutputAddressTF.setText(String.valueOf(transaction.getTxOuts()[0].address));
                OutputAmountTF.setText(String.valueOf(transaction.getTxOuts()[0].amount));
            }
        });
    }

    public void setShowUTXOPanel() {
        UTXOInfoTA = new JTextArea();
        UTXOInfoTA.setSelectedTextColor(Color.RED);
        UTXOInfoTA.setLineWrap(true);
        showUTXOPanel.setPreferredSize(new Dimension(500, 500));
        showUTXOPanel.setLayout(null);
        UTXOInfoJSP = new JScrollPane(UTXOInfoTA);
        UTXOInfoJSP.setBounds(25, 25, 450, 450);
        UTXOInfoJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        showUTXOPanel.add(UTXOInfoJSP, BorderLayout.CENTER);
        mainPanel.add(showUTXOPanel);
    }

    public void updateUTXO(){
        utxoStr = setUTXO();
        UTXOInfoTA.setText(utxoStr);
        UTXOInfoTA.paintImmediately(UTXOInfoTA.getBounds());
        UTXOInfoTA.setEditable(false);
    }
    public String setUTXO(){
        HashMap<String, TxOut> map = node.getUtxo();
        //System.out.println("来里面了"+map.size()+"\n");

        String uxtoTempStr = "";
        for (String s : map.keySet()) {
            String[] arr = s.split("\\s+");
            String id = arr[0];
            String idx = arr[1];
            //System.out.println("idx.equals(\"1\")"+idx.equals("1"));
            //String idx ="0";
            double amount = map.get(s).amount;
            uxtoTempStr += "id: "+id + "\nidx: "+idx + "\namount: "+amount+"\n\n";
        }
        //System.out.println(uxtoTempStr);
        return uxtoTempStr;
    }




    public void setMkTxPanel() {
        makeTxPanel.setPreferredSize(new Dimension(500, 500));
        makeTxPanel.setLayout(null);

        JTextField TxInoutIdxTF, MakeTxAmountTF;
        JLabel TxInoutId, TxInoutIdx, TxAddress, MakeTxAmount;

        TxInoutId = new JLabel("TxIn outId");
        TxInoutId.setBounds(30, 30, 300, 20);
        JTextArea TxInoutIdTA = new JTextArea();
        TxInoutIdTA.setLineWrap(true);
        TxInoutIdTA.setBounds(200, 30, 300, 60);

        TxInoutIdx = new JLabel("TxIn outIdx");
        TxInoutIdx.setBounds(30, 120, 300, 20);
        TxInoutIdxTF = new JTextField();
        TxInoutIdxTF.setBounds(200, 120, 300, 20);

        TxAddress = new JLabel("Address");
        TxAddress.setBounds(30, 170, 300, 20);
        JTextArea TxAddressTF = new JTextArea();
        TxAddressTF.setBounds(200, 170, 300, 80);
        TxAddressTF.setSelectedTextColor(Color.RED);
        TxAddressTF.setLineWrap(true);

        MakeTxAmount = new JLabel("Amount");
        MakeTxAmount.setBounds(30, 280, 300, 20);
        MakeTxAmountTF = new JTextField();
        MakeTxAmountTF.setBounds(200, 280, 300, 20);

        JButton send = new JButton("Send");
        send.setBounds(240, 330, 100, 20);

        makeTxPanel.add(TxInoutId);
        makeTxPanel.add(TxInoutIdTA);
        makeTxPanel.add(TxInoutIdx);
        makeTxPanel.add(TxInoutIdxTF);
        makeTxPanel.add(TxAddress);
        makeTxPanel.add(TxAddressTF);
        makeTxPanel.add(MakeTxAmount);
        makeTxPanel.add(MakeTxAmountTF);
        makeTxPanel.add(send);

        mainPanel.add(makeTxPanel);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send transaction
                String inputTxOutId=TxInoutIdTA.getText();
                int inputTxOutIndex=Integer.parseInt(TxInoutIdxTF.getText());
                String signature=ECDSAUtils.signECDSA(node.getPriKey(),inputTxOutId);
                TxIn in=new TxIn(inputTxOutId,inputTxOutIndex,signature);
                TxOut out=new TxOut(TxAddressTF.getText(),Integer.parseInt(MakeTxAmountTF.getText()));
                TxIn[] ins={in};
                TxOut[] outs = {out};
                Transaction transaction = new Transaction(ins, outs);
                //transaction=node.testCoinbase(0);
                try {
                    if(node.isValidTx(transaction)){
                        //node.getTxPool().add(transaction);
                        Message msg = new Message(transaction, 2);
                        node.getMsgChannel().sendMsg(msg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public void addButtons(JPanel panel) {
        JButton showNode = new JButton("Node Info");
        JButton showBlock = new JButton("Find Block");
        JButton showTx = new JButton("Find Transaction");
        JButton showUTXO = new JButton("UTXO");
        JButton mkTx = new JButton("Make Transaction");

        showNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowNodePanel();
                showNodePanel.setVisible(true);
                showBlockPanel.setVisible(false);
                showTxPanel.setVisible(false);
                showUTXOPanel.setVisible(false);
                makeTxPanel.setVisible(false);
            }

        });
        showBlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowBlockPanel();
                showNodePanel.setVisible(false);
                showBlockPanel.setVisible(true);
                showTxPanel.setVisible(false);
                showUTXOPanel.setVisible(false);
                makeTxPanel.setVisible(false);
            }

        });
        showTx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setShowTxPanel();
                showNodePanel.setVisible(false);
                showBlockPanel.setVisible(false);
                showTxPanel.setVisible(true);
                showUTXOPanel.setVisible(false);
                makeTxPanel.setVisible(false);
            }

        });
        showUTXO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUTXO();
                showNodePanel.setVisible(false);
                showBlockPanel.setVisible(false);
                showTxPanel.setVisible(false);
                showUTXOPanel.setVisible(true);
                makeTxPanel.setVisible(false);
            }

        });
        mkTx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMkTxPanel();
                showNodePanel.setVisible(false);
                showBlockPanel.setVisible(false);
                showTxPanel.setVisible(false);
                showUTXOPanel.setVisible(false);
                makeTxPanel.setVisible(true);
            }
        });
        panel.add(showNode);
        panel.add(showBlock);
        panel.add(showTx);
        panel.add(showUTXO);
        panel.add(mkTx);
    }
}

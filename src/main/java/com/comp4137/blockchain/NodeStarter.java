package com.comp4137.blockchain;

import com.comp4137.blockchain.gui.NodeGUI;
import com.comp4137.blockchain.network.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class NodeStarter {

    int nodeIdx;
    int port;
    Node node;
    Boolean mine;
    int[] portList;


    //Port, mine or not
    public NodeStarter(String idx,String portNum, String miner) throws Exception {
        this.nodeIdx = Integer.valueOf(idx);
        this.port = Integer.valueOf(portNum);
        if(miner.equals("yes")) this.mine = true;
        else this.mine = false;
        this.portList = getPortList();
        node = new Node(nodeIdx, port, mine, portList);
    }
    //默认不挖
    public NodeStarter(String idx, String portNum) throws Exception {
        this(idx, portNum, " ");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
//        new NodeStarter(args[0], args[1]).startApp();
//        new NodeStarter("0","8081", "yes").startApp();
//        new NodeStarter("1","8080").startApp();
    }

    public void startApp() throws SocketException, InterruptedException {
        new NodeGUI(this.node);
        this.node.initNode();
    }

    public int[] getPortList() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("etc/portList.cfg"));
        String contentLine = in.readLine();
        List<Integer> list = new ArrayList<Integer>();
        while (contentLine != null) {
            list.add(Integer.valueOf(contentLine));
            contentLine = in.readLine();
        }
        int[] out = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            out[i] = list.get(i);
        }
        return out;
    }



}

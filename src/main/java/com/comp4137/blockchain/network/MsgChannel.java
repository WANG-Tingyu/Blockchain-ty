package com.comp4137.blockchain.network;

import com.comp4137.blockchain.model.Message;
import lombok.SneakyThrows;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MsgChannel {

    DatagramSocket socket = null;
    public int from;
    public int[] portList;

    public MsgChannel(int from, int[] portList) {
        this.from = from;
        this.portList = portList;
//        System.out.println(Arrays.toString(portList));
        try {
            socket = new DatagramSocket(from);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] msg, int port) throws Exception{
        DatagramPacket packet = new DatagramPacket(msg, 0, msg.length, new InetSocketAddress("localhost", port));
        socket.send(packet);
    }

    public void sendMsg(Message msg) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        oos.flush();
        byte[] buffer = baos.toByteArray();

        for (int port : portList) {
            //if(port != from)
                send(buffer, port);
        }
    }

    public Message receiveMsg() throws IOException, ClassNotFoundException {
        byte[] container = new byte[10240];
        DatagramPacket datagram = new DatagramPacket(container, 0, container.length);

        socket.receive(datagram);
        ByteArrayInputStream bais = new ByteArrayInputStream(container);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message msg = (Message) ois.readObject();
        return msg;
    }


}

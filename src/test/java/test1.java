import com.comp4137.blockchain.NodeStarter;
import com.comp4137.blockchain.model.Message;
import com.comp4137.blockchain.model.Transaction;
import com.comp4137.blockchain.model.TxIn;
import com.comp4137.blockchain.model.TxOut;
import com.comp4137.blockchain.network.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class test1 {

    public static void main(String[] args) throws Exception {
        new NodeStarter("0", "1111", "yes").startApp();
    }
}

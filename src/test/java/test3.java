import com.comp4137.blockchain.NodeStarter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.SocketException;

public class test3 {

    public static void main(String[] args) throws Exception {
        NodeStarter nodeStarter1 = new NodeStarter("2", "3333");

        nodeStarter1.startApp();
    }
}

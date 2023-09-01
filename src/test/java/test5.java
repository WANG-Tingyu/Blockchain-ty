import com.comp4137.blockchain.model.Block;
import com.comp4137.blockchain.model.Transaction;
import com.comp4137.blockchain.model.TxIn;
import com.comp4137.blockchain.network.Node;

import java.util.ArrayList;
import java.util.List;

public class test5 {
    static List<Transaction> txPool = new ArrayList<>();


    public static void updateTxPool(Block block){
        List<Transaction> list = block.getTransactionList();
        for (Transaction transaction : list) {
            txPool.remove(transaction);
        }
    }
    public static void main(String[] args) {

    }
}

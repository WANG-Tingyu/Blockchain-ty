package com.comp4137.blockchain.listener;

import com.comp4137.blockchain.event.NewBlockEvent;

public class NewBlockListener implements IBlockListener{
    public void newBlockEvent(NewBlockEvent event) {
        //验证现在的block是否是新的
        if(event.getIndex() != 0){
            System.out.println("现在的block idx: "+event.getIndex());
            //添加block进本地copy
        }else {
            System.out.println("没收到或已经有了");
        }
    }
}

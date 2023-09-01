package com.comp4137.blockchain.event;



import java.util.EventObject;

public class NewBlockEvent extends EventObject implements Cloneable{
    private int index;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NewBlockEvent(Object source, int index) {
        super(source);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

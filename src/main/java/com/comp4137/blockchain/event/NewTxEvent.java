package com.comp4137.blockchain.event;

import java.util.EventObject;

public class NewTxEvent extends EventObject implements Cloneable {
    private String id;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NewTxEvent(Object source, String id) {
        super(source);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

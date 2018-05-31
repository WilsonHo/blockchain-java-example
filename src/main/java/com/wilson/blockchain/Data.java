package com.wilson.blockchain;

/**
 * Created on 5/31/18.
 */
public class Data {
    private String sender;
    private String recipient;
    private String value;
    private String sequence;

    public Data(String sender, String recipient, String value, String sequence) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.sequence = sequence;
    }
}

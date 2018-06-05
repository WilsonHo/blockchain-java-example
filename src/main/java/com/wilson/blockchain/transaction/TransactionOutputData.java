package com.wilson.blockchain.transaction;

import java.util.UUID;

/**
 * Created on 6/1/18.
 */
public class TransactionOutputData {
    private String sender;
    private String recipient;
    private String value;
    private String leftOver;
    private UUID parentTransactionId;


    private TransactionOutputData() {

    }

    public static TransactionOutputData generateTransactionOutputSenderData(String sender,
                                                                            String leftOver,
                                                                            UUID parentTransactionId) {
        return new TransactionOutputData()
                .setSender(sender)
                .setLeftOver(leftOver)
                .setParentTransactionId(parentTransactionId)
                .setRecipient("")
                .setValue("");
    }

    public static TransactionOutputData generateTransactionOutputRecipientData(String recipient,
                                                                               String value,
                                                                               UUID parentTransactionId) {
        return new TransactionOutputData()
                .setSender("")
                .setLeftOver("")
                .setParentTransactionId(parentTransactionId)
                .setRecipient(recipient)
                .setValue(value);
    }

    public String getSender() {
        return sender;
    }

    public TransactionOutputData setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getRecipient() {
        return recipient;
    }

    public TransactionOutputData setRecipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TransactionOutputData setValue(String value) {
        this.value = value;
        return this;
    }

    public String getLeftOver() {
        return leftOver;
    }

    public TransactionOutputData setLeftOver(String leftOver) {
        this.leftOver = leftOver;
        return this;
    }

    public UUID getParentTransactionId() {
        return parentTransactionId;
    }

    public TransactionOutputData setParentTransactionId(UUID parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
        return this;
    }
}

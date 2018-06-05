package com.wilson.blockchain.transaction;

import java.security.PublicKey;
import java.util.UUID;

/**
 * Created on 6/1/18.
 */
public class TransactionInput {
    private UUID transactionOutputId; //Reference to TransactionOutputs -> transactionId
    private TransactionOutput utxo; //Contains the Unspent transaction output

    public TransactionInput(UUID transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public UUID getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionOutput getUtxo() {
        return utxo;
    }

    public void setUtxo(TransactionOutput utxo) {
        this.utxo = utxo;
    }
}

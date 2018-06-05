package com.wilson.blockchain.transaction;

/**
 * Created on 6/1/18.
 */
public class TransactionInput {
    private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    private TransactionOutput utxo; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionOutput getUtxo() {
        return utxo;
    }

    public void setUtxo(TransactionOutput utxo) {
        this.utxo = utxo;
    }
}

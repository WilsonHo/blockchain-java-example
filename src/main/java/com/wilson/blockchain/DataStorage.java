package com.wilson.blockchain;

import com.wilson.blockchain.transaction.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 6/1/18.
 */
public final class DataStorage {
    private DataStorage() {
    }

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //list of all unspent transactions.

}

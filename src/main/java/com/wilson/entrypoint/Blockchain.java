package com.wilson.entrypoint;

import com.google.gson.GsonBuilder;
import com.wilson.blockchain.Block;
import com.wilson.blockchain.BlockUtil;
import com.wilson.blockchain.Data;

import java.util.ArrayList;

/**
 * Created on 5/31/18.
 */
public class Blockchain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 3;

    public static void main(String[] args) {
        // add our blocks to the blockchain ArrayList:
        blockchain.add(new Block(new Data("Hi im the first block"), "0"));
        System.out.println("Trying to Mine block 1... ");
        BlockUtil.mineBlock(blockchain.get(0), difficulty);

        System.out.println("\nBlockchain is Valid: " + BlockUtil.isChainValid(blockchain, difficulty));


        blockchain.add(new Block(new Data("Yo im the second block"), blockchain.get(blockchain.size() - 1).getHash()));
        System.out.println("Trying to Mine block 2... ");
        BlockUtil.mineBlock(blockchain.get(1), difficulty);

        blockchain.add(new Block(new Data("Hey im the third block"), blockchain.get(blockchain.size() - 1).getHash()));
        System.out.println("Trying to Mine block 3... ");
        BlockUtil.mineBlock(blockchain.get(2), difficulty);

        System.out.println("\nBlockchain is Valid: " + BlockUtil.isChainValid(blockchain, difficulty));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);

    }
}

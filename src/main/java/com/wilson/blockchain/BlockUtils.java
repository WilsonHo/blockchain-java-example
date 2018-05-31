package com.wilson.blockchain;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created on 5/31/18.
 */
public final class BlockUtils {
    private BlockUtils() {
    }

    // Applies Sha256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHash(Block block) {
        String inputData = block.getPreviousHash()
                + Long.toString(block.getCreatedAt())
                + Integer.toString(block.getNonce())
                + block.getData();
        return BlockUtils.applySha256(inputData);
    }

    public static Boolean isChainValid(ArrayList<Block> blockchain, int difficulty) {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            // compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(calculateHash(currentBlock))) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            // compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

    public static void mineBlock(Block block, int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            int nonce = block.getNonce();
            block = block.setNonce(++nonce)
                    .setHash(calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());
    }

}

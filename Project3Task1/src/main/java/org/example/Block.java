/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;


/**
 * The Block class represents an individual block within the blockchain.
 * Each block has an index, timestamp, data, previous hash, nonce, and difficulty.
 */
public class Block {
    private int index; // Position of the block within the blockchain
    private Timestamp timestamp; // Time when the block was generated
    private String data; // Transaction data stored in the block
    private String previousHash; // Hash of the preceding block in the chain
    private BigInteger nonce; // Nonce value determined by proof of work
    private int difficulty; // Mining difficulty level for the block

    /**
     * Constructor to initialize a new Block.
     *
     * @param index      Position within the chain. Genesis block is at 0.
     * @param timestamp  Creation time of the block.
     * @param data       Transaction details stored in the block.
     * @param difficulty Required number of leading hex zeroes for the hash.
     */
    public Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.previousHash = "0";
        this.nonce = BigInteger.ZERO;
    }

    /**
     * Calculates the SHA-256 hash of the block based on its properties.
     *
     * @return a String representing the hash in hexadecimal format.
     */
    public String calculateHash() {
        try {
            String input = index + timestamp.toString() + data + previousHash + nonce + difficulty;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }

    /**
     * Finds a valid hash by incrementing the nonce until the hash meets the difficulty.
     *
     * @return the valid hash with the required number of leading hex zeroes.
     */
    public String mineBlock() {
        String target = "0".repeat(difficulty);
        while (!calculateHash().startsWith(target)) {
            nonce = nonce.add(BigInteger.ONE);
        }
        return calculateHash();
    }

    /**
     * Gets the nonce for this block.
     *
     * @return a BigInteger representing the nonce for this block.
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * Gets the difficulty level of the block.
     *
     * @return the difficulty of this block.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level required for this block.
     *
     * @param difficulty the difficulty level specifying the required leading zeroes in the hash.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the previous hash for this block.
     *
     * @return the hash of the preceding block in the chain.
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Sets the previous hash (hash pointer) of this block's parent.
     *
     * @param previousHash the hash of the preceding block.
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * Gets the index of the block.
     *
     * @return the index of this block within the chain.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the block.
     *
     * @param index the position of this block within the chain.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the timestamp of this block.
     *
     * @return the timestamp representing when this block was created.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the block.
     *
     * @param timestamp the creation time of this block.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the transaction data stored in this block.
     *
     * @return the transaction details of this block.
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the transaction data for this block.
     *
     * @param data the transaction details to be included in the block.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Provides a JSON-like string representation of the block's data.
     *
     * @return a JSON-like string with the block's data fields.
     */
    @Override
    public String toString() {
        return "{\"index\" : " + index + "," +
                "\"time stamp \" : \"" + timestamp + "\"," +
                "\"Tx \": \"" + data + "\"," +
                "\"PrevHash\" : \"" + previousHash + "\"," +
                "\"nonce\" : " + nonce + "," +
                "\"difficulty\": " + difficulty + "}";
    }
}

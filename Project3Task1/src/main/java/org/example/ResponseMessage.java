/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

import java.math.BigInteger;

/**
 * Represents a response message sent from the server to the client.
 * Contains details about the blockchain's status or the result of an operation.
 */
public class ResponseMessage {
    private String status;              // Status of the response (e.g., "success" or "error")
    private String message;             // Message detailing the response
    private String chainHash;           // Current hash of the blockchain
    private int chainSize;              // Total number of blocks in the blockchain
    private int difficulty;             // Difficulty level of the last block
    private int totalDifficulty;        // Aggregate difficulty of all blocks in the blockchain
    private int hashesPerSecond;        // Estimated hashing speed of the system
    private double totalExpectedHashes; // Total expected hashes for the blockchain based on difficulty
    private BigInteger nonce;           // Nonce of the last block in the blockchain
    private long startTime;             // Timestamp for the start of an operation
    private long endTime;               // Timestamp for the end of an operation

    /**
     * Constructs a new ResponseMessage with the specified details.
     *
     * @param status              The status of the response
     * @param message             A message describing the response
     * @param chainHash           The hash of the blockchain
     * @param chainSize           The number of blocks in the blockchain
     * @param difficulty          The difficulty of the most recent block
     * @param totalDifficulty     The total difficulty across all blocks
     * @param hashesPerSecond     The blockchain's approximate hash rate
     * @param totalExpectedHashes The expected total number of hashes for the entire blockchain
     * @param nonce               The nonce of the last block
     * @param startTime           The start time of the operation
     * @param endTime             The end time of the operation
     */
    public ResponseMessage(String status, String message, String chainHash, int chainSize, int difficulty, int totalDifficulty, int hashesPerSecond, double totalExpectedHashes, BigInteger nonce, long startTime, long endTime) {
        this.status = status;
        this.message = message;
        this.chainHash = chainHash;
        this.chainSize = chainSize;
        this.difficulty = difficulty;
        this.totalDifficulty = totalDifficulty;
        this.hashesPerSecond = hashesPerSecond;
        this.totalExpectedHashes = totalExpectedHashes;
        this.nonce = nonce;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the response status.
     *
     * @return The status of the response
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the message associated with the response.
     *
     * @return The message detailing the response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the hash of the blockchain.
     *
     * @return The current chain hash
     */
    public String getChainHash() {
        return chainHash;
    }

    /**
     * Gets the number of blocks in the blockchain.
     *
     * @return The total number of blocks
     */
    public int getChainSize() {
        return chainSize;
    }

    /**
     * Gets the difficulty level of the last block in the blockchain.
     *
     * @return The difficulty of the last block
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the total difficulty of the blockchain.
     *
     * @return The aggregated difficulty of all blocks
     */
    public int getTotalDifficulty() {
        return totalDifficulty;
    }

    /**
     * Gets the approximate hash rate of the system.
     *
     * @return The hash rate in hashes per second
     */
    public int getHashesPerSecond() {
        return hashesPerSecond;
    }

    /**
     * Gets the total expected number of hashes for the blockchain.
     *
     * @return The total expected hashes based on difficulty
     */
    public double getTotalExpectedHashes() {
        return totalExpectedHashes;
    }

    /**
     * Gets the nonce of the last block.
     *
     * @return The nonce of the most recent block
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * Gets the start time of the operation.
     *
     * @return The operation start timestamp
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the operation.
     *
     * @return The operation end timestamp
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Provides a JSON-like string representation of the response message.
     *
     * @return A string representation of the response message
     */
    @Override
    public String toString() {
        return "{\"status\":\"" + status + "\", \"message\":\"" + message + "\", \"chainHash\":\"" + chainHash + "\", \"chainSize\":" + chainSize +
                ", \"difficulty\":" + difficulty + ", \"totalDifficulty\":" + totalDifficulty + ", \"hashesPerSecond\":" + hashesPerSecond +
                ", \"totalExpectedHashes\":" + totalExpectedHashes + ", \"nonce\":" + nonce + "}";
    }
}

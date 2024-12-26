/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

/**
 * Represents a request message sent from the client to the server,
 * specifying the action, data (transaction information), and difficulty level.
 */
public class RequestMessage {
    private String action;        // The action to be performed, e.g., "addBlock", "isChainValid", "corruptBlock"
    private String transactionData; // Data related to the transaction, if applicable
    private int difficultyOrIndex;  // Difficulty level for mining or block index for specific actions

    /**
     * Constructs a RequestMessage with specified action, transaction data, and difficulty level.
     *
     * @param action           the action to be performed by the server
     * @param transactionData  the transaction data or content relevant to the action
     * @param difficultyOrIndex the difficulty level or index for the block depending on the action
     */
    public RequestMessage(String action, String transactionData, int difficultyOrIndex) {
        this.action = action;
        this.transactionData = transactionData;
        this.difficultyOrIndex = difficultyOrIndex;
    }

    /**
     * Gets the action associated with the request.
     *
     * @return the action as a string
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the transaction data associated with the request.
     *
     * @return the transaction data as a string
     */
    public String getTransactionData() {
        return transactionData;
    }

    /**
     * Gets the difficulty level or block index associated with the request.
     *
     * @return an integer representing either the difficulty or block index
     */
    public int getDifficultyOrIndex() {
        return difficultyOrIndex;
    }

    /**
     * Returns a string representation of the request message in JSON format.
     *
     * @return a JSON-like string with the action, transaction data, and difficulty or index
     */
    @Override
    public String toString() {
        return "{\"action\":\"" + action + "\", \"transactionData\":\"" + transactionData + "\", \"difficultyOrIndex\":" + difficultyOrIndex + "}";
    }
}

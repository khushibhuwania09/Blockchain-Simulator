/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 */
package org.example;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * ServerTCP handles client requests over TCP sockets, manages the blockchain on the server,
 * and processes various blockchain operations.
 */
public class VerifyingServerTCP {
    public static void main(String[] args) {
        try {
            // Set up server socket to listen on port 7777
            int port = 7777;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Blockchain Server Running");

            // Initialize blockchain with a genesis block
            Blockchain blockchain = new Blockchain();

            // Continuously listen for incoming client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try (Scanner inputScanner = new Scanner(clientSocket.getInputStream());
                     PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())))) {

                    Gson gson = new Gson();

                    while (inputScanner.hasNextLine()) {
                        // Process each incoming client request
                        String requestJson = inputScanner.nextLine();
                        SignedRequest signedRequest = gson.fromJson(requestJson, SignedRequest.class);
                        RequestMessage request = signedRequest.getTransactionRequest();

                        // Verify client ID and signature
                        if (!isValidRequest(signedRequest)) {
                            outputWriter.println(gson.toJson(new ResponseMessage("error", "Invalid request", null, -1, 0, 0, 0, 0, null, 0L, 0L)));
                            outputWriter.flush();
                            continue;
                        }

                        String responseJson = processRequest(request, blockchain, gson);
                        outputWriter.println(responseJson);
                        outputWriter.flush();

                        // Log request and response details for debugging
                        System.out.println("\nWe have a visitor");
                        System.out.println("THE JSON REQUEST MESSAGE IS: " + requestJson);
                        System.out.println("THE JSON RESPONSE MESSAGE IS: " + responseJson);
                        System.out.println("Number of Blocks on Chain: " + blockchain.getSize());
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                } finally {
                    System.out.println("Client disconnected");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Validates the client's request by checking the public key and verifying the signature.
     *
     * @param signedRequest The signed request object from the client.
     * @return true if the request is valid, false otherwise.
     */
    private static boolean isValidRequest(SignedRequest signedRequest) {
        try {
            // Recreate the hash of the request for signature verification
            Gson gson = new Gson();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String requestString = gson.toJson(signedRequest.getTransactionRequest()); // Convert request to JSON
            byte[] requestHash = md.digest(requestString.getBytes()); // Hash the request
            BigInteger requestHashBigInt = new BigInteger(1, requestHash); // Ensure the hash is positive

            // Get the public key components from the signed request
            BigInteger e = new BigInteger(signedRequest.getPublicKeyExponent());
            BigInteger n = new BigInteger(signedRequest.getPublicKeyModulus());

            // Verify the client's ID
            String publicKeyConcat = signedRequest.getPublicKeyExponent() + signedRequest.getPublicKeyModulus();
            MessageDigest idDigest = MessageDigest.getInstance("SHA-256");
            byte[] idHash = idDigest.digest(publicKeyConcat.getBytes());
            String clientId = new BigInteger(1, idHash).toString(16).substring(idHash.length - 20);

            if (!clientId.equals(signedRequest.getClientIdentifier())) {
                return false; // Invalid public key ID
            }

            // Decrypt the signature using the public key
            BigInteger decryptedSignature = new BigInteger(signedRequest.getDigitalSignature()).modPow(e, n);

            // Check if the recreated hash matches the decrypted signature
            boolean isValid = requestHashBigInt.equals(decryptedSignature);
            // Print verification status
            if (isValid) {
                System.out.println("Signature verified successfully.");
            } else {
                System.out.println("Signature verification failed.");
            }
            return isValid;
        } catch (Exception e) {
            System.err.println("Error in request validation: " + e.getMessage());
            return false; // Return false in case of any error
        }
    }

    /**
     * Processes incoming requests and generates appropriate responses.
     *
     * @param request    The request message received from the client.
     * @param blockchain The blockchain instance to perform operations on.
     * @param gson       Gson instance for JSON processing.
     * @return JSON-formatted response message.
     */
    private static String processRequest(RequestMessage request, Blockchain blockchain, Gson gson) {
        long startTime, endTime;
        String responseJson;

        switch (request.getAction()) {
            case "getStatus": // Retrieve blockchain status
                startTime = System.currentTimeMillis();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Viewing status", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "addBlock": // Add a new block to the blockchain
                startTime = System.currentTimeMillis();
                Block newBlock = new Block(blockchain.getSize(), new Timestamp(System.currentTimeMillis()), request.getTransactionData(), request.getDifficultyOrIndex());
                blockchain.addBlock(request.getTransactionData(), request.getDifficultyOrIndex());
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Block added", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "verifyChain": // Verify the blockchain's integrity
                startTime = System.currentTimeMillis();
                String validationResult = blockchain.isChainValid();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", validationResult, blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "viewChain": // View the entire blockchain
                startTime = System.currentTimeMillis();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Viewing blockchain", blockchain.toString(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            case "corruptBlock": // Corrupt a specific block in the blockchain
                startTime = System.currentTimeMillis();
                int blockIndex = request.getDifficultyOrIndex();
                if (blockIndex >= 0 && blockIndex < blockchain.getSize()) {
                    blockchain.corruptBlock(blockIndex, request.getTransactionData());
                    endTime = System.currentTimeMillis();
                    responseJson = gson.toJson(new ResponseMessage(
                            "success", "Block " + blockIndex + " corrupted with new data",
                            blockchain.getBlockchainHash(), blockchain.getSize(),
                            blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                            blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                            blockchain.getLastBlock().getNonce(), startTime, endTime));
                } else {
                    responseJson = gson.toJson(new ResponseMessage(
                            "error", "Invalid block index", null, blockchain.getSize(),
                            0, blockchain.getTotalDifficulty(), 0, 0, null, 0L, 0L));
                }
                break;

            case "repairChain": // Repair the blockchain by recalculating hashes
                startTime = System.currentTimeMillis();
                blockchain.repairChain();
                endTime = System.currentTimeMillis();
                responseJson = gson.toJson(new ResponseMessage(
                        "success", "Blockchain repaired", blockchain.getBlockchainHash(), blockchain.getSize(),
                        blockchain.getLastBlock().getDifficulty(), blockchain.getTotalDifficulty(),
                        blockchain.getHashRate(), blockchain.getExpectedTotalHashes(),
                        blockchain.getLastBlock().getNonce(), startTime, endTime));
                break;

            default: // Invalid action
                responseJson = gson.toJson(new ResponseMessage(
                        "error", "Invalid action", null, -1, 0, 0, 0, 0, null, 0L, 0L));
                break;
        }
        return responseJson;
    }
}

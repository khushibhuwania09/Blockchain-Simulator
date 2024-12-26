/*
 * Khushi Bhuwania
 * kbhuwani@andrew.cmu.edu
 * */

package org.example;

/**
 * The SignedRequest class represents a signed request from a client to the server.
 * It includes the transaction request, digital signature, public key components,
 * and a unique client identifier for verification purposes.
 */
public class SignedRequest {
    /* The request message containing the transaction details */
    private RequestMessage transactionRequest;

    /* The digital signature of the request, used to verify authenticity */
    private String digitalSignature;

    /* The public key exponent used for verifying the signature */
    private String publicKeyExponent;

    /* The public key modulus used for verifying the signature */
    private String publicKeyModulus;

    /* The identifier of the client, derived from the client's public key */
    private String clientIdentifier;

    /**
     * Constructor for SignedRequest.
     *
     * @param transactionRequest The request message to be signed.
     * @param digitalSignature The digital signature of the request.
     * @param publicKeyExponent The public key exponent used for signature verification.
     * @param publicKeyModulus The public key modulus used for signature verification.
     * @param clientIdentifier The unique identifier of the client.
     */
    public SignedRequest(RequestMessage transactionRequest, String digitalSignature,
                         String publicKeyExponent, String publicKeyModulus,
                         String clientIdentifier) {
        this.transactionRequest = transactionRequest; // Assign the transaction request
        this.digitalSignature = digitalSignature; // Assign the digital signature
        this.publicKeyExponent = publicKeyExponent; // Assign the public key exponent
        this.publicKeyModulus = publicKeyModulus; // Assign the public key modulus
        this.clientIdentifier = clientIdentifier; // Assign the client identifier
    }

    // Getter for the transaction request
    public RequestMessage getTransactionRequest() {
        return transactionRequest; // Return the transaction request
    }

    // Getter for the digital signature
    public String getDigitalSignature() {
        return digitalSignature; // Return the digital signature
    }

    // Getter for the public key exponent
    public String getPublicKeyExponent() {
        return publicKeyExponent; // Return the public key exponent
    }

    // Getter for the public key modulus
    public String getPublicKeyModulus() {
        return publicKeyModulus; // Return the public key modulus
    }

    // Getter for the client identifier
    public String getClientIdentifier() {
        return clientIdentifier; // Return the client identifier
    }

    /**
     * Override the toString method for printing the SignedRequest.
     *
     * @return A string representation of the SignedRequest object.
     */
    @Override
    public String toString() {
        return "SignedRequest{" +
                "transactionRequest=" + transactionRequest + // Print the transaction request
                ", digitalSignature='" + digitalSignature + '\'' + // Print the digital signature
                ", publicKeyExponent='" + publicKeyExponent + '\'' + // Print the public key exponent
                ", publicKeyModulus='" + publicKeyModulus + '\'' + // Print the public key modulus
                ", clientIdentifier='" + clientIdentifier + '\'' + // Print the client identifier
                '}';
    }
}

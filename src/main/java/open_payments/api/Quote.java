package open_payments.api;

public class Quote {
    /**
     * The URL identifying the quote.
     */
    String id;

    /**
     * The URL of the wallet address from which this quote’s payment would be sent.
     */
    String walletAddress;

    /**
     * The URL of the incoming payment that the quote is created for.
     */
    String receiver;

    /**
     * The total amount that should be received by the receiver when the corresponding outgoing payment has been paid.
     */
    Amount receiveAmount;

    /**
     * The total amount that should be deducted from the sender’s account when the corresponding outgoing payment has been paid.
     */
    Amount debitAmount;

    /**
     * Must be 'ilp'
     */
    String method;

    /**
     * The date and time when the calculated debitAmount is no longer valid.
     */
    String expiresAt;

    /**
     * The date and time when the quote was created.
     */
    String createdAt;
}

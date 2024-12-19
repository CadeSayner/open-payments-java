package open_payments.api;

public class OutgoingPayment {
    /**
     * The URL identifying the outgoing payment.
     */
    String id;

    /**
     * The URL of the wallet address from which this payment is sent.
     */
    String walletAddress;

    /**
     * The URL of the quote defining this payment’s amounts.
     */
    String quoteId;

    /**
     * Describes whether the payment failed to send its full amount.
     */
    boolean failed;

    /**
     * The URL of the incoming payment that is being paid.
     */
    String receiver;

    /**
     * The total amount that should be received by the receiver when this outgoing payment has been paid.
     */
    Amount receiveAmount;

    /**
     * The total amount that should be deducted from the sender’s account when this outgoing payment has been paid.
     */
    Amount debitAmount;

    /**
     * The total amount that has been sent under this outgoing payment.
     */
    Amount sentAmount;

    /**
     * The total amount successfully deducted from the sender’s account using the current outgoing payment grant.
     */
    Amount grantSentDebitAmount;

    /**
     * The total amount successfully received (by all receivers) using the current outgoing payment grant.
     */
    Amount grantSpentReceiveAmount;

    /**
     * Additional metadata associated with the outgoing payment. (Optional)
     */
    Object metaData;

    /**
     * The date and time when the outgoing payment was created.
     */
    String createdAt;

    /**
     * The date and time when the outgoing payment was updated.
     */
    String updatedAt; 
}

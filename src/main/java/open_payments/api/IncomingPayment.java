package open_payments.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IncomingPayment{
    /**
     * The URL identifying the incoming payment.
     */
    String id;

    /**
     * URL of a wallet address hosted by a Rafiki instance.
     */
    String walletAddress;

    /**
     * Describes whether the incoming payment has completed receiving fund.
     */
    boolean completed;

    /**
     * The maximum amount that should be paid into the wallet address under this incoming payment.
     */
    Amount incomingAmount;

    /**
     * The total amount that has been paid into the wallet address under this incoming payment.
     */
    Amount receivedAmount;

    /**
     * The date and time when payments under this incoming payment will no longer be accepted.
     */
    String expiresAt;

    /**
     * Additional metadata associated with the incoming payment. (Optional)
     */
    Object metaData;

    /**
     * The date and time when the incoming payment was created.
     */
    String createdAt;

    /**
     * The date and time when the incoming payment was updated.
     */
    String updatedAt;

    /**
     * The list of payment methods supported by this incoming payment.
     */
    Method[] methods;

    private class Method{
        String type;
        String ilpAddress;
        String sharedSecret;
    }
    
    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}

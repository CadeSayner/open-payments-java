package open_payments.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IncomingPayment{
    /**
     * The URL identifying the incoming payment.
     */
    public String id;

    /**
     * URL of a wallet address hosted by a Rafiki instance.
     */
    public String walletAddress;

    /**
     * Describes whether the incoming payment has completed receiving fund.
     */
    public boolean completed;

    /**
     * The maximum amount that should be paid into the wallet address under this incoming payment.
     */
    public Amount incomingAmount;

    /**
     * The total amount that has been paid into the wallet address under this incoming payment.
     */
    public Amount receivedAmount;

    /**
     * The date and time when payments under this incoming payment will no longer be accepted.
     */
    public String expiresAt;

    /**
     * Additional metadata associated with the incoming payment. (Optional)
     */
    public Object metaData;

    /**
     * The date and time when the incoming payment was created.
     */
    public String createdAt;

    /**
     * The date and time when the incoming payment was updated.
     */
    public String updatedAt;

    /**
     * The list of payment methods supported by this incoming payment.
     */
    public Method[] methods;

    public class Method{
        public String type;
        public String ilpAddress;
        public String sharedSecret;
    }
    
    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}

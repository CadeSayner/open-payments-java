package open_payments.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IncomingPaymentRequest {

    /** URL of a wallet address hosted by a Rafiki instance.*/
    String walletAddress;
    /** The maximum amount that should be paid into the wallet address under this incoming payment. */
    IncomingAmount incomingAmount; 
    /** The date and time when payments into the incoming payment must no longer be accepted. */
    String expiresAt;

    IncomingPaymentRequest(WalletAddress receivingWallet, int amount){
        this.walletAddress = receivingWallet.id;
        this.incomingAmount = new IncomingAmount(amount, receivingWallet.assetCode, Integer.parseInt(receivingWallet.assetScale));
    }
    
    IncomingPaymentRequest(WalletAddress receivingWallet, int amount, String expiresAt)
    {
        this.walletAddress = receivingWallet.id;
        this.incomingAmount = new IncomingAmount(amount, receivingWallet.assetCode, Integer.parseInt(receivingWallet.assetScale));
        this.expiresAt = expiresAt;
    }

    private class IncomingAmount{
        String assetCode;
        int assetScale;
        String value;
        IncomingAmount(int value, String assetCode, int assetScale){
            this.value = Integer.toString(value);
            this.assetCode = assetCode;
            this.assetScale = assetScale;
        }
    }
    
    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}

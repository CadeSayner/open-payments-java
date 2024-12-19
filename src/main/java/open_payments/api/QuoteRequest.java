package open_payments.api;

public class QuoteRequest {
    String walletAddress;
    String receiver;
    String method;
    Amount receiveAmount;
    Amount debitAmount;

    QuoteRequest(String walletAddress, String receiver){
        this.walletAddress = walletAddress;
        this.receiver = receiver;
        this.method = "ilp";
    }

    QuoteRequest(String walletAddress, String receiver, Amount receiveAmount){
        this.walletAddress = walletAddress;
        this.receiver = receiver;
        this.method = "ilp";
        this.receiveAmount = receiveAmount;
    }

}

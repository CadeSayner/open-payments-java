package open_payments.api;

public class OutgoingPaymentRequest {
    String walletAddress;
    String quoteId;
    String incomingPayment;
    Amount debitAmount;
    Object metadata;

    OutgoingPaymentRequest(WalletAddress walletAddress, Quote quote){
        this.walletAddress = walletAddress.id;
        this.quoteId = quote.id;
    }

    OutgoingPaymentRequest(IncomingPayment incomingPayment, Amount debitAmount){
        this.incomingPayment = incomingPayment.id;
        this.debitAmount = debitAmount;
    }



}

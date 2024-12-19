package open_payments.api;

public class Limit {
   String receiver;
   Amount debitAmount;
   Amount receiveAmount;
   String interval;
   
   public Limit(Amount debitAmount){
    this.debitAmount = debitAmount; 
   }
   
   public Limit(Amount debitAmount, Amount receiveAmount){
    this.debitAmount = debitAmount;
    this.receiveAmount = receiveAmount;
   }

   public Limit(Amount debitAmount, Amount receiveAmount, String interval){
        this.debitAmount = debitAmount;
        this.receiveAmount = receiveAmount;
        this.interval = interval;
   }

   public Limit(String receiver, Amount debitAmount, Amount receiveAmount, String interval){
        this.receiver = receiver;
        this.debitAmount = debitAmount;
        this.receiveAmount = receiveAmount;
        this.interval = interval;
   }

}

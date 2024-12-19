package open_payments.api;

public class OutgoingPaymentAccess extends Access{
   Limit limits;
   public OutgoingPaymentAccess(String[] actions, String identifier){
        super("outgoing-payment", actions);
        this.identifier = identifier;
   }

   public OutgoingPaymentAccess(String[] actions, String identifier, Limit limit){
        super("outgoing-payment", actions);
        this.identifier = identifier;
        this.limits = limit;
   }
}

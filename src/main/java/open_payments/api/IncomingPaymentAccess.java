package open_payments.api;

public class IncomingPaymentAccess extends Access{
    public IncomingPaymentAccess(String[] actions, String identifier){
        // TODO validate the actions properly
        super("incoming-payment", actions);
        this.identifier = identifier;
    }

    public IncomingPaymentAccess(String[] actions){
        super("incoming-payment", actions);
        this.identifier = null;
    }
}

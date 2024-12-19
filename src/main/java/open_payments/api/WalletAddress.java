package open_payments.api;

public class WalletAddress {

    /**
     * The URL identifying the wallet address.
     */
    String id;

    /**
     * A public name for the account. This should be set by the account holder with their provider to provide a hint to counterparties as to the identity of the account holder.
     */
    String publicName;

    /**
     * The assetCode is a code that indicates the underlying asset. This SHOULD be an ISO4217 currency code.
     */
    String assetCode;

    /**
     * The scale of amounts denoted in the corresponding asset code.
     */
    String assetScale;

    /**
     * The URL of the authorization server endpoint for getting grants and access tokens for this wallet address.
     */
    String authServer;

    /**
     * The URL of the resource server endpoint for performing Open Payments with this wallet address.
     */
    String resourceServer;
}

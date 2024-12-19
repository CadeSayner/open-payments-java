package open_payments.api;

public class GrantRequest {
    AccessToken access_token;

    /**
     * Wallet address of the client instance that is making this request.

        When sending a non-continuation request to the AS, the client instance MUST identify itself by including the client field of the request and by signing the request.

        A JSON Web Key Set document, including the public key that the client instance will use to protect this request and any continuation requests at the AS and any user-facing information about the client instance used in interactions, MUST be available at the wallet address + /jwks.json url.

        If sending a grant initiation request that requires RO interaction, the wallet address MUST serve necessary client display information.
    */
    String client;


    /**
     * The client instance declares the parameters for interaction methods that it can support using the interact field.
     */
    Interaction interact;

    public GrantRequest(Access[] accesses, String client){
        this.access_token = new AccessToken(accesses);
        this.client = client;
    }

    public GrantRequest(Access[] accesses, String client, Interaction interaction){
        this.access_token = new AccessToken(accesses);
        this.client = client;
        this.interact = interaction;
    }

    private class AccessToken{
        @SuppressWarnings("unused")
        /**
         * A description of the rights associated with this access token.
         */
        Access[] access;

        AccessToken(Access[] accesses){
            this.access = accesses;
        }
    }
}

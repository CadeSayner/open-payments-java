package open_payments.api;
import java.util.HashMap;
import java.util.Scanner;
import open_payments.http_requests.GetRequest;
import open_payments.http_requests.PostRequest;
import open_payments.http_utils.HeaderUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AuthenticatedClient {
    HeaderUtils headerFactory;
    private Gson gson = new GsonBuilder().create();
    private String walletAddress;

    /**
     * Constructs an {@code AuthenticatedClient} instance with the specified parameters.
     * 
     * @param url        The wallet address URL to associate with this client.
     * @param pk_filename The filename of the private key used for signing requests.
     * @param keyId      The key ID associated with the private key.
     */
    public AuthenticatedClient(String url, String pk_filename, String keyId){
        this.headerFactory = new HeaderUtils(pk_filename, keyId);
        this.walletAddress = url;
    }

    /**
     * Retrieves a wallet address from the specified URL by sending a GET request.
     * 
     * @param url the endpoint URL of the Wallet Address Server to fetch the wallet address.
     *            This should point to a valid resource as defined in the Open Payments API.
     * @return a {@code WalletAddress} object containing the wallet address details if the request is successful,
     *         or {@code null} if an exception occurs during the request or response processing.
     * 
     * @throws IllegalArgumentException if the URL is null or invalid.
     * 
     * Example usage:
     * <pre>
     * {@code
     * String url = "https://wallet-address-server.example.com/api/v1/wallet-address";
     * WalletAddress walletAddress = getWalletAddress(url);
     * 
     * if (walletAddress != null) {
     *     System.out.println("Wallet Address ID: " + walletAddress.getId());
     * } else {
     *     System.out.println("Failed to retrieve the wallet address.");
     * }
     * }
     * </pre>
     * 
     * Notes:
     * - The method uses the {@code headerFactory} to generate unsigned HTTP headers for the request.
     * - The JSON response is deserialized into a {@code WalletAddress} object using the Gson library.
     * - Ensure that the provided {@code url} matches the Wallet Address Server API structure.
     * - Handle potential null values in the calling code to avoid {@code NullPointerException}.
     * 
     * @see <a href="https://openpayments.dev/apis/wallet-address-server/operations/get-wallet-address/">Wallet Address Server API - Get Wallet Address</a>
     */

    public WalletAddress getWalletAddress(String url){
        HashMap<String, String> headers = headerFactory.getUnsignedHeaders();
        GetRequest req = new GetRequest(url, headers);
        try{
            req.send();
            String rawResponse = req.getResponse();
            return gson.fromJson(rawResponse, WalletAddress.class);
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

/**
 * Requests a grant from the authorization server using the provided access requirements and interaction details.
 *
 * @param {Access[]} accesses - An array of access objects defining the permissions required by the client.
 * @param {String} authServerUrl - The URL of the authorization server's endpoint to send the grant request.
 * @param {String} redirectUrl - The URL to which the user will be redirected after completing the interaction.
 * @param {String} nonce - A unique value to associate the interaction with the grant request for security purposes.
 * @return {Grant} A {@code Grant} object containing the grant details if the request is successful, 
 *                 or {@code null} if an error occurs during the process.
 *
 * Example usage:
 * <pre>
 * {@code
 * Access[] accesses = { new Access("read"), new Access("write") };
 * String authServerUrl = "https://auth-server.example.com";
 * String redirectUrl = "https://client.example.com/callback";
 * String nonce = "unique-nonce-value";
 *
 * Grant grant = requestGrant(accesses, authServerUrl, redirectUrl, nonce);
 *
 * if (grant != null) {
 *     System.out.println("Grant received: " + grant.getAccessToken());
 * } else {
 *     System.out.println("Failed to request grant.");
 * }
 * }
 * </pre>
 *
 * Notes:
 * - The method constructs a `GrantRequest` object with the specified accesses, wallet address, and interaction details.
 * - The JSON payload is signed and sent as a POST request to the authorization server.
 * - The method replaces the reserved Java keyword `continue` with `continuation` in the response to allow parsing.
 * - Ensure the `authServerUrl` is valid and points to a compliant Open Payments authorization server.
 * - Handle potential null values in the calling code to avoid {@code NullPointerException}.
 *
 * @see <a href="https://openpayments.dev/apis/auth-server/operations/post-request/">Authorization Server API - POST /request</a>
 */
    public Grant requestGrant(Access[] accesses, String authServerUrl, String redirectUrl, String nonce){
        try{
            String[] start = {"redirect"};
            Interaction interaction = new Interaction(start, new Finish(redirectUrl, nonce));
            GrantRequest grantRequest = new GrantRequest(accesses, this.walletAddress, interaction);
            String body = gson.toJson(grantRequest).strip();
            PostRequest post = new PostRequest(authServerUrl+'/', body);
            headerFactory.addDefaultHeaders(post.getConnection(), body); 
            headerFactory.signRequest(post.getConnection(), body, null);
            post.send();
            String response = post.getResponse();
            // since continue is reserved in java
            response = response.replace("continue", "continuation");
            return (Grant)gson.fromJson(response, Grant.class);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Requests a grant from the authorization server using the provided access requirements.
     *
     * @param accesses      An array of {@code Access} objects defining the permissions required by the client.
     * @param authServerUrl The URL of the authorization server's endpoint to send the grant request.
     * @return A {@code Grant} object containing the grant details if the request is successful, 
     *         or {@code null} if an error occurs during the process.
     *
     * Example usage:
     * <pre>
     * {@code
     * Access[] accesses = { new Access("read"), new Access("write") };
     * String authServerUrl = "https://auth-server.example.com";
     *
     * Grant grant = requestGrant(accesses, authServerUrl);
     *
     * if (grant != null) {
     *     System.out.println("Grant received: " + grant.getAccessToken());
     * } else {
     *     System.out.println("Failed to request grant.");
     * }
     * }
     * </pre>
     *
     * Notes:
     * - The method constructs a {@code GrantRequest} object with the specified accesses and wallet address.
     * - The JSON payload is signed and sent as a POST request to the authorization server.
     * - The method replaces the reserved Java keyword {@code continue} with {@code continuation} in the response to allow parsing.
     * - Ensure the {@code authServerUrl} is valid and points to a compliant Open Payments authorization server.
     * - Handle potential {@code null} values in the calling code to avoid {@code NullPointerException}.
     *
     * @see <a href="https://openpayments.dev/apis/auth-server/operations/post-request/">Authorization Server API - POST /request</a>
    */
    public Grant requestGrant(Access[] accesses, String authServerUrl){
        try{
            // move this to the grant request constructor rather
            String[] start = {"redirect"};
            Interaction interaction = new Interaction(start);
            GrantRequest grantRequest = new GrantRequest(accesses, this.walletAddress, interaction);
            String body = gson.toJson(grantRequest).strip();
            PostRequest post = new PostRequest(authServerUrl+'/', body);
            headerFactory.addDefaultHeaders(post.getConnection(), body); 
            headerFactory.signRequest(post.getConnection(), body, null);
            post.send();
            String response = post.getResponse();
            // since continue is reserved in java
            response = response.replace("continue", "continuation");
            return (Grant)gson.fromJson(response, Grant.class);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 
     * @param receivingWallet URL of a wallet address hosted by a Rafiki instance.
     * @param accessToken GNAP Access Token associated with a Grant with permissions necessary to create an Incoming Payment at the RS.
     * @param amount The amount to be transfered.
     * @return An {@code IncomingPayment} object.
     */
    public IncomingPayment createIncomingPayment(WalletAddress receivingWallet, String accessToken, int amount){
        try{
            IncomingPaymentRequest incomingPaymentRequest = new IncomingPaymentRequest(receivingWallet, amount);
            String req_body = gson.toJson(incomingPaymentRequest).strip();
            PostRequest post = new PostRequest(receivingWallet.resourceServer + "/incoming-payments", req_body);
            headerFactory.addDefaultHeaders(post.getConnection(), req_body);
            headerFactory.addAuthHeader(post.getConnection(), accessToken);
            headerFactory.signRequest(post.getConnection(), req_body, accessToken);
            post.send();
            String response = post.getResponse();
            return (IncomingPayment)gson.fromJson(response, IncomingPayment.class);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * @param walletAddress URL of a wallet address hosted by a Rafiki instance.
     * @param receiver The URL of the incoming payment that is being paid.
     * @param quoteGrant A Grant with Quote creation privileges.
     * @return A {@code Quote} object.
     */
    public Quote createQuote(WalletAddress walletAddress, String receiver, Grant quoteGrant){
        try{
            QuoteRequest quoteRequest = new QuoteRequest(walletAddress.id, receiver);
            String req_body = gson.toJson(quoteRequest).strip();
            PostRequest post = new PostRequest(walletAddress.resourceServer + "/quotes", req_body);
            headerFactory.addDefaultHeaders(post.getConnection(), req_body);
            headerFactory.addAuthHeader(post.getConnection(), quoteGrant.access_token.value);
            headerFactory.signRequest(post.getConnection(), req_body, quoteGrant.access_token.value);
            post.send();
            String response = post.getResponse();
            return (Quote)gson.fromJson(response, Quote.class);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Requests a grant for creating an incoming payment using the provided wallet address.
     *
     * <p>This method generates an access request for creating incoming payments and sends it to the authorization server.</p>
     *
     * @param walletAddress The wallet address for which the grant is requested.
     * @return A {@code Grant} object containing the grant details if the request is successful, or {@code null} if an error occurs.
     */
    public Grant requestIncomingPaymentGrant(WalletAddress walletAddress){
        String[] actions = {"create"};
        Access[] accesses = {new IncomingPaymentAccess(actions)};
        return requestGrant(accesses, walletAddress.authServer);
    }

    /**
     * Requests a grant for creating a quote using the provided wallet address.
     *
     * <p>This method generates an access request for creating quotes and sends it to the authorization server.</p>
     *
     * @param walletAddress The wallet address for which the grant is requested.
     * @return A {@code Grant} object containing the grant details if the request is successful, or {@code null} if an error occurs.
     */
    public Grant requestQuoteGrant(WalletAddress walletAddress){
        String[] actions = {"create"};
        Access[] accesses = {new QuoteAccess(actions)};
        return requestGrant(accesses, walletAddress.authServer);
    }

    /**
     * Requests a grant for creating an outgoing payment using the provided wallet address.
     *
     * <p>This method generates an access request for creating outgoing payments and sends it to the authorization server.</p>
     *
     * @param walletAddress The wallet address for which the grant is requested.
     * @return A {@code Grant} object containing the grant details if the request is successful, or {@code null} if an error occurs.
     */
    public Grant requestOutgoingPaymentGrant(WalletAddress walletAddress){
        String[] actions = {"create"};
        Access[] accesses = {new OutgoingPaymentAccess(actions, walletAddress.id)};
        return requestGrant(accesses, walletAddress.authServer);
    }

    /**
     * Requests a grant for creating an outgoing payment with additional redirection and nonce parameters.
     *
     * <p>This method generates an access request for creating outgoing payments, including redirection and nonce details, 
     * and sends it to the authorization server.</p>
     *
     * @param walletAddress The wallet address for which the grant is requested.
     * @param redirectUrl The URL to which the user will be redirected after the interaction.
     * @param nonce A unique value associated with the request for security purposes.
     * @return A {@code Grant} object containing the grant details if the request is successful, or {@code null} if an error occurs.
     */
    public Grant requestOutgoingPaymentGrant(WalletAddress walletAddress, String redirectUrl, String nonce){
        String[] actions = {"create"};
        Access[] accesses = {new OutgoingPaymentAccess(actions, walletAddress.id)};
        return requestGrant(accesses, walletAddress.authServer , redirectUrl, nonce);
    }


    /**
     * Creates an outgoing payment by sending a request to the resource server.
     *
     * <p>This method constructs an outgoing payment request using the provided wallet address, quote, and grant, 
     * and sends it to the resource server to initiate the payment.</p>
     *
     * @param walletAddress The wallet address associated with the outgoing payment.
     * @param quote The quote object containing payment details.
     * @param grant The authorization grant to be used for the request.
     * @return An {@code OutgoingPayment} object containing the payment details if successful, 
     *         or {@code null} if an error occurs during the process.
     */
    public OutgoingPayment createOutgoingPayment(WalletAddress walletAddress, Quote quote, Grant grant){
        try{
            OutgoingPaymentRequest request = new OutgoingPaymentRequest(walletAddress, quote);
            String req_body = gson.toJson(request).strip();
            PostRequest post = new PostRequest(walletAddress.resourceServer + "/outgoing-payments", req_body);
            headerFactory.addDefaultHeaders(post.getConnection(), req_body);
            headerFactory.addAuthHeader(post.getConnection(), grant.access_token.value);
            headerFactory.signRequest(post.getConnection(),req_body, grant.access_token.value);
            post.send();
            String response = post.getResponse();
            return (OutgoingPayment)gson.fromJson(response, OutgoingPayment.class);

        }catch(Exception e){
            return null;
        }
    }

    /**
     * Continues the grant process by sending a request to the continue URI.
     *
     * <p>This method uses the continuation information from the provided {@code Grant} object to send a request 
     * to the authorization server, potentially completing the grant and returning the updated {@code Grant} object.</p>
     *
     * @param grant The {@code Grant} object containing continuation information.
     * @return A {@code Grant} object, 
     *         or {@code null} if an error occurs during the process.
     */
    public Grant continueGrant(Grant grant){
        try{
            PostRequest post = new PostRequest(grant.continuation.uri.replace("continuation", "continue"), "");
            headerFactory.addDefaultHeaders(post.getConnection());
            headerFactory.addAuthHeader(post.getConnection(), grant.continuation.access_token.value);
            headerFactory.signRequest(post.getConnection(), null, grant.continuation.access_token.value);
            post.send();
            String response = post.getResponse();
            return (Grant)gson.fromJson(response, Grant.class);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Continues the grant process by sending a request to the continue URI with specified parameters.
     *
     * <p>This method sends a continuation request to the authorization server using the provided continue URI.
     *
     * @param grantContinuationUri The URI to which the continuation request is sent.
     * @param continuationAccessToken The access token used to authorize the continuation request.
     * @param interactionReference A reference to the interaction that is being continued.
     * @return A {@code Grant} object containing the updated grant details if successful, 
     *         or {@code null} if an error occurs during the process.
     */
    public Grant continueGrant(String grantContinuationUri, String continuationAccessToken, String interactionReference){
        try{
            String body = String.format("{\"interact_ref\": %s}", interactionReference);
            PostRequest post = new PostRequest(grantContinuationUri.replace("continuation", "continue"), body);
            headerFactory.addDefaultHeaders(post.getConnection());
            headerFactory.addAuthHeader(post.getConnection(), continuationAccessToken);
            headerFactory.signRequest(post.getConnection(), body, continuationAccessToken);
            post.send();
            String response = post.getResponse();
            return (Grant)gson.fromJson(response, Grant.class);
        }catch(Exception e){
            return null;
        }
    }

    public static void main(String[] args) throws Exception{
        AuthenticatedClient client = new AuthenticatedClient("https://ilp.interledger-test.dev/cadesayner", "private.key", "cc8de9e6-1160-4038-ab9d-38c984db39a0");
        final String receivingAddress = "https://ilp.interledger-test.dev/spca";
        final String sendingAddress = "https://ilp.interledger-test.dev/kaylasayner";
        WalletAddress receivingWalletAddress = client.getWalletAddress(receivingAddress);
        WalletAddress sendingWalletAddress = client.getWalletAddress(sendingAddress);

        Grant incomingPaymentGrant = client.requestIncomingPaymentGrant(receivingWalletAddress);
        Grant quoteGrant = client.requestQuoteGrant(sendingWalletAddress);
        Grant outgoingPaymentGrant = client.requestOutgoingPaymentGrant(sendingWalletAddress, "http://localhost:5000", "202");
        System.out.println(incomingPaymentGrant);
        // create incoming payment 
        IncomingPayment incomingPayment = client.createIncomingPayment(receivingWalletAddress,incomingPaymentGrant.access_token.value, 200);

        //TODO: make it so that the create quote takes in an incomingPayment directly
        Quote quote = client.createQuote(sendingWalletAddress, incomingPayment.id, quoteGrant);
        System.out.println(outgoingPaymentGrant.interact.redirect);
        Scanner s = new Scanner(System.in);
        String x = s.next();

        // Grant continuedGrant = client.continueGrant(outgoingPaymentGrant);
        // System.out.println(continuedGrant.access_token.value);

        // OutgoingPayment outgoingPayment = client.createOutgoingPayment(sendingWalletAddress, quote, continuedGrant);
        // if(outgoingPayment.failed == false){
        //     System.out.println("Payment successful");
        // }
    }
}

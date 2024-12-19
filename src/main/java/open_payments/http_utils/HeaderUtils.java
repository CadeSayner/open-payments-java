package open_payments.http_utils;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class HeaderUtils {
    PrivateKey key;
    String keyID;
    
    public HeaderUtils(String pk_filename, String keyId){
        try{
            this.key = getKey(pk_filename);
            this.keyID = keyId;
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private PrivateKey getKey(String filePath) throws Exception{
          PemReader pemReader = new PemReader(new FileReader(filePath));
          PemObject pemObject = pemReader.readPemObject();
          byte[] privateKeyBytes = pemObject.getContent();
          pemReader.close(); 
          // Create a PrivateKey object
          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("Ed25519");
          PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
          return privateKey;
   }

    public HashMap<String, String> getUnsignedHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public void addDefaultHeaders(HttpURLConnection connection, String body) throws Exception{
        int contentLength = body.getBytes("UTF-8").length;
        try{
          connection.setRequestProperty("accept", "application/json");
          connection.setRequestProperty("content-type", "application/json");
          connection.setRequestProperty("content-digest", "sha-512=:" + hash(body) + ":");
          connection.setRequestProperty("content-length", Integer.toString(contentLength));
        }catch(Exception e){
          throw new Exception("Failed to add headers" + e);
        }
    }

    public void addDefaultHeaders(HttpURLConnection connection) throws Exception{
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("content-type", "application/json");
    }

    public void signRequest(HttpURLConnection connection, String body, String authToken)throws Exception{
        String[] coveredArr = {"content-digest", "content-length", "content-type"};
        String[] coveredArrNoBody = {"content-type"};
        if(body == null){
            coveredArr = coveredArrNoBody;
        }
        ArrayList<String> coveredComponents = new ArrayList<String>(Arrays.asList(coveredArr));
        String signatureBase = getSignatureBase(connection, coveredComponents, body, authToken);
        String signature = getSignature(signatureBase);
        connection.setRequestProperty("signature", String.format("sig1=:%s:", signature));
        String[] sig_base_split = signatureBase.split(":");
        connection.setRequestProperty("signature-input", "sig1=" + sig_base_split[sig_base_split.length -1].strip());
    }

    private String getSignature(String signatureBase) throws Exception{
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(this.key);
        signature.update(signatureBase.getBytes("UTF-8"));
        byte[] signedBytes = signature.sign();
        String final_signature = Base64.getEncoder().encodeToString(signedBytes);
        return final_signature;
    }

    public String getSignatureBase(HttpURLConnection connection, ArrayList<String> coveredComponents, String body, String authToken){
        String signatureBase = "";
        String signatureInput = "(\"@method\" \"@target-uri\" ";
        signatureBase += "\"@method\": " + connection.getRequestMethod() + "\n";
        signatureBase += "\"@target-uri\": " + connection.getURL() + "\n";

        if(authToken != null){
            signatureBase += "\"authorization\": " + "GNAP " + authToken + '\n';
            signatureInput += ( String.format("\"%s\" ", "authorization"));
        }

        for (String component : coveredComponents) {
            String prop = component == "content-length" ? Integer.toString(body.getBytes().length) : connection.getRequestProperty(component);
            signatureBase += String.format("\"%s\": ", component) + prop + "\n";
            signatureInput += ( String.format("\"%s\" ", component));
        }
        signatureInput = signatureInput.strip() + ")";
        signatureInput += (";keyid=\"" + this.keyID + "\";");
        signatureInput += ("created=" + Instant.now().getEpochSecond());
        signatureBase += ("\"@signature-params\": " + signatureInput.strip());
        return signatureBase;
    }

    public static String hash(String body) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(body.getBytes("UTF-8"));
        String base64Hash = Base64.getEncoder().encodeToString(hashBytes);
        return base64Hash;
    }

    public void addAuthHeader(HttpURLConnection connection, String token){
          connection.setRequestProperty("authorization", "GNAP " + token);
    }
}

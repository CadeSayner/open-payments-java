package open_payments.http_requests;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class GetRequest {
    String urlString;
    HashMap<String,String> headers;
    String response;

    public GetRequest(String urlString, HashMap<String, String> headers){
        this.urlString = urlString;
        this.headers = headers;
    }

    public String getResponse(){
        return this.response;
    }
    public void send() throws Exception {
        // Prepare the URL
        URL url = new URL(this.urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Set headers
        for (String header : headers.keySet()) {
            connection.setRequestProperty(header, headers.get(header));
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Read the response
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();
            this.response = responseBuffer.toString();

        } else {
            this.response = null;
            throw new Exception("GET Request failed " + responseCode);
        }
    }

    @Override
    public String toString() {
        String s = "Url: %s\nHeaders: %s";
        return String.format(s, this.urlString, this.headers.toString());
    }
}


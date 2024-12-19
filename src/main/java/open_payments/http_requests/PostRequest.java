package open_payments.http_requests;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class PostRequest {
    String urlString;
    HashMap<String, String> headers;
    String body;
    String response;
    HttpURLConnection con;

    public PostRequest(String urlString,  String body) throws Exception{
        this.urlString = urlString;
        this.body = body;
        URL url = new URL(urlString);
        this.con = (HttpURLConnection) url.openConnection();
        this.con.setRequestMethod("POST");
        this.con.setDoOutput(true);
    }

    public HttpURLConnection getConnection(){
        return this.con;
    }
            
    public void send() throws Exception{
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = this.body.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }
        // Get the response code
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED ) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
            this.response = response.toString();
            
		} else {
            if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
			System.out.println("POST request did not work.");
            this.response = null; // represents a failed request
            throw new Exception("POST request failed");
		}
    }

    public String getResponse(){
        return this.response;
    }

    @Override
    public String toString() {
        String s = "Url: %s\nHeaders: %s\nBody: %s";
        return String.format(s, this.urlString, this.headers.toString(), this.body);
    }
}

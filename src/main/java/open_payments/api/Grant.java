package open_payments.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Grant {
    /**
     * If the AS determines that the request can be continued with additional requests, it responds with the continue field.
     */
    public Interact interact;
    /**
     * A single access token or set of access tokens that the client instance can use to call the RS on behalf of the RO.
     */
    public AccessToken access_token;
    /**
     * If the AS determines that the request can be continued with additional requests, it responds with the continue field.
     */
    public Continue continuation;
    
    public class AccessToken{
        public String value;
        public String manage;
        public int expires_in;
        public Access[] access;

        public String toString(){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(this);
        }
    }

    /**
     * If the AS determines that the request can be continued with additional requests, it responds with the continue field.
     */
    public class Continue{
        /**
         * A single access token or set of access tokens that the client instance can use to call the RS on behalf of the RO.
         */
        public AccessToken access_token;
        /**
         * The URI at which the client instance can make continuation requests.
         */
        public String uri;
        /**
         * The amount of time in integer seconds the client instance MUST wait after receiving this request continuation response and calling the continuation URI.
         */
        public int wait;

        public String toString(){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(this);
        }
    }

    public class Interact{
        /**
         * The URI to direct the end user to.
         */
        public String redirect;

        /**
         * Unique key to secure the callback.
         */
        public String finish;
        
        public String toString(){
            Gson gson = new GsonBuilder().create();
            return gson.toJson(this);
        }
    }

    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}



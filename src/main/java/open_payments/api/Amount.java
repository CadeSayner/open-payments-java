package open_payments.api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Amount {
    /**
     * The value is an unsigned 64-bit integer amount, represented as a string.
     */
    String value;

    /**
     * The assetCode is a code that indicates the underlying asset. This SHOULD be an ISO4217 currency code.
     */
    String assetCode;

    /**
     * The scale of amounts denoted in the corresponding asset code. <255
     */
    String assetScale;
    
    /**
     * Constructs an {@code Amount} object with the specified value, asset code, and asset scale.
     * 
     * @param value      The unsigned 64-bit integer amount, provided as an {@code int}. 
     *                   It will be converted to a string representation internally.
     * @param assetCode  The code indicating the underlying asset. This should follow the ISO4217 currency code standard.
     * @param assetScale The scale of amounts for the corresponding asset code, represented as a string. 
     *                   This value must be less than 255.
     */
    public Amount(int value, String assetCode, String assetScale){
        this.value = Integer.toString(value);
        this.assetCode = assetCode;
        this.assetScale = assetScale;
    }

    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}

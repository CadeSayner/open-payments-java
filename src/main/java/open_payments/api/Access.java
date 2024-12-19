package open_payments.api;

import java.util.Arrays;

public class Access {
    /**
     * The type of resource request as a string. This field defines which other fields are allowed in the request object.
     */
    String type;

    /**
     * Represents the types of actions the client instance will perform at the Resource Server (RS).
     * 
     * <p>The actions are specified as an array of unique strings. The allowed values include:
     * <ul>
     *   <li><code>create</code> - Indicates the client intends to create a resource.</li>
     *   <li><code>complete</code> - Indicates the client intends to complete an action or process.</li>
     *   <li><code>read</code> - Indicates the client intends to read a resource.</li>
     * <li><code>read-all</code> - Indicates the client intends to read all resources.</li>
    *   <li><code>list</code> - Indicates the client intends to list specific resources.</li>
    *   <li><code>list-all</code> - Indicates the client intends to list all resources.</li>
     * </ul>
     * </p>
     * 
     * <p>Each action in the array must be one of the allowed values, and no duplicate actions are permitted.</p>
     */
    String[] actions;

    /**
     * A string identifier indicating a specific resource at the RS.
     * */
    String identifier;

    Access(String type, String[] actions){
        this.type = type;
        this.actions = actions;
    }

    public String toString(){
        String formatString = "Type: %s\nActions: %s\nIdentifier: %s\n";
        return String.format(formatString, type, Arrays.toString(actions), identifier);
    }
}

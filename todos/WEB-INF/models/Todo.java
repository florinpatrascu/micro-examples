package models;

import org.javalite.activejdbc.Model;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013.02.17)
 */
public class Todo extends Model {

    public String toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("title", get("title"));
        json.put("order", getInteger("ord"));
        json.put("done", getBoolean("done"));
        return json.toString();
    }
}

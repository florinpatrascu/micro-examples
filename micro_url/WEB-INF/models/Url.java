package models;

import ca.simplegames.micro.Globals;
import controllers.utils.Base64;
import org.javalite.activejdbc.Model;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013.02.17)
 */
public class Url extends Model {

    public String toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        try {
            json.put("url", new String(Base64.decode(getString("url")
                    .getBytes(Charset.forName(Globals.UTF8)))));
        } catch (IOException e) {
            json.put("url", "decoding error, sorry");
        }
        json.put("created_at", getTimestamp("created_at"));
        return json.toString();
    }
}

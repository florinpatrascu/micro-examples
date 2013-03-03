package models;

import org.javalite.activejdbc.Model;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013.02.17)
 */
public class Customer extends Model {
    static {
        validatePresenceOf("username", "username");
        validatePresenceOf("password", "password");
        validatePresenceOf("name", "name");
        validateRegexpOf("email", "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
        validateEmailOf("email");
    }
}

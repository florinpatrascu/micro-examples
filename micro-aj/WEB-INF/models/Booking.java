package models;

import org.javalite.activejdbc.Model;
import org.joda.time.DateTime;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-24 1:43 PM)
 */
public class Booking extends Model {

    static {
        validatePresenceOf("credit_card", "credit_card_name");
        validateRange("credit_card_expiry_month", 0, 13);
        validateRange("credit_card_expiry_year", DateTime.now().getYear(), DateTime.now().getYear() + 4);
    }

    @Override
    protected void beforeValidation() {
        DateTime checkinDate = new DateTime(this.getDate("checkin_date"));
        DateTime checkoutDate = new DateTime(this.getDate("checkout_date"));
        if (checkoutDate.isEqual(checkinDate)) {
            final String errMsg = "can't checkout the same day";
            errors.put("checkout_date", errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        if (checkoutDate.isBefore(checkinDate)) {
            final String errMsg = "can't checkout before checking in";
            errors.put("checkout_date", errMsg);
            throw new IllegalArgumentException(errMsg);
        }
    }
}

package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.controllers.ControllerException;
import models.Booking;
import models.Customer;
import models.Hotel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * find the hotel based on the request parameter: 'id'
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013.02.24)
 */

public class HotelInfo implements Controller {
    protected static final Log log = LogFactory.getLog(HotelInfo.class);

    public void execute(MicroContext context, Map configuration) throws ControllerException {
        String id = StringUtils.defaultString(context.getRequest().getParameter("id"));

        if (!id.isEmpty()) {
            Hotel hotel = Hotel.findById(id);
            context.put("hotel", hotel);

            Map user = (Map) context.getRequest().getSession().getAttribute("user");
            if (user != null) {
                Customer customer = Customer.findById(user.get("id"));
                if (customer != null) {
                    List<Booking> reservations = customer.get(Booking.class, "hotel_id=?", hotel.getId());
                    context.put("reservations", reservations);
                }
            }
        }

        context.with("yyyyMMdd", DateTime.now().toString("yyyy-MM-dd"));
        context.with("month", DateTime.now().getMonthOfYear());
        context.with("year", DateTime.now().getYear());
    }
}

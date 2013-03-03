package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.Globals;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.utils.StringUtils;
import models.Booking;
import models.Customer;
import models.Hotel;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * collect the data from the booking order, validate some fields and save everything
 * in the session.
 */

public class BookingController implements Controller {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    static DateTimeFormatter fmt = DateTimeFormat.forPattern(YYYY_MM_DD);

    public void execute(MicroContext context, Map map) throws ControllerException, FileNotFoundException {
        DateTime today = DateTime.now();
        HttpServletRequest request = context.getRequest();

        Hotel hotel = (Hotel) context.get("hotel");
        final HttpSession session = request.getSession();
        Map user = (Map) session.getAttribute("user");

        if (user != null) {
            Customer customer = Customer.findById(user.get("id"));

            if (hotel != null) {

                Booking booking = Booking.create(
                        "customer_id", customer.getId(),
                        "hotel_id", hotel.getId(),
                        "checkin_date", fmt.parseDateTime(request.getParameter("checkinDate")).toDate(),
                        "checkout_date", fmt.parseDateTime(request.getParameter("checkoutDate")).toDate(),
                        "beds", Integer.parseInt(StringUtils.defaultString(request.getParameter("beds"), "1")),
                        "smoking", request.getParameter("smoking") != null && request.getParameter("smoking").equalsIgnoreCase("true"),
                        "credit_card", request.getParameter("creditCard"),
                        "credit_card_name", request.getParameter("creditCardName"),
                        "credit_card_expiry_month", Integer.parseInt(StringUtils.defaultString(request.getParameter("creditCardExpiryMonth"), "0")),
                        "credit_card_expiry_year", Integer.parseInt(StringUtils.defaultString(request.getParameter("creditCardExpiryYear"), "0"))
                );

                try {
                    booking.validate();
                    if (StringUtils.defaultString(request.getParameter("eh"),
                            Globals.EMPTY_STRING).equalsIgnoreCase("Ido")) {
                        booking.saveIt();
                    }
                } catch (Exception e) {
                    context.put("errors", booking.errors());
                    e.printStackTrace();
                    //context.setRedirect(String.format("%s/book.html?id=%s", request.getContextPath(), hotel.getId()), false);
                }

                Map bookingOrder = booking.toMap();
                bookingOrder.put("hotel", hotel.toMap());
                session.setAttribute("bookingOrder", bookingOrder);

                if (booking.getId() != null) {
                    context.setRedirect(String.format("%s/thanks.html", request.getContextPath()), false);
                } else {
                    context.put("booking", booking);
                    DateTime cIn = new DateTime(booking.get("checkin_date"));
                    DateTime cOut = new DateTime(booking.get("checkout_date"));
                    Duration stayDuration = new Duration(cIn, cOut);

                    context.put("checkinDate", cIn.toString(YYYY_MM_DD));
                    context.put("checkoutDate", cOut.toString(YYYY_MM_DD));
                    final long days = stayDuration.getStandardDays();
                    context.put("total", ((BigDecimal) hotel.get("price")).doubleValue() * days);
                    context.put("days", days);
                    context.put("orderDate", today);
                }
            } else {
                context.with("error", "Please select a hotel first!");
            }
        } else {
            context.with("error", "Please login first!");
        }
    }
}

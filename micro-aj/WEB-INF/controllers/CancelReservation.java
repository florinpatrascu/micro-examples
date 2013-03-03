package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import models.Booking;
import models.Customer;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * cancel a reservation
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-28 7:27 PM)
 */
public class CancelReservation implements Controller {
    public void execute(MicroContext context, Map configuration) throws ControllerException, FileNotFoundException {
        Map user = (Map) context.getRequest().getSession().getAttribute("user");
        ActivejdbcExtension aj = (ActivejdbcExtension) context.getSiteContext().get("activejdbc_m");

        Integer reservationId = Integer.parseInt(
                StringUtils.defaultIfEmpty(
                        context.getRequest().getParameter("id"), "-1"));

        if (reservationId > 0) {
            aj.before();

            try {
                if (user != null) {
                    Customer customer = Customer.findById(user.get("id"));
                    if (customer != null) {
                        List<Booking> reservations = customer.get(Booking.class, "id=?", reservationId);
                        for (Booking reservation : reservations) {
                            reservation.delete();
                        }
                        context.with("success", Boolean.TRUE);
                    }
                }
            } catch (Exception e) {
                throw new ControllerException(e.getMessage(), e);
            } finally {
                aj.after();
            }
        }

    }
}

package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.controllers.ControllerException;
import models.Customer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * simple login support, the username and the password must match to an existing db record
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013.02.24)
 */

public class Login implements Controller {
    protected static final Log log = LogFactory.getLog(Login.class);

    public void execute(MicroContext context, Map configuration) throws ControllerException {

        HttpServletRequest request = context.getRequest();
        String username = StringUtils.defaultString(request.getParameter("username"));
        String password = StringUtils.defaultString(request.getParameter("password"));

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            try {
                List customers = Customer.where("lower(username) = ? and lower(password) = ? ",
                        username, password).limit(1);
                if (customers.size() > 0) {
                    request.getSession().setAttribute("user", ((Customer) customers.get(0)).toMap());
                    final String hotelId = request.getParameter("id");

                    if (hotelId != null) {
                        context.setRedirect(String.format("%s/hotel.html?id=%s",
                                request.getContextPath(), hotelId), false);
                    } else {
                        context.setRedirect(String.format("%s/", request.getContextPath()), false);
                    }
                } else {
                    context.with("error", "Invalid credentials, sorry.");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
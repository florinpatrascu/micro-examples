package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.RedirectException;
import ca.simplegames.micro.controllers.ControllerException;
import models.Customer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-24 2:15 PM)
 */
public class Register implements Controller {
    protected static final Logger log = LoggerFactory.getLogger(Register.class);

    @SuppressWarnings("unchecked")
    public void execute(MicroContext context, Map config) throws ControllerException, FileNotFoundException {
        HttpServletRequest request = context.getRequest();

        try {
            String registration = StringUtils.defaultString(request.getParameter("registration"));

            if (StringUtils.isNotEmpty(registration)) {
                String username = StringUtils.defaultString(request.getParameter("username"));
                String name = StringUtils.defaultString(request.getParameter("name"));
                String password = StringUtils.defaultString(request.getParameter("password"));
                String verify = StringUtils.defaultString(request.getParameter("verify"));

                List users = Customer.where("username = ?", username);
                if (users != null && users.isEmpty()) {
                    if (password.equals(verify)) {

                        Customer user = Customer.createIt("username", username, "name", name, "password", password);
                        if (user != null) {
                            Map richUser = user.toMap();
                            richUser.put("welcome", Boolean.TRUE);
                            request.getSession().setAttribute("user", richUser);
                            context.setRedirect(request.getContextPath()+"/index.html", false);
                        }
                    } else {
                        context.with("error", "The two passwords don't match");
                    }
                } else {
                    context.with("error", String.format("Username %s already exists", username));
                }
            }
        } catch (Exception e) {
            if (!(e instanceof RedirectException)) {
                e.printStackTrace();
                context.with("error", String.format("%s", e.getMessage()));
            }
        }
    }
}

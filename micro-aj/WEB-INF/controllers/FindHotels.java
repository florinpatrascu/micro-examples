package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.controllers.ControllerException;
import models.Hotel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javalite.activejdbc.Paginator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-01-12)
 */
public class FindHotels implements Controller {
    protected static final Log log = LogFactory.getLog(FindHotels.class);

    public void execute(MicroContext context, Map configuration) throws ControllerException {
        HttpServletRequest request = context.getRequest();
        String searchString = StringUtils.defaultString(request.getParameter("search_string"));

        if (!searchString.isEmpty()) {
            int currentPage = Integer.parseInt(StringUtils.defaultIfEmpty(
                    request.getParameter("page"), "1"));
            // hotels = Hotel.findAll().limit(5);
            String pattern = '%' + searchString.toLowerCase().replace('*', '%') + '%';

            log.info("Searching for pattern: `" + pattern + "`");
            Paginator paginator = new Paginator(Hotel.class, 5, "lower(name) like ? or lower(city) like ?"
                    + " or lower(zip) like ? or lower(address) like ?",
                    pattern, pattern, pattern, pattern);

            context.put("hotels", paginator.getPage(currentPage));
            context.put("searchString", searchString);

            context.put("totalPages", paginator.pageCount());
            context.put("currentPage", currentPage);
            context.put("searchString", searchString);
        }
    }
}


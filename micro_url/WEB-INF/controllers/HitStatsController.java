package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.Globals;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.SiteContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import ca.simplegames.micro.utils.Assert;
import controllers.utils.Base64;
import controllers.utils.Bijective;
import models.Url;
import org.javalite.activejdbc.Base;
import org.joda.time.DateTime;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Calculate the number of hits per MONTH in a given YEAR for a specific URL
 * Using the H2 db synatx and functions, will be something like this:
 * <p/>
 * - select url_id, count(*) as total_hits from hits
 * where url_id = 3 and YEAR(created_at) = 2013 and MONTH(created_at)=4
 * group by YEAR(created_at), MONTH(created_at)
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-04-13 2:34 PM)
 */
public class HitStatsController implements Controller {
    public void execute(MicroContext context, Map configuration) throws ControllerException {
        @SuppressWarnings(value = "unchecked")
        Map<String, Object> params = (Map<String, Object>) context.get(Globals.PARAMS);
        SiteContext site = context.getSiteContext();
        ActivejdbcExtension aj = (ActivejdbcExtension) site.get("activejdbc_m");
        Assert.notNull(aj, "Please install the ActiveJDBC extension.");

        String url = (String) params.get("url");
        String year = (String) params.get("year");

        url = url == null || url.isEmpty() ? Globals.EMPTY_STRING : url;
        year = year == null || year.isEmpty() ? Globals.EMPTY_STRING : year;

        // String format = (String) params.get("format");
        // format = format == null || format.isEmpty() ? "html" : format;

        try {
            aj.before();

            if (!url.isEmpty() && !year.isEmpty()) {
                calculateHitStats(context, url, year);
            }

        } catch (Exception e) {
            e.printStackTrace();
            aj.onException();
            context.put("error", e.getMessage());
        } finally {
            aj.after();
        }
    }

    public static void calculateHitStats(MicroContext context, String url, String year) throws java.io.IOException {
        Url u = Url.findById(Bijective.decode(url));

        if (u != null) {
            List<Map> hitStats = Base.findAll("SELECT\n" +
                    "MONTH(HITS.created_at) as month, count(HITS.created_at) as total_hitcount\n" +
                    "FROM URLS\n" +
                    "INNER JOIN HITS ON URLS.ID = HITS.URL_ID\n" +
                    "and YEAR(HITS.created_at) = ?\n" +
                    "where URLS.ID = ?\n" +
                    "group by MONTH(HITS.created_at)\n" +
                    "order by MONTH(HITS.created_at) ASC", year, u.getId());

            // todo: render for {format}
            context.put("shortUrl", url);
            context.put("originalUrlCreatedAt", new DateTime(u.getDate("created_at")));
            context.put("url", new String(Base64.decode(u.getString("url")
                    .getBytes(Charset.forName(Globals.UTF8)))));
            context.put("year", year);
            context.put("hitStats", hitStats);
        }
    }
}

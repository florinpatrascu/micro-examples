package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.Globals;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.SiteContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import ca.simplegames.micro.utils.Assert;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import controllers.utils.Bijective;
import models.Hit;
import models.Url;
import org.joda.time.DateTime;
import org.jrack.JRack;
import org.jrack.RackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Main controller responsible for finding and creating short URLs
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-04-12)
 */
public class MicroUrlController implements Controller {
    public static final Logger log = LoggerFactory.getLogger(MicroUrlController.class);

    enum Methods {POST, DELETE, PUT, HEAD, GET}

    public void execute(MicroContext context, Map map) throws ControllerException {
        @SuppressWarnings(value = "unchecked")
        Map<String, Object> params = (Map<String, Object>) context.get(Globals.PARAMS);
        SiteContext site = context.getSiteContext();
        ActivejdbcExtension aj = (ActivejdbcExtension) site.get("activejdbc_m");
        Assert.notNull(aj, "Please install the ActiveJDBC extension.");

        String url = (String) params.get("url");
        url = url == null || url.isEmpty() ? Globals.EMPTY_STRING : url;

        try {
            aj.before();

            switch (Methods.valueOf((String) context.getRackInput().get(JRack.REQUEST_METHOD))) {
                case POST:
                    if (!url.isEmpty()) {
                        List<Pattern> domainRestrictionPatterns = (List<Pattern>) site.get("domainRestrictionPatterns");
                        String error = Globals.EMPTY_STRING;
                        if (domainRestrictionPatterns != null && !domainRestrictionPatterns.isEmpty()) {
                            boolean matches = false;
                            for (Pattern domainRestrictionPattern : domainRestrictionPatterns) {
                                matches = domainRestrictionPattern.matcher(url).matches();
                                if (matches) break;
                            }

                            if (!matches) {
                                error = "Domain restrictions apply, sorry.";
                            }
                        }

                        if (error.isEmpty()) {
                            String hashedurl = Base64.encode(url.getBytes(Charset.forName(Globals.UTF8)));
                            List<Url> urls = Url.where("url=?", hashedurl);
                            Url u = (urls != null && !urls.isEmpty()) ? urls.get(0) : Url.<Url>createIt("url", hashedurl);

                            HitStatsController.calculateHitStats(context,
                                    Bijective.encode(u.getLongId().intValue()),
                                    DateTime.now().toString("yyyy"));

                            context.put("shortUrl", Bijective.encode(u.getLongId().intValue()));
                        } else {
                            context.with("error", error);
                        }
                    }
                    break;

                case DELETE: // delete... really? Should anybody be able to delete a µURL?
                    if (!url.isEmpty()) {
                        Url u = Url.findById(Bijective.decode(url));

                        if (u != null) {
                            u.deleteCascade();
                        }
                    }
                    context.halt();
                    break;

                default: // GET => list
                    if (!url.isEmpty()) {
                        Url u = Url.findById(Bijective.decode(url));

                        if (u != null) {
                            Hit.createIt("url_id", u.getId());

                            int rCode = !"GET".equalsIgnoreCase(context.getRequest().getMethod()) ? 303 : 302;

                            RackResponse response = new RackResponse(rCode)
                                    .withBody(Globals.EMPTY_STRING)
                                    .withContentLength(0);

                            context.with(Globals.RACK_RESPONSE, response
                                    .withHeader("Location", new String(Base64.decode(u.getString("url")
                                            .getBytes(Charset.forName(Globals.UTF8))))));

                            context.halt();
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            aj.onException();
            context.put("error", e.getMessage());
        } finally {
            aj.after();
        }
    }
}

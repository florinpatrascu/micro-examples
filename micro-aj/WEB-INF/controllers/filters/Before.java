package controllers.filters;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.SiteContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * basic BEFORE advise that take care about closing an AJ connection
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-24 12:37 PM)
 */
public class Before implements Controller {
    protected static final Log log = LogFactory.getLog(Before.class);

    public void execute(MicroContext context, Map configuration) throws ControllerException {
        SiteContext site = context.getSiteContext();
        ActivejdbcExtension aj = (ActivejdbcExtension) site.get("activejdbc_m");
        aj.before();
    }
}

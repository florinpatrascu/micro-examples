/*
 * Copyright (c)2013 Florin T.Pătraşcu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.simplegames.micro.extensions;

import ca.simplegames.micro.Extension;
import ca.simplegames.micro.SiteContext;

import java.util.Map;

/**
 * Adding support for the StringTemplate library
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-03 7:43 PM)
 */
public class STExtension implements Extension {
    private String name;

    @SuppressWarnings("unchecked")
    @Override
    public Extension register(String name, SiteContext site, Map<String, Object> configuration) throws Exception {
        // File extensionPath = new File(site.getApplicationConfigPath(), "/extensions/st");
        this.name = name;
        site.getTemplateEnginesManager().addTemplateEngine(site, (Map) configuration);
        return this;
    }

    @Override
    public String getName() {
        return name;
    }
}


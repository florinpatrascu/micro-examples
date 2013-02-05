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

package ca.simplegames.micro.viewers.st;

import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.SiteContext;
import ca.simplegames.micro.repositories.Repository;
import ca.simplegames.micro.utils.CollectionUtils;
import ca.simplegames.micro.utils.IO;
import ca.simplegames.micro.viewers.ViewException;
import ca.simplegames.micro.viewers.ViewRenderer;
import org.stringtemplate.v4.ST;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;

/**
 * Simple StringTemplate view renderer.
 * <p/>
 * If you learn more about the StringTemplate, visit their site:
 * - http://www.antlr.org/wiki/display/ST4/StringTemplate+4+Wiki+Home
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-02-03 7:42 PM)
 */
public class STViewRenderer implements ViewRenderer {
    public static final String NAME = "st";

    @Override
    public long render(String path, Repository repository, MicroContext context, Writer out) throws FileNotFoundException, ViewException {

        if (repository != null && out != null) {
            try {

                String source = repository.read(path);
                ST st = new ST(source);
                //todo: Think! Should I use: st.write( ...)??
                if (!CollectionUtils.isEmpty(context.getMap())) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) context.getMap()).entrySet()) {
                        if (!entry.getKey().equalsIgnoreCase("rack.logger")) {
                            st.add(entry.getKey(), entry.getValue());
                        }
                    }
                }
                return IO.copy(new StringReader(st.render()), out);

            } catch (FileNotFoundException e) {
                throw new FileNotFoundException(String.format("%s not found.", path));
            } catch (Exception e) {
                throw new ViewException(e);
            }
        }
        return 0;
    }

    @Override
    public void loadConfiguration(SiteContext site, Map<String, Object> configuration) throws Exception {
    }

    @Override
    public String getName() {
        return NAME;
    }
}

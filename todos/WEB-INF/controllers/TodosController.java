package controllers;

import ca.simplegames.micro.Controller;
import ca.simplegames.micro.Globals;
import ca.simplegames.micro.MicroContext;
import ca.simplegames.micro.SiteContext;
import ca.simplegames.micro.controllers.ControllerException;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import ca.simplegames.micro.utils.Assert;
import ca.simplegames.micro.utils.IO;
import ca.simplegames.micro.utils.StringUtils;
import models.Project;
import models.Todo;
import org.javalite.activejdbc.LazyList;
import org.jrack.JRack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An all-in-one simple Controller to assist with the CRUD for the checklists CRUD, including the
 * Checklist name creation, etc.
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since 2013-03-28
 */
public class TodosController implements Controller {
    public static final Logger log = LoggerFactory.getLogger(TodosController.class);

    enum Methods {POST, DELETE, PUT, HEAD, GET}

    public void execute(MicroContext context, Map map) throws ControllerException {

        @SuppressWarnings(value = "unchecked")
        Map<String, Object> params = (Map<String, Object>) context.get(Globals.PARAMS);
        HttpServletRequest request = context.getRequest();
        SiteContext site = context.getSiteContext();
        String checklistName = (String) params.get("checklistName");
        Project project = null;
        Todo todo;

        if (checklistName != null) {
            ActivejdbcExtension aj = (ActivejdbcExtension) site.get("activejdbc_m");
            Assert.notNull(aj, "Please install the ActiveJDBC extension.");

            try {
                aj.before();

                context.put("checklistName", checklistName);
                context.put("template", "todos");
                List<Project> projects = Project.where("name = ?", checklistName.toLowerCase());

                if (projects != null && !projects.isEmpty()) {
                    project = projects.get(0);
                }

                switch (Methods.valueOf((String) context.getRackInput().get(JRack.REQUEST_METHOD))) {
                    case POST: // find/create list, or save a new Todo record
                        try {
                            final String requestBody = IO.getString(request.getInputStream());
                            String jsonString = Globals.EMPTY_STRING;

                            if (StringUtils.hasLength(requestBody)) {

                                if (projects == null || projects.isEmpty()) {
                                    project = Project.create(
                                            "name", checklistName);
                                    if (!project.saveIt()) {
                                        throw new Exception("cannot save this list");
                                    }
                                }

                                if (project != null) {
                                    JSONObject jsonTodo = new JSONObject(requestBody);
                                    todo = Todo.createIt("title", jsonTodo.get("title"),
                                            "ord", jsonTodo.getInt("order"),
                                            "project_id", project.getId()
                                    );

                                    jsonString = todo.toJson();
                                }
                                context.getRackResponse()
                                        .withBody(jsonString)
                                        .withContentLength(jsonString.length());

                                context.halt();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case DELETE: // delete
                        todo = Todo.findById(params.get("id"));
                        if (project != null && todo != null &&
                                ((Integer)todo.get("project_id")).intValue() == (Integer)project.getId()) { //safety first
                            todo.delete();
                        }
                        context.halt();
                        break;

                    case PUT:    //edit
                        try {
                            final String requestBody = IO.getString(request.getInputStream());

                            if (StringUtils.hasLength(requestBody) && project != null) {
                                JSONObject jsonTodo = new JSONObject(requestBody);
                                List<Todo> todos = Todo.where("id = ? and project_id = ?",
                                        jsonTodo.getInt("id"), project.getId()).limit(1);

                                if (todos != null && !todos.isEmpty()) {
                                    todo = todos.get(0);
                                    todo.set("title", jsonTodo.get("title"),
                                            "ord", jsonTodo.getInt("order"),
                                            "modified_at", new Date(),
                                            "done", jsonTodo.getBoolean("done"))
                                            .saveIt();

                                    String jsonString = todo.toJson();
                                    context.getRackResponse()
                                            .withBody(jsonString).withContentLength(jsonString.length());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        context.halt();
                        break;

                    default: // GET => list
                        if (project != null) {
                            final LazyList<Todo> todos = project.getAll(Todo.class).<Todo>orderBy("ord ASC");
                            JSONArray json = todosJson(todos);

                            if (request.getHeader("X-Requested-With") != null
                                    && request.getHeader("X-Requested-With").contains("XMLHttpRequest")) {

                                if (json != null) {
                                    String r = json.toString();
                                    context.getRackResponse().withBody(r).withContentLength(r.length());
                                }
                                context.halt();
                            }
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
                aj.onException();
                throw new ControllerException(e.getMessage(), e);
            } finally {
                aj.after();
            }
        }
    }

    /**
     * convert a list with Todo models to a JSONArray
     * This is basically a poor man solution, since I am using the ancient json.org lib
     *
     * @param todos a list containing out todos
     * @return a {@link JSONArray} object
     * @throws JSONException if we can't create the JSON
     */
    public JSONArray todosJson(List<Todo> todos) throws JSONException {
        JSONArray json = null;

        if (todos != null && !todos.isEmpty()) {
            StringBuilder sb = new StringBuilder().append("[");

            for (Todo td : todos) {
                sb.append(td.toJson()).append(",");
            }

            final String source = sb.toString().substring(0, sb.length() - 1) + "]";
            // and now, because this lib is silly :(, I have to create a top key because JSONObject will
            // refuse a perfectly valid JSON String otherwise.
            json = (JSONArray) new JSONObject("{\"todos\": " + source + "}").get("todos");
        }
        return json;
    }
}

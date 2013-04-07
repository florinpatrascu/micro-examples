import ca.simplegames.micro.Globals;
import ca.simplegames.micro.Micro;
import ca.simplegames.micro.extensions.ActivejdbcExtension;
import junit.framework.Assert;
import models.Project;
import models.Todo;
import org.jrack.Context;
import org.jrack.Rack;
import org.jrack.RackResponse;
import org.jrack.context.MapContext;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-03-30 11:08 AM)
 */
public class TestApp {
    public static Micro micro;
    protected static final org.slf4j.Logger log = LoggerFactory.getLogger(TestApp.class);
    final String A_TEST_CHECKLIST = "test";

    @BeforeClass
    public static void setup() throws Exception {
        micro = new Micro("../../", null, "/lib");

        Assert.assertNotNull(micro);
        Assert.assertNotNull("Micro 'site' initialization failed", micro.getSite().getWebInfPath());
        Assert.assertTrue("Micro is not pointing to the correct test web app",
                micro.getSite().getWebInfPath().getAbsolutePath().contains("WEB-INF"));
        Assert.assertTrue("Micro test web app is not properly defined",
                micro.getSite().getWebInfPath().exists());
    }

    @Test
    public void clearDB() throws Exception {
        ActivejdbcExtension aj = (ActivejdbcExtension) micro.getSite().get("activejdbc_m");
        Assert.assertNotNull("ActiveJDBC not installed", aj);
        aj.before();

        log.info("cleaning the db: Todos");
        Todo.deleteAll();
        List models = Todo.findAll();
        Assert.assertTrue("Database must not contain any Project records", models.isEmpty());

        log.info("cleaning the db: Projects");
        Project.deleteAll();
        models = Project.findAll();
        Assert.assertTrue("Database must not contain any Todos records", models.isEmpty());

        aj.after();
        log.info("db clear.");
    }

    @Test
    public void newRecordCreation() throws Exception {
        Context<String> input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "POST")
                .with(Rack.PATH_INFO, "/" + A_TEST_CHECKLIST)
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST,
                        "{\"title\":\"buy the milk\",\"order\":1,\"done\":false}"));

        String jsonString = RackResponse.getBodyAsString(micro.call(input));
        Assert.assertTrue("The todo wasn't saved", jsonString != null && jsonString.contains("id\":"));

        JSONObject newTodo = new JSONObject(jsonString);
        Assert.assertTrue("The todo is not a valid JSON string", newTodo.get("id") != null);

        input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "GET")
                .with(Rack.PATH_INFO, String.format("/%s/todos%s", A_TEST_CHECKLIST, newTodo.get("id")))
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST));

        jsonString = RackResponse.getBodyAsString(micro.call(input));
        Assert.assertTrue("The saved todo cannot be retrieved from the db", jsonString != null);

        JSONObject savedTodo = new JSONObject("{\"data\": " + jsonString + "}")
                .getJSONArray("data").getJSONObject(0);
        Iterator<?> keys = savedTodo.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Assert.assertTrue(String.format("The saved Todo has a different value for: %s", key),
                    savedTodo.get(key).equals(newTodo.get(key)));
        }
    }

    @Test
    public void testTodoCompleted() throws Exception {
        JSONObject todo = oneTodo();
        int id = todo.getInt("id");

        Assert.assertTrue("invalid id: " + id, id > 0);
        log.info("this todo must not be done"+ todo);
        Assert.assertFalse("this todo must not be done", todo.getBoolean("done"));

        final String requestPath = "/" + A_TEST_CHECKLIST + "/todos/" + id;
        todo.put("done", true);
        Assert.assertTrue("this todo must was supposed to be done", todo.getBoolean("done"));

        Context<String> input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "PUT")
                .with(Rack.PATH_INFO, requestPath)
                .with(Rack.REQUEST, mockRequest(requestPath, todo.toString()));

        micro.call(input);

        input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "GET")
                .with(Rack.PATH_INFO, "/" + A_TEST_CHECKLIST + "/todos/" + id)
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST));

        final RackResponse response = micro.call(input);
        Assert.assertTrue("Can't update the todo with id: " + id,
                response.getBody() != null && response.getStatus() == HttpServletResponse.SC_OK);

        String jsonString = RackResponse.getBodyAsString(micro.call(input));
        Assert.assertNotNull(String.format("Can't get any Todo from this list: %s", A_TEST_CHECKLIST),
                jsonString);

        todo = new JSONObject("{\"data\": " + jsonString + "}")
                .getJSONArray("data").getJSONObject(0);

        Assert.assertTrue("this todo must be done", todo.getBoolean("done"));
    }

    @Test
    public void testDeleteTodo() throws Exception {
        JSONObject todo = oneTodo();
        int id = todo.getInt("id");
        log.info("got a Todo: " + todo.toString());

        Assert.assertTrue("invalid id: " + id, id > 0);

        Context<String> input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "DELETE")
                .with(Rack.PATH_INFO, "/" + A_TEST_CHECKLIST + "/todos/" + id)
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST));

        micro.call(input);

        input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "GET")
                .with(Rack.PATH_INFO, "/" + A_TEST_CHECKLIST + "/todos/" + id)
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST));

        final RackResponse response = micro.call(input);
        Assert.assertTrue("Can't delete the todo with id: " + id,
                response.getBody() == null && response.getStatus() == HttpServletResponse.SC_OK);
    }

    private JSONObject oneTodo() throws Exception {
        Context<String> input = new MapContext<String>()
                .with(Rack.REQUEST_METHOD, "GET")
                .with(Rack.PATH_INFO, "/" + A_TEST_CHECKLIST)
                .with(Rack.REQUEST, mockRequest(A_TEST_CHECKLIST));

        String jsonString = RackResponse.getBodyAsString(micro.call(input));
        Assert.assertNotNull(String.format("Can't get any Todo from this list: %s", A_TEST_CHECKLIST),
                jsonString);

        return new JSONObject("{\"data\": " + jsonString + "}")
                .getJSONArray("data").getJSONObject(0);
    }

    /**
     * mocking a basic HttpServletRequest that will be used to hold the A_TEST_CHECKLIST and an optional
     * payload containing a todo
     *
     * @param checklistName the name of the checklist
     * @param payload       optional parameter containing a todo json
     * @return
     * @throws Exception
     */
    private HttpServletRequest mockRequest(String checklistName, String... payload) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getParameter("A_TEST_CHECKLIST")).willReturn(checklistName);
        given(request.getParameterNames()).willReturn(Collections.enumeration(Arrays.asList(checklistName)));
        given(request.getAttributeNames()).willReturn(Collections.enumeration(Collections.EMPTY_LIST));
        given(request.getSession(false)).willReturn(null);
        given(request.getHeader("X-Requested-With")).willReturn("XMLHttpRequest");

        // doesn't stub :( InputStream is = (InputStream) new ByteArrayInputStream(Globals.EMPTY_STRING.getBytes());
        if (payload != null && payload.length > 0) {
            given(request.getInputStream()).willReturn(
                    createServletInputStream(payload[0]));
        }

        return request;
    }

    private static ServletInputStream createServletInputStream(String string) throws Exception {
        final InputStream bais = new ByteArrayInputStream(string.getBytes(Globals.UTF8));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


}

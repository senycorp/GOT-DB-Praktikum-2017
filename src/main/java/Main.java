import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

/**
 * Got WebApp
 *
 * @author Selcuk Kekec <senycorp@googlemail.com>
 */
public class Main {

    /**
     * Template Engine Instance
     */
    protected static FreeMarkerEngine templateEngine = null;

    /**
     * Database Connection Instance
     */
    protected static Connection databaseConnection = null;

    /**
     * User Instance
     */
    protected static Map<String, String> user = null;

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Initialize
         */
        Main.initialize();

        /**
         * Start
         */
        get("/", (req, res) -> {
            Map<String, Object> attributes = getViewMap();
            attributes.put("message", "Hello World!");

            return render();
        }, getTemplateEngine());

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = getViewMap();
            attributes.put("message", "Hello World!");

            return render(new ModelAndView(attributes, "hello.ftl"));
        }, getTemplateEngine());

        /**
         * GZIP all requests
         */
        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }

    protected static Map<String, String> getUser() {
        if (Main.user == null) {
            try {
                ResultSet resultSet = Main.query("SELECT * FROM benutzer WHERE id = 1;");
                resultSet.next();
                Main.user = new HashMap<>();
                Main.user.put("id", resultSet.getString("id"));
                Main.user.put("name", resultSet.getString("name"));
                Main.user.put("username", resultSet.getString("login_kennung"));
                Main.user.put("password", resultSet.getString("passwort"));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Main.user;
    }

    /**
     * Initialize app
     */
    protected static void initialize() {
        /**
         * Static File Configuration
         */
        Spark.externalStaticFileLocation("/home/selcuk/Development/Database/src/main/resources/public");

        /**
         * Initialize Database Connection
         */
        getDatabaseConnection();
    }

    /**
     * Render Template
     *
     * This method will render a child template into
     * the main structure template with all dependencies
     *
     * @param modelAndView
     * @return
     */
    protected static ModelAndView render(ModelAndView modelAndView) {
        Map<String, Object> attributes = getViewMap();
        attributes.put("content", getTemplateEngine().render(modelAndView));
        attributes.put("userData", Main.getUser());

        return new ModelAndView(attributes, "index.ftl");
    }

    /**
     * Render main template with blank content
     *
     * @return
     */
    protected static ModelAndView render() {
        Map<String, Object> attributes = getViewMap();
        attributes.put("content", "");
        attributes.put("userData", Main.getUser());

        return new ModelAndView(attributes, "index.ftl");
    }

    /**
     * Get a database connection
     *
     * @return
     */
    protected static Connection getDatabaseConnection() {
        if (Main.databaseConnection == null) {
            try {
                // Load Driver
                Class.forName("com.mysql.jdbc.Driver");

                // Create Connection
                Main.databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/game_of_thrones?user=root&password=MKataturk89");
            } catch (ClassNotFoundException e) {
                System.out.println("Unable to find driver. Please check the dependencies.");
            } catch (SQLException e) {
                System.out.println("Unable to establish a connection to server: ");
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }

        return Main.databaseConnection;
    }

    /**
     * Fire query to DBMS
     *
     * @param queryString
     * @return
     * @throws SQLException
     */
    protected static ResultSet query(String queryString) throws SQLException {
        return getDatabaseConnection().createStatement().executeQuery(queryString);
    }

    /**
     * Get View Map
     *
     * @return
     */
    protected static Map<String, Object> getViewMap() {
        return new HashMap<String, Object>();
    }

    /**
     * Get Template Engine Instance
     *
     * @return
     */
    protected static FreeMarkerEngine getTemplateEngine() {
        if (Main.templateEngine == null) {
            /**
             * FreeMarker Initialization
             */
            FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
            Configuration freeMarkerConfiguration = new Configuration();
            freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/"));
            freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

            Main.templateEngine = freeMarkerEngine;
        }

        return Main.templateEngine;
    }
}

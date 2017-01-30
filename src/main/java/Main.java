import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    /**
     * Get user
     *
     * @return
     */
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
        attributes.put("figures", getFigures());
        attributes.put("haueser", getHaeuser());
        attributes.put("seasons", getSeasons());
        attributes.put("playlists", getPlaylists());

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

    /**
     * Get Figures
     *
     * @return
     */
    protected static ArrayList<HashMap> getFigures() {
        ArrayList<HashMap> results = new ArrayList();
        try {
            ResultSet resultSet = query(
                    "SELECT *, person.id AS id, ort.name AS heimat FROM person " +
                            "INNER JOIN figur  ON   figur.id        =  person.id " +
                            "LEFT  JOIN ort    ON   figur.heimat_id =  ort.id " +
                            "WHERE TRUE LIMIT 5;"
            );

            while (resultSet.next()) {
                HashMap<String, String> person = new HashMap<>();

                person.put("id", resultSet.getString("id"));
                person.put("typ", "person");
                person.put("name", resultSet.getString("name"));
                person.put("heimat", resultSet.getString("heimat"));
                person.put("titel", resultSet.getString("titel"));
                person.put("biografie", resultSet.getString("biografie"));

                results.add(person);
            }

            resultSet = query(
                    "SELECT tier.id as id, " +
                            "besitzer.name AS besitzerName, " +
                            "person.titel AS besitzerTitel, " +
                            "tierFigur.name AS tierName,  " +
                            "ort.name AS heimat " +
                            "FROM tier " +
                            "INNER JOIN figur   tierFigur           ON   tierFigur.id                   =  tier.id " +
                            "LEFT  JOIN ort                         ON   tierFigur.heimat_id            =  ort.id " +
                            "LEFT  JOIN  person                     ON   person.id                      =  tier.besitzer_id " +
                            "LEFT  JOIN  figur besitzer             ON   person.id                      =  besitzer.id " +
                            "WHERE TRUE LIMIT 5;"
            );

            while (resultSet.next()) {
                HashMap<String, String> tier = new HashMap<>();

                tier.put("id", resultSet.getString("id"));
                tier.put("typ", "tier");
                tier.put("name", resultSet.getString("tierName"));
                tier.put("besitzer", resultSet.getString("besitzerName"));
                tier.put("besitzerTitel", resultSet.getString("besitzerTitel"));
                tier.put("heimat", resultSet.getString("heimat"));

                results.add(tier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Get Häuser
     *
     * @return
     */
    protected static ArrayList<HashMap> getHaeuser() {
        ArrayList<HashMap> results = new ArrayList<>();


        try {
            ResultSet resultSet = query(
                    "SELECT *, haus.id AS id, haus.name AS name, burg.name AS burgName, ort.name AS standortName FROM haus " +
                            "INNER JOIN burg ON haus.sitz_id = burg.id " +
                            "INNER JOIN ort ON burg.standort_id = ort.id " +
                            "WHERE TRUE"
            );

            while (resultSet.next()) {
                HashMap<String, String> haus = new HashMap<>();

                haus.put("id", resultSet.getString("id"));
                haus.put("name", resultSet.getString("name"));
                haus.put("motto", resultSet.getString("motto"));
                haus.put("wappen", resultSet.getString("wappen"));
                haus.put("burg", resultSet.getString("burgName"));
                haus.put("standort", resultSet.getString("standortName"));

                results.add(haus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Get Seasons
     *
     * @return
     */
    protected static ArrayList<HashMap> getSeasons() {
        ArrayList<HashMap> results = new ArrayList<>();

        try {
            ResultSet resultSet = query(
                    "SELECT * FROM staffel "+

                    "WHERE TRUE"
            );

            while (resultSet.next()) {
                HashMap<String, String> season = new HashMap<>();

                season.put("id", resultSet.getString("id"));
                season.put("nummer", resultSet.getString("nummer"));
                season.put("startdatum", resultSet.getString("startdatum"));
                season.put("episodenanzahl", resultSet.getString("episodenanzahl"));

                results.add(season);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Get Playlists of user
     *
     * @return
     */
    protected static ArrayList<HashMap> getPlaylists() {
        ArrayList<HashMap> results = new ArrayList<>();

        try {
            ResultSet resultSet = query(
                    "SELECT * FROM (SELECT playlist.id, count(playlist.id) AS episodenAnzahl FROM playlist \n" +
                            "LEFT JOIN episode_playlist ON episode_playlist.playlist_id = playlist.id \n" +
                            "WHERE besitzer_id = " + getUser().get("id") + " " +
                            "GROUP BY playlist.id) AS grp"
            );

            while (resultSet.next()) {
                HashMap<String, String> playlist = new HashMap<>();

                playlist.put("id", resultSet.getString("id"));
                playlist.put("episodenAnzahl", resultSet.getString("episodenAnzahl"));

                results.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}

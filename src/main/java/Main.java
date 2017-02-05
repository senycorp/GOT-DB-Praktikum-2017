
/**
 * IMPORTS
 */
import Exceptions.NotFoundException;
import Exceptions.TypeNotFoundException;
import org.apache.commons.dbutils.DbUtils;
import spark.ModelAndView;
import spark.Spark;
import java.util.Map;
import static spark.Spark.*;

/**
 * Got WebApp
 *
 * @author Selcuk Kekec <senycorp@googlemail.com>
 */
public class Main {

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
            return TemplateEngine.render();
        }, TemplateEngine.getTemplateEngine());

        /**
         * Person Detail View
         */
        get("/person/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("person", DAO.getPerson(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Person Detail View");
                return TemplateEngine.renderDetail(new ModelAndView(attributes, "person.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Animal Detail View
         */
        get("/animal/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("animal", DAO.getAnimal(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Animal Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "animal.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Haus Detail View
         */
        get("/haus/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("haus", DAO.getHaus(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Haus Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "haus.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Location Detail View
         */
        get("/location/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("location", DAO.getLocation(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Location Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "location.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Episode Detail View
         */
        get("/episode/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("episode", DAO.getEpisode(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Episode Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "episode.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Season Detail View
         */
        get("/season/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("season", DAO.getSeason(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Season Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "season.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Playlist Detail View
         */
        get("/playlist/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                attributes.put("playlist", DAO.getPlaylist(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Playlist Detail View");

                return TemplateEngine.renderDetail(new ModelAndView(attributes, "playlist.ftl"), attributes);
            } catch (NotFoundException e) {
                return TemplateEngine.renderNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Search for artifacts
         */
        get("/search/:type/:keyword", (req, res) -> {
            try {
                Map<String, Object> attributes = TemplateEngine.getViewMap();
                String keyword = req.params(":keyword");

                if (keyword.equalsIgnoreCase("all")) keyword = "";

                attributes.put("keyword", keyword);
                attributes.put("type", req.params(":type").toUpperCase());
                String artifactType = req.params(":type");
                String resultTemplate = "";

                if (artifactType.equalsIgnoreCase("figures")) {
                    attributes.put("title", "Searching for figures");
                    attributes.put("rows", DAO.searchFigures(keyword));
                    resultTemplate = "figureResults.ftl";
                } else if (artifactType.equalsIgnoreCase("haus")) {
                    attributes.put("title", "Searching for Haus");
                    attributes.put("haueser", DAO.searchHaeuser(keyword));
                    resultTemplate = "hausResults.ftl";
                } else if (artifactType.equalsIgnoreCase("seasons")) {
                    attributes.put("title", "Searching for Seasons");
                    attributes.put("seasons", DAO.searchSeasons(keyword));
                    resultTemplate = "seasonResults.ftl";
                } else {
                    throw new TypeNotFoundException();
                }

                return TemplateEngine.renderResults(new ModelAndView(attributes, resultTemplate), attributes);
            } catch (TypeNotFoundException e) {
                return TemplateEngine.renderTypeNotFound(TemplateEngine.getViewMap());
            }
        }, TemplateEngine.getTemplateEngine());

        /**
         * Create Rating
         */
        get("/rate/:id", (req, res) -> {
            DAO.saveRating(Integer.parseInt(req.params(":id")), Integer.parseInt(req.queryMap().get("rating").value()), req.queryMap().get("text").value());
            return "OK";
        });

        /**
         * Create Playlist
         */
        get("/createPlaylist", (req, res) -> {
            DAO.createPlaylist(req.queryMap().get("title").value());

            return "OK";
        });

        /**
         * After request proceeded
         */
        after((request, response) -> {
            // Close Database Connection
            DbUtils.closeQuietly(DAO.getDatabaseConnection());

            // GZIP request
            response.header("Content-Encoding", "gzip");
        });
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
        DAO.getDatabaseConnection();
    }
}

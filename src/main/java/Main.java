import Exceptions.NotFoundException;
import Exceptions.TypeNotFoundException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.dbutils.DbUtils;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * @todo FIX DATABASE CONNECTION STATUS
 */

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

        /**
         * Person Detail View
         */
        get("/person/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("person", DAO.getPerson(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Person Detail View");
                return renderDetail(new ModelAndView(attributes, "person.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Animal Detail View
         */
        get("/animal/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("animal", DAO.getAnimal(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Animal Detail View");

                return renderDetail(new ModelAndView(attributes, "animal.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Haus Detail View
         */
        get("/haus/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("haus", DAO.getHaus(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Haus Detail View");

                return renderDetail(new ModelAndView(attributes, "haus.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Location Detail View
         */
        get("/location/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("location", DAO.getLocation(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Location Detail View");

                return renderDetail(new ModelAndView(attributes, "location.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Episode Detail View
         */
        get("/episode/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("episode", DAO.getEpisode(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Episode Detail View");

                return renderDetail(new ModelAndView(attributes, "episode.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Season Detail View
         */
        get("/season/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("season", DAO.getSeason(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Season Detail View");

                return renderDetail(new ModelAndView(attributes, "season.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Playlist Detail View
         */
        get("/playlist/:id", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
                attributes.put("playlist", DAO.getPlaylist(Integer.parseInt(req.params(":id"))));
                attributes.put("title", "Playlist Detail View");

                return renderDetail(new ModelAndView(attributes, "playlist.ftl"), attributes);
            } catch (NotFoundException e) {
                return renderNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Search for artifacts
         */
        get("/search/:type/:keyword", (req, res) -> {
            try {
                Map<String, Object> attributes = getViewMap();
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

                return renderResults(new ModelAndView(attributes, resultTemplate), attributes);
            } catch (TypeNotFoundException e) {
                return renderTypeNotFound(getViewMap());
            }
        }, getTemplateEngine());

        /**
         * Create Rating
         */
        get("/rate/:id", (req, res) -> {
            DAO.saveRating(Integer.parseInt(req.params(":id")), Integer.parseInt(req.queryMap().get("rating").value()), req.queryMap().get("text").value());
            return "OK";
        });

        /**
         * Create Rating
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

    /**
     * Render main template with blank content
     *
     * @return
     */
    protected static ModelAndView render() {
        Map<String, Object> attributes = getViewMap();
        attributes.put("content", "");
        attributes.put("userData", DAO.getUser());
        attributes.put("figures", DAO.getFigures());
        attributes.put("haueser", DAO.getHaeuser());
        attributes.put("seasons", DAO.getSeasons());
        attributes.put("playlists", DAO.getPlaylists());

        return new ModelAndView(attributes, "index.ftl");
    }

    /**
     * Render main detail template
     *
     * @param modelAndView
     * @param additionalAttributes
     * @return
     */
    protected static ModelAndView renderDetail(ModelAndView modelAndView, Map<String, Object> additionalAttributes) {
        Map<String, Object> attributes = getViewMap();

        attributes.putAll(additionalAttributes);
        attributes.put("content", getTemplateEngine().render(modelAndView));
        attributes.put("userData", DAO.getUser());

        return new ModelAndView(attributes, "detail.ftl");
    }

    /**
     * Render main results template
     *
     * @param modelAndView
     * @param additionalAttributes
     * @return
     */
    protected static ModelAndView renderResults(ModelAndView modelAndView, Map<String, Object> additionalAttributes) {
        Map<String, Object> attributes = getViewMap();

        attributes.putAll(additionalAttributes);
        attributes.put("content", getTemplateEngine().render(modelAndView));
        attributes.put("userData", DAO.getUser());

        return new ModelAndView(attributes, "results.ftl");
    }

    /**
     * Render NotFound template
     *
     * @param attributes
     * @return
     */
    protected static ModelAndView renderNotFound(Map<String, Object> attributes) {
        attributes.put("userData", DAO.getUser());
        return new ModelAndView(attributes, "notFound.ftl");
    }

    /**
     * Render TypeNotFound template
     *
     * @param attributes
     * @return
     */
    protected static ModelAndView renderTypeNotFound(Map<String, Object> attributes) {
        attributes.put("userData", DAO.getUser());
        return new ModelAndView(attributes, "typeNotFound.ftl");
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

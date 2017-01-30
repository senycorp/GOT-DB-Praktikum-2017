import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
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
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Static File Configuration
         */
        Spark.externalStaticFileLocation("/home/selcuk/Development/Database/src/main/resources/public");

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
        return new ModelAndView(attributes, "index.ftl");
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
